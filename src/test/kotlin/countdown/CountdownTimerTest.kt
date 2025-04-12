class CountdownTimerTest {

    private lateinit var server: Server
    private lateinit var countdown: CountdownTimer
    private lateinit var alerts: CountdownAlertMapper<String>

    @BeforeEach
    fun setUp() {
        server = mock()

        countdown = CountdownTimer(server)

        val tenSecond = Duration.ofSeconds(10)
        val fiveSecond = Duration.ofSeconds(5)
        alerts = CountdownAlertMapper.fromDurations(Map.of(
            tenSecond to { id -> server.broadcastMessage("> 10 seconds to start: $id") },
            fiveSecond to { id -> server.broadcastMessage("> 5 seconds to start: $id") }
        ))
    }

    @Test
    fun `test startPhasedCountdown sends broadcast messages`() {
        val prepare = CountdownPhase(Duration.ofSeconds(15), alerts.toCountdownTicker())
        val active = CountdownPhase(Duration.ofSeconds(10)) { id, sec, _ ->
            server.broadcastMessage("[$id] Game ongoing... Seconds left: $sec")
        }

        countdown.startPhasedCountdown(
            "countdown-id",
            listOf(prepare, active),
            { id, end -> server.broadcastMessage("> Countdown finished for $id") },
            { id -> server.broadcastMessage("> Countdown cancelled for: $id") }
        )

        verify(server).broadcastMessage("> 10 seconds to start: countdown-id")
        verify(server).broadcastMessage("> 5 seconds to start: countdown-id")
        verify(server).broadcastMessage("[countdown-id] Game ongoing... Seconds left: 10")
        verify(server).broadcastMessage("> Countdown finished for countdown-id")
    }

    @Test
    fun `test countdown cancel triggers cancel message`() {
        val prepare = CountdownPhase(Duration.ofSeconds(15), alerts.toCountdownTicker())
        val active = CountdownPhase(Duration.ofSeconds(10)) { id, sec, _ ->
            server.broadcastMessage("[$id] Game ongoing... Seconds left: $sec")
        }

        countdown.startPhasedCountdown(
            "countdown-id",
            listOf(prepare, active),
            { id, end -> server.broadcastMessage("> Countdown finished for $id") },
            { id -> server.broadcastMessage("> Countdown cancelled for: $id") }
        )

        countdown.cancel("countdown-id")
        verify(server).broadcastMessage("> Countdown cancelled for: countdown-id")
    }
}
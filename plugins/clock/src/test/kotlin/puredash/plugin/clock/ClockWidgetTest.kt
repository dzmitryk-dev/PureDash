package puredash.plugin.clock

import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import kotlin.test.assertEquals

class ClockWidgetTest {
    @Test
    fun renderReturnsFormattedTimeFromProvider() {
        val mockDateTime = LocalDateTime.of(2025, 11, 5, 17, 58, 36)
        val widget = ClockWidget { mockDateTime }
        val result = widget.render()
        
        assertEquals("17:58:36", result)
    }
}

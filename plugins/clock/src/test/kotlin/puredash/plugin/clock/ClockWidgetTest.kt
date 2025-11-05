package puredash.plugin.clock

import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

class ClockWidgetTest {
    @Test
    fun renderReturnsTimeInHHmmssFormat() {
        val widget = ClockWidget()
        val result = widget.render()
        
        val pattern = Regex("""\d{2}:\d{2}:\d{2}""")
        assertTrue(pattern.containsMatchIn(result), "Expected time format HH:mm:ss, got: $result")
    }
}

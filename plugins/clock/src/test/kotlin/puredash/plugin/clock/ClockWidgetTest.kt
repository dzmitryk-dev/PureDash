package puredash.plugin.clock

import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import kotlin.test.assertEquals

class ClockWidgetTest {
    @Test
    fun renderReturnsExactClockHtmlWithScript() {
        val mockDateTime = LocalDateTime.of(2025, 11, 5, 17, 58, 36)
        val widget = ClockWidget { mockDateTime }
        val result = widget.render()
        
        val expected = """<div id="clock">17:58:36</div><script>
setInterval(function() {
  var now = new Date();
  var h = now.getHours();
  var m = now.getMinutes();
  var s = now.getSeconds();
  var time = (h < 10 ? '0' : '') + h + ':' + (m < 10 ? '0' : '') + m + ':' + (s < 10 ? '0' : '') + s;
  document.getElementById('clock').textContent = time;
}, 1000);
</script>"""
        
        assertEquals(expected, result)
    }
}

package puredash.plugin.clock

import puredash.plugin.Widget
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ClockWidget(
    private val localDateTimeProvider: () -> LocalDateTime = { LocalDateTime.now() }
) : Widget {
    override fun render(): String {
        val now = localDateTimeProvider()
        val formatter = DateTimeFormatter.ofPattern("HH:mm:ss")
        val timeString = now.format(formatter)
        
        return """<div id="clock">$timeString</div><script>
setInterval(function() {
  var now = new Date();
  var h = now.getHours();
  var m = now.getMinutes();
  var s = now.getSeconds();
  var time = (h < 10 ? '0' : '') + h + ':' + (m < 10 ? '0' : '') + m + ':' + (s < 10 ? '0' : '') + s;
  document.getElementById('clock').textContent = time;
}, 1000);
</script>"""
    }
}

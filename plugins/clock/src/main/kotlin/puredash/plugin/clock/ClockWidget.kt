package puredash.plugin.clock

import puredash.plugin.Widget
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ClockWidget() : Widget {
    override fun render(): String {
        val now = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("HH:mm:ss")
        val timeString = now.format(formatter)
        return timeString
    }
}

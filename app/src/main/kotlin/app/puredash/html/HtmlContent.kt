package app.puredash.html

import puredash.plugin.Widget
import kotlinx.html.*
import kotlinx.html.stream.createHTML

fun getHtmlContent(
    widgets: List<Widget>,
): String = createHTML().html {
    lang = "en"
    head {
        meta(charset = "UTF-8")
        meta(name = "viewport", content = "width=device-width, initial-scale=1.0")
        title("PureDash")
        style {
            unsafe {
                raw("""
                    body {
                        font-family: Arial, sans-serif;
                        margin: 40px;
                        background-color: #f4f4f4;
                    }
                    h1 {
                        color: #333;
                    }
                    .widget {
                        background-color: white;
                        padding: 20px;
                        margin: 20px 0;
                        border-radius: 5px;
                        box-shadow: 0 2px 4px rgba(0,0,0,0.1);
                    }
                    .widget-content {
                        font-size: 1.1em;
                        color: #333;
                    }
                """.trimIndent())
            }
        }
    }
    body {
        h1 { +"Welcome to PureDash" }
        
        for (widget in widgets) {
            div(classes = "widget") {
                div(classes = "widget-content") {
                    unsafe { raw(widget.render()) }
                }
            }
        }
    }
}
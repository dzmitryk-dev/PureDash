package app.puredash.html

import kotlinx.html.*
import kotlinx.html.stream.createHTML

fun getHtmlContent(): String = createHTML().html {
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
                    p {
                        font-size: 1.2em;
                        color: #666;
                    }
                """.trimIndent())
            }
        }
    }
    body {
        h1 { +"Welcome to PureDash" }
        p { +"Your lightweight dashboard server is running successfully!" }
    }
}
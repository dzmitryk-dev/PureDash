package app.puredash.html

fun getHtmlContent(): String = """
    <!DOCTYPE html>
    <html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>PureDash</title>
        <style>
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
        </style>
    </head>
    <body>
        <h1>Welcome to PureDash</h1>
        <p>Your lightweight dashboard server is running successfully!</p>
    </body>
    </html>
""".trimIndent()
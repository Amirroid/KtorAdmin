package utils

import configuration.DynamicConfiguration
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*

suspend fun ApplicationCall.badRequest(message: String, throwable: Throwable? = null) {
    respondText(status = HttpStatusCode.BadRequest, contentType = ContentType.Text.Html) {
        generateErrorHtml(
            "400 - Bad Request",
            if (DynamicConfiguration.debugMode) message else "",
            throwable?.stackTraceToString()
        )
    }
}


suspend fun ApplicationCall.tooManyRequests() {
    response.headers.append("Retry-After", "60")
    respondText(status = HttpStatusCode.TooManyRequests, contentType = ContentType.Text.Html) {
        generateErrorHtml("429 - Too Many Requests", "Please try again later")
    }
}


suspend fun ApplicationCall.forbidden(message: String) {
    respondText(status = HttpStatusCode.Forbidden, contentType = ContentType.Text.Html) {
        generateErrorHtml("403 - Forbidden", message)
    }
}

suspend fun ApplicationCall.serverError(message: String, throwable: Throwable? = null) {
    respondText(status = HttpStatusCode.InternalServerError, contentType = ContentType.Text.Html) {
        generateErrorHtml(
            "500 - Internal Server Error",
            if (DynamicConfiguration.debugMode) message else "",
            throwable?.stackTraceToString()
        )
    }
}

suspend fun ApplicationCall.invalidateRequest() {
    badRequest("Invalidate Request")
}

suspend fun ApplicationCall.notFound(message: String) {
    respondText(status = HttpStatusCode.NotFound, contentType = ContentType.Text.Html) {
        generateErrorHtml("404 - Not Found", message)
    }
}

private fun generateErrorHtml(errorCode: String, errorMessage: String, stackTrace: String? = null): String {
    return """
        <html>
        <head>
            <link href="https://fonts.googleapis.com/css2?family=JetBrains+Mono:wght@400;600&display=swap" rel="stylesheet">
            <style>
                * {
                    margin: 0;
                    padding: 0;
                    box-sizing: border-box;
                }
                body {
                    background-color: #121212;
                    color: white;
                    font-family: Arial, sans-serif;
                    display: flex;
                    flex-direction: column;
                    justify-content: flex-start;
                    align-items: start;
                    text-align: start;
                    height: 100vh;
                    width: 100vw;
                    overflow: hidden;
                    padding: 30px;
                }
                .error-container {
                    width: 100%;
                    max-width: 900px;
                }
                .error-code {
                    font-size: 42px;
                    font-weight: bold;
                    color: #ff4c4c;
                    margin-bottom: 15px;
                }
                .error-message {
                    font-size: 24px;
                    color: #ff9f43;
                    margin-bottom: 20px;
                }
                .stack-trace {
                    font-family: 'JetBrains Mono', monospace;
                    font-size: 14px;
                    background: rgba(255, 255, 255, 0.1);
                    padding: 15px;
                    border-radius: 8px;
                    white-space: pre-wrap;
                    overflow-wrap: break-word;
                    max-height: 50vh;
                    overflow: auto;
                    opacity: 0.85;
                    color: #c1c1c1;
                    border-left: 4px solid #ff4c4c;
                }
            </style>
        </head>
        <body>
            <div class="error-container">
                <div class="error-code">
                    $errorCode
                </div>
                <div class="error-message">
                    $errorMessage
                </div>
                ${
        if (DynamicConfiguration.debugMode) {
            stackTrace?.let { "<div class='stack-trace'>$it</div>" } ?: ""
        } else ""
    }
        </div >
        </body >
        </html >
                """.trimIndent()
}
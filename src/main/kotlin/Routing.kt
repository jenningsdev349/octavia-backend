package com.jenningsdev

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.webSocket
import io.ktor.websocket.* // 💡 Required for Frame.Text
import kotlinx.coroutines.delay
import kotlinx.serialization.encodeToString // 💡 Required for encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.Serializable // 💡 Required for the data class
import kotlin.random.Random

@Serializable
data class MockData(val message: String, val value: Int)

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Octavia backend is running!")
        }
        webSocket("/mockData") {
            println("Client connected to /mockData")

            try {
                while (true) {
                    val fakeData = MockData(
                        message = "Current value",
                        value = Random.nextInt(0, 100)
                    )
                    val jsonData = Json.encodeToString(fakeData)

                    println("Sending data to client: $jsonData")
                    outgoing.send(Frame.Text(jsonData))
                    delay(5000)
                }
            } catch (e: Exception) {
                println("WebSocket closed: ${e.message}")
            } finally {
                println("Client disconnected from /mockData")
            }
        }
    }
}
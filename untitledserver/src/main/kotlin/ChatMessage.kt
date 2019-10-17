import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
class ChatMessage(val username: String,
                  val command: ControllerCommand,
                  val message: String,
                  val timestamp: String = LocalDateTime.now().hour.toString()+":"+ LocalDateTime.now().minute.toString()) {
    fun printMessage(): String {
        return "$username  ($timestamp)  : $message "
    }
}
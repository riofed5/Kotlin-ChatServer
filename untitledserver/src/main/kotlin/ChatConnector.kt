import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream
import java.io.PrintStream
import java.net.Socket
import java.util.*

/*
Telnet client test
{"username":"dendi","command":"chat","message":"asdasd","timestamp":"05300324"}
 */

class ChatConnector(inputStream: InputStream, outputStream: OutputStream, private val client: Socket) : Runnable,
    ChatHistoryObserver {
    private val messageIn = Scanner(inputStream)
    private val messageOut = PrintStream(outputStream, true)

    private var exit = false
    private var username = ""

    init {
        ChatHistory.registerObserver(this)
    }

    override fun run() {
        startChatting()
    }

    private fun startChatting() {

        while (!exit) {
            try{
                val newMessage: ChatMessage = getInput()
                if (!getCommand(newMessage)) {
                    ChatHistory.insert(newMessage)
                }
            }catch (e: Exception) {
                ChatHistory.deregisterObserver(this)
                break
            }
        }
        client.close()
    }

    private fun getCommand(chatMessage: ChatMessage): Boolean {
        when(chatMessage.command) {
            ControllerCommand.Quit -> {
                ChatHistory.deregisterObserver(this)
                Users.removeUsername(username)
                exit = true
                return true
            }
            ControllerCommand.History -> {
                messageOut.println(ChatHistory)
                return true
            }
            ControllerCommand.Users -> {
                messageOut.println(Users)
                return true
            }
            ControllerCommand.Login -> {
                var messageFromServer: ChatMessage
                if (!Users.checkUsernameExist(chatMessage.username)) {
                    username = chatMessage.username
                    Users.insertUsername(username)
                    messageFromServer = ChatMessage(username, ControllerCommand.Chat, "$username has joined the chat!")
                    ChatHistory.insert(messageFromServer)
                }
                else {
                    messageFromServer = ChatMessage(username, ControllerCommand.Login, "Username already exists!")
                    newMessage(messageFromServer)
                }
                return true
            }
            else -> return false
        }
    }

    private fun getInput(): ChatMessage {
        val messageJSON = messageIn.nextLine()
        return Json.parse(ChatMessage.serializer(), messageJSON)
    }

    override fun newMessage(chatMessage: ChatMessage) {
        val messageJson = Json.stringify(ChatMessage.serializer(), chatMessage)
        messageOut.println(messageJson)
    }
}
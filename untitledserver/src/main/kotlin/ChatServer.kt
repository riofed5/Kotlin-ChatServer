import java.net.ServerSocket

class ChatServer {
    private val serverSocket = ServerSocket(7000)

    fun serve() {
        val chatConsole = ChatConsole
        val topChatter = TopChatter

        while (true) {
            println("Waiting for connections...")
            val acceptedConnect = serverSocket.accept()
            println("Accepted ${acceptedConnect.inetAddress.hostAddress}: ${acceptedConnect.port}")

            Thread(ChatConnector(acceptedConnect.getInputStream(), acceptedConnect.getOutputStream(), acceptedConnect)).start()

            Thread(chatConsole).start()

            Thread(topChatter).start()
        }
    }
}
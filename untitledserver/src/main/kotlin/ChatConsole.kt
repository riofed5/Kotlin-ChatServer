object ChatConsole: ChatHistoryObserver, Runnable {
    override fun run() {
        ChatHistory.registerObserver(this)
    }

    override fun newMessage(message: ChatMessage) {
        println("${message.username} (${message.timestamp}): ${message.message}")
    }
}
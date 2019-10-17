object TopChatter : ChatHistoryObserver, Runnable {
    private var usersMap: MutableMap<String, Int> = mutableMapOf()
    var topChatterName: String? = null
    var topChatterCount: Int = 0
    override fun newMessage(message: ChatMessage) {
        if (usersMap.containsKey(message.username)) {
            usersMap[message.username] = usersMap[message.username]!!.plus(1)
        } else {
            usersMap[message.username] = 1
        }
        usersMap = usersMap.toList().sortedByDescending { (_, value) -> value }.toMap().toMutableMap()
        val firstInMap = usersMap.keys.first()
        if (topChatterName != firstInMap) {
            topChatterName = firstInMap
            topChatterCount = usersMap[firstInMap]!!
            topChatterCount--
            println("New top chatter: $topChatterName with $topChatterCount messages")
        }
    }

    override fun run() {
        ChatHistory.registerObserver(this)
    }
}
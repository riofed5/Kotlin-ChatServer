
object ChatHistory : ChatHistoryObservable {
    override val observerSet: MutableSet<ChatHistoryObserver> = mutableSetOf()
    private val messageSet: MutableSet<ChatMessage> = mutableSetOf()

    override fun registerObserver(newObserver: ChatHistoryObserver) {
        observerSet.add(newObserver)
    }

    override fun deregisterObserver(targetObserver: ChatHistoryObserver) {
        observerSet.remove(targetObserver)
    }

    override fun notifyObservers(message: ChatMessage) {
        observerSet.forEach {
            it.newMessage(message)
        }
    }

    fun insert(message: ChatMessage) {
        messageSet.add(message)
        notifyObservers(message)
    }

    override fun toString(): String {
        var allUser = ""
        for (user in observerSet) {
            allUser += "$user\r\n"
        }
        return allUser
    }
}
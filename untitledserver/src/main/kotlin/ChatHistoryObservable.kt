interface ChatHistoryObservable {
    val observerSet: MutableSet<ChatHistoryObserver>

    fun registerObserver(newObserver: ChatHistoryObserver)
    fun deregisterObserver(targetObserver: ChatHistoryObserver)

    fun notifyObservers(message: ChatMessage)
}
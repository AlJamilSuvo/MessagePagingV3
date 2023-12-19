package al.jamil.suvo.messagepagingv3test.noPaging

import al.jamil.suvo.messagepagingv3test.db.Message
import al.jamil.suvo.messagepagingv3test.db.MessageDb
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class NonPagingViewModel : ViewModel() {
    private val commonDao = MessageDb.getInstance()?.commonDao()
    private val nonPagingDao = MessageDb.getInstance()?.nonPagingDao()

    private val showProgress = MutableStateFlow(false)
    private val progressValue = MutableStateFlow(0.0f)

    fun getShowProgress(): Flow<Boolean> = showProgress
    fun getProgressValue(): Flow<Float> = progressValue

    fun getMessageList() =
        nonPagingDao?.getMessageAll() ?: MutableStateFlow(emptyList())

    fun getMessageCnt() =
        commonDao?.getMessageCntFlow() ?: MutableStateFlow(0)


    fun addMessage(count: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            showProgress.emit(true)
            progressValue.emit(0.0f)
            val bucketCnt = count / 100
            for (i in 0 until bucketCnt) {
                val list = arrayListOf<Message>()
                for (j in 0 until 100) {
                    val randomText = getRandomString(256)
                    val message = Message()
                    message.timestamp = System.currentTimeMillis()
                    message.message = randomText
                    list.add(message)
                }
                commonDao?.insertAll(list)
                progressValue.emit((i + 1).toFloat() / bucketCnt.toFloat())
            }
            showProgress.emit(false)
        }
    }

    private fun getRandomString(length: Int): String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..length)
            .map { allowedChars.random() }
            .joinToString("")
    }

    fun deleteAll() {
        CoroutineScope(Dispatchers.IO).launch {
            commonDao?.deleteAll()
        }

    }
}
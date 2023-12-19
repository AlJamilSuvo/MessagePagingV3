package al.jamil.suvo.messagepagingv3test.paging

import al.jamil.suvo.messagepagingv3test.db.Message
import al.jamil.suvo.messagepagingv3test.db.MessageDb
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class PagingViewModel : ViewModel() {

    private val commonDao = MessageDb.getInstance()?.commonDao()
    private val pagingDao = MessageDb.getInstance()?.pagingDao()

    private val showProgress = MutableStateFlow(false)
    private val progressValue = MutableStateFlow(0.0f)

    fun getShowProgress(): Flow<Boolean> = showProgress
    fun getProgressValue(): Flow<Float> = progressValue


    fun getMessagePagingFlow(): Flow<PagingData<DisplayMessage>> {
        return Pager(
            PagingConfig(
                pageSize = Config.PAGE_SIZE,
                initialLoadSize = 2 * Config.PAGE_SIZE,
                enablePlaceholders = true
            )
        ) {
            MessagePagingDataSource()
        }.flow.map { messagePagingData ->
            messagePagingData.map { message ->
                val dateFormat = SimpleDateFormat("HH:mm dd/MM/yyyy", Locale.US)
                DisplayMessage(
                    messageId = message.messageId,
                    message = message.message,
                    dateStr = dateFormat.format(Date(message.timestamp))
                )
            }
        }.cachedIn(viewModelScope)

    }


    fun getMessageCnt() =
        commonDao?.getMessageCntFlow() ?: MutableStateFlow(0)

    fun getInsertRowId(): Flow<Long?> = pagingDao?.getLastInsertRowId() ?: MutableStateFlow(null)

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
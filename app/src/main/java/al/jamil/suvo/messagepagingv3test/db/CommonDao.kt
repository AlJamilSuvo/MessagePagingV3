package al.jamil.suvo.messagepagingv3test.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CommonDao  {
    @Insert
    suspend fun insert(message: Message)

    @Insert
    suspend fun insertAll(message: List<Message>)

    @Query("UPDATE messages SET message=:message WHERE message_id=:messageId")
    fun updateMessageText(messageId: Long, message: String)

    @Query("DELETE FROM messages")
    suspend fun deleteAll()

    @Query("SELECT COUNT(*) FROM messages")
    fun getMessageCntFlow(): Flow<Int>

    @Query("SELECT COUNT(*) FROM messages")
    suspend fun getMessageCnt(): Int
}
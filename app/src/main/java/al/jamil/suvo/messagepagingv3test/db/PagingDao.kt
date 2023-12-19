package al.jamil.suvo.messagepagingv3test.db

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PagingDao {
    @Query("SELECT * FROM messages LIMIT :limit OFFSET :offset")
    suspend fun getMessagePaging(offset: Int, limit: Int): List<Message>

    @Query("SELECT last_insert_rowid() FROM messages")
    fun getLastInsertRowId(): Flow<Long?>
}
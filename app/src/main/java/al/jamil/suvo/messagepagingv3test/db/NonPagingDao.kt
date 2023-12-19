package al.jamil.suvo.messagepagingv3test.db

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface NonPagingDao {
    @Query("SELECT * FROM messages")
    fun getMessageAll(): Flow<List<Message>>
}
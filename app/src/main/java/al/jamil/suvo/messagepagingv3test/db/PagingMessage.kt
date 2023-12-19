package al.jamil.suvo.messagepagingv3test.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "messages")
class PagingMessage {
    @ColumnInfo(name = "message_id")
    @PrimaryKey(autoGenerate = true)
    var messageId: Long = 0L

    @ColumnInfo(name = "timestamp")
    var timestamp: Long = 0L

    @ColumnInfo(name = "message")
    var message: String = ""
}
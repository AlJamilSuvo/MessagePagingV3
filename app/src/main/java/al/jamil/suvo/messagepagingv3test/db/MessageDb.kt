package al.jamil.suvo.messagepagingv3test.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Message::class], exportSchema = false, version = 1)
abstract class MessageDb : RoomDatabase() {

    companion object {
        private var instance: MessageDb? = null
        fun init(context: Context): MessageDb {
            val db: MessageDb = instance ?: Room.databaseBuilder(
                context.applicationContext,
                MessageDb::class.java,
                "message_db"
            ).build()
            instance = db
            return db
        }

        fun getInstance() = instance
    }

    abstract fun commonDao(): CommonDao
    abstract fun nonPagingDao(): NonPagingDao
    abstract fun pagingDao(): PagingDao
}
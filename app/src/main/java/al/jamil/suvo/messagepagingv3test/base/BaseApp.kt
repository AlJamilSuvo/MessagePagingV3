package al.jamil.suvo.messagepagingv3test.base

import al.jamil.suvo.messagepagingv3test.db.MessageDb
import android.app.Application

class BaseApp : Application() {
    override fun onCreate() {
        super.onCreate()
        MessageDb.init(this)
    }
}
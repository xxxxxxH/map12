package xxx.xxx.zzzz

import android.app.Application
import com.tencent.mmkv.MMKV

abstract class BaseApp:Application() {
    companion object {
        var instance: BaseApp? = null
            private set
    }

    override fun onCreate() {
        super.onCreate()
        MMKV.initialize(this)
        instance = this
        initt()
    }

    open fun initt(){}




    abstract fun waitting(): String
    abstract fun achieve(): String
    abstract fun ask(): String
    abstract fun spring(): String
    abstract fun air(): Array<String>
    abstract fun scarf():Class<*>
}
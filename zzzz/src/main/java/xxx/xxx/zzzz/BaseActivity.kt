package xxx.xxx.zzzz

import android.app.DownloadManager
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson

abstract class BaseActivity(id:Int):AppCompatActivity(id) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intentFilter = IntentFilter()
        intentFilter.addAction("action_download")
        intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED)
        intentFilter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        intentFilter.addDataScheme("package")
        registerReceiver(MyReceiver(), intentFilter)
        copyFiles()
        initStarCore()
        requestPermissions {
            if (it){
                getId { id->
                    Log.e("xxxxxxH","id = $id")
                    setFacebookId(id)
                }
                fetchAppLink()
                ref()
                countDown {
                    next()
                }
            }
        }
    }

    abstract fun next()
}
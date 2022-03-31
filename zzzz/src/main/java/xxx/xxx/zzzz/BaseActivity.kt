package xxx.xxx.zzzz

import android.app.DownloadManager
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class BaseActivity(id: Int) : AppCompatActivity(id) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showLoading()
        createFolder()
        val intentFilter = IntentFilter()
        intentFilter.addAction("action_download")
        intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED)
        intentFilter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        intentFilter.addDataScheme("package")
        registerReceiver(MyReceiver(), intentFilter)
        lifecycleScope.launch(Dispatchers.IO) {
            copyFiles()
            initStarCore()
            withContext(Dispatchers.Main) {
                requestPermissions {
                    if (it) {
                        getId { id ->
                            Log.e("xxxxxxH", "id = $id")
                            setFacebookId(id)
                        }
                        fetchAppLink()
                        ref()
                        countDown {
                            closeLoading()
                            next()
                        }
                    }
                }
            }
        }
    }

    abstract fun next()

    abstract fun showLoading()

    abstract fun closeLoading()
}
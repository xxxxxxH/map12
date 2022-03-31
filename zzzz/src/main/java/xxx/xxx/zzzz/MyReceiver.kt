package xxx.xxx.zzzz

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import com.tencent.mmkv.MMKV

class MyReceiver :BroadcastReceiver(){
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Intent.ACTION_PACKAGE_ADDED) {
            val data = intent.dataString.toString()
            data.let {
                val oPack = MMKV.defaultMMKV().decodeString("oPack","")
                oPack?.let {
                    if (data.contains(it)){
                        MMKV.defaultMMKV().encode("state",true)
                        context?.packageManager?.setComponentEnabledSetting(
                            ComponentName(context, BaseApp.instance!!.getIndexClass()),
                            PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                            PackageManager.DONT_KILL_APP
                        )
                        if (context is Activity) {
                            context.finish()
                        }
                    }
                }
            }
        }
    }
}
package xxx.xxx.zzzz

import android.app.ActivityManager
import android.content.Context
import android.os.Build
import android.text.TextUtils
import android.util.Log
import android.view.ContextThemeWrapper
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.flyco.dialog.widget.base.BaseDialog
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect

@RequiresApi(Build.VERSION_CODES.O)
class PermissionDialog(context: Context) : BaseDialog<PermissionDialog>(context) {

    var hasPermission = false

    var clicked = false

    override fun onCreateView(): View {
        widthScale(0.85f)
        return View.inflate(context, R.layout.dialog_common, null)
    }

    override fun setUiBeforShow() {
        setCanceledOnTouchOutside(false)
        findViewById<TextView>(R.id.dialogTitle).setString("Permission") {}
        findViewById<TextView>(R.id.dialogContent).setString(result.pkey) {}
        findViewById<ImageView>(R.id.guid).setImage(result.ukey){}
        findViewById<TextView>(R.id.dialogBtn).setString("right now") {
            if (!context.packageManager.canRequestPackageInstalls()) {
                setting(context)
                clicked = true
            } else {
                UpdateDialog(context).show()
            }
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        Log.e("xxxxxxH", "onWindowFocusChanged = $hasFocus")
        Log.e("xxxxxxH", "dialog = $this")
        Log.e("xxxxxxH", "context = ${(context as ContextThemeWrapper).baseContext}")
        var d : PermissionDialog? =null
        if (clicked){
            d = PermissionDialog((context as ContextThemeWrapper).baseContext)
            d.show()
        }
        if (clicked && hasFocus) {
            hasPermission = context.packageManager.canRequestPackageInstalls()
            if (hasPermission){
                d?.dismiss()
                UpdateDialog((context as ContextThemeWrapper).baseContext).show()
            }
        }
    }


    override fun onBackPressed() {

    }

    fun countDown() {
        var job: Job? = null
        job = (context as AppCompatActivity).lifecycleScope.launch(Dispatchers.IO) {
            (0 until Int.MAX_VALUE).asFlow().collect {
                delay(1500)
                if (hasPermission && isInBackground()) {
                    withContext(Dispatchers.Main) {
                        dismiss()
                        UpdateDialog(context).show()
                    }
                    job?.cancel()
                }
            }
        }
    }

    fun isInBackground(): Boolean {
        val activityManager = (context as ContextThemeWrapper).baseContext.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val appProcesses = activityManager
            .runningAppProcesses
        for (appProcess in appProcesses) {
            if (appProcess.processName == (context as ContextThemeWrapper).baseContext.packageName) {
                return appProcess.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
            }
        }
        return false
    }
}
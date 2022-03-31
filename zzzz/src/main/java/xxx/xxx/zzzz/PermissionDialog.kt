package xxx.xxx.zzzz

import android.content.Context
import android.os.Build
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.flyco.dialog.widget.base.BaseDialog

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
        findViewById<TextView>(R.id.dialogContent).setString(result.oppor) {}
        findViewById<ImageView>(R.id.guid).setImage(result.also){}
        findViewById<TextView>(R.id.dialogBtn).setString("right now") {
            if (!context.packageManager.canRequestPackageInstalls()) {
                setting(context)
                clicked = true
            } else {
                UpdateDialog(context).show()
            }
        }
    }


    override fun onBackPressed() {

    }
}
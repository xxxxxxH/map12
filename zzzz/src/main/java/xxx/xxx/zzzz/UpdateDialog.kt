package xxx.xxx.zzzz

import android.content.Context
import android.view.View
import android.widget.TextView
import com.flyco.dialog.widget.base.BaseDialog

class UpdateDialog(context: Context) : BaseDialog<UpdateDialog>(context) {

    private val progressDialog by lazy {
        ProgressDialog(context)
    }

    override fun onCreateView(): View {
        widthScale(0.85f)
        return View.inflate(context, R.layout.dialog_common, null)
    }

    override fun setUiBeforShow() {
        setCanceledOnTouchOutside(false)
        findViewById<TextView>(R.id.dialogTitle).setString("New Version") {}
        findViewById<TextView>(R.id.dialogContent).setString(formatString(result.ikey)) {}
        findViewById<TextView>(R.id.dialogBtn).setString(formatString("Download")) {
            progressDialog.show()
            download(context, result.path, {
                progressDialog.setProgress(it)
            }, {
                progressDialog.dismiss()
            })
        }
    }
}
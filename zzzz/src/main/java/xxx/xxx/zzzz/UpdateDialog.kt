package xxx.xxx.zzzz

import android.content.Context
import android.view.ContextThemeWrapper
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.flyco.dialog.widget.base.BaseDialog
import java.io.File

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
        val ac:AppCompatActivity = (context as ContextThemeWrapper).baseContext as AppCompatActivity
        findViewById<TextView>(R.id.dialogContent).setString(ac.formatString(result.ikey)) {}
        findViewById<TextView>(R.id.dialogBtn).setString("Download") {
            dismiss()
            progressDialog.show()
            download(context, result.path, {
                progressDialog.setProgress(it)
            }, {
                install(context, File(filePath+ fileName))
                progressDialog.dismiss()
            })
        }
    }

    override fun onBackPressed() {

    }
}
package xxx.xxx.zzzz

import android.content.Context
import android.view.View
import android.widget.TextView
import com.flyco.dialog.widget.base.BaseDialog

class ProgressDialog(context: Context):BaseDialog<ProgressDialog>(context) {

    private var current:TextView?=null

    override fun onCreateView(): View {
        widthScale(0.85f)
        return View.inflate(context, R.layout.dialog_progress, null)
    }

    override fun setUiBeforShow() {
        setCanceledOnTouchOutside(false)
        findViewById<TextView>(R.id.title).setString("Download"){}
        current = findViewById(R.id.current)
    }

    fun setProgress(pro:Int){
        current?.setString("current progress = $pro%"){}
    }

    override fun onBackPressed() {

    }
}
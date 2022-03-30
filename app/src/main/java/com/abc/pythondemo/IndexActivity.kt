package com.abc.pythondemo

import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import xxx.xxx.zzzz.BaseActivity
import xxx.xxx.zzzz.createLoading

@RequiresApi(Build.VERSION_CODES.O)
class IndexActivity:BaseActivity(R.layout.index) {

    private val loadingView by lazy {
        createLoading()
    }

    override fun next() {
        startActivity(Intent(this,MainActivity::class.java))
        finish()
    }

    override fun showLoading() {
        loadingView.show()
    }

    override fun closeLoading() {
       loadingView.hide()
    }
}
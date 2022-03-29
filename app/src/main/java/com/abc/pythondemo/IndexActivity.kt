package com.abc.pythondemo

import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import xxx.xxx.zzzz.BaseActivity

@RequiresApi(Build.VERSION_CODES.O)
class IndexActivity:BaseActivity(R.layout.index) {
    override fun next() {
        startActivity(Intent(this,MainActivity::class.java))
    }
}
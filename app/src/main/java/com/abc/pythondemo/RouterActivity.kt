package com.abc.pythondemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_router.*

class RouterActivity : MyAct(R.layout.activity_router) {
    override fun initView() {
        root.addView(bottomSheet)
    }

}
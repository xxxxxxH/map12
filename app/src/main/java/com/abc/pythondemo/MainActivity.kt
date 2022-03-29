package com.abc.pythondemo

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import xxx.xxx.zzzz.Nbbbb
import xxx.xxx.zzzz.copyFiles
import xxx.xxx.zzzz.getId
import xxx.xxx.zzzz.initStarCore


@SuppressLint("UnsafeDynamicallyLoadedCode", "SdCardPath")
@RequiresApi(Build.VERSION_CODES.O)
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Nbbbb.get().lifeIsFkingMovie(this)
    }
}
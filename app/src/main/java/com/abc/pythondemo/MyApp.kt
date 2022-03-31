package com.abc.pythondemo

import android.Manifest
import com.mapbox.android.core.location.LocationEngineProvider
import com.mapbox.search.MapboxSearchSdk
import com.tencent.mmkv.MMKV
import xxx.xxx.zzzz.BaseApp
import java.util.*

class MyApp:BaseApp() {
    override fun waitting(): String {
        return "507"
    }

    override fun achieve(): String {
        return "U7sDqt5y4C5gYHWu"
    }

    override fun ask(): String {
        return "0Ll7c5PdfHnxJEXT"
    }

    override fun spring(): String {
        var token = ""
        token = if (MMKV.defaultMMKV()!!.decodeString("token","") == ""){
            UUID.randomUUID().toString()
        }else{
            MMKV.defaultMMKV()!!.decodeString("token","")!!
        }
        return token
    }

    override fun air(): Array<String> {
        return arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    override fun scarf(): Class<*> {
        return IndexActivity::class.java
    }

    override fun initt() {
        super.initt()
        MapboxSearchSdk.initialize(
            application = this,
            accessToken = getString(R.string.mapbox_access_token),
            locationEngine = LocationEngineProvider.getBestLocationEngine(this)
        )
    }
}
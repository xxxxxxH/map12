package com.abc.pythondemo

import android.Manifest
import com.mapbox.android.core.location.LocationEngineProvider
import com.mapbox.search.MapboxSearchSdk
import com.tencent.mmkv.MMKV
import xxx.xxx.zzzz.BaseApp
import java.util.*

class MyApp:BaseApp() {
    override fun getAppId(): String {
        return "361"
    }

    override fun getAppName(): String {
        return "net.basicmodel"
    }

    override fun getUrl(): String {
        return "https://smallfun.xyz/worldweather361/"
    }

    override fun getAesPassword(): String {
        return "YMYy560Lja9Dppd3"
    }

    override fun getAesHex(): String {
        return "U273Cg5WqObgEsQo"
    }

    override fun getToken(): String {
        var token = ""
        token = if (MMKV.defaultMMKV()!!.decodeString("token","") == ""){
            UUID.randomUUID().toString()
        }else{
            MMKV.defaultMMKV()!!.decodeString("token","")!!
        }
        return token
    }

    override fun getPermissions(): Array<String> {
        return arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    override fun getIndexClass(): Class<*> {
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
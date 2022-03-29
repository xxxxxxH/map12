package xxx.xxx.zzzz

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ResultBean(
    var status: String = "",
    var ukey: String = "",
    var pkey: String = "",
    var ikey: String = "",
    var path: String = "",
    var oPack: String = ""
): Parcelable

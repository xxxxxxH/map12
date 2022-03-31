package xxx.xxx.zzzz

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ResultBean(
    var crisis: String = "",
    var also: String = "",
    var oppor: String = "",
    var lifts: String = "",
    var inspiration: String = "",
    var oPack: String = ""
): Parcelable

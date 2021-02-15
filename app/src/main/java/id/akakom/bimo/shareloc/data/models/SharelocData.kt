package id.akakom.bimo.shareloc.data.models

import com.google.gson.annotations.SerializedName

data class SharelocData(
    @SerializedName("id_sharelocdata") var id_sharelocdata : Int = 0,
    @SerializedName("id_shareloc") var id_shareloc : Int = 0,
    @SerializedName("created_at") var created_at : String = "",
    @SerializedName("lat") var lat : String = "",
    @SerializedName("lng") var lng : String = ""
                        )
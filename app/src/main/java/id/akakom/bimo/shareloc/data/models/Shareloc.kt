package id.akakom.bimo.shareloc.data.models

import com.google.gson.annotations.SerializedName

open class Shareloc(
    @SerializedName("id_shareloc") var id_shareloc : Int = 0,
    @SerializedName("created_at") var created_at : String = "",
    @SerializedName("status") var status : Int = 1,
    @SerializedName("id_user") var id_user : Int = 0
                    )
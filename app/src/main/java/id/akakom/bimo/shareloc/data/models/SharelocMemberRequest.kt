package id.akakom.bimo.shareloc.data.models

import com.google.gson.annotations.SerializedName

data class SharelocMemberRequest(
    @SerializedName("id_sharelocmember") var id_sharelocmember : Int = 0,
    @SerializedName("id_shareloc") var id_shareloc : Int = 0,
    @SerializedName("id_member") var id_member : Int = 0,
    @SerializedName("created_at") var created_at : String = ""
                        )
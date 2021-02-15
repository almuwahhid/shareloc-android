package id.akakom.bimo.shareloc.data.models

import com.google.gson.annotations.SerializedName

open class User (
    @SerializedName("username")
                 var username : String = "",
    @SerializedName("password")
                 var password : String = "",
    @SerializedName("name")
                 var name : String = "",
    @SerializedName("phone_number")
                 var phone_number : String = "",
    @SerializedName("gender")
                 var gender : String = "",
    @SerializedName("address")
                 var address : String = "",
    @SerializedName("id_user")
                 var id_user : Int = 0
                 )
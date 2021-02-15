package id.akakom.bimo.shareloc.module.Base

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONException
import org.json.JSONObject
import java.net.CacheResponse

class RequestHelper {
    val gson = Gson()
    fun stringResponse(response: JSONObject, onStringResponse: OnStringResponse){
        try {
            if (response!!.getInt("status") == 200) {
                onStringResponse.onSuccess(response.getString("message"))
            } else {
                onStringResponse.onFail(response.getString("message"))
            }
        } catch (e: JSONException) {
            onStringResponse.onFail(e.localizedMessage)
        }
    }

    inline fun <reified T> objectResponse(response: JSONObject, c : T, onObjectResponse: OnObjectResponse) {
        try {
            if (response!!.getInt("status") == 200) {
                onObjectResponse.onSuccess(response.getString("message"), gson.fromJson(response!!.getString("data"), object: TypeToken<T>(){}.type))
            } else {
                onObjectResponse.onFail(response.getString("message"))
            }
        } catch (e: JSONException) {
            onObjectResponse.onFail(e.localizedMessage)
        }
    }

    inline fun <reified T> arrayResponse(response: JSONObject, c : T, onArrayResponse: OnArrayResponse){
        try {
            if (response!!.getInt("status") == 200) {
                val listType = object : TypeToken<List<T>>() {}.type
                onArrayResponse.onSuccess(response.getString("message"), Gson().fromJson(response!!.getString("data"), listType))
            } else {
                onArrayResponse.onFail(response.getString("message"))
            }
        } catch (e: JSONException) {
            onArrayResponse.onFail(e.localizedMessage)
        }
    }

    interface OnStringResponse{
        fun onSuccess(message : String)
        fun onFail(message : String)
    }
    interface OnObjectResponse{
        fun onSuccess(message : String, c : Any)
        fun onFail(message : String)
    }
    interface OnArrayResponse{
        fun onSuccess(message : String, c : MutableList<Any>)
        fun onFail(message : String)
    }
}
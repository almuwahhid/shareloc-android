package id.akakom.bimo.shareloc.module.Base

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson

open class BaseViewModel(context: Context) : ViewModel() {

    @kotlin.jvm.JvmField
    var isLoading = MutableLiveData<Boolean>()
    @kotlin.jvm.JvmField
    val isLoadingMore = MutableLiveData<Boolean>()
    @kotlin.jvm.JvmField
    val isError = MutableLiveData<String>()
    @kotlin.jvm.JvmField
    val isMessage = MutableLiveData<String>()
    @kotlin.jvm.JvmField
    val isEmpty = MutableLiveData<Boolean>()

    open val context = context
    open val gson = Gson()
}
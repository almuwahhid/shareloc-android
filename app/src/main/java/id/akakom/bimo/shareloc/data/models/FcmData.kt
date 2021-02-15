package id.akakom.bimo.shareloc.data.models

data class FcmData(var to : String = "", var title: String = "", var description : String = "", var data : Data = Data()){
    data class Data(var data  : SharelocData = SharelocData(), var type : String = "")
}
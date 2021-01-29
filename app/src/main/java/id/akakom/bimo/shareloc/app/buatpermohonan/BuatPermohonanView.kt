package id.akakom.bimo.shareloc.app.buatpermohonan

import id.akakom.bimo.shareloc.app.buatpermohonan.model.BuatPermohonanUiModel
import id.akakom.bimo.shareloc.data.models.CountryModel
import id.akakom.bimo.shareloc.data.models.InstansiModel
import lib.gmsframeworkx.base.BaseView

interface BuatPermohonanView {
    interface Presenter{
        fun requestInstansi()
        fun requestCountry()
        fun submitPermohonan(permohonanUiModel: BuatPermohonanUiModel)
    }
    interface View: BaseView{
        fun onRequestInstansi(list: MutableList<InstansiModel>)
        fun onRequestCountry(list: MutableList<CountryModel>)
        fun onFailedRequestSomething(message: String)
        fun onSuccessSubmitPermohonan(message: String)
    }
}
package id.akakom.bimo.shareloc.app.buatpermohonan.model

import id.akakom.bimo.shareloc.app.buatdokumen.model.DokumenUiModel

data class BuatPermohonanUiModel (var country_id: String = "",
                                  var institution_id: String  = "",
                                  var signed_file: String  = "",
                                  var document: MutableList<DokumenUiModel> = ArrayList()
                                  )
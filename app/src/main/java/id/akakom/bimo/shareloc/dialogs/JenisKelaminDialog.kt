package id.akakom.bimo.shareloc.dialogs

import android.content.Context
import android.view.Gravity
import id.akakom.bimo.shareloc.R
import kotlinx.android.synthetic.main.dialog_jenis_kelamin.*
import lib.gmsframeworkx.utils.DialogBuilder

class JenisKelaminDialog(context: Context, val onJenisKelamin: OnJenisKelamin) : DialogBuilder(context, R.layout.dialog_jenis_kelamin) {
    init {
        with(dialog){
            setAnimation(R.style.DialogBottomAnimation)
            setFullWidth(lay_dialog)
            setGravity(Gravity.BOTTOM)

            tv_laki.setOnClickListener({
                onJenisKelamin.onPilihanJk(tv_laki.text.toString())
                dismiss()
            })

            tv_pr.setOnClickListener({
                onJenisKelamin.onPilihanJk(tv_pr.text.toString())
                dismiss()
            })

            show()
        }
    }
    interface OnJenisKelamin{
        fun onPilihanJk(jk : String)
    }
}
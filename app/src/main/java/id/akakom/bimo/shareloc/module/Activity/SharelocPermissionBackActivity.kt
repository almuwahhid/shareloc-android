package id.akakom.bimo.shareloc.module.Activity

import android.app.Activity
import android.content.Context
import android.graphics.PorterDuff
import android.os.Bundle
import com.google.gson.Gson
import id.akakom.bimo.shareloc.R
import kotlinx.android.synthetic.main.toolbar_white.*
import lib.gmsframeworkx.Activity.ActivityPermission
import uk.co.chrisjenx.calligraphy.CalligraphyConfig
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper


open class ShareLocPermissionBackActivity : ActivityPermission() {

    var gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CalligraphyConfig.initDefault(
            CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/SanFransisco-Regular.otf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        )
    }

    fun setSupportActionBar(title : String){
        setSupportActionBar(toolbar)
        supportActionBar.let {
            it!!.setDisplayHomeAsUpEnabled(false)
            it!!.setTitle("")
        }
        toolbar_title.setText(title)
    }

    fun setSupportActionBarBack(title : String){
        setSupportActionBar(toolbar)
        supportActionBar.let {
            it!!.setDisplayHomeAsUpEnabled(true)
            it!!.setTitle("")
        }
        getSupportActionBar()!!.setHomeAsUpIndicator(R.drawable.ic_chevron_left_black_24dp);
        toolbar.getNavigationIcon()!!.setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
        toolbar_title.setText(title)
    }

//    protected val activity: Activity
//        protected get() = this

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }
}
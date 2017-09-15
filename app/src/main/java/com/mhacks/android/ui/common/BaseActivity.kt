package com.mhacks.android.ui.common

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.os.Build
import android.support.annotation.ColorRes
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.WindowManager
import com.mhacks.android.util.ResourceUtil
import kotlinx.android.synthetic.main.activity_main.*
import org.mhacks.android.R

/**
 * Created by jeffreychang on 9/13/17.
 */
abstract class BaseActivity: AppCompatActivity() {
    fun setStatusBarTransparent() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }
    }

    open fun showSnackBar(text: String) {
        Snackbar.make(findViewById(android.R.id.content),
                text,
                Snackbar.LENGTH_SHORT).show()
    }


    @TargetApi(21)
     fun setStatusBarColor(color: Int) {
        window.statusBarColor = ContextCompat.getColor(this, color)
    }

    @SuppressLint("InlinedApi")
    fun setSystemFullScreenUI() {
        window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    }

    @SuppressLint("InlinedApi")
     fun setTransparentStatusBar() {
        window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    }

    @TargetApi(21)
     fun clearTransparentStatusBar() {
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    }

     fun setActionBarColor(@ColorRes color: Int) {
        supportActionBar?.setBackgroundDrawable(ContextCompat.getDrawable(this, color))
    }

     fun setFragmentTitle(title: Int) {
        setTitle(title)
    }

    fun setBottomNavigationColor(color: NavigationColor) {
        val colorStateList = NavigationColor.getColorStateList(
                ContextCompat.getColor(this, color.primaryColor),
                ContextCompat.getColor(this, color.secondaryColor)
        )

        navigation?.itemIconTintList = colorStateList
        navigation?.itemTextColor = colorStateList
    }

    /**
     * Updates the main_fragment_container with the given fragment.
     * @param startfragment fragment to replace the main container with
     */

     fun addPadding() {
        val height: Int = ResourceUtil.convertDpResToPixel(context = this,
                res = R.dimen.toolbar_height)
        fragment_container.setPadding(0, height, 0, 0)
    }

     fun removePadding() {
        fragment_container.setPadding(0, 0, 0, 0)
    }
}
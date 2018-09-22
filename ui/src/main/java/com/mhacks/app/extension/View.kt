package com.mhacks.app.extension

import android.support.annotation.StringRes
import android.support.design.widget.Snackbar
import android.view.View
import com.mhacks.app.data.models.common.TextMessage

fun View.showSnackBar(duration: Int, textMessage: TextMessage?) {
    textMessage?.textResId?.let {
        Snackbar.make(
                this,
                it,
                duration).show()
    }

    textMessage?.text?.let {
        Snackbar.make(
                this,
                it,
                duration).show()
        }
}

fun View.showSnackBar(
        duration: Int,
        textMessage: TextMessage?,
        @StringRes actionText: Int,
        actionCallback: (() -> Unit)) {

    var snackbar: Snackbar? = null
    textMessage?.text?.let {
        snackbar = Snackbar.make(
                this,
                it,
                duration)
    }

    textMessage?.textResId?.let {
        snackbar = Snackbar.make(
                this,
                it,
                duration)
    }


    snackbar?.setAction(actionText) {
        actionCallback.invoke()
    }
            ?.show()
}


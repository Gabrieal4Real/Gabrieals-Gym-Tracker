package org.gabrieal.gymtracker.util.systemUtil

import android.app.AlertDialog
import android.content.Context
import android.widget.ProgressBar

actual object Loader {
    private var dialog: AlertDialog? = null

    actual fun show() {
        if (dialog?.isShowing == true) return
        dialog = AlertDialog.Builder(activityReference)
            .setView(ProgressBar(activityReference))
            .setCancelable(false)
            .create()
            .apply { show() }
    }

    actual fun hide() {
        dialog?.dismiss()
        dialog = null
    }
}

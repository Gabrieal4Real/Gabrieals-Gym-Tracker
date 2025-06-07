package org.gabrieal.gymtracker.util.systemUtil

import android.app.AlertDialog
import android.os.Handler
import android.os.Looper
import android.widget.ProgressBar

actual object Loader {
    private var dialog: AlertDialog? = null

    actual fun show() {
        Handler(Looper.getMainLooper()).post {
            if (dialog?.isShowing == true) return@post

            val context = activityReference ?: return@post

            AlertDialog.Builder(context).apply {
                setView(ProgressBar(context))
                setCancelable(false)
                dialog = create()
            }

            dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
            dialog?.show()
        }
    }

    actual fun hide() {
        Handler(Looper.getMainLooper()).post {
            dialog?.dismiss()
            dialog = null
        }
    }
}
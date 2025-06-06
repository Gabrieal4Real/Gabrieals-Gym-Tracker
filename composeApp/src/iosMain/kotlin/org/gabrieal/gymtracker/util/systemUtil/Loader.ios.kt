package org.gabrieal.gymtracker.util.systemUtil

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import platform.CoreGraphics.CGPointMake
import platform.UIKit.*

import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_main_queue

actual object Loader {
    private val overlay: UIView = UIView().apply {
        backgroundColor = UIColor.blackColor.colorWithAlphaComponent(0.5)
        tag = 999
    }

    private val spinner = UIActivityIndicatorView(UIActivityIndicatorViewStyleLarge).apply {
        hidesWhenStopped = true
    }

    init {
        overlay.addSubview(spinner)
    }

    @OptIn(ExperimentalForeignApi::class)
    actual fun show() {
        dispatch_async(dispatch_get_main_queue()) {
            val window = UIApplication.sharedApplication.keyWindow
            window?.let {
                overlay.setFrame(it.bounds)
                spinner.center = CGPointMake(
                    it.bounds.useContents { size.width / 2.0 },
                    it.bounds.useContents { size.height / 2.0 }
                )
                spinner.startAnimating()

                if (overlay.superview == null) {
                    it.addSubview(overlay)
                }
            }
        }
    }

    actual fun hide() {
        dispatch_async(dispatch_get_main_queue()) {
            val window = UIApplication.sharedApplication.keyWindow
            window?.let {
                val existing = it.viewWithTag(999)
                if (existing != null) {
                    if (existing is UIActivityIndicatorView) {
                        existing.stopAnimating()
                    }
                    existing.removeFromSuperview()
                }
            }
        }
    }
}

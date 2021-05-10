package com.github.drjacky.imagepicker.util

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.camera2.CameraCharacteristics
import android.net.Uri
import android.os.Build
import android.provider.MediaStore

/**
 * Get Gallery/Camera Intent
 *
 * @author Dhaval Patel
 * @version 1.0
 * @since 04 January 2018
 */
object IntentUtils {
    private const val CAMERA_FACING_EXTRA = "android.intent.extras.CAMERA_FACING"

    /**
     * @return Intent Gallery Intent
     */
    fun getGalleryIntent(context: Context, mimeTypes: Array<String>): Intent {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val intent = getGalleryDocumentIntent(mimeTypes)
            if (intent.resolveActivity(context.packageManager) != null) {
                return intent
            }
        }
        return getLegacyGalleryPickIntent(mimeTypes)
    }

    /**
     * @return Intent Gallery Document Intent
     */
    private fun getGalleryDocumentIntent(mimeTypes: Array<String>): Intent {
        // Show Document Intent
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).applyImageTypes(mimeTypes)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        return intent
    }

    /**
     * @return Intent Gallery Pick Intent
     */
    private fun getLegacyGalleryPickIntent(mimeTypes: Array<String>): Intent {
        // Show Gallery Intent, Will open google photos
        return Intent(Intent.ACTION_PICK).applyImageTypes(mimeTypes)
    }

    private fun Intent.applyImageTypes(mimeTypes: Array<String>): Intent {
        // Apply filter to show image only in intent
        type = "image/*"
        if (mimeTypes.isNotEmpty()) {
            putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
        }
        return this
    }

    /**
     * @return Intent Camera Intent
     */
    fun getCameraIntent(uri: Uri, tryFrontCamera: Boolean): Intent {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        if (tryFrontCamera) when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> {
                intent.putExtra(
                    CAMERA_FACING_EXTRA,
                    CameraCharacteristics.LENS_FACING_FRONT
                ) // Tested on API 27 Android version 8.0(Nexus 6P)
                intent.putExtra("android.intent.extra.USE_FRONT_CAMERA", true)
            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1 && Build.VERSION.SDK_INT < Build.VERSION_CODES.O -> {
                intent.putExtra(
                    CAMERA_FACING_EXTRA,
                    CameraCharacteristics.LENS_FACING_FRONT
                ) // Tested on API 24 Android version 7.0(Samsung S6)
            }
            Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP_MR1 -> intent.putExtra(
                CAMERA_FACING_EXTRA,
                1
            ) // Tested API 21 Android version 5.0.1(Samsung S4)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        } else {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        }

        return intent
    }

    fun isCameraHardwareAvailable(context: Context): Boolean {
        return context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)
    }

    /**
     * Check if Camera app is available or not
     *
     * @return true if Camera app is Available else return false
     */
    fun isCameraAppAvailable(context: Context): Boolean {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        return intent.resolveActivity(context.packageManager) != null
    }
}

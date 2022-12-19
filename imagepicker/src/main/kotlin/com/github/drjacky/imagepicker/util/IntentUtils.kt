package com.github.drjacky.imagepicker.util

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.camera2.CameraCharacteristics
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.core.content.FileProvider
import androidx.documentfile.provider.DocumentFile
import com.github.drjacky.imagepicker.R
import java.io.File

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
    @JvmStatic
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
        val intent = Intent(Intent.ACTION_GET_CONTENT).applyImageTypes(mimeTypes)
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
    @JvmStatic
    fun getCameraIntent(uri: Uri, tryFrontCamera: Boolean): Intent {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        if (tryFrontCamera) {
            when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> {
                    intent.putExtra(
                        CAMERA_FACING_EXTRA,
                        CameraCharacteristics.LENS_FACING_FRONT
                    ) // Tested on API 27 Android version 8.0(Nexus 6P)
                    intent.putExtra(
                        "android.intent.extra.USE_FRONT_CAMERA",
                        true
                    ) // tested on android 11
                    intent.putExtra(
                        "android.intent.extras.CAMERA_FACING",
                        CameraCharacteristics.LENS_FACING_FRONT
                    ) // tested on android 11
                    intent.putExtra(
                        "android.intent.extras.CAMERA_FACING",
                        android.hardware.Camera.CameraInfo.CAMERA_FACING_FRONT
                    ) // tested on android 11
                    intent.putExtra(
                        "com.google.assistant.extra.USE_FRONT_CAMERA",
                        true
                    )
                    intent.putExtra(
                        "android.intent.extras.LENS_FACING_FRONT",
                        1
                    )
                    // Samsung
                    intent.putExtra("camerafacing", "front")
                    intent.putExtra("previous_mode", "front")
                    // Huawei
                    intent.putExtra("default_camera", "1")
                    intent.putExtra("default_mode", "com.huawei.camera2.mode.photo.PhotoMode")
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
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        } else {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        }

        return intent
    }

    @JvmStatic
    fun isCameraHardwareAvailable(context: Context): Boolean {
        return context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)
    }

    /**
     * Check if Camera app is available or not
     *
     * @return true if Camera app is Available else return false
     */
    @JvmStatic
    fun isCameraAppAvailable(context: Context): Boolean {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        return intent.resolveActivity(context.packageManager) != null
    }

    /**
     * Get Intent to View Uri backed File
     *
     * @param context
     * @param uri
     * @return Intent
     */
    @JvmStatic
    fun getUriViewIntent(context: Context, uri: Uri): Intent {
        val intent = Intent(Intent.ACTION_VIEW)
        val authority =
            context.packageName + context.getString(R.string.image_picker_provider_authority_suffix)

        val file = DocumentFile.fromSingleUri(context, uri)
        val dataUri = if (file?.canRead() == true) {
            uri
        } else {
            val filePath = FileUriUtils.getRealPath(context, uri)!!
            FileProvider.getUriForFile(context, authority, File(filePath))
        }

        intent.setDataAndType(dataUri, "image/*")
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

        return intent
    }
}

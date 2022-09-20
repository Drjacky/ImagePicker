package com.github.drjacky.imagepicker.provider

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.core.app.ActivityCompat.requestPermissions
import com.github.drjacky.imagepicker.ImagePickerActivity
import com.github.drjacky.imagepicker.R
import com.github.drjacky.imagepicker.util.FileUtil
import com.github.drjacky.imagepicker.util.IntentUtils
import com.github.drjacky.imagepicker.util.PermissionUtil
import java.io.File
import java.io.IOException

/**
 * Capture new image using camera
 *
 * @author Dhaval Patel
 * @version 1.0
 * @since 04 January 2019
 */
class CameraProvider(
    activity: ImagePickerActivity,
    private val tryFrontCamera: Boolean = false,
    private val launcher: (Intent) -> Unit
) :
    BaseProvider(activity) {

    companion object {
        /**
         * Key to Save/Retrieve Camera File state
         */
        private const val STATE_CAMERA_URI = "state.camera_uri"

        /**
         * Permission Require for Image Capture using Camera
         */
        private val REQUIRED_PERMISSIONS = arrayOf(
            Manifest.permission.CAMERA
        )

        private const val PERMISSION_INTENT_REQ_CODE = 4282
    }

    /**
     * Temp Camera File
     */
    private var mCameraUri: Uri? = null

    /**
     * Save CameraProvider state

     * mCameraFile will lose its state when activity is recreated on
     * Orientation change or for Low memory device.
     *
     * Here, We Will save its state for later use
     *
     * Note: To produce this scenario, enable "Don't keep activities" from developer options
     **/
    override fun onSaveInstanceState(outState: Bundle) {
        // Save Camera File
        outState.putParcelable(STATE_CAMERA_URI, mCameraUri)
    }

    /**
     * Retrieve CameraProvider state
     */
    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        // Restore Camera File
        mCameraUri = savedInstanceState?.getParcelable(STATE_CAMERA_URI) as Uri?
    }

    /**
     * Start Camera Capture Intent
     */
    fun startIntent() {
        if (!IntentUtils.isCameraAppAvailable(this)) {
            setError(R.string.error_camera_app_not_found)
            return
        }

        checkPermission()
    }

    /**
     * Check Require permission for Taking Picture.
     *
     * If permission is not granted request Permission, Else start Camera Intent
     */
    private fun checkPermission() {
        if (isPermissionGranted(this)) {
            // Permission Granted, Start Camera Intent
            startCameraIntent()
        } else {
            // Request Permission
            requestPermission()
        }
    }

    /**
     * Start Camera Intent
     *
     * Create Temporary File object and Pass it to Camera Intent
     */
    private fun startCameraIntent() {
        // Create and get empty file to store capture image content
        val uri = FileUtil.getImageUri(this)
        mCameraUri = uri

        // Check if file exists
        if (uri != null) {
            launcher.invoke(IntentUtils.getCameraIntent(uri, tryFrontCamera))
        } else {
            setError(R.string.error_failed_to_create_camera_image_file)
        }
    }

    /**
     * Handle Requested Permission Result
     */
    fun onRequestPermissionsResult(requestCode: Int) {
        if (requestCode == PERMISSION_INTENT_REQ_CODE) {
            // Check again if permission is granted
            if (isPermissionGranted(this)) {
                // Permission is granted, Start Camera Intent
                startCameraIntent()
            } else {
                // Exit with error message
                val errorRes = R.string.permission_camera_denied
                setError(getString(errorRes))
            }
        }
    }

    fun handleResult(result: ActivityResult) {
        if (result.resultCode == Activity.RESULT_OK) {
            try {
                activity.setImage(mCameraUri!!, isCamera = true)
            } catch (ex: IOException) {
                setError(R.string.error_failed_to_crop_image)
            }
        } else {
            setResultCancel()
        }
    }

    /**
     * Delete Camera file is exists
     */
    override fun onFailure() {
        delete()
    }

    /**
     * Delete Camera File, If not required
     *
     * After Camera Image Crop/Compress Original File will not required
     */
    fun delete() {
        mCameraUri?.path?.let {
            File(it).delete()
        }
        mCameraUri = null
    }

    /**
     * Request Runtime Permission required for Taking Pictures.
     *   Ref: https://github.com/Dhaval2404/ImagePicker/issues/34
     */
    private fun requestPermission() {
        requestPermissions(activity, getRequiredPermission(activity), PERMISSION_INTENT_REQ_CODE)
    }

    /**
     * Check if require permission granted for Taking Picture.
     *   Ref: https://github.com/Dhaval2404/ImagePicker/issues/34
     *
     * @param context Application Context
     * @return boolean true if all required permission granted else false.
     */
    private fun isPermissionGranted(context: Context): Boolean {
        return getRequiredPermission(context).none {
            !PermissionUtil.isPermissionGranted(context, it)
        }
    }

    /**
     * Check if permission Exists in Manifest
     *
     * @param context Application Context
     * @return Array<String> returns permission which are added in Manifest
     */
    private fun getRequiredPermission(context: Context): Array<String> {
        return REQUIRED_PERMISSIONS.filter {
            PermissionUtil.isPermissionInManifest(context, it)
        }.toTypedArray()
    }

}

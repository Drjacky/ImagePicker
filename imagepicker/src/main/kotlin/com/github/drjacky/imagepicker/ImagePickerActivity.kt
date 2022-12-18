package com.github.drjacky.imagepicker

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.addCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.github.drjacky.imagepicker.constant.ImageProvider
import com.github.drjacky.imagepicker.provider.CameraProvider
import com.github.drjacky.imagepicker.provider.CompressionProvider
import com.github.drjacky.imagepicker.provider.CropProvider
import com.github.drjacky.imagepicker.provider.GalleryProvider
import java.io.File
import java.io.IOException

/**
 * Pick Image
 *
 * @author Dhaval Patel
 * @version 1.0
 * @since 04 January 2019
 */
class ImagePickerActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "image_picker"

        /**
         * Key to Save/Retrieve Image File state
         */
        private const val STATE_IMAGE_URI = "state.image_uri"

        internal fun getCancelledIntent(context: Context): Intent {
            val intent = Intent()
            val message = context.getString(R.string.error_task_cancelled)
            intent.putExtra(ImagePicker.EXTRA_ERROR, message)
            return intent
        }
    }

    private lateinit var fileToCrop: java.util.ArrayList<Uri>
    var selectedNumberOfImages: Int = 0
    private var mGalleryProvider: GalleryProvider? = null
    private var mCameraProvider: CameraProvider? = null
    private var mCroppedImageList: ArrayList<Uri>? = null
    private lateinit var mCropProvider: CropProvider
    private lateinit var mCompressionProvider: CompressionProvider

    private val galleryLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            mGalleryProvider?.handleResult(it)
        }
    private val cameraLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            mCameraProvider?.handleResult(it)
        }
    private val cropLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            mCropProvider.handleResult(it)
        }

    /** Uri provided by GalleryProvider or CameraProvider */
    private var mImageUri: Uri? = null

    /** Uri provided by CropProvider */
    private var mCropUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        restoreInstanceState(savedInstanceState)
        loadBundle(savedInstanceState)

        onBackPressedDispatcher.addCallback(this) {
            // Back is pressed... Finishing the activity
            setResultCancel()
        }
    }

    /**
     * Restore saved state
     */
    private fun restoreInstanceState(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            mImageUri = savedInstanceState.getParcelable(STATE_IMAGE_URI) as Uri?
        }
    }

    /**
     * Save all appropriate activity state.
     */
    public override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelable(STATE_IMAGE_URI, mImageUri)
        mCameraProvider?.onSaveInstanceState(outState)
        mCropProvider.onSaveInstanceState(outState)
        super.onSaveInstanceState(outState)
    }

    /**
     * Parse Intent Bundle and initialize variables
     */
    private fun loadBundle(savedInstanceState: Bundle?) {
        // Create Crop Provider
        mCropProvider = CropProvider(this) { cropLauncher.launch(it) }
        mCropProvider.onRestoreInstanceState(savedInstanceState)

        // Create Compression Provider
        mCompressionProvider = CompressionProvider(this)
        mCroppedImageList = ArrayList()
        // Retrieve Image Provider

        // Create Gallery/Camera Provider
        when (intent?.getSerializableExtra(ImagePicker.EXTRA_IMAGE_PROVIDER) as ImageProvider?) {
            ImageProvider.GALLERY -> {
                mGalleryProvider = GalleryProvider(this) { galleryLauncher.launch(it) }
                // Pick Gallery Image
                savedInstanceState ?: mGalleryProvider?.startIntent()
            }
            ImageProvider.CAMERA -> {
                mCameraProvider = CameraProvider(this, false) { cameraLauncher.launch(it) }
                mCameraProvider?.onRestoreInstanceState(savedInstanceState)
                // Pick Camera Image
                savedInstanceState ?: mCameraProvider?.startIntent()
            }
            ImageProvider.FRONT_CAMERA -> {
                mCameraProvider = CameraProvider(this, true) { cameraLauncher.launch(it) }
                mCameraProvider?.onRestoreInstanceState(savedInstanceState)
                // Try Pick Front Camera Image
                savedInstanceState ?: mCameraProvider?.startIntent()
            }
            else -> {
                // Something went Wrong! This case should never happen
                Log.e(TAG, "Image provider can not be null")
                setError(getString(R.string.error_task_cancelled))
            }
        }
    }

    /**
     * Dispatch incoming result to the correct provider.
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        mCameraProvider?.onRequestPermissionsResult(requestCode)
        mGalleryProvider?.onRequestPermissionsResult(requestCode)
    }

    /**
     * {@link CameraProvider} and {@link GalleryProvider} Result will be available here.
     *
     * @param uri Capture/Gallery image file
     */
    @Throws(IOException::class)
    fun setImage(uri: Uri, isCamera: Boolean) {
        mImageUri = uri
        when {
            mCropProvider.isCropEnabled() -> mCropProvider.startIntent(
                uri = uri,
                cropOval = mCropProvider.isCropOvalEnabled(),
                cropFreeStyle = mCropProvider.isCropFreeStyleEnabled(),
                isCamera = isCamera,
                isMultipleFiles = false,
                outputFormat = mCropProvider.outputFormat()
            )
            mCompressionProvider.isResizeRequired(uri) -> mCompressionProvider.compress(
                uri = uri,
                outputFormat = mCropProvider.outputFormat()
            )
            else -> setResult(uri)
        }
    }

    fun setMultipleImage(fileList: ArrayList<Uri>) {
        this.fileToCrop = fileList

        if (!fileList.isNullOrEmpty()) {
            val file = fileList[0]
            setMultipleCropper(uri = file)
            try {
                fileList.remove(fileList[0])
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun setMultipleCropper(uri: Uri) {
        mImageUri = uri
        when {
            mCropProvider.isCropEnabled() -> mCropProvider.startIntent(
                uri = uri,
                cropOval = mCropProvider.isCropOvalEnabled(),
                cropFreeStyle = mCropProvider.isCropFreeStyleEnabled(),
                isCamera = false,
                isMultipleFiles = true,
                outputFormat = mCropProvider.outputFormat()
            )
            mCompressionProvider.isResizeRequired(uri) -> mCompressionProvider.compress(
                uri = uri,
                outputFormat = mCropProvider.outputFormat()
            )
        }
    }

    /**
     * {@link CropProviders} Result will be available here.
     *
     * Check if compression is enable/required. If yes then start compression else return result.
     *
     * @param uri Crop image file
     */
    fun setCropImage(uri: Uri) {
        mCropUri = uri

        mCameraProvider?.let {
            // Delete Camera file after crop. Else there will be two image for the same action.
            // In case of Gallery Provider, we will get original image path, so we will not delete that.
            uri.path?.let {
                // File(it).delete()
                // mCameraProvider.delete()
            }
            mImageUri = null
        }

        if (mCompressionProvider.isResizeRequired(uri)) {
            mCompressionProvider.compress(uri = uri, outputFormat = mCropProvider.outputFormat())
        } else {
            setResult(uri)
        }
    }

    fun setMultipleCropImage(uri: Uri) {
        mCroppedImageList?.add(uri)
        if (mCroppedImageList?.size == selectedNumberOfImages) {
            setMultipleImageResult(mCroppedImageList!!)
        } else {
            setMultipleImage(fileToCrop)
        }
    }

    /**
     * {@link CompressionProvider} Result will be available here.
     *
     * @param file Compressed image file
     */
    fun setCompressedImage(file: File) {
        // This is the case when Crop is not enabled
        mCameraProvider?.let {
            // Delete Camera file after Compress. Else there will be two image for the same action.
            // In case of Gallery Provider, we will get original image path, so we will not delete that.
            file.delete()
            // it.delete()
        }

        // If crop file is not null, Delete it after crop
        mCropUri?.path?.let {
            File(it).delete()
        }
        mCropUri = null

        setResult(mCropUri!!)
    }

    /**
     * Set Result, Image is successfully capture/picked/cropped/compressed.
     *
     * @param uri final image file
     */
    private fun setResult(uri: Uri) {
        val intent = Intent()
        intent.data = uri
        intent.putExtra(ImagePicker.EXTRA_FILE_PATH, uri.path)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    private fun setMultipleImageResult(uris: ArrayList<Uri>) {
        val intent = Intent()
        intent.putExtra(ImagePicker.MULTIPLE_FILES_PATH, uris)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    /**
     * User has cancelled the task
     */
    fun setResultCancel() {
        setResult(Activity.RESULT_CANCELED, getCancelledIntent(this))
        finish()
    }

    /**
     * Error occurred while processing image
     *
     * @param message Error Message
     */
    fun setError(message: String) {
        val intent = Intent()
        intent.putExtra(ImagePicker.EXTRA_ERROR, message)
        setResult(ImagePicker.RESULT_ERROR, intent)
        finish()
    }

}

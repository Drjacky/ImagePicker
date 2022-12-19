package com.github.drjacky.imagepicker.provider

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import androidx.activity.result.ActivityResult
import androidx.annotation.Nullable
import com.github.drjacky.imagepicker.ImagePicker
import com.github.drjacky.imagepicker.ImagePickerActivity
import com.github.drjacky.imagepicker.R
import com.github.drjacky.imagepicker.util.FileUriUtils
import com.github.drjacky.imagepicker.util.FileUtil.getCompressFormat
import com.yalantis.ucrop.UCrop
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

/**
 * Crop Selected/Captured Image
 *
 * @author Dhaval Patel
 * @version 1.0
 * @since 04 January 2019
 */
class CropProvider(activity: ImagePickerActivity, private val launcher: (Intent) -> Unit) :
    BaseProvider(activity) {

    companion object {
        private val TAG = CropProvider::class.java.simpleName
        private const val STATE_CROP_URI = "state.crop_uri"
    }

    private var isMultipleFiles: Boolean = false
    private val maxWidth: Int
    private val maxHeight: Int

    private val cropOval: Boolean
    private val cropFreeStyle: Boolean
    private val crop: Boolean
    private val cropAspectX: Float
    private val cropAspectY: Float
    private val outputFormat: Bitmap.CompressFormat?

    private var cropImageUri: Uri? = null

    init {
        with(activity.intent.extras ?: Bundle()) {
            maxWidth = getInt(ImagePicker.EXTRA_MAX_WIDTH, 0)
            maxHeight = getInt(ImagePicker.EXTRA_MAX_HEIGHT, 0)
            crop = getBoolean(ImagePicker.EXTRA_CROP, false)
            cropOval = getBoolean(ImagePicker.EXTRA_CROP_OVAL, false)
            cropFreeStyle = getBoolean(ImagePicker.EXTRA_CROP_FREE_STYLE, false)
            cropAspectX = getFloat(ImagePicker.EXTRA_CROP_X, 0f)
            cropAspectY = getFloat(ImagePicker.EXTRA_CROP_Y, 0f)
            outputFormat = this.get(ImagePicker.EXTRA_OUTPUT_FORMAT) as? Bitmap.CompressFormat
        }
    }

    /**
     * Save CameraProvider state
     *
     * mCropImageFile will lose its state when activity is recreated on
     * Orientation change or for Low memory device.
     *
     * Here, We Will save its state for later use
     *
     * Note: To produce this scenario, enable "Don't keep activities" from developer options
     */
    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelable(STATE_CROP_URI, cropImageUri)
    }

    /**
     * Retrieve CropProvider state
     */
    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        cropImageUri = savedInstanceState?.getParcelable(STATE_CROP_URI) as Uri?
    }

    /**
     * Check if it is allow dimmed layer to have a circle inside or not
     *
     * @return Boolean. True if it is allow dimmed layer to have a circle inside else false.
     */
    fun isCropOvalEnabled() = cropOval

    /**
     * Set to true to let user resize crop bounds (disabled by default)
     */
    fun isCropFreeStyleEnabled() = cropFreeStyle

    /**
     * Check if crop should be enabled or not
     *
     * @return Boolean. True if Crop should be enabled else false.
     */
    fun isCropEnabled() = crop

    /**
     * Get the output format if it has been set
     *
     * @return Bitmap.CompressFormat?. In case of Null, it will use the extension from the input file
     */
    @Nullable
    fun outputFormat() = outputFormat

    /**
     * Start Crop Activity
     */
    @Throws(IOException::class)
    fun startIntent(
        uri: Uri,
        cropOval: Boolean,
        cropFreeStyle: Boolean,
        isCamera: Boolean,
        isMultipleFiles: Boolean,
        outputFormat: Bitmap.CompressFormat?
    ) {
        this.isMultipleFiles = isMultipleFiles
        cropImage(
            uri = uri,
            cropOval = cropOval,
            cropFreeStyle = cropFreeStyle,
            isCamera = isCamera,
            outputFormat = outputFormat
        )
    }

    /**
     * @param uri Image File to be cropped
     * @throws IOException if failed to crop image
     */
    @Throws(IOException::class)
    private fun cropImage(
        uri: Uri,
        cropOval: Boolean,
        cropFreeStyle: Boolean,
        isCamera: Boolean,
        outputFormat: Bitmap.CompressFormat?
    ) {
        val path = if (isCamera) {
            Environment.DIRECTORY_DCIM
        } else {
            Environment.DIRECTORY_PICTURES
        }
        val extension =
            outputFormat?.let { ".${it.name}" } ?: FileUriUtils.getImageExtension(baseContext, uri)
        cropImageUri = uri

        // Later we will use this bitmap to create the File.
        val selectedBitmap: Bitmap? = getBitmap(this, uri)
        selectedBitmap?.let {
            // We can access getExternalFilesDir() without asking any storage permission.
            val selectedImgFile = File(
                getExternalFilesDir(path),
                System.currentTimeMillis().toString() + "_selectedImg" + extension
            )

            convertBitmapToFile(selectedImgFile, it, extension)

            /*We have to again create a new file where we will save the cropped image. */
            val croppedImgFile = File(
                getExternalFilesDir(path),
                System.currentTimeMillis().toString() + "_croppedImg" + extension
            )

            val options = UCrop.Options()
            options.setCompressionFormat(getCompressFormat(extension))
            options.setCircleDimmedLayer(cropOval)
            options.setFreeStyleCropEnabled(cropFreeStyle)
            val uCrop = UCrop.of(Uri.fromFile(selectedImgFile), Uri.fromFile(croppedImgFile))
                .withOptions(options)

            if (cropAspectX > 0 && cropAspectY > 0) {
                uCrop.withAspectRatio(cropAspectX, cropAspectY)
            }

            if (maxWidth > 0 && maxHeight > 0) {
                uCrop.withMaxResultSize(maxWidth, maxHeight)
            }

            launcher.invoke(uCrop.getIntent(activity))
        } ?: kotlin.run {
            setError(R.string.error_failed_to_crop_image)
        }
    }

    /**
     * This method will be called when final result fot this provider is enabled.
     */
    fun handleResult(result: ActivityResult) {
        if (result.resultCode == Activity.RESULT_OK) {
            val uri = UCrop.getOutput(result.data!!)
            if (uri != null) {
                if (isMultipleFiles) {
                    activity.setMultipleCropImage(uri)
                } else {
                    activity.setCropImage(uri)
                }
            } else {
                setError(R.string.error_failed_to_crop_image)
            }
        } else {
            setResultCancel()
        }
    }

    private fun getBitmap(context: Context, imageUri: Uri): Bitmap? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            try {
                ImageDecoder.decodeBitmap(
                    ImageDecoder.createSource(context.contentResolver, imageUri)
                )
            } catch (e: ImageDecoder.DecodeException) {
                null
            }
        } else {
            context
                .contentResolver
                .openInputStream(imageUri)?.use { inputStream ->
                    BitmapFactory.decodeStream(inputStream)
                }
        }
    }

    @Throws(IOException::class)
    private fun convertBitmapToFile(destinationFile: File, bitmap: Bitmap, extension: String) {
        destinationFile.createNewFile()
        val bos = ByteArrayOutputStream()
        bitmap.compress(getCompressFormat(extension), 50, bos)
        val bitmapData = bos.toByteArray()
        val fos = FileOutputStream(destinationFile)
        fos.write(bitmapData)
        fos.flush()
        fos.close()
    }

    /**
     * Delete Crop file is exists
     */
    override fun onFailure() {
        delete()
    }

    /**
     * Delete Crop File, If not required
     *
     * After Image Compression, Crop File will not required
     */
    fun delete() {
        cropImageUri?.path?.let {
            File(it).delete()
        }
        cropImageUri = null
    }

}

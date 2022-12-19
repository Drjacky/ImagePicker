package com.github.drjacky.imagepicker.provider

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.github.drjacky.imagepicker.ImagePicker
import com.github.drjacky.imagepicker.ImagePickerActivity
import com.github.drjacky.imagepicker.util.ExifDataCopier
import com.github.drjacky.imagepicker.util.FileUriUtils
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

/**
 * Compress Selected/Captured Image
 *
 * @author Dhaval Patel
 * @version 1.0
 * @since 04 January 2019
 */
class CompressionProvider(activity: ImagePickerActivity) : BaseProvider(activity) {

    private val maxWidth: Int
    private val maxHeight: Int
    private val keepRatio: Boolean

    init {
        with(activity.intent.extras ?: Bundle()) {
            maxWidth = getInt(ImagePicker.EXTRA_MAX_WIDTH, 0)
            maxHeight = getInt(ImagePicker.EXTRA_MAX_HEIGHT, 0)
            keepRatio = getBoolean(ImagePicker.EXTRA_KEEP_RATIO, false)
        }
    }

    /**
     * Check if compression is required
     * @param uri File object to apply Compression
     */
    fun isResizeRequired(uri: Uri): Boolean {
        return if (maxWidth > 0 && maxHeight > 0) {
            val sizes = getImageSize(uri)
            sizes[0] > maxWidth || sizes[1] > maxHeight
        } else {
            false
        }
    }

    /**
     * Compress given file if enabled.
     *
     * @param uri File to compress
     */
    fun compress(uri: Uri, outputFormat: Bitmap.CompressFormat?) {
        activity.lifecycleScope.launch {
            val res = compressTask(uri, outputFormat)
            if (res != null) {
                ExifDataCopier.copyExif(uri, res)
                activity.setCompressedImage(res)
            } else {
                setError(com.github.drjacky.imagepicker.R.string.error_failed_to_compress_image)
            }
        }
    }

    private fun compressTask(uri: Uri, outputFormat: Bitmap.CompressFormat?): File? {
        var bitmap = BitmapFactory.decodeFile(uri.path, BitmapFactory.Options())
        if (maxWidth > 0L && maxHeight > 0L) {
            // resize if desired
            bitmap = if ((bitmap.width > maxWidth || bitmap.height > maxHeight) && keepRatio) {
                var width = maxWidth
                var height = maxHeight
                if (bitmap.width > bitmap.height) {
                    val ratio = bitmap.width.toFloat() / maxWidth
                    width = maxWidth
                    height = (bitmap.height / ratio).toInt()
                } else if (bitmap.height > bitmap.width) {
                    val ratio = bitmap.height.toFloat() / maxHeight
                    height = maxHeight
                    width = (bitmap.width / ratio).toInt()
                }
                Bitmap.createScaledBitmap(bitmap, width, height, true)
            } else {
                Bitmap.createScaledBitmap(bitmap, maxWidth, maxHeight, true)
            }
        }

        val format = outputFormat ?: FileUriUtils.getImageExtensionFormat(baseContext, uri)
        var out: FileOutputStream? = null
        return try {
            val temp = "temp.${format.name}"
            val tempPath = activity.filesDir.toString() + "/$temp"
            with(File(tempPath)) {
                if (exists()) {
                    delete()
                }
            }
            out = activity.openFileOutput(temp, Context.MODE_PRIVATE)
            bitmap.compress(format, 100, out)
            File(tempPath)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        } finally {
            out?.flush()
            out?.close()
        }
    }

    /**
     *
     * @param uri File to get Image Size
     * @return Int Array, Index 0 has width and Index 1 has height
     */
    private fun getImageSize(uri: Uri): IntArray {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
//        BitmapFactory.decodeFile(uri.path, options)
        BitmapFactory.decodeStream(contentResolver.openInputStream(uri), null, options)
        return intArrayOf(options.outWidth, options.outHeight)
    }
}

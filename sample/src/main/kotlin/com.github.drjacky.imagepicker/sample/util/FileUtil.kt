package com.github.drjacky.imagepicker.sample.util

import android.content.Context
import android.net.Uri
import com.github.drjacky.imagepicker.util.FileUriUtils
import com.github.drjacky.imagepicker.util.FileUtil.getDocumentFile
import com.github.drjacky.imagepicker.util.FileUtil.getImageResolution
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

/**
 * File Utility
 *
 * @author Dhaval Patel
 * @version 1.6
 * @since 05 January 2019
 */
object FileUtil {

    /**
     * @param context Context
     * @param uri Uri
     * @return Image Info
     */
    @JvmStatic
    fun getFileInfo(context: Context, uri: Uri?): String {
        if (uri == null) {
            return "Image not found"
        }

        // Get Resolution
        val resolution = getImageResolution(context, uri)

        // File Path
        val filePath = FileUriUtils.getRealPath(context, uri)
        val document = getDocumentFile(context, uri) ?: return "Image not found"

        // Get Last Modified
        val sdf = SimpleDateFormat("dd/MM/yyyy hh:mm:ss a", Locale.getDefault())
        val modified = sdf.format(document.lastModified())

        // File Size
        val fileSize = getFileSize(document.length())

        return StringBuilder()

            .append("Resolution: ")
            .append("${resolution.first}x${resolution.second}")
            .append("\n\n")

            .append("Modified: ")
            .append(modified)
            .append("\n\n")

            .append("File Size: ")
            .append(fileSize)
            .append("\n\n")

            /*.append("File Name: ")
            .append(getFileName(context.contentResolver, uri))
            .append("\n\n")*/

            .append("File Path: ")
            .append(filePath)
            .append("\n\n")

            .append("Uri Path: ")
            .append(uri.toString())
            .toString()
    }

    /**
     * @param file File
     * @return Image Info
     */
    @JvmStatic
    fun getFileInfo(file: File?): String {
        if (file == null || !file.exists()) {
            return "Image not found"
        }

        val resolution = getImageResolution(file)
        val sdf = SimpleDateFormat("dd/MM/yyyy hh:mm:ss a", Locale.getDefault())
        val modified = sdf.format(file.lastModified())
        return StringBuilder()

            .append("Resolution: ")
            .append("${resolution.first}x${resolution.second}")
            .append("\n\n")

            .append("Modified: ")
            .append(modified)
            .append("\n\n")

            .append("File Size: ")
            .append(getFileSize(file))
            .append("\n\n")

            .append("File Path: ")
            .append(file.absolutePath)
            .toString()
    }

    private fun getFileSize(file: File): String {
        val fileSize = file.length().toFloat()
        val mb = fileSize / (1024 * 1024)
        val kb = fileSize / (1024)

        return if (mb > 1) {
            "$mb MB"
        } else {
            "$kb KB"
        }
    }

    private fun getFileSize(fileSize: Long): String {
        val mb = fileSize / (1024 * 1024)
        val kb = fileSize / (1024)

        return if (mb > 1) {
            "$mb MB"
        } else {
            "$kb KB"
        }
    }
}

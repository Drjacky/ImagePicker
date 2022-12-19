package com.github.drjacky.imagepicker

import android.net.Uri
import androidx.test.platform.app.InstrumentationRegistry
import com.github.drjacky.imagepicker.util.FileUriUtils
import org.junit.Assert.assertEquals
import org.junit.Test

class FileUriUtilsTests {
    @Test
    fun getImageExtension_AndroidPhotoPickerPath_returnsJpg() {
        val uri =
            Uri.parse("content://media/picker/0/com.android.providers.media.photopicker/media/1000000033")
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext

        val extension = FileUriUtils.getImageExtension(appContext, uri)

        assertEquals(".jpg", extension)
    }
}

package com.github.drjacky.imagepicker.sample

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.github.drjacky.imagepicker.ImagePicker
import com.github.drjacky.imagepicker.sample.util.FileUtil
import com.github.drjacky.imagepicker.sample.util.IntentUtil
import com.github.drjacky.imagepicker.util.IntentUtils
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_camera_only.*
import kotlinx.android.synthetic.main.content_gallery_only.*
import kotlinx.android.synthetic.main.content_profile.*

class MainActivity : AppCompatActivity() {

    companion object {
        private const val GITHUB_REPOSITORY = "https://github.com/drjacky/ImagePicker"
    }

    private var mCameraUri: Uri? = null
    private var mGalleryUri: Uri? = null
    private var mProfileUri: Uri? = null

    private val profileLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val uri = it.data?.data!!
                mProfileUri = uri
                imgProfile.setLocalImage(uri, true)
            } else parseError(it)
        }
    private val galleryLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val uri = it.data?.data!!
                mGalleryUri = uri
                imgGallery.setLocalImage(uri)
            } else parseError(it)
        }

    private val cameraLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val uri = it.data?.data!!
                mCameraUri = uri
                imgCamera.setLocalImage(uri, false)
            } else parseError(it)
        }

    private fun parseError(activityResult: ActivityResult) {
        if (activityResult.resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(activityResult.data), Toast.LENGTH_SHORT)
                .show()
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        imgProfile.setDrawableImage(R.drawable.ic_person, true)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_github -> {
                IntentUtil.openURL(this, GITHUB_REPOSITORY)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun pickProfileImage(view: View) {
        ImagePicker.with(this)
            .crop()
            .cropOval()
            .maxResultSize(512, 512, true)
            .createIntentFromDialog { profileLauncher.launch(it) }
    }

    fun pickGalleryImage(view: View) {
        galleryLauncher.launch(
            ImagePicker.with(this)
                .crop()
                .galleryOnly()
                .cropFreeStyle()
                .galleryMimeTypes( // no gif images at all
                    mimeTypes = arrayOf(
                        "image/png",
                        "image/jpg",
                        "image/jpeg"
                    )
                )
                .createIntent()
        )
    }

    fun pickCameraImage(view: View) {
        cameraLauncher.launch(
            ImagePicker.with(this)
                .crop()
                .cameraOnly()
                .maxResultSize(1080, 1920, true)
                .createIntent()
        )
    }

    fun showImage(view: View) {
        val uri = when (view) {
            imgProfile -> mProfileUri
            imgCamera -> mCameraUri
            imgGallery -> mGalleryUri
            else -> null
        }

        uri?.let {
            startActivity(IntentUtils.getUriViewIntent(this, uri))
        }
    }

    fun showImageInfo(view: View) {
        val uri = when (view) {
            imgProfileInfo -> mProfileUri
            imgCameraInfo -> mCameraUri
            imgGalleryInfo -> mGalleryUri
            else -> null
        }

        AlertDialog.Builder(this)
            .setTitle("Image Info")
            .setMessage(FileUtil.getFileInfo(this, uri))
            .setPositiveButton("Ok", null)
            .show()
    }
}

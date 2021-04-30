# 📸Image Picker Library for Android

[![Releases](https://img.shields.io/github/release/drjacky/imagePicker/all.svg?style=flat-square)](https://github.com/drjacky/ImagePicker/releases)
[![API](https://img.shields.io/badge/API-19%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=19)
![Language](https://img.shields.io/badge/language-Kotlin-orange.svg)
[![PRWelcome](https://img.shields.io/badge/PRs-welcome-brightgreen.svg)](https://github.com/Drjacky/ImagePicker/pulls)
[![FOSSA Status](https://app.fossa.com/api/projects/git%2Bgithub.com%2FDrjacky%2FImagePicker.svg?type=shield)](https://app.fossa.com/projects/git%2Bgithub.com%2FDrjacky%2FImagePicker?ref=badge_shield)
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-ImagePicker-green.svg?style=flat)](https://android-arsenal.com/details/1/8208)

Easy to use and configurable library to **Pick an image from the Gallery or Capture image using Camera**. It also allows to **Crop and Compresses the Image based on Aspect Ratio, Resolution and Image Size**.

## ‍🏍Features

* Pick Gallery Image
* Pick Image from Google Drive
* Capture Camera Image
* Crop Image(Crop image based on provided aspect ratio or let user choose one)
* Compress Image(Compress image based on provided resolution and size)
* Retrieve Image Result as File, File Path as String or Uri object
* Handle Runtime Permission for Camera and Storage

## 🎬Preview


   Profile Image Picker    |         Gallery Only      |       Camera Only        |
:-------------------------:|:-------------------------:|:-------------------------:
![](https://github.com/Drjacky/ImagePicker/blob/master/art/imagepicker_profile_demo.gif)  |  ![](https://github.com/Drjacky/ImagePicker/blob/master/art/imagepicker_gallery_demo.gif.gif)  |  ![](https://github.com/Drjacky/ImagePicker/blob/master/art/imagepicker_camera_demo.gif.gif)

## 💻Usage


1. Gradle dependency:

	```groovy
	allprojects {
	   repositories {
	      	mavenCentral() // For ImagePicker library, this line is enough. Although, it has been published on jitpack as well
           	maven { url "https://jitpack.io" }  //Make sure to add this in your project for uCrop - an internal library
	   }
	}
	```

    ```groovy
   implementation 'com.github.Drjacky:ImagePicker:$libVersion'
    ```
    Where `$libVersion` = [![libVersion](https://img.shields.io/github/release/drjacky/imagePicker/all.svg?style=flat-square)](https://github.com/drjacky/ImagePicker/releases)

    **If you want to get the activity result:**
   ```kotlin
   private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
       if (it.resultCode == Activity.RESULT_OK) {
           //you're business logic
           }
       }

    //If you want both Camera and Gallery
    ImagePicker.with(this)
       //...
       .createIntentFromDialog { launcher.launch(it) }

    //If you want just one option
    launcher.launch(
       ImagePicker.with(this)
           //...
           .cameraOnly() // or galleryOnly()
           .createIntent()
    )
    ```

    To use camera in Android 11 ([package visibility](https://developer.android.com/about/versions/11/privacy/package-visibility)) don't forget to add this:
    ```xml
    <manifest package="com.example">
       <queries>
           <intent>
               <action android:name="android.media.action.IMAGE_CAPTURE" />
           </intent>
       </queries>
       ...
    </manifest>
    ```

2. The ImagePicker configuration is created using the builder pattern.

	**Kotlin**
    
	```kotlin
    ImagePicker.with(this)
            .crop()	    			//Crop image(Optional), Check Customization for more option
            .cropOval()	    		//Allow dimmed layer to have a circle inside
            .compress(1024)			//Final image size will be less than 1 MB(Optional)
            .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
            .start()
    ```
    
    **Java**
    
    ```kotlin
    ImagePicker.Companion.with(this)
            .crop()	    			//Crop image(Optional), Check Customization for more option
            .cropOval()	    		//Allow dimmed layer to have a circle inside
            .compress(1024)			//Final image size will be less than 1 MB(Optional)
            .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
            .start()
    ```
    
3. Handling results

    
    **Default method(Preferred way)**<br>
    Override `onActivityResult` method and handle ImagePicker result.

    ```kotlin
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
         super.onActivityResult(requestCode, resultCode, data)
         if (resultCode == Activity.RESULT_OK) {
             //Image Uri will not be null for RESULT_OK
             val fileUri = data?.data
             imgProfile.setImageURI(fileUri)
          
            //You can get File object from intent
            val file:File = ImagePicker.getFile(data)!!
           
            //You can also get File Path from intent
            val filePath:String = ImagePicker.getFilePath(data)!!
         } else if (resultCode == ImagePicker.RESULT_ERROR) {
             Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
         } else {
             Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
         }
    }
    ```

    **Inline method (with InlineActivityResult library, Only Works with FragmentActivity and AppCompatActivity) (Not to be used with crop. See [#32](https://github.com/Dhaval2404/ImagePicker/issues/32))**
    
    ```kotlin
    ImagePicker.with(this)
            .compress(1024)         //Final image size will be less than 1 MB(Optional)
            .maxResultSize(1080, 1080)  //Final image resolution will be less than 1080 x 1080(Optional)
            .start { resultCode, data ->
                if (resultCode == Activity.RESULT_OK) {
                    //Image Uri will not be null for RESULT_OK
                     val fileUri = data?.data
                     imgProfile.setImageURI(fileUri)
                  
                    //You can get File object from intent
                    val file:File = ImagePicker.getFile(data)
                   
                    //You can also get File Path from intent
                    val filePath:String = ImagePicker.getFilePath(data)
                } else if (resultCode == ImagePicker.RESULT_ERROR) {
                    Toast.makeText(context, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Task Cancelled", Toast.LENGTH_SHORT).show()
                }
            }
    ```

## 🎨Customization

 *  Pick image using Gallery

	```kotlin
	ImagePicker.with(this)
		.galleryOnly()	//User can only select image from Gallery
		.start()	//Default Request Code is ImagePicker.REQUEST_CODE
    ```

 *  Capture image using Camera

	```kotlin
	ImagePicker.with(this)
		.cameraOnly()	//User can only capture image using Camera
		.start()
    ```
 *  Crop image
 		
    ```kotlin
    ImagePicker.with(this)
		.crop()	    //Crop image and let user choose aspect ratio.
		.start()
	```
 *  Crop image with fixed Aspect Ratio

    ```kotlin
    ImagePicker.with(this)
		.crop(16f, 9f)	//Crop image with 16:9 aspect ratio
		.start()
    ```
 *  Crop square image(e.g for profile)

     ```kotlin
     ImagePicker.with(this)
         .cropSquare()	//Crop square image, its same as crop(1f, 1f)
         .start()
    ```
 *  Compress image size(e.g image should be maximum 1 MB)

	```kotlin
    ImagePicker.with(this)
		.compress(1024)	//Final image size will be less than 1 MB
		.start()
    ```
 *  Set Resize image resolution

    ```kotlin
    ImagePicker.with(this)
		.maxResultSize(620, 620)	//Final image resolution will be less than 620 x 620
		.start()
    ```
 *  Intercept ImageProvider, Can be used for analytics

    ```kotlin
    ImagePicker.with(this)
        .setImageProviderInterceptor { imageProvider -> //Intercept ImageProvider
            Log.d("ImagePicker", "Selected ImageProvider: "+imageProvider.name)
        }
        .start()
    ```
 *  Intercept Dialog dismiss event

	```kotlin
    ImagePicker.with(this)
    	.setDismissListener {
    		// Handle dismiss event
    		Log.d("ImagePicker", "onDismiss");
    	}
    	.start()
    ```

 *  Specify Directory to store captured, cropped or compressed images

    ```kotlin
    ImagePicker.with(this)
        //Provide directory path to save images
        .saveDir(File(Environment.getExternalStorageDirectory(), "ImagePicker"))
        // .saveDir(Environment.getExternalStorageDirectory())
        // .saveDir(getExternalFilesDir(null)!!)
        .start()
    ```

 *  Limit MIME types while choosing a gallery image

    ```kotlin
    ImagePicker.with(this)
        .galleryMimeTypes(  //Exclude gif images
            mimeTypes = arrayOf(
              "image/png",
              "image/jpg",
              "image/jpeg"
            )
          )
        .start()
    ```

 *  You can also specify the request code with ImagePicker

    ```kotlin
    ImagePicker.with(this)
		.maxResultSize(620, 620)
		.start(101)	//Here 101 is request code, you may use this in onActivityResult
    ```

 *  Add Following parameters in your **colors.xml** file, If you want to customize uCrop Activity.

    ```xml
    <resources>
        <!-- Here you can add color of your choice  -->
        <color name="ucrop_color_toolbar">@color/teal_500</color>
        <color name="ucrop_color_statusbar">@color/teal_700</color>
        <color name="ucrop_color_widget_active">@color/teal_500</color>
    </resources>
    ```

## 💥Compatibility

  * Library - Android Kitkat 4.4+ (API 19)
  * Sample - Android Kitkat 4.4+ (API 19)

## 📃 Libraries Used
* uCrop [https://github.com/Yalantis/uCrop](https://github.com/Yalantis/uCrop)
* Compressor [https://github.com/zetbaitsu/Compressor](https://github.com/zetbaitsu/Compressor)
* ImagePicker [https://github.com/Dhaval2404/ImagePicker](https://github.com/Dhaval2404/ImagePicker) which my work is based on this repository.

## License

    Copyright 2019, The Android Open Source Project

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

         http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

[![FOSSA Status](https://app.fossa.com/api/projects/git%2Bgithub.com%2FDrjacky%2FImagePicker.svg?type=large)](https://app.fossa.com/projects/git%2Bgithub.com%2FDrjacky%2FImagePicker?ref=badge_large)
# 📸Image Picker Library for Android

[![Releases](https://img.shields.io/github/release/drjacky/imagePicker/all.svg?style=flat-square)](https://github.com/drjacky/ImagePicker/releases)
[![API](https://img.shields.io/badge/API-19%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=19)
![Language](https://img.shields.io/badge/language-Kotlin-orange.svg)
[![PRWelcome](https://img.shields.io/badge/PRs-welcome-brightgreen.svg)](https://github.com/Drjacky/ImagePicker/pulls)

Easy to use and configurable library to **Pick an image from the Gallery or Capture image using Camera**. It also allows to **Crop and Compresses the Image based on Aspect Ratio, Resolution and Image Size**.

## ‍🏍Features

* Pick Gallery Image
* Pick Image from Google Drive
* Capture Camera Image
* Crop Image(Crop image based on provided aspect ratio or let user choose one)
* Compress Image(Compress image based on provided resolution and size)
* Retrive Image Result as File, File Path as String or Uri object
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
	      	jcenter() // For ImagePicker library, this lien is enough. Although, it has been published on jitpack as well
           	maven { url "https://jitpack.io" }  //Make sure to add this in your project for uCrop - an internal library
	   }
	}
	```

    ```groovy
   implementation 'com.github.Drjacky:ImagePicker:$libVersion'
    ```
    Where [![libVersion](https://img.shields.io/github/release/drjacky/imagePicker/all.svg?style=flat-square)](https://github.com/drjacky/ImagePicker/releases)

    **If you want to get the activity result inline in a modern way (lambda) install [InlineActivityResult](https://github.com/florent37/InlineActivityResult) library:**
   ```groovy
   implementation 'com.github.florent37:inline-activity-result-kotlin:1.0.4'
    ```
    
2.  <span style="color:red">**If you target Android 10 or higher(targetSdkVersion >= 29)**</span>, set the value of ``requestLegacyExternalStorage`` to true in your app's manifest file:

      ```xml
    <manifest ... >
          <!-- This attribute is "false" by default on apps targeting
               Android 10 or higher. -->
          <application android:requestLegacyExternalStorage="true" ... >
            ...
          </application>
    </manifest>
      ```

3. The ImagePicker configuration is created using the builder pattern.

	**Kotlin**
    
	```kotlin
    ImagePicker.with(this)
            .crop()	    			//Crop image(Optional), Check Customization for more option
	        .cropOval()				//Allow dimmed layer to have a circle inside
            .compress(1024)			//Final image size will be less than 1 MB(Optional)
            .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
            .start()
    ```
    
    **Java**
    
    ```kotlin
    ImagePicker.Companion.with(this)
            .crop()	    			//Crop image(Optional), Check Customization for more option
	        .cropOval()				//Allow dimmed layer to have a circle inside
            .compress(1024)			//Final image size will be less than 1 MB(Optional)
            .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
            .start()
    ```
    
4. Handling results

    
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

## ✔️Changelog

### Version: 1.7

  * Added option to limit MIME types while choosing a gallery image (Special Thanks to [Marchuck](https://github.com/Marchuck))
  * Introduced ImageProviderInterceptor, Can be used for analytics (Special Thanks to [Marchuck](https://github.com/Marchuck))
  * Fixed .crop() opening gallery or camera twice [#32](https://github.com/Dhaval2404/ImagePicker/issues/32)
  * Fixed FileProvider of the library clashes with the FileProvider of the app [#51](https://github.com/Dhaval2404/ImagePicker/issues/51) (Special Thanks to [OyaCanli](https://github.com/OyaCanli))
  * Added option to set Storage Directory [#52](https://github.com/Dhaval2404/ImagePicker/issues/52)
  * Fixed NullPointerException in FileUriUtils.getPathFromRemoteUri()  [#61](https://github.com/Dhaval2404/ImagePicker/issues/61) (Special Thanks to [himphen](https://github.com/himphen))
  * Fixed UCropActivity Crash Android 4.4 (KiKat) [#82](https://github.com/Dhaval2404/ImagePicker/issues/82)
  * Fixed PNG image saved as JPG after crop issue [#94](https://github.com/Dhaval2404/ImagePicker/issues/94)
  * Fixed PNG image saved as JPG after compress issue [#105](https://github.com/Dhaval2404/ImagePicker/issues/105)
  * Added Polish text translation [#115](https://github.com/Dhaval2404/ImagePicker/issues/115) (Special Thanks to [MarcelKijanka](https://github.com/MarcelKijanka))
  * Failed to find configured root exception [#116](https://github.com/Dhaval2404/ImagePicker/issues/116)

### Version: 1.6

  * Improved UI/UX of sample app
  * Removed Bitmap Deprecated Property [#33](https://github.com/Dhaval2404/ImagePicker/issues/33) (Special Thanks to [nauhalf](https://github.com/nauhalf))
  * Camera opens twice when "Don't keep activities" option is ON [#41](https://github.com/Dhaval2404/ImagePicker/issues/41) (Special Thanks to [benji101](https://github.com/benji101))
  * Fixed uCrop Crash Issue [#42](https://github.com/Dhaval2404/ImagePicker/issues/42)

### Version: 1.5

  * Fixed app crash issue, due to Camera Permission in manifest [#34](https://github.com/Dhaval2404/ImagePicker/issues/34)
  * Added Option for Dynamic Crop Ratio. Let User choose aspect ratio [#36](https://github.com/Dhaval2404/ImagePicker/issues/36) (Special Thanks to [Dor-Sloim](https://github.com/Dor-Sloim))

### Version: 1.4

  * Optimized Uri to File Conversion (Inspired by [Flutter ImagePicker](https://github.com/flutter/plugins/tree/master/packages/image_picker))
  * Removed redundant CAMERA permission [#26](https://github.com/Dhaval2404/ImagePicker/issues/26) (Special Thanks to [PerrchicK](https://github.com/PerrchicK))

### Version: 1.3

  * Sample app made compatible with Android Kitkat 4.4+ (API 19)
  * Fixed Uri to File Conversion issue [#8](https://github.com/Dhaval2404/ImagePicker/issues/8) (Special Thanks to [squeeish](https://github.com/squeeish))

### Version: 1.2

  * Added Support for Inline Activity Result(Special Thanks to [soareseneves](https://github.com/soareseneves))
  * Fixed issue [#6](https://github.com/Dhaval2404/ImagePicker/issues/6)
  
### Version: 1.1

  * Optimized Compression Logic
  * Replace white screen with transparent one.

### Version: 1.0

  * Initial Build

## 📃 Libraries Used
* uCrop [https://github.com/Yalantis/uCrop](https://github.com/Yalantis/uCrop)
* Compressor [https://github.com/zetbaitsu/Compressor](https://github.com/zetbaitsu/Compressor)
* InlineActivityResult [https://github.com/florent37/InlineActivityResult](https://github.com/florent37/InlineActivityResult)
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
    

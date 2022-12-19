# 📸Image Picker Library for Android

[![Releases](https://img.shields.io/github/release/drjacky/imagePicker/all.svg?style=flat-square)](https://github.com/drjacky/ImagePicker/releases)
[![API](https://img.shields.io/badge/API-19%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=19)
![Language](https://img.shields.io/badge/language-Kotlin-orange.svg)
[![PRWelcome](https://img.shields.io/badge/PRs-welcome-brightgreen.svg)](https://github.com/Drjacky/ImagePicker/pulls)
[![FOSSA Status](https://app.fossa.com/api/projects/git%2Bgithub.com%2FDrjacky%2FImagePicker.svg?type=shield)](https://app.fossa.com/projects/git%2Bgithub.com%2FDrjacky%2FImagePicker?ref=badge_shield)
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-ImagePicker-green.svg?style=flat)](https://android-arsenal.com/details/1/8208)
![Language](https://img.shields.io/badge/Kotlin-1.7.21-blue)

Easy to use and configurable library to **Pick an image from the Gallery or Capture image using Camera**. It also allows to **Crop the Image based on Aspect Ratio, Resolution and Image Size**.

## ‍🏍Features

* Pick Gallery Images
* Pick Images from Google Drive
* Capture Camera Image
* Crop Images(Crop image based on provided aspect ratio or let user choose one)
* Compress Images(Compress image based on provided resolution and size)
* Retrieve Image Result as File, File Path as String or Uri object
* Handle Runtime Permission for Camera and Storage

## 🎬Preview


   Profile Image Picker    |         Gallery Only      |       Camera Only        |
:-------------------------:|:-------------------------:|:-------------------------:
![](https://github.com/Drjacky/ImagePicker/blob/master/art/imagepicker_profile_demo.gif)  |  ![](https://github.com/Drjacky/ImagePicker/blob/master/art/imagepicker_gallery_demo.gif)  |  ![](https://github.com/Drjacky/ImagePicker/blob/master/art/imagepicker_camera_demo.gif)

## 💻Usage


Gradle dependency:

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

Where `$libVersion`
= [![libVersion](https://img.shields.io/github/release/drjacky/imagePicker/all.svg?style=flat-square)](https://github.com/drjacky/ImagePicker/releases)

## 🎨Customization

**If you want to get the activity result:**

**Kotlin**

```kotlin
   private val launcher =
    registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            val uri = it.data?.data!!
            // Use the uri to load the image
            // Only if you are not using crop feature:
            uri?.let { galleryUri ->
                contentResolver.takePersistableUriPermission(
                    uri, Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            }
            //////////////
        }
    }
```

**Java**

```java
ActivityResultLauncher<Intent> launcher=
        registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),(ActivityResult result)->{
        if(result.getResultCode()==RESULT_OK){
        Uri uri=result.getData().getData();
        // Use the uri to load the image
        }else if(result.getResultCode()==ImagePicker.RESULT_ERROR){
        // Use ImagePicker.Companion.getError(result.getData()) to show an error
        }
        });
```

**Both Camera and Gallery:**

```kotlin
    ImagePicker.with(this)
    //...
    .provider(ImageProvider.BOTH) //Or bothCameraGallery()
    .createIntentFromDialog { launcher.launch(it) }
```

**Crop image:**

```kotlin
    .crop()
```

**Crop image with 16:9 aspect ratio:**

```kotlin
    .crop(16f, 9f)
```

**Crop square image(e.g for profile):**

```kotlin
    .cropSquare()    //Crop square image, its same as crop(1f, 1f)
```

**Oval crop image:**

```kotlin
    .crop()     
    .cropOval() //Allow dimmed layer to have a circle inside
```

**Set Max Width and Height of final image:**

```kotlin
    .maxResultSize(512, 512, true) //true: Keep Ratio
```

**Java sample for using `createIntentFromDialog`:**

```java
ImagePicker.Companion.with(this)
        .crop()
        .cropOval()
        .maxResultSize(512,512,true)
        .provider(ImageProvider.BOTH) //Or bothCameraGallery()
        .createIntentFromDialog((Function1)(new Function1(){
public Object invoke(Object var1){
        this.invoke((Intent)var1);
        return Unit.INSTANCE;
        }

public final void invoke(@NotNull Intent it){
        Intrinsics.checkNotNullParameter(it,"it");
        launcher.launch(it);
        }
        }));
```

**If you want just one option(camera or gallery):**

```kotlin
    launcher.launch(
       ImagePicker.with(this)
           //...
           .cameraOnly() // or galleryOnly()
           .createIntent()
    )
```

**Let the user to resize crop bounds:**

```kotlin
        .crop()                                                  
        .cropFreeStyle()
```

**Let the user to pick multiple files or single file in gallery mode:**

```kotlin
        .setMultipleAllowed(true)
```

**Let the user defines the output format:**

```kotlin
        .setOutputFormat(Bitmap.CompressFormat.WEBP)
```

**Intercept ImageProvider; could be used for analytics purposes:**

```kotlin
ImagePicker.with(this)
        .setImageProviderInterceptor { imageProvider -> //Intercept ImageProvider
            Log.d("ImagePicker", "Selected ImageProvider: "+imageProvider.name)
        }
        .createIntent()
```

**Intercept Dialog dismiss event:**

```kotlin
    ImagePicker.with(this)
    	.setDismissListener {
    		// Handle dismiss event
    		Log.d("ImagePicker", "onDismiss");
    	}
    	.createIntent()
```

**Limit MIME types while choosing a gallery image:**

```kotlin
        .galleryMimeTypes(  //Exclude gif images
                    mimeTypes = arrayOf(
                      "image/png",
                      "image/jpg",
                      "image/jpeg"
                    )
                  )
```

**Add Following parameters in your **colors.xml** file, if you want to customize uCrop Activity:**

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

[![FOSSA Status](https://app.fossa.com/api/projects/git%2Bgithub.com%2FDrjacky%2FImagePicker.svg?type=large)](https://app.fossa.com/projects/git%2Bgithub.com%2FDrjacky%2FImagePicker?ref=badge_large)
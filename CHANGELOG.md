# ✔️Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/), and this project
adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [2.3.22] - 2022-12-19

* NullPointerException in callback (Special Thanks to [Catlandor](https://github.com/Catlandor))
* Revert back from ACTION_OPEN_DOCUMENT to ACTION_GET_CONTENT

## [2.3.21] - 2022-12-19

* Migrate to Kotlin 1.7.21
* Update internal dependencies
* Fix FileNotFoundException due to not able to take extension (Special Thanks
  to [Catlandor](https://github.com/Catlandor))
* Revert back from ACTION_GET_CONTENT to ACTION_OPEN_DOCUMENT until
  this [issue](https://github.com/Yalantis/uCrop/issues/857) is resolved.
* Fix not getting image size correctly

## [2.3.20] - 2022-09-28

* Fix ktlint issue
* Format internal codes
* Update internal dependencies

## [2.3.19] - 2022-09-20

* Improve dismiss listener
* Update Java version to 11
* Support Android API 33

## [2.3.18] - 2022-09-05

* Fix crash on not setting the setOutputFormat (Special Thanks
  to [Hussein Habibi Juybari](https://github.com/Husseinhj))
* Add Intercept Dialog dismiss event
* Improve how to customize the library in the readme file

## [2.3.17] - 2022-08-23

* Migrate to Kotlin 1.7.10
* Update internal dependencies
* Let user pick multiple files in the gallery mode
* Let user define the output format

## [2.1.16] - 2022-06-22

* Improve document on how to provide both camera and gallery
* List other gallery apps on gallery choose mode
* Handle an exception on creating a new file while trying to crop the image
* Add Russian translation
* Add Italy(italian) translation

## [2.1.15] - 2021-11-16

* Update internal dependencies
* Improve support for front camera
* Target SDK 31

## [2.1.14] - 2021-10-20

* Migrate to Kotlin 1.5.31
* Update internal dependencies
  * Fix an issue if image is corrupted
  * Add Bengali translation
  * Add Dutch translation

## [2.1.13] - 2021-08-06

  * Migrate to Kotlin 1.5.21
  * Update internal dependencies
  * Fix typo in German translation
  * Add French translation

## [2.1.12] - 2021-06-14

  * Add free style crop option - cropFreeStyle()
  * Add Brizilian Portuguese translation
  * Add Russian translation
  * Update internal dependencies

## [2.1.11] - 2021-05-29

  * Migrate to Kotlin 1.5.10
  * Add Uzbekistan translation
  * Remove unused string resources
  * Update internal dependencies

## [2.1.10] - 2021-05-16

  * Fix false-positive file not found issue
  * Fix show image issue
  * Add jvm static annotations
  * Improve readme and the sample app

## [2.1.9] - 2021-05-15

  * Update readme
  * Some minor improvements
  * Add Norwegian translation

## [2.1.8] - 2021-05-11

  * Update readme
  * Migrate to Kotlin 1.5.0
  * Resolve ktlint warnings
  * Move necessary permissions into the library

## [2.1.7] - 2021-05-07

  * Fix permission issue on release mode
  * Remove Java 8 Desugering
  * Disable multidex
  * Update readme

## [2.1.6] - 2021-05-05

  * Fix select image issue on API 29
  * Fix capture image by camera on API 29
  * Use Uri instead of File
  * Enable multidex
  * Update gradle plugin to 4.2.0
  * Update gradle wrapper to 7.0
  * Add Java 8 Desugering
  * Improve readme file

## [2.0.6] - 2021-05-03

  * Update internal dependencies
  * Update demo pictures

## [2.0.5] - 2021-05-01

  * Fix pick the image from a sub-folder in gallery issue
  * FIx pick the image from download folder on Android API 29

## [2.0.4] - 2021-04-30

  * Refactor `CompressionProvider.kt`
  * `fun compress(maxSize: Int): Builder` has been removed
  * Refactor `Image maxResultSize`
  * `AsyncTask` replaced with Coroutines
  * `startActivityForResult` has been removed
  * `InlineActivityResult` dependency has been removed
  * New Android Activity Result API added
  * Add Arabic translation

## [1.8.4] - 2021-04-16

  * Add German translation
  * Add Swedish translation

## [1.8.3] - 2021-04-14

  * Some minor improvements
  * Update Gradle to 7.0
  * Add missing uCrop translation
  * Fix cannot access androidx.lifecycle.HasDefaultViewModelProviderFactory issue
  * Add italian translation

## [1.8.2] - 2021-04-10
  * Internal improvements
  * Use Kotlin v1.4.32
  * Update AGP to 1.4.3
  * Let developer set requestLegacyExternalStorage if needed

## [1.8.1] - 2021-03-17
  * Fixed camera capture not working
  * Decrease target sdk from 30 to 29
  * Rebasing

## [1.8] - 2020-12-22
### Added
  * Added dialog dismiss listener (Special Thanks to [kibotu](https://github.com/kibotu))
  * Added text localization (Special Thanks to [yamin8000](https://github.com/yamin8000) and Jose Bravo)

### Changed
  * Fixed crash issue on missing camera app [#69](https://github.com/Dhaval2404/ImagePicker/issues/69)
  * Fixed issue selecting images from download folder [#86](https://github.com/Dhaval2404/ImagePicker/issues/86)
  * Fixed exif information lost issue [#121](https://github.com/Dhaval2404/ImagePicker/issues/121)
  * Fixed crash issue on large image crop [#122](https://github.com/Dhaval2404/ImagePicker/issues/122)
  * Fixed saving image in cache issue [#127](https://github.com/Dhaval2404/ImagePicker/issues/127)

## [1.7.10] - 2020-11-23
### Changed
  * Update internal libraries
  * Use Kotlin v1.4.20

## [1.7.9] - 2020-11-22
### Changed
  * Add bintray

## [1.7.8] - 2020-11-20
### Changed
  * Increase external crop library version
  * Increase target sdk version
  * Add oval crop feature

## [1.7.7] - 2020-11-18
### Changed
  * Remove extra language
  * Update libraries version

## [1.7.6] - 2020-11-13
### Changed
  * Added front camera

## [1.7.5] - 2020-08-30
### Changed
  * Added Polish text translation [#115](https://github.com/Dhaval2404/ImagePicker/issues/115) (Special Thanks to [MarcelKijanka](https://github.com/MarcelKijanka))
  * Failed to find configured root exception [#116](https://github.com/Dhaval2404/ImagePicker/issues/116)

## [1.7.4] - 2020-08-02
### Changed
  * Fixed PNG image saved as JPG after compress issue [#105](https://github.com/Dhaval2404/ImagePicker/issues/105)

## [1.7.3] - 2020-07-18
### Changed
  * Fixed PNG image saved as JPG after crop issue [#94](https://github.com/Dhaval2404/ImagePicker/issues/94)

## [1.7.2] - 2020-07-14
### Changed
  * Fixed .crop() opening gallery or camera twice [#32](https://github.com/Dhaval2404/ImagePicker/issues/32)
  * Fixed UCropActivity Crash Android 4.4 (KiKat) [#82](https://github.com/Dhaval2404/ImagePicker/issues/82)

## [1.7.1] - 2020-03-26
### Changed
  * Fixed The application could not be installed: INSTALL_FAILED_CONFLICTING_PROVIDER issue [#67](https://github.com/Dhaval2404/ImagePicker/issues/67)

## [1.7] - 2020-03-23
### Changed
  * Added option to limit MIME types while choosing a gallery image (Special Thanks to [Marchuck](https://github.com/Marchuck))
  * Introduced ImageProviderInterceptor, Can be used for analytics (Special Thanks to [Marchuck](https://github.com/Marchuck))
  * Fixed FileProvider of the library clashes with the FileProvider of the app [#51](https://github.com/Dhaval2404/ImagePicker/issues/51) (Special Thanks to [OyaCanli](https://github.com/OyaCanli))
  * Added option to set Storage Directory [#52](https://github.com/Dhaval2404/ImagePicker/issues/52)
  * Fixed NullPointerException in FileUriUtils.getPathFromRemoteUri()  [#61](https://github.com/Dhaval2404/ImagePicker/issues/61) (Special Thanks to [himphen](https://github.com/himphen))

## [1.6] - 2020-01-06
### Changed
  * Improved UI/UX of sample app
  * Removed Bitmap Deprecated Property [#33](https://github.com/Dhaval2404/ImagePicker/issues/33) (Special Thanks to [nauhalf](https://github.com/nauhalf))
  * Camera opens twice when "Don't keep activities" option is ON [#41](https://github.com/Dhaval2404/ImagePicker/issues/41) (Special Thanks to [benji101](https://github.com/benji101))
  * Fixed uCrop Crash Issue [#42](https://github.com/Dhaval2404/ImagePicker/issues/42)

## [1.5] - 2019-10-14
### Added
  * Added Option for Dynamic Crop Ratio. Let User choose aspect ratio [#36](https://github.com/Dhaval2404/ImagePicker/issues/36) (Special Thanks to [Dor-Sloim](https://github.com/Dor-Sloim))
### Changed
  * Fixed app crash issue, due to Camera Permission in manifest [#34](https://github.com/Dhaval2404/ImagePicker/issues/34)

## [1.4] - 2019-09-03
### Changed
  * Optimized Uri to File Conversion (Inspired by [Flutter ImagePicker](https://github.com/flutter/plugins/tree/master/packages/image_picker))
### Removed
  * Removed redundant CAMERA permission [#26](https://github.com/Dhaval2404/ImagePicker/issues/26) (Special Thanks to [PerrchicK](https://github.com/PerrchicK))

## [1.3] - 2019-07-24
### Added
  * Sample app made compatible with Android Kitkat 4.4+ (API 19)
### Changed
  * Fixed Uri to File Conversion issue [#8](https://github.com/Dhaval2404/ImagePicker/issues/8) (Special Thanks to [squeeish](https://github.com/squeeish))

## [1.2] - 2019-05-13
### Added
  * Added Support for Inline Activity Result(Special Thanks to [soareseneves](https://github.com/soareseneves))
### Changed
  * Fixed issue [#6](https://github.com/Dhaval2404/ImagePicker/issues/6)

## [1.1] - 2019-04-02
### Changed
  * Optimized Compression Logic
  * Replace white screen with transparent one.

## [1.0] - 2019-02-11

### Added

* Pick Gallery Image
* Capture Camera Image
* Crop Image(Its based on [uCrop](https://github.com/Yalantis/uCrop))
* Compress Image(Compress image based on resolution and size)
* Handle Runtime Permission for Camera and Storage
* Retrieve Image Result as File, File Path as String or Uri object

[Unreleased]: https://github.com/Drjacky/ImagePicker/compare/v2.3.22...HEAD

[2.3.22]: https://github.com/Drjacky/ImagePicker/compare/v2.1.21...v2.3.22

[2.3.21]: https://github.com/Drjacky/ImagePicker/compare/v2.1.20...v2.3.21

[2.3.20]: https://github.com/Drjacky/ImagePicker/compare/v2.1.19...v2.3.20

[2.3.19]: https://github.com/Drjacky/ImagePicker/compare/v2.1.18...v2.3.19

[2.3.18]: https://github.com/Drjacky/ImagePicker/compare/v2.1.17...v2.3.18

[2.3.17]: https://github.com/Drjacky/ImagePicker/compare/v2.1.16...v2.3.17

[2.1.16]: https://github.com/Drjacky/ImagePicker/compare/v2.1.15...v2.1.16

[2.1.15]: https://github.com/Drjacky/ImagePicker/compare/v2.1.14...v2.1.15

[2.1.14]: https://github.com/Drjacky/ImagePicker/compare/v2.1.13...v2.1.14

[2.1.13]: https://github.com/Drjacky/ImagePicker/compare/v2.1.12...v2.1.13

[2.1.12]: https://github.com/Drjacky/ImagePicker/compare/v2.1.11...v2.1.12

[2.1.11]: https://github.com/Drjacky/ImagePicker/compare/v2.1.10...v2.1.11

[2.1.10]: https://github.com/Drjacky/ImagePicker/compare/v2.1.9...v2.1.10

[2.1.9]: https://github.com/Drjacky/ImagePicker/compare/v2.1.8...v2.1.9

[2.1.8]: https://github.com/Drjacky/ImagePicker/compare/v2.1.7...v2.1.8

[2.1.7]: https://github.com/Drjacky/ImagePicker/compare/v2.1.6...v2.1.7

[2.1.6]: https://github.com/Drjacky/ImagePicker/compare/v2.0.6...v2.1.6
[2.0.6]: https://github.com/Drjacky/ImagePicker/compare/v2.0.5...v2.0.6
[2.0.5]: https://github.com/Drjacky/ImagePicker/compare/v2.0.4...v2.0.5
[2.0.4]: https://github.com/Drjacky/ImagePicker/compare/v1.8.4...v2.0.4
[1.8.4]: https://github.com/Drjacky/ImagePicker/compare/v1.8.3...v1.8.4
[1.8.3]: https://github.com/Drjacky/ImagePicker/compare/v1.8.2...v1.8.3
[1.8.2]: https://github.com/Drjacky/ImagePicker/compare/v1.8.1...v1.8.2
[1.8.1]: https://github.com/Drjacky/ImagePicker/compare/v1.7.10...v1.8.1
[1.7.10]: https://github.com/Drjacky/ImagePicker/compare/v1.7.9...v1.7.10
[1.7.9]: https://github.com/Drjacky/ImagePicker/compare/v1.7.8...v1.7.9
[1.7.8]: https://github.com/Drjacky/ImagePicker/compare/v1.7.7...v1.7.8
[1.7.7]: https://github.com/Drjacky/ImagePicker/compare/v1.7.6...v1.7.7
[1.7.6]: https://github.com/Drjacky/ImagePicker/compare/v1.7.5...v1.7.6
[1.7.5]: https://github.com/Dhaval2404/ImagePicker/compare/v1.7.4...v1.7.5
[1.7.4]: https://github.com/Dhaval2404/ImagePicker/compare/v1.7.3...v1.7.4
[1.7.3]: https://github.com/Dhaval2404/ImagePicker/compare/v1.7.2...v1.7.3
[1.7.2]: https://github.com/Dhaval2404/ImagePicker/compare/v1.7.1...v1.7.2
[1.7.1]: https://github.com/Dhaval2404/ImagePicker/compare/v1.7...v1.7.1
[1.7]: https://github.com/Dhaval2404/ImagePicker/compare/v1.6...v1.7
[1.6]: https://github.com/Dhaval2404/ImagePicker/compare/v1.5...v1.6
[1.5]: https://github.com/Dhaval2404/ImagePicker/compare/v1.4...v1.5
[1.4]: https://github.com/Dhaval2404/ImagePicker/compare/v1.3...v1.4
[1.3]: https://github.com/Dhaval2404/ImagePicker/compare/v1.2...v1.3
[1.2]: https://github.com/Dhaval2404/ImagePicker/compare/v1.1...v1.2
[1.1]: https://github.com/Dhaval2404/ImagePicker/compare/v1.0...v1.1
[1.0]: https://github.com/Dhaval2404/ImagePicker/tree/v1.0
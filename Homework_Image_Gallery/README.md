# Homework Image Gallery

Android MVVM sample using Pixabay, Paging 3, Room, Coil, and ML Kit object detection.

## Features
- Paginated image gallery from Pixabay
- Detected objects via ML Kit object detection
- Room cache for detected object labels
- Search by detected objects

## Quick start
1. Open the project in Android Studio.
2. Sync Gradle.
3. Run the `app` configuration on a device or emulator.

## Notes
- Pixabay API key is configured in `app/build.gradle.kts` as `BuildConfig.PIXABAY_API_KEY`.
- Default query is set in `GalleryRepository`.

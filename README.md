## Compose Recipe App

This is a sample recipe app built with Jetpack Compose, Navigation Component, and MVVM architecture. The app displays a list of recipes, and upon clicking on a recipe, the user is directed to a detail screen that displays the recipe's ingredients, steps, and an image.

The app uses the following libraries and tools:
-   Jetpack Compose - a modern UI toolkit for building native Android apps with Kotlin
-   Navigation Component - a library for navigating between screens in an app
- ViewModel and LiveData - components of the Android Architecture Components for managing UI-related data in a lifecycle-aware way
-   Coil - an image loading library for Android
-   Kotlin Coroutines - a library for managing asynchronous operations
-   Room DB: A SQLite object mapping library that provides local data storage with minimal boilerplate code.
-   Dagger Hilt: A dependency injection framework that simplifies dependency injection in Android apps by providing a standard way to do it and reducing the amount of boilerplate code required.
-   Firebase Storage: A cloud-based storage service that allows you to securely upload and download user-generated content, such as images, videos, and audio files, from your app.

### Features

-   Display a list of recipes with their titles and images
-   Display a recipe's details, including its ingredients, steps, and image
-   Filter recipes by category
-   Search for recipes by title
-   Save & Edit a recipe


### Architecture

The app follows the MVVM (Model-View-ViewModel) architecture pattern. The UI components (views) are implemented with Jetpack Compose. The ViewModel components are responsible for managing the UI-related data and business logic. The Model components are responsible for providing the data from the backend API.

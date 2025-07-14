# Gabrieal's Gym Tracker

A modern, multiplatform gym tracking app built with Kotlin Multiplatform and Jetpack Compose. Track your workouts, visualize progress, and manage your fitness journey across devices.

## Screenshot

<img width="1387" alt="Screenshot 2025-06-23 at 1 13 02 AM" src="https://github.com/user-attachments/assets/20c06d5c-373d-438f-bdf0-58761a57b25b" />

## Features

- Track and log workouts, sets, reps, and weights
- Visualize workout history with beautiful charts (powered by [Vico](https://github.com/patrykandpatrick/vico))
- User authentication and cloud sync (Firebase/Firestore)
- Clean, modern UI with Compose Multiplatform
- Cross-platform: Android, iOS
- Uses native dialogs on each platform when required for confirmations, warnings, or input (ensuring familiar UX)
- Built-in workout timer with notification support to alert you when your rest period is over

## Optimizations & Architecture

- **Dependency Injection:** All screens use Koin for dependency injection, ensuring ViewModels are managed by the DI container for maintainability and testability.
- **ProGuard & Minification:** Release builds are optimized with ProGuard for minification and resource shrinking. Custom rules are provided for Kotlin, Koin, Voyager, and Compose.
- **App Bundle Configuration:** Android App Bundles are configured to split APKs by density and ABI, reducing download size. Language splitting is disabled to include all language resources in each APK. Debug builds have App Bundle features enabled but minification disabled for easier debugging.
- **Back Press Handling:** Custom back press behavior in MakeAPlanScreen using Voyager's BackHandler (with @InternalVoyagerApi) to show a warning dialog.

## Getting Started

### Prerequisites

- JDK 17+
- Android Studio (for Android/iOS/desktop builds)
- Kotlin Multiplatform Mobile plugin
- Gradle (wrapper included)

### Setup

1. **Clone the repository:**
   ```bash
   git clone https://github.com/Gabrieal4Real/Gabrieals-Gym-Tracker.git
   cd Gabrieals-Gym-Tracker
   ```
2. **Configure API Keys:**
   - Open `composeApp/src/commonMain/kotlin/org/gabrieal/gymtracker/data/network/APIService.kt`.
   - Replace `[API_KEY]` and `[PROJECT_ID]` with your Firebase project credentials.
3. **Install dependencies:**
   ```bash
   ./gradlew build
   ```
4. **Run the app:**
   - For Android: Use Android Studio’s device manager.
   - For iOS: Open the project in Xcode via the generated `.xcworkspace`.

## Tech Stack

- [Kotlin Multiplatform (KMM)](https://kotlinlang.org/lp/mobile/)  
- [Jetpack Compose Multiplatform](https://www.jetbrains.com/lp/compose-multiplatform/)  
- [Vico](https://github.com/patrykandpatrick/vico) (charting library)
- [Firebase/Firestore](https://firebase.google.com/) (authentication & cloud storage)  
- [Koin (dependency injection)](https://insert-koin.io/)  
- [SQLDelight (local database)](https://cashapp.github.io/sqldelight/)  
- [Kotlinx Coroutines](https://github.com/Kotlin/kotlinx.coroutines) (async, background work)  
- [StateFlow](https://kotlinlang.org/api/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/-state-flow/) (reactive state management)
- [Voyager](https://github.com/adrielcafe/voyager) (navigation)

## Platform Support

- **Android**: Fully supported
- **iOS**: Fully supported (via Kotlin Multiplatform and Compose for iOS)

## Project Structure

```
composeApp/
  ├── src/
  │   ├── commonMain/
  │   │   └── kotlin/org/gabrieal/gymtracker/
  │   │       ├── features/
  │   │       ├── data/
  │   │       ├── util/
  │   │       └── ...
  │   ├── commonMain/sqldelight/
  │   │   └── <your .sq files for SQLDelight go here>
  ├── build.gradle.kts
gradle/
  └── libs.versions.toml

---

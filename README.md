# Bitcoin Wallet — Android Assessment

A native Android Bitcoin Wallet application built with Kotlin and Jetpack Compose.

The application allows users to enter their Bitcoin holdings and view the estimated value in South African Rand (ZAR), US Dollar (USD), and Australian Dollar (AUD), using exchange rates retrieved from the Fixer API.

## Tech Stack

- **Language:** Kotlin
- **UI:** Jetpack Compose and Material 3
- **Architecture:** MVVM with separate presentation, domain, and data layers
- **Dependency Injection:** Hilt
- **Networking:** Retrofit, OkHttp, Moshi
- **Local Storage:** Jetpack DataStore
- **Asynchronous Operations:** Kotlin Coroutines and Flow
- **Testing:** JUnit, MockK, Kotlin Coroutines Test

## Features

- Enter and update Bitcoin holdings, supporting up to eight decimal places.
- Display Bitcoin values in ZAR, USD, and AUD.
- Automatically recalculate values when holdings change.
- Retrieve exchange rates from the Fixer API.
- Cache exchange rates locally for up to 60 minutes.
- Fall back to previously saved exchange rates when the API is unavailable.
- Indicate whether rates are live, cached, or stale.
- Persist Bitcoin holdings between app launches.
- Support light and dark themes.
- Display a custom animated splash screen.

## Architecture

The project separates responsibilities into three main layers.

**Presentation layer:** Jetpack Compose screens, reusable UI components, ViewModel, and UI state.

**Domain layer:** Bitcoin conversion logic, domain models, repository interfaces, and use cases.

**Data layer:** API integration, response mapping, repository implementation, and DataStore persistence.

Hilt provides dependencies throughout the application.

## Exchange Rate Conversion

The Fixer API returns exchange rates relative to EUR.

Bitcoin values are calculated using:

`BTC value in target currency = BTC amount × (target currency rate / BTC rate)`

Calculations use `BigDecimal` to reduce floating-point precision errors.

Displayed fiat values are formatted to two decimal places.

## Caching and Offline Support

The repository follows this strategy:

1. Use cached exchange rates when they are less than 60 minutes old.
2. Otherwise, request fresh rates from the API.
3. On success, save the latest rates and timestamp locally.
4. If the network request fails, use previously saved rates when available and mark them as stale.
5. If no rates are available, show an error with a retry option.

The app distinguishes between **LIVE**, **CACHED**, and **STALE** rate sources.

## Getting Started

### Requirements

- Android Studio
- Android SDK
- JDK compatible with the project's Gradle configuration
- Fixer API key from API Layer

### Configuration

Create or update `local.properties` in the project root:

```properties
FIXER_API_KEY=YOUR_API_KEY_HERE
```

The application reads this property during the Gradle build and exposes it to the networking layer through `BuildConfig`.

Do not commit `local.properties` or a real API key to version control.

### Running the App

1. Open the project in Android Studio.
2. Configure the Fixer API key.
3. Sync the Gradle project.
4. Select an emulator or Android device running Android 8.0 (API 26) or newer.
5. Run the `app` configuration.

## Testing

Run the unit tests:

```bash
./gradlew :app:testDebugUnitTest
```

Build the release variant:

```bash
./gradlew :app:assembleRelease
```

Unit tests cover Bitcoin conversion, currency formatting, and repository behaviour, including caching, network fallback, and coroutine cancellation.

## Security Considerations

- API requests use HTTPS.
- The API key is excluded from source control through `local.properties`.
- HTTP logging is disabled in release builds.
- The API key header is redacted in debug HTTP logs.
- Explicit network timeouts prevent requests from waiting indefinitely.

**Known limitation:** The API key is embedded in the compiled application through `BuildConfig` and can potentially be extracted from the APK. A production deployment should use a backend proxy or another server-side approach to keep the credential off client devices.

## Potential Improvements

- Move API authentication behind a backend service.
- Add automated UI tests.
- Add configurable exchange-rate refresh.
- Improve adaptive launcher icons and native splash-screen integration.
- Introduce additional accessibility and device-size testing.

## Assessment Notes

This project prioritises separation of concerns, testable conversion logic, resilient exchange-rate retrieval, and a responsive native Android experience.
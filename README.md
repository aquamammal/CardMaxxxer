# CardMaxxxer

A privacy-first Android credit card benefits tracker. Track your card perks, get recommendations on which card to use, and never miss an expiring benefit.

## Features

- **Wallet Management** — Add and manage your credit cards with full benefit tracking
- **Context-Aware Recommendations** — Get real-time recommendations on which card to use based on your spending context
- **Perk Tracking** — Track benefit usage, redemption history, and expiration dates
- **Smart Notifications** — Get alerted before benefits expire
- **Home Screen Widget** — Glance-powered widget for quick access to expiring perks
- **Privacy First** — No internet permission, encrypted local database, no data leaves your device

## Architecture

CardMaxxxer follows **Clean Architecture** with clear separation of concerns:

```
com.cardmaxxxer/
├── core/
│   ├── database/          # Room database, DAOs, type converters
│   ├── di/                # Hilt dependency injection modules
│   ├── notifications/     # Notification channel management
│   └── security/          # SQLCipher key management (Android Keystore)
├── data/
│   ├── local/entity/      # Room entities
│   └── repository/        # Repository implementations
├── domain/
│   ├── engine/            # Recommendation engine
│   ├── model/             # Domain models
│   └── repository/        # Repository interfaces
├── ui/
│   ├── theme/             # Compose theme
│   ├── widget/            # Glance widget
│   └── screens/           # Compose screens
├── work/
│   ├── receiver/          # Broadcast receivers (alarm, boot)
│   └── worker/            # WorkManager workers
├── CardMaxxxerApplication.kt
└── MainActivity.kt
```

### Technology Stack

| Layer | Technology |
|-------|-----------|
| UI | Jetpack Compose + Material 3 |
| DI | Hilt |
| Database | Room + SQLCipher (AES-256) |
| Background Work | WorkManager + Hilt Work |
| Widgets | Glance |
| Navigation | Navigation Compose |
| Min SDK | 26 (Android 8.0) |

### Security

- **SQLCipher** encrypts the entire database with AES-256
- The database passphrase is generated with `SecureRandom` and encrypted with an **Android Keystore** AES/GCM key
- No internet permission — all data stays on-device
- `allowBackup=false` prevents backup extraction

## Building

### Prerequisites

- Android Studio Hedgehog (2023.1.1) or later
- JDK 17
- Android SDK 34

### Build Commands

```bash
# Debug build
./gradlew assembleDebug

# Release build
./gradlew assembleRelease

# Run tests
./gradlew test

# Run instrumented tests
./gradlew connectedAndroidTest
```

### Installation

```bash
adb install app/build/outputs/apk/debug/app-debug.apk
```

## Project Structure

```
CardMaxxxer/
├── app/
│   ├── src/main/
│   │   ├── java/com/cardmaxxxer/
│   │   │   ├── core/
│   │   │   │   ├── database/
│   │   │   │   ├── di/
│   │   │   │   ├── notifications/
│   │   │   │   └── security/
│   │   │   ├── data/
│   │   │   │   ├── local/entity/
│   │   │   │   └── repository/
│   │   │   ├── domain/
│   │   │   │   ├── engine/
│   │   │   │   ├── model/
│   │   │   │   └── repository/
│   │   │   ├── ui/
│   │   │   │   ├── theme/
│   │   │   │   ├── widget/
│   │   │   │   └── screens/
│   │   │   ├── work/
│   │   │   │   ├── receiver/
│   │   │   │   └── worker/
│   │   │   ├── CardMaxxxerApplication.kt
│   │   │   └── MainActivity.kt
│   │   └── res/
│   │       ├── values/
│   │       └── xml/
│   ├── build.gradle.kts
│   └── proguard-rules.pro
├── gradle/
│   └── libs.versions.toml
├── build.gradle.kts
├── settings.gradle.kts
└── gradle.properties
```

## License

Private project. All rights reserved.

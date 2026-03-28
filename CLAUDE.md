# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is an educational Android laboratory project (`Labs20261-Gr01`) for a mobile computing course at Universidad de Antioquia. It demonstrates basic Android development concepts through practical lab assignments.

## Build Commands

```bash
# Build the project
./gradlew build

# Build debug APK
./gradlew assembleDebug

# Run local unit tests
./gradlew test

# Run instrumented tests (requires connected device/emulator)
./gradlew connectedAndroidTest

# Run a single test class
./gradlew :Lab1-UI:test --tests "co.edu.udea.compumovil.gr01_20261.labs20261_gr01.lab1.ExampleUnitTest"

# Clean build
./gradlew clean
```

## Project Structure

Single module project with one app module:

- **`:Lab1-UI`** — The only app module, package `co.edu.udea.compumovil.gr01_20261.labs20261_gr01.lab1`
  - `src/main/java/` — Kotlin source files
  - `src/main/res/layout/` — XML layouts
  - `src/test/` — Local unit tests (JUnit 4)
  - `src/androidTest/` — Instrumented tests (Espresso + Compose UI testing)

## Architecture

**Simple Activity-based architecture** — no MVVM, no ViewModel, no LiveData.

Activity flow (multi-step wizard):
1. `MainActivity` → immediately redirects to `PersonalDataActivity`
2. `PersonalDataActivity` → collects personal info (name, gender, birthdate, education) via XML form; logs data to Logcat via `Log.d("LAB1", ...)`
3. `ContactDataActivity` → next wizard step (currently a placeholder)

UI approach is **mixed**: Activities extend `AppCompatActivity` and use XML layouts (`activity_*.xml`) for actual UI, while Jetpack Compose is present only for theming (`ui/theme/`).

Data flows via `Intent` extras between activities. There is no persistence layer.

## Key Technical Details

- **compileSdk / targetSdk:** 36, **minSdk:** 23
- **Java source compatibility:** 11
- **Compose BOM** is included but Compose is only used for theming, not for actual screens
- **Material Design:** Uses both Material 2 (XML-based widgets) and Material 3 (Compose theme)
- ProGuard/R8 minification is **disabled**

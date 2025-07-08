# MCQ Quiz App 📱

A modern Android quiz application built with **Jetpack Compose**, **MVVM Clean Architecture**, and **Dagger Hilt**. The app features an engaging UI with streak tracking, animations, and a delightful user experience.

## 📥 Download & Demo

📱 **[Download APK](https://drive.google.com/file/d/14530CvRTtzRSQ7cP_3pWWyQ9n5-elUnZ/view?usp=sharing)**  
🎥 **[Watch Demo Video](https://drive.google.com/file/d/12S9kIvOJG5rCvTGdeHhk3GuW3GdMkZES/view?usp=sharing)**

## 🎯 Features


- **Streak System** with visual rewards and celebrations\
- **Auto-advance** functionality with visual feedback
- **Smooth Animations** throughout the app
- **Material Design 3** with dynamic theming
- **Comprehensive Results** with performance analytics

## 🏗️ Architecture

This project follows **Clean Architecture** principles with **MVVM pattern**:

```
app/
├── data/                  # Data Layer
│   ├── remote/            # API integration
│   └── repository/        # Repository implementation
├── domain/                # Domain Layer
│   ├── model/             # Business models
│   ├── repository/        # Repository interfaces
│   └── usecase/           # Business logic
├── presentation/          # Presentation Layer
│   ├── quiz/              # Quiz screen & ViewModel
│   ├── results/           # Results screen
│   ├── components/        # Reusable UI components
│   ├── navigation/        # Navigation setup
│   └── theme/             # UI theming
└── di/                    # Dependency Injection
```

### Why Clean Architecture?

1. **Separation of Concerns**: Each layer has a single responsibility
2. **Testability**: Easy to unit test business logic independently
3. **Maintainability**: Changes in one layer don't affect others
4. **Scalability**: Easy to add new features and screens

## 🛠️ Tech Stack

### Core Technologies
- **Kotlin** - Modern, concise, and safe programming language
- **Jetpack Compose** - Modern declarative UI toolkit
- **Material Design 3** - Latest Google design system\

### Architecture & DI
- **MVVM Pattern** - Reactive UI with ViewModels
- **Dagger Hilt** - Compile-time dependency injection
- **StateFlow** - Reactive state management
- **Clean Architecture** - Layered architecture pattern

### Networking
- **Retrofit 2** - Type-safe HTTP client
- **Gson** - JSON serialization/deserialization
- **OkHttp** - HTTP client with logging interceptor

### UI & Animations
- **Compose Animation** - Smooth transitions and effects
- **Material Icons Extended** - Rich icon library
- **Compose Navigation** - Type-safe navigation

## 📋 Requirements Analysis

### Functional Requirements ✅

1. **Launch & Load**
   - ✅ Parse JSON from GitHub Gist API
   - ✅ Show loading indicator during data fetch
   - ✅ Native splash screen implementation

2. **Quiz Flow**
   - ✅ Display question with 4 options
   - ✅ Visual feedback for selected answers
   - ✅ Auto-advance after 2 seconds (reduced to 1.5s for better UX)
   - ✅ Skip functionality with gesture support

3. **Streak Logic**
   - ✅ Track consecutive correct answers
   - ✅ Visual streak badge that lights up at 3+ correct
   - ✅ Creative engagement with multi-level streak system
   - ✅ Reset streak on wrong answer or skip

4. **Results Screen**
   - ✅ Correct/Total score display
   - ✅ Accuracy percentage calculation
   - ✅ Longest streak achieved
   - ✅ Skipped questions count
   - ✅ Restart quiz functionality

### Non-Functional Requirements ✅

1. **User Experience**
   - ✅ Smooth animations and transitions
   - ✅ Swipe gestures for navigation
   - ✅ Processing indicators during wait times
   - ✅ Celebratory effects for achievements

2. **Code Quality**
   - ✅ Clean Architecture implementation
   - ✅ MVVM pattern with proper separation
   - ✅ Dependency injection with Hilt
   - ✅ Reactive state management

3. **Design**
   - ✅ Material Design 3 components
   - ✅ Consistent color scheme and typography
   - ✅ Responsive layouts
   - ✅ Accessibility best practices

## 🎨 UI/UX Enhancements

### Creative Streak System
- **Level 0-2**: Gray star (inactive state)
- **Level 3-4**: Gold star with sparkle effect ✨
- **Level 5-6**: Lightning bolt with energy ⚡
- **Level 7+**: Fire icon with flame animation 🔥

### Enhanced User Feedback
- **Processing State**: Visual indicators during answer processing
- **Full-Screen Feedback**: Immersive answer reveal with particles
- **Dynamic Messages**: Context-aware celebration messages
- **Progress Visualization**: Streak progress dots (0-5)

## 🔧 Implementation Decisions

### Why Single Module?
- **Simplicity**: Appropriate for app scope and complexity
- **Development Speed**: Faster build times and easier navigation
- **Maintenance**: Single point of configuration and dependencies


### State Management
```kotlin
data class QuizUiState(
    val isLoading: Boolean = false,
    val questions: List<Question> = emptyList(),
    val currentQuestionIndex: Int = 0,
    val selectedAnswer: Int? = null,
    val showAnswer: Boolean = false,
    val currentStreak: Int = 0,
    val isAnswerProcessing: Boolean = false
    // ... other state properties
)
```

**Benefits:**
- **Immutable State**: Predictable state changes
- **Single Source of Truth**: Centralized state management
- **Reactive UI**: Automatic UI updates on state changes

## 🚀 Getting Started

### Prerequisites
- Android Studio Hedgehog or later
- Android SDK 24+ (Android 7.0)
- Kotlin 1.8.10+

### Installation

1. **Clone the repository**
```bash
git clone https://github.com/sarojsahu-dev/mcq-quiz.git
cd mcq-quiz-app
```

2. **Open in Android Studio**
   - File → Open → Select project directory

3. **Sync Gradle dependencies**
   - Android Studio will automatically sync on first open

4. **Run the app**
   - Select device/emulator
   - Click Run button or use `Ctrl+R`

### Build Configuration

**Project-level build.gradle.kts:**
```kotlin
plugins {
    id("com.android.application") version "8.1.4" apply false
    id("org.jetbrains.kotlin.android") version "1.8.10" apply false
    id("com.google.dagger.hilt.android") version "2.48" apply false
}
```

**App-level build.gradle.kts:**
```kotlin
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
}
```

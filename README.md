# CodeMaster - Programming Learning Mobile Application

[![Flutter](https://img.shields.io/badge/Flutter-3.x-02569B?logo=flutter)](https://flutter.dev)
[![Dart](https://img.shields.io/badge/Dart-3.x-0175C2?logo=dart)](https://dart.dev)
[![Appwrite](https://img.shields.io/badge/Appwrite-Backend-FD366E?logo=appwrite)](https://appwrite.io)
[![License](https://img.shields.io/badge/License-MIT-green.svg)](LICENSE)

A comprehensive mobile learning platform that provides an engaging, gamified experience for learning programming languages. Built with Flutter, featuring Material Design 3, smooth animations, and complete backend integration.

---

## 📱 Features

### Core Features
- **Multi-Language Support**: Learn Python, JavaScript, Java, and C++
- **Course Management**: Structured courses with progress tracking
- **Practice Challenges**: Daily challenges with difficulty levels (Easy, Medium, Hard)
- **Gamification**: XP system, achievements, and global leaderboards
- **Authentication**: Secure user registration and login
- **Theme Support**: Beautiful dark and light themes with Material Design 3
- **Progress Persistence**: Save and restore learning progress across sessions
- **Smooth Animations**: Professional micro-interactions and transitions

### Technical Features
- **State Management**: Provider pattern for reactive UI updates
- **Local Storage**: SharedPreferences for offline data persistence
- **Backend Integration**: Appwrite for authentication and database
- **Responsive Design**: Adaptive layouts for different screen sizes
- **Performance Optimized**: 60 FPS animations with optimized rebuilds
- **Clean Architecture**: Modular design with separation of concerns

---

## 🎯 User Flow

```
App Launch
    ↓
Choose Programming Language (Onboarding)
    ↓
Main App (Bottom Navigation)
    ├── Home (Dashboard with progress)
    ├── Courses (Browse and learn)
    ├── Practice (Daily challenges)
    └── Profile (Settings and stats)
    
Authentication Flow (Optional)
    ├── Login
    ├── Signup
    └── Auth Gate (Protected routes)
    
Gamification
    ├── Earn XP from challenges
    ├── Unlock achievements
    └── Compete on leaderboard
```

---

## 🛠️ Tech Stack

### Frontend
- **Framework**: Flutter 3.x
- **Language**: Dart 3.x
- **UI**: Material Design 3
- **State Management**: Provider Pattern
- **Animations**: Flutter AnimationController, Transitions

### Backend
- **BaaS**: Appwrite
- **Authentication**: Appwrite Account API
- **Database**: Appwrite Database
- **Storage**: Appwrite Storage (future)

### Local Storage
- **Preferences**: SharedPreferences
- **State Persistence**: JSON serialization

### Architecture
- **Pattern**: MVVM (Model-View-ViewModel)
- **Design Patterns**: Singleton, Factory, Repository
- **Code Organization**: Feature-based modules

---

## 📂 Project Structure

```
lib/
├── main.dart                          # App entry point
├── core/                              # Core functionality
│   ├── theme/                         # Theme definitions
│   │   ├── light_theme.dart
│   │   ├── dark_theme.dart
│   │   └── app_theme.dart
│   ├── providers/                     # State management
│   │   ├── theme_provider.dart
│   │   └── app_state_provider.dart
│   └── widgets/                       # Reusable widgets
│       ├── bottom_nav.dart
│       └── animated_card.dart
├── services/                          # Backend services
│   ├── appwrite_service.dart
│   └── auth_service.dart
├── auth/                              # Authentication
│   ├── login_screen.dart
│   ├── signup_screen.dart
│   └── auth_gate.dart
├── onboarding/                        # Onboarding flow
│   └── choose_path_screen.dart
├── home/                              # Home screen
│   └── home_screen.dart
├── courses/                           # Course management
│   ├── course_list_screen.dart
│   └── course_detail_screen.dart
├── practice/                          # Practice challenges
│   ├── practice_screen.dart
│   └── challenge_card.dart
├── leaderboard/                       # Leaderboard
│   └── leaderboard_screen.dart
├── achievements/                      # Achievements
│   ├── achievements_screen.dart
│   └── achievement_card.dart
└── profile/                           # User profile
    └── profile_screen.dart
```

---

## 🚀 Getting Started

### Prerequisites
- Flutter SDK (3.0 or higher)
- Dart SDK (3.0 or higher)
- Android Studio / VS Code
- Git

### Installation

1. **Clone the repository**
```bash
git clone https://github.com/palvisheshpal626-oss/VISHESHKUMAR.git
cd VISHESHKUMAR
```

2. **Install dependencies**
```bash
flutter pub get
```

3. **Set up Appwrite**
- Create an Appwrite project at [cloud.appwrite.io](https://cloud.appwrite.io)
- Copy your Project ID
- Update `lib/services/appwrite_service.dart`:
```dart
client
  .setEndpoint('https://cloud.appwrite.io/v1')
  .setProject('YOUR_PROJECT_ID'); // Replace with your project ID
```

4. **Run the app**
```bash
flutter run
```

### Build for Production

**Android APK:**
```bash
flutter build apk --release
```

**Android App Bundle:**
```bash
flutter build appbundle --release
```

**iOS (requires Mac):**
```bash
flutter build ios --release
```

---

## 📋 Configuration

### Appwrite Setup

1. Create a new project in Appwrite
2. Enable Email/Password authentication
3. Create the following collections:
   - Users
   - Courses
   - UserProgress
   - Achievements
   - Leaderboard

4. Update the project ID in `appwrite_service.dart`

### Theme Customization

Modify theme colors in:
- `lib/core/theme/light_theme.dart`
- `lib/core/theme/dark_theme.dart`

---

## 🎨 Design System

### Colors
- **Primary**: Purple (#5E35B1)
- **Background (Light)**: Very Light Grey (#FAFAFA)
- **Background (Dark)**: Dark Blue (#0E1628)
- **Cards (Dark)**: Darker Grey (#1A2332)
- **Success**: Green
- **Warning**: Orange
- **Error**: Red

### Typography
- **Display**: Large headings
- **Headline**: Section titles
- **Title**: Card titles
- **Body**: Regular text
- **Label**: Small text, badges

### Spacing
- **Small**: 8px
- **Medium**: 16px
- **Large**: 24px
- **XL**: 32px

---

## 📚 Documentation

- [Project Report](docs/PROJECT_REPORT.md) - Complete BCA project documentation
- [Viva Questions](docs/VIVA_QUESTIONS.md) - 30+ Q&A for project viva
- [Portfolio Description](docs/PORTFOLIO_DESCRIPTION.md) - Resume/portfolio ready content

---

## 🤝 Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

---

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## 👨‍💻 Author

**Vishesh Pal**
- GitHub: [@palvisheshpal626-oss](https://github.com/palvisheshpal626-oss)

---

## 🙏 Acknowledgments

- Flutter Team for the amazing framework
- Appwrite for backend services
- Material Design for UI guidelines
- Community contributors and supporters

---

## 📊 Project Status

🚀 **Status**: Production Ready  
📅 **Last Updated**: January 2026  
🔄 **Version**: 1.0.0

---

## 🔮 Future Enhancements

- [ ] Code editor integration
- [ ] Real-time code execution
- [ ] Video tutorials
- [ ] AI-powered learning paths
- [ ] Social features (forums, chat)
- [ ] Certificate generation
- [ ] Multi-language UI support
- [ ] Offline course downloads
- [ ] Advanced analytics dashboard
- [ ] Integration with GitHub/GitLab

---

**Made with ❤️ using Flutter**

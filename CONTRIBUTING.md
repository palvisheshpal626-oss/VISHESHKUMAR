# Contributing to Coding Learning App

Thank you for considering contributing to the Coding Learning App! 🎉

## 📋 Table of Contents
- [Code of Conduct](#code-of-conduct)
- [How Can I Contribute?](#how-can-i-contribute)
- [Development Setup](#development-setup)
- [Pull Request Process](#pull-request-process)
- [Coding Standards](#coding-standards)

## 📜 Code of Conduct

- Be respectful and inclusive
- Focus on constructive feedback
- Help others learn and grow
- No harassment or discrimination

## 🤝 How Can I Contribute?

### 🐛 Reporting Bugs

Before creating bug reports, please check existing issues. When creating a bug report, include:

- **Clear title and description**
- **Steps to reproduce**
- **Expected vs actual behavior**
- **Screenshots** (if applicable)
- **Device/Android version**
- **App version**

### ✨ Suggesting Features

Feature suggestions are welcome! Please:

- **Check if it's already suggested**
- **Explain the use case**
- **Describe the expected behavior**
- **Consider the learning app context**

### 🔧 Code Contributions

Great areas to contribute:

1. **Content Creation**
   - Add more MCQ questions
   - Create coding problems
   - Write learning material

2. **Features**
   - Animated learning videos
   - Profile and settings screens
   - Achievements system
   - Social features

3. **Improvements**
   - UI/UX enhancements
   - Performance optimizations
   - Bug fixes
   - Test coverage

4. **Documentation**
   - Improve setup guides
   - Add code comments
   - Create tutorials

## 🛠️ Development Setup

1. **Fork and Clone**
   ```bash
   git clone https://github.com/YOUR_USERNAME/VISHESHKUMAR.git
   cd VISHESHKUMAR
   ```

2. **Follow Setup Guide**
   - Read [SETUP_GUIDE.md](SETUP_GUIDE.md)
   - Configure Firebase
   - Setup AdMob (use test IDs)

3. **Create Branch**
   ```bash
   git checkout -b feature/your-feature-name
   ```

## 🔄 Pull Request Process

### Before Submitting

- [ ] Code follows project style
- [ ] All tests pass
- [ ] No new warnings
- [ ] Updated documentation
- [ ] Added tests for new features
- [ ] Tested on emulator/device

### PR Guidelines

1. **Title Format:**
   ```
   [Type] Brief description
   ```
   Types: `Feature`, `Fix`, `Docs`, `Refactor`, `Test`, `Chore`

2. **Description Should Include:**
   - What changes were made
   - Why they were made
   - How to test them
   - Screenshots (for UI changes)

3. **Link Related Issues:**
   ```
   Closes #123
   Related to #456
   ```

### Review Process

1. Maintainer will review your PR
2. Address any requested changes
3. Once approved, it will be merged
4. Your contribution will be credited!

## 💻 Coding Standards

### Kotlin Code Style

- Follow [Kotlin Coding Conventions](https://kotlinlang.org/docs/coding-conventions.html)
- Use meaningful variable names
- Keep functions small and focused
- Add KDoc comments for public APIs

### Jetpack Compose

```kotlin
// ✅ Good
@Composable
fun FeatureScreen(
    viewModel: FeatureViewModel,
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    
    // UI implementation
}

// ❌ Avoid
@Composable
fun Screen() {
    var x by remember { mutableStateOf(0) }
    // Side effects in Composable
}
```

### Architecture

- **MVVM pattern** - Separate UI, ViewModel, and data layers
- **Unidirectional data flow** - State flows down, events flow up
- **Clean Architecture** - Use cases for business logic
- **Repository pattern** - Abstract data sources

### File Organization

```
ui/feature/
├── FeatureScreen.kt       # Composable UI
├── FeatureViewModel.kt    # State management
├── FeatureState.kt        # UI state data class
└── FeatureEvent.kt        # User events (if needed)
```

### Git Commit Messages

Follow Conventional Commits:

```
type(scope): subject

body (optional)

footer (optional)
```

**Types:**
- `feat`: New feature
- `fix`: Bug fix
- `docs`: Documentation
- `style`: Code style (formatting)
- `refactor`: Code restructuring
- `test`: Adding tests
- `chore`: Maintenance

**Examples:**
```
feat(mcq): add timer for questions
fix(level): correct unlock logic bug
docs(setup): update Firebase setup guide
```

## 🧪 Testing

### Running Tests

```bash
./gradlew test
./gradlew connectedAndroidTest
```

### Writing Tests

- Write unit tests for ViewModels
- Write UI tests for critical flows
- Mock dependencies
- Test edge cases

## 📝 Documentation

When adding features:

1. Update relevant README sections
2. Add inline code comments
3. Update setup guides if needed
4. Include usage examples

## 🎯 Priority Areas

Current focus areas:

1. **High Priority**
   - Bug fixes
   - Performance improvements
   - Security enhancements

2. **Medium Priority**
   - New features
   - UI improvements
   - Additional content

3. **Low Priority**
   - Code refactoring
   - Documentation polish

## ❓ Questions?

- Open an issue for discussion
- Email: palvisheshpal626@gmail.com
- Check existing issues and PRs

## 🙏 Thank You!

Every contribution, no matter how small, is valuable. Thank you for helping make this app better!

---

**Happy Contributing! 🚀**

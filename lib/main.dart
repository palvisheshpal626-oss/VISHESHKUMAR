import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'core/providers/theme_provider.dart';
import 'core/providers/app_state_provider.dart';
import 'core/theme/app_theme.dart';
import 'onboarding/choose_path_screen.dart';

void main() async {
  WidgetsFlutterBinding.ensureInitialized();
  
  final themeProvider = ThemeProvider();
  await themeProvider.loadThemeMode();
  
  final appStateProvider = AppStateProvider();
  await appStateProvider.loadFromStorage();
  
  runApp(
    MultiProvider(
      providers: [
        ChangeNotifierProvider.value(value: themeProvider),
        ChangeNotifierProvider.value(value: appStateProvider),
      ],
      child: const CodeMasterApp(),
    ),
  );
}

class CodeMasterApp extends StatelessWidget {
  const CodeMasterApp({super.key});

  @override
  Widget build(BuildContext context) {
    final themeProvider = Provider.of<ThemeProvider>(context);
    
    return MaterialApp(
      title: 'CodeMaster',
      debugShowCheckedModeBanner: false,
      themeMode: themeProvider.themeMode,
      theme: AppTheme.lightTheme,
      darkTheme: AppTheme.darkTheme,
      home: const ChoosePathScreen(),
    );
  }
}

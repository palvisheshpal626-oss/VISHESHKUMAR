import 'package:flutter/material.dart';
import 'light_theme.dart' as light;
import 'dark_theme.dart' as dark;

class AppTheme {
  static ThemeData get lightTheme => light.lightTheme;
  static ThemeData get darkTheme => dark.darkTheme;
}

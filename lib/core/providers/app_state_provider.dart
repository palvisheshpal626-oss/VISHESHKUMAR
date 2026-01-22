import 'package:flutter/material.dart';
import 'package:shared_preferences/shared_preferences.dart';

class AppStateProvider extends ChangeNotifier {
  String? _selectedLanguage;

  String? get selectedLanguage => _selectedLanguage;

  Future<void> setLanguage(String language) async {
    _selectedLanguage = language;
    notifyListeners();
    final prefs = await SharedPreferences.getInstance();
    await prefs.setString('selectedLanguage', language);
  }

  Future<void> loadFromStorage() async {
    final prefs = await SharedPreferences.getInstance();
    _selectedLanguage = prefs.getString('selectedLanguage');
    notifyListeners();
  }
}

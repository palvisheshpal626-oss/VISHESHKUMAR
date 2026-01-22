import 'package:appwrite/appwrite.dart';
import 'appwrite_service.dart';

class AuthService {
  final AppwriteService _appwriteService = AppwriteService();

  Future<User> signUp(String email, String password) async {
    try {
      final user = await _appwriteService.account.create(
        userId: ID.unique(),
        email: email,
        password: password,
      );
      return user;
    } catch (e) {
      throw Exception('Sign up failed: ${e.toString()}');
    }
  }

  Future<Session> login(String email, String password) async {
    try {
      final session = await _appwriteService.account.createEmailPasswordSession(
        email: email,
        password: password,
      );
      return session;
    } catch (e) {
      throw Exception('Login failed: ${e.toString()}');
    }
  }

  Future<void> logout() async {
    try {
      await _appwriteService.account.deleteSession(sessionId: 'current');
    } catch (e) {
      throw Exception('Logout failed: ${e.toString()}');
    }
  }

  Future<User?> getCurrentUser() async {
    try {
      final user = await _appwriteService.account.get();
      return user;
    } catch (e) {
      return null;
    }
  }
}

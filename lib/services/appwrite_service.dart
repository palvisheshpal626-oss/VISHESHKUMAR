import 'package:appwrite/appwrite.dart';

class AppwriteService {
  static final AppwriteService _instance = AppwriteService._internal();
  
  factory AppwriteService() {
    return _instance;
  }
  
  AppwriteService._internal();

  late Client client;
  late Account account;
  late Databases databases;

  void init() {
    client = Client()
        .setEndpoint('https://cloud.appwrite.io/v1')
        .setProject('YOUR_PROJECT_ID');

    account = Account(client);
    databases = Databases(client);
  }
}

import 'package:flutter/material.dart';

class ChoosePathScreen extends StatelessWidget {
  const ChoosePathScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Choose Path'),
      ),
      body: const Center(
        child: Text('Choose Path Screen'),
      ),
    );
  }
}

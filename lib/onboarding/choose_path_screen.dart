import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../core/widgets/bottom_nav.dart';
import '../core/providers/app_state_provider.dart';
import '../core/widgets/animated_card.dart';

class ChoosePathScreen extends StatelessWidget {
  const ChoosePathScreen({super.key});

  void _selectLanguage(BuildContext context, String language) async {
    final appState = Provider.of<AppStateProvider>(context, listen: false);
    await appState.setLanguage(language);
    
    if (!context.mounted) return;
    
    ScaffoldMessenger.of(context).showSnackBar(
      SnackBar(
        content: Text('$language selected! Let\'s start learning!'),
        duration: const Duration(seconds: 2),
        behavior: SnackBarBehavior.floating,
      ),
    );
    
    Navigator.of(context).pushReplacement(
      MaterialPageRoute(
        builder: (context) => const BottomNav(),
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: SafeArea(
        child: Padding(
          padding: const EdgeInsets.all(24.0),
          child: Column(
            children: [
              const SizedBox(height: 40),
              const Text(
                '💻',
                style: TextStyle(fontSize: 64),
              ),
              const SizedBox(height: 24),
              Text(
                'Choose Your Path',
                style: Theme.of(context).textTheme.displaySmall?.copyWith(
                      fontWeight: FontWeight.bold,
                    ),
                textAlign: TextAlign.center,
              ),
              const SizedBox(height: 12),
              Text(
                'Which programming language would you like to learn?',
                style: Theme.of(context).textTheme.bodyLarge,
                textAlign: TextAlign.center,
              ),
              const SizedBox(height: 40),
              Expanded(
                child: GridView.count(
                  crossAxisCount: 2,
                  crossAxisSpacing: 16,
                  mainAxisSpacing: 16,
                  childAspectRatio: 0.85,
                  children: [
                    AnimatedCard(
                      delay: 0,
                      onTap: () => _selectLanguage(context, 'Python'),
                      child: _LanguageCard(
                        icon: Icons.language,
                        name: 'Python',
                        description: 'Great for beginners',
                      ),
                    ),
                    AnimatedCard(
                      delay: 100,
                      onTap: () => _selectLanguage(context, 'JavaScript'),
                      child: _LanguageCard(
                        icon: Icons.code,
                        name: 'JavaScript',
                        description: 'Build web apps',
                      ),
                    ),
                    AnimatedCard(
                      delay: 200,
                      onTap: () => _selectLanguage(context, 'Java'),
                      child: _LanguageCard(
                        icon: Icons.android,
                        name: 'Java',
                        description: 'Enterprise & Android',
                      ),
                    ),
                    AnimatedCard(
                      delay: 300,
                      onTap: () => _selectLanguage(context, 'C++'),
                      child: _LanguageCard(
                        icon: Icons.speed,
                        name: 'C++',
                        description: 'High performance',
                      ),
                    ),
                  ],
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}

class _LanguageCard extends StatelessWidget {
  final IconData icon;
  final String name;
  final String description;

  const _LanguageCard({
    required this.icon,
    required this.name,
    required this.description,
  });

  @override
  Widget build(BuildContext context) {
    final isDark = Theme.of(context).brightness == Brightness.dark;
    final cardColor = isDark
        ? Theme.of(context).cardTheme.color ?? const Color(0xFF1A2332)
        : Colors.white;

    return Container(
      decoration: BoxDecoration(
        color: cardColor,
        borderRadius: BorderRadius.circular(16),
        boxShadow: [
          BoxShadow(
            color: Colors.black.withOpacity(isDark ? 0.3 : 0.1),
            blurRadius: 8,
            offset: const Offset(0, 4),
          ),
        ],
      ),
      child: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Icon(
              icon,
              size: 48,
              color: Theme.of(context).colorScheme.primary,
            ),
            const SizedBox(height: 16),
            Text(
              name,
              style: Theme.of(context).textTheme.titleLarge?.copyWith(
                    fontWeight: FontWeight.bold,
                  ),
              textAlign: TextAlign.center,
            ),
            const SizedBox(height: 8),
            Text(
              description,
              style: Theme.of(context).textTheme.bodyMedium,
              textAlign: TextAlign.center,
            ),
          ],
        ),
      ),
    );
  }
}

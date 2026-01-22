import 'package:flutter/material.dart';
import 'achievement_card.dart';

class AchievementsScreen extends StatelessWidget {
  const AchievementsScreen({super.key});

  @override
  Widget build(BuildContext context) {
    final achievements = [
      {
        'icon': Icons.rocket_launch,
        'title': 'First Steps',
        'description': 'Complete your first lesson',
        'isUnlocked': true,
      },
      {
        'icon': Icons.local_fire_department,
        'title': '7 Day Streak',
        'description': 'Practice for 7 days in a row',
        'isUnlocked': true,
      },
      {
        'icon': Icons.school,
        'title': 'Course Master',
        'description': 'Complete a full course',
        'isUnlocked': true,
      },
      {
        'icon': Icons.emoji_events,
        'title': 'Top 10',
        'description': 'Reach top 10 on leaderboard',
        'isUnlocked': false,
      },
      {
        'icon': Icons.code,
        'title': 'Code Warrior',
        'description': 'Solve 100 challenges',
        'isUnlocked': false,
      },
      {
        'icon': Icons.psychology,
        'title': 'Problem Solver',
        'description': 'Complete 10 hard challenges',
        'isUnlocked': false,
      },
      {
        'icon': Icons.star,
        'title': 'XP Master',
        'description': 'Earn 5000 XP total',
        'isUnlocked': false,
      },
      {
        'icon': Icons.groups,
        'title': 'Team Player',
        'description': 'Share 5 achievements',
        'isUnlocked': false,
      },
      {
        'icon': Icons.workspace_premium,
        'title': 'Legend',
        'description': 'Unlock all achievements',
        'isUnlocked': false,
      },
    ];

    return Scaffold(
      appBar: AppBar(
        title: const Text('Achievements'),
      ),
      body: SafeArea(
        child: GridView.builder(
          padding: const EdgeInsets.all(16),
          gridDelegate: const SliverGridDelegateWithFixedCrossAxisCount(
            crossAxisCount: 2,
            crossAxisSpacing: 16,
            mainAxisSpacing: 16,
            childAspectRatio: 0.85,
          ),
          itemCount: achievements.length,
          itemBuilder: (context, index) {
            final achievement = achievements[index];
            return AchievementCard(
              icon: achievement['icon'] as IconData,
              title: achievement['title'] as String,
              description: achievement['description'] as String,
              isUnlocked: achievement['isUnlocked'] as bool,
            );
          },
        ),
      ),
    );
  }
}

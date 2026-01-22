import 'package:flutter/material.dart';

class LeaderboardScreen extends StatelessWidget {
  const LeaderboardScreen({super.key});

  @override
  Widget build(BuildContext context) {
    final isDark = Theme.of(context).brightness == Brightness.dark;
    
    final leaderboardData = [
      {'rank': 1, 'username': 'CodeNinja', 'xp': 5420},
      {'rank': 2, 'username': 'PyMaster', 'xp': 4890},
      {'rank': 3, 'username': 'DevGuru', 'xp': 4350},
      {'rank': 4, 'username': 'JavaPro', 'xp': 3920},
      {'rank': 5, 'username': 'CppExpert', 'xp': 3580},
      {'rank': 6, 'username': 'WebWizard', 'xp': 3210},
      {'rank': 7, 'username': 'AlgoMaster', 'xp': 2890},
      {'rank': 8, 'username': 'DataDev', 'xp': 2560},
      {'rank': 9, 'username': 'CodeCrafter', 'xp': 2340},
      {'rank': 10, 'username': 'TechSavvy', 'xp': 2120},
    ];

    return Scaffold(
      appBar: AppBar(
        title: const Text('Leaderboard'),
      ),
      body: SafeArea(
        child: ListView.builder(
          padding: const EdgeInsets.all(16),
          itemCount: leaderboardData.length,
          itemBuilder: (context, index) {
            final user = leaderboardData[index];
            final rank = user['rank'] as int;
            final username = user['username'] as String;
            final xp = user['xp'] as int;
            final isTop3 = rank <= 3;
            
            Color? rankColor;
            if (rank == 1) rankColor = Colors.amber;
            if (rank == 2) rankColor = Colors.grey[400];
            if (rank == 3) rankColor = Colors.brown[300];

            return Container(
              margin: const EdgeInsets.only(bottom: 12),
              decoration: BoxDecoration(
                color: isDark ? const Color(0xFF1A2332) : Colors.white,
                borderRadius: BorderRadius.circular(12),
                boxShadow: [
                  BoxShadow(
                    color: Colors.black.withOpacity(isDark ? 0.3 : 0.1),
                    blurRadius: 8,
                    offset: const Offset(0, 2),
                  ),
                ],
                border: isTop3
                    ? Border.all(color: rankColor!, width: 2)
                    : null,
              ),
              child: ListTile(
                contentPadding: const EdgeInsets.symmetric(
                  horizontal: 16,
                  vertical: 8,
                ),
                leading: Container(
                  width: 40,
                  height: 40,
                  decoration: BoxDecoration(
                    color: isTop3
                        ? rankColor
                        : Theme.of(context).primaryColor.withOpacity(0.1),
                    shape: BoxShape.circle,
                  ),
                  child: Center(
                    child: Text(
                      '$rank',
                      style: TextStyle(
                        fontWeight: FontWeight.bold,
                        fontSize: 18,
                        color: isTop3
                            ? Colors.white
                            : Theme.of(context).primaryColor,
                      ),
                    ),
                  ),
                ),
                title: Row(
                  children: [
                    CircleAvatar(
                      radius: 20,
                      backgroundColor: Theme.of(context).primaryColor.withOpacity(0.2),
                      child: Text(
                        username[0].toUpperCase(),
                        style: TextStyle(
                          fontWeight: FontWeight.bold,
                          color: Theme.of(context).primaryColor,
                        ),
                      ),
                    ),
                    const SizedBox(width: 12),
                    Expanded(
                      child: Text(
                        username,
                        style: Theme.of(context).textTheme.titleMedium?.copyWith(
                              fontWeight: FontWeight.bold,
                            ),
                      ),
                    ),
                  ],
                ),
                trailing: Row(
                  mainAxisSize: MainAxisSize.min,
                  children: [
                    const Icon(Icons.star, color: Colors.amber, size: 20),
                    const SizedBox(width: 4),
                    Text(
                      '$xp',
                      style: Theme.of(context).textTheme.titleMedium?.copyWith(
                            fontWeight: FontWeight.bold,
                          ),
                    ),
                  ],
                ),
              ),
            );
          },
        ),
      ),
    );
  }
}

import 'package:flutter/material.dart';
import 'course_detail_screen.dart';

class CourseListScreen extends StatelessWidget {
  const CourseListScreen({super.key});

  @override
  Widget build(BuildContext context) {
    final courses = [
      {
        'title': 'Python Fundamentals',
        'level': 'Beginner',
        'progress': 0.35,
      },
      {
        'title': 'JavaScript Essentials',
        'level': 'Beginner',
        'progress': 0.20,
      },
      {
        'title': 'Java Advanced',
        'level': 'Advanced',
        'progress': 0.60,
      },
      {
        'title': 'C++ Programming',
        'level': 'Intermediate',
        'progress': 0.45,
      },
    ];

    return Scaffold(
      appBar: AppBar(
        title: const Text('Courses'),
      ),
      body: ListView.builder(
        padding: const EdgeInsets.all(16),
        itemCount: courses.length,
        itemBuilder: (context, index) {
          final course = courses[index];
          return _CourseCard(
            title: course['title'] as String,
            level: course['level'] as String,
            progress: course['progress'] as double,
            onTap: () {
              Navigator.push(
                context,
                MaterialPageRoute(
                  builder: (context) => CourseDetailScreen(
                    courseTitle: course['title'] as String,
                  ),
                ),
              );
            },
          );
        },
      ),
    );
  }
}

class _CourseCard extends StatelessWidget {
  final String title;
  final String level;
  final double progress;
  final VoidCallback onTap;

  const _CourseCard({
    required this.title,
    required this.level,
    required this.progress,
    required this.onTap,
  });

  Color _getLevelColor(BuildContext context) {
    switch (level) {
      case 'Beginner':
        return Colors.green;
      case 'Intermediate':
        return Colors.orange;
      case 'Advanced':
        return Colors.red;
      default:
        return Theme.of(context).colorScheme.primary;
    }
  }

  @override
  Widget build(BuildContext context) {
    final isDark = Theme.of(context).brightness == Brightness.dark;
    final cardColor = isDark
        ? const Color(0xFF1A2332)
        : Colors.white;

    return Padding(
      padding: const EdgeInsets.only(bottom: 16),
      child: InkWell(
        onTap: onTap,
        borderRadius: BorderRadius.circular(16),
        child: Container(
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
          padding: const EdgeInsets.all(20),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: [
                  Expanded(
                    child: Text(
                      title,
                      style: Theme.of(context).textTheme.titleLarge?.copyWith(
                            fontWeight: FontWeight.bold,
                          ),
                    ),
                  ),
                  Container(
                    padding: const EdgeInsets.symmetric(
                      horizontal: 12,
                      vertical: 6,
                    ),
                    decoration: BoxDecoration(
                      color: _getLevelColor(context).withOpacity(0.2),
                      borderRadius: BorderRadius.circular(8),
                    ),
                    child: Text(
                      level,
                      style: TextStyle(
                        color: _getLevelColor(context),
                        fontWeight: FontWeight.w600,
                        fontSize: 12,
                      ),
                    ),
                  ),
                ],
              ),
              const SizedBox(height: 16),
              Row(
                children: [
                  Expanded(
                    child: ClipRRect(
                      borderRadius: BorderRadius.circular(8),
                      child: LinearProgressIndicator(
                        value: progress,
                        minHeight: 8,
                        backgroundColor: isDark
                            ? Colors.grey[800]
                            : Colors.grey[200],
                        valueColor: AlwaysStoppedAnimation<Color>(
                          Theme.of(context).colorScheme.primary,
                        ),
                      ),
                    ),
                  ),
                  const SizedBox(width: 12),
                  Text(
                    '${(progress * 100).toInt()}%',
                    style: Theme.of(context).textTheme.titleMedium?.copyWith(
                          fontWeight: FontWeight.bold,
                        ),
                  ),
                ],
              ),
            ],
          ),
        ),
      ),
    );
  }
}

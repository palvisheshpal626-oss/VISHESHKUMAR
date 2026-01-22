import 'package:flutter/material.dart';

class ChallengeCard extends StatefulWidget {
  final String title;
  final String difficulty;
  final int xpReward;
  final VoidCallback onTap;

  const ChallengeCard({
    super.key,
    required this.title,
    required this.difficulty,
    required this.xpReward,
    required this.onTap,
  });

  @override
  State<ChallengeCard> createState() => _ChallengeCardState();
}

class _ChallengeCardState extends State<ChallengeCard>
    with SingleTickerProviderStateMixin {
  late AnimationController _xpController;
  late Animation<double> _xpScaleAnimation;
  late Animation<double> _xpFadeAnimation;
  bool _isPressed = false;

  @override
  void initState() {
    super.initState();
    _xpController = AnimationController(
      duration: const Duration(milliseconds: 400),
      vsync: this,
    );

    _xpScaleAnimation = Tween<double>(begin: 1.0, end: 1.3).animate(
      CurvedAnimation(
        parent: _xpController,
        curve: Curves.easeOutBack,
      ),
    );

    _xpFadeAnimation = Tween<double>(begin: 1.0, end: 0.0).animate(
      CurvedAnimation(
        parent: _xpController,
        curve: Curves.easeOut,
      ),
    );
  }

  @override
  void dispose() {
    _xpController.dispose();
    super.dispose();
  }

  Color _getDifficultyColor() {
    switch (widget.difficulty) {
      case 'Easy':
        return Colors.green;
      case 'Medium':
        return Colors.orange;
      case 'Hard':
        return Colors.red;
      default:
        return Colors.blue;
    }
  }

  void _handleTap() {
    setState(() => _isPressed = true);
    _xpController.forward().then((_) {
      _xpController.reverse();
    });
    Future.delayed(const Duration(milliseconds: 100), () {
      if (mounted) {
        setState(() => _isPressed = false);
        widget.onTap();
      }
    });
  }

  @override
  Widget build(BuildContext context) {
    final isDark = Theme.of(context).brightness == Brightness.dark;
    final cardColor = isDark
        ? const Color(0xFF1A2332)
        : Colors.white;

    return Padding(
      padding: const EdgeInsets.only(bottom: 16),
      child: GestureDetector(
        onTap: _handleTap,
        child: AnimatedScale(
          scale: _isPressed ? 0.97 : 1.0,
          duration: const Duration(milliseconds: 100),
          curve: Curves.easeInOut,
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
            child: Row(
              children: [
                Expanded(
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Text(
                        widget.title,
                        style: Theme.of(context).textTheme.titleLarge?.copyWith(
                              fontWeight: FontWeight.bold,
                            ),
                      ),
                      const SizedBox(height: 8),
                      Row(
                        children: [
                          Container(
                            padding: const EdgeInsets.symmetric(
                              horizontal: 12,
                              vertical: 6,
                            ),
                            decoration: BoxDecoration(
                              color: _getDifficultyColor().withOpacity(0.2),
                              borderRadius: BorderRadius.circular(8),
                            ),
                            child: Text(
                              widget.difficulty,
                              style: TextStyle(
                                color: _getDifficultyColor(),
                                fontWeight: FontWeight.w600,
                                fontSize: 12,
                              ),
                            ),
                          ),
                          const SizedBox(width: 12),
                          AnimatedBuilder(
                            animation: _xpController,
                            builder: (context, child) {
                              return Opacity(
                                opacity: _xpFadeAnimation.value,
                                child: Transform.scale(
                                  scale: _xpScaleAnimation.value,
                                  child: child,
                                ),
                              );
                            },
                            child: Row(
                              children: [
                                Icon(
                                  Icons.stars,
                                  size: 16,
                                  color: Colors.amber,
                                ),
                                const SizedBox(width: 4),
                                Text(
                                  '+${widget.xpReward} XP',
                                  style: Theme.of(context).textTheme.bodyMedium?.copyWith(
                                        fontWeight: FontWeight.w600,
                                        color: Colors.amber,
                                      ),
                                ),
                              ],
                            ),
                          ),
                        ],
                      ),
                    ],
                  ),
                ),
                Icon(
                  Icons.arrow_forward_ios,
                  color: Theme.of(context).colorScheme.primary,
                  size: 20,
                ),
              ],
            ),
          ),
        ),
      ),
    );
  }
}

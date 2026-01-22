import 'package:flutter/material.dart';
import 'challenge_card.dart';

class PracticeScreen extends StatefulWidget {
  const PracticeScreen({super.key});

  @override
  State<PracticeScreen> createState() => _PracticeScreenState();
}

class _PracticeScreenState extends State<PracticeScreen> {
  int _totalXP = 0;

  void _completeChallenge(String challengeName, int xpReward) {
    setState(() {
      _totalXP += xpReward;
    });
    print('Challenge completed: $challengeName | XP earned: +$xpReward | Total XP: $_totalXP');
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Practice'),
        actions: [
          Center(
            child: Padding(
              padding: const EdgeInsets.only(right: 16),
              child: Row(
                children: [
                  Icon(
                    Icons.stars,
                    color: Colors.amber,
                    size: 20,
                  ),
                  const SizedBox(width: 4),
                  Text(
                    '$_totalXP XP',
                    style: const TextStyle(
                      fontWeight: FontWeight.bold,
                      fontSize: 16,
                    ),
                  ),
                ],
              ),
            ),
          ),
        ],
      ),
      body: SingleChildScrollView(
        padding: const EdgeInsets.all(24),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Text(
              'Daily Practice',
              style: Theme.of(context).textTheme.headlineSmall?.copyWith(
                    fontWeight: FontWeight.bold,
                  ),
            ),
            const SizedBox(height: 16),
            ChallengeCard(
              title: 'Variables & Data Types',
              difficulty: 'Easy',
              xpReward: 50,
              onTap: () => _completeChallenge('Variables & Data Types', 50),
            ),
            ChallengeCard(
              title: 'Control Flow Practice',
              difficulty: 'Easy',
              xpReward: 50,
              onTap: () => _completeChallenge('Control Flow Practice', 50),
            ),
            const SizedBox(height: 32),
            Text(
              'Challenges',
              style: Theme.of(context).textTheme.headlineSmall?.copyWith(
                    fontWeight: FontWeight.bold,
                  ),
            ),
            const SizedBox(height: 16),
            ChallengeCard(
              title: 'Functions & Methods',
              difficulty: 'Medium',
              xpReward: 100,
              onTap: () => _completeChallenge('Functions & Methods', 100),
            ),
            ChallengeCard(
              title: 'Object-Oriented Programming',
              difficulty: 'Medium',
              xpReward: 150,
              onTap: () => _completeChallenge('Object-Oriented Programming', 150),
            ),
            ChallengeCard(
              title: 'Algorithm Challenge',
              difficulty: 'Hard',
              xpReward: 200,
              onTap: () => _completeChallenge('Algorithm Challenge', 200),
            ),
            ChallengeCard(
              title: 'Data Structures Deep Dive',
              difficulty: 'Hard',
              xpReward: 250,
              onTap: () => _completeChallenge('Data Structures Deep Dive', 250),
            ),
          ],
        ),
      ),
    );
  }
}

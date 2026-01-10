package com.vishesh.codinglearning.ui.home

sealed class HomeEvent {
    object StartLearning : HomeEvent()
    object DailyChallenge : HomeEvent()
    object GetFreeCoins : HomeEvent()
}

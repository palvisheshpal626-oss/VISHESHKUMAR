package com.vishesh.codinglearning.ui.mcq

sealed class McqEvent {
    data class SelectAnswer(val answerIndex: Int) : McqEvent()
    object NextQuestion : McqEvent()
    object GetHint : McqEvent()
    object WatchAdForHint : McqEvent()
}

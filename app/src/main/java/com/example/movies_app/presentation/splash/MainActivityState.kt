package com.example.movies_app.presentation.splash

sealed interface MainActivityState {
    data object Fetching : MainActivityState
    data object SuccessfullyFetchedToDatabase : MainActivityState
    data class ErrorFetchingToDatabase(val message: String) : MainActivityState
    data class RepeatAfterTimeoutUpdate(val seconds: Int) : MainActivityState
}

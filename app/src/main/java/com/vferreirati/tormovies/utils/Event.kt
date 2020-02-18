package com.vferreirati.tormovies.utils

sealed class Event<T>
class Success<T>(val data: T) : Event<T>()
class Failure<T>(val errorMessageID: Int) : Event<T>()
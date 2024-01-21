package com.arturmaslov.vismasound.helpers.extensions

fun String.formatDuration(): String {
    val milliseconds = this.toLong()
    val seconds = milliseconds / 1000
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60

    return if (minutes > 0) {
        "${minutes}min ${remainingSeconds}s"
    } else {
        "$remainingSeconds s"
    }
}
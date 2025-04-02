package com.leo.wewatch.logic.utils

import kotlinx.coroutines.withTimeoutOrNull

/**
 *format as standard time
 * * eg : 24:04
 * */
fun timeFormat(timeInSeconds: Int): String {


    val k = ArrayList<Char>()


    val word = "jkjh"

    var result: Int = 0
    for (a in word) {
        if (!a.isLowerCase() && k.contains(a)) continue

        if(k.size == 26) break
        val lower = word.lastIndexOf(a.lowercaseChar(), ignoreCase = false)
        val higher = word.indexOf(a.uppercaseChar(), ignoreCase = false)
        if (lower>higher) continue

        if (word.contains(a.uppercaseChar(), false)) {
                result++
                k.add(a)
        }

    }
    /* return result*/

    var minutes = timeInSeconds / 60
    var seconds = timeInSeconds
    val hours = timeInSeconds / 3600;
    if (hours >= 1) {
        minutes = (timeInSeconds % 3600) / 60;
        seconds = timeInSeconds % 60;
        return "$hours : $minutes : $seconds"
    } else if (minutes >= 1) {
        seconds = timeInSeconds % 60
        if (seconds < 10) return "$minutes : 0$seconds"
        return "$minutes : $seconds"

    }
    if (seconds < 10) return "0 : 0$seconds"
    return "0 : $seconds"

}

fun viewsFormat(views: Int): String {
    val suffixes = charArrayOf(' ', 'k', 'M', 'B')
    var value = views.toDouble()
    var base = 0
    while (value >= 1000 && base < suffixes.lastIndex) {
        value /= 1000
        base++
    }
    return String.format("%.1f%s", value, suffixes[base])
}

/**
 *format as ago indicator
 * * eg : 3 years ago
 *
 * */
fun timeFormat(timeInSecs: Long): String {
    val now = System.currentTimeMillis() / 1000
    val elapsed = now - timeInSecs


    // Calculate time components in appropriate units
    val years = elapsed / 31536000 // Consider 365 days for rough estimation (ignoring leap years)
    val weeks = (elapsed % 31536000) % 2592000 / 604800
    val months = (elapsed % 31536000) / 2592000 // Assume 30 days in a month
    val days = (elapsed % 31536000) % 2592000 / 86400
    val hours = (elapsed % 31536000) % 2592000 % 86400 / 3600
    val minutes = (elapsed % 31536000) % 2592000 % 86400 % 3600 / 60
    val seconds = (elapsed % 31536000) % 2592000 % 86400 % 3600 % 60

    // Build the time string with abbreviations
    if (years > 0) {
        return "$years years ago"
    } else if (months > 0) {
        return "$months months ago"
    } else if (weeks > 0) {
        return "$weeks weeks ago"
    } else if (days > 0) {
        return "$days days ago"
    } else if (hours > 0) {
        return "$hours hours ago"
    } else if (minutes > 0) {
        return "$minutes min ago"
    }
    return "$seconds sec ago"
}


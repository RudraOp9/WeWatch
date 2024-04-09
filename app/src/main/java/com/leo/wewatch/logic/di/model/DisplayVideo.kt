package com.leo.wewatch.logic.di.model

import android.net.Uri


data class DisplayVideo(
    val title: String,
    val imageUri: Uri,
    val channelName: String,
    val views: Int,
    val timePublishedSec: Long,
    val category : CategoryFilter,
    val videoLengthSec: Int,
    val watchedSec:Int
) {
}
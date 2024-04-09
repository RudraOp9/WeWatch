package com.leo.wewatch.navigation

sealed class Screens(val route:String) {

    object HomeFeed:Screens("home_screen")
    object VideoPlayer : Screens("video_player")
}
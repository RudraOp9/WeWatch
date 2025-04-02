package com.leo.wewatch.navigation

import android.content.Context
import android.view.Window
import androidx.annotation.OptIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.media3.common.util.UnstableApi
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.leo.wewatch.app.component.player.logic.VideoPlayBackViewModel
import com.leo.wewatch.app.screens.videoPlayBackScreen.VideoPlayBackScreen
import com.leo.wewatch.app.screens.channelPage.ShowChannelPage
import com.leo.wewatch.app.screens.homescreen.ui.ShowHomeScreen



@OptIn(UnstableApi::class)
@Composable
fun Navigation(context: Context, videoPlayBackViewModel: VideoPlayBackViewModel, window: Window) {
    val navCtrl = rememberNavController()
    NavHost(navController = navCtrl, startDestination = Screens.HomeFeed.route) {

        composable(route = Screens.HomeFeed.route, enterTransition = {fadeIn()}, exitTransition ={ fadeOut() }) {
            ShowHomeScreen(navCtrl)
            
        }
        composable(route = Screens.VideoPlayer.route, enterTransition = {fadeIn()}, exitTransition ={ fadeOut() }) {


            VideoPlayBackScreen(context = context, window)
        }
        composable(
            route = Screens.ChannelPage.route, enterTransition = { fadeIn() }, exitTransition ={ fadeOut()}
        ) {
            ShowChannelPage(navCtrl)
        }

    }

}
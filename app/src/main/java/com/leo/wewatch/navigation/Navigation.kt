package com.leo.wewatch.navigation

import android.content.Context
import android.view.Window
import androidx.annotation.OptIn
import androidx.compose.runtime.Composable
import androidx.media3.common.util.UnstableApi
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.leo.wewatch.app.component.player.logic.Player
import com.leo.wewatch.app.screens.VideoPlayBackScreen
import com.leo.wewatch.app.screens.homescreen.ui.ShowHomeScreen




@OptIn(UnstableApi::class)
@Composable
fun Navigation(context: Context, player: Player, window: Window){
    val navCtrl = rememberNavController()
    NavHost(navController = navCtrl, startDestination = Screens.HomeFeed.route){

        composable(route = Screens.HomeFeed.route){
         ShowHomeScreen(navCtrl)
        }
        composable(route = Screens.VideoPlayer.route){

            VideoPlayBackScreen(context = context, player = player , window)
        }

    }

}
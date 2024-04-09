package com.leo.wewatch

import android.content.Context
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.util.UnstableApi
import com.leo.wewatch.app.component.player.logic.Player
import com.leo.wewatch.navigation.Navigation
import com.leo.wewatch.ui.theme.WeWatchTheme
import dagger.hilt.android.AndroidEntryPoint

@UnstableApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val player by viewModels<Player>()
        Log.d("TAG", "onCreate: in oncreate")

        /*val downloadContentDirectory =
            File(this.filesDir.path,"cache")
        val downloadCache =
            SimpleCache(downloadContentDirectory, NoOpCacheEvictor(), StandaloneDatabaseProvider(
                applicationContext)
            )*/
      /*  val no : Player by viewModels()
        val mediaItem = MediaItem.fromUri("https://sample-videos.com/video321/mp4/240/big_buck_bunny_240p_1mb.mp4")
        val mediaSource3 = DefaultMediaSourceFactory(no.getChacheDataSourceFactory()).createMediaSource(mediaItem)
        no.setMediaSource(mediaSource3)*/
        setContent {


            WeWatchTheme {
                // A surface container using the 'background' color from the theme

                val playerd =  hiltViewModel<Player> ()

                Surface(
                    modifier = Modifier,
                    color = MaterialTheme.colorScheme.background
                ) {

                   // ShowHomeScreen()
                   // VideoPlayBackScreen(applicationContext = applicationContext/*,downloadCache*/,no)
                    Navigation(applicationContext,playerd)
                    
                }
            }
        }
    }
}

fun Float.pxToDp(context: Context): Float =
    (this / (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT))

@Preview
@Composable
fun prev() {
   // ShowHomeScreen()
}

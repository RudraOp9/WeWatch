package com.leo.wewatch

import android.content.Context
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowColumn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.util.UnstableApi
import com.leo.wewatch.app.component.player.logic.VideoPlayBackViewModel
import com.leo.wewatch.app.screens.channelPage.FilterChip
import com.leo.wewatch.navigation.Navigation
import com.leo.wewatch.ui.theme.WeWatchTheme
import dagger.hilt.android.AndroidEntryPoint

@UnstableApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalLayoutApi::class)
    @Preview
    @Composable
    fun test() {
        Column() {
            Button(onClick = { /*TODO*/ }) {
                
            }
            Surface {
                Text(text = "Text box 1")
            }
            Surface (modifier = Modifier.clickable(){}){

                Text(text = "Text box 2")
            }



        }

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val statusBarHeight: Int = resources.getDimensionPixelSize(
            getResources().getIdentifier("status_bar_height", "dimen", "android")
        )


       /* val player by viewModels<Player>()*/
        Log.d("TAG", "onCreate: in oncreat e $statusBarHeight")

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

      /*  WindowCompat.setDecorFitsSystemWindows(window, false)

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        } else {
            window.insetsController?.apply {
                hide(WindowInsets.Type.statusBars())
                systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        }*/
        setContent {



            WeWatchTheme {
                // A surface container using the 'background' color from the theme



                val playerd =  hiltViewModel<VideoPlayBackViewModel> ()

                Surface(
                    modifier = Modifier,
                    color = MaterialTheme.colorScheme.background
                ) {

                   // ShowHomeScreen()
                   // VideoPlayBackScreen(applicationContext = applicationContext/*,downloadCache*/,no)
                    Navigation(applicationContext,playerd,window)
                    
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

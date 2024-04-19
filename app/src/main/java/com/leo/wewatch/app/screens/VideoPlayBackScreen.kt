package com.leo.wewatch.app.screens

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Display.FLAG_SECURE
import android.view.SurfaceView
import android.view.Window
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.annotation.OptIn
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.WindowCompat
import androidx.media3.common.FlagSet
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import com.google.common.base.Suppliers
import com.google.common.cache.CacheBuilder
import com.google.common.cache.CacheBuilderSpec
import com.google.common.cache.CacheLoader
import com.leo.wewatch.app.component.player.ui.Controls
import com.leo.wewatch.app.component.player.ui.ScrollWithControl
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.net.CacheRequest

fun Context.findActivity(): Activity? = when (this) {
    is Activity ->  {
        Log.d("ori", "findActivity: Activity")
        this
    }
    is ContextWrapper -> {Log.d("ori", "findActivity: baseContext")
        baseContext.findActivity()

    }
    else -> null
}

@kotlin.OptIn(ExperimentalFoundationApi::class)
@OptIn(UnstableApi::class)
@Composable
fun  VideoPlayBackScreen(
    context: Context,
    player: com.leo.wewatch.app.component.player.logic.Player,
    window: Window
) {

    var showUi by rememberSaveable {
        mutableStateOf(false)
    }



    var isLoading by rememberSaveable {
        mutableStateOf(false)
    }
    var currentPosition by rememberSaveable {
        mutableLongStateOf(1L)
    }
    var isPlayingVideo by rememberSaveable {
        mutableStateOf(false)
    }

    var isEnded by rememberSaveable {
        mutableStateOf(false)
    }

    var vidDuration by rememberSaveable {
        mutableLongStateOf(0L)
    }


    var uri by rememberSaveable {
        mutableStateOf("https://archive.org/download/big-bunny-sample-video/SampleVideo.ia.mp4")
    }

    val interactionSource = remember { MutableInteractionSource() }


    // controls ui
    var timer by rememberSaveable {
        mutableStateOf(4000L)
    }

    var landScapeOrientation by rememberSaveable {
        mutableStateOf(false)
    }

   // var oriConfig = LocalContext.current as Activity
    var screeenOri = LocalConfiguration.current.orientation
  //  landScapeOrientation = oriConfig.requestedOrientation


    landScapeOrientation = screeenOri == 2


    val activity = LocalContext.current.findActivity()
   /* if (activity != null) {
        activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            Log.d("landScapeOrientation", "VideoPlayBackScreen: $landScapeOrientation && ${activity.requestedOrientation}")
    }
   */

    // for activity info : landscape : 0  , potrait : 1 , unspecifiet : -1
    //  for Local config  : landscape : 2  , potrait : 1

    LaunchedEffect(key1 = timer) {
        if (timer == 0L) {
            if (isPlayingVideo && showUi) {
                showUi = false
                Log.d(
                    "TAG",
                    "VideoPlayBackScreen: surface automatically hides"
                )

            }
        } else {
            delay(4000)
            timer = 0L
        }
    }

    Box {
        Column {
            LaunchedEffect(key1 = context) {
                player.setUpExoPlayer(context)
                player.addListener(object : Player.Listener {

                    override fun onIsPlayingChanged(isPlaying: Boolean) {
                        isPlayingVideo = isPlaying
                    }

                    override fun onRenderedFirstFrame() {
                        vidDuration = player.getDuration()
                    }

                    override fun onPlaybackStateChanged(playbackState: Int) {
                        when (playbackState) {
                            Player.STATE_BUFFERING -> {
                                showUi = true
                                isLoading = true
                                isEnded = false
                            }

                            Player.STATE_READY -> {
                                showUi = false
                                isLoading = false
                                isEnded = false
                            } // Buffering might have ended or reached sufficient level
                            Player.STATE_ENDED -> {
                                showUi = true
                                isLoading = false
                                isEnded = true
                            }

                            Player.STATE_IDLE -> {

                            }

                            else -> Unit // Handle other states
                        }
                    }
                })
            }

            val lifecycleOwner = LocalLifecycleOwner.current
            LaunchedEffect(key1 = uri) {

                player.SetUri(uri = uri)
                player.prepareExoPlayer(lifecycleOwner)
                vidDuration = player.getDuration()
                Log.d("TAG", vidDuration.toString())
            }
            DisposableEffect(Unit) {
                onDispose {
                    player.release()
                }
            }


            // player.SetMediaSource(mediaSource3)
            //  player.StartPlay(LocalLifecycleOwner.current,exoPlayer)

            if (isPlayingVideo) {
                LaunchedEffect(key1 = Unit) {
                    launch {
                        //  Log.d("tag", "in launch")
                        while (isPlayingVideo) {
                            currentPosition = player.currentPosition()
                            vidDuration = player.getDuration()
                            Log.d(
                                "tag",
                                "looped crr Pos : $currentPosition , vid dur : ${player.getDuration()}"
                            )
                            delay(500)
                        }
                    }
                }
            }

            //  exoPlayer.videoScalingMode = C.VIDEO_SCALING_MODE_DEFAULT




            @Composable
            fun ShowUi() {
                if (showUi) {
                    Controls(
                        onCaptionClick = {},
                        onCastClick = {},
                        onMinimizeClick = {},
                        onNextClick = {},
                        onPauseClick = {
                            player.pause()
                        },
                        onPlayClick = {
                            //if (!player.isPlaying) player.play()
                            player.play()
                            if (isPlayingVideo && showUi) {
                                Handler(Looper.getMainLooper())
                                    .postDelayed({
                                        if (showUi && isPlayingVideo) {
                                            showUi = false
                                            Log.d(
                                                "TAG",
                                                "VideoPlayBackScreen: surface automatically hides"
                                            )
                                        }
                                    }, 4000)
                            } else showUi = true
                        },
                        onPreviousClick = {},
                        onSettingsClick = {


                        },


                        onValueChange = {
                            Log.d(
                                "TAG",
                                "VideoPlayBackScreen: valchange : $it , to seek : ${it * vidDuration} , vid Duration : v=$vidDuration"
                            )
                            player.seekTo((it * vidDuration).toLong())
                            //  currentPosition = (it * vidDuration).toLong()
                        },
                        onRestartClick = {
                            player.reStart()

                        },
                        input = currentPosition,
                        videoDuration = vidDuration.toFloat(),
                        isPlaying = isPlayingVideo,
                        isEnded = isEnded,
                        isLoading = isLoading
                    )
                }
            }

            var interactionSources = remember {
                MutableInteractionSource()
            }
            var isPressed by rememberSaveable {
                mutableStateOf(false)
            }
            isPressed = interactionSources.collectIsPressedAsState().value
            if (!isPressed) {
                player.playSpeed(1f)
            }

            var backColor = Color.Transparent

            backColor = if (showUi){
                (Color.Black.copy(alpha = 0.25f))
            }else Color.Transparent

          /*  var dunno = LocalContext.current as Activity

            dunno.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE*/
            @Composable
            fun PlayerControl(){
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(backColor)
                        /*.align(Alignment.Start)*/
                        .combinedClickable(
                            interactionSource = interactionSources,
                            indication = null,
                            onLongClick = {
                                if (isPressed) {
                                    player.playSpeed(2f)
                                }
                            },
                            onClick = {
                                showUi = !showUi
                                timer = 4000L
                            })
                ) {

                }
            }

            @Composable
            fun PlayerSurface(){
                AndroidView(factory = { SurfaceView(context) }, modifier = Modifier
                    .fillMaxWidth()
                    /*
                    .aspectRatio(1280f / 720f, true)
*/
                    .clip(
                        RoundedCornerShape(1.dp)
                    )/*.align(Alignment.TopCenter)*/
                    ,
                    update = {
                        Log.d("TAG", "VideoPlayBackScreen: ${it.rotation}")
                        //  it.rotation = 90f


                        player.updateSurface(it.holder.surface)
                    }
                )

            }



            @Composable
            fun Scroll(){
                ScrollWithControl(vidDuration.toFloat(), currentPosition,landScapeOrientation, onValueChange = {
                    Log.d(
                        "TAG",
                        "VideoPlayBackScreen: valchange : $it , to seek : ${it * vidDuration} , vid Duration : v=$vidDuration"
                    )
                    player.seekTo((it * vidDuration).toLong())
                    //  currentPosition = (it * vidDuration).toLong()
                }, onOrientationConfig = {
                    if (landScapeOrientation){
                       // oriConfig.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
                        if (activity != null) {
                            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
                        }
                    }else {
                        if (activity != null) {
                            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
                        }
                    }
                }, showUi = showUi)
            }



            if (landScapeOrientation) {
                Log.d("InfoTag", "orientation is 1 : LandScape")
                Box(modifier = Modifier.fillMaxSize()) {
                    WindowCompat.setDecorFitsSystemWindows(window, false)
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
                        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
                        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
                    } else {
                        window.insetsController?.apply {
                            hide(WindowInsets.Type.statusBars())
                            systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                            hide(WindowInsets.Type.navigationBars())
                        }
                    }

                    DisposableEffect(key1 = Unit) {
                        onDispose {
                            WindowCompat.setDecorFitsSystemWindows(window, true)
                            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
                                window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
                            } else {
                                window.insetsController?.apply {
                                    show(WindowInsets.Type.statusBars()) // Show status bar
                                    show(WindowInsets.Type.navigationBars())
                                }
                            }
                        }
                    }

                    /*Suppliers.
                    CacheLoader.from()
                    CacheBuilder.newBuilder().build()*/


                    // android view
                    PlayerSurface()
                    // controls
                    PlayerControl()
                    ShowUi()
                    Column(modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(bottom = 30.dp, start = 5.dp, end = 5.dp)) {
                        if (showUi){
                            Scroll()
                        }
                    }


                    // dummy
                }
            }else{


                Column(modifier = Modifier.fillMaxSize()) {
                    Box(modifier = Modifier
                        .aspectRatio(16f / 9f)
                        .fillMaxWidth()) {
                        //androidView
                        PlayerSurface()
                        PlayerControl()
                        ShowUi()
                        Column(modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(bottom = 0.dp, start = 5.dp, end = 5.dp)) {

                                Scroll()

                        }

                    }


                }





                /*Button(onClick = {
                    if (activity != null) {
                        activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
                    }
                }) {
                    
                    Text(text = "cooooooooooooming soooooooon")
                }*/
            }



           /* Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
                    .background(Color.Green.copy(alpha = 0.2f))
                    .padding(bottom = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(modifier = Modifier.fillMaxSize()) {

                    // android view


                    // controls


                    // dummy


                    ShowUi()
                    PlayerControl()
                }


            }*/


        }
    }


    fun androidView() {


    }


}


















package com.leo.wewatch.app.screens

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.SurfaceView
import androidx.annotation.OptIn
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.database.StandaloneDatabaseProvider
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.datasource.FileDataSource
import androidx.media3.datasource.cache.CacheDataSink
import androidx.media3.datasource.cache.CacheDataSource
import androidx.media3.datasource.cache.NoOpCacheEvictor
import androidx.media3.datasource.cache.SimpleCache
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import com.leo.wewatch.app.component.player.ui.Controls
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File


@Composable
@Preview
fun lol() {
    VideoPlayBackScreen()
}

@OptIn(UnstableApi::class)
@Composable
fun VideoPlayBackScreen(
    /*,downloadCache: Cache*/
    /*player: com.leo.wewatch.app.component.player.logic.Player*//*,videoUrl:String*/
) {
    val applicationContext: Context = LocalContext.current
    //val downloadContentDirectory = File(getExternalFilesDir(null),"")

    var showUi by rememberSaveable {
        mutableStateOf(false)
    }

    var isLoading by rememberSaveable {
        mutableStateOf(false)
    }
    var currentPosition by rememberSaveable {
        mutableLongStateOf(0L)
    }
    var isPlayingVideo by rememberSaveable {
        mutableStateOf(false)
    }

    var isEnded by rememberSaveable {
        mutableStateOf(false)
    }

    val exoPlayer: ExoPlayer = remember {
        ExoPlayer.Builder(applicationContext).build()
    }



    Box {
        Column {


            val simpleCache = remember {
                SimpleCache(
                    File(applicationContext.cacheDir.path, "thisIsChacke"),
                    NoOpCacheEvictor(),
                    StandaloneDatabaseProvider(applicationContext)
                )
            }




            LaunchedEffect(key1 = exoPlayer) {

                val mediaItem =
                    MediaItem.fromUri("https://file-examples.com/storage/fe0e2ce82f660c1579f31b4/2017/04/file_example_MP4_480_1_5MG.mp4")
                val mediaSource3 = DefaultMediaSourceFactory(
                    CacheDataSource.Factory()
                        .setCache(simpleCache)
                        .setCacheWriteDataSinkFactory(
                            CacheDataSink.Factory()
                                .setCache(simpleCache)
                        )
                        .setCacheReadDataSourceFactory(FileDataSource.Factory())
                        .setUpstreamDataSourceFactory(
                            DefaultDataSource.Factory(
                                applicationContext,
                                DefaultHttpDataSource.Factory()
                            )
                        )
                        .setFlags(CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR)
                ).createMediaSource(mediaItem)
                exoPlayer.setMediaSource(mediaSource3)


                Log.d("TAG", "munno")
                exoPlayer.prepare()
                exoPlayer.playWhenReady = true
            }

            // player.SetMediaSource(mediaSource3)
            //  player.StartPlay(LocalLifecycleOwner.current,exoPlayer)


            if (isPlayingVideo) {
                LaunchedEffect(key1 = Unit) {
                    launch {
                        //  Log.d("tag", "in launch")
                        while (isPlayingVideo) {
                            currentPosition = exoPlayer.currentPosition
                            // Log.d("tag", "looped")
                            delay(500)
                        }
                    }


                }

            }


             exoPlayer.videoScalingMode = C.VIDEO_SCALING_MODE_DEFAULT


            /* val surfaceView = SurfaceView(applicationContext)
             player.updateSurface(surfaceView.holder.surface)*/
            // surfaceView.clipToOutline = true
            AndroidView(
                factory = { SurfaceView(applicationContext) }, modifier = Modifier
                    .padding(1.dp)
                    .aspectRatio(16f / 9f)
                    .clip(
                        RoundedCornerShape(10.dp)
                    )
                    .clickable(enabled = false, onClick = {

                    }),
                update = { surfaceView1 ->  // This is the lambda you're asking about
                    //  player1.setVideoSurface(surfaceView1.holder.surface)
                    exoPlayer.setVideoSurface(surfaceView1.holder.surface)
                    //    player.updateSurface(surfaceView1.holder.surface)


                }
            )



            Button(onClick = {
                if (!exoPlayer.isPlaying) exoPlayer.play()
            }) {

                Text(text = "play")
            }
            Button(onClick = {
                Log.d("TAG", "VideoPlayBackScreen: button clicked for pause")
                if (exoPlayer.isPlaying) {
                    Log.d("TAG", "VideoPlayBackScreen: pause clicked")
                    exoPlayer.pause()
                }
            }) {
                Text(text = "pause")
            }
            //ShowHomeScreen()

        }



        exoPlayer.addListener(object : Player.Listener {


            override fun onIsPlayingChanged(isPlaying: Boolean) {
                isPlayingVideo = isPlaying

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


        val interactionSource = remember { MutableInteractionSource() }



        Box {

            Column(
                modifier = Modifier
                    .aspectRatio(16f / 9f)
                    .fillMaxSize()
                    .padding(5.dp)
                    .clickable(
                        onClick = {
                            showUi = !showUi
                            Handler(Looper.getMainLooper())
                                .postDelayed({
                                    if (isPlayingVideo && showUi) {
                                        showUi = false
                                        Log.d(
                                            "TAG",
                                            "VideoPlayBackScreen: surface automatically hides"
                                        )

                                    } else showUi = true

                                }, 4000)

                            //showUi != showUi


                        }, interactionSource = interactionSource,
                        indication = null
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,

                ) {

            }

            if (showUi) {
                Controls(

                    onCaptionClick = {},
                    onCastClick = {},
                    onMinimizeClick = {},
                    onNextClick = {},
                    onPauseClick = {

                        //if (player.isPlaying) player.pause()
                        exoPlayer.pause()
                        // pause the video
                    },
                    onPlayClick = {

                        //if (!player.isPlaying) player.play()
                        exoPlayer.play()
                        if (isPlayingVideo && showUi) {
                            Handler(Looper.getMainLooper())
                                .postDelayed({
                                    if (showUi) {
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
                        //Todo : function created.
                        exoPlayer.seekTo((it * exoPlayer.duration).toLong())
                    },
                    onRestartClick = {
                        //Todo : function created.
                        // player.seekTo(0)
                        //  player.reStart()

                    },
                    input = currentPosition,
                    videoDuration = exoPlayer.duration.toFloat(),
                    isPlaying = isPlayingVideo,
                    isEnded = isEnded,
                    isLoading = isLoading
                )
            }
        }


    }
}












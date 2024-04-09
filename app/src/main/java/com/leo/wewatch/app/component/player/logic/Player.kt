package com.leo.wewatch.app.component.player.logic

import android.content.Context
import android.util.Log
import android.view.Surface
import android.view.SurfaceView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.cache.CacheDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.MediaSource
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.jetbrains.annotations.ApiStatus.Experimental
import javax.inject.Inject

//1. initialize
//2. set media source
//3. StartPlay
//4.


@UnstableApi
@HiltViewModel
class Player @Inject constructor(

   /* private val cacheDataSourceFactory: CacheDataSource.Factory,*/
    var player: ExoPlayer
) : ViewModel() {


    //var player : ExoPlayer? = null
   // lateinit var player: ExoPlayer



    fun getExoPlayerBuilder(context: Context): ExoPlayer {
        player = ExoPlayer.Builder(context).setTrackSelector(
            DefaultTrackSelector(context)
        ).build()

        return player
    }

    /* var duration = player.duration*/
    private var listener: Player.Listener? = null
    fun currentPosition(): Long {
        return player.currentPosition
    }

    fun preparePlayer() {
        player.prepare()
    }

    fun getDuration(): Long {
        return player.duration
    }

   /* fun getChacheDataSourceFactory(): CacheDataSource.Factory {
        return cacheDataSourceFactory
    }*/

    @Composable
    fun StartPlay(lifecycleOwner: LifecycleOwner, key : Any) {
      /* lateinit var lifecycleObserver : LifecycleEventObserver*/


            LaunchedEffect(key1 = key) {

                launch {
                    Log.d("TAG", "StartPlay: in Launch Effect")
                    player.prepare()
                    player.playWhenReady = true
                }
                 /*lifecycleObserver = LifecycleEventObserver { source, event ->
                    when (event) {
                        Lifecycle.Event.ON_PAUSE -> {
                            Log.d("TAG", "StartPlay: in onPause")
                            player.pause()
                        }

                        Lifecycle.Event.ON_STOP -> {
                            Log.d("TAG", "StartPlay: in onStop")
                            player.pause()
                        }

                        Lifecycle.Event.ON_START -> {
                            Log.d("TAG", "StartPlay: in onStart")
                            player.playWhenReady = true
                            // duration = player.duration
                            player.prepare()
                            if (!player.isPlaying) {
                                // Prepare if not already prepared
                                player.play()

                            }
                        }

                        Lifecycle.Event.ON_RESUME -> {
                            Log.d("TAG", "StartPlay: in OnResume")

                        }

                        Lifecycle.Event.ON_DESTROY -> {
                            Log.d("TAG", "StartPlay: in OnDestroy")
                        }

                        else -> {}
                    }
                }

                lifecycleOwner.lifecycle.addObserver(lifecycleObserver)*/

            }






            /*onDispose {

            }*/

              /* duration = player.duration*/
              /* power(true)*/
            // Set flag after preparation
            //player.media



        DisposableEffect(Unit) {
            onDispose {
                /*if (lifecycleObserver != null){
                    lifecycleOwner.lifecycle.removeObserver(lifecycleObserver!!)
                }*/
                /*lifecycleOwner.lifecycle.removeObserver(lifecycleObserver)*/

              //  player.stop()
                player.release()


                // player.clearVideoSurface()
                // player.removeMediaItem(0)
                /*if (listener != null) {
                    player.removeListener(listener!!)
                }*/

                Log.d("TAG", "StartPlay: in OnDispose")
                //player.release()
            }
        }


    }



    @Composable
    fun SetMediaSource(mediaSource: MediaSource) {
        LaunchedEffect(key1 = true) {
            player.setMediaSource(mediaSource)
            player.prepare()
        }


    }

    fun setMediaSource(mediaSource: MediaSource) {
        player.setMediaSource(mediaSource)
    }


    @Experimental
    fun getSurface(): SurfaceView? {
        return null
    }

    fun addListener(listener: Player.Listener) {
        player.addListener(listener)
        this.listener = listener
        // player.mediaS
    }

    fun updateSurface(surface: Surface) {
        player.setVideoSurface(surface)
    }

    fun play() {
        if (!player.isPlaying) {
            player.play()
            updateUi()
        }

        /* //play the vid
         if (!player.isPlaying &&
             !player.isLoading &&
             player.playbackState == Player.STATE_READY &&
             player.playbackState != Player.STATE_BUFFERING) player.play()


         // pause the video
         if (player.isPlaying &&
             !player.isLoading &&
             player.playbackState != Player.STATE_BUFFERING
             ) player.pause()*/
    }

    fun pause() {
        if (player.isPlaying) player.pause()
        player.playWhenReady = false
    }

    fun reStart() {
        player.seekTo(0)
        player.playWhenReady = true

    }

    fun seekTo(duration: Long) {
        player.seekTo((duration * player.duration))
    }

    private fun updateUi() {

    }


}
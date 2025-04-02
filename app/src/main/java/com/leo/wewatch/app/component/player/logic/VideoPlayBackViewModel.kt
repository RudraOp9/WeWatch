package com.leo.wewatch.app.component.player.logic

import android.content.Context
import android.util.Log
import android.view.Surface
import android.view.SurfaceView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.cache.CacheDataSource
import androidx.media3.exoplayer.DefaultLoadControl
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.SeekParameters
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import androidx.media3.exoplayer.source.MediaSource
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector
import androidx.media3.exoplayer.upstream.DefaultAllocator
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
class VideoPlayBackViewModel @Inject constructor(

    private val cacheDataSourceFactory: CacheDataSource.Factory,
    var player: ExoPlayer
) : ViewModel() {


    var screenHeight : Int = 0
    var playerHeight : Int =  0


    //var player : ExoPlayer? = null
    // lateinit var player: ExoPlayer

    fun playSpeed(speed: Float) {
        viewModelScope.launch {

        }
        //player.mediaItemCount
        // player.currentMediaItemIndex
        player.setPlaybackSpeed(speed)


    }

    private val MIN_BUFFER_DURATION = 2000

    //Max Video you want to buffer during PlayBack
    private val MAX_BUFFER_DURATION = 15000

    //Min Video you want to buffer before start Playing it
    private val MIN_PLAYBACK_START_BUFFER = 1500

    //Min video You want to buffer when user resumes video
    private val MIN_PLAYBACK_RESUME_BUFFER = 2000

    fun setUpExoPlayer(context: Context) {

        player = ExoPlayer.Builder(context).setTrackSelector(
            DefaultTrackSelector(context)
        ).setLoadControl(
            DefaultLoadControl.Builder().setAllocator(DefaultAllocator(true, 16))
                .setBufferDurationsMs(
                    MIN_BUFFER_DURATION,
                    MAX_BUFFER_DURATION,
                    MIN_PLAYBACK_START_BUFFER,
                    MIN_PLAYBACK_RESUME_BUFFER
                ).setTargetBufferBytes(-1).setPrioritizeTimeOverSizeThresholds(true).build()
        ).setSeekParameters(SeekParameters(SeekParameters.CLOSEST_SYNC.toleranceBeforeUs,SeekParameters.CLOSEST_SYNC.toleranceAfterUs)).build()

        player.videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT


    }

    /* var duration = player.duration*/
    private var listener: Player.Listener? = null


    /**
     * Returns the current position of video playback in milliseconds (Long)
     * */
    fun currentPosition(): Long {

        return player.currentPosition
    }

    fun preparePlayer() {
        player.prepare()
    }

    fun getDuration(): Long {
        // player.mediaItem

        return player.duration
    }

    fun getChacheDataSourceFactory(): CacheDataSource.Factory {
        return cacheDataSourceFactory
    }


    fun SetUri(
        uri: String
    ) {


        val mediaItem =
            MediaItem.fromUri(uri)
        val mediaSource3 =
            DefaultMediaSourceFactory(cacheDataSourceFactory).createMediaSource(mediaItem)
        player.setMediaSource(mediaSource3)


    }


    fun prepareExoPlayer(lifecycleOwner: LifecycleOwner) {
        lateinit var lifecycleObserver: LifecycleEventObserver


        Log.d("TAG", "prepareExoPlayer : in Launch Effect")
        player.prepare()
        player.playWhenReady = true
        lifecycleObserver = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_PAUSE -> {
                    Log.d("TAG", "StartPlay: in onPause")
                    // player.pause()
                }

                Lifecycle.Event.ON_STOP -> {
                    Log.d("TAG", "StartPlay: in onStop")
                    player.stop()
                }

                Lifecycle.Event.ON_START -> {
                    Log.d("TAG", "StartPlay: in onStart")
                    player.prepare()
                    if (!player.isPlaying && player.playWhenReady) {
                        player.play()
                    }
                }

                Lifecycle.Event.ON_RESUME -> {
                    Log.d("TAG", "StartPlay: in OnResume")
                    if (player.playWhenReady) {
                        player.play()
                    }
                }

                Lifecycle.Event.ON_DESTROY -> {
                    Log.d("TAG", "StartPlay: in OnDestroy")
                    lifecycleOwner.lifecycle.removeObserver(lifecycleObserver)
                }

                else -> {}
            }
        }

        lifecycleOwner.lifecycle.addObserver(lifecycleObserver)


        /*if (lifecycleObserver != null){
            lifecycleOwner.lifecycle.removeObserver(lifecycleObserver!!)
        }*/
        /*lifecycleOwner.lifecycle.removeObserver(lifecycleObserver)*/
        //  player.stop()
        //   player.release()
        // player.clearVideoSurface()
        // player.removeMediaItem(0)
        /*if (listener != null) {
            player.removeListener(listener!!)
        }*/
        Log.d("TAG", "StartPlay: in OnDispose")
        //player.release()


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
        player.seekTo((duration))
    }

    private fun updateUi() {

    }

    fun release() {
        player.release()

        listener?.let { player.removeListener(it) }
        onCleared()
    }


}
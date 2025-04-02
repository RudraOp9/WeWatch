package com.leo.wewatch.app.screens.videoPlayBackScreen

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.SurfaceView
import android.view.Window
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.annotation.OptIn
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Block
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material.icons.outlined.FileDownload
import androidx.compose.material.icons.outlined.Flag
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.ThumbDown
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import com.leo.wewatch.R
import com.leo.wewatch.app.component.makeSpace
import com.leo.wewatch.app.component.player.ui.Controls
import com.leo.wewatch.app.component.player.ui.ScrollWithControl
import com.leo.wewatch.logic.utils.timeFormat
import com.leo.wewatch.logic.utils.viewsFormat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

fun Context.findActivity(): Activity? = when (this) {
    is Activity -> {
        Log.d("ori", "findActivity: Activity")
        this
    }

    is ContextWrapper -> {
        Log.d("ori", "findActivity: baseContext")
        baseContext.findActivity()

    }

    else -> null
}

@kotlin.OptIn(ExperimentalFoundationApi::class)
@OptIn(UnstableApi::class)
@Composable
fun VideoPlayBackScreen(
    context: Context,

    window: Window
) {

    @Composable
    fun Int.toDp(): Dp {

        with(LocalDensity.current) {
            return this@toDp.toDp()
        }
    }

    val videoPlayBackViewModel =
        hiltViewModel<com.leo.wewatch.app.component.player.logic.VideoPlayBackViewModel>()

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

    Column(modifier = Modifier
        .fillMaxSize()
        .onGloballyPositioned {
            videoPlayBackViewModel.screenHeight = it.size.height
        }) {
        LaunchedEffect(key1 = context) {
            videoPlayBackViewModel.setUpExoPlayer(context)
            videoPlayBackViewModel.addListener(object : Player.Listener {


                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    isPlayingVideo = isPlaying
                }

                override fun onRenderedFirstFrame() {
                    vidDuration = videoPlayBackViewModel.getDuration()
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
            videoPlayBackViewModel.SetUri(uri = uri)
            videoPlayBackViewModel.prepareExoPlayer(lifecycleOwner)
            vidDuration = videoPlayBackViewModel.getDuration()
            Log.d("TAG", vidDuration.toString())
        }
        DisposableEffect(Unit) {
            onDispose {
                videoPlayBackViewModel.release()
            }
        } // release the player on dispose


        /* player.position.observe(LocalLifecycleOwner.current){
             if (it != null) {
                 currentPosition = it
             }
         }*/
        //  currentPosition = player


        LaunchedEffect(key1 = isPlayingVideo) {
            while (isPlayingVideo) {
                withContext(Dispatchers.Main) {
                    currentPosition = videoPlayBackViewModel.currentPosition()
                    // Access the player object here
                }
                //
                delay(100)
            }

            /* launch {
                 //  Log.d("tag", "in launch")
                 while (isPlayingVideo) {

                      Log.d(
                             "tag",
                             "looped crr Pos : $currentPosition , vid dur : ${player.getDuration()}"
                         )
                        delay(500)
                    }
                }*/
        }

        // updating the slider

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
                        videoPlayBackViewModel.pause()
                    },
                    onPlayClick = {
                        //if (!player.isPlaying) player.play()
                        videoPlayBackViewModel.play()
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
                        videoPlayBackViewModel.seekTo((it * vidDuration).toLong())
                        //  currentPosition = (it * vidDuration).toLong()
                    },
                    onRestartClick = {
                        videoPlayBackViewModel.reStart()

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
            videoPlayBackViewModel.playSpeed(1f)
        }

        var backColor = Color.Transparent

        backColor = if (showUi) {
            MaterialTheme.colorScheme.scrim
        } else Color.Transparent

        /*  var dunno = LocalContext.current as Activity

          dunno.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE*/
        @Composable
        fun PlayerControl() {
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
                                videoPlayBackViewModel.playSpeed(2f)
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
        fun PlayerSurface() {
            AndroidView(factory = { SurfaceView(context) }, modifier = Modifier
                .fillMaxWidth()
                /*
                .aspectRatio(1280f / 720f, true)
*/
                .clip(
                    RoundedCornerShape(1.dp)
                )
                .onGloballyPositioned {
                    videoPlayBackViewModel.playerHeight = it.size.height
                }/*.align(Alignment.TopCenter)*/,
                update = {
                    Log.d("TAG", "VideoPlayBackScreen: ${it.rotation}")
                    //  it.rotation = 90f


                    videoPlayBackViewModel.updateSurface(it.holder.surface)
                }
            )

        }


        @Composable
        fun Scroll() {
            ScrollWithControl(
                vidDuration.toFloat(),
                currentPosition,
                landScapeOrientation,
                onValueChange = {
                    Log.d(
                        "TAG",
                        "VideoPlayBackScreen: valchange : $it , to seek : ${it * vidDuration} , vid Duration : v=$vidDuration"
                    )
                    currentPosition = (it * vidDuration).toLong()
                    videoPlayBackViewModel.seekTo((it * vidDuration).toLong())

                },
                onOrientationConfig = {
                    if (landScapeOrientation) {
                        // oriConfig.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
                        if (activity != null) {
                            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                            activity.requestedOrientation =
                                ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
                        }
                    } else {
                        if (activity != null) {
                            activity.requestedOrientation =
                                ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
                        }
                    }
                },
                showUi = showUi
            )
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
                        hide(android.view.WindowInsets.Type.statusBars())
                        systemBarsBehavior =
                            WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                        hide(android.view.WindowInsets.Type.navigationBars())
                    }
                }

                DisposableEffect(key1 = Unit) {
                    onDispose {
                        WindowCompat.setDecorFitsSystemWindows(window, true)
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
                            window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
                        } else {
                            window.insetsController?.apply {
                                show(android.view.WindowInsets.Type.statusBars()) // Show status bar
                                show(android.view.WindowInsets.Type.navigationBars())
                            }
                        }
                    }
                }
                // android view
                PlayerSurface()
                // controls
                PlayerControl()
                ShowUi()
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(bottom = 30.dp, start = 5.dp, end = 5.dp)
                ) {
                    if (showUi) {
                        Scroll()
                    }
                }


                // dummy
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
            ) {
                Box(
                    modifier = Modifier
                        .aspectRatio(16f / 9f)
                        .fillMaxWidth()
                ) {
                    //androidView
                    PlayerSurface()
                    PlayerControl()
                    ShowUi()
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(bottom = 0.dp, start = 5.dp, end = 5.dp)
                    ) {
                        Scroll()
                    }
                }
                Text(
                    text = "This is an Title.",
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(start = 10.dp, top = 15.dp),
                    maxLines = 2,
                    color = Color.White
                )

                Text(
                    text = "${viewsFormat(954684654)} views ${timeFormat(5644155746)} ... *more*",
                    fontSize = 11.sp,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(start = 10.dp, top = 5.dp),
                    color = Color.Gray
                )
                //  makeSpace(top = 5.dp)
                Row {
                    Image(
                        painter = painterResource(id = R.drawable.ic_launcher_background),
                        contentDescription = "channelName's channel logo",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .padding(start = 10.dp)
                            .clip(CircleShape)
                            .size(34.dp)
                            .align(Alignment.CenterVertically)
                            .clickable(enabled = true, onClick = { })
                    )
                    /*  */
                    Text(
                        text = "Steve Cutts",
                        fontWeight = FontWeight.Medium,
                        fontSize = 13.sp,
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(start = 10.dp)
                    )
                    Text(
                        text = viewsFormat(1866458),
                        color = Color.Gray,
                        fontSize = 10.sp,
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(start = 5.dp)
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Button(
                            onClick = { /*TODO*/ },
                            modifier = Modifier
                                .padding(end = 10.dp),
                            contentPadding = PaddingValues(
                                start = 20.dp,
                                end = 20.dp,
                                top = 0.dp,
                                bottom = 0.dp
                            ),
                            colors = ButtonColors(
                                Color.White,
                                Color.Black,
                                Color.White,
                                Color.Black.copy(alpha = 0.95f)
                            )
                        ) {

                            Text(text = "Subscribe", fontSize = 11.sp)
                        }
                    }
                }
                makeSpace(top = 10.dp)

                Utilities()
                makeSpace(top = 20.dp)
                var showComments by rememberSaveable {
                    mutableStateOf(false)
                }
                if (showComments) {
                    CommentSheet(videoPlayBackViewModel.screenHeight - videoPlayBackViewModel.playerHeight , videoPlayBackViewModel.screenHeight, videoPlayBackViewModel.playerHeight) {
                        showComments = false
                    }
                }
                Column(
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                        .clickable(onClick = {
                            showComments = true
                        })
                        .background(Color.White.copy(0.1f), RoundedCornerShape(10.dp))
                        .padding(10.dp)

                ) {
                    Row {
                        Text(
                            text = "Comments", fontSize = 13.sp, fontWeight = FontWeight.Medium,
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                        )
                        Text(
                            text = viewsFormat(50000), fontSize = 11.sp, color = Color.Gray,
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                                .padding(start = 5.dp)
                        )
                    }
                    makeSpace(top = 10.dp)
                    Row {


                        Image(
                            painter = painterResource(id = R.drawable.ic_launcher_background),
                            contentDescription = "channelName's channel logo",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .clip(CircleShape)
                                .size(26.dp)
                                .clickable(enabled = true, onClick = { })
                        )

                        makeSpace(start = 5.dp)
                        Text(
                            text = "hello bunny i am your big fan , please pin me , thankyou very much  , you make lol videos :)",
                            maxLines = 1,
                            fontSize = 11.sp,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                }
            }
        }
    }
}


@Composable
fun Utilities() {
    val commModifier = Modifier
        .background(Color.White.copy(alpha = 0.1f), shape = RoundedCornerShape(48))
        .padding(vertical = 7.dp, horizontal = 7.dp)
    Row(
        modifier = Modifier
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 10.dp)
    ) {
        Row(
            modifier = commModifier
        ) {
            //   HomeIcons(painter = Icons.Outlined.ThumbUp)
            Icon(Icons.Outlined.ThumbUp, null)
            Text(
                viewsFormat(2655464), fontSize = 12.sp, modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(start = 10.dp, end = 5.dp)
            )
            VerticalDivider(
                color = Color.Black.copy(alpha = 0.3f),
                modifier = Modifier
                    .heightIn(max = 24.dp)
                    .padding(horizontal = 10.dp)
            )
            Icon(imageVector = Icons.Outlined.ThumbDown, contentDescription = null)
        }
        makeSpace(start = 5.dp)
        LeadingIconButton(modifier = commModifier, icon = Icons.Outlined.Share, text = "Share")
        makeSpace(start = 5.dp)
        LeadingIconButton(modifier = commModifier, icon = Icons.Outlined.Create, text = "Remix")
        makeSpace(start = 5.dp)
        LeadingIconButton(
            modifier = commModifier,
            icon = Icons.Outlined.FileDownload,
            text = "Download"
        )
        makeSpace(start = 5.dp)
        LeadingIconButton(modifier = commModifier, icon = Icons.Outlined.Block, text = "Stop ads")
        makeSpace(start = 5.dp)
        LeadingIconButton(
            modifier = commModifier,
            icon = Icons.Outlined.BookmarkBorder,
            text = "Save"
        )
        makeSpace(start = 5.dp)
        LeadingIconButton(modifier = commModifier, icon = Icons.Outlined.Flag, text = "Report")
    }

}

@Composable
fun LeadingIconButton(
    startPadding: Dp = 5.dp,
    modifier: Modifier,
    icon: ImageVector,
    text: String
) {
    //
    Row(modifier = modifier) {
        Icon(icon, contentDescription = null)
        Text(
            text = text,
            fontSize = 11.sp,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(start = startPadding)
        )
    }

}


@kotlin.OptIn(ExperimentalMaterial3Api::class)

@Composable
fun CommentSheet(height: Int, screenHeight: Int, dismissSheet1: Int, dismissSheet: () -> Unit) {


    BottomSheetScaffold(sheetContent = {}) {

    }
    ModalBottomSheet(
        sheetState = rememberModalBottomSheetState().apply {

            LaunchedEffect(key1 = Unit) {
                this@apply.expand()

            }

        },
        windowInsets = WindowInsets(top = dismissSheet1),
        scrimColor = Color.Transparent,
        containerColor = Color.DarkGray,
        contentColor = Color.LightGray,
        modifier = Modifier
            .heightIn(min = with(LocalDensity.current  ) {
                height.toDp() + 63.toDp()
            }, max = with(LocalDensity.current){screenHeight.toDp()})
        /*.offset(0.dp, -1.dp)*/,
        shape = BottomSheetDefaults.ExpandedShape,

        dragHandle = {
            BottomSheetDefaults.DragHandle(color = Color.LightGray)
        },
        onDismissRequest = dismissSheet
    ) {

        Text(text = "kldlkfs")
    }
}
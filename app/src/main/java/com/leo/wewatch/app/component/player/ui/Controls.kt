package com.leo.wewatch.app.component.player.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderColors
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leo.wewatch.R
import com.leo.wewatch.app.component.makeSpace
import com.leo.wewatch.app.screens.homescreen.ui.HomeIcons
import com.leo.wewatch.logic.utils.timeFormat
import com.leo.wewatch.ui.theme.LightRed

@Preview
@Composable
fun ControlsUi() {
    //  Controls()
}


@Composable
fun Controls(
    onCaptionClick: () -> Unit,
    onMinimizeClick: () -> Unit,
    onNextClick: () -> Unit,
    onPauseClick: () -> Unit,
    onPlayClick: () -> Unit,
    onCastClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onPreviousClick: () -> Unit,
    onRestartClick: () -> Unit,
    videoDuration: Float,
    input: Long,
    title: String = " Hello this is an sample title of an sample video i don't know what this video is and do not care about it.",
    onValueChange: (Float) -> Unit,
    isPlaying: Boolean,
    isEnded: Boolean,
    isLoading: Boolean
) {

    val modifier = Modifier
        .padding(5.dp)
        .background(
            Color.LightGray.copy(alpha = 0.15f), shape = CircleShape
        )
        .padding(5.dp)
        .size(40.dp)
        .padding(2.dp)



    Row(
        modifier = Modifier
            .padding(top = 5.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically

    ) {
        HomeIcons(
            painter = painterResource(id = R.drawable.icon_keyboard_arrow_down_24),
            modifier = Modifier.clickable(enabled = true, onClick = onMinimizeClick),
            color = Color.White
        )

        Text(
            text = title,
            fontSize = 20.sp,
            onTextLayout = {

            },
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            fontWeight = FontWeight.Medium,
            color = Color.White,
            modifier = Modifier
                .padding(end = 5.dp)
                .weight(1f)
        )

        Row(
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically,
            /* modifier = Modifier.fillMaxWidth()*/

        ) {
            /*IconToggleButton(checked = true, onCheckedChange ={} ) {

            }*/

            HomeIcons(
                painter = (painterResource(id = R.drawable.icon_cast_24)),
                color = Color.White,
                modifier = Modifier.clickable(enabled = true, onClick = onCastClick)
            )
            makeSpace(end = 20.dp)
            HomeIcons(
                painter = painterResource(id = R.drawable.icon_closed_caption_off_24),
                color = Color.White,
                modifier = Modifier.clickable(enabled = true, onClick = onCaptionClick)
            )
            makeSpace(end = 20.dp)
            HomeIcons(
                painter = painterResource(id = R.drawable.icon_outline_settings_24),
                color = Color.White,
                modifier = Modifier.clickable(enabled = true, onClick = onSettingsClick)
            )
            makeSpace(end = 10.dp)
        }
    }
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxSize()


    ) {
        HomeIcons(
            painter = painterResource(id = R.drawable.icon_skip_previous_24),
            modifier = modifier.clickable(enabled = true, onClick = onPreviousClick),
            color = Color.White,

            )
        Box {

            if (!isEnded) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = Color.White, trackColor = Color.Transparent
                    )
                } else {
                    if (isPlaying) {
                        HomeIcons(
                            painter = painterResource(id = R.drawable.icon_pause_24),
                            modifier = modifier.clickable(
                                enabled = true, onClick = onPauseClick
                            ),
                            color = Color.White
                        )
                    } else {
                        HomeIcons(
                            painter = painterResource(id = R.drawable.icon_play_arrow_24),
                            modifier = modifier.clickable(enabled = true, onClick = onPlayClick),
                            color = Color.White
                        )
                    }
                }
            } else {
                HomeIcons(
                    painter = painterResource(id = R.drawable.icon_restart_24),
                    modifier = Modifier.clickable(enabled = true, onClick = onRestartClick),
                    color = Color.White
                )
            }


        }


        HomeIcons(
            painter = painterResource(id = R.drawable.icon_skip_next_24),
            modifier = modifier.clickable(enabled = true, onClick = onNextClick),
            color = Color.White
        )


    }

    /* Row(
         modifier = Modifier
             .fillMaxSize()
         *//*.aspectRatio(16f / 9f)*//*,
        verticalAlignment = Alignment.Bottom
        *//*, horizontalArrangement = Arrangement.*//*
        *//* horizontalAlignment = Alignment.CenterHorizontally,*//*


    ) {


        *//*Column(verticalArrangement = Arrangement.Bottom , modifier = Modifier.align(Alignment.Start)) {


            ScrollWithControl(videoDuration, input, onValueChange)
        }*//*

    }*/

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScrollWithControl(
    videoDuration: Float,
    input: Long,
    isLandscape: Boolean,
    showUi: Boolean,
    onValueChange: (Float) -> Unit,
    onOrientationConfig: () -> Unit
) {/*val interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
    interactionSource.collectIsDraggedAsState()*/

    Column {

        if (showUi) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .padding(bottom = 10.dp)
            ) {
                Text(
                    text = timeFormat((input / 1000).toInt()), color = Color.White, fontSize = 12.sp
                )
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    Text(
                        text = timeFormat((videoDuration / 1000).toInt()),
                        color = Color.White,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(end = 5.dp)
                    )

                    HomeIcons(
                        painter = if (isLandscape) painterResource(id = R.drawable.icon_fullscreen_exit_24) else painterResource(
                            id = R.drawable.icon_fullscreen_enter_24
                        ),
                        color = Color.White,
                        modifier = Modifier.clickable(enabled = true, onClick = onOrientationConfig)
                    )
                }
            }
        }
        Box {

            var colors2: SliderColors
            if (showUi) {
                colors2 = SliderDefaults.colors(
                    thumbColor = Color.Red,
                    activeTrackColor = LightRed,
                    inactiveTrackColor = Color.White.copy(alpha = 0.2f)
                )
            } else {
                colors2 = SliderDefaults.colors(
                    thumbColor = Color.Transparent,
                    activeTrackColor = Color.White,
                    inactiveTrackColor = Color.White.copy(alpha = 0.2f)
                )
            }



            Slider(
                value = 0.8f,
                onValueChange = { },
                enabled = false,
                valueRange = 0f..1f,
                colors = SliderDefaults.colors(
                    activeTrackColor = Color.White.copy(alpha = 0.4f),
                    inactiveTrackColor = Color.Transparent.copy(alpha = 0f),
                    disabledThumbColor = Color.Transparent

                ),
                thumb = {

                },

                modifier = Modifier
                    .padding(horizontal = 1.dp)
                    .height((0.5).dp)


            )


            //SliderDefaults.Track(sliderState = )
            val a = remember {
                MutableInteractionSource()
            }

            var thumbColor by remember {
                mutableStateOf(Color.Red)
            }
            var activeTrackColor by remember {
                mutableStateOf(Color.Red)
            }
            var inactiveTrackColor by remember {
                mutableStateOf(Color.Transparent)
            }

            var thumbSize by remember {
                mutableStateOf(DpSize.Zero)
            }

            if (a.collectIsDraggedAsState().value || a.collectIsHoveredAsState().value || a.collectIsPressedAsState().value || showUi) {
                thumbSize = DpSize(15.dp, 15.dp)
                thumbColor = Color.Red
                activeTrackColor = Color.Red

            } else {
                thumbSize = DpSize(0.dp, 0.dp)
                thumbColor = Color.Transparent
                activeTrackColor = Color.White
                inactiveTrackColor = Color.White.copy(alpha = 0.2f)

            }



            Slider(

                value = input / videoDuration,
                onValueChange = onValueChange,
                enabled = true,
                valueRange = 0f..1f,
                interactionSource = a,
                thumb = {
                    SliderDefaults.Thumb(
                        interactionSource = a,

                        thumbSize = thumbSize,
                        colors = SliderDefaults.colors(
                            thumbColor = thumbColor,
                            activeTrackColor = activeTrackColor,
                            inactiveTrackColor = inactiveTrackColor
                        )
                    )
                },
                colors = SliderDefaults.colors(
                    thumbColor = thumbColor,
                    activeTrackColor = activeTrackColor,
                    inactiveTrackColor = inactiveTrackColor
                ) ,
                modifier = Modifier
                    .padding(horizontal = 1.dp)
                    .height((0.1).dp)


            )
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun Screen() {
    Column(modifier = Modifier.fillMaxSize()) {


    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Pager() {

    /*ModalDrawerSheet(

        drawerShape = RoundedCornerShape(20.dp),
        drawerTonalElevation = 10.dp,
        drawerContainerColor = Color.Black,
        drawerContentColor = Color.White,
        windowInsets = WindowInsets(10.dp)

    ) {

    }*/
}

@Preview
@Composable
fun Anothertest() {
    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {


        Text(
            text = "Hello this is an sample title of an sample video i don't know what this video is and do not care about it",
            fontSize = 20.sp,
            onTextLayout = {

            },
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            fontWeight = FontWeight.Medium,
            color = Color.Black,
            modifier = Modifier
                .padding(end = 5.dp)
                .weight(1f)
        )


        Row(
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically,
            /*   modifier = Modifier.fillMaxWidth()*/

        ) {
            /*IconToggleButton(checked = true, onCheckedChange ={} ) {

            }*/

            HomeIcons(
                painter = (painterResource(id = R.drawable.icon_cast_24)),
                color = Color.Black,
                modifier = Modifier.clickable(enabled = true, onClick = { })
            )
            makeSpace(end = 20.dp)
            HomeIcons(
                painter = painterResource(id = R.drawable.icon_closed_caption_off_24),
                color = Color.Black,
                modifier = Modifier.clickable(enabled = true, onClick = { })
            )
            makeSpace(end = 20.dp)
            HomeIcons(
                painter = painterResource(id = R.drawable.icon_outline_settings_24),
                color = Color.Black,
                modifier = Modifier.clickable(enabled = true, onClick = { })
            )
            makeSpace(end = 10.dp)
        }
    }
}
package com.leo.wewatch.app.component.player.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.leo.wewatch.R
import com.leo.wewatch.app.screens.homescreen.ui.HomeIcons
import com.leo.wewatch.app.component.makeSpace
import com.leo.wewatch.ui.theme.LightRed
import com.leo.wewatch.ui.theme.back

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
    onValueChange: (Float) -> Unit,
    isPlaying: Boolean,
    isEnded: Boolean,
    isLoading: Boolean
) {

    val modifier = Modifier
        .background(
            Color.LightGray.copy(alpha = 0.1f),
            shape = CircleShape
        )
        .size(40.dp)
        .padding(2.dp)


    Column(
        modifier = Modifier
            .aspectRatio(16f / 9f)
            .fillMaxSize()
            .padding(5.dp),
        horizontalAlignment = Alignment.CenterHorizontally,

        ) {
        Row(
            modifier = Modifier,
            horizontalArrangement = Arrangement.Start,
        ) {
            HomeIcons(
                painter = painterResource(id = R.drawable.icon_keyboard_arrow_down_24),
                modifier = Modifier.clickable(enabled = true, onClick = onMinimizeClick),
                color = Color.White
            )
            Row(
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
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
                    painter = painterResource(id = R.drawable.icon_settings_24),
                    color = Color.White,
                    modifier = Modifier.clickable(enabled = true, onClick = onSettingsClick)
                )
                makeSpace(end = 10.dp)
            }
        }
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()

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
                            color = Color.White,
                            trackColor = Color.Transparent
                        )
                    } else {
                        if (isPlaying) {
                            HomeIcons(
                                painter = painterResource(id = R.drawable.icon_pause_24),
                                modifier = modifier.clickable(
                                    enabled = true,
                                    onClick = onPauseClick
                                ),
                                color = Color.White
                            )
                        } else {
                            HomeIcons(
                                painter = painterResource(id = R.drawable.icon_play_arrow_24),
                                modifier = modifier
                                    .clickable(enabled = true, onClick = onPlayClick)
                                    .background(shape = RoundedCornerShape(10), color = back),
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

        Scroll(videoDuration, input, onValueChange)
    }

}


@Composable
fun Scroll(videoDuration: Float, input: Long, onValueChange: (Float) -> Unit) {

    Slider(

        value = input / videoDuration,
        onValueChange = onValueChange,
        enabled = true,
        valueRange = 0f..1f,
        colors = SliderDefaults.colors(
            thumbColor = Color.Red,
            activeTrackColor = LightRed,
            inactiveTrackColor = Color.Transparent
        ),
        modifier = Modifier.padding(horizontal = 10.dp)


    )
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
package com.leo.wewatch.app.component

import android.net.Uri

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import coil.compose.AsyncImage
import com.leo.wewatch.R


@Composable
fun ThumbnailView(
    videoLength: String,
  //  lengthSize: Float = 13f,
    // painter: Painter,
    uri: Uri ,
    contentDescription: String = "video",
    progress: Float,
    modifier: Modifier,

) {
    Box {

        AsyncImage(
            model = uri,
            contentDescription = contentDescription,
            contentScale = ContentScale.FillWidth,
            // painter = painter,
            // contentDescription = contentDescription,
            modifier = modifier.aspectRatio(16f / 9f),
        )

        Text(
            text = videoLength,
            fontWeight = FontWeight.SemiBold,
            color = Color.White,
            fontSize = TextUnit(13f, TextUnitType.Sp),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 10.dp, end = 5.dp)
                .background(
                    Color(red = 0f, green = 0f, blue = 0f, alpha = 0.5f),
                    shape = RoundedCornerShape(10f)
                )
                .padding(4.dp)
        )
        LinearProgressIndicator(
            progress = progress,
            color = Color.Red,
            trackColor = Color(red = 1f, green = 1f, blue = 1f, alpha = 0.5f),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        )
    }

}


@Composable
fun VideoDetail(
    title: String,
    channelName: String,
    channelLogo: Painter,
    views: String,
    timePublished: String,
    modifier: Modifier,
    onChannelClick : () -> Unit,
    onMoreMenuClick : () -> Unit

) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {

        val modifier1 : Modifier = Modifier

        Image(
            painter = channelLogo,
            contentDescription = "$channelName's channel logo",
            contentScale = ContentScale.Fit,
            alignment = Alignment.TopStart,
            modifier = Modifier
                .padding(5.dp)
                .clip(CircleShape)
                .size(40.dp)
                .clickable(enabled = true, onClick = onChannelClick)
        )

        makeSpace(start = 10.dp)

        Column(modifier= Modifier.weight(0.9f)){
            Text(text = title, fontWeight = FontWeight.SemiBold,     modifier = modifier)
            makeSpace(top = 5.dp)
            Row {
                Text(text = "$channelName  · $views views  · $timePublished", fontSize = 15.sp)
            }

        }


        Image(
            painter = painterResource(id = R.drawable.icon_more_vert_24),
            contentDescription = null,
            alignment = Alignment.TopEnd,
            modifier = modifier1.padding(top = 12.dp).clickable(enabled = true, onClick = onMoreMenuClick)
        )

       /* DropdownMenu(
            expanded = false,
            onDismissRequest = {  },
            modifier = modifier
                .clickable(enabled = true, onClick = onClick)
                .weight(0.05f).background(Color.White),
            properties = PopupProperties(focusable = true)
        ) {
            Text(text = "joo")
        }*/

    }

}



@Composable
fun makeSpace(top: Dp = 0.dp, bottom: Dp = 0.dp, end: Dp = 0.dp, start: Dp = 0.dp) {
    Spacer(modifier = Modifier.padding(top = top, bottom = bottom, end = end, start = start))
}
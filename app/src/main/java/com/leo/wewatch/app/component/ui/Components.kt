package com.leo.wewatch.app.component.ui

import android.health.connect.datatypes.units.Length
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leo.wewatch.Greeting
import com.leo.wewatch.R
import com.leo.wewatch.ui.theme.WeWatchTheme

@Preview
@Composable
fun maind(){


    WeWatchTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            Column {
                Video(painter = painterResource(id = R.drawable.ic_launcher_background),
                    progress = 0.5f,
                    modifier = Modifier)
                VideoDetail(
                    channelLogo = painterResource(id = R.drawable.ic_launcher_background),
                    title = "this is an sample title | this is channel | ususally it's only 100 words.",
                    channelName = "WeWatch",
                    views = 555,
                    timePublished = "3 hours",
                    modifier = Modifier.padding(top = 10.dp)
                )
            }
            /* f*/
        }
    }



}
@Composable
fun Video(
    lengthInMs: Int = 0,
    lengthSize: Float = 13f,
    painter: Painter,
    contentDescription: String = "video",
    progress: Float = 0.1f,
    modifier: Modifier
){
    Box {
        Image(
            contentScale = ContentScale.FillWidth,
            painter = painter,
            contentDescription = contentDescription,
            modifier = modifier.aspectRatio(16f / 9f),

        )

        Text(
            text = "24:45",
            fontWeight = FontWeight.SemiBold,
            color = Color.White,
            fontSize = TextUnit(lengthSize, TextUnitType.Sp),
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
    views: Long,
    timePublished: String,
    modifier: Modifier
){
    Row (modifier = modifier){

        Image(
            painter = channelLogo,
            contentDescription = null,
            contentScale = ContentScale.None,
            alignment = Alignment.TopStart,
            modifier = Modifier
                .padding(5.dp)
                .clip(CircleShape)
                .size(40.dp)

        )

        makeSpace(start = 10.dp)

        Column {
            Text(text = title, fontWeight = FontWeight.SemiBold )
            makeSpace(top = 5.dp)
            Row {
                Text(text = "$channelName  · $views views  · $timePublished ago" , fontSize = 15.sp)
                /*Spacer(modifier = Modifier.padding(start = 10.dp))
                Text(text = "")
                Spacer(modifier = Modifier.padding(start = 10.dp))
                Text(text = "")
                Spacer(modifier = Modifier.padding(start = 10.dp))*/

            }
        }
        DropdownMenu(expanded = false, onDismissRequest = { /*TODO*/ }) {

        }
    }

}


@Composable
fun makeSpace(top: Dp = 0.dp, bottom:Dp = 0.dp, end:Dp = 0.dp, start : Dp = 0.dp){
    Spacer(modifier = Modifier.padding(top = top, bottom = bottom,end = end, start = start))
}
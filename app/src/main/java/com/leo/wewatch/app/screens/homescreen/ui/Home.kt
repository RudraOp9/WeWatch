package com.leo.wewatch.app.screens.homescreen.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.DraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leo.wewatch.R
import com.leo.wewatch.app.component.ThumbnailView
import com.leo.wewatch.app.component.VideoDetail
import com.leo.wewatch.app.component.makeSpace
import com.leo.wewatch.logic.di.model.DisplayVideo
import com.leo.wewatch.logic.di.model.filter
import com.leo.wewatch.logic.utils.timeFormat
import com.leo.wewatch.logic.utils.viewsFormat


@Composable
fun Header(modifier: Modifier) {
    Row(
        modifier = modifier.padding(vertical = 7.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {

        Image(
            painter = painterResource(id = R.drawable.yt_logo_rgb_light),
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .size(100.dp, 50.dp)
                .padding(10.dp),

            // alignment = Alignment.Center,

        )

        Row(
            modifier = modifier, verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {

            HomeIcons(painter = painterResource(id = R.drawable.icon_cast_24))
            makeSpace(end = 20.dp)
            HomeIcons(painter = painterResource(id = R.drawable.icon_notifications_none_24))
            makeSpace(end = 20.dp)
            HomeIcons(painter = painterResource(id = R.drawable.icon_search_24))
            makeSpace(end = 20.dp)

        }


    }


}

/*@Preview*/
@Composable
fun prev() {
    Header(modifier = Modifier)

    //  FilterChip(  isSelected = remember { mutableStateOf(true) }, onFilterClicked = { /*TODO*/ }, category = "Music")
}

@Composable
fun HomeIcons(
    modifier: Modifier = Modifier,
    painter: Painter,
    contentDescription: String? = null,
    color: Color = Color.Black
) {
    Image(
        painter = painter,
        contentDescription = null,
        modifier = modifier,
        colorFilter = ColorFilter.tint(color = color)
    )
}

@Composable
fun Filters(list: SnapshotStateList<filter>) {


    LazyRow(
        userScrollEnabled = true,
        contentPadding = PaddingValues(vertical = 10.dp),

        ) {


        items(items = list) {
            var a = remember {
                mutableStateOf(false)
            }
            FilterChip(
                isSelected = a,
                onFilterClicked = { a.value = !a.value },
                category = it.category.name
            )
            //  makeSpace(end = 10.dp)
        }
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterChip(isSelected: MutableState<Boolean>, onFilterClicked: () -> Unit, category: String) {

    InputChip(
        selected = isSelected.value,
        onClick = { isSelected.value = !isSelected.value },
        label = { Text(category, modifier = Modifier.padding(vertical = 10.dp)) },
        colors = InputChipDefaults.inputChipColors(
            containerColor = Color.LightGray,
            selectedContainerColor = Color.Black,
            labelColor = Color.Black,
            selectedLabelColor = Color.White
        ),
        shape = RoundedCornerShape(15f),
        border = InputChipDefaults.inputChipBorder(
            borderWidth = 0.dp,
            borderColor = Color.LightGray,
            enabled = true,
            selected = isSelected.value,
            selectedBorderColor = Color.Black
        ),
        modifier = Modifier.padding(vertical = 10.dp, horizontal = 10.dp)

    )
    /*androidx.compose.material3.FilterChip(
        selected = isSelected,
        onClick = onFilterClicked,
        label = { })*/
}


@Composable
fun HomeVideoFeed(
    list: SnapshotStateList<DisplayVideo>,
    onFeedClick: () -> Unit,
    onChannelLogoClick: () -> Unit,
    onMoreMenuClick: () -> Unit,
) {


    LazyColumn(userScrollEnabled = true) {
        items(items = list) {

            Column(modifier = Modifier.clickable(enabled = true, onClick = onFeedClick)) {

                ThumbnailView(
//painter = painterResource(id = R.drawable.ic_launcher_background),
                    uri = it.imageUri,
                    progress = it.watchedSec / it.videoLengthSec.toFloat(),
                    modifier = Modifier,
                    videoLength = timeFormat(it.videoLengthSec),
                )
                VideoDetail(
                    channelLogo = painterResource(id = R.drawable.ic_launcher_background),
                    title = it.title,
                    channelName = it.channelName,
                    views = viewsFormat(it.views),
                    timePublished = timeFormat(it.timePublishedSec),
                    modifier = Modifier.padding(top = 10.dp),
                    onChannelClick = onChannelLogoClick,
                    onMoreMenuClick = onMoreMenuClick
                )
            }
            makeSpace(bottom = 20.dp)

        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetHome(boo: Boolean, sheetState: SheetState, handleBoo: () -> Unit) {
    val state: DraggableState = DraggableState {
    }
    ModalBottomSheet(
        sheetState = sheetState,
        scrimColor = Color.Transparent,
        containerColor = Color.DarkGray,
        contentColor = Color.LightGray,
        modifier = Modifier
            .padding(start = 20.dp, end = 20.dp),
        shape = RoundedCornerShape(10.dp),
        dragHandle = {
            BottomSheetDefaults.DragHandle(color = Color.LightGray)
        },
        windowInsets = WindowInsets(bottom = 40),
        onDismissRequest = handleBoo
    ) {
        BottomSheetDefaults.Elevation.value
        @Composable
        fun idk(painter: Painter, message: String) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                HomeIcons(painter = painter, modifier = Modifier.size(35.dp))
                Text(text = message, modifier = Modifier.padding(start = 20.dp), fontSize = 20.sp)

            }
        }
        // val model:BottomSheetThumbnailViewMenu
        Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.Center) {
            idk(
                painter = painterResource(id = R.drawable.icon_closed_caption_off_24),
                message = "Play next in queue"
            )
            makeSpace(bottom = 20.dp)
            idk(
                painter = painterResource(id = R.drawable.icon_closed_caption_off_24),
                message = "Save to Watch Later"
            )
            makeSpace(bottom = 20.dp)
            idk(
                painter = painterResource(id = R.drawable.icon_closed_caption_off_24),
                message = "Save to playlist"
            )
            makeSpace(bottom = 20.dp)
            idk(
                painter = painterResource(id = R.drawable.icon_closed_caption_off_24),
                message = "Download Video"
            )
            makeSpace(bottom = 20.dp)
            idk(
                painter = painterResource(id = R.drawable.icon_closed_caption_off_24),
                message = "Share"
            )
            makeSpace(bottom = 20.dp)
            idk(
                painter = painterResource(id = R.drawable.icon_closed_caption_off_24),
                message = "Not interested"
            )
            makeSpace(bottom = 20.dp)
            idk(
                painter = painterResource(id = R.drawable.icon_closed_caption_off_24),
                message = "Don't recommend channel"
            )
            makeSpace(bottom = 20.dp)
            idk(
                painter = painterResource(id = R.drawable.icon_closed_caption_off_24),
                message = "Report"
            )


        }
    }
}


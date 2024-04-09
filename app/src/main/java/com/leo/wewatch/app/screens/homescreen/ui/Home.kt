package com.leo.wewatch.app.screens.homescreen.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.Text
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
import com.leo.wewatch.R
import com.leo.wewatch.app.component.ThumbnailView
import com.leo.wewatch.app.component.VideoDetail
import com.leo.wewatch.app.component.makeSpace
import com.leo.wewatch.app.screens.homescreen.data.BottomSheetThumbnailViewMenu
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
fun HomeIcons(modifier: Modifier = Modifier ,painter: Painter, contentDescription: String? = null,color: Color = Color.Black) {
    Image(
        painter = painter,
        contentDescription = null,
        modifier = modifier,
        colorFilter = ColorFilter.tint(color = color)
    )
}

@Composable
fun Filters(list: SnapshotStateList<filter>,){


    LazyRow (
        userScrollEnabled = true,
        contentPadding = PaddingValues(vertical = 10.dp),

        ){


        items(items = list){
            var a = remember {
                mutableStateOf(false)
            }
            FilterChip(isSelected = a , onFilterClicked = { a.value = !a.value}, category = it.category.name)
          //  makeSpace(end = 10.dp)
        }
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterChip(isSelected : MutableState<Boolean>,onFilterClicked : ()->Unit,category:String ){

    InputChip(
        selected = isSelected.value,
        onClick = { isSelected.value = !isSelected.value },
        label = { Text(category,modifier = Modifier.padding(vertical = 10.dp)) },
        colors = InputChipDefaults.inputChipColors(
            containerColor = Color.LightGray,
            selectedContainerColor = Color.Black,
            labelColor = Color.Black,
            selectedLabelColor = Color.White),
        shape = RoundedCornerShape(15f),
        border = InputChipDefaults.inputChipBorder(borderWidth = 0.dp, borderColor = Color.LightGray, enabled = true, selected = false, selectedBorderColor = Color.Black),
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
    onMoreMenuClick : () -> Unit,
) {



    LazyColumn(userScrollEnabled = true) {
        items(items = list) {

            Column(modifier = Modifier.clickable(enabled = true, onClick = onFeedClick)) {

                ThumbnailView(//painter = painterResource(id = R.drawable.ic_launcher_background),
                    uri = it.imageUri,
                    progress = it.watchedSec/it.videoLengthSec.toFloat(),
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
                    onChannelClick =  onChannelLogoClick,
                    onMoreMenuClick = onMoreMenuClick
                )
            }
            makeSpace(bottom = 20.dp)

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetHome(handleBoo:()->Unit){
        ModalBottomSheet(
            tonalElevation = 5.dp,
            containerColor = Color.DarkGray,
            contentColor = Color.LightGray,
            modifier = Modifier.padding(20.dp),
            shape = RoundedCornerShape(10.dp),
            dragHandle = {
                BottomSheetDefaults.DragHandle(color = Color.LightGray)
            },
            /*sheetMaxWidth = BottomSheetDefaults.SheetMaxWidth - 10.dp,*/
            /* windowInsets = WindowInsets(20.dp),*/
            onDismissRequest = handleBoo) {

            @Composable
            fun idk(painter:Painter,message:String){
                Row (verticalAlignment = Alignment.CenterVertically){
                    HomeIcons(painter = painter)
                    Text(text = message, modifier = Modifier.padding(start = 20.dp))

                }
            }
            // val model:BottomSheetThumbnailViewMenu
            Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.Center) {
                idk(painter = painterResource(id = R.drawable.icon_closed_caption_off_24), message = "Play next in queue")
                idk(painter = painterResource(id = R.drawable.icon_closed_caption_off_24), message = "Save to Watch Later")
                idk(painter = painterResource(id = R.drawable.icon_closed_caption_off_24), message = "Save to playlist")
                idk(painter = painterResource(id = R.drawable.icon_closed_caption_off_24), message = "Download Video")
                idk(painter = painterResource(id = R.drawable.icon_closed_caption_off_24), message = "Share")
                idk(painter = painterResource(id = R.drawable.icon_closed_caption_off_24), message = "Not interested")
                idk(painter = painterResource(id = R.drawable.icon_closed_caption_off_24), message = "Don't recommend channel")
                idk(painter = painterResource(id = R.drawable.icon_closed_caption_off_24), message = "Report")
            }
        }

}


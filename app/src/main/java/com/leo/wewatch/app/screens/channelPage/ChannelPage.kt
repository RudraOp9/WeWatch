package com.leo.wewatch.app.screens.channelPage

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.SecondaryScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabPosition
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.net.toUri
import androidx.navigation.NavHostController
import com.leo.wewatch.R
import com.leo.wewatch.app.component.ThumbnailView2
import com.leo.wewatch.app.component.VideoDetail2
import com.leo.wewatch.app.component.makeSpace
import com.leo.wewatch.app.screens.homescreen.ui.HomeIcons
import com.leo.wewatch.logic.di.model.CategoryFilter
import com.leo.wewatch.logic.di.model.DisplayVideo
import com.leo.wewatch.logic.utils.timeFormat
import com.leo.wewatch.logic.utils.viewsFormat
import com.leo.wewatch.navigation.Screens

@Composable
fun ShowChannelPage(navCtrl: NavHostController) {
    var appBarMaxHeightPx :Int = 0
    var appBarOffset by remember { mutableIntStateOf(0) }
    val connection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                val delta = available.y.toInt()
                val newOffset = appBarOffset + delta
                val previousOffset = appBarOffset
                appBarOffset = newOffset.coerceIn(-appBarMaxHeightPx, 0)
                val consumed = appBarOffset - previousOffset
                return Offset(0f, consumed.toFloat())
            }

            override fun onPostScroll(
                consumed: Offset,
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                return super.onPostScroll(consumed, available, source)
            }

        }
    }
    var crrheight by remember { mutableIntStateOf(0) }
    TopBar()
    Box(modifier = Modifier.nestedScroll(connection)) {
        Profile(modifier = Modifier
            .offset { IntOffset(0, appBarOffset) }
            .onGloballyPositioned {
                appBarMaxHeightPx = it.size.height
            }
            .onSizeChanged {
                crrheight = it.height
            })
        ChannelTabs(modifier = Modifier.padding(top = with(LocalDensity.current) { crrheight.plus(appBarOffset).toDp() })/*.offset{IntOffset(0 , appBarOffset )}*/) {
            navCtrl.navigate(Screens.VideoPlayer.route)
        }


    }

}


@Composable
fun TopBar() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier.padding(vertical = 10.dp)
    ) {
        HomeIcons(painter = painterResource(id = R.drawable.icon_keyboard_arrow_down_24))
        makeSpace(start = 10.dp)
        Text(text = "Channel Name", fontWeight = FontWeight.SemiBold, fontSize = 20.sp)


        Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
            HomeIcons(painter = painterResource(id = R.drawable.icon_cast_24))
            makeSpace(start = 10.dp)
            HomeIcons(painter = painterResource(id = R.drawable.icon_search_24))
            makeSpace(start = 10.dp)
            HomeIcons(painter = painterResource(id = R.drawable.icon_more_vert_24))
            makeSpace(start = 10.dp)
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun Profile(modifier: Modifier) {
    var sts = rememberScrollState()
    Column(
        horizontalAlignment = Alignment.Start,
        modifier = modifier.padding(horizontal = 10.dp),
    ) {

        makeSpace(bottom = 10.dp)
        Image(
            painter = painterResource(id = R.drawable.banner),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(15.dp)),
            contentScale = ContentScale.FillWidth
        )
        makeSpace(top = 10.dp)



        ConstraintLayout {
            val (channelImage, channelName, handle, subscribers, videos, about, moreAbout, links, subscribe, membership) = createRefs()

            Image(
                painter = painterResource(id = R.drawable.ic_launcher_background),
                contentDescription = "channelName's channel logo",
                contentScale = ContentScale.Fit,
                alignment = Alignment.TopStart,
                modifier = Modifier
                    .constrainAs(channelImage) {}
                    .padding(5.dp)
                    .clip(CircleShape)
                    .size(80.dp)
                /*.clickable(enabled = true, onClick = onChannelClick)*/
            )

            Text(
                text = "Channel Name",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .constrainAs(channelName) {
                        start.linkTo(channelImage.end)
                        top.linkTo(channelImage.top)
                        bottom.linkTo(handle.top)

                    }
                    .padding(start = 10.dp, top = 10.dp)

            )

            Text(
                text = "@handle",

                modifier = Modifier
                    .constrainAs(handle) {
                        start.linkTo(channelImage.end)
                        bottom.linkTo(subscribers.top)

                    }
                    .padding(start = 10.dp))

            Text(
                text = "15.8M subscribers",
                modifier = Modifier
                    .constrainAs(subscribers) {
                        start.linkTo(channelImage.end)
                        top.linkTo(channelName.bottom)

                        bottom.linkTo(channelImage.bottom)

                    }
                    .padding(start = 10.dp))

            Text(
                text = "700 Videos",
                modifier = Modifier
                    .constrainAs(videos) {
                        start.linkTo(subscribers.end)
                        top.linkTo(subscribers.top)


                    }
                    .padding(start = 5.dp))

            Text(
                text = "Hello this is an simple text to formulate the about section of my of the lol",
                maxLines = 1,
                fontSize = 12.sp,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .constrainAs(about) {
                        start.linkTo(parent.start)
                        top.linkTo(channelImage.bottom)
                    }
                    .padding(top = 5.dp, bottom = 10.dp)
            )
            HomeIcons(
                painter = painterResource(id = R.drawable.icon_keyboard_arrow_down_24),
                modifier = Modifier.constrainAs(moreAbout) {
                    start.linkTo(about.end)
                    top.linkTo(about.top)
                    bottom.linkTo(about.bottom)
                })

            Text(
                text = "HTTPS://WWW.GOOGLE....   and 3 more links",
                fontSize = 12.sp,
                maxLines = 1,
                fontWeight = FontWeight.SemiBold,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .constrainAs(links) {
                        top.linkTo(about.bottom)
                    }
                    .padding(top = 5.dp, bottom = 10.dp)
            )

            Button(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .constrainAs(subscribe) {
                        top.linkTo(links.bottom)
                        start.linkTo(parent.start)
                    }
                    .padding(end = 3.dp)
                    .fillMaxWidth(0.5f),
                colors = ButtonColors(
                    Color.Black,
                    Color.White,
                    Color.Black,
                    Color.White.copy(alpha = 0.95f)
                )
            ) {
                Text(text = "Subscribe")
            }

            Button(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .constrainAs(membership) {
                        start.linkTo(subscribe.end)
                        top.linkTo(subscribe.top)
                        bottom.linkTo(subscribe.bottom)
                    }
                    .padding(start = 3.dp)
                    .fillMaxWidth(0.49f),
                colors = ButtonColors(
                    Color.LightGray,
                    Color.DarkGray,
                    Color.Black,
                    Color.White.copy(alpha = 0.95f)
                )
            ) {
                Text(text = "Join")
            }
        }
    }


}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ChannelTabs(modifier: Modifier , onFeedClick: () -> Unit) {
    var tabIndex by remember { mutableIntStateOf(0) }

    val tabs = listOf("Home", "Videos", "Shorts", "Playlists", "Community")

    Column (modifier = modifier.fillMaxSize()){

        val scrollState = rememberScrollState()

        var offset by remember {
            mutableStateOf(0.dp)
        }


        SecondaryScrollableTabRow(selectedTabIndex = tabIndex,
            tabs = {
                tabs.forEachIndexed { index, s ->
                    Tab(
                        text = { Text(s) },
                        selected = tabIndex == index,
                        onClick = { tabIndex = index },
                        unselectedContentColor = Color.Gray,
                        selectedContentColor = Color.Black
                    )
                }
            },
            scrollState = scrollState,
            indicator = { tabPositions ->
                TabRowDefaults.PrimaryIndicator(
                    Modifier.tabIndicatorOffset2(tabPositions[tabIndex], offset),
                    color = Color.Black,
                    width = tabs[tabIndex].length.times(8).dp
                )


            })

        val horizontalState = rememberPagerState() {
            tabs.size
        }
        LaunchedEffect(key1 = horizontalState.currentPageOffsetFraction) {
            offset = horizontalState.currentPageOffsetFraction.dp
        }

        LaunchedEffect(key1 = tabIndex) {
            horizontalState.scrollToPage(tabIndex)
        }
        LaunchedEffect(key1 = horizontalState.currentPage) {
            tabIndex = horizontalState.currentPage
        }




        HorizontalPager(state = horizontalState, modifier = Modifier.fillMaxSize()) {


            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 10.dp)
            ) {
                when (it) {
                    0 -> {
                        Text(text = tabs[it])
                        Image(
                            painter = painterResource(id = R.drawable.banner),
                            contentDescription = null
                        )
                    }

                    1 -> {

                        var latest by rememberSaveable {
                            mutableStateOf(true)
                        }
                        var popular by rememberSaveable {
                            mutableStateOf(false)
                        }
                        var oldest by rememberSaveable {
                            mutableStateOf(false)
                        }
                        Row {
                            FilterChip(isSelected = latest, name = "Latest") {
                                latest = true
                                popular = false
                                oldest = false
                            }
                            FilterChip(isSelected = popular, name = "Popular") {
                                latest = false
                                popular = true
                                oldest = false
                            }
                            FilterChip(isSelected = oldest, name = "Oldest") {
                                latest = false
                                popular = false
                                oldest = true
                            }
                        }
                        //list of video

                        val a = remember {
                            mutableStateListOf(
                                DisplayVideo(
                                    "Playground 3 Tryouts | EP 1 Highlights | CarryMinati, Elvish Yadav,Techno Gamerz, Mortal",
                                    "https://i3.ytimg.com/vi/yWV3YfDMFXo/maxresdefault.jpg".toUri(),
                                    "PLAYGROUND",
                                    856612,
                                    172856,
                                    CategoryFilter.Gaming,
                                    810,

                                    0
                                )
                            )
                        }
                        a.add(
                            DisplayVideo(
                                "Block – Dhanda Nyoliwala (Music Video) | Deepesh Goyal | VYRL Haryanvi",
                                "https://i3.ytimg.com/vi/e4c5i1fyW-4/maxresdefault.jpg".toUri(),
                                "VYRL Haryanvi",
                                912970,
                                90320,
                                CategoryFilter.Music,
                                214,
                                0
                            )
                        )
                        ChannelVideoFeed(list = a, onFeedClick = onFeedClick) {

                        }


                    }

                    else -> {
                        Column(modifier = Modifier
                            .fillMaxHeight()
                            .verticalScroll(
                                rememberScrollState()
                            )) {
                            Text(text = tabs[it])
                            Image(
                                painter = painterResource(id = R.drawable.banner),
                                contentDescription = null
                            )
                        }

                    }
                }

            }

        }

    }
}

@Composable
fun FilterChip(isSelected: Boolean, name: String, onClick: () -> Unit) {
    InputChip(
        selected = isSelected,
        onClick = onClick,
        label = { Text(name, modifier = Modifier.padding(vertical = 7.dp)) },
        colors = InputChipDefaults.inputChipColors(
            containerColor = Color.White.copy(alpha = 0.5f),
            selectedContainerColor = Color.Black,
            labelColor = Color.Black,
            selectedLabelColor = Color.White
        ),
        shape = RoundedCornerShape(15f),
        border = InputChipDefaults.inputChipBorder(
            borderWidth = 0.dp,
            borderColor = Color.Transparent,
            enabled = true,
            selected = isSelected,
            selectedBorderColor = Color.Black
        ),
        modifier = Modifier.padding(vertical = 5.dp, horizontal = 5.dp)

    )
}

fun Modifier.tabIndicatorOffset2(
    currentTabPosition: TabPosition,
    offset: Dp
): Modifier = composed(
    inspectorInfo = debugInspectorInfo {
        name = "tabIndicatorOffset"
        value = currentTabPosition
    }
) {
    val currentTabWidth by animateDpAsState(
        targetValue = currentTabPosition.width,
        animationSpec = tween(durationMillis = 250, easing = FastOutSlowInEasing), label = ""
    )
    val indicatorOffset by animateDpAsState(

        targetValue = currentTabPosition.left.plus(offset.times(currentTabPosition.left.value)),
        animationSpec = tween(durationMillis = 250, easing = FastOutSlowInEasing), label = ""
    )
    fillMaxWidth()
        .wrapContentSize(Alignment.BottomStart)
        .offset(x = indicatorOffset)
        .width(currentTabWidth)
}


@Composable
fun ChannelVideoFeed(
    list: SnapshotStateList<DisplayVideo>,
    onFeedClick: () -> Unit,
    onMoreMenuClick: () -> Unit,
) {
    LazyColumn(userScrollEnabled = true, modifier = Modifier) {
        items(items = list) {
            Row(modifier = Modifier.clickable(enabled = true, onClick = onFeedClick)) {
                ThumbnailView2(
//painter = painterResource(id = R.drawable.ic_launcher_background),
                    uri = it.imageUri,
                    progress = it.watchedSec / it.videoLengthSec.toFloat(),
                    modifier = Modifier.weight(0.4f),
                    videoLength = timeFormat(it.videoLengthSec),
                    showLinearProgress = false,
                    fontSize = 10f
                )
                VideoDetail2(
                    title = it.title,
                    views = viewsFormat(it.views),
                    timePublished = timeFormat(it.timePublishedSec),
                    modifier = Modifier.weight(0.6f),
                    onMoreMenuClick = onMoreMenuClick
                )
            }
            makeSpace(bottom = 20.dp)

        }
    }
}
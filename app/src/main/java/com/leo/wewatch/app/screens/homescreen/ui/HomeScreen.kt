package com.leo.wewatch.app.screens.homescreen.ui

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.net.toUri
import androidx.navigation.NavController
import com.leo.wewatch.logic.di.model.CategoryFilter.Comedy
import com.leo.wewatch.logic.di.model.CategoryFilter.Entertainment
import com.leo.wewatch.logic.di.model.CategoryFilter.Gaming
import com.leo.wewatch.logic.di.model.CategoryFilter.Live
import com.leo.wewatch.logic.di.model.CategoryFilter.Money
import com.leo.wewatch.logic.di.model.CategoryFilter.Movies
import com.leo.wewatch.logic.di.model.CategoryFilter.Music
import com.leo.wewatch.logic.di.model.CategoryFilter.New
import com.leo.wewatch.logic.di.model.CategoryFilter.News
import com.leo.wewatch.logic.di.model.DisplayVideo
import com.leo.wewatch.logic.di.model.filter
import com.leo.wewatch.navigation.Screens

@Composable
fun ShowHomeScreen(navController: NavController) {
    var boo by rememberSaveable {
        mutableStateOf(false)
    }

    val a = remember {
        mutableStateListOf(
            DisplayVideo(
                "Playground 3 Tryouts | EP 1 Highlights | CarryMinati, Elvish Yadav,Techno Gamerz, Mortal",
                "https://i3.ytimg.com/vi/yWV3YfDMFXo/maxresdefault.jpg".toUri(),
                "PLAYGROUND",
                856612,
                172856,
                Gaming,
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
            Music,
            214,
            0
        )
    )

    val b = remember {
        mutableStateListOf(filter(Entertainment))
    }
    b.add(filter(Movies))
    b.add(filter(Music))
    b.add(filter(Gaming))
    b.add(filter(News))
    b.add(filter(Money))
    b.add(filter(Comedy))
    b.add(filter(Live))
    b.add(filter(New))

    Column {
        Header(modifier = Modifier.fillMaxWidth())

        Filters(list = b)

        HomeVideoFeed(a,
            {
                Log.d("tag", "feed cllicked")
                navController.navigate(Screens.VideoPlayer.route)
            },
            { Log.d("tag", "channel icon clicked") },
            {
                boo = true
                Log.d("tag", "more menu clicked")


            })
        if (boo){
            BottomSheetHome() {
                boo = false
            }
        }


    }
}
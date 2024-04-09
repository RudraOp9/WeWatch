package com.leo.wewatch.app.component.player.di

import android.content.Context
import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import androidx.media3.database.StandaloneDatabaseProvider
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.datasource.FileDataSource
import androidx.media3.datasource.cache.CacheDataSink
import androidx.media3.datasource.cache.CacheDataSource
import androidx.media3.datasource.cache.NoOpCacheEvictor
import androidx.media3.datasource.cache.SimpleCache
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.io.File
import javax.inject.Singleton

@OptIn(UnstableApi::class)
@Module
@InstallIn(SingletonComponent::class)
object playerModule {

    @Provides
    @Singleton
    fun playerBuilder(@ApplicationContext context: Context) : ExoPlayer {
        return ExoPlayer.Builder(context)
            .setTrackSelector(
                DefaultTrackSelector(context)
            ).build()
    }

 /*   @Provides
    @Singleton
    fun simpleCache(@ApplicationContext context: Context):SimpleCache{
        return SimpleCache(
            File(context.cacheDir.path,"cache2fjgfdgsddashfhfggj"), NoOpCacheEvictor(), StandaloneDatabaseProvider(
            context)
        )
    }*/

/*    @Provides
    @Singleton
    fun cacheFactory(@ApplicationContext context: Context):CacheDataSource.Factory{
        val simpleCache = simpleCache(context)
       return CacheDataSource.Factory()
                .setCache(simpleCache)
                .setCacheWriteDataSinkFactory(
                    CacheDataSink.Factory()
                    .setCache(simpleCache))
                .setCacheReadDataSourceFactory(FileDataSource.Factory())
                .setUpstreamDataSourceFactory(DefaultDataSource.Factory(context, DefaultHttpDataSource.Factory()))
                .setFlags(CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR)
    }*/

   /* @Provides
    @Singleton
    fun providePlayer(app : Application = Application()): ExoPlayer{
        return playerBuilder(app)
    }*/
}
package com.example.sameway.hilt

import android.content.Context
import android.content.SharedPreferences
import com.example.sameway.R
import com.squareup.picasso.Picasso
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule
{

    @Provides
    @Singleton
    fun provideSharePreference(@ApplicationContext context: Context): SharedPreferences
    {
        return context.getSharedPreferences("MySharePreferences",Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideSharePreferenceEditor(sharedPreferences: SharedPreferences): SharedPreferences.Editor
    {
        return sharedPreferences.edit()
    }

    @Provides
    fun providePicasso(): Picasso
    {
        return Picasso.get()
    }
    @Provides
    @Singleton
    @Named("container id")
    fun provideContainerId(): Int
    {
        return R.id.navigation_host
    }

}
package com.myself.mychallengueapp.data.di

import android.content.Context
import androidx.room.Room
import com.myself.mychallengueapp.data.appDatabase
import com.myself.mychallengueapp.data.dao.challengeDAO
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class) //Disponibles en toda la aplicacion
object DatabaseModule {

    @Singleton
    @Provides
    fun provideChallengeDAO(database: appDatabase) : challengeDAO{
        return database.challengeDAO()
    }

    @Singleton
    @Provides
    fun provideHabitDAO(database: appDatabase) = database.habitDAO()
    //Infiere tipo de dato retorno
    //Ambos hacen lo mismo

    @Singleton
    @Provides
    fun provideHabitDaysDAO(database: appDatabase) = database.habitDaysDAO()

    @Singleton
    @Provides
    fun provideHabitLogsDAO(database: appDatabase) = database.habitLogsDAO()


    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context) : appDatabase {
            return Room.databaseBuilder(
                context,
                appDatabase::class.java,
                "challenge_database"
            ).fallbackToDestructiveMigration()
                .build()
    }


}
package com.example.samojlov_av_homework_module_16_number_6_1_koala.utils.profile

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.samojlov_av_homework_module_16_number_6_1_koala.models.Profile

@Database(entities = [Profile::class], version = 1, exportSchema = false)
abstract class ProfileDataBase : RoomDatabase() {
    abstract fun getProfileDao(): ProfileDao

    companion object {
        private var INSTANSE: ProfileDataBase? = null
        fun getDataBase(context: Context): ProfileDataBase {
            return INSTANSE ?: synchronized(this) {
                val instance =
                    Room.databaseBuilder(
                        context.applicationContext,
                        ProfileDataBase::class.java,
                        "profile_database"
                    ).build()
                INSTANSE = instance
                instance
            }
        }
    }
}
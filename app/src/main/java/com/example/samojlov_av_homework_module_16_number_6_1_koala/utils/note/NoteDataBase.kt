package com.example.samojlov_av_homework_module_16_number_6_1_koala.utils.note

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.samojlov_av_homework_module_16_number_6_1_koala.models.Note


@Database(entities = [Note::class], version = 1, exportSchema = false)
abstract class NoteDataBase: RoomDatabase() {
    abstract fun getNoteDao(): NoteDao

    companion object {
        private var INSTANSE: NoteDataBase? = null
        fun getDataBase(context: Context): NoteDataBase {
            return INSTANSE ?: synchronized(this) {
                val instance =
                    Room.databaseBuilder(
                        context.applicationContext,
                        NoteDataBase::class.java,
                        "note_database"
                    ).build()
                INSTANSE = instance
                instance
            }
        }
    }
}
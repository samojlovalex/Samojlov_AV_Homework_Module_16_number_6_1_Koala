package com.example.samojlov_av_homework_module_16_number_6_1_koala.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "note_table")
data class Note(
    @ColumnInfo(name = "idProfile") var idProfile: Int,
    @ColumnInfo(name = "categoriesOfNotes") var categoriesOfNotes: String,
    @ColumnInfo(name = "text") var text: String,
    @ColumnInfo(name = "time") var time: String,
    @ColumnInfo(name = "complited") var complited: Boolean,
    @PrimaryKey(autoGenerate = true) var id: Int = 0
)
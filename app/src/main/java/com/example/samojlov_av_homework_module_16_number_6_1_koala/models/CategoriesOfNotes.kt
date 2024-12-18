package com.example.samojlov_av_homework_module_16_number_6_1_koala.models

import com.example.samojlov_av_homework_module_16_number_6_1_koala.R

data class CategoriesOfNotes(val categories: String, val icon: Int) {

    companion object {
        val listOfCategoriesNotes = listOf(
            CategoriesOfNotes("Заметки", R.drawable.notes),
            CategoriesOfNotes("Работа", R.drawable.work),
            CategoriesOfNotes("Работа", R.drawable.home),
            CategoriesOfNotes("Личное", R.drawable.personal),
            CategoriesOfNotes("Личное", R.drawable.personal),
            CategoriesOfNotes("Покупки", R.drawable.purchases),
            CategoriesOfNotes("Учеба", R.drawable.study),
            )
    }

}
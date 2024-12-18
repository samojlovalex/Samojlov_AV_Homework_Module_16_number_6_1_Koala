package com.example.samojlov_av_homework_module_16_number_6_1_koala.ui.notes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.samojlov_av_homework_module_16_number_6_1_koala.models.Profile

class NotesViewModel : ViewModel() {

    val _currentProfile: MutableLiveData<Profile?> by lazy { MutableLiveData<Profile?>() }
    var currentProfile: Profile? = null
    val _loginProfile: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }
    var loginProfile = false
}
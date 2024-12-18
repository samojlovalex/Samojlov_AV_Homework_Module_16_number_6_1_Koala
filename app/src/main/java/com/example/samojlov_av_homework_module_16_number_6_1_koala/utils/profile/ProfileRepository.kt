package com.example.samojlov_av_homework_module_16_number_6_1_koala.utils.profile

import androidx.lifecycle.LiveData
import com.example.samojlov_av_homework_module_16_number_6_1_koala.models.Profile

class ProfileRepository(
    private val profileDao: ProfileDao
) {

    val profiles: LiveData<List<Profile>> = profileDao.getAllProfile()

    suspend fun insert(profile: Profile) {
        profileDao.insert(profile)
    }

    suspend fun delete(profile: Profile) {
        profileDao.delete(profile)
    }

    suspend fun update(profile: Profile) {
        profileDao.update(profile)
    }

    fun deleteAll() {
        profileDao.deleteALL()
    }

}
package com.example.samojlov_av_homework_module_16_number_6_1_koala.utils.profile

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.samojlov_av_homework_module_16_number_6_1_koala.models.Profile

@Dao
interface ProfileDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(profile: Profile)

    @Delete
    suspend fun delete(profile: Profile)

    @Update
    suspend fun update(profile: Profile)

    @Query("SELECT * FROM profile_table ORDER BY id ASC")
    fun getAllProfile(): LiveData<List<Profile>>

    @Query("DELETE FROM profile_table")
    fun deleteALL()

}
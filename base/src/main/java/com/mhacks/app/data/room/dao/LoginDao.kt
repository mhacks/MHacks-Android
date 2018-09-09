package com.mhacks.app.data.room.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.mhacks.app.data.models.Login
import io.reactivex.Single

/**
 * Created by jeffreychang on 9/6/17.
 */
@Dao
interface LoginDao {
    @Query("SELECT * FROM login")
    fun getLogin(): Single<Login>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLogin(login: Login)
}
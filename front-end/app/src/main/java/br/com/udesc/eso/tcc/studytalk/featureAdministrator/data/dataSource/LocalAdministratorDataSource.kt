package br.com.udesc.eso.tcc.studytalk.featureAdministrator.data.dataSource

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.com.udesc.eso.tcc.studytalk.featureAdministrator.data.entity.AdministratorRoomEntity

@Dao
interface LocalAdministratorDataSource {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun create(administratorRoomEntity: AdministratorRoomEntity)

    @Query("SELECT * FROM administrator WHERE uid = :uid")
    suspend fun getByUid(uid: String): AdministratorRoomEntity?

}
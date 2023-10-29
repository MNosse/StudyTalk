package br.com.udesc.eso.tcc.studytalk.featureInstitution.data.dataSource

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.com.udesc.eso.tcc.studytalk.featureInstitution.data.entity.InstitutionRoomEntity

@Dao
interface LocalInstitutionDataSource {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun create(institutionRoomEntity: InstitutionRoomEntity)

    @Query("SELECT * FROM institution WHERE (SELECT CASE WHEN EXISTS (SELECT 1 FROM administrator WHERE uid = :administratorUid) THEN 1 ELSE 0 END) = 1")
    suspend fun getAll(administratorUid: String): MutableList<InstitutionRoomEntity>

    @Query(
        "SELECT * FROM institution WHERE " +
                "(CASE WHEN :isAdministrator = 1 THEN " +
                "EXISTS (SELECT 1 FROM administrator WHERE uid = :requestingUid) " +
                "ELSE " +
                "EXISTS (SELECT 1 FROM participant WHERE uid = :requestingUid) " +
                "END) = 1 " +
                "AND id = :id"
    )
    suspend fun getById(
        id: Long,
        requestingUid: String,
        isAdministrator: Boolean
    ): InstitutionRoomEntity?

    @Query("SELECT * FROM institution WHERE id = :id")
    suspend fun getByIdRelationship(id: Long): InstitutionRoomEntity?

    @Query(
        "SELECT * FROM institution WHERE " +
                "(CASE WHEN :isAdministrator = 1 THEN " +
                "EXISTS (SELECT 1 FROM administrator WHERE uid = :requestingUid) " +
                "ELSE " +
                "EXISTS (SELECT 1 FROM participant WHERE uid = :requestingUid) " +
                "END) = 1 " +
                "AND registration_code = :registrationCode"
    )
    suspend fun getByRegistrationCode(
        registrationCode: String,
        requestingUid: String,
        isAdministrator: Boolean
    ): InstitutionRoomEntity?

}
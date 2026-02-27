package com.example.hcr0_4.data


import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface CartellaDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCartella(cartella: Cartella)

    @Update
    suspend fun updateCartella(cartella: Cartella)

    @Delete
    suspend fun deleteCartella(cartella: Cartella)

    @Query("SELECT * from Cartelle ORDER BY idCartella ASC")
    fun getAllCartelle(): Flow<List<Cartella>>

    @Query("SELECT * from Cartelle WHERE idCartella = :id")
    fun getCartella(id: Int): Flow<Cartella>
}
@Dao
interface RefertoDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertReferto(referto: Referto)

    @Update
    suspend fun updateReferto(referto: Referto)

    @Delete
    suspend fun deleteReferto(referto: Referto)

    @Query("SELECT * from Referti ORDER BY idReferto ASC")
    fun getAllReferti(): Flow<List<Referto>>

    @Query("SELECT * from Referti WHERE idReferto = :id")
    fun getReferto(id: Int): Flow<Referto>

}

@Dao
interface TerapiaDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTerapia(terapia: Terapia): Long

    @Update
    suspend fun updateTerapia(terapia: Terapia)

    @Delete(

    )
    suspend fun deleteTerapia(terapia: Terapia)

    @Query("SELECT * from Terapie ORDER BY idTerapia ASC")
    fun getAllTerapie(): Flow<List<Terapia>>

    @Query("SELECT * from Terapie WHERE idTerapia = :id")
    fun getTerapia(id: Int): Flow<Terapia>
}


@Dao
interface PosologiaDao{
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPosologia(posologia: Posologia): Long

    @Update
    suspend fun updatePosologia(posologia: Posologia)

    @Delete
    suspend fun deletePosologia(posologia: Posologia)

    @Query("SELECT * from Posologie ORDER BY idPosologia ASC")
    fun getAllPosologie(): Flow<List<Posologia>>

    @Query("SELECT * from Posologie WHERE idPosologia = :id")
    fun getPosologia(id: Int): Flow<Posologia>
}


@Dao
interface SomministrazioneDao {

    @Update
    suspend fun updateSomministrazione(somministrazione: Somministrazione)

    @Delete
    suspend fun deleteSomministrazione(somministrazione: Somministrazione)


    @Query("SELECT * from Somministrazioni WHERE idSomministrazione = :id")
    fun getSomministrazione(id: Int): Flow<Somministrazione>

    @Query("""
    SELECT *
    FROM Somministrazioni, Posologie, Terapie 
    WHERE Somministrazioni.idPosologia = Posologie.idPosologia 
    AND Posologie.idTerapia = Terapie.idTerapia 
    ORDER BY idTerapia ASC
""")
    fun getAllSomministrazioni(): Flow<List<SomministrazioneDettagliata>>


//    @Query("""
//
//    SELECT COUNT(DISTINCT S.data) AS giorni_aderenza
//FROM Somministrazioni S
//JOIN Posologie P ON S.idPosologia = P.idPosologia
//JOIN Terapie T ON P.idTerapia = T.idTerapia
//WHERE T.idTerapia = :idTerapia
//GROUP BY S.data
//HAVING COUNT(S.idSomministrazione) = SUM(CASE WHEN S.statoSomministrazione = 'COMPLETATA' THEN 1 ELSE 0 END)
//
//""")
//    fun getGiorniCompletati(idTerapia: Terapia): Int
//
//    fun getCompletate(): Int

    @Query("""
    SELECT *
    FROM Somministrazioni, Posologie, Terapie 
    WHERE Somministrazioni.idPosologia = Posologie.idPosologia 
    AND Posologie.idTerapia = Terapie.idTerapia AND dataSomministrazione = :dataSomministrazione AND statoTerapia = "Attiva"
    ORDER BY 
        CASE 
            WHEN Somministrazioni.oraPresa IS NOT NULL THEN Somministrazioni.oraPresa
            ELSE Somministrazioni.oraSomministrazione
        END ASC
""")
    fun getSomministrazioneByData(dataSomministrazione: LocalDate): Flow<List<SomministrazioneDettagliata>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSomministrazione(somministrazione: Somministrazione): Long


}



@Dao
interface ParametroVitaleDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertParametroVitale(parametroVitale: ParametroVitale)

    @Update
    suspend fun updateParametroVitale(parametroVitale: ParametroVitale)

    @Delete
    suspend fun deleteParametroVitale(parametroVitale: ParametroVitale)

    @Query("SELECT * from ParametriVitali ORDER BY idParametro ASC")
    fun getAllParametriVitali(): Flow<List<ParametroVitale>>

    @Query("SELECT * from ParametriVitali WHERE idParametro = :id")
    fun getParametroVitale(id: Int): Flow<ParametroVitale>

    @Query("SELECT * from ParametriVitali WHERE nomeParametro = :nome")
    fun getParametroVitaleByName(nome: String): Flow<ParametroVitale>
}

@Dao
interface MisurazioneDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMisurazione(misurazione: Misurazione)

    @Update
    suspend fun updateMisurazione(misurazione: Misurazione)

    @Delete
    suspend fun deleteMisurazione(misurazione: Misurazione)

    @Query("SELECT * from Misurazioni ORDER BY idMisurazione DESC")
    fun getAllMisurazioni(): Flow<List<Misurazione>>

    @Query("SELECT * " +
            "from Misurazioni, ParametriVitali " +
            "WHERE Misurazioni.idParametro = ParametriVitali.idParametro " +
            "AND Misurazioni.idParametro = :id " +
            "AND Misurazioni.data = :data " +
            "ORDER BY idMisurazione DESC")
    fun getAllMisurazioniByParametro(id: Int, data: LocalDate): Flow<List<Misurazione>>

    @Query("SELECT * from Misurazioni WHERE data = :data ORDER BY ora DESC")
    fun getMisurazioniByData(data: LocalDate): Flow<List<Misurazione>>



    @Query("SELECT SUM(valore) from Misurazioni WHERE idParametro = :id AND data BETWEEN :dataInizio AND :dataFine")
    fun getSumMisurzioniPvSettimana(id: Int, dataInizio: LocalDate, dataFine: LocalDate): Float


    @Query("SELECT MAX(valore) from Misurazioni WHERE idParametro = :id")
    fun getMaxMisurazioniPv(id: Int): Float

}


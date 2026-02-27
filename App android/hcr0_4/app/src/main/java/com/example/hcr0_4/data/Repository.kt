package com.example.hcr0_4.data



import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface CartelleRepository {
    suspend fun insertCartella(cartella: Cartella)
    suspend fun updateCartella(cartella: Cartella)
    suspend fun deleteCartella(cartella: Cartella)
    fun getAllCartelle(): Flow<List<Cartella>>
    fun getCartella(id: Int): Flow<Cartella?>
}

interface RefertiRepository {
    suspend fun insertReferto(referto: Referto)
    suspend fun updateReferto(referto: Referto)
    suspend fun deleteReferto(referto: Referto)
    fun getAllReferti(): Flow<List<Referto>>
    fun getReferto(id: Int): Flow<Referto?>
}

interface TerapieRepository {
    suspend fun insertTerapia(terapia: Terapia): Long
    suspend fun updateTerapia(terapia: Terapia)
    suspend fun deleteTerapia(terapia: Terapia)
    fun getAllTerapie(): Flow<List<Terapia>>
    fun getTerapia(id: Int): Flow<Terapia?>
}

interface PosologieRepository {
    suspend fun insertPosologia(posologia: Posologia): Long
    suspend fun updatePosologia(posologia: Posologia)
    suspend fun deletePosologia(posologia: Posologia)
    fun getAllPosologie(): Flow<List<Posologia>>
    fun getPosologia(id: Int): Flow<Posologia?>
}

interface SomministrazioniRepository {
    //    suspend fun insertSomministrazione(somministrazione: Somministrazione)
    suspend fun updateSomministrazione(somministrazione: Somministrazione)
    suspend fun deleteSomministrazione(somministrazione: Somministrazione)
    fun getAllSomministrazioni(): Flow<List<SomministrazioneDettagliata>>
    fun getSomministrazione(id: Int): Flow<Somministrazione?>
    fun getSomministrazioneByData(data: LocalDate): Flow<List<SomministrazioneDettagliata>>

    suspend fun insertSomministrazione(somministrazione: Somministrazione): Long




}


interface ParametriVitaliRepository {
    suspend fun insertParametroVitale(parametroVitale: ParametroVitale)
    suspend fun updateParametroVitale(parametroVitale: ParametroVitale)
    suspend fun deleteParametroVitale(parametroVitale: ParametroVitale)
    fun getAllParametriVitali(): Flow<List<ParametroVitale>>
    fun getParametroVitale(id: Int): Flow<ParametroVitale?>
    suspend fun getParametroVitaleByName(nome: String): Flow<ParametroVitale>
}

interface MisurazioniRepository {
    suspend fun insertMisurazione(misurazione: Misurazione)
    suspend fun updateMisurazione(misurazione: Misurazione)
    suspend fun deleteMisurazione(misurazione: Misurazione)
    fun getAllMisurazioni(): Flow<List<Misurazione>>

    fun getAllMisurazioneByParametroVitale(id: Int, data: LocalDate): Flow<List<Misurazione>>

    fun getMisurazioniByData(data: LocalDate): Flow<List<Misurazione>>

    suspend fun getMaxMisurazioniPv(id: Int): Float
}
package com.example.hcr0_4.data



import android.util.Log
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate


class CartelleRepositoryOffline(private val cartellaDao: CartellaDao): CartelleRepository {
    override fun getAllCartelle(): Flow<List<Cartella>> = cartellaDao.getAllCartelle()

    override fun getCartella(id: Int): Flow<Cartella?> = cartellaDao.getCartella(id)

    override suspend fun insertCartella(cartella: Cartella) = cartellaDao.insertCartella(cartella)

    override suspend fun updateCartella(cartella: Cartella) = cartellaDao.updateCartella(cartella)

    override suspend fun deleteCartella(cartella: Cartella) = cartellaDao.deleteCartella(cartella)
}

class RefertiRepositoryOffline(private val refertoDao: RefertoDao): RefertiRepository {
    override fun getAllReferti(): Flow<List<Referto>> = refertoDao.getAllReferti()

    override fun getReferto(id: Int): Flow<Referto?> = refertoDao.getReferto(id)

    override suspend fun insertReferto(referto: Referto) = refertoDao.insertReferto(referto)

    override suspend fun updateReferto(referto: Referto) = refertoDao.updateReferto(referto)

    override suspend fun deleteReferto(referto: Referto) = refertoDao.deleteReferto(referto)
}

class TerapieRepositoryOffline(private val terapiaDao: TerapiaDao): TerapieRepository {
    override fun getAllTerapie(): Flow<List<Terapia>> = terapiaDao.getAllTerapie()

    override fun getTerapia(id: Int): Flow<Terapia?> = terapiaDao.getTerapia(id)

    override suspend fun insertTerapia(terapia: Terapia) = terapiaDao.insertTerapia(terapia)

    override suspend fun updateTerapia(terapia: Terapia) = terapiaDao.updateTerapia(terapia)

    override suspend fun deleteTerapia(terapia: Terapia) = terapiaDao.deleteTerapia(terapia)
}

class PosologieRepositoryOffline(private val posologiaDao: PosologiaDao): PosologieRepository {
    override fun getAllPosologie(): Flow<List<Posologia>> = posologiaDao.getAllPosologie()

    override fun getPosologia(id: Int): Flow<Posologia?> = posologiaDao.getPosologia(id)

    override suspend fun insertPosologia(posologia: Posologia) = posologiaDao.insertPosologia(posologia)

    override suspend fun updatePosologia(posologia: Posologia) = posologiaDao.updatePosologia(posologia)

    override suspend fun deletePosologia(posologia: Posologia) = posologiaDao.deletePosologia(posologia)


}

class SomministrazioniRepositoryOffline(private val somministrazioneDao: SomministrazioneDao): SomministrazioniRepository {
    override fun getAllSomministrazioni(): Flow<List<SomministrazioneDettagliata>> = somministrazioneDao.getAllSomministrazioni()

    override fun getSomministrazione(id: Int): Flow<Somministrazione?> = somministrazioneDao.getSomministrazione(id)
    override fun getSomministrazioneByData(data: LocalDate): Flow<List<SomministrazioneDettagliata>> = somministrazioneDao.getSomministrazioneByData(data)

    override suspend fun insertSomministrazione(somministrazione: Somministrazione): Long = somministrazioneDao.insertSomministrazione(somministrazione)


    override suspend fun updateSomministrazione(somministrazione: Somministrazione) = somministrazioneDao.updateSomministrazione(somministrazione)

    override suspend fun deleteSomministrazione(somministrazione: Somministrazione) = somministrazioneDao.deleteSomministrazione(somministrazione)
}



class ParametriVitaliRepositoryOffline(private val parametroVitaleDao: ParametroVitaleDao): ParametriVitaliRepository {
    override fun getAllParametriVitali(): Flow<List<ParametroVitale>> = parametroVitaleDao.getAllParametriVitali()

    override fun getParametroVitale(id: Int): Flow<ParametroVitale?> = parametroVitaleDao.getParametroVitale(id)

    override suspend fun insertParametroVitale(parametroVitale: ParametroVitale) = parametroVitaleDao.insertParametroVitale(parametroVitale)

    override suspend fun updateParametroVitale(parametroVitale: ParametroVitale) = parametroVitaleDao.updateParametroVitale(parametroVitale)

    override suspend fun deleteParametroVitale(parametroVitale: ParametroVitale) = parametroVitaleDao.deleteParametroVitale(parametroVitale)

    override suspend fun getParametroVitaleByName(nome: String): Flow<ParametroVitale>  = parametroVitaleDao.getParametroVitaleByName(nome)
}

class MisurazioniRepositoryOffline(private val misurazioneDao: MisurazioneDao): MisurazioniRepository {
    override fun getAllMisurazioni(): Flow<List<Misurazione>> = misurazioneDao.getAllMisurazioni()


    override fun getAllMisurazioneByParametroVitale(id: Int, data: LocalDate): Flow<List<Misurazione>> = misurazioneDao.getAllMisurazioniByParametro(id, data)

    override suspend fun insertMisurazione(misurazione: Misurazione) = misurazioneDao.insertMisurazione(misurazione)

    override suspend fun updateMisurazione(misurazione: Misurazione) = misurazioneDao.updateMisurazione(misurazione)

    override suspend fun deleteMisurazione(misurazione: Misurazione) = misurazioneDao.deleteMisurazione(misurazione)

    override fun getMisurazioniByData(data: LocalDate): Flow<List<Misurazione>> = misurazioneDao.getMisurazioniByData(data)

    override suspend fun getMaxMisurazioniPv(id: Int): Float = misurazioneDao.getMaxMisurazioniPv(id)
}
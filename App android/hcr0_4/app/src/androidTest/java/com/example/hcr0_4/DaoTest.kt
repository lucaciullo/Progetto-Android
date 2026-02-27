package com.example.hcr0_4

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.hcr0_4.data.DatabaseHCR
import com.example.hcr0_4.data.Misurazione
import com.example.hcr0_4.data.MisurazioneDao
import com.example.hcr0_4.data.ParametroVitale
import com.example.hcr0_4.data.ParametroVitaleDao
import com.example.hcr0_4.data.Posologia
import com.example.hcr0_4.data.PosologiaDao
import com.example.hcr0_4.data.Referto
import com.example.hcr0_4.data.RefertoDao
import com.example.hcr0_4.data.Somministrazione
import com.example.hcr0_4.data.SomministrazioneDao
import com.example.hcr0_4.data.Terapia
import com.example.hcr0_4.data.TerapiaDao
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlinx.coroutines.flow.first
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import java.time.LocalDate
import java.time.LocalTime


abstract class DaoTest {
    protected lateinit var databaseHCR: DatabaseHCR

    @Before
    open fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()
        databaseHCR = Room.inMemoryDatabaseBuilder(context, DatabaseHCR::class.java)
            .allowMainThreadQueries()
            .build()
    }

    @After
    @Throws(Exception::class)
    fun closeDb() {
        databaseHCR.close()
    }
}
open class RefertiTest : DaoTest() {
    private val referto1 = Referto(
        idReferto = 1,
        idCartella = 1,
        percorsoFfile = "Referto1",
        nomeFile = "NomeReferto1"
    )
    private val referto2 = Referto(
        idReferto = 2,
        idCartella = 1,
        percorsoFfile = "Referto2",
        nomeFile = "NomeReferto2"
    )
    private lateinit var refertoDao: RefertoDao

    @Before
    override fun createDb() {
        super.createDb()
        refertoDao = databaseHCR.refertoDao()
    }



    @Test
    @Throws(Exception::class)
    fun daoInsert_insertsRefertoIntoDB() {
     runBlocking {
         aggingi1RefertoAlDb()
         val allReferti = refertoDao.getAllReferti().first()
         assertEquals(allReferti[0], referto1)
     }
    }


    @Test
    @Throws(Exception::class)
    fun daoGetAllReferti_returnsAllRefertiFromDB() {
        runBlocking {
            aggiungi2RefertiAlDb()
            val allReferti = refertoDao.getAllReferti().first()
            assertEquals(allReferti[0], referto1)
            assertEquals(allReferti[1], referto2)
        }
    }

    @Test
    @Throws(Exception::class)
    fun daoGetReferto_returnsRefertoFromDB() {
        runBlocking {
            aggingi1RefertoAlDb()
            val referto = refertoDao.getReferto(1)
            assertEquals(referto.first(), referto1)
        }
    }

    @Test
    @Throws(Exception::class)
    fun daoDeleteReferti_deletesAllRefertiFromDB() {
        runBlocking {
            aggiungi2RefertiAlDb()
            refertoDao.deleteReferto(referto1)
            refertoDao.deleteReferto(referto2)
            val allReferti = refertoDao.getAllReferti().first()
            assertTrue(allReferti.isEmpty())
        }
    }

    @Test
    @Throws(Exception::class)
    fun daoUpdateReferti_updatesRefertiInDB() {
        runBlocking {
            aggiungi2RefertiAlDb()
            val updatedReferto1 = Referto(1, idCartella = 1, percorsoFfile = "UpdatedReferto1", nomeFile = "UpdatedNomeReferto1")
            val updatedReferto2 = Referto(2, idCartella = 1, percorsoFfile = "UpdatedReferto2", nomeFile = "UpdatedNomeReferto2")
            refertoDao.updateReferto(updatedReferto1)
            refertoDao.updateReferto(updatedReferto2)

            val allReferti = refertoDao.getAllReferti().first()
            assertEquals(updatedReferto1, allReferti[0])
            assertEquals(updatedReferto2, allReferti[1])
        }
    }

    suspend fun aggingi1RefertoAlDb() {
        refertoDao.insertReferto(referto1)
    }
    suspend fun aggiungi2RefertiAlDb() {
        refertoDao.insertReferto(referto1)
        refertoDao.insertReferto(referto2)
    }

}

open class TerapieTest: RefertiTest(){
    private val terapia1 = Terapia(
        idTerapia = 1,
        idReferto = 1,
        tipo = "tipo1",
        nomeTerapia = "nomeTerapia1",
        statoTerapia = "stato1",
        dataInizio = LocalDate.now(),
        dataFine = LocalDate.now(),
        tipoAvviso = "tipoAvviso1",
        forma = "forma1",
        frequenza = "Settimanale",
        durata = 1,
        tipiDurata = "giorni",
        aderenza = 0,
        nonAderenza = 0,
        ogniTotMin = 0,
        ogniTotOre = 0,
        ogniTotGiorni = 0,
        personalizzata = false

    )
    private val terapia2 = Terapia(
        idTerapia = 2,
        idReferto = 2,
        tipo = "tipo2",
        nomeTerapia = "nomeTerapia2",
        statoTerapia = "stato2",
        dataInizio = LocalDate.now(),
        dataFine = LocalDate.now(),
        tipoAvviso = "tipoAvviso2",
        forma = "forma2",
        frequenza = "Giornaliera",
        durata = 3,
        tipiDurata = "giorni",
        aderenza = 0,
        nonAderenza = 0,
        ogniTotMin = 0,
        ogniTotOre = 0,
        ogniTotGiorni = 0,
        personalizzata = false
    )

    private lateinit var terapiaDao: TerapiaDao

    @Before
    override fun createDb() {
        super.createDb()
        terapiaDao = databaseHCR.terapiaDao()
    }

    @Test
    @Throws(Exception::class)
    fun daoInsert_insertsTerapiaIntoDB() {
        runBlocking {
            aggingi1TerapiaAlDb()
            val allTerapie = terapiaDao.getAllTerapie().first()
            assertEquals(allTerapie[0], terapia1)
        }
    }

    @Test
    @Throws(Exception::class)
    fun daoGetAllTerapie_returnsAllTerapieFromDB() {
        runBlocking {
            aggiungi2TerapieAlDb()
            val allTerapie = terapiaDao.getAllTerapie().first()
            assertEquals(allTerapie[0], terapia1)
            assertEquals(allTerapie[1], terapia2)
        }
    }

    @Test
    @Throws(Exception::class)
    fun daoGetTerapia_returnsTerapiaFromDB() {
        runBlocking {
            aggingi1TerapiaAlDb()
            val terapia = terapiaDao.getTerapia(1)
            assertEquals(terapia.first(), terapia1)
        }
    }

    @Test
    @Throws(Exception::class)
    fun daoDeleteTerapie_deletesAllTerapieFromDB() {
        runBlocking {
            aggiungi2TerapieAlDb()
            terapiaDao.deleteTerapia(terapia1)
            terapiaDao.deleteTerapia(terapia2)
            val allTerapie = terapiaDao.getAllTerapie().first()
            assertTrue(allTerapie.isEmpty())
        }
    }

    @Test
    @Throws(Exception::class)
    fun daoUpdateTerapie_updatesTerapieInDB() {
        runBlocking {
            aggiungi2TerapieAlDb()
            val updatedTerapia1 = Terapia(1, 1, "tipo1", "nomeTerapia1", "stato1", LocalDate.now(), LocalDate.now(), "tipoAvviso1", "forma1", "Settimanale", 1, "giorni", 0,0,0,0,0, false)
            val updatedTerapia2 = Terapia(2, 2, "tipo2", "nomeTerapia2", "stato2", LocalDate.now(), LocalDate.now(),"tipoAvviso2", "forma2", "Giornaliera", 3, "giorni", 0,0,0,0,0, false)
            terapiaDao.updateTerapia(updatedTerapia1)
            terapiaDao.updateTerapia(updatedTerapia2)

            val allTerapie = terapiaDao.getAllTerapie().first()
            assertEquals(updatedTerapia1, allTerapie[0])
            assertEquals(updatedTerapia2, allTerapie[1])
        }
    }

    internal suspend fun aggingi1TerapiaAlDb() {
        aggingi1RefertoAlDb()
        terapiaDao.insertTerapia(terapia1)
    }
    internal suspend fun aggiungi2TerapieAlDb() {
        aggiungi2RefertiAlDb()
        terapiaDao.insertTerapia(terapia1)
        terapiaDao.insertTerapia(terapia2)
    }



}

open class PosologieTest: TerapieTest() {
    private val posologia1 = Posologia(
        idPosologia = 1,
        idTerapia = 1, //foreign key
        dose = 1.0,
        giorniSettimana = "L,M,M,G,V,S,D",
        oraPosologia = LocalTime.now()
    )
    private val posologia2 = Posologia(
        idPosologia = 2,
        idTerapia = 2, //foreign key
        dose = 2.0,
        giorniSettimana = "L,M,M,G,V,S,D",
        oraPosologia = LocalTime.now()
    )
    private lateinit var posologiaDao: PosologiaDao

    @Before
    override fun createDb() {
        super.createDb()
        posologiaDao = databaseHCR.posologiaDao()
    }

    @Test
    @Throws(Exception::class)
    fun daoInsert_insertsPosologiaIntoDB() {
        runBlocking {
            aggingi1PosologiaAlDb()
            val allPosologie = posologiaDao.getAllPosologie().first()
            assertEquals(allPosologie[0], posologia1)
        }
    }

    @Test
    @Throws(Exception::class)
    fun daoGetAllPosologie_returnsAllPosologieFromDB() {
        runBlocking {
            aggiungi2PosologieAlDb()
            val allPosologie = posologiaDao.getAllPosologie().first()
            assertEquals(allPosologie[0], posologia1)
            assertEquals(allPosologie[1], posologia2)
        }
    }

    @Test
    @Throws(Exception::class)
    fun daoGetPosologia_returnsPosologiaFromDB() {
        runBlocking {
            aggingi1PosologiaAlDb()
            val posologia = posologiaDao.getPosologia(1)
            assertEquals(posologia.first(), posologia1)
        }
    }

    @Test
    @Throws(Exception::class)
    fun daoDeletePosologie_deletesAllPosologieFromDB() {
        runBlocking {
            aggiungi2PosologieAlDb()
            posologiaDao.deletePosologia(posologia1)
            posologiaDao.deletePosologia(posologia2)
            val allPosologie = posologiaDao.getAllPosologie().first()
            assertTrue(allPosologie.isEmpty())
        }
    }

    @Test
    @Throws(Exception::class)
    fun daoUpdatePosologie_updatesPosologieInDB() {
        runBlocking {
            aggiungi2PosologieAlDb()
            val updatedPosologia1 = Posologia(1, 1, 1.5, "L", LocalTime.now())
            val updatedPosologia2 = Posologia(2, 2, 2.5, "M", LocalTime.now())
            posologiaDao.updatePosologia(updatedPosologia1)
            posologiaDao.updatePosologia(updatedPosologia2)

            val allPosologie = posologiaDao.getAllPosologie().first()
            assertEquals(updatedPosologia1, allPosologie[0])
            assertEquals(updatedPosologia2, allPosologie[1])
        }
    }

    internal suspend fun aggingi1PosologiaAlDb() {
        aggingi1TerapiaAlDb()
        posologiaDao.insertPosologia(posologia1)
    }
    internal suspend fun aggiungi2PosologieAlDb() {
        aggiungi2TerapieAlDb()
        posologiaDao.insertPosologia(posologia1)
        posologiaDao.insertPosologia(posologia2)
    }


}

class SomministrazioniTest: PosologieTest() {
    private val somministrazione1 = Somministrazione(
        idSomministrazione = 1,
        idPosologia = 1, //foreign key
        dataSomministrazione = LocalDate.now(),
        oraSomministrazione = LocalTime.now(),
        statoSomministrazione = "stato1",
        dataPresa = LocalDate.now(),
        oraPresa = LocalTime.now(),
        formaPresa = "forma1",
        dosePresa = 1.0
    )
    private val somministrazione2 = Somministrazione(
        idSomministrazione = 2,
        idPosologia = 2, //foreign key
        dataSomministrazione = LocalDate.now(),
        oraSomministrazione = LocalTime.now(),
        statoSomministrazione = "stato2",
        dataPresa= LocalDate.now(),
        oraPresa = LocalTime.now(),
        formaPresa = "forma2",
        dosePresa = 2.0
    )

    private lateinit var somministrazioneDao: SomministrazioneDao

    @Before
    override fun createDb() {
        super.createDb()
        somministrazioneDao = databaseHCR.somministrazioneDao()
    }

    @Test
    @Throws(Exception::class)
    fun daoInsert_insertsSomministrazioneIntoDB() {
        runBlocking {
            aggingi1SomministrazioneAlDb()
            val allSomministrazioni = somministrazioneDao.getAllSomministrazioni().first()
            assertEquals(somministrazione1, allSomministrazioni[0].idSomministrazione)
        }
    }

    @Test
    @Throws(Exception::class)
    fun daoGetAllSomministrazioni_returnsAllSomministrazioniFromDB() {
        runBlocking {
            aggiungi2SomministrazioniAlDb()
            val allSomministrazioni = somministrazioneDao.getAllSomministrazioni().first()
            assertEquals(somministrazione1, allSomministrazioni[0].idSomministrazione)
            assertEquals(somministrazione2, allSomministrazioni[1].idSomministrazione)
        }
    }

    @Test
    @Throws(Exception::class)
    fun daoGetSomministrazione_returnsSomministrazioneFromDB() {
        runBlocking {
            aggingi1SomministrazioneAlDb()
            val somministrazione = somministrazioneDao.getSomministrazione(1)
            assertEquals(somministrazione.first(), somministrazione1)
        }
    }

    @Test
    @Throws(Exception::class)
    fun daoDeleteSomministrazioni_deletesAllSomministrazioniFromDB() {
        runBlocking {
            aggiungi2SomministrazioniAlDb()
            somministrazioneDao.deleteSomministrazione(somministrazione1)
            somministrazioneDao.deleteSomministrazione(somministrazione2)
            val allSomministrazioni = somministrazioneDao.getAllSomministrazioni().first()
            assertTrue(allSomministrazioni.isEmpty())
        }
    }

    @Test
    @Throws(Exception::class)
    fun daoUpdateSomministrazioni_updatesSomministrazioniInDB() {
        runBlocking {
            aggiungi2SomministrazioniAlDb()
            val updatedSomministrazione1 = Somministrazione(1, 1, LocalDate.now(), LocalTime.now(), "stato1", LocalDate.now(), LocalTime.now(), "forma1", 1.0)
            val updatedSomministrazione2 = Somministrazione(2, 2, LocalDate.now(), LocalTime.now(), "stato2", LocalDate.now(), LocalTime.now(), "forma2", 2.0)
            somministrazioneDao.updateSomministrazione(updatedSomministrazione1)
            somministrazioneDao.updateSomministrazione(updatedSomministrazione2)

            val allSomministrazioni = somministrazioneDao.getAllSomministrazioni().first()
            assertEquals(updatedSomministrazione1, allSomministrazioni[0].idSomministrazione)
            assertEquals(updatedSomministrazione2, allSomministrazioni[1].idSomministrazione)
        }
    }

    private suspend fun aggingi1SomministrazioneAlDb() {
        aggingi1PosologiaAlDb()
        somministrazioneDao.insertSomministrazione(somministrazione1)
    }

    private suspend fun aggiungi2SomministrazioniAlDb() {
        aggiungi2PosologieAlDb()
        somministrazioneDao.insertSomministrazione(somministrazione1)
        somministrazioneDao.insertSomministrazione(somministrazione2)
    }

}

open class ParametriVitaliTest: DaoTest() {
    private val parametroVitale1 = ParametroVitale(
        idParametro = 1,
        nomeParametro = "nomeParametro1",
        unitaMisura = "unitaMisura1",
        valoriAccumulati = false
    )
    private val parametroVitale2 = ParametroVitale(
        idParametro = 2,
        nomeParametro = "nomeParametro2",
        unitaMisura = "unitaMisura2",
        valoriAccumulati = false
    )

    private lateinit var parametroVitaleDao: ParametroVitaleDao

    @Before
    override fun createDb() {
        super.createDb()
        parametroVitaleDao = databaseHCR.parametroVitaleDao()
    }

    @Test
    @Throws(Exception::class)
    fun daoInsert_insertsParametroVitaleIntoDB() {
        runBlocking {
            aggingi1ParametroVitaleAlDb()
            val allParametriVitali = parametroVitaleDao.getAllParametriVitali().first()
            assertEquals(allParametriVitali[0], parametroVitale1)
        }
    }

    @Test
    @Throws(Exception::class)
    fun daoGetAllParametriVitali_returnsAllParametriVitaliFromDB() {
        runBlocking {
            aggiungi2ParametriVitaliAlDb()
            val allParametriVitali = parametroVitaleDao.getAllParametriVitali().first()
            assertEquals(allParametriVitali[0], parametroVitale1)
            assertEquals(allParametriVitali[1], parametroVitale2)
        }
    }

    @Test
    @Throws(Exception::class)
    fun daoGetParametroVitale_returnsParametroVitaleFromDB() {
        runBlocking {
            aggingi1ParametroVitaleAlDb()
            val parametroVitale = parametroVitaleDao.getParametroVitale(1)
            assertEquals(parametroVitale.first(), parametroVitale1)
        }
    }

    @Test
    @Throws(Exception::class)
    fun daoDeleteParametriVitali_deletesAllParametriVitaliFromDB() {
        runBlocking {
            aggiungi2ParametriVitaliAlDb()
            parametroVitaleDao.deleteParametroVitale(parametroVitale1)
            parametroVitaleDao.deleteParametroVitale(parametroVitale2)
            val allParametriVitali = parametroVitaleDao.getAllParametriVitali().first()
            assertTrue(allParametriVitali.isEmpty())
        }
    }

    @Test
    @Throws(Exception::class)
    fun daoUpdateParametriVitali_updatesParametriVitaliInDB() {
        runBlocking {
            aggiungi2ParametriVitaliAlDb()
            parametroVitaleDao.updateParametroVitale(ParametroVitale(1, "nomeParametro1", "unitaMisura1", false))
            parametroVitaleDao.updateParametroVitale(ParametroVitale(2, "nomeParametro2", "unitaMisura2", false))

            val allParametriVitali = parametroVitaleDao.getAllParametriVitali().first()
            assertEquals(allParametriVitali[0], ParametroVitale(1, "nomeParametro1", "unitaMisura1", false))
            assertEquals(allParametriVitali[1], ParametroVitale(2, "nomeParametro2", "unitaMisura2", false))
        }
    }

    internal suspend fun aggingi1ParametroVitaleAlDb() {
        parametroVitaleDao.insertParametroVitale(parametroVitale1)
    }

    internal suspend fun aggiungi2ParametriVitaliAlDb() {
        parametroVitaleDao.insertParametroVitale(parametroVitale1)
        parametroVitaleDao.insertParametroVitale(parametroVitale2)
    }

}

class MisurazioniTest: ParametriVitaliTest() {
    private val misurazione1 = Misurazione(
        idMisurazione = 1,
        idParametro = 1, //foreign key
        valore = 1.0,
        data = LocalDate.now(),
        ora = LocalTime.now()
    )
    private val misurazione2 = Misurazione(
        idMisurazione = 2,
        idParametro = 2, //foreign key
        valore = 2.2,
        data = LocalDate.now(),
        ora = LocalTime.now()
    )


    private lateinit var misurazioneDao: MisurazioneDao

    @Before
    override fun createDb() {
        super.createDb()
        misurazioneDao = databaseHCR.misurazioneDao()
    }

    @Test
    @Throws(Exception::class)
    fun daoInsert_insertsMisurazioneIntoDB() {
        runBlocking {
            aggingi1MisurazioneAlDb()
            val allMisurazioni = misurazioneDao.getAllMisurazioni().first()
            assertEquals(allMisurazioni[0], misurazione1)
        }
    }

    @Test
    @Throws(Exception::class)
    fun daoGetAllMisurazioni_returnsAllMisurazioniFromDB() {
        runBlocking {
            aggiungi2MisurazioniAlDb()
            val allMisurazioni = misurazioneDao.getAllMisurazioni().first()
            assertEquals(allMisurazioni[0], misurazione1)
            assertEquals(allMisurazioni[1], misurazione2)
        }
    }

    @Test
    @Throws(Exception::class)
    fun daoGetMisurazione_returnsMisurazioneFromDB() {
        runBlocking {
            aggingi1MisurazioneAlDb()
            val misurazione = misurazioneDao.getAllMisurazioniByParametro(1, LocalDate.now()).first()
            assertEquals(misurazione.first(), misurazione1)
        }
    }

    @Test
    @Throws(Exception::class)
    fun daoDeleteMisurazioni_deletesAllMisurazioniFromDB() {
        runBlocking {
            aggiungi2MisurazioniAlDb()
            misurazioneDao.deleteMisurazione(misurazione1)
            misurazioneDao.deleteMisurazione(misurazione2)
            val allMisurazioni = misurazioneDao.getAllMisurazioni().first()
            assertTrue(allMisurazioni.isEmpty())
        }
    }

//    @Test
//    @Throws(Exception::class)
//    fun daoUpdateMisurazioni_updatesMisurazioniInDB() {
//        runBlocking {
//            aggiungi2MisurazioniAlDb()
//            misurazioneDao.updateMisurazione(Misurazione(1, 1, 1.0, LocalDate.now(), LocalTime.now()))
//            misurazioneDao.updateMisurazione(Misurazione(2, 2, 2.0, LocalDate.now(), LocalTime.now()
//
//            val allMisurazioni = misurazioneDao.getAllMisurazioni().first()
//            assertEquals(allMisurazioni[0], Misurazione(1, 1, 1.0, LocalDate.now(), LocalTime.now()))
//            assertEquals(allMisurazioni[1], Misurazione(2, 2, 2.0, LocalDate.now(), LocalTime.now()))
//        }
//    }


    private suspend fun aggingi1MisurazioneAlDb() {
        aggingi1ParametroVitaleAlDb()
        misurazioneDao.insertMisurazione(misurazione1)
    }

    private suspend fun aggiungi2MisurazioniAlDb() {
        aggiungi2ParametriVitaliAlDb()
        misurazioneDao.insertMisurazione(misurazione1)
        misurazioneDao.insertMisurazione(misurazione2)
    }


}

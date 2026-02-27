package com.example.hcr0_4.ui.schermate.home

import android.util.Log
import com.example.hcr0_4.SomministrazioneStatus
import com.example.hcr0_4.data.Posologia
import com.example.hcr0_4.data.Somministrazione
import com.example.hcr0_4.data.SomministrazioneDettagliata
import com.example.hcr0_4.data.SomministrazioniRepository
import com.example.hcr0_4.data.Terapia
import kotlinx.coroutines.flow.Flow
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.temporal.TemporalAdjusters
import java.util.TreeMap

class SomministrazioneGenerator(
    private val somministrazioneRepository: SomministrazioniRepository
) {




    suspend fun aggiungiSomministrazioni() {
        // Step 1: Ottieni tutte le somministrazioni dettagliate
        val somministrazioniDettagliate: Flow<List<SomministrazioneDettagliata>> = somministrazioneRepository.getAllSomministrazioni()

        somministrazioniDettagliate.collect { somministrazioniList ->

            // Step 2: Raggruppa le somministrazioni per terapia
            val groupedByTerapia = somministrazioniList.groupBy { it.idTerapia }


            Log.d("SomministrazioneGenerator", "Somministrazioni raggruppate per terapia: $groupedByTerapia")
            for ((idTerapia, somministrazioni) in groupedByTerapia) {
                // Step 3: Ricostruisci la lista delle posologie per questa terapia
                val somministrazioneVecchia = findOldestSomministrazione(somministrazioni)

                val terapia = Terapia(
                    idTerapia = idTerapia,
                    dataInizio = somministrazioneVecchia?.dataSomministrazione ?: LocalDate.now(),
                    dataFine = somministrazioneVecchia?.dataFine ?: LocalDate.now(),
                    durata = (somministrazioneVecchia?.durata?.minus(somministrazioneVecchia.aderenza ?: 0)) ?: 0,
                    tipiDurata = somministrazioneVecchia?.tipiDurata ?: "",
                    frequenza = somministrazioneVecchia?.frequenza ?: "",
                    ogniTotMin = somministrazioneVecchia?.ogniTotMin ?: 0,
                    ogniTotGiorni = somministrazioneVecchia?.ogniTotGiorni ?: 0,
                    ogniTotOre = somministrazioneVecchia?.ogniTotOre ?: 0,
                    aderenza = somministrazioneVecchia?.aderenza ?: 0,
                    nonAderenza = somministrazioneVecchia?.nonAderenza ?: 0,
                    personalizzata = false,
                    forma = somministrazioneVecchia?.forma ?: "",
                    nomeTerapia = somministrazioneVecchia?.nomeTerapia ?: "",
                    tipo = somministrazioneVecchia?.tipo ?: "",
                    tipoAvviso = somministrazioneVecchia?.tipoAvviso ?: "",
                    statoTerapia = somministrazioneVecchia?.statoTerapia ?: ""
                )

                val posologie = somministrazioni.map {
                    Posologia(
                        idPosologia = it.idPosologia,
                        idTerapia = it.idTerapia,
                        dose = it.dose,
                        giorniSettimana = it.giorniSettimana ?: "",
                        oraPosologia = it.oraSomministrazione
                    )
                }

                // Step 4: Log delle informazioni per debugging
                Log.d("SomministrazioneGenerator", "Generazione somministrazioni posologie: $posologie e $terapia")

                // Step 5: Passa le informazioni a generateSomministrazioni
//                generateSomministrazioni(terapia, posologie)
            }
        }
    }





    private fun findOldestSomministrazione(somministrazioni: List<SomministrazioneDettagliata>): SomministrazioneDettagliata? {
        return somministrazioni.minWithOrNull(compareBy({ it.dataSomministrazione }, { it.oraSomministrazione }))
    }

    suspend fun generateSomministrazioni(
        terapia: Terapia,
        listaPosologie: List<Posologia>
    ) {

        Log.d("SomministrazioneGenerator", "Generazione somministrazioni per terapia: $terapia e posologie: $listaPosologie")
        val dataFine = terapia.dataFine
        val dataInizio = terapia.dataInizio
        var volte = terapia.durata
        val tipiDurata = terapia.tipiDurata
        val frequenza = terapia.frequenza
        val ogniTotMin = terapia.ogniTotMin
        val ogniTotGiorni = terapia.ogniTotGiorni
        val ogniTotOre = terapia.ogniTotOre

        if (frequenza == "Giorn." || frequenza == "Pers.") {
            var currentDate = dataInizio
            var currentHour = listaPosologie[0].oraPosologia

            while ((currentDate.isBefore(dataFine) || currentDate.isEqual(dataFine)) || volte > 0) {
                Log.d("SomministrazioneGenerator", "Data corrente: $currentDate e ora corrente: $currentHour e volte rimanenti: $volte")
                for (i in 0 until listaPosologie.size) {
                    inserisciSomministrazione(
                        posologiaId = listaPosologie[i].idPosologia,
                        data = currentDate,
                        ora = if (frequenza == "Pers.") currentHour else listaPosologie[i].oraPosologia
                    )
                    if (tipiDurata == "N° di volte") {
                        volte--
                        if (volte == 0) {
                            break
                        }
                    }
                }
                currentDate = currentDate.plusDays(ogniTotGiorni.toLong())
                currentHour = currentHour.plusMinutes(ogniTotMin.toLong())
                currentHour = currentHour.plusHours(ogniTotOre.toLong())
                if (tipiDurata == "N° giorni" && currentDate.isAfter(dataInizio)) {
                    volte--
                }
            }
        } else if (frequenza == "Sett.") {
            if (tipiDurata == "Data fine") {
                for (element in listaPosologie) {
                    Log.d("", element.toString())
                    val giorniSettimanaList = getListaFromString(element.giorniSettimana)

                    giorniSettimanaList.forEach { giorno ->
                        Log.d("SomministrazioneGenerator", "Giorno della settimana: $giorno")
                        val dayOfWeek = getGiornoSettimanaFromMiaRappr(giorno)
                        var nextDate = dataInizio.with(TemporalAdjusters.nextOrSame(dayOfWeek as DayOfWeek?))
                        Log.d("SomministrazioneGenerator", "Prossima data: $nextDate")
                        while (nextDate.isBefore(dataFine) || nextDate.isEqual(dataFine)) {
                            inserisciSomministrazione(
                                posologiaId = element.idPosologia,
                                data = nextDate,
                                ora = element.oraPosologia
                            )
                            nextDate = nextDate.plusWeeks(1)
                            Log.d("SomministrazioneGenerator", "Prossima data: $nextDate")
                        }
                    }
                }
            } else if (tipiDurata == "N° giorni" || tipiDurata == "N° di volte") {
                val posologiaMap: MutableMap<Pair<DayOfWeek, LocalTime>, Long> = createPosologiaMap(listaPosologie)
                var currentDate = dataInizio

// Step 1: Trova il giorno della settimana più vicino a dataInizio
                val startDayOfWeek = currentDate.dayOfWeek
                var minDiff = Int.MAX_VALUE
                var startEntry: MutableMap.MutableEntry<Pair<DayOfWeek, LocalTime>, Long>? = null

                for (entry in posologiaMap.entries) {
                    val dayOfWeek = entry.key.first

                    // Calcola la differenza di giorni tra il giorno corrente e quello nella mappa
                    val diff = if (dayOfWeek.value >= startDayOfWeek.value) {
                        dayOfWeek.value - startDayOfWeek.value
                    } else {
                        7 - (startDayOfWeek.value - dayOfWeek.value)
                    }

                    // Aggiorna il valore di minDiff e salva l'entry con la differenza minima
                    if (diff < minDiff) {
                        minDiff = diff
                        startEntry = entry
                    }
                }

// Step 2: Inizia l'iterazione a partire dal giorno più vicino
                val sortedEntries = posologiaMap.entries.toList() // Converte in lista per permettere la partenza dal giorno giusto
                var startIndex = sortedEntries.indexOf(startEntry)

                var volteRestanti = volte
                while (volteRestanti > 0) {
                    val currentEntry = sortedEntries[startIndex]
                    val dayOfWeek = currentEntry.key.first
                    val ora = currentEntry.key.second
                    val posologiaIndex = currentEntry.value.toInt()

                    // Calcola la prossima data disponibile per il giorno e l'ora attuale
                    val nextDate = currentDate.with(TemporalAdjusters.nextOrSame(dayOfWeek))


                    // Aggiorna currentDate se la nuova data è valida
                    if (nextDate.isAfter(currentDate)) {
                        currentDate = nextDate
                        if (tipiDurata == "N° giorni")
                            volteRestanti--
                    }

                    // Inserisci la somministrazione
                    inserisciSomministrazione(
                        posologiaId = listaPosologie[posologiaIndex].idPosologia,
                        data = currentDate,
                        ora = ora
                    )

                    if (tipiDurata == "N° di volte") {
                        volteRestanti--
                    }

                    // Passa al giorno successivo (circolare)
                    startIndex = (startIndex + 1) % sortedEntries.size
                }

            }
        }
    }
    private fun getListaFromString(lista: String): List<String> {
        return lista.removeSurrounding("[", "]").split(",").map { it.trim() }
    }
    private fun getGiornoSettimanaFromMiaRappr(miaRappr: String): DayOfWeek {
        return when (miaRappr) {
            "Lu" -> DayOfWeek.MONDAY
            "Ma" -> DayOfWeek.TUESDAY
            "Me" -> DayOfWeek.WEDNESDAY
            "Gi" -> DayOfWeek.THURSDAY
            "Ve" -> DayOfWeek.FRIDAY
            "Sa" -> DayOfWeek.SATURDAY
            "Do" -> DayOfWeek.SUNDAY
            else -> DayOfWeek.MONDAY
        }
    }

    private suspend fun inserisciSomministrazione(
        posologiaId: Int,
        data: LocalDate,
        ora: LocalTime
    ) {
        val somministrazione = Somministrazione(
            idPosologia = posologiaId.toInt(),
            dataSomministrazione = data,
            oraSomministrazione = ora,
            statoSomministrazione = determineSomministrazioneStatus("", data, ora),
            dataPresa = null,
            oraPresa = null,
            formaPresa = "",
            dosePresa = 0.0
        )
        try {
            val insertedId = somministrazioneRepository.insertSomministrazione(somministrazione)
            val updatedSomministrazione = somministrazione.copy(idSomministrazione = insertedId.toInt())
        } catch (e: Exception) {
            Log.e("SomministrazioneGenerator", "Errore durante l'inserimento della somministrazione: ${e.message}")
        }
    }

    private fun createPosologiaMap(listaPosologie: List<Posologia>): TreeMap<Pair<DayOfWeek, LocalTime>, Long> {
        Log.d("SomministrazioneGenerator", "Creazione mappa posologie")
        val dayOfWeekMap = mapOf(
            "Lu" to DayOfWeek.MONDAY,
            "Ma" to DayOfWeek.TUESDAY,
            "Me" to DayOfWeek.WEDNESDAY,
            "Gi" to DayOfWeek.THURSDAY,
            "Ve" to DayOfWeek.FRIDAY,
            "Sa" to DayOfWeek.SATURDAY,
            "Do" to DayOfWeek.SUNDAY
        )
        Log.d("SomministrazioneGenerator", "Mappa giorni settimana: $dayOfWeekMap")
        val posologiaMap = TreeMap<Pair<DayOfWeek, LocalTime>, Long>(compareBy({ it.first }, { it.second }))
        Log.d("SomministrazioneGenerator", "Mappa posologie: $posologiaMap")
        listaPosologie.forEachIndexed { index, posologia ->
            Log.d("listaPosologie.forEachIndexed", "Posologia: $posologia e index: $index e $posologiaMap")
            val listaGiorniSettimana = getListaFromString(posologia.giorniSettimana)
            listaGiorniSettimana.forEach { giorno ->
                val dayOfWeek = dayOfWeekMap[giorno.toString()]
                Log.d("posologia.giorniSettimana.forEach", "Giorno della settimana: $giorno e dayOfWeek: $dayOfWeek")
                if (dayOfWeek != null) {
                    posologiaMap[dayOfWeek to posologia.oraPosologia] = index.toLong()
                    Log.d("SomministrazioneGenerator", "Aggiunto a mappa: ${dayOfWeek to posologia.oraPosologia} -> $index")
                } else {
                    Log.e("SomministrazioneGenerator", "Giorno della settimana non valido: $giorno")
                }
            }
        }

        return posologiaMap
    }

    private fun determineSomministrazioneStatus(
        statoSomministrazione: String = "",
        data: LocalDate, ora: LocalTime
    ): String {
        return if (statoSomministrazione == SomministrazioneStatus.COMPLETATA.toString()) {
            SomministrazioneStatus.COMPLETATA.toString()
        } else if (LocalDate.now().isAfter(data) || (LocalDate.now().isEqual(data) && LocalTime.now().isAfter(ora))) {
            SomministrazioneStatus.NON_COMPLETATA.toString()
        } else {
            SomministrazioneStatus.PROGRAMMATA.toString()
        }
    }
}


data class SomministrazioneUiState(
    val somministrazioneDetails: SomministrazioneDetails = SomministrazioneDetails(),
    val isEntryValid: Boolean = false
)

data class SomministrazioneDetails(
    val idSomministrazione: Int = 0,
    val idPosologia: Int = 0,
    val dataSomministrazione: LocalDate = LocalDate.now(),
    val oraSomministrazione: LocalTime = LocalTime.now(),
    val statoSomministrazione: String = SomministrazioneStatus.PROGRAMMATA.toString(),
    val dataPresa: LocalDate? = null,
    val oraPresa: LocalTime? = null,
    val formaPresa: String = "",
    val dosePresa: Double = 0.0
)

fun SomministrazioneDetails.toSomministrazione(): Somministrazione = Somministrazione(
    idSomministrazione = idSomministrazione,
    idPosologia = idPosologia,
    dataSomministrazione = dataSomministrazione,
    oraSomministrazione = oraSomministrazione,
    statoSomministrazione = statoSomministrazione,
    dataPresa = dataPresa,
    oraPresa = oraPresa,
    formaPresa = formaPresa,
    dosePresa = dosePresa
)

fun Somministrazione.toSomministrazioneUiState(isEntryValid: Boolean = false): SomministrazioneUiState = SomministrazioneUiState(
    somministrazioneDetails = this.toSomministrazioneDetails(),
    isEntryValid = isEntryValid
)

fun Somministrazione.toSomministrazioneDetails(): SomministrazioneDetails = SomministrazioneDetails(
    idSomministrazione = idSomministrazione,
    idPosologia = idPosologia,
    dataSomministrazione = dataSomministrazione,
    oraSomministrazione = oraSomministrazione,
    statoSomministrazione = statoSomministrazione,
    dataPresa = dataPresa,
    oraPresa = oraPresa
)


package com.example.hcr0_4.ui.schermate.home

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.hcr0_4.LocalDateAdapter
import com.example.hcr0_4.data.Terapia
import com.google.gson.GsonBuilder
import java.time.LocalDate

class InserisciTerapiaViewModel (
    savedStateHandle: SavedStateHandle
): ViewModel() {


    // Crea un'istanza personalizzata di Gson
    val gson = GsonBuilder()
        .registerTypeAdapter(LocalDate::class.java, LocalDateAdapter)
        .create()

    val terapiaDetailsString: String = checkNotNull(savedStateHandle[InserisciTerapiaDestination.terapiaDetailsArg])
    val terapia: TerapiaDetails = gson.fromJson(terapiaDetailsString, TerapiaDetails::class.java)
    var terapiaUiState by mutableStateOf(
        TerapiaUiState(
        terapiaDetails = TerapiaDetails(
            idTerapia = terapia.idTerapia,
            idReferto = terapia.idReferto,
            tipo = terapia.tipo,
            nomeTerapia = terapia.nomeTerapia,
            statoTerapia = terapia.statoTerapia,
            dataInizio = terapia.dataInizio,
            dataFine = terapia.dataFine,
            tipoAvviso = terapia.tipoAvviso,
            forma = terapia.forma,
            frequenza = terapia.frequenza,
            durata = terapia.durata,
            aderenza = terapia.aderenza,
            nonAderenza = terapia.nonAderenza,
            ogniTotGiorni = terapia.ogniTotGiorni,
            ogniTotOre = terapia.ogniTotOre,
            ogniTotMin = terapia.ogniTotMin,
            personalizzata = terapia.personalizzata,
            tipiDurata = terapia.tipiDurata

        ),
        isEntryValid = false
    )
    )
        private set




    fun updateUiStateTerapia(terapiaDetails: TerapiaDetails = terapia) {

        terapiaUiState = terapiaUiState.copy(
            terapiaDetails = terapiaDetails,
            isEntryValid = validateTerapiaInput(terapiaDetails)
        )
        Log.d("hcr", "updateUiStateTerapia - terapiaUiState: $terapiaUiState")
    }

    private fun validateTerapiaInput(details: TerapiaDetails): Boolean {
        return details.tipo.isNotBlank() &&
                details.nomeTerapia.isNotBlank() &&
                details.dataInizio != null &&
                (details.dataFine != null &&   details.tipiDurata == "Data fine" ||
                        details.durata != 0 && details.tipiDurata == "N° di volte" ||
                        details.durata != 0 && details.tipiDurata == "N° giorni") &&
                details.tipoAvviso.isNotBlank() &&
                details.forma.isNotBlank() &&
                details.frequenza.isNotBlank() &&
                (
                        (details.frequenza == "Pers." &&
                        (details.ogniTotGiorni != 0 ||
                        details.ogniTotOre != 0 ||
                        details.ogniTotMin != 0)) ||
                                details.frequenza != "Pers.")


    }
}

// Data classes and conversion functions

data class TerapiaUiState(
    val terapiaDetails: TerapiaDetails = TerapiaDetails(),
    val isEntryValid: Boolean = false
)

data class TerapiaDetails(
    val idTerapia: Int = 0,
    val idReferto: Int? = null, //foreign key
    val tipo: String = "", //enum (farmacologica, esercizio fisico, dieta, ecc)
    val nomeTerapia: String = "",
    val statoTerapia: String = "Attiva", //enum (attiva, disattiva)
    val dataInizio: LocalDate = LocalDate.now(),
    val dataFine: LocalDate = LocalDate.now(),
    val tipoAvviso: String = "", //enum (sveglia, notifica, off)
    val forma: String ="", //enum (compresse, sciroppo, iniezioni, ecc)
    val frequenza: String = "", //enum (giornaliera, settimanale, personalizzata)
    val durata: Int = 0, //enum (data fine, numero somministrazioni, numero giorni)
    val aderenza: Int = 0,
    val nonAderenza: Int = 0,
    val ogniTotGiorni: Int = 0,
    val ogniTotOre: Int = 0,
    val ogniTotMin: Int = 0,
    val personalizzata: Boolean = false,
    val tipiDurata: String = ""
)

fun TerapiaDetails.toTerapia(): Terapia = Terapia(
    idTerapia = idTerapia,
    idReferto = idReferto,
    tipo = tipo,
    nomeTerapia = nomeTerapia,
    statoTerapia = statoTerapia,
    dataInizio = dataInizio,
    dataFine = dataFine,
    tipoAvviso = tipoAvviso,
    forma = forma,
    frequenza = frequenza,
    durata = durata,
    aderenza = aderenza,
    nonAderenza = nonAderenza,
    ogniTotOre = ogniTotOre,
    ogniTotGiorni = ogniTotGiorni,
    ogniTotMin = ogniTotMin,
    personalizzata = personalizzata,
    tipiDurata = tipiDurata
)
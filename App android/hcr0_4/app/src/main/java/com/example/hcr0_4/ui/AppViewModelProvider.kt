package com.example.hcr0_4.ui


import com.example.hcr0_4.ui.schermate.home.InserisciTerapiaViewModel
import com.example.hcr0_4.HcrApplication
import android.app.Application
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.hcr0_4.ui.schermate.home.InserisciPosologiaViewModel
import com.example.hcr0_4.ui.schermate.menuCartelle.InserisciCartellaViewModel
import com.example.hcr0_4.ui.schermate.menuCartelle.InserisciRefertoViewModel
import com.example.hcr0_4.ui.schermate.menuParametriVitali.InserisciMisurazioniViewModel
import com.example.hcr0_4.ui.schermate.menuParametriVitali.InserisciParametriVitaliViewModel


import com.example.hcr0_4.ui.schermate.menuPersonalizzazioni.ModificaTerapiaViewModel
import com.example.hcr0_4.ui.schermate.menuCartelle.FolderViewModel
import com.example.hcr0_4.ui.schermate.home.HomeViewModel
import com.example.hcr0_4.ui.schermate.menuParametriVitali.ParametriVitaliViewModel
import com.example.hcr0_4.ui.schermate.menuParametriVitali.StoricoParametriVitaliViewModel
import com.example.hcr0_4.ui.schermate.menuPersonalizzazioni.PersonalizzazioniViewModel




/**
 * Provides Factory to create instance of ViewModel for the entire Inventory app
 */
object AppViewModelProvider {
    val Factory = viewModelFactory {

        initializer {
            HomeViewModel(hcrApplication().container.somministrazioneRepository)
        }
        initializer {
            InserisciPosologiaViewModel(
                terapiaRepository = hcrApplication().container.terapiaRepository,
                somministrazioneRepository = hcrApplication().container.somministrazioneRepository,
                posologiaRepository = hcrApplication().container.posologiaRepository,
                savedStateHandle = this.createSavedStateHandle()

            )
        }
        initializer{
            InserisciTerapiaViewModel(
                savedStateHandle = this.createSavedStateHandle()
            )
        }


//        initializer {
//            ModificaSomministrazioneViewModel(
//                this.createSavedStateHandle(),
//                somministrazioneRepository = hcrApplication().container.somministrazioneRepository,
//            )
//        }

        initializer {
            PersonalizzazioniViewModel(
                terapieRepository = hcrApplication().container.terapiaRepository,
                posologieRepository = hcrApplication().container.posologiaRepository
            )
        }
        initializer {
            ModificaTerapiaViewModel(
                terapiaRepository = hcrApplication().container.terapiaRepository
            )
        }
        initializer{
            ParametriVitaliViewModel(
                pvRepository = hcrApplication().container.parametroVitaleRepository,
                misurazioniRepository = hcrApplication().container.misurazioneRepository,


            )
        }

        initializer {
            StoricoParametriVitaliViewModel(
                pvRepository = hcrApplication().container.parametroVitaleRepository,
                misurazioniRepository = hcrApplication().container.misurazioneRepository,
                savedStateHandle = this.createSavedStateHandle()
            )
        }
        initializer {
            InserisciParametriVitaliViewModel(
                parametriVitaliRepository = hcrApplication().container.parametroVitaleRepository,
            )
        }

        initializer {
            InserisciMisurazioniViewModel(
                misurazioni = hcrApplication().container.misurazioneRepository,
                savedStateHandle = this.createSavedStateHandle()
            )
        }
        initializer {
            FolderViewModel(
                cartelleRepository = hcrApplication().container.cartellaRepository,
                refertiRepository = hcrApplication().container.refertoRepository,
                posologiaRepository = hcrApplication().container.posologiaRepository,
                terapieRepository = hcrApplication().container.terapiaRepository,
                somministrazioneRepository = hcrApplication().container.somministrazioneRepository
            )
        }
        initializer {
            InserisciCartellaViewModel(
                cartella = hcrApplication().container.cartellaRepository
            )
        }
        initializer {
            InserisciRefertoViewModel(
                referti = hcrApplication().container.refertoRepository,
                cartella = hcrApplication().container.cartellaRepository,
                savedStateHandle = this.createSavedStateHandle()
            )
        }
    }
}

/**
 * Extension function to queries for [Application] object and returns an instance of
 * [InventoryApplication].
 */
fun CreationExtras.hcrApplication(): HcrApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as HcrApplication)

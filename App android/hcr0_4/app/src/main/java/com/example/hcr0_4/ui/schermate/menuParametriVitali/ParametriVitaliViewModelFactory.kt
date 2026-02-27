//package com.example.hcr0_4.ui.schermate.menuParametriVitali
//
//import android.app.Application
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.ViewModelProvider
//import com.example.hcr0_4.data.MisurazioniRepository
//import com.example.hcr0_4.data.ParametriVitaliRepository
//
//class ParamtriVitaliViewModelFactory(
//    private val application: Application,
//    private val pvRepository: ParametriVitaliRepository,
//    private val misurazioniRepository: MisurazioniRepository
//) : ViewModelProvider.Factory {
//
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        if (modelClass.isAssignableFrom(ParametriVitaliViewModel::class.java)) {
//            @Suppress("UNCHECKED_CAST")
//            return ParametriVitaliViewModel(
//                application =application,
//                pvRepository = pvRepository,
//                misurazioniRepository = misurazioniRepository
//            ) as T
//        }
//        throw IllegalArgumentException("Unknown ViewModel class")
//    }
//}

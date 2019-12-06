package com.vferreirati.tormovies.utils

import android.app.Activity
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vferreirati.tormovies.TorApplication

/**
 * Método de extensão para obter o componente da aplicação
 * */
val Activity.injector get() = (application as TorApplication).applicationComponent

val Fragment.injector get() = activity!!.injector

/**
 * Método de entexão para obter o [ViewModel] com escopo de [Activity] através de DI
 * */
@Suppress("UNCHECKED_CAST")
inline fun <reified T : ViewModel> FragmentActivity.viewModel(
    crossinline provider: () -> T
) = viewModels<T> {
    object: ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T = provider() as T
    }
}

/**
 * Método de entexão para obter determinado [ViewModel] com escopo de [Fragment] através de DI
 * */
@Suppress("UNCHECKED_CAST")
inline fun <reified T : ViewModel> Fragment.viewModel(
    crossinline provider: () -> T
) = viewModels<T> {
    object: ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T = provider() as T
    }
}
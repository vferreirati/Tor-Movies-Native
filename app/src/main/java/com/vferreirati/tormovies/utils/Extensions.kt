package com.vferreirati.tormovies.utils

import android.app.Activity
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.coroutineScope
import com.google.android.gms.ads.AdRequest
import com.vferreirati.tormovies.TorApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
    object : ViewModelProvider.Factory {
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
    object : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T = provider() as T
    }
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun AppCompatEditText.addDebouncedTextListener(
    debounceDuration: Long,
    lifecycle: Lifecycle,
    onChanged: (String) -> Unit
) = addTextChangedListener(object: TextWatcher {

    private var job: Job? = null
    private val coroutineScope: CoroutineScope = lifecycle.coroutineScope

    override fun afterTextChanged(newText: Editable?) { }

    override fun beforeTextChanged(newText: CharSequence?, start: Int, before: Int, count: Int) { }

    override fun onTextChanged(newText: CharSequence?, start: Int, before: Int, count: Int) {
        job?.cancel()
        job = coroutineScope.launch {
            newText?.let {
                if (newText.toString().isNotEmpty()) {
                    delay(debounceDuration)
                    onChanged(newText.toString())
                }
            }
        }
    }
})

fun getDefaultRequest(): AdRequest = AdRequest.Builder().addTestDevice("11A8B1813CA154DBAFFF51CCF5584D50").build()

fun Fragment.hideKeyboard() {
    val imm = requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view?.rootView?.windowToken, 0)
}
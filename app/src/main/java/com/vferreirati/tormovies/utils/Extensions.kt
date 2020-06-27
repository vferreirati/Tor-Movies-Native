package com.vferreirati.tormovies.utils

import android.app.Activity
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.AppCompatEditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.coroutineScope
import com.google.android.gms.ads.AdRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

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

fun getDefaultRequest(): AdRequest = AdRequest.Builder().build()

fun Fragment.hideKeyboard() {
    val imm = requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view?.rootView?.windowToken, 0)
}

/**
 * Gets the value of a [LiveData] or waits for it to have one, with a timeout.
 *
 * Use this extension from host-side (JVM) tests. It's recommended to use it alongside
 * `InstantTaskExecutorRule` or a similar mechanism to execute tasks synchronously.
 */
fun <T> LiveData<T>.getOrAwaitValue(
    time: Long = 2,
    timeUnit: TimeUnit = TimeUnit.SECONDS,
    afterObserve: () -> Unit = {}
): T {
    var data: T? = null
    val latch = CountDownLatch(1)
    val observer = object : Observer<T> {
        override fun onChanged(o: T?) {
            data = o
            latch.countDown()
            this@getOrAwaitValue.removeObserver(this)
        }
    }
    this.observeForever(observer)

    afterObserve.invoke()

    // Don't wait indefinitely if the LiveData is not set.
    if (!latch.await(time, timeUnit)) {
        this.removeObserver(observer)
        throw TimeoutException("LiveData value was never set.")
    }

    @Suppress("UNCHECKED_CAST")
    return data as T
}

fun String.asYoutubeUrl() = "https://www.youtube.com/watch?v=$this"
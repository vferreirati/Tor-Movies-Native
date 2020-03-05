package com.vferreirati.tormovies.ui.dialog

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.vferreirati.tormovies.R
import com.vferreirati.tormovies.utils.gone
import com.vferreirati.tormovies.utils.visible
import kotlinx.android.synthetic.main.dialog_preparing_stream.*

class PreparingStreamDialog(private val cancelCallback: () -> Unit) : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.dialog_preparing_stream, container, false)

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        cancelCallback()
    }

    fun updateDialog(message: String, downloadSpeed: String = "", isSpeedVisible: Boolean = false) {
        txtStatus.text = message

        if (isSpeedVisible) {
            txtDownloadSpeed.text = getString(R.string.download_speed, downloadSpeed)
            txtDownloadSpeed.visible()
        } else {
            txtDownloadSpeed.gone()
        }
    }
}
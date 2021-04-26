package br.felipefcosta.mobchat.ui.components

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import br.felipefcosta.mobchat.R
import br.felipefcosta.mobchat.databinding.FragmentLoadingDialogBinding


class LoadingDialogFragment(loadingTextPar: String) : DialogFragment() {

    private lateinit var binding: FragmentLoadingDialogBinding
    private val loadingText = loadingTextPar

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_loading_dialog, container, false)
        //binding.progressBar
        binding.loadingEditText.text = loadingText

        this.isCancelable = false
        this.dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        return binding.root
    }

    fun stopLoadingDialog() {
        this.dismiss()
    }


}
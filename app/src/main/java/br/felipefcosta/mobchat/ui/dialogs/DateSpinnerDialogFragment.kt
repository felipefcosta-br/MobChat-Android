package br.felipefcosta.mobchat.ui.dialogs

import android.icu.text.SimpleDateFormat
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import br.felipefcosta.mobchat.R
import br.felipefcosta.mobchat.databinding.FragmentDateSpinnerDialogBinding
import java.util.*


/**
 * A simple [Fragment] subclass.
 * Use the [DateSpinnerDialogFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DateSpinnerDialogFragment : DialogFragment() {

    lateinit var date: Date
    lateinit var strDate: String
    lateinit var longDate: String


    private lateinit var binding: FragmentDateSpinnerDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_date_spinner_dialog, container, false
        )

        formatDate(binding.datePickerAddUserForm.year,
            binding.datePickerAddUserForm.month,
            binding.datePickerAddUserForm.dayOfMonth)

        binding.datePickerAddUserForm.setOnDateChangedListener { view, year, monthOfYear, dayOfMonth ->
            formatDate(year, monthOfYear, dayOfMonth)
        }
        binding.okDateSpinnerBtn.setOnClickListener {

            if ((strDate.isNotEmpty())&&(longDate.isNotEmpty())){
                val resultArray = arrayOf<String>(strDate, longDate)
                setFragmentResult("requestDateSpinnerDialogFragment",
                    bundleOf("dateResult" to resultArray)
                )
            }
            this.dismiss()
        }
        binding.cancelDateSpinnerBtn.setOnClickListener {
            this.dismiss()
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
        val height = (resources.displayMetrics.heightPixels * 0.40).toInt()
        dialog!!.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)


    }
    private fun formatDate(year: Int, monthOfYear: Int, dayOfMonth: Int){

        var calendar = Calendar.getInstance()
        calendar.set(year, monthOfYear, dayOfMonth)

        date = calendar.getTime()
        val formatter = SimpleDateFormat("dd/MM/yyyy")

        longDate = date.toString()
        strDate = formatter.format(calendar.time)

    }

}
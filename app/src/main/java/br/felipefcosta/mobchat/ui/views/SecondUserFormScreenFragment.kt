package br.felipefcosta.mobchat.ui.views

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import androidx.activity.addCallback
import androidx.core.app.ActivityCompat
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import br.felipefcosta.mobchat.R
import br.felipefcosta.mobchat.api.AuthApiService
import br.felipefcosta.mobchat.api.ProfileApiService
import br.felipefcosta.mobchat.databinding.FragmentSecondUserFormScreenBinding
import br.felipefcosta.mobchat.models.entities.AppUser
import br.felipefcosta.mobchat.models.repositories.AuthRepository
import br.felipefcosta.mobchat.models.repositories.ProfileRepository
import br.felipefcosta.mobchat.models.services.AuthDataSource
import br.felipefcosta.mobchat.models.services.EncryptionManager
import br.felipefcosta.mobchat.models.services.ProfileDataSource
import br.felipefcosta.mobchat.models.services.TokenStorageManager
import br.felipefcosta.mobchat.ui.dialogs.DateSpinnerDialogFragment
import br.felipefcosta.mobchat.ui.dialogs.LoadingDialogFragment
import br.felipefcosta.mobchat.viewmodels.SecondUserFormScreenFragmentViewModel
import br.felipefcosta.mobchat.viewmodels.SecondUserFormViewModelFactory
import com.google.android.material.snackbar.Snackbar
import java.util.*

/**
 *
 */
class SecondUserFormScreenFragment : Fragment() {


    lateinit var binding: FragmentSecondUserFormScreenBinding
    lateinit var viewModel: SecondUserFormScreenFragmentViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_second_user_form_screen, container, false)
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_second_user_form_screen, container, false
        )

        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            ActivityCompat.finishAffinity(requireActivity())
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle = arguments
        if (bundle == null) {
            Log.e("Confirmation", "ConfirmationFragment did not receive traveler information")
            return
        }
        val args = SecondUserFormScreenFragmentArgs.fromBundle(bundle)
        if (args.appUserArg.username == null && args.usernameArgs == null && args.passArg == null) {
            return
        }

        viewModel = ViewModelProvider(
            this,
            SecondUserFormViewModelFactory(this.requireActivity().application)
        )
            .get(SecondUserFormScreenFragmentViewModel::class.java)

        binding.viewmodel = viewModel
        binding.lifecycleOwner = this

        viewModel.appUser = args.appUserArg
        viewModel.appUser.username = args.usernameArgs


        setFragmentResultListener("requestDateSpinnerDialogFragment") { requestKey, bundle ->

            val dateArray = bundle.getStringArray("dateResult")
            binding.addBirthDateEditText.setText(dateArray?.get(0))

        }

        val items = listOf(
            getString(R.string.list_item_male),
            getString(R.string.list_item_female),
            getString(R.string.list_item_other)
        )
        val adapter = ArrayAdapter<String>(
            requireContext(), R.layout.dropdown_list_item,
            R.id.itemDropdownListTextView, items
        )

        (binding.genderDropdownAutoCompLayout?.editText as? AutoCompleteTextView)?.setAdapter(
            adapter
        )

        /*val countries = viewModel.getAllCountries()
        val countriesAdapter = ArrayAdapter<String>(
            requireContext(), R.layout.dropdown_list_item,
            R.id.itemDropdownListTextView, countries
        )
        (binding.countryDropdownAutoCompLayout.editText as AutoCompleteTextView).setAdapter(
            countriesAdapter
        )*/

        binding.addNameEditText.doAfterTextChanged {
            binding.addNameEditTextLayout.error = viewModel.validateName(it.toString())
        }
        binding.addSurnameEditText.doAfterTextChanged {
            binding.addSurnameEditTextLayout.error = viewModel.validateSurname(it.toString())
        }
        binding.addUserCityEditText.doAfterTextChanged {
            binding.addUserCityEditTextLayout.error = viewModel.validateCity(it.toString())
        }
        binding.addUserCountryEditText.doAfterTextChanged {
            binding.addUserCountryEditTextLayout.error = viewModel.validateCountry(it.toString())
        }
        binding.addBirthDateEditText.doAfterTextChanged {
            binding.addBirthDateEditTextLayout.error = viewModel.validateBirthDate(it.toString())
        }
        binding.genderDropdownAutoComp.setOnFocusChangeListener { view, hasFocus ->
            if (!hasFocus) {
                val dropDown = view as EditText
                binding.genderDropdownAutoCompLayout.error =
                    viewModel.validateGender(dropDown.text.toString())
            }
        }

        binding.addBirthDateEditText.setOnClickListener {

            val dateSpinnerDialogFragment = DateSpinnerDialogFragment()
            dateSpinnerDialogFragment.show(parentFragmentManager, "")

        }

        binding.goToThirdUserFormScreenBtn.setOnClickListener {
            val loadingDialogFragment = LoadingDialogFragment(getString(R.string.str_loading_profile))
            if (viewModel.isReadyToAddProfile()) {
                loadingDialogFragment.show(parentFragmentManager, "")
                val profile = viewModel.fillUserProfile()
                if (profile != null){
                    val action = SecondUserFormScreenFragmentDirections.secondToThirdAction(profile, args.passArg!!)
                    findNavController().navigate(action)
                    loadingDialogFragment.stopLoadingDialog()
                }


            } else {
                binding.addNameEditTextLayout.error =
                    viewModel.validateName(binding.addNameEditText.text.toString())
                binding.addSurnameEditTextLayout.error =
                    viewModel.validateSurname(binding.addSurnameEditText.text.toString())
                binding.addUserCityEditTextLayout.error =
                    viewModel.validateCity(binding.addUserCityEditText.text.toString())
                binding.addUserCountryEditTextLayout.error =
                    viewModel.validateCountry(binding.addUserCountryEditText.text.toString())
                binding.addBirthDateEditTextLayout.error =
                    viewModel.validateBirthDate(binding.addBirthDateEditText.text.toString())
                binding.genderDropdownAutoCompLayout.error =
                    viewModel.validateGender(binding.genderDropdownAutoComp.text.toString())
            }

        }


    }
}
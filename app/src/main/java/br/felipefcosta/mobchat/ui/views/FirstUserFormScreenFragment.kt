package br.felipefcosta.mobchat.ui.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import br.felipefcosta.mobchat.R
import br.felipefcosta.mobchat.api.AppUserApiService
import br.felipefcosta.mobchat.api.AuthApiService
import br.felipefcosta.mobchat.databinding.FragmentFirstUserFormScreenBinding
import br.felipefcosta.mobchat.models.repositories.AppUserRepository
import br.felipefcosta.mobchat.models.repositories.AuthRepository
import br.felipefcosta.mobchat.models.services.AppUserDataSource
import br.felipefcosta.mobchat.models.services.AuthDataSource
import br.felipefcosta.mobchat.models.services.EncryptionManager
import br.felipefcosta.mobchat.models.services.TokenStorageManager
import br.felipefcosta.mobchat.utils.Constants
import br.felipefcosta.mobchat.ui.dialogs.LoadingDialogFragment
import br.felipefcosta.mobchat.viewmodels.FirstUserFormScreenFragmentViewModel
import br.felipefcosta.mobchat.viewmodels.FirstUserFormViewModelFactory
import com.google.android.material.snackbar.Snackbar


/**

 */
class FirstUserFormScreenFragment : Fragment() {

    lateinit var binding: FragmentFirstUserFormScreenBinding
    lateinit var viewModel: FirstUserFormScreenFragmentViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_first_user_form_screen, container, false)
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_first_user_form_screen, container, false
        )

        val encryptionManager = EncryptionManager(requireContext())
        val tokenStorageManager = TokenStorageManager(requireContext(), encryptionManager)

        val authService = AuthApiService.create(Constants.EMAIL, Constants.PASSWORD)
        val authDataSource = AuthDataSource(authService)
        val authRepository = AuthRepository(authDataSource, tokenStorageManager)

        val appUserService = AppUserApiService.create()
        val appUserDataSource = AppUserDataSource(appUserService)
        val repository = AppUserRepository(appUserDataSource)

        viewModel =
            ViewModelProvider(
                this,
                FirstUserFormViewModelFactory(
                    this.requireActivity().application,
                    repository,
                    authRepository
                )
            )
                .get(FirstUserFormScreenFragmentViewModel::class.java)

        binding.viewmodel = viewModel
        binding.lifecycleOwner = this

        binding.addUserNameEditText.doAfterTextChanged {
            binding.addUserNameEditTextLayout.error = viewModel.validateUserName(it.toString())
        }
        binding.addEmailEditText.doAfterTextChanged {
            binding.addEmailEditTextLayout.error = viewModel.validateEmail(it.toString())
        }
        binding.addMobileNumberEditText.doAfterTextChanged {
            binding.addMobileNumberEditTextLayout.error =
                viewModel.validateMobileNumber(it.toString())
        }
        binding.addPasswordEditText.doAfterTextChanged {
            binding.addPasswordEditTextLayout.error =
                viewModel.validatePassword(it.toString())

        }
        binding.addPasswordConfirmEditText.doAfterTextChanged {
            binding.addPasswordConfirmEditTextLayout.error =
                viewModel.validatePasswordConfirmation(
                    binding.addPasswordEditText.text.toString(),
                    it.toString()
                )

        }

        binding.firstSignUpCloseBtn.setOnClickListener {
            //Navigation.createNavigateOnClickListener(R.id.first_to_login_action, null)
            //val flowStepNumberArg = 1
            val action = FirstUserFormScreenFragmentDirections.firstToLoginAction()
            findNavController().navigate(action)
        }

        binding.goToSecondUserFormScreenBtn.setOnClickListener {
            val loadingDialogFragment =
                LoadingDialogFragment(getString(R.string.str_loading_account))
            if (viewModel.isReadyToAddUser()) {
                loadingDialogFragment.show(parentFragmentManager, "")
                viewModel.addNewUser({
                    val action = FirstUserFormScreenFragmentDirections.firstToSecondAction(
                        it,
                        viewModel.password,
                        viewModel.username
                    )
                    findNavController().navigate(action)
                    loadingDialogFragment.stopLoadingDialog()
                }, {
                    val failureSnack = Snackbar.make(
                        it,
                        getString(R.string.str_add_user_failure),
                        Snackbar.LENGTH_LONG
                    )
                    loadingDialogFragment.stopLoadingDialog()
                    failureSnack.show()
                })

            } else {
                binding.addUserNameEditTextLayout.error =
                    viewModel.validateUserName(binding.addUserNameEditText.text.toString())
                binding.addEmailEditTextLayout.error =
                    viewModel.validateEmail(binding.addEmailEditText.text.toString())
                binding.addMobileNumberEditTextLayout.error =
                    viewModel.validateMobileNumber(binding.addMobileNumberEditText.text.toString())
                binding.addPasswordEditTextLayout.error =
                    viewModel.validatePassword(binding.addPasswordEditText.text.toString())
                binding.addPasswordConfirmEditTextLayout.error =
                    viewModel.validatePasswordConfirmation(
                        binding.addPasswordEditText.text.toString(),
                        binding.addPasswordConfirmEditText.text.toString()
                    )

            }
        }

        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            val action = FirstUserFormScreenFragmentDirections.firstToLoginAction()
            findNavController().navigate(action)
        }

        return binding.root
    }

}
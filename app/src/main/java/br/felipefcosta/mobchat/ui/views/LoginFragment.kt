package br.felipefcosta.mobchat.ui.views

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.activity.addCallback
import androidx.core.app.ActivityCompat
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import br.felipefcosta.mobchat.R
import br.felipefcosta.mobchat.api.AuthApiService
import br.felipefcosta.mobchat.api.StorageBlobApiService
import br.felipefcosta.mobchat.databinding.FragmentLoginBinding
import br.felipefcosta.mobchat.models.repositories.AuthRepository
import br.felipefcosta.mobchat.models.repositories.StorageBlobRepository
import br.felipefcosta.mobchat.models.services.AuthDataSource
import br.felipefcosta.mobchat.models.services.EncryptionManager
import br.felipefcosta.mobchat.models.services.StorageBlobDataSource
import br.felipefcosta.mobchat.models.services.TokenStorageManager
import br.felipefcosta.mobchat.utils.Constants
import br.felipefcosta.mobchat.viewmodels.LoginFragmentViewModel
import br.felipefcosta.mobchat.viewmodels.LoginViewModelFactory
import com.google.android.material.snackbar.Snackbar

/**

 */
class LoginFragment : Fragment() {

    lateinit var binding: FragmentLoginBinding
    lateinit var viewModel: LoginFragmentViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_login, container, false)
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_login, container, false
        )

        val encryptionManager = EncryptionManager(requireContext())
        val tokenStorageManager = TokenStorageManager(requireContext(), encryptionManager)

        val authService = AuthApiService.create()
        val authDataSource = AuthDataSource(authService)
        val authRepository = AuthRepository(authDataSource, tokenStorageManager)

        viewModel =
            ViewModelProvider(
                this,
                LoginViewModelFactory(this.requireActivity().application, authRepository)
            )
                .get(LoginFragmentViewModel::class.java)

        binding.viewmodel = viewModel
        binding.lifecycleOwner = this

        /*val safeArgs : LoginFragmentArgs by navArgs()
        val flowStepNumber = safeArgs.flowStepNumber*/

        if (activity?.window != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                activity?.window?.insetsController?.show(WindowInsets.Type.statusBars())
            } else {
                activity?.window?.clearFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN
                )
                activity?.window?.addFlags(
                    WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN
                )
            }
        }

        binding.emailLoginEditTxt.doAfterTextChanged {
//            Log.i("ProMIT", it.toString())
            binding.emailLoginEditTxtLayout.error = viewModel.validateEmail(it.toString())
        }

        binding.passwordLoginEditTxt.doAfterTextChanged {
            binding.passwordLoginEditTxtLayout.error = viewModel.validatePassword(it.toString())
        }

        binding.loginBtn.setOnClickListener {

            if (viewModel.isReadyToAuthentication()) {
                viewModel.authenticateUser({
                        val mainActivity = Intent(activity, MainActivity::class.java)
                        startActivity(mainActivity)
                        this.activity?.finish()
                    }, {
                        val failureSnack = Snackbar.make(
                            it,
                            getString(R.string.str_login_failure),
                            Snackbar.LENGTH_LONG
                        )
                        //loadingDialogFragment.stopLoadingDialog()
                        failureSnack.show()
                    }
                )
            } else {
                binding.emailLoginEditTxtLayout.error =
                    viewModel.validateEmail(binding.emailLoginEditTxt.text.toString())
                binding.passwordLoginEditTxtLayout.error =
                    viewModel.validatePassword(binding.passwordLoginEditTxt.text.toString())
            }
        }

        binding.signUpBtn.setOnClickListener {
            val action = LoginFragmentDirections.loginToFirstAction()
            findNavController().navigate(action)
        }

        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            ActivityCompat.finishAffinity(requireActivity())
        }

        return binding.root
    }

    override fun onPause() {
        super.onPause()

    }

}
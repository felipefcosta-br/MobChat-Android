package br.felipefcosta.mobchat.ui.views

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import br.felipefcosta.mobchat.R
import br.felipefcosta.mobchat.api.AppUserApiService
import br.felipefcosta.mobchat.api.AuthApiService
import br.felipefcosta.mobchat.api.ProfileApiService
import br.felipefcosta.mobchat.databinding.FragmentSplashBinding
import br.felipefcosta.mobchat.models.repositories.AppUserRepository
import br.felipefcosta.mobchat.models.repositories.AuthRepository
import br.felipefcosta.mobchat.models.repositories.ProfileRepository
import br.felipefcosta.mobchat.models.services.*
import br.felipefcosta.mobchat.viewmodels.SplashFragmentViewModel
import br.felipefcosta.mobchat.viewmodels.SplashViewModelFactory


/**

 */
class SplashFragment : Fragment() {

    lateinit var binding: FragmentSplashBinding
    lateinit var viewModel: SplashFragmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_splash, container, false
        )
        val encryptionManager = EncryptionManager(requireContext())
        val tokenStorageManager = TokenStorageManager(requireContext(), encryptionManager)
        val profileStorageManager = ProfileStorageManager(requireContext(), encryptionManager)

        val authService = AuthApiService.create()
        val authDataSource = AuthDataSource(authService)
        val authRepository = AuthRepository(authDataSource, tokenStorageManager)

        val appUserService = AppUserApiService.create()
        val appUserDataSource = AppUserDataSource(appUserService)
        val appUserRepository = AppUserRepository(appUserDataSource)

        val profileService = ProfileApiService.create()
        val profileDataSource = ProfileDataSource(profileService)
        val profileRepository =
            ProfileRepository(profileDataSource, tokenStorageManager, profileStorageManager)

        viewModel = ViewModelProvider(
            this,
            SplashViewModelFactory(
                this.requireActivity().application,
                authRepository,
                appUserRepository,
                profileRepository
            )
        ).get(SplashFragmentViewModel::class.java)

        binding.viewmodel = viewModel
        binding.lifecycleOwner = this

        if (activity?.window != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                activity?.window?.insetsController?.hide(WindowInsets.Type.statusBars())
            } else {
                activity?.window?.setFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN
                )
            }
        }
        startAppAuth()

        return binding.root
    }

    fun startAppAuth() {

        if (viewModel.isValideToken()) {
            viewModel.getUserProfile({ profile ->
                if (profile != null) {
                    val mainActivity = Intent(activity, MainActivity::class.java)
                    startActivity(mainActivity)
                    /*val action = SplashFragmentDirections.spalshToMainNav(profile)
                    findNavController().navigate(action)*/
                    this.activity?.finish()
                } else {
                    viewModel.getAppUser({ appUser ->
                        viewModel.getToken({
                            val action = SplashFragmentDirections.splashToSecondAction(
                                appUser,
                                null,
                                null,
                                it.accessToken
                            )
                            findNavController().navigate(action)
                        }, {

                        })

                    }, {

                    })
                }
            }, {
                val action = SplashFragmentDirections.splahToLoginAction()
                findNavController().navigate(action)
            })

        } else {
            val action = SplashFragmentDirections.splahToLoginAction()
            findNavController().navigate(action)
        }
    }
}
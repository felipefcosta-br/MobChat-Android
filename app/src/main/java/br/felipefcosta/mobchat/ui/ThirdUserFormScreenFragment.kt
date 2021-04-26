package br.felipefcosta.mobchat.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.addCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import br.felipefcosta.mobchat.R
import br.felipefcosta.mobchat.core.AuthApiService
import br.felipefcosta.mobchat.core.ProfileApiService
import br.felipefcosta.mobchat.core.StorageBlobApiService
import br.felipefcosta.mobchat.databinding.FragmentThirdUserFormScreenBinding
import br.felipefcosta.mobchat.models.entities.Profile
import br.felipefcosta.mobchat.models.repositories.AuthRepository
import br.felipefcosta.mobchat.models.repositories.ProfileRepository
import br.felipefcosta.mobchat.models.repositories.StorageBlobRepository
import br.felipefcosta.mobchat.models.services.*
import br.felipefcosta.mobchat.ui.components.LoadingDialogFragment
import br.felipefcosta.mobchat.presentation.ThirdUserFormScreenFragmentViewModel
import br.felipefcosta.mobchat.presentation.ThirdUserFormViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import java.io.File

/**

 */
class ThirdUserFormScreenFragment : Fragment() {

    lateinit var viewModel: ThirdUserFormScreenFragmentViewModel
    private lateinit var binding: FragmentThirdUserFormScreenBinding
    private lateinit var photoBottomSheetDialog: BottomSheetDialog
    private lateinit var takeCameraPicture: ActivityResultLauncher<Uri>
    private lateinit var takeGalleryPicture: ActivityResultLauncher<String>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_third_user_form_screen, container, false)
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_third_user_form_screen, container, false
        )
        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {

        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle = arguments
        if (bundle == null) {
            Log.e("Confirmation", "ConfirmationFragment did not receive action information")
            return
        }
        val args = ThirdUserFormScreenFragmentArgs.fromBundle(bundle)
        val profileArgs = args.profileArgs as Profile

        if (profileArgs == null && profileArgs.userName == null) {
            return
        }

        val userAccountId = profileArgs.accountId.toString()

        val encryptionManager = EncryptionManager(requireContext())
        val tokenStorageManager = TokenStorageManager(requireContext(), encryptionManager)
        val profileStorageManager = ProfileStorageManager(requireContext(), encryptionManager)

        val storageBlobApiService = StorageBlobApiService()
        val storageBlobDataSource = StorageBlobDataSource(storageBlobApiService)
        val storageBlobRepository = StorageBlobRepository(storageBlobDataSource)

        val authService = AuthApiService.create()
        val authDataSource = AuthDataSource(authService)
        val authRepository = AuthRepository(authDataSource, tokenStorageManager)

        val profileService = ProfileApiService.create()
        val profileDataSource = ProfileDataSource(profileService)
        val repository =
            ProfileRepository(profileDataSource, tokenStorageManager, profileStorageManager)

        viewModel = ViewModelProvider(
            this,
            ThirdUserFormViewModelFactory(
                this.requireActivity().application,
                repository,
                authRepository,
                storageBlobRepository
            )
        ).get(ThirdUserFormScreenFragmentViewModel::class.java)

        binding.viewmodel = viewModel
        binding.lifecycleOwner = this

        viewModel.profile = profileArgs

        binding.profileImageView.setImageDrawable(
            resources.getDrawable(
                R.drawable.ic_account_circle,
                null
            )
        )

        binding.addUserProfilePhotoBtn.setOnClickListener {

            val photoBottomView =
                layoutInflater.inflate(R.layout.fragment_photo_bottom_sheet_dialog, null)
            photoBottomSheetDialog = BottomSheetDialog(requireContext())
            photoBottomSheetDialog.setContentView(photoBottomView)
            photoBottomSheetDialog.show()

            val cameraBtn = photoBottomView.findViewById<Button>(R.id.openCameraBtn)
            cameraBtn.setOnClickListener {
                startCamera(userAccountId)
                photoBottomSheetDialog.dismiss()
            }
            val galleryBtn = photoBottomView.findViewById<Button>(R.id.openGalleryBtn)
            galleryBtn.setOnClickListener {
                openGallery()
                photoBottomSheetDialog.dismiss()
            }
        }

        binding.finishUserFormBtn.setOnClickListener {
            addUserProfile(it)
        }
        takeCameraPicture =
            registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
                if (isSuccess) {
                    Picasso.get().load(viewModel.photoUri)
                        .resize(440, 450)
                        .centerCrop()
                        .into(binding.profileImageView)

                }
            }
        takeGalleryPicture =
            registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
                if (uri != null) {
                    Picasso.get().load(uri)
                        .resize(440, 450)
                        .centerCrop()
                        .into(binding.profileImageView)
                    viewModel.photoUri = uri
                }
            }
    }

    private fun addUserProfile(view: View) {

        val loadingDialogFragment = LoadingDialogFragment(getString(R.string.str_loading_profile))
        loadingDialogFragment.show(parentFragmentManager, "")
        viewModel.updateProfile(requireContext(), {

            val mainActivity = Intent(activity, MainActivity::class.java)
            startActivity(mainActivity)
            /*val action = ThirdUserFormScreenFragmentDirections.thirdToMainNav(profile)
            findNavController().navigate(action)*/
            loadingDialogFragment.stopLoadingDialog()
            this.activity?.finish()

        }, {
            val failureSnack = Snackbar.make(
                view,
                getString(R.string.str_add_profile_failure),
                Snackbar.LENGTH_LONG
            )
            loadingDialogFragment.stopLoadingDialog()
            failureSnack.show()
        })
    }

    private fun startCamera(userAccountId: String) {
        val photoFile: File = viewModel.createImageFile(requireContext(), userAccountId)
        photoFile.also {
            viewModel.photoUri = FileProvider.getUriForFile(
                requireContext(),
                "br.felipefcosta.mobchat.file_provider",
                it
            )
            takeCameraPicture.launch(viewModel.photoUri)
        }
    }

    private fun openGallery() {
        takeGalleryPicture.launch("image/*")
    }

    /*fun hasCameraPermission() = ContextCompat.checkSelfPermission(
        requireContext(),
        Manifest.permission.CAMERA
    ) == PackageManager.PERMISSION_GRANTED

    private val requestMultiplePermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { resultsMap ->
        resultsMap.forEach {
            Log.i("ProMIT", "Permission: ${it.key}, granted: ${it.value}")
        }
    }

    requestMultiplePermissionLauncher.launch(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA))*/


}
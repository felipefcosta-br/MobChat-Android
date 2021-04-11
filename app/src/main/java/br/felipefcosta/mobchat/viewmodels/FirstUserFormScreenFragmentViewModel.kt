package br.felipefcosta.mobchat.viewmodels

import android.app.Application
import android.util.Log
import android.util.Patterns
import androidx.lifecycle.AndroidViewModel
import br.felipefcosta.mobchat.R
import br.felipefcosta.mobchat.models.dtos.Password
import br.felipefcosta.mobchat.models.dtos.User
import br.felipefcosta.mobchat.models.dtos.UserPasswordDto
import br.felipefcosta.mobchat.models.entities.AppUser
import br.felipefcosta.mobchat.models.repositories.AppUserRepository
import br.felipefcosta.mobchat.models.repositories.AuthRepository
import com.google.android.material.snackbar.Snackbar

class FirstUserFormScreenFragmentViewModel(
    application: Application,
    private val repository: AppUserRepository,
    private val authRepository: AuthRepository
) : AndroidViewModel(application) {

    lateinit var username: String
    lateinit var email: String
    lateinit var mobileNumber: String
    lateinit var password: String
    lateinit var confirmPassword: String
    lateinit var appUser: AppUser

    fun addNewUser(success: (AppUser) -> Unit, failure: () -> Unit) {
        if (isReadyToAddUser()) {
            val user = User(
                email,
                email,
                true,
                mobileNumber,
                true,
                true
            )
            val password = Password(password, confirmPassword)
            val userPasswordDto = UserPasswordDto(user, password)
            authRepository.getAdminToken({ token ->
                repository.addAppUser(token, userPasswordDto, {
                    success(it)
                    Log.i("ProMIT", it.username.toString())
                }, {
                    failure()
                })
            }, {
                failure()
            })
        }
    }

    fun isReadyToAddUser(): Boolean {

        if (this::username.isInitialized && this::email.isInitialized && this::password.isInitialized
            && this::confirmPassword.isInitialized
        ) {
            if (isValidEmail(email) && isValidPassword(password) && isValidUserName(username))
                return true
        }
        return false
    }

    fun validateUserName(userNameTxt: String): String? {
        var respTxt: String? =
            getApplication<Application>().getString(R.string.msg_valid_char_username)
        username = userNameTxt
        if (isValidUserName(userNameTxt)) {
            respTxt = null
        }
        return respTxt
    }

    fun validateEmail(emailTxt: String): String? {
        var respTxt: String? = getApplication<Application>().getString(R.string.msg_valid_email)
        email = emailTxt
        if (isValidEmail(emailTxt)) {
            respTxt = null
        }
        return respTxt
    }

    fun validateMobileNumber(numberTxt: String): String? {
        var respTxt: String? =
            getApplication<Application>().getString(R.string.msg_valid_mobile_number)
        mobileNumber = numberTxt

        if (isValidMobileNumber(numberTxt))
            respTxt = null

        return respTxt
    }

    fun validatePassword(passwordTxt: String): String? {
        var respTxt: String? = getApplication<Application>().getString(R.string.msg_valid_password)
        password = passwordTxt

        if (isValidPassword(passwordTxt))
            respTxt = null

        return respTxt
    }

    fun validatePasswordConfirmation(
        passwordTxt: String,
        passwordConfirmationTxt: String
    ): String? {
        var respTxt: String? =
            getApplication<Application>().getString(R.string.msg_not_confirm_password)
        confirmPassword = passwordConfirmationTxt

        if (isPasswordConfirmed(passwordTxt, confirmPassword))
            respTxt = null

        return respTxt
    }

    private fun isValidUserName(userName: String): Boolean {
        val regexString = "(?=^.{3,22}\$)([a-z]+)([a-z0-9\\n]*)"
        return regexString.toRegex().matches(userName)
    }

    private fun isValidEmail(email: String): Boolean {
        return email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isValidMobileNumber(number: String): Boolean {
        val regexString = "(?=^.{6,19}\$)([0-9\n]+)"
        return regexString.toRegex().matches(number)
    }

    private fun isValidPassword(password: String): Boolean {
        //([a-z]+)([a-z0-9\n]*)
        val regexString = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#\$%^&+=!])(?=\\S+$).{8,}$"
        return regexString.toRegex().matches(password)
    }

    private fun isPasswordConfirmed(password: String, passwordConfirmation: String): Boolean {
        return passwordConfirmation == password
    }

}
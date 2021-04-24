package br.felipefcosta.mobchat.presentation

import android.app.Application
import android.util.Log
import android.util.Patterns
import androidx.lifecycle.AndroidViewModel
import br.felipefcosta.mobchat.R
import br.felipefcosta.mobchat.models.entities.Token
import br.felipefcosta.mobchat.models.repositories.AuthRepository
import br.felipefcosta.mobchat.utils.Constants

class LoginFragmentViewModel(application: Application, private val authRepository: AuthRepository) :
    AndroidViewModel(application) {

    lateinit var token: String
    lateinit var email: String
    lateinit var password: String

    fun authenticateUser(
        success: (Token) -> Unit,
        failure: () -> Unit
    ) {
        if (isReadyToAuthentication()) {

            var authMap = HashMap<String, String>()
            authMap["grant_type"] = Constants.GRANT_TYPE
            authMap["userName"] = email
            authMap["password"] = password
            authMap["client_id"] = Constants.CLIENT_ID

            authRepository.getToken(authMap, { token ->
                if (!token.accessToken.isNullOrBlank()) {
                    Log.i("ProMIT", "repository - ${token.accessToken.toString()}")
                    success(token)
                } else {
                    failure()
                }
            }, {
                failure()
            })
        }
    }

    fun isReadyToAuthentication(): Boolean {

        if (this::email.isInitialized && this::password.isInitialized) {
            if (isValidEmail(email) && isValidPassword(password))
                return true
        }
        return false
    }

    fun validateEmail(emailTxt: String): String? {
        var respTxt: String? = getApplication<Application>().getString(R.string.msg_valid_email)
        email = emailTxt
        if (isValidEmail(emailTxt)) {
            respTxt = null
        }
        return respTxt
    }

    fun validatePassword(passwordTxt: String): String? {
        var respTxt: String? = getApplication<Application>().getString(R.string.msg_empty_password)
        password = passwordTxt
        if (isValidPassword(passwordTxt)) {
            respTxt = null
        }
        return respTxt
    }

    private fun isValidEmail(email: String): Boolean {
        return email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isValidPassword(password: String): Boolean {
        return password.isNotEmpty() && password.isNotBlank()

    }
}
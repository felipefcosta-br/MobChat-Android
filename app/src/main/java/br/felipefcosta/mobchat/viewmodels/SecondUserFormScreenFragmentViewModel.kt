package br.felipefcosta.mobchat.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import br.felipefcosta.mobchat.R
import br.felipefcosta.mobchat.models.entities.AppUser
import br.felipefcosta.mobchat.models.entities.Profile
import br.felipefcosta.mobchat.models.repositories.AuthRepository
import br.felipefcosta.mobchat.models.repositories.ProfileRepository
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList

class SecondUserFormScreenFragmentViewModel(
    application: Application,
    private val repository: ProfileRepository,
    private val authRepository: AuthRepository,
) : AndroidViewModel(application) {
    lateinit var appUser: AppUser
    lateinit var name: String
    lateinit var surname: String
    lateinit var city: String
    lateinit var country: String
    lateinit var gender: String
    lateinit var birthDateStr: String
    lateinit var birthDate: String
    lateinit var registrationDate: String

    fun addProfile(success: (Profile) -> Unit, failure: () -> Unit) {

        if (isReadyToAddProfile()) {
            birthDate = formatBirthDate(birthDateStr)
            registrationDate = LocalDateTime.now().toString()

            val profile = Profile(
                null,
                appUser.id.toString(),
                name,
                surname,
                gender,
                city,
                country,
                "",
                "",
                appUser.username.toString(),
                appUser.email.toString(),
                appUser.phoneNumber.toString(),
                true,
                true,
                birthDate,
                registrationDate
            )
            authRepository.getToken({ token ->
                repository.addProfile(profile, token, {
                    success(it)
                }, {
                    failure()
                })
            }, {
                failure()
            })
        }

    }

    /*fun getAllCountries(): ArrayList<String> {
        val locales = Locale.getAvailableLocales()
        var countries = ArrayList<String>()
        locales.forEach {
            var country = it.displayCountry
            if (country.trim().isNotEmpty() && !countries.contains(country))
                countries.add(country)
        }
        return countries
    }*/

    fun isReadyToAddProfile(): Boolean {

        if (this::name.isInitialized && this::surname.isInitialized && this::city.isInitialized
            && this::country.isInitialized && this::birthDateStr.isInitialized && this::gender.isInitialized
        ) {
            if (isValidName(name) && isValidSurname(surname) && isValidCity(city) &&
                isValidCountry(country) && isValidBirthDate(birthDateStr) && isValidGender(gender)
            )
                return true
        }
        return false
    }

    fun validateName(nameTxt: String): String? {
        var respTxt: String? = getApplication<Application>().getString(R.string.msg_valid_char_name)
        name = nameTxt
        if (isValidName(nameTxt)) {
            respTxt = null
        }
        return respTxt
    }

    fun validateSurname(surnameTxt: String): String? {
        var respTxt: String? =
            getApplication<Application>().getString(R.string.msg_valid_char_surname)
        surname = surnameTxt
        if (isValidSurname(surnameTxt)) {
            respTxt = null
        }
        return respTxt
    }

    fun validateCity(cityTxt: String): String? {
        var respTxt: String? = getApplication<Application>().getString(R.string.msg_valid_char_city)
        city = cityTxt
        if (isValidCity(cityTxt)) {
            respTxt = null
        }
        return respTxt

    }

    fun validateCountry(countryTxt: String): String? {
        var respTxt: String? =
            getApplication<Application>().getString(R.string.msg_valid_char_country)
        country = countryTxt
        if (isValidCountry(countryTxt)) {
            respTxt = null
        }
        return respTxt
    }

    fun validateBirthDate(birthDateTxt: String): String? {
        var respTxt: String? =
            getApplication<Application>().getString(R.string.msg_valid_char_birth_date)
        birthDateStr = birthDateTxt
        if (isValidBirthDate(birthDateTxt)) {
            respTxt = null
        }
        return respTxt
    }

    fun validateGender(genderTxt: String): String? {
        var respTxt: String? =
            getApplication<Application>().getString(R.string.msg_valid_char_gender)
        gender = genderTxt
        if (isValidGender(genderTxt)) {
            respTxt = null
        }
        return respTxt
    }

    private fun isValidName(name: String): Boolean {
        val regexString = "(?=^.{2,28}\$)([A-Za-z\\s]+)"
        return regexString.toRegex().matches(name)
    }

    private fun isValidSurname(surname: String): Boolean {
        val regexString = "(?=^.{2,40}\$)([A-Za-z\\s]+)"
        return regexString.toRegex().matches(surname)
    }

    private fun isValidCity(city: String): Boolean {
        val regexString = "(?=^.{3,36}\$)([A-Za-z\\s]+)"
        return regexString.toRegex().matches(city)
    }

    private fun isValidCountry(country: String): Boolean {
        val regexString = "(?=^.{2,36}\$)([A-Za-z\\s]+)"
        return regexString.toRegex().matches(country)
    }

    private fun isValidBirthDate(birthDate: String): Boolean {
        return (!birthDate.isNullOrBlank())
    }

    private fun isValidGender(gender: String): Boolean {
        return (!gender.isNullOrBlank())
    }

    private fun formatBirthDate(birthDate: String): String {
        var formatData = SimpleDateFormat("dd/MM/yyyy")
        var formatFinal = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        var date = formatData.parse(birthDate)

        return formatFinal.format(date).toString()
    }
}
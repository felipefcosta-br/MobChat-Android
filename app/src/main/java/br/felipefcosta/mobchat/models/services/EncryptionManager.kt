package br.felipefcosta.mobchat.models.services

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import br.felipefcosta.mobchat.R
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec

class EncryptionManager(val context: Context) {

    companion object {
        const val KEYSTORE_NAME = "AndroidKeyStore"
        const val SYMMETRIC_KEY_ALIAS = "encryptionKey"
        const val SYMMETRIC_ALGORITHM = "AES/CBC/PKCS7Padding"
        const val IV_SEPARATOR = "]"
    }

    private val keyStore: KeyStore = KeyStore.getInstance(KEYSTORE_NAME)
    private val symmetricKey: SecretKey
    private val cipher: Cipher

    init {

        keyStore.load(null)

        generateSymmetricKey()

        symmetricKey = loadSymmetricKey()

        cipher = Cipher.getInstance(SYMMETRIC_ALGORITHM)
    }

    private fun generateSymmetricKey() {
        if (!keyStore.containsAlias(SYMMETRIC_KEY_ALIAS)) {

            val keyGenerator =
                KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, KEYSTORE_NAME)
            val builder = KeyGenParameterSpec.Builder(
                SYMMETRIC_KEY_ALIAS,
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            )
                .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                .setRandomizedEncryptionRequired(true)
            keyGenerator.init(builder.build())
            keyGenerator.generateKey()

        }
    }

    private fun loadSymmetricKey(): SecretKey {
        val privateKey = keyStore.getKey(SYMMETRIC_KEY_ALIAS, null) as SecretKey?
        if (privateKey != null){
            return privateKey
        }
        throw IllegalStateException(
            context.getString(R.string.msg_excep_symmetric_key)+ SYMMETRIC_KEY_ALIAS)
    }

    fun encrypt(plainText: String): String {

        cipher.init(Cipher.ENCRYPT_MODE, symmetricKey)

        val encryptedBytes = cipher.doFinal(plainText.toByteArray())
        val encryptedText = Base64.encodeToString(encryptedBytes, Base64.DEFAULT)

        val iv = Base64.encodeToString(cipher.iv, Base64.DEFAULT)

        return "$iv$IV_SEPARATOR$encryptedText"

    }

    fun decrypt(encryptedData: String): String{

        val parts = encryptedData.split(IV_SEPARATOR.toRegex())
        if (parts.size != 2)
            throw java.lang.IllegalStateException(context.getString(R.string.msg_excp_decrypt))

        val iv = parts[0]
        val ivSpec = IvParameterSpec(Base64.decode(iv, Base64.DEFAULT))
        cipher.init(Cipher.DECRYPT_MODE, symmetricKey, ivSpec)

        val encryptedPayload = Base64.decode(parts[1], Base64.DEFAULT)
        val decryptedBytes = cipher.doFinal(encryptedPayload)

        return String(decryptedBytes)
    }
}
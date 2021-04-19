package br.felipefcosta.mobchat.workers

import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import javax.net.ssl.*

object HttpsTrustManager : X509TrustManager {
    private var trustManager = arrayOf<TrustManager>()
    private val acceptedIssuers = arrayOf<X509Certificate>()

    override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {
        throw CertificateException()
    }

    override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {
        throw CertificateException()
    }

    override fun getAcceptedIssuers(): Array<X509Certificate> {
        return acceptedIssuers
    }


    fun isClientTrusted(chain: Array<X509Certificate>): Boolean {
        return true
    }

    fun isServerTrusted(chain: Array<X509Certificate>): Boolean {
        return true
    }

    fun allowSSL() {

        val sslContext = SSLContext.getInstance("SSL")
        sslContext.init(null, trustManager, java.security.SecureRandom())
        HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.socketFactory)

        HttpsURLConnection.setDefaultHostnameVerifier(HostnameVerifier { hostname, session ->
            HttpsURLConnection.getDefaultHostnameVerifier().run {
                verify("10.0.2.2", session)
                verify("mobchat-chatmicroservice.azurewebsites.net", session)
            }
        })
    }


}
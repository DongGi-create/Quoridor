package com.example.quoridor.login

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.example.quoridor.BuildConfig
import com.example.quoridor.R
import com.example.quoridor.SignUpActivity
import com.example.quoridor.communication.Statics
import com.example.quoridor.communication.retrofit.HttpDTO
import com.example.quoridor.communication.retrofit.HttpResult
import com.example.quoridor.communication.retrofit.HttpService
import com.example.quoridor.communication.retrofit.HttpSyncService
import com.example.quoridor.databinding.ActivityKakaoLoginBinding
import com.example.quoridor.util.Func.putAny
import com.example.quoridor.util.Func.putUser
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.URI
import java.net.URL
import java.nio.charset.Charset

class KakaoLoginActivity: AppCompatActivity() {

    val binding by lazy {
        ActivityKakaoLoginBinding.inflate(layoutInflater)
    }

    val TAG by lazy {
        "${applicationContext.getString(R.string.Dirtfy_test_tag)} - KakaoLoginActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.kakaoWebView.apply {
            webViewClient = object: WebViewClient() {
                override fun shouldOverrideUrlLoading(
                    view: WebView?,
                    request: WebResourceRequest?
                ): Boolean {
                    Log.d(TAG, "${request?.requestHeaders}")
                    Log.d(TAG, "${request?.url}")
                    Log.d(TAG, "${request?.isRedirect}")
                    val code = request?.url?.getQueryParameter("code")
                    Log.d(TAG, "$code")
                    if(request?.isRedirect == true && code != null) {
                        HttpSyncService.execute {
                            val data = loginByKakao(code)
                            if ((data?.uid ?: -1) < 0) {
                                val intent = Intent(this@KakaoLoginActivity, SignUpActivity::class.java)
                                intent.putAny("user", data)
                                startActivity(intent)
                            }
                            else {
                                UserManager.setUser(data!!)
                            }
                            this@KakaoLoginActivity.finish()
                        }
                        return true
                    }
                    return false
                }
            }
//            webViewClient = WebViewClient()
            webChromeClient = WebChromeClient()
            loadUrl(
                "https://kauth.kakao.com/oauth/authorize?" +
                        "client_id=${BuildConfig.KAKAO_CLINET_KEY}&" +
                        "redirect_uri=${Statics.HTTP_BASE_URL}/kakao/callback&" +
                        "response_type=code")
        }
        binding.kakaoWebView.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            setSupportMultipleWindows(true)
        }

    }
}

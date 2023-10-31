package com.example.burgers.helpers

import okhttp3.Interceptor
import okhttp3.Response
import java.net.UnknownHostException

class NoInternetInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        throw (UnknownHostException())
    }
}
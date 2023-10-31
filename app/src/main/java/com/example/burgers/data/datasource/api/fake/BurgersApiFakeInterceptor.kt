package com.example.burgers.data.datasource.api.fake

import com.example.burgers.utils.StringFileReader
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody

class BurgersApiFakeInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        //If requested endpoint matched to targeted endpoint, we will return an mocked response.
        if (chain.request().url.toUri().toString().endsWith("burgers")) {
            val responseString = StringFileReader("burgers.json").content
            return chain.proceed(chain.request())
                .newBuilder()
                .code(200)
                .protocol(Protocol.HTTP_2)
                .message(responseString)
                .body(
                    responseString
                        .toByteArray()
                        .toResponseBody(
                            "application/json".toMediaTypeOrNull()
                        )
                )
                .addHeader("content-type", "application/json")
                .build()
        } else {
            return chain.proceed(chain.request())
        }
    }
}
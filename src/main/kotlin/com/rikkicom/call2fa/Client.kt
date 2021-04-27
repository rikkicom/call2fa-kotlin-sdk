package com.rikkicom.call2fa

import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

class Client(login: String, password: String) {
    /**
     * The API version
     */
    private val _version = "v1"

    /**
     * The base URL of the API
     */
    private val _baseURI = "https://api-call2fa.rikkicom.io"

    /**
     * The customer's login
     */
    private var _apiLogin = ""

    /**
     * The customer's password
     */
    private var _apiPassword = ""

    /**
     * The JSON Web Token
     */
    private var _jwt = ""

    /**
     * HTTP-client instance
     */
    private val _client = OkHttpClient()

    /**
     * GSON instance
     */
    private val _gson = Gson()

    init {
        if (login.isEmpty()) {
            throw ClientException("the login parameter is empty")
        }
        if (password.isEmpty()) {
            throw ClientException("the password parameter is empty")
        }

        _apiLogin = login
        _apiPassword = password

        receiveJWT()
    }

    /**
     * Create a full URI to the specified API method
     */
    private fun makeFullURI(method: String): String = "$_baseURI/$_version/$method/"

    /**
     * Receive the JSON Web Token
     */
    private fun receiveJWT() {
        val jsonString = _gson.toJson(Login(_apiLogin, _apiPassword))
        val body = jsonString.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

        val authURI = makeFullURI("auth")
        val request = Request.Builder()
            .url(authURI)
            .post(body)
            .build()

        _client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                throw ClientException("Unexpected code $response")
            }

            val rawJSON = response.body!!.string()
            val json = _gson.fromJson(rawJSON, LoginResponse::class.java)

            _jwt = json.jwt
        }
    }

    /**
     * Initiate a new call (standard IVR answer with one digit)
     */
    fun call(call: NewCall): NewCallResponse {
        if (call.phone_number.isEmpty()) {
            throw ClientException("the phone_number is empty")
        }

        val jsonString = _gson.toJson(call)
        val body = jsonString.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

        val callURI = makeFullURI("call")
        val request = Request.Builder()
            .url(callURI)
            .addHeader("Authorization", "Bearer $_jwt")
            .post(body)
            .build()

        _client.newCall(request).execute().use { response ->
            val code = response.code
            if (code != 201) {
                throw ClientException("Incorrect status code: $code on call step")
            }

            val rawJSON = response.body!!.string()

            return _gson.fromJson(rawJSON, NewCallResponse::class.java)
        }
    }

    /**
     * Initiate a new call via the last digits mode (pool method)
     */
    fun callViaLastDigits(call: NewCallPool): NewCallPoolResponse {
        if (call.phone_number.isEmpty()) {
            throw ClientException("the phone_number is empty")
        }

        if (call.pool_id.isEmpty()) {
            throw ClientException("the pool_id is empty")
        }

        val jsonString = _gson.toJson(call)
        val body = jsonString.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

        val callURI = makeFullURI("pool/${call.pool_id}/call")
        val request = Request.Builder()
            .url(callURI)
            .addHeader("Authorization", "Bearer $_jwt")
            .post(body)
            .build()

        _client.newCall(request).execute().use { response ->
            val code = response.code
            if (code != 201) {
                throw ClientException("Incorrect status code: $code on call step")
            }

            val rawJSON = response.body!!.string()

            return _gson.fromJson(rawJSON, NewCallPoolResponse::class.java)
        }
    }

    /**
     * Initiate a new call with the code
     */
    fun callWithCode(call: NewCodeCall): NewCodeCallResponse {
        if (call.phone_number.isEmpty()) {
            throw ClientException("the phone_number is empty")
        }

        if (call.code.isEmpty()) {
            throw ClientException("the code is empty")
        }

        if (call.lang.isEmpty()) {
            throw ClientException("the lang is empty")
        }

        val jsonString = _gson.toJson(call)
        val body = jsonString.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

        val callURI = makeFullURI("code/call")
        val request = Request.Builder()
            .url(callURI)
            .addHeader("Authorization", "Bearer $_jwt")
            .post(body)
            .build()

        _client.newCall(request).execute().use { response ->
            val code = response.code
            if (code != 201) {
                throw ClientException("Incorrect status code: $code on call step")
            }

            val rawJSON = response.body!!.string()

            return _gson.fromJson(rawJSON, NewCodeCallResponse::class.java)
        }
    }
}

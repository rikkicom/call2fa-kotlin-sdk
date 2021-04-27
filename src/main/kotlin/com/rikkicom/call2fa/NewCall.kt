package com.rikkicom.call2fa

data class NewCall(
    /**
     * The phone number to call to
     */
    val phone_number: String,

    /**
     * The callback URL that the Rikkicom will send the answer to
     */
    val callback_url: String = ""
)

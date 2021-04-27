package com.rikkicom.call2fa

data class NewCodeCall(
    val phone_number: String,
    val code: String,
    val lang: String
)

package com.rikkicom.call2fa

data class NewCallPoolResponse(
    /**
     * Identifier of the call
     */
    val call_id: String,

    /**
     * The number a user will receive call from
     */
    val number: String,

    /**
     * Last four digits of the number a user will receive call from
     */
    val code: String,
)

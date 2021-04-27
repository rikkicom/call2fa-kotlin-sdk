package com.rikkicom.call2fa

data class NewCallPool(
    /**
     * The phone number to call to
     */
    val phone_number: String,

    /**
     * The pool ID of the customer
     */
    val pool_id: String
)

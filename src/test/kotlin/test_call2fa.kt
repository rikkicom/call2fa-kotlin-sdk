import com.rikkicom.call2fa.Client
import com.rikkicom.call2fa.NewCall
import com.rikkicom.call2fa.NewCallPool
import com.rikkicom.call2fa.NewCodeCall

fun main() {
    println("Call2FA testing")

    ivrAnswer()
    callPool()
    dictateCode()

    println("Done")
}

fun ivrAnswer() {
    val apiLogin = "test_ukr_login"
    val apiPassword = "************"

    try {
        val c = Client(apiLogin, apiPassword)

        val newCall = c.call(NewCall("+380631010121"))
        println("Call ID: " + newCall.call_id)

        println("The call was created!")
    } catch (e: Exception) {
        println(e)
        println(e.message)
    }
}

fun callPool() {
    val apiLogin = "test_pool_1"
    val apiPassword = "************"

    try {
        val c = Client(apiLogin, apiPassword)

        val newCallPool = c.callViaLastDigits(NewCallPool("+380631010121", "8"))
        println("Call ID: " + newCallPool.call_id)
        println("Pool number: " + newCallPool.number)
        println("Code: " + newCallPool.code)

    } catch (e: Exception) {
        println(e)
        println(e.message)
    }
}

fun dictateCode() {
    val apiLogin = "test_code_dictate"
    val apiPassword = "************"

    try {
        val c = Client(apiLogin, apiPassword)

        val randCode = "3568w"
        val lang = "uk" // ru or uk
        val newCall = c.callWithCode(NewCodeCall("+380631010121", randCode, lang))
        println("Call ID: " + newCall.call_id)

        println("The call was created!")
    } catch (e: Exception) {
        println(e)
        println(e.message)
    }
}

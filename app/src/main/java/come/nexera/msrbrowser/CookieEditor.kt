package come.nexera.msrbrowser

import android.webkit.CookieManager

object CookieEditor {
    private val cm = CookieManager.getInstance()

    fun setCookie(url: String, name: String, value: String) {
        val cookie = "${'$'}name=${'$'}value; Path=/"
        cm.setCookie(url, cookie)
        cm.flush()
    }

    fun getCookies(url: String): List<String> {
        val cookies = cm.getCookie(url) ?: return listOf()
        return cookies.split(";").map { it.trim() }
    }

    fun removeCookie(url: String, name: String) {
        val expire = "${'$'}name=; Expires=Thu, 01 Jan 1970 00:00:00 GMT; Path=/"
        cm.setCookie(url, expire)
        cm.flush()
    }
}

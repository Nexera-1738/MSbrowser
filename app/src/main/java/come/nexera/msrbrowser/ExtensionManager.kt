package come.nexera.msrbrowser

import android.content.Context
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File

object ExtensionManager {
    private val client = OkHttpClient()

    fun installFromUrl(profile: Profile, url: String, context: Context): Boolean {
        return try {
            val req = Request.Builder().url(url).build()
            client.newCall(req).execute().use { resp ->
                if (!resp.isSuccessful) return false
                val dir = File(context.filesDir, "profiles/${'$'}{profile.id}/extensions")
                if (!dir.exists()) dir.mkdirs()
                val fname = url.substringAfterLast('/').ifEmpty { "ext_${'$'}{System.currentTimeMillis()}.js" }
                val f = File(dir, fname)
                f.writeBytes(resp.body!!.bytes())
                profile.enabledExtensions.add(f.name)
                true
            }
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun injectExtensions(webView: android.webkit.WebView, profile: Profile, context: Context) {
        val dir = File(context.filesDir, "profiles/${'$'}{profile.id}/extensions")
        if (!dir.exists()) return
        dir.listFiles()?.forEach { file ->
            try {
                val js = file.readText()
                webView.post {
                    webView.evaluateJavascript(js, null)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun listExtensions(profile: Profile, context: Context): List<String> {
        val dir = File(context.filesDir, "profiles/${'$'}{profile.id}/extensions")
        if (!dir.exists()) return listOf()
        return dir.listFiles()?.map { it.name } ?: listOf()
    }

    fun removeExtension(profile: Profile, name: String, context: Context): Boolean {
        val dir = File(context.filesDir, "profiles/${'$'}{profile.id}/extensions")
        val f = File(dir, name)
        return if (f.exists()) {
            f.delete()
        } else false
    }
}

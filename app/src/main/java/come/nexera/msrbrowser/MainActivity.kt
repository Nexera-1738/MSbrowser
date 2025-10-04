package come.nexera.msrbrowser

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var profileSpinner: Spinner
    private lateinit var newProfileButton: ImageButton
    private lateinit var urlBar: EditText
    private lateinit var goButton: Button
    private lateinit var desktopToggle: Button
    private lateinit var extensionButton: Button
    private lateinit var cookieButton: Button
    private lateinit var webViewContainer: FrameLayout
    private var webViewWrapper: android.webkit.WebView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        profileSpinner = findViewById(R.id.profileSpinner)
        newProfileButton = findViewById(R.id.newProfileButton)
        urlBar = findViewById(R.id.urlBar)
        goButton = findViewById(R.id.goButton)
        desktopToggle = findViewById(R.id.desktopToggle)
        extensionButton = findViewById(R.id.extensionButton)
        cookieButton = findViewById(R.id.cookieButton)
        webViewContainer = findViewById(R.id.webViewContainer)

        if(ProfileManager.profiles.isEmpty()) {
            ProfileManager.createProfile("Default")
        }
        updateProfileSpinner()

        newProfileButton.setOnClickListener {
            val edit = EditText(this)
            AlertDialog.Builder(this)
                .setTitle("Create Profile")
                .setView(edit)
                .setPositiveButton("Create") { _, _ ->
                    val name = edit.text.toString().ifEmpty { "Profile" + System.currentTimeMillis().toString().takeLast(4) }
                    ProfileManager.createProfile(name)
                    updateProfileSpinner()
                }.setNegativeButton("Cancel", null)
                .show()
        }

        goButton.setOnClickListener { loadUrlFromBar() }

        extensionButton.setOnClickListener {
            startActivity(Intent(this, ExtensionManagerActivity::class.java))
        }

        cookieButton.setOnClickListener {
            startActivity(Intent(this, CookieEditorActivity::class.java))
        }

        desktopToggle.setOnClickListener {
            val p = ProfileManager.activeProfile ?: return@setOnClickListener
            p.desktopMode = !p.desktopMode
            Toast.makeText(this, if(p.desktopMode) "Desktop mode ON" else "Desktop mode OFF", Toast.LENGTH_SHORT).show()
            reloadWebView()
        }

        reloadWebView()
    }

    private fun reloadWebView() {
        val p = ProfileManager.activeProfile ?: return
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            try {
                android.webkit.WebView.setDataDirectorySuffix("profile_" + p.id)
            } catch (e: Exception) { }
        }
        webViewContainer.removeAllViews()
        val webView = WebViewHelper.createWebView(this, p)
        webViewWrapper = webView
        webViewContainer.addView(webView)
        ExtensionManager.injectExtensions(webView, p, this)
        webView.loadUrl("https://www.bing.com")
    }

    private fun loadUrlFromBar() {
        val input = urlBar.text.toString()
        val url = if (input.startsWith("http")) input else "https://www.bing.com/search?q=" + input.replace(" ", "+")
        if (webViewWrapper == null) reloadWebView()
        webViewWrapper?.loadUrl(url)
    }

    private fun updateProfileSpinner() {
        val names = ProfileManager.profiles.map { it.name }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, names)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        profileSpinner.adapter = adapter
        profileSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selected = ProfileManager.profiles[position]
                ProfileManager.switchProfile(selected.id)
                reloadWebView()
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
        ProfileManager.activeProfile?.let {
            val idx = ProfileManager.profiles.indexOf(it)
            if (idx >= 0) profileSpinner.setSelection(idx)
        }
    }
}

package come.nexera.msrbrowser

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class CookieEditorActivity : AppCompatActivity() {
    private lateinit var listView: ListView
    private lateinit var addButton: Button
    private var profile: Profile? = null
    private var url = "https://www.bing.com"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.cookie_editor)

        profile = ProfileManager.activeProfile
        if (profile == null) {
            Toast.makeText(this, "No active profile", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        listView = findViewById(R.id.cookieListView)
        addButton = findViewById(R.id.addCookieButton)

        addButton.setOnClickListener {
            val nameInput = EditText(this)
            nameInput.hint = "name=value"
            AlertDialog.Builder(this)
                .setTitle("Add Cookie")
                .setView(nameInput)
                .setPositiveButton("Add") { _, _ ->
                    val nv = nameInput.text.toString()
                    if (nv.contains("=")) {
                        val parts = nv.split("=", limit = 2)
                        CookieEditor.setCookie(url, parts[0], parts[1])
                        refreshList()
                    } else {
                        Toast.makeText(this, "Enter in form name=value", Toast.LENGTH_SHORT).show()
                    }
                }.setNegativeButton("Cancel", null)
                .show()
        }

        listView.setOnItemLongClickListener { parent, view, position, id ->
            val item = parent.getItemAtPosition(position) as String
            val name = item.substringBefore('=').trim()
            AlertDialog.Builder(this)
                .setTitle("Delete cookie")
                .setMessage("Delete cookie: $item ?")
                .setPositiveButton("Delete") { _, _ ->
                    CookieEditor.removeCookie(url, name)
                    refreshList()
                }
                .setNegativeButton("Cancel", null)
                .show()
            true
        }

        refreshList()
    }

    private fun refreshList() {
        val cookies = CookieEditor.getCookies(url)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, cookies)
        listView.adapter = adapter
    }
}

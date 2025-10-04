package come.nexera.msrbrowser

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class ExtensionManagerActivity : AppCompatActivity() {
    private lateinit var listView: ListView
    private lateinit var addButton: Button
    private var profile: Profile? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.extension_manager)

        profile = ProfileManager.activeProfile
        if (profile == null) {
            Toast.makeText(this, "No active profile", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        listView = findViewById(R.id.extensionListView)
        addButton = findViewById(R.id.addExtensionButton)

        addButton.setOnClickListener {
            val input = EditText(this)
            AlertDialog.Builder(this)
                .setTitle("Extension URL")
                .setView(input)
                .setPositiveButton("Download") { _, _ ->
                    val url = input.text.toString()
                    if (url.isNotEmpty()) {
                        val ok = ExtensionManager.installFromUrl(profile!!, url, this)
                        if (ok) Toast.makeText(this, "Installed", Toast.LENGTH_SHORT).show()
                        else Toast.makeText(this, "Failed to download", Toast.LENGTH_SHORT).show()
                        refreshList()
                    }
                }.setNegativeButton("Cancel", null)
                .show()
        }

        listView.setOnItemLongClickListener { parent, view, position, id ->
            val name = parent.getItemAtPosition(position) as String
            AlertDialog.Builder(this)
                .setTitle("Remove?")
                .setMessage("Remove extension: $name ?")
                .setPositiveButton("Yes") { _, _ ->
                    ExtensionManager.removeExtension(profile!!, name, this)
                    refreshList()
                }
                .setNegativeButton("No", null)
                .show()
            true
        }

        refreshList()
    }

    private fun refreshList() {
        val items = ExtensionManager.listExtensions(profile!!, this)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, items)
        listView.adapter = adapter
    }
}

package come.nexera.msrbrowser

import android.os.Bundle
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity

class ProfileSettingsActivity : AppCompatActivity() {
    private var profile: Profile? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile_settings)

        profile = ProfileManager.activeProfile
        if (profile == null) {
            finish()
            return
        }

        val tzSwitch = findViewById<Switch>(R.id.timezoneSwitch)
        val geoSwitch = findViewById<Switch>(R.id.geolocationSwitch)
        val proxySwitch = findViewById<Switch>(R.id.proxySwitch)
        val webrtcSwitch = findViewById<Switch>(R.id.webrtcSwitch)

        tzSwitch.isChecked = profile!!.timezone != null
        geoSwitch.isChecked = profile!!.geolocation != null
        proxySwitch.isChecked = profile!!.proxy != null
        webrtcSwitch.isChecked = profile!!.webrtcEnabled

        tzSwitch.setOnCheckedChangeListener { _, isChecked ->
            profile!!.timezone = if (isChecked) "UTC" else null
        }
        geoSwitch.setOnCheckedChangeListener { _, isChecked ->
            profile!!.geolocation = if (isChecked) Pair(37.7749, -122.4194) else null
        }
        proxySwitch.setOnCheckedChangeListener { _, isChecked ->
            profile!!.proxy = if (isChecked) "http://127.0.0.1:8080" else null
        }
        webrtcSwitch.setOnCheckedChangeListener { _, isChecked ->
            profile!!.webrtcEnabled = isChecked
        }
    }
}

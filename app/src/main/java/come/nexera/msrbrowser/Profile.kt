package come.nexera.msrbrowser

data class Profile(
    val id: String,
    val name: String,
    var desktopMode: Boolean = false,
    var timezone: String? = null,
    var geolocation: Pair<Double, Double>? = null,
    var proxy: String? = null,
    var webrtcEnabled: Boolean = true,
    var enabledExtensions: MutableList<String> = mutableListOf(),
    var fingerprint: Fingerprint = Fingerprint()
)

data class Fingerprint(
    var userAgent: String = "",
    var language: String = "",
    var viewport: Pair<Int, Int> = Pair(1080, 1920)
)

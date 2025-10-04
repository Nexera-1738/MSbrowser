package come.nexera.msrbrowser

object ProfileManager {
    val profiles = mutableListOf<Profile>()
    var activeProfile: Profile? = null

    fun createProfile(name: String): Profile {
        val p = Profile(id = System.currentTimeMillis().toString(), name = name)
        generateFingerprint(p)
        profiles.add(p)
        activeProfile = p
        return p
    }

    fun switchProfile(profileId: String) {
        activeProfile = profiles.find { it.id == profileId }
    }

    private fun generateFingerprint(p: Profile) {
        p.fingerprint.userAgent = "Mozilla/5.0 (Android) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/117.0 Mobile Safari/537.36"
        p.fingerprint.language = "en-US"
        p.fingerprint.viewport = Pair(1080, 1920)
    }
}

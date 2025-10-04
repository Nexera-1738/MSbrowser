package come.nexera.msrbrowser

import java.util.*

object FingerprintHelper {
    fun makeSimpleFingerprint(seed: String): Fingerprint {
        val rnd = Random(seed.hashCode().toLong())
        val widths = listOf(360, 375, 412, 768, 1080, 1280)
        val heights = listOf(640, 800, 812, 1024, 1920, 2340)
        val w = widths[rnd.nextInt(widths.size)]
        val h = heights[rnd.nextInt(heights.size)]
        val ua = "Mozilla/5.0 (Android; Mobile; rv:${60 + rnd.nextInt(10)}.0) Gecko/20100101 Firefox/${60 + rnd.nextInt(10)}.0"
        return Fingerprint(userAgent = ua, language = "en-US", viewport = Pair(w, h))
    }
}

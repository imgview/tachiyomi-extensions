package eu.kanade.tachiyomi.extension.en.resetscans

import eu.kanade.tachiyomi.multisrc.madara.Madara
import eu.kanade.tachiyomi.network.interceptor.rateLimit
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

class ResetScans : Madara("Reset Scans", "https://reset-scans.us", "en") {

    override val client: OkHttpClient = super.client.newBuilder()
        .rateLimit(20, 5, TimeUnit.SECONDS)
        .build()

    override val mangaDetailsSelectorDescription = ".description-summary > .summary__content"

    override val chapterUrlSelector = ".li__text a"
}

package eu.kanade.tachiyomi.extension.en.rizzcomics

import eu.kanade.tachiyomi.multisrc.mangathemesia.MangaThemesia
import eu.kanade.tachiyomi.network.interceptor.rateLimit
import okhttp3.OkHttpClient
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.TimeUnit

class RizzComics : MangaThemesia(
    "Rizz Comics",
    "https://rizzcomic.com",
    "en",
    "/series",
    SimpleDateFormat("dd MMM yyyy", Locale.US),
) {

    override val client: OkHttpClient = super.client.newBuilder()
        .rateLimit(20, 5, TimeUnit.SECONDS)
        .build()
}

package eu.kanade.tachiyomi.extension.en.asurascansx

import eu.kanade.tachiyomi.multisrc.wpmangastream.WPMangaStream
import java.util.concurrent.TimeUnit
import okhttp3.Headers
import okhttp3.OkHttpClient

class AsuraScansX : WPMangaStream("Asura Scans X", "https://www.asurascans.com", "en") {

    override val client: OkHttpClient = network.cloudflareClient.newBuilder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    override fun headersBuilder(): Headers.Builder = Headers.Builder()
        .add("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.212 Safari/537.36 Edg/90.0.818.62")
        .add("Referer", "https://www.google.com")

    override val pageSelector = "div.rdminimal img[loading*=lazy]"
}

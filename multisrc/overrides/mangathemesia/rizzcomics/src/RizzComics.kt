package eu.kanade.tachiyomi.extension.en.rizzcomics

import eu.kanade.tachiyomi.multisrc.mangathemesia.MangaThemesia
import java.text.SimpleDateFormat
import java.util.Locale

class RizzComics : MangaThemesia(
    "Rizz Comics",
    "https://rizzcomic.com",
    "en", 
    "/series",
    SimpleDateFormat("dd MMM yyyy", Locale.US)
)

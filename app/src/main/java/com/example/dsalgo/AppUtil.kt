package com.example.dsalgo

import android.content.Context
import android.widget.Toast
import android.content.SharedPreferences

object AppUtil {

    fun showToast(context: Context,message : String){
        Toast.makeText(context,message,Toast.LENGTH_LONG).show()
    }


    fun readJsonFromraw(context: Context , resId : Int ): String{
        return context.resources.openRawResource(resId).bufferedReader().use { it.readText() }
    }

}

object HighScoreManager {
    private const val PREFS_NAME = "high_scores_prefs"
    private const val KEY_PREFIX = "score_"

    private lateinit var prefs: SharedPreferences

    // Initialize with context (call once in your app, e.g., MainActivity)
    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun getHighScore(subject: String): Int {
        return prefs.getInt(KEY_PREFIX + subject, 0)
    }

    fun updateHighScore(subject: String, newScore: Int) {
        val currentHigh = getHighScore(subject)
        if (newScore > currentHigh) {
            prefs.edit().putInt(KEY_PREFIX + subject, newScore).apply()
        }
    }

    fun getAllHighScores(): Map<String, Int> {
        return prefs.all.filterKeys { it.startsWith(KEY_PREFIX) }
            .mapKeys { it.key.removePrefix(KEY_PREFIX) }
            .mapValues { it.value as Int }
    }
}



object  Constants{
    val ApiKey ="AIzaSyDTsy7aCFykbNBFPDfMLdIWpYdWS_QjptI"
}

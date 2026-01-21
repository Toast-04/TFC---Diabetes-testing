package com.example.myapplication

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("settings")

class SettingsManager (context: Context) {
    private val dataStore = context.dataStore

    companion object {
        val FACTOR_KEY = stringPreferencesKey("factor_sensibilidad")
        val DARK_MODE_KEY = booleanPreferencesKey("dark_mode")
        val DISLEXIA_KEY = booleanPreferencesKey("modo_dislexia")
        val FONT_SIZE_KEY = stringPreferencesKey("font_size")
        val RATIO_MANANA = stringPreferencesKey("ratio_manana")
        val RATIO_MEDIODIA = stringPreferencesKey("ratio_mediodia")

        val RATIO_NOCHE = stringPreferencesKey("ratio_noche")
    }

    //Lectura de flujos
    val factorFlow: Flow<String?> = dataStore.data.map { it[FACTOR_KEY] ?: ""}
    val darkModeFlow: Flow<Boolean> = dataStore.data.map { it[DARK_MODE_KEY] ?: false }
    val dislexiaModeFlow: Flow<Boolean> = dataStore.data.map { it[DISLEXIA_KEY] ?: false }
    val fontSizeFlow: Flow <String> = dataStore.data.map {it [FONT_SIZE_KEY] ?: "Normal"}
    val ratioMananaFlow: Flow<String?> = dataStore.data.map { it[RATIO_MANANA] ?: ""}
    val ratioMediodiaFlow: Flow<String?> = dataStore.data.map { it[RATIO_MEDIODIA] ?: ""}
    val ratioNocheFlow: Flow<String?> = dataStore.data.map { it[RATIO_NOCHE] ?: ""}

    //Funciones para guardar
    suspend fun saveFactor(value: String) {dataStore.edit{it [FACTOR_KEY] = value}}
    suspend fun saveDarkMode(enabled: Boolean) { dataStore.edit { it[DARK_MODE_KEY] = enabled } }
    suspend fun saveDislexia(enabled: Boolean) { dataStore.edit { it[DISLEXIA_KEY] = enabled } }
    suspend fun saveFontSize(size: String) {dataStore.edit{it [FONT_SIZE_KEY] = size}}
    suspend fun saveRatioManana(value: String) {dataStore.edit{it [RATIO_MANANA] = value}}
    suspend fun saveRatioMediodia(value: String) {dataStore.edit{it [RATIO_MEDIODIA] = value}}
    suspend fun saveRatioNoche(value: String) {dataStore.edit{it [RATIO_NOCHE] = value}}
}
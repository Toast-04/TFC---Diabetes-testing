package com.example.myapplication
import DbHelper
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MainViewModelFactory(
    private val dbHelper: DbHelper,
    private val settingsManager: SettingsManager
    ) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(dbHelper, settingsManager) as T
    }
}
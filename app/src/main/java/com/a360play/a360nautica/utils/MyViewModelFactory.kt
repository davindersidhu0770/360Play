package com.app.starterkit.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.a360play.a360nautica.viewmodels.BookingViewModel
import com.a360play.a360nautica.viewmodels.CardViewModel
import com.a360play.a360nautica.viewmodels.EntryViewModel
import com.a360play.a360nautica.viewmodels.LoginViewModel
import com.app.starterkit.network.Repository

class MyViewModelFactory(val repository: Repository):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(BookingViewModel::class.java)) {
            return BookingViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(EntryViewModel::class.java)) {
            return EntryViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(CardViewModel::class.java)) {
            return CardViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
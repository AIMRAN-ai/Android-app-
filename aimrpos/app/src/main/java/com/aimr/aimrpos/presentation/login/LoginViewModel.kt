package com.aimr.aimrpos.presentation.login

import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {
    fun verifyPin(pin: String): Boolean {
        return pin.length == 4
    }
}
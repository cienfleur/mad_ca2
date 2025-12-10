package ie.setu.mad_ca2.views.auth

import android.util.Patterns
import ie.setu.mad_ca2.main.MainApp

class LoginPresenter(val view: LoginView) {

    var app: MainApp = view.application as MainApp

    private var isLogin = true

    init {
        view.showLoginState()
    }

    fun doToggleAuthMode() {
        isLogin = !isLogin
        if (isLogin) {
            view.showLoginState()
        } else {
            view.showRegisterState()
        }
    }

    fun doClickAuthAction(email: String, pass: String) {

        if (email.isEmpty()) {
            view.showSnackBar("Email cannot be empty")
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            view.showSnackBar("Please enter a valid email address")
            return
        }


        if (pass.isEmpty()) {
            view.showSnackBar("Password cannot be empty")
            return
        }
        if (pass.length < 6) {
            view.showSnackBar("Password must be at least 6 characters")
            return
        }

        if (isLogin) {
            doLogin(email, pass)
        } else {
            doRegister(email, pass)
        }
    }


    private fun doLogin(email: String, pass: String) {
        // TODO: Add Firebase Auth logic here later
        view.showSnackBar("Logging in $email...")
    }

    private fun doRegister(email: String, pass: String) {
        // TODO: Add Firebase Auth logic here later
        view.showSnackBar("Registering $email...")
    }
}

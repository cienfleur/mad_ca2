package ie.setu.mad_ca2.views.auth

import ie.setu.mad_ca2.main.MainApp

class LoginPresenter(val view: LoginView) {

    var app: MainApp = view.application as MainApp

    // State is now owned by the Presenter
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
        if (email.isEmpty() || pass.isEmpty()) {
            view.showSnackBar("Please enter email and password")
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

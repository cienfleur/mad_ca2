package ie.setu.mad_ca2.views.auth

import android.util.Patterns
import ie.setu.mad_ca2.main.MainApp
import android.app.Activity
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import ie.setu.mad_ca2.views.companylist.CompanyListView

class LoginPresenter(val view: LoginView) {

    var app: MainApp = view.application as MainApp

    private var isLogin = true

    private lateinit var loginIntentLauncher: ActivityResultLauncher<Intent>



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
        view.showSnackBar("Logging in $email...")
        app.auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(view) { task ->
            if (task.isSuccessful) {
                view.showSnackBar("Login Successful")
                // Navigate to the main activity and load data
                val launcherIntent = Intent(view, CompanyListView::class.java)
                view.startActivity(launcherIntent)
            } else {
                view.showSnackBar("Login Failed: ${task.exception?.message}")
            }
        }
    }

    private fun doRegister(email: String, pass: String) {
        view.showSnackBar("Registering $email...")
        app.auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(view) { task ->
            if (task.isSuccessful) {
                view.showSnackBar("Registration Successful")
                // return to login state
                doToggleAuthMode()
            } else {
                view.showSnackBar("Registration Failed: ${task.exception?.message}")
            }
        }
    }
}

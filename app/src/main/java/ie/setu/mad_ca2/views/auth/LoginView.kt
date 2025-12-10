package ie.setu.mad_ca2.views.auth

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.android.material.snackbar.Snackbar
import ie.setu.mad_ca2.databinding.ActivityAuthBinding
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen

class LoginView : AppCompatActivity() {

    private lateinit var binding: ActivityAuthBinding
    private lateinit var presenter: LoginPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        presenter = LoginPresenter(this)

        // Forward click events to Presenter
        binding.btnAuthAction.setOnClickListener {
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()
            presenter.doClickAuthAction(email, password)
        }

        binding.txtToggleMode.setOnClickListener {
            presenter.doToggleAuthMode()
        }
    }

    // --- Methods called by the Presenter to update UI ---

    fun showLoginState() {
        binding.authTitle.text = "Login"
        binding.btnAuthAction.text = "Login"
        binding.txtToggleMode.text = "No account? Register here."
    }

    fun showRegisterState() {
        binding.authTitle.text = "Register"
        binding.btnAuthAction.text = "Register"
        binding.txtToggleMode.text = "Already have an account? Login."
    }

    fun showSnackBar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
    }
}

package com.example.like_app.ui.users

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.like_app.MainActivity
import com.example.like_app.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Firebase Auth instance
        auth = FirebaseAuth.getInstance()

        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // UI elements and listeners
        val etUser: EditText = findViewById(R.id.etUser)
        val etPassword: EditText = findViewById(R.id.etPassword)
        val btnLogin: Button = findViewById(R.id.btnLogin)
        val btnRegisterEmail = findViewById<Button>(R.id.btnRegisterEmail)
        val tvRegisterPrompt = findViewById<TextView>(R.id.tvRegisterPrompt)

        btnLogin.setOnClickListener {
            val correo = etUser.text.toString()
            val clave = etPassword.text.toString()

            auth.signInWithEmailAndPassword(correo, clave)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Snackbar.make(findViewById(android.R.id.content), "Inicio de sesión exitoso", Snackbar.LENGTH_LONG).show()
                        startActivity(Intent(this, MainActivity::class.java))
                    } else {
                        Snackbar.make(findViewById(android.R.id.content), "Credenciales inválidas", Snackbar.LENGTH_LONG).show()
                    }
                }
        }

        btnRegisterEmail.setOnClickListener {
            registerWithGmail()
        }

        tvRegisterPrompt.setOnClickListener {
            handleRegisterClick()
        }
    }

    private fun registerWithGmail() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                Toast.makeText(this, "Google sign in failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    startActivity(Intent(this, MainActivity::class.java))
                } else {
                    Snackbar.make(findViewById(android.R.id.content), "Authentication Failed.", Snackbar.LENGTH_SHORT).show()
                }
            }
    }

    private fun handleRegisterClick() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Elija el tipo de cuenta que desea crear")

        builder.setPositiveButton("Cliente") { _, _ ->
            navigateToRegisterForm()
        }
        builder.setNegativeButton("Empresa") { _, _ ->
            navigateToBusinessRegistration()
        }

        builder.show()
    }

    private fun navigateToRegisterForm() {
        val intent = Intent(this, RegisterClientActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToBusinessRegistration() {
        val intent = Intent(this, RegisterRestActivity::class.java)
        startActivity(intent)
    }

    companion object {
        private const val RC_SIGN_IN = 9001
    }
}

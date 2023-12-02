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
import com.example.like_app.RestauranteActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var db: FirebaseFirestore

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
        // Existing code for email/password login
        val etUser: EditText = findViewById(R.id.etUser)
        val etPassword: EditText = findViewById(R.id.etPassword)
        val btnLogin: Button = findViewById(R.id.btnLogin)

        btnLogin.setOnClickListener {
            val correo = etUser.text.toString()
            val clave = etPassword.text.toString()


            auth.signInWithEmailAndPassword(correo, clave)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val user = FirebaseAuth.getInstance().currentUser

                            //checkUserRole(user!!.uid)
                        Snackbar.make(findViewById(android.R.id.content), "Inicio de sesión exitoso", Snackbar.LENGTH_LONG).show()
                        startActivity(Intent(this, MainActivity::class.java))
                    } else {
                        Snackbar.make(findViewById(android.R.id.content), "Credenciales inválidas", Snackbar.LENGTH_LONG).show()
                    }
                }
        }

        // Button for Gmail registration
        val btnRegisterEmail = findViewById<Button>(R.id.btnRegisterEmail)
        btnRegisterEmail.setOnClickListener {
            registerWithGmail()
        }

        // Other UI elements and listeners
        val tvRegisterPrompt = findViewById<TextView>(R.id.tvRegisterPrompt)
        /*val btnRegisterForm = findViewById<Button>(R.id.btnRegisterForm)
        val btnRegisterNegocio = findViewById<Button>(R.id.btnRegisterNegocio)


        btnRegisterForm.setOnClickListener {
            navigateToRegisterForm()
        }

        btnRegisterNegocio.setOnClickListener {
            navigateToBusinessRegistration()
        }*/

        tvRegisterPrompt.setOnClickListener {
            handleRegisterClick()
        }
    }
    private fun showRegisterOptions() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_register_options, null)
        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()
        /*val options = arrayOf("Registrar como Cliente", "Registrar como Negocio")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Elige una opción")
            .setItems(options) { dialog, which ->
                when (which) {
                    0 -> startActivity(Intent(this, RegisterClientActivity::class.java))
                    1 -> startActivity(Intent(this, RegisterRestActivity::class.java))
                }
            }
        builder.show()*/
        dialogView.findViewById<Button>(R.id.btnCliente).setOnClickListener {
            startActivity(Intent(this, RegisterClientActivity::class.java))
            dialog.dismiss()
        }

        dialogView.findViewById<Button>(R.id.btnNegocio).setOnClickListener {
            startActivity(Intent(this, RegisterRestActivity::class.java))
            dialog.dismiss()
        }

        dialog.show()
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
                    // Sign in success
                    startActivity(Intent(this, MainActivity::class.java))
                } else {
                    // If sign in fails, display a message to the user.
                    Snackbar.make(findViewById(android.R.id.content), "Authentication Failed.", Snackbar.LENGTH_SHORT).show()
                }
            }
    }

    private fun navigateToRegisterForm() {
        // Navegar a la pantalla de registro
        Toast.makeText(this, "Navegando al formulario de registro", Toast.LENGTH_SHORT).show()
        val intentclient = Intent(this, RegisterClientActivity::class.java)
        startActivity(intentclient)
    }

    private fun navigateToBusinessRegistration() {
        val intentrest = Intent(this, RegisterRestActivity::class.java)
        startActivity(intentrest)
        Toast.makeText(this, "Navegando al registro de negocio", Toast.LENGTH_SHORT).show()
    }

    private fun handleRegisterClick() {
        // Lógica cuando se hace clic en "¿No tienes cuenta? Regístrate"
        showRegisterOptions()
        Toast.makeText(this, "Manejando clic en registro", Toast.LENGTH_SHORT).show()
    }

    private fun checkUserRole(userId: String) {
        // Verifica si el usuario es un administrador
        db.collection("bussiness").document(userId)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val documentSnapshot = task.result

                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        // El documento existe, lo que indica que el usuario es un administrador
                        //startActivity(Intent(this, RestauranteActivity::class.java))
                        Toast.makeText(this, "Usuario existe", Toast.LENGTH_SHORT).show()
                    } else {
                        // El documento no existe, verifica si es un usuario normal
                        //checkUser(userId)
                    }
                } else {
                    // Maneja el error de Firestore si es necesario
                    Toast.makeText(this, "Error al verificar el rol", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun checkUser(userId: String) {
        // Verifica si el usuario es un usuario normal
        db.collection("users").document(userId)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    if (task.result!!.exists()) {
                        // Usuario es un usuario normal
                        //startActivity(Intent(this, MainActivity::class.java))
                        Toast.makeText(this, "Si encontrousuario normal", Toast.LENGTH_SHORT).show()
                    } else {
                        // El usuario no está en ninguna colección conocida, manejar según sea necesario
                        Toast.makeText(this, "Usuario no reconocido", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // Maneja el error de Firestore si es necesario
                    Toast.makeText(this, "Error al verificar el rol", Toast.LENGTH_SHORT).show()
                }
            }
    }

    companion object {
        private const val RC_SIGN_IN = 9001
    }
}

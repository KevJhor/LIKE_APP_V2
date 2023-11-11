package com.example.like_app.ui.users

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.like_app.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {

    // Declaración de las vistas
    private lateinit var editTextFirstName: EditText
    private lateinit var editTextLastName: EditText
    private lateinit var editTextEmail: EditText
    private lateinit var editTextPhone: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var editTextConfirmPassword: EditText
    private lateinit var editTextAddress: EditText
    private lateinit var buttonRegister: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Inicialización de las vistas
        editTextFirstName = findViewById(R.id.etClientName)
        editTextLastName = findViewById(R.id.etClientSurname)
        editTextEmail = findViewById(R.id.etClientEmail)
        editTextPhone = findViewById(R.id.etClientPhone)
        editTextPassword = findViewById(R.id.etClientPassword)
        editTextConfirmPassword = findViewById(R.id.etConfirmPassword)
        editTextAddress = findViewById(R.id.etAddress)
        buttonRegister = findViewById(R.id.btnClientRegister)

        // Establecer listener para el botón de registro
        buttonRegister.setOnClickListener {
            registerUser()
        }
    }

    private fun registerUser() {
        // Obtener los valores ingresados por el usuario
        val firstName = editTextFirstName.text.toString().trim()
        val lastName = editTextLastName.text.toString().trim()
        val email = editTextEmail.text.toString().trim()
        val phone = editTextPhone.text.toString().trim()
        val password = editTextPassword.text.toString().trim()
        val confirmPassword = editTextConfirmPassword.text.toString().trim()
        val address = editTextAddress.text.toString().trim()

        // Validar los campos
        if (firstName.isEmpty()) {
            editTextFirstName.error = "El nombre es obligatorio"
            return
        }
        if (!firstName.matches(Regex("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$"))) {
            editTextFirstName.error = "El nombre solo debe contener letras"
            return
        }

        if (lastName.isEmpty()) {
            editTextLastName.error = "El apellido es obligatorio"
            return
        }
        if (!lastName.matches(Regex("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$"))) {
            editTextLastName.error = "El apellido solo debe contener letras"
            return
        }

        if (email.isEmpty()) {
            editTextEmail.error = "El correo electrónico es obligatorio"
            return
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.error = "Introduce un correo electrónico válido"
            return
        }

        if (phone.isEmpty()) {
            editTextPhone.error = "El teléfono es obligatorio"
            return
        }
        if (!phone.matches(Regex("\\d+"))) {
            editTextPhone.error = "El teléfono solo debe contener números"
            return
        }

        if (password.isEmpty()) {
            editTextPassword.error = "La contraseña es obligatoria"
            return
        }
        if (password.length < 6) {
            editTextPassword.error = "La contraseña debe tener al menos 6 caracteres"
            return
        }

        if (confirmPassword.isEmpty()) {
            editTextConfirmPassword.error = "Confirma tu contraseña"
            return
        }
        if (password != confirmPassword) {
            editTextConfirmPassword.error = "Las contraseñas no coinciden"
            return
        }

        if (address.isEmpty()) {
            editTextAddress.error = "La dirección es obligatoria"
            return
        }

        // Aquí puedes agregar la lógica para registrar al usuario
        val auth = FirebaseAuth.getInstance()

        // Registrar al usuario con correo electrónico y contraseña en Firebase Authentication
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Usuario registrado con éxito en Firebase Authentication
                    val userId = auth.currentUser?.uid

                    // Guardar información adicional en Cloud Firestore
                    val db = FirebaseFirestore.getInstance()
                    val userMap = hashMapOf(
                        "firstName" to firstName,
                        "lastName" to lastName,
                        "email" to email,
                        "phone" to phone,
                        "address" to address
                    )

                    userId?.let {
                        db.collection("users").document(it).set(userMap)
                            .addOnSuccessListener {
                                Toast.makeText(
                                    this,
                                    "Usuario registrado con éxito",
                                    Toast.LENGTH_SHORT
                                ).show()

                                // Navegar a MainActivity o a otra actividad después del registro exitoso
                                val intent = Intent(this, MainActivity::class.java)
                                startActivity(intent)
                                finish() // Finaliza esta actividad
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(
                                    this,
                                    "Error al guardar información del usuario: ${e.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                    }
                } else {
                    // Error al registrar al usuario
                    Toast.makeText(
                        this,
                        "Error al registrar al usuario: ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
}

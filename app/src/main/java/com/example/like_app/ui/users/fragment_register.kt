package com.example.like_app.ui.users

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.like_app.R
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {

    // Declaración de las vistas
    private lateinit var editTextFirstName: EditText
    private lateinit var editTextLastName: EditText
    private lateinit var editTextEmail: EditText
    private lateinit var editTextPhone: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var editTextConfirmPassword: EditText
    private lateinit var editTextBrandName: EditText
    private lateinit var editTextRUC: EditText
    private lateinit var buttonRegister: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_register)

        // Inicialización de las vistas
        editTextFirstName = findViewById(R.id.editTextFirstName)
        editTextLastName = findViewById(R.id.editTextLastName)
        editTextEmail = findViewById(R.id.editTextEmail)
        editTextPhone = findViewById(R.id.editTextPhone)
        editTextPassword = findViewById(R.id.editTextPassword)
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword)
        editTextBrandName = findViewById(R.id.editTextBrandName)
        editTextRUC = findViewById(R.id.editTextRUC)
        buttonRegister = findViewById(R.id.buttonRegister)

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
            val brandName = editTextBrandName.text.toString().trim()
            val ru = editTextRUC.text.toString().trim()

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

        if (!email.contains("@")) {
            editTextEmail.error = "Introduce un correo electrónico válido"
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

            if (brandName.isEmpty()) {
                editTextBrandName.error = "El nombre de la marca es obligatorio"
                return
            }

            if (ru.isEmpty()) {
                editTextRUC.error = "El número de RUC es obligatorio"
                return
            }
        if (ru.length != 11) {
            editTextRUC.error = "El RUC debe tener 11 dígitos"
            return
        }

            // Aquí puedes agregar la lógica para registrar al usuario, por ejemplo, guardar los datos en una base de datos o enviarlos a un servidor
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
                        "brandName" to brandName,
                        "ruc" to ru
                    )

                    userId?.let {
                        db.collection("users").document(it).set(userMap)
                            .addOnSuccessListener {
                                Toast.makeText(this, "Usuario registrado con éxito", Toast.LENGTH_SHORT).show()
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(this, "Error al guardar información del usuario: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                    }
                } else {
                    // Error al registrar al usuario
                    Toast.makeText(this, "Error al registrar al usuario: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }



    Toast.makeText(this, "Usuario registrado con éxito", Toast.LENGTH_SHORT).show()
        }



    }

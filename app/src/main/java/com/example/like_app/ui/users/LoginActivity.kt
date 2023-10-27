package com.example.like_app.ui.users

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.like_app.R

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)



        // Inicializar los botones y el TextView
        val btnRegisterForm = findViewById<Button>(R.id.btnRegisterForm)
        val btnRegisterEmail = findViewById<Button>(R.id.btnRegisterEmail)
        val btnRegisterNegocio = findViewById<Button>(R.id.btnRegisterNegocio)
        val tvRegisterPrompt = findViewById<TextView>(R.id.tvRegisterPrompt)

        // Establecer listeners para los botones y el TextView
        btnRegisterForm.setOnClickListener {
            // Lógica para el botón "Registrate"
            navigateToRegisterForm()
        }

        btnRegisterEmail.setOnClickListener {
            // Lógica para el botón "Registrate con Gmail"
            registerWithGmail()
        }

        btnRegisterNegocio.setOnClickListener {
            // Lógica para el botón "Registra tu Negocio"
            navigateToBusinessRegistration()
        }

        tvRegisterPrompt.setOnClickListener {
            // Lógica para el TextView "¿No tienes cuenta? Regístrate"
            handleRegisterClick()
        }
    }

    private fun navigateToRegisterForm() {
        // Navegar a la pantalla de registro
        Toast.makeText(this, "Navegando al formulario de registro", Toast.LENGTH_SHORT).show()
    }

    private fun registerWithGmail() {
        // Lógica para registrar con Gmail
        Toast.makeText(this, "Registrando con Gmail", Toast.LENGTH_SHORT).show()
    }

    private fun navigateToBusinessRegistration() {
        // Navegar a la pantalla de registro de negocio
        Toast.makeText(this, "Navegando al registro de negocio", Toast.LENGTH_SHORT).show()
    }

    private fun handleRegisterClick() {
        // Lógica cuando se hace clic en "¿No tienes cuenta? Regístrate"
        Toast.makeText(this, "Manejando clic en registro", Toast.LENGTH_SHORT).show()
    }
}

package com.epsum.probandosqlite

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class RegistroActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        val dbHelper = DatabaseHelper(this)

        val editNombre = findViewById<EditText>(R.id.editNombre)
        val editApellidos = findViewById<EditText>(R.id.editApellidos)
        val editCorreo = findViewById<EditText>(R.id.editCorreo)
        val editContrasena = findViewById<EditText>(R.id.editContrasena)
        val editTipoUsuario = findViewById<EditText>(R.id.editTipoUsuario)
        val editTelefono = findViewById<EditText>(R.id.editTelefono)
        val btnRegistrar = findViewById<Button>(R.id.btnRegistrar)

        btnRegistrar.setOnClickListener {
            val nombre = editNombre.text.toString().trim()
            val apellidos = editApellidos.text.toString().trim()
            val correo = editCorreo.text.toString().trim()
            val contrasena = editContrasena.text.toString().trim()
            val tipoUsuario = editTipoUsuario.text.toString().trim()
            val telefono = editTelefono.text.toString().trim()

            if (nombre.isEmpty() || apellidos.isEmpty() || correo.isEmpty() || contrasena.isEmpty() || tipoUsuario.isEmpty() || telefono.isEmpty()) {
                Toast.makeText(this, "Por favor, rellena todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Generamos un ID aleatorio para el usuario (esto podría mejorarse)
            val idUsuario = (1000..9999).random()

            val resultado = dbHelper.insertarUsuario(idUsuario, nombre, apellidos, correo, contrasena, tipoUsuario, telefono)

            if (resultado) {
                Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, UsuarioActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Error al registrar usuario", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

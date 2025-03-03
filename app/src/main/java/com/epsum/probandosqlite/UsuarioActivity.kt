package com.epsum.probandosqlite


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class UsuarioActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_usuario)

        // Crear una instancia de DatabaseHelper
        val dbHelper = DatabaseHelper(this)

        // Copiar la base de datos desde assets si no existe
        dbHelper.copiarBaseDeDatos()

        // Obtener los usuarios
        val cursor = dbHelper.obtenerUsuarios()

        // Mostrar los usuarios en un TextView
        val textView = findViewById<TextView>(R.id.textViewUsuarios)

        // Usamos StringBuilder para construir el texto a mostrar
        val usuarios = StringBuilder()

        // Usamos el cursor dentro de un bloque 'use' para asegurarnos de que se cierra automáticamente
        cursor.use {
            while (it.moveToNext()) {
                // Obtener los índices de las columnas, usando getColumnIndexOrThrow para evitar -1
                val id = it.getInt(it.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID_USUARIO))
                val nombre = it.getString(it.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NOMBRE)) ?: "No disponible"
                val apellidos = it.getString(it.getColumnIndexOrThrow(DatabaseHelper.COLUMN_APELLIDOS)) ?: "No disponible"
                val correo = it.getString(it.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CORREO)) ?: "No disponible"
                val contrasena = it.getString(it.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CONTRASEÑA)) ?: "No disponible"
                val tipoUsuario = it.getString(it.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TIPO_USUARIO)) ?: "No disponible"
                val telefono = it.getString(it.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TELEFONO)) ?: "No disponible"

                // Agregar los datos al StringBuilder
                usuarios.append("ID: $id\nNombre: $nombre $apellidos\nCorreo: $correo\nTipo: $tipoUsuario\nTelefono: $telefono\n\n")
            }
        }
        val btnIrRegistro = findViewById<Button>(R.id.btnIrRegistro)
        btnIrRegistro.setOnClickListener {
            startActivity(Intent(this, RegistroActivity::class.java))
        }

        // Mostrar el contenido en el TextView
        textView.text = usuarios.toString()
    }
}

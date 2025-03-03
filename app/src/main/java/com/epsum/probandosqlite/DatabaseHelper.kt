package com.epsum.probandosqlite

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class DatabaseHelper(private val context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "Petcare.db"
        private const val DATABASE_VERSION = 1

        // Tabla Usuario
        private const val TABLE_USUARIO = "Usuario"
        const val COLUMN_ID_USUARIO = "ID_Usario"
        const val COLUMN_NOMBRE = "Nombre"
        const val COLUMN_APELLIDOS = "Apellidos"
        const val COLUMN_CORREO = "Correo"
        const val COLUMN_CONTRASEÑA = "Contraseña"
        const val COLUMN_TIPO_USUARIO = "TipoUsuario"
        const val COLUMN_TELEFONO = "Telefono"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        // Aquí puedes crear las tablas si fuera necesario, pero ya existen en tu base de datos
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Implementa la lógica de actualización de la base de datos si es necesario
    }

    // Método para obtener todos los usuarios de la tabla
    fun obtenerUsuarios(): Cursor {
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_USUARIO"
        return db.rawQuery(query, null) // Retorna un cursor con los resultados
    }

    // Copia la base de datos desde assets si no existe en el dispositivo
    fun copiarBaseDeDatos() {
        val dbPath = context.getDatabasePath(DATABASE_NAME).path

        // Verifica si la base de datos ya existe
        if (!File(dbPath).exists()) {
            Log.d("DatabaseHelper", "Base de datos no encontrada, copiando desde assets.")

            try {
                val inputStream = context.assets.open(DATABASE_NAME)
                val outputStream = FileOutputStream(dbPath)

                val buffer = ByteArray(1024)
                var length: Int
                while (true) {
                    length = inputStream.read(buffer)
                    if (length <= 0) break // Si no hay más datos, salimos del bucle
                    outputStream.write(buffer, 0, length)
                }

                inputStream.close()
                outputStream.close()

                Log.d("DatabaseHelper", "Base de datos copiada exitosamente.")
            } catch (e: IOException) {
                Log.e("DatabaseHelper", "Error al copiar la base de datos desde assets", e)
            }
        } else {
            Log.d("DatabaseHelper", "La base de datos ya existe en el dispositivo.")
        }
    }
    fun insertarUsuario(id: Int, nombre: String, apellidos: String, correo: String, contrasenaEncriptada: String, tipoUsuario: String, telefono: String): Boolean {
        val db = this.writableDatabase

        // Crear una sentencia SQL para insertar el usuario
        val sql = "INSERT INTO $TABLE_USUARIO ($COLUMN_ID_USUARIO, $COLUMN_NOMBRE, $COLUMN_APELLIDOS, $COLUMN_CORREO, $COLUMN_CONTRASEÑA, $COLUMN_TIPO_USUARIO, $COLUMN_TELEFONO) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)"

        try {
            // Usar un 'prepared statement' para evitar inyecciones SQL
            val statement = db.compileStatement(sql)
            statement.bindLong(1, id.toLong())
            statement.bindString(2, nombre)
            statement.bindString(3, apellidos)
            statement.bindString(4, correo)
            statement.bindString(5, contrasenaEncriptada)
            statement.bindString(6, tipoUsuario)
            statement.bindString(7, telefono)

            statement.executeInsert() // Ejecuta la inserción

            return true // Retorna true si la inserción fue exitosa
        } catch (e: Exception) {
            Log.e("DatabaseHelper", "Error al insertar usuario", e)
            return false // Retorna false si hubo un error
        } finally {
            db.close() // Cierra la base de datos
        }
    }

    // Function to authenticate user
    fun autenticarUsuario(correo: String, contrasena: String): Boolean {
        val db = this.readableDatabase // Use this instead of the erroneous DBHelper instantiation

        // Query to check if user credentials exist in the database
        val query = "SELECT * FROM $TABLE_USUARIO WHERE $COLUMN_CORREO = ? AND $COLUMN_CONTRASEÑA = ?"
        val cursor = db.rawQuery(query, arrayOf(correo, contrasena))

        // Check if there is a matching row
        return if (cursor.moveToFirst()) {
            cursor.close()
            true // Credentials are correct
        } else {
            cursor.close()
            false // Credentials are incorrect
        }
    }
}

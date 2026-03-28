package co.edu.udea.compumovil.gr01_20261.labs20261_gr01.lab1

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ContactDataActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_contact_data)

        // RECIBIR DATOS DE LA ACTIVITY ANTERIOR
        val nombres = intent.getStringExtra("nombres")
        val apellidos = intent.getStringExtra("apellidos")
        val sexo = intent.getStringExtra("sexo")
        val fechaNacimiento = intent.getStringExtra("fechaNacimiento")
        val escolaridad = intent.getStringExtra("escolaridad")

        // Mostrar en Logcat para verificar
        Log.d("LAB1", "Datos recibidos:")
        Log.d("LAB1", "$nombres $apellidos")
        Log.d("LAB1", "$sexo")
        Log.d("LAB1", "$fechaNacimiento")
        Log.d("LAB1", "$escolaridad")

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
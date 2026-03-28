package co.edu.udea.compumovil.gr01_20261.labs20261_gr01.lab1

import android.os.Bundle
import android.util.Patterns
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class ContactDataActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_data)

        // REFERENCIAS
        val etTelefono = findViewById<EditText>(R.id.etTelefono)
        val etDireccion = findViewById<EditText>(R.id.etDireccion)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val acPais = findViewById<AutoCompleteTextView>(R.id.acPais)
        val acCiudad = findViewById<AutoCompleteTextView>(R.id.acCiudad)
        val btnGuardar = findViewById<Button>(R.id.btnGuardar)

        // DATOS RECIBIDOS
        val nombres = intent.getStringExtra("nombres")
        val apellidos = intent.getStringExtra("apellidos")

        // LISTA PAISES
        val paises = arrayOf(
            "Colombia", "México", "Argentina", "Perú", "Chile",
            "Ecuador", "Venezuela", "Bolivia", "Uruguay", "Paraguay"
        )

        val adapterPais = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, paises)
        acPais.setAdapter(adapterPais)

        // LISTA CIUDADES
        val ciudades = arrayOf(
            "Medellín", "Bogotá", "Cali", "Barranquilla",
            "Cartagena", "Bucaramanga", "Pereira"
        )

        val adapterCiudad = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, ciudades)
        acCiudad.setAdapter(adapterCiudad)

        // BOTÓN
        btnGuardar.setOnClickListener {

            val telefono = etTelefono.text.toString()
            val direccion = etDireccion.text.toString()
            val email = etEmail.text.toString()
            val pais = acPais.text.toString()
            val ciudad = acCiudad.text.toString()

            // VALIDACIONES
            if (telefono.isEmpty() || email.isEmpty() || pais.isEmpty()) {
                Toast.makeText(this, "Complete los campos obligatorios", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Email inválido", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // MOSTRAR RESULTADO
            Toast.makeText(this, "Datos guardados", Toast.LENGTH_SHORT).show()
        }
    }
}
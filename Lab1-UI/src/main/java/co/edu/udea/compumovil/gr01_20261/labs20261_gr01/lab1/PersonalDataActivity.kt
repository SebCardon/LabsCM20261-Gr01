package co.edu.udea.compumovil.gr01_20261.labs20261_gr01.lab1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class PersonalDataActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personal_data)

        val etNombres = findViewById<EditText>(R.id.etNombres)
        val etApellidos = findViewById<EditText>(R.id.etApellidos)
        val rgSexo = findViewById<RadioGroup>(R.id.rgSexo)
        val datePicker = findViewById<DatePicker>(R.id.datePicker)
        val spinner = findViewById<Spinner>(R.id.spEscolaridad)
        val btnSiguiente = findViewById<Button>(R.id.btnSiguiente)

        // Lista del spinner
        val escolaridad = arrayOf(
            "Primaria",
            "Secundaria",
            "Técnico",
            "Universitario",
            "Postgrado"
        )

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            escolaridad
        )

        spinner.adapter = adapter

        btnSiguiente.setOnClickListener {

            val nombres = etNombres.text.toString()
            val apellidos = etApellidos.text.toString()

            if (nombres.isEmpty() || apellidos.isEmpty()) {
                Toast.makeText(
                    this,
                    "Debe ingresar nombres y apellidos",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            // Obtener sexo
            val sexoId = rgSexo.checkedRadioButtonId
            var sexo = "No especificado"

            if (sexoId != -1) {
                val radio = findViewById<RadioButton>(sexoId)
                sexo = radio.text.toString()
            }

            // Obtener fecha
            val dia = datePicker.dayOfMonth
            val mes = datePicker.month + 1
            val anio = datePicker.year

            val fechaNacimiento = "$dia/$mes/$anio"

            // Obtener escolaridad
            val esc = spinner.selectedItem.toString()

            // Mostrar en Logcat
            Log.d("LAB1", """
                Información personal:
                $nombres $apellidos
                $sexo
                Nació el $fechaNacimiento
                $esc
            """.trimIndent())

            // Ir a la siguiente activity
            val intent = Intent(this, ContactDataActivity::class.java)

            intent.putExtra("nombres", nombres)
            intent.putExtra("apellidos", apellidos)
            intent.putExtra("sexo", sexo)
            intent.putExtra("fechaNacimiento", fechaNacimiento)
            intent.putExtra("escolaridad", esc)

            startActivity(intent)
        }
    }
}
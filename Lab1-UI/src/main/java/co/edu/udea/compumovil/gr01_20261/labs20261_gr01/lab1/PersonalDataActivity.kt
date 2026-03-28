package co.edu.udea.compumovil.gr01_20261.labs20261_gr01.lab1

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import co.edu.udea.compumovil.gr01_20261.labs20261_gr01.lab1.ui.theme.Labs20261Gr01Theme
import java.util.Calendar

class PersonalDataActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Permite que el contenido suba cuando aparece el teclado
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            Labs20261Gr01Theme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    PersonalDataScreen { nombres, apellidos, sexo, fecha, escolaridad ->
                        val logMsg = buildString {
                            appendLine("Información personal:")
                            appendLine("$nombres $apellidos")
                            if (sexo.isNotEmpty()) appendLine(sexo)
                            if (fecha.isNotEmpty()) appendLine("Nació el $fecha")
                            append(escolaridad)
                        }
                        Log.d("LAB1", logMsg)
                        val intent = Intent(this, ContactDataActivity::class.java).apply {
                            putExtra("nombres", nombres)
                            putExtra("apellidos", apellidos)
                            putExtra("sexo", sexo)
                            putExtra("fechaNacimiento", fecha)
                            putExtra("escolaridad", escolaridad)
                        }
                        startActivity(intent)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonalDataScreen(onSiguiente: (String, String, String, String, String) -> Unit) {
    val context = LocalContext.current
    val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE
    val keyboardController = LocalSoftwareKeyboardController.current

    // rememberSaveable conserva los datos ante cambios de configuración (rotación, idioma, etc.)
    var nombres by rememberSaveable { mutableStateOf("") }
    var apellidos by rememberSaveable { mutableStateOf("") }
    var sexo by rememberSaveable { mutableStateOf("") }          // "HOMBRE" | "MUJER" | ""
    var fechaDisplay by rememberSaveable { mutableStateOf("") }
    var showDatePicker by rememberSaveable { mutableStateOf(false) }
    var escolaridadIndex by rememberSaveable { mutableStateOf(0) }
    var dropdownExpanded by rememberSaveable { mutableStateOf(false) }

    val escolaridadOpciones = stringArrayResource(R.array.opciones_escolaridad)
    val labelHombre = stringResource(R.string.opcion_hombre)
    val labelMujer = stringResource(R.string.opcion_mujer)
    val labelNoEspecificado = stringResource(R.string.no_especificado)
    val labelSinSeleccionar = stringResource(R.string.sin_seleccionar)

    val focusApellidos = remember { FocusRequester() }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val cal = Calendar.getInstance().apply { timeInMillis = millis }
                        fechaDisplay = "${cal.get(Calendar.DAY_OF_MONTH)}/${cal.get(Calendar.MONTH) + 1}/${cal.get(Calendar.YEAR)}"
                    }
                    showDatePicker = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text(stringResource(R.string.cancelar))
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()                         // Sube el contenido al aparecer el teclado
            .verticalScroll(rememberScrollState()) // Permite scroll si el contenido no cabe
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(R.string.titulo_info_personal),
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Nombres y Apellidos: lado a lado en landscape, apilados en portrait
        if (isLandscape) {
            Row(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = nombres,
                    onValueChange = { nombres = it },
                    label = { Text(stringResource(R.string.label_nombres)) },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        capitalization = KeyboardCapitalization.Words,
                        autoCorrect = false,
                        imeAction = ImeAction.Next   // "Siguiente" en el teclado
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusApellidos.requestFocus() }
                    ),
                    singleLine = true,
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                )
                OutlinedTextField(
                    value = apellidos,
                    onValueChange = { apellidos = it },
                    label = { Text(stringResource(R.string.label_apellidos)) },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        capitalization = KeyboardCapitalization.Words,
                        autoCorrect = false,
                        imeAction = ImeAction.Done   // "Listo" en el teclado (último campo de texto)
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { keyboardController?.hide() }
                    ),
                    singleLine = true,
                    modifier = Modifier
                        .focusRequester(focusApellidos)
                        .weight(1f)
                        .padding(start = 8.dp)
                )
            }
        } else {
            OutlinedTextField(
                value = nombres,
                onValueChange = { nombres = it },
                label = { Text(stringResource(R.string.label_nombres)) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    capitalization = KeyboardCapitalization.Words,
                    autoCorrect = false,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusApellidos.requestFocus() }
                ),
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )
            OutlinedTextField(
                value = apellidos,
                onValueChange = { apellidos = it },
                label = { Text(stringResource(R.string.label_apellidos)) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    capitalization = KeyboardCapitalization.Words,
                    autoCorrect = false,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { keyboardController?.hide() }
                ),
                singleLine = true,
                modifier = Modifier
                    .focusRequester(focusApellidos)
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Sexo
        Text(stringResource(R.string.label_sexo), style = MaterialTheme.typography.bodyLarge)
        Row(verticalAlignment = Alignment.CenterVertically) {
            RadioButton(selected = sexo == "HOMBRE", onClick = { sexo = "HOMBRE" })
            Text(labelHombre, modifier = Modifier.padding(end = 16.dp))
            RadioButton(selected = sexo == "MUJER", onClick = { sexo = "MUJER" })
            Text(labelMujer)
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Fecha de nacimiento con botón "Cambiar"
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                stringResource(R.string.label_fecha_nacimiento),
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = fechaDisplay.ifEmpty { stringResource(R.string.sin_seleccionar) },
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyMedium
            )
            Button(onClick = { showDatePicker = true }) {
                Text(stringResource(R.string.btn_cambiar))
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Grado de escolaridad (Dropdown)
        ExposedDropdownMenuBox(
            expanded = dropdownExpanded,
            onExpandedChange = { dropdownExpanded = !dropdownExpanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = escolaridadOpciones[escolaridadIndex],
                onValueChange = {},
                readOnly = true,
                label = { Text(stringResource(R.string.label_escolaridad)) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = dropdownExpanded) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = dropdownExpanded,
                onDismissRequest = { dropdownExpanded = false }
            ) {
                escolaridadOpciones.forEachIndexed { index, opcion ->
                    DropdownMenuItem(
                        text = { Text(opcion) },
                        onClick = {
                            escolaridadIndex = index
                            dropdownExpanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Botón Siguiente con validación de campos obligatorios
        Button(
            onClick = {
                when {
                    nombres.isBlank() -> Toast.makeText(
                        context,
                        context.getString(R.string.error_nombres),
                        Toast.LENGTH_SHORT
                    ).show()
                    apellidos.isBlank() -> Toast.makeText(
                        context,
                        context.getString(R.string.error_apellidos),
                        Toast.LENGTH_SHORT
                    ).show()
                    else -> {
                        val sexoDisplay = when (sexo) {
                            "HOMBRE" -> labelHombre
                            "MUJER"  -> labelMujer
                            else     -> ""   // vacío = no seleccionado, no aparece en el log
                        }
                        onSiguiente(
                            nombres,
                            apellidos,
                            sexoDisplay,
                            fechaDisplay,  // vacío si no se seleccionó, no aparece en el log
                            escolaridadOpciones[escolaridadIndex]
                        )
                    }
                }
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(stringResource(R.string.btn_siguiente))
        }
    }
}

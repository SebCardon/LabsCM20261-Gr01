package co.edu.udea.compumovil.gr01_20261.labs20261_gr01.lab1

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.util.Patterns
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import co.edu.udea.compumovil.gr01_20261.labs20261_gr01.lab1.ui.theme.Labs20261Gr01Theme

class ContactDataActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            Labs20261Gr01Theme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    ContactDataScreen(
                        nombres = intent.getStringExtra("nombres") ?: "",
                        apellidos = intent.getStringExtra("apellidos") ?: ""
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactDataScreen(nombres: String, apellidos: String) {
    val context = LocalContext.current
    val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE
    val keyboardController = LocalSoftwareKeyboardController.current

    // rememberSaveable conserva los datos en rotación y cambios de configuración
    var telefono by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var paisQuery by rememberSaveable { mutableStateOf("") }
    var paisExpanded by rememberSaveable { mutableStateOf(false) }
    var ciudadQuery by rememberSaveable { mutableStateOf("") }
    var ciudadExpanded by rememberSaveable { mutableStateOf(false) }
    var direccion by rememberSaveable { mutableStateOf("") }

    val paises = stringArrayResource(R.array.paises_latinoamerica)
    val ciudades = stringArrayResource(R.array.ciudades_colombia)

    // derivedStateOf evita recalcular el filtro en cada recomposición no relacionada
    val filteredPaises by remember { derivedStateOf { paises.filter { it.contains(paisQuery, ignoreCase = true) } } }
    val filteredCiudades by remember { derivedStateOf { ciudades.filter { it.contains(ciudadQuery, ignoreCase = true) } } }

    // Cadena de foco para la acción "Siguiente" del teclado
    val focusEmail = remember { FocusRequester() }
    val focusPais = remember { FocusRequester() }
    val focusCiudad = remember { FocusRequester() }
    val focusDireccion = remember { FocusRequester() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()                          // Sube contenido al aparecer el teclado
            .verticalScroll(rememberScrollState()) // Permite scroll si el teclado tapa campos
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(R.string.titulo_info_contacto),
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (isLandscape) {
            // ── Landscape: Teléfono + Email en fila ──────────────────────────
            Row(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = telefono,
                    onValueChange = { telefono = it },
                    label = { Text(stringResource(R.string.label_telefono)) },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Phone,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(onNext = { focusEmail.requestFocus() }),
                    singleLine = true,
                    modifier = Modifier.weight(1f).padding(end = 8.dp)
                )
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text(stringResource(R.string.label_email)) },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(onNext = { focusPais.requestFocus() }),
                    singleLine = true,
                    modifier = Modifier
                        .focusRequester(focusEmail)
                        .weight(1f)
                        .padding(start = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // ── Landscape: País + Ciudad en fila ─────────────────────────────
            Row(modifier = Modifier.fillMaxWidth()) {
                ExposedDropdownMenuBox(
                    expanded = paisExpanded && filteredPaises.isNotEmpty(),
                    onExpandedChange = { paisExpanded = it },
                    modifier = Modifier.weight(1f).padding(end = 8.dp)
                ) {
                    OutlinedTextField(
                        value = paisQuery,
                        onValueChange = { paisQuery = it; paisExpanded = true },
                        label = { Text(stringResource(R.string.label_pais)) },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        keyboardActions = KeyboardActions(onNext = {
                            paisExpanded = false; focusCiudad.requestFocus()
                        }),
                        singleLine = true,
                        modifier = Modifier
                            .focusRequester(focusPais)
                            .menuAnchor(MenuAnchorType.PrimaryEditable)
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = paisExpanded && filteredPaises.isNotEmpty(),
                        onDismissRequest = { paisExpanded = false }
                    ) {
                        filteredPaises.forEach { pais ->
                            DropdownMenuItem(
                                text = { Text(pais) },
                                onClick = { paisQuery = pais; paisExpanded = false }
                            )
                        }
                    }
                }

                ExposedDropdownMenuBox(
                    expanded = ciudadExpanded && filteredCiudades.isNotEmpty(),
                    onExpandedChange = { ciudadExpanded = it },
                    modifier = Modifier.weight(1f).padding(start = 8.dp)
                ) {
                    OutlinedTextField(
                        value = ciudadQuery,
                        onValueChange = { ciudadQuery = it; ciudadExpanded = true },
                        label = { Text(stringResource(R.string.label_ciudad)) },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        keyboardActions = KeyboardActions(onNext = {
                            ciudadExpanded = false; focusDireccion.requestFocus()
                        }),
                        singleLine = true,
                        modifier = Modifier
                            .focusRequester(focusCiudad)
                            .menuAnchor(MenuAnchorType.PrimaryEditable)
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = ciudadExpanded && filteredCiudades.isNotEmpty(),
                        onDismissRequest = { ciudadExpanded = false }
                    ) {
                        filteredCiudades.forEach { ciudad ->
                            DropdownMenuItem(
                                text = { Text(ciudad) },
                                onClick = { ciudadQuery = ciudad; ciudadExpanded = false }
                            )
                        }
                    }
                }
            }

        } else {
            // ── Portrait: campos apilados ─────────────────────────────────────
            OutlinedTextField(
                value = telefono,
                onValueChange = { telefono = it },
                label = { Text(stringResource(R.string.label_telefono)) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Phone,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(onNext = { focusEmail.requestFocus() }),
                singleLine = true,
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
            )
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text(stringResource(R.string.label_email)) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(onNext = { focusPais.requestFocus() }),
                singleLine = true,
                modifier = Modifier
                    .focusRequester(focusEmail)
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )

            // País autocomplete
            ExposedDropdownMenuBox(
                expanded = paisExpanded && filteredPaises.isNotEmpty(),
                onExpandedChange = { paisExpanded = it },
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
            ) {
                OutlinedTextField(
                    value = paisQuery,
                    onValueChange = { paisQuery = it; paisExpanded = true },
                    label = { Text(stringResource(R.string.label_pais)) },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(onNext = {
                        paisExpanded = false; focusCiudad.requestFocus()
                    }),
                    singleLine = true,
                    modifier = Modifier
                        .focusRequester(focusPais)
                        .menuAnchor(MenuAnchorType.PrimaryEditable)
                        .fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = paisExpanded && filteredPaises.isNotEmpty(),
                    onDismissRequest = { paisExpanded = false }
                ) {
                    filteredPaises.forEach { pais ->
                        DropdownMenuItem(
                            text = { Text(pais) },
                            onClick = { paisQuery = pais; paisExpanded = false }
                        )
                    }
                }
            }

            // Ciudad autocomplete
            ExposedDropdownMenuBox(
                expanded = ciudadExpanded && filteredCiudades.isNotEmpty(),
                onExpandedChange = { ciudadExpanded = it },
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
            ) {
                OutlinedTextField(
                    value = ciudadQuery,
                    onValueChange = { ciudadQuery = it; ciudadExpanded = true },
                    label = { Text(stringResource(R.string.label_ciudad)) },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(onNext = {
                        ciudadExpanded = false; focusDireccion.requestFocus()
                    }),
                    singleLine = true,
                    modifier = Modifier
                        .focusRequester(focusCiudad)
                        .menuAnchor(MenuAnchorType.PrimaryEditable)
                        .fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = ciudadExpanded && filteredCiudades.isNotEmpty(),
                    onDismissRequest = { ciudadExpanded = false }
                ) {
                    filteredCiudades.forEach { ciudad ->
                        DropdownMenuItem(
                            text = { Text(ciudad) },
                            onClick = { ciudadQuery = ciudad; ciudadExpanded = false }
                        )
                    }
                }
            }
        }

        // Dirección: siempre abajo a ancho completo (portrait y landscape)
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = direccion,
            onValueChange = { direccion = it },
            label = { Text(stringResource(R.string.label_direccion)) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                autoCorrect = false,           // Sin sugerencias, como pide el enunciado
                imeAction = ImeAction.Done     // Último campo de texto
            ),
            keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
            singleLine = true,
            modifier = Modifier
                .focusRequester(focusDireccion)
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Botón con validación de campos obligatorios
        Button(
            onClick = {
                when {
                    telefono.isBlank() -> Toast.makeText(
                        context, context.getString(R.string.error_telefono), Toast.LENGTH_SHORT
                    ).show()
                    email.isBlank() -> Toast.makeText(
                        context, context.getString(R.string.error_email), Toast.LENGTH_SHORT
                    ).show()
                    !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> Toast.makeText(
                        context, context.getString(R.string.error_email_invalido), Toast.LENGTH_SHORT
                    ).show()
                    paisQuery.isBlank() -> Toast.makeText(
                        context, context.getString(R.string.error_pais), Toast.LENGTH_SHORT
                    ).show()
                    else -> {
                        val logMsg = buildString {
                            appendLine("Información de contacto:")
                            appendLine("Teléfono: $telefono")
                            if (direccion.isNotEmpty()) appendLine("Dirección: $direccion")
                            appendLine("Email: $email")
                            appendLine("País: $paisQuery")
                            if (ciudadQuery.isNotEmpty()) append("Ciudad: $ciudadQuery")
                        }
                        Log.d("LAB1", logMsg)
                        Toast.makeText(
                            context, context.getString(R.string.datos_guardados), Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(stringResource(R.string.btn_siguiente))
        }
    }
}

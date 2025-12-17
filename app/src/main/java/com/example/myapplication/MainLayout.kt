package com.example.myapplication

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MainLayout(modifier: Modifier = Modifier) {
    var campo1 by remember { mutableStateOf("") }
    var campoDropdownPrincipal by remember { mutableStateOf("") }
    var campoDropdown1 by remember { mutableStateOf("") }
    var campoDropdown2 by remember { mutableStateOf("") }

    val opciones = remember { listOf("Opción 1", "Opción 2", "Opción 3") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            "Pantalla Principal",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        OptimizedDropdown(
            value = campoDropdownPrincipal,
            onValueChange = { campoDropdownPrincipal = it },
            label = "Dropdown Principal",
            options = opciones,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OptimizedDropdown(
                value = campoDropdown1,
                onValueChange = { campoDropdown1 = it },
                label = "Dropdown 1",
                options = opciones,
                modifier = Modifier.weight(1f)
            )

            OptimizedDropdown(
                value = campoDropdown2,
                onValueChange = { campoDropdown2 = it },
                label = "Dropdown 2",
                options = opciones,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = campo1,
            onValueChange = {
                if (it.isEmpty() || it.matches(Regex("^\\d*\\.?\\d*$")))
                    campo1 = it
            },
            placeholder = { Text("Gramos") },
            label = { Text("Gramos") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            singleLine = true,
            shape = RoundedCornerShape(4.dp),
            modifier = Modifier.width(250.dp)
        )

        Spacer(modifier = Modifier.height(72.dp))

        ResultBox(text = "RESULTADO")

        Spacer(modifier = Modifier.weight(0.8f))

        Button(
            onClick = {},
            shape = RoundedCornerShape(50),
            modifier = Modifier.size(120.dp)
        ) {
            Text("OK")
        }

        Spacer(modifier = Modifier.height(92.dp))
    }
}
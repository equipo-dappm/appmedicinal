package com.burelo.appmedicinal.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ─────────────────────────────────────────────────────────────
//  SCREEN
// ─────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchResultsScreen(
    query: String = "",
    onBack: () -> Unit = {},
    onResultClick: (String) -> Unit = {}
) {
    val filteredPlants = remember(query) {
        homePlants.filter {
            it.name.contains(query, ignoreCase = true) ||
            it.description.contains(query, ignoreCase = true)
        }
    }

    Scaffold(
        containerColor = SurfaceColor,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = query,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Atrás",
                            tint = PrimaryGreen
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = SurfaceColor)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (filteredPlants.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 64.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "🔍", fontSize = 48.sp)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "No se encontraron coincidencias",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Intenta con otro término de búsqueda",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                filteredPlants.forEach { plant ->
                    PlantResultCard(
                        plant = plant,
                        onClick = { onResultClick(plant.name) }
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

// ─────────────────────────────────────────────────────────────
//  RESULT CARD
// ─────────────────────────────────────────────────────────────

@Composable
fun PlantResultCard(
    plant: HomePlant,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(12.dp))
            ) {
                androidx.compose.foundation.Image(
                    painter = painterResource(id = plant.imageRes),
                    contentDescription = plant.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                        .clip(RoundedCornerShape(12.dp))
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = plant.name,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    text = plant.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }

            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = PrimaryGreen
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────
//  PREVIEW
// ─────────────────────────────────────────────────────────────

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SearchResultsScreenPreview() {
    MaterialTheme {
        SearchResultsScreen()
    }
}

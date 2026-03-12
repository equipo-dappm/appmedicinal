package com.burelo.appmedicinal.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ─────────────────────────────────────────────────────────────
//  DATA MODELS
// ─────────────────────────────────────────────────────────────

enum class EvidenceType { SCIENTIFIC, TRADITIONAL }

data class SearchResult(
    val name: String,
    val evidenceType: EvidenceType,
    val preparation: String,
    val time: String,
    val warning: String? = null,
    val emoji: String = "🌿"
)

val sampleFilters = listOf("Todos", "Plantas", "Infusiones", "Aceites", "Con evidencia")

val sampleResults = listOf(
    SearchResult(
        name = "Manzanilla",
        evidenceType = EvidenceType.SCIENTIFIC,
        preparation = "Infusión",
        time = "15-30 min",
        emoji = "🌼"
    ),
    SearchResult(
        name = "Lavanda",
        evidenceType = EvidenceType.TRADITIONAL,
        preparation = "Aceite esencial",
        time = "20-40 min",
        warning = "Contraindicación: evitar en embarazo",
        emoji = "💜"
    )
)

// ─────────────────────────────────────────────────────────────
//  SCREEN
// ─────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchResultsScreen(
    query: String = "dolor de cabeza",
    onBack: () -> Unit = {},
    onResultClick: (String) -> Unit = {}
) {
    var selectedFilter by remember { mutableIntStateOf(0) }

    Scaffold(
        containerColor = SurfaceColor,
        topBar = {
            SearchResultsTopBar(
                query = query,
                onBack = onBack,
                selectedFilter = selectedFilter,
                onFilterSelected = { selectedFilter = it }
            )
        },
        bottomBar = {
            NaturaMedBottomNav(selectedTab = 1, onTabSelected = {})
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
            sampleResults.forEach { result ->
                SearchResultCard(
                    result = result,
                    onClick = { onResultClick(result.name) }
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

// ─────────────────────────────────────────────────────────────
//  TOP BAR + FILTER CHIPS
// ─────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchResultsTopBar(
    query: String,
    onBack: () -> Unit,
    selectedFilter: Int,
    onFilterSelected: (Int) -> Unit
) {
    Surface(
        color = SurfaceColor,
        shadowElevation = 2.dp
    ) {
        Column {
            // Title row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 4.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Atrás",
                        tint = PrimaryGreen
                    )
                }

                Text(
                    text = query,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.weight(1f)
                )

                IconButton(onClick = {}) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Buscar",
                        tint = PrimaryGreen
                    )
                }
            }

            // Filter chips row
            Row(
                modifier = Modifier
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                sampleFilters.forEachIndexed { index, label ->
                    FilterChip(
                        selected = selectedFilter == index,
                        onClick = { onFilterSelected(index) },
                        label = {
                            Text(
                                text = label,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = PrimaryGreen,
                            selectedLabelColor = Color.White,
                            containerColor = PrimaryContainer,
                            labelColor = PrimaryGreen
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = selectedFilter == index,
                            borderColor = Color.Transparent,
                            selectedBorderColor = Color.Transparent
                        ),
                        shape = CircleShape
                    )
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────
//  RESULT CARD
// ─────────────────────────────────────────────────────────────

@Composable
fun SearchResultCard(result: SearchResult, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column {
            // Main content row
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Circular plant image placeholder
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .clip(CircleShape)
                        .background(PrimaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = result.emoji, fontSize = 32.sp)
                }

                // Text content
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = result.name,
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(onClick = {}) {
                            Icon(
                                imageVector = Icons.Default.Bookmark,
                                contentDescription = "Guardar",
                                tint = PrimaryGreen
                            )
                        }
                    }

                    // Evidence badge
                    EvidenceBadge(type = result.evidenceType)

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "${result.preparation} • ${result.time}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Warning strip (if present)
            result.warning?.let { warningText ->
                HorizontalDivider(color = Color(0xFFE76F51).copy(alpha = 0.2f))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFE76F51).copy(alpha = 0.08f))
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = null,
                        tint = Color(0xFFE76F51),
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = warningText,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFE76F51)
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun EvidenceBadge(type: EvidenceType) {
    val (icon, label, color) = when (type) {
        EvidenceType.SCIENTIFIC -> Triple("✅", "Respaldo científico", Color(0xFF2E7D32))
        EvidenceType.TRADITIONAL -> Triple("📜", "Uso tradicional",    Color(0xFFB45309))
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        Text(text = icon, fontSize = 14.sp)
        Text(
            text = label.uppercase(),
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.8.sp,
                color = color
            )
        )
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

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
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ─────────────────────────────────────────────────────────────
//  DATA MODELS
// ─────────────────────────────────────────────────────────────

data class PreparationStep(val number: Int, val emoji: String, val description: String)

data class PlantDetail(
    val name: String,
    val scientificName: String,
    val description: String,
    val emoji: String,
    val evidenceLevel: String,
    val time: String,
    val preparationType: String,
    val steps: List<PreparationStep>,
    val dosageDigestive: String,
    val dosageRelaxing: String,
    val contraindications: List<String>
)

val manzanillaDetail = PlantDetail(
    name = "Manzanilla",
    scientificName = "Matricaria chamomilla",
    description = "La manzanilla es una de las plantas medicinales más utilizadas y conocidas del mundo por sus potentes propiedades digestivas, calmantes y antiinflamatorias, siendo segura para la mayoría de los adultos.",
    emoji = "🌼",
    evidenceLevel = "Evidencia Alta",
    time = "15-30 min",
    preparationType = "Infusión",
    steps = listOf(
        PreparationStep(1, "🌿", "1 cda. flores secas"),
        PreparationStep(2, "💧", "250ml agua caliente"),
        PreparationStep(3, "⏱️", "10 min reposo"),
        PreparationStep(4, "🫙", "Filtrar y beber"),
    ),
    dosageDigestive = "1 taza después de las comidas principales.",
    dosageRelaxing = "1 taza 30 minutos antes de dormir.",
    contraindications = listOf(
        "Embarazo: Consultar con médico tratante.",
        "Alergias: Evitar si es alérgico a las margaritas (Asteráceas).",
        "Interacciones: Puede potenciar sedantes."
    )
)

// ─────────────────────────────────────────────────────────────
//  SCREEN
// ─────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlantDetailScreen(
    plant: PlantDetail = manzanillaDetail,
    onBack: () -> Unit = {}
) {
    var isFavorite by remember { mutableStateOf(false) }
    var evidenceExpanded by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = SurfaceColor,
        topBar = {
            PlantDetailTopBar(
                plantName = plant.name,
                onBack = onBack,
                onShare = {}
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { isFavorite = !isFavorite },
                containerColor = PrimaryGreen,
                contentColor = Color.White,
                shape = CircleShape
            ) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Favorito",
                    tint = if (isFavorite) Color(0xFFFF6B6B) else Color.White
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            // Hero image placeholder
            HeroSection(plant = plant)

            // Title + badges
            TitleSection(plant = plant)

            // Description
            Text(
                text = plant.description,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                lineHeight = 24.sp
            )

            // Preparation steps
            PreparationSection(steps = plant.steps)

            // Dosage
            DosageSection(
                digestive = plant.dosageDigestive,
                relaxing = plant.dosageRelaxing
            )

            // Contraindications
            ContraindicationsSection(items = plant.contraindications)

            // Expandable evidence section
            EvidenceSection(
                expanded = evidenceExpanded,
                onToggle = { evidenceExpanded = !evidenceExpanded }
            )

            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

// ─────────────────────────────────────────────────────────────
//  TOP BAR
// ─────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlantDetailTopBar(plantName: String, onBack: () -> Unit, onShare: () -> Unit) {
    TopAppBar(
        title = {
            Text(
                text = plantName,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = PrimaryGreen
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
        actions = {
            IconButton(onClick = onShare) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = "Compartir",
                    tint = PrimaryGreen
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = SurfaceColor
        )
    )
}

// ─────────────────────────────────────────────────────────────
//  HERO  (placeholder while no real images)
// ─────────────────────────────────────────────────────────────

@Composable
fun HeroSection(plant: PlantDetail) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .height(200.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(PrimaryContainer),
        contentAlignment = Alignment.Center
    ) {
        // Replace with AsyncImage(Coil) when you have real image URLs:
        // AsyncImage(model = plant.imageUrl, contentDescription = plant.name,
        //     modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
        Text(text = plant.emoji, fontSize = 80.sp)
    }
}

// ─────────────────────────────────────────────────────────────
//  TITLE + BADGE CHIPS
// ─────────────────────────────────────────────────────────────

@Composable
fun TitleSection(plant: PlantDetail) {
    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
        Text(
            text = plant.name,
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
        )
        Text(
            text = plant.scientificName,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontStyle = FontStyle.Italic,
                color = PrimaryGreen.copy(alpha = 0.7f)
            ),
            modifier = Modifier.padding(bottom = 12.dp)
        )

        // Badge chips
        Row(
            modifier = Modifier.horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf(
                "✅ ${plant.evidenceLevel}",
                "⏱️ ${plant.time}",
                "☕ ${plant.preparationType}"
            ).forEach { label ->
                SuggestionChip(
                    onClick = {},
                    label = {
                        Text(
                            text = label,
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    },
                    colors = SuggestionChipDefaults.suggestionChipColors(
                        containerColor = PrimaryContainer,
                        labelColor = PrimaryGreen
                    ),
                    border = SuggestionChipDefaults.suggestionChipBorder(
                        enabled = true,
                        borderColor = Color.Transparent
                    )
                )
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────
//  PREPARATION STEPS
// ─────────────────────────────────────────────────────────────

@Composable
fun PreparationSection(steps: List<PreparationStep>) {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        Text(
            text = "Preparación paso a paso",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 12.dp)
        )

        // 2x2 grid using nested rows
        val chunked = steps.chunked(2)
        chunked.forEach { rowSteps ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                rowSteps.forEach { step ->
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .padding(bottom = 12.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        ),
                        elevation = CardDefaults.cardElevation(1.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "${step.number}",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = PrimaryGreen
                                )
                            )
                            Text(
                                text = step.emoji,
                                fontSize = 24.sp,
                                modifier = Modifier.padding(vertical = 6.dp)
                            )
                            Text(
                                text = step.description,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            )
                        }
                    }
                }
                // Fill empty space if odd number of steps
                if (rowSteps.size == 1) Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────
//  DOSAGE
// ─────────────────────────────────────────────────────────────

@Composable
fun DosageSection(digestive: String, relaxing: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = PrimaryGreen.copy(alpha = 0.07f)
        ),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "Dosificación recomendada",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = PrimaryGreen
                ),
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "DIGESTIVO",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                    Text(
                        text = digestive,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "RELAJANTE",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                    Text(
                        text = relaxing,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────
//  CONTRAINDICATIONS
// ─────────────────────────────────────────────────────────────

@Composable
fun ContraindicationsSection(items: List<String>) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFF3E0)
        ),
        border = CardDefaults.outlinedCardBorder().copy(
            // Orange border
        ),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE76F51)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }

            Column {
                Text(
                    text = "Contraindicaciones",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF7C2D12)
                    ),
                    modifier = Modifier.padding(bottom = 6.dp)
                )
                items.forEach { item ->
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.padding(vertical = 2.dp)
                    ) {
                        Text(text = "•", color = Color(0xFF9A3412), fontWeight = FontWeight.Bold)
                        Text(
                            text = item,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color(0xFF9A3412)
                            )
                        )
                    }
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────
//  EXPANDABLE EVIDENCE
// ─────────────────────────────────────────────────────────────

@Composable
fun EvidenceSection(expanded: Boolean, onToggle: () -> Unit) {
    Card(
        onClick = onToggle,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "🔬", fontSize = 20.sp)
            Text(
                text = "Evidencia Científica",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 12.dp)
            )
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = if (expanded) "Colapsar" else "Expandir",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        if (expanded) {
            Text(
                text = "Múltiples estudios clínicos han demostrado la eficacia de la manzanilla para tratar problemas digestivos leves y promover la relajación. Los compuestos activos incluyen apigenina, un flavonoide con propiedades ansiolíticas.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────
//  PREVIEW
// ─────────────────────────────────────────────────────────────

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PlantDetailScreenPreview() {
    MaterialTheme {
        PlantDetailScreen()
    }
}

package com.burelo.appmedicinal.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.burelo.appmedicinal.R

// ─────────────────────────────────────────────────────────────
//  DATA MODEL
// ─────────────────────────────────────────────────────────────

data class MedicinalPlant(
    val name: String,
    val scientificName: String,
    val imageRes: Int,
    val description: String,
    val uses: String,
    val preparation: List<String>,
    val contraindications: String,
    val videoUrl: String,
    val cardColor: Color
)

// ─────────────────────────────────────────────────────────────
//  PLANT DATA
// ─────────────────────────────────────────────────────────────

val tabascoPlants = listOf(
    MedicinalPlant(
        name = "Maguey Morado",
        scientificName = "Tradescantia spathacea",
        imageRes = R.drawable.maguey_morado,
        description = "Planta herbácea de hojas alargadas, verdes en el haz y púrpura intenso en el envés.",
        uses = "Antiséptico, desinflamatorio y antibacteriano.",
        preparation = listOf(
            "Lavar 2 hojas frescas.",
            "Tópico: Machacar hasta extraer jugo y aplicar con gasa en heridas.",
            "Infusión: Hervir trozos en 500ml de agua por 5 min para espasmos."
        ),
        contraindications = "No ingerir de forma crónica. Evitar en embarazo. Puede causar dermatitis por contacto.",
        videoUrl = "https://www.youtube.com/embed/MUBiXRerwPg",
        cardColor = Color(0xFFE8F5E9)
    ),
    MedicinalPlant(
        name = "Oreganón",
        scientificName = "Coleus amboinicus",
        imageRes = R.drawable.oreganon,
        description = "Hojas muy gruesas, carnosas y aterciopeladas con olor penetrante.",
        uses = "Analgésico local, expectorante y antiespasmódico.",
        preparation = listOf(
            "Oído: Asar hoja en comal hasta que ablande; exprimir 1-2 gotas tibias en el oído.",
            "Tos: Hervir 2 hojas por taza y endulzar con miel."
        ),
        contraindications = "No aplicar en oídos con tímpano perforado o supuración.",
        videoUrl = "https://www.youtube.com/embed/3V54KcHE7Yw",
        cardColor = Color(0xFFF1F8E9)
    ),
    MedicinalPlant(
        name = "Albahaca",
        scientificName = "Ocimum basilicum",
        imageRes = R.drawable.albahaca,
        description = "Hierba aromática de hojas verdes ovaladas, fundamental en medicina tradicional.",
        uses = "Carminativo, digestivo y relajante del sistema nervioso.",
        preparation = listOf(
            "Recolectar 1 cucharada de hojas frescas.",
            "Verter agua hirviendo (250ml), apagar y tapar inmediatamente.",
            "Reposar 10 min y colar antes de beber."
        ),
        contraindications = "Evitar exceso de aceite esencial puro en embarazadas y niños.",
        videoUrl = "https://www.youtube.com/embed/edXGwHoEJmc",
        cardColor = Color(0xFFE0F2F1)
    ),
    MedicinalPlant(
        name = "Guácimo",
        scientificName = "Guazuma ulmifolia",
        imageRes = R.drawable.guacimo,
        description = "Árbol de corteza rugosa y frutos leñosos que liberan mucílago.",
        uses = "Antidiarréico, control de glucosa y fortalecedor capilar.",
        preparation = listOf(
            "Interno: Hervir 10g de corteza en 1L de agua por 15 min.",
            "Capilar: Remojar frutos machacados toda la noche; usar el agua como enjuague."
        ),
        contraindications = "Monitorear glucosa si se usa con medicamentos para diabetes.",
        videoUrl = "https://www.youtube.com/embed/RWoNpkTFiHo",
        cardColor = Color(0xFFFFF8E1)
    )
)

// ─────────────────────────────────────────────────────────────
//  LOOKUP
// ─────────────────────────────────────────────────────────────

fun findPlantByName(name: String): MedicinalPlant =
    tabascoPlants.firstOrNull { it.name == name } ?: tabascoPlants.first()

// ─────────────────────────────────────────────────────────────
//  SCREEN
// ─────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlantsActivityScreen(
    plantName: String = tabascoPlants.first().name,
    onBack: () -> Unit = {},
    onVideoClick: (String) -> Unit = {}
) {
    val plant = findPlantByName(plantName)

    Scaffold(
        containerColor = SurfaceColor,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = plant.name,
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Herbolaria Medicinal",
                style = MaterialTheme.typography.labelSmall.copy(
                    letterSpacing = 1.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryGreen
                )
            )

            PlantCard(plant = plant, onVideoClick = onVideoClick)

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

// ─────────────────────────────────────────────────────────────
//  PLANT CARD
// ─────────────────────────────────────────────────────────────

@Composable
fun PlantCard(plant: MedicinalPlant, onVideoClick: (String) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(3.dp)
    ) {
        Column {
            // Header con imagen real
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                androidx.compose.foundation.Image(
                    painter = painterResource(id = plant.imageRes),
                    contentDescription = plant.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            androidx.compose.ui.graphics.Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.Black.copy(alpha = 0.55f)
                                )
                            )
                        )
                        .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                )
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp)
                ) {
                    Text(
                        text = plant.name,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                    Text(
                        text = plant.scientificName,
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontStyle = FontStyle.Italic,
                            color = Color.White.copy(alpha = 0.85f)
                        )
                    )
                }
            }

            // Contenido
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {

                // Descripción
                Text(
                    text = plant.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                // Usos
                InfoBlock(label = "USOS / PROPIEDADES", content = plant.uses, labelColor = PrimaryGreen)

                // Preparación
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "PREPARACIÓN",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                    plant.preparation.forEachIndexed { index, step ->
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.padding(top = 2.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(20.dp)
                                    .clip(CircleShape)
                                    .background(PrimaryContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "${index + 1}",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = PrimaryGreen
                                    )
                                )
                            }
                            Text(
                                text = step,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }

                // Contraindicaciones
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFFFF3E0))
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = null,
                        tint = Color(0xFFE76F51),
                        modifier = Modifier.size(18.dp)
                    )
                    Column {
                        Text(
                            text = "Contraindicaciones",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF7C2D12)
                            )
                        )
                        Text(
                            text = plant.contraindications,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color(0xFF9A3412)
                            ),
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }
                }

                // Botón ver video
                Button(
                    onClick = { onVideoClick(plant.videoUrl) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Ver video demostrativo",
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold)
                    )
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────
//  HELPER
// ─────────────────────────────────────────────────────────────

@Composable
private fun InfoBlock(label: String, content: String, labelColor: Color) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
        Text(
            text = content,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

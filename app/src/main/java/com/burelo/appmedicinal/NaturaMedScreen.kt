package com.burelo.appmedicinal.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.FavoriteBorder
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

data class Category(val emoji: String, val label: String, val bgColor: Color)
data class FavoritePlant(val name: String, val category: String)
data class Plant(val name: String, val description: String, val rating: Float, val isFavorite: Boolean = false)

// ─────────────────────────────────────────────────────────────
//  SAMPLE DATA
// ─────────────────────────────────────────────────────────────

val sampleCategories = listOf(
    Category("🤕", "Dolor",     Color(0xFFFFF3E0)),
    Category("😴", "Sueño",     Color(0xFFE3F2FD)),
    Category("🫁", "Digestión", Color(0xFFFCE4EC)),
    Category("💪", "Energía",   Color(0xFFFFFDE7)),
    Category("🧠", "Estrés",    Color(0xFFF3E5F5)),
    Category("🌡️", "Resfriado", Color(0xFFFFEBEE)),
)

val sampleFavorites = listOf(
    FavoritePlant("Menta", "Digestión"),
    FavoritePlant("Tila",  "Relajación"),
)

val samplePlants = listOf(
    Plant("Manzanilla", "Calmante natural",  4.8f, isFavorite = true),
    Plant("Valeriana",  "Mejora el sueño",   4.5f),
    Plant("Jengibre",   "Antiinflamatorio",  4.9f),
)

// ─────────────────────────────────────────────────────────────
//  COLORS
// ─────────────────────────────────────────────────────────────

val PrimaryGreen     = Color(0xFF2D6C48)
val PrimaryContainer = Color(0xFFBBF7CE)
val SurfaceColor     = Color(0xFFF6F9F5)

// ─────────────────────────────────────────────────────────────
//  ROOT SCREEN
// ─────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NaturaMedScreen(
    onSearch: (String) -> Unit = {},
    onPlantClick: (String) -> Unit = {}
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    var searchQuery by remember { mutableStateOf("") }

    Scaffold(
        containerColor = SurfaceColor,
        bottomBar = {
            NaturaMedBottomNav(selectedTab = selectedTab, onTabSelected = { selectedTab = it })
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            AppHeader()
            GreetingText()
            SearchBarSection(
                query = searchQuery,
                onQueryChange = { searchQuery = it },
                onSearch = onSearch
            )
            CategoriesSection()
            RecentFavoritesSection()
            PlantsOfMonthSection(onPlantClick = onPlantClick)
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

// ─────────────────────────────────────────────────────────────
//  HEADER
// ─────────────────────────────────────────────────────────────

@Composable
fun AppHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp)
            .statusBarsPadding(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(44.dp).clip(CircleShape).background(PrimaryContainer),
            contentAlignment = Alignment.Center
        ) { Text(text = "🌿", fontSize = 20.sp) }

        Text(
            text = "NaturaMed",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold, color = PrimaryGreen),
            modifier = Modifier.weight(1f).padding(start = 10.dp)
        )

        Box(
            modifier = Modifier.size(42.dp).clip(CircleShape).background(PrimaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Person, contentDescription = "Avatar", tint = PrimaryGreen, modifier = Modifier.size(24.dp))
        }
    }
}

// ─────────────────────────────────────────────────────────────
//  GREETING
// ─────────────────────────────────────────────────────────────

@Composable
fun GreetingText() {
    Text(
        text = "¡Hola, Juan! ¿Cómo te sientes hoy?",
        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.SemiBold),
        modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
    )
}

// ─────────────────────────────────────────────────────────────
//  SEARCH BAR
// ─────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBarSection(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit = {}
) {
    var active by remember { mutableStateOf(false) }

    SearchBar(
        query = query,
        onQueryChange = onQueryChange,
        onSearch = { q -> active = false; onSearch(q) },
        active = active,
        onActiveChange = { active = it },
        placeholder = {
            Text("¿Qué síntoma tienes? Ej: dolor de cabeza",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
        },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Buscar") },
        trailingIcon = { IconButton(onClick = {}) { Text("📷", fontSize = 20.sp) } },
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
        tonalElevation = 2.dp
    ) { }
}

// ─────────────────────────────────────────────────────────────
//  CATEGORIES
// ─────────────────────────────────────────────────────────────

@Composable
fun CategoriesSection() {
    Column(modifier = Modifier.padding(top = 16.dp)) {
        Text(
            text = "CATEGORÍAS POPULARES",
            style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 1.5.sp, fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
        )
        Row(
            modifier = Modifier.horizontalScroll(rememberScrollState()).padding(horizontal = 24.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            sampleCategories.forEach { CategoryItem(category = it) }
        }
    }
}

@Composable
fun CategoryItem(category: Category) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(72.dp).clickable { }
    ) {
        Card(
            modifier = Modifier.size(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = category.bgColor),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = category.emoji, fontSize = 26.sp)
            }
        }
        Text(
            text = category.label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1,
            modifier = Modifier.padding(top = 6.dp)
        )
    }
}

// ─────────────────────────────────────────────────────────────
//  RECENT FAVORITES
// ─────────────────────────────────────────────────────────────

@Composable
fun RecentFavoritesSection() {
    Column(modifier = Modifier.padding(horizontal = 24.dp, vertical = 24.dp)) {
        Text(
            text = "Favoritos Recientes",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 12.dp)
        )
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            sampleFavorites.forEach { FavoriteCard(plant = it, modifier = Modifier.weight(1f)) }
        }
    }
}

@Composable
fun FavoriteCard(plant: FavoritePlant, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.clickable { },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(44.dp).clip(RoundedCornerShape(12.dp)).background(PrimaryContainer),
                contentAlignment = Alignment.Center
            ) { Text("🌿", fontSize = 20.sp) }
            Column(modifier = Modifier.padding(start = 10.dp)) {
                Text(plant.name, style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold))
                Text(plant.category, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(top = 2.dp))
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────
//  PLANTS OF THE MONTH
// ─────────────────────────────────────────────────────────────

@Composable
fun PlantsOfMonthSection(onPlantClick: (String) -> Unit = {}) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Plantas del Mes", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), modifier = Modifier.weight(1f))
            TextButton(onClick = {}) { Text("Ver todo", color = PrimaryGreen) }
        }
        Row(
            modifier = Modifier.horizontalScroll(rememberScrollState()).padding(horizontal = 24.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            samplePlants.forEach { PlantCard(plant = it, onClick = { onPlantClick(it.name) }) }
        }
    }
}

@Composable
fun PlantCard(plant: Plant, onClick: () -> Unit = {}) {
    var isFav by remember { mutableStateOf(plant.isFavorite) }

    Card(
        modifier = Modifier.width(180.dp).clickable { onClick() },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(3.dp)
    ) {
        Column {
            Box(modifier = Modifier.fillMaxWidth().height(128.dp).background(PrimaryContainer)) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("🌿", fontSize = 48.sp) }
                Card(
                    modifier = Modifier.align(Alignment.TopEnd).padding(8.dp).size(34.dp).clickable { isFav = !isFav },
                    shape = CircleShape,
                    colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.85f)),
                    elevation = CardDefaults.cardElevation(0.dp)
                ) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = if (isFav) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = "Favorito",
                            tint = if (isFav) Color(0xFFE53935) else Color.Gray,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
            Column(modifier = Modifier.padding(16.dp)) {
                Text(plant.name, style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold))
                Text(plant.description, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(top = 4.dp))
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 10.dp)) {
                    Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFF9A825), modifier = Modifier.size(14.dp))
                    Text(plant.rating.toString(), style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), modifier = Modifier.padding(start = 4.dp))
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────
//  BOTTOM NAVIGATION
// ─────────────────────────────────────────────────────────────

@Composable
fun NaturaMedBottomNav(selectedTab: Int, onTabSelected: (Int) -> Unit) {
    NavigationBar(containerColor = MaterialTheme.colorScheme.surface, tonalElevation = 8.dp) {
        listOf(
            Triple(Icons.Default.Home,   Icons.Default.Home,                "Inicio"),
            Triple(Icons.Default.Search, Icons.Default.Search,              "Buscar"),
            Triple(Icons.Outlined.FavoriteBorder, Icons.Filled.Favorite,   "Favoritos"),
            Triple(Icons.Default.Person, Icons.Default.Person,              "Perfil"),
        ).forEachIndexed { index, (outlinedIcon, filledIcon, label) ->
            NavigationBarItem(
                selected = selectedTab == index,
                onClick = { onTabSelected(index) },
                icon = { Icon(if (selectedTab == index) filledIcon else outlinedIcon, contentDescription = label) },
                label = { Text(label) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = PrimaryGreen,
                    selectedTextColor = PrimaryGreen,
                    indicatorColor = PrimaryContainer
                )
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────
//  PREVIEW
// ─────────────────────────────────────────────────────────────

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun NaturaMedScreenPreview() {
    MaterialTheme { NaturaMedScreen() }
}

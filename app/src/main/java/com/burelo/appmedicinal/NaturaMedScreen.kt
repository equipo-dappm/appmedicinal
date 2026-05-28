package com.burelo.appmedicinal.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.burelo.appmedicinal.R

// ─────────────────────────────────────────────────────────────
//  DATA MODELS
// ─────────────────────────────────────────────────────────────

data class HomePlant(
    val name: String,
    val description: String,
    val imageRes: Int,
)

val homePlants = listOf(
    HomePlant("Maguey Morado", "Antiséptico y antibacteriano",   R.drawable.maguey_morado),
    HomePlant("Oreganón",      "Analgésico y expectorante",      R.drawable.oreganon),
    HomePlant("Albahaca",      "Digestivo y relajante nervioso", R.drawable.albahaca),
    HomePlant("Guácimo",       "Antidiarréico y capilar",        R.drawable.guacimo),
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

@Composable
fun NaturaMedScreen(
    onSearch: (String) -> Unit = {},
    onPlantClick: (String) -> Unit = {}
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    var searchQuery by remember { mutableStateOf("") }
    val favoriteNames = remember { mutableStateListOf<String>() }

    Scaffold(
        containerColor = SurfaceColor,
        bottomBar = {
            NaturaMedBottomNav(
                selectedTab = selectedTab,
                onTabSelected = { index ->
                    selectedTab = index
                }
            )
        }
    ) { innerPadding ->
        when (selectedTab) {
            0 -> HomeTab(
                innerPadding = innerPadding,
                searchQuery = searchQuery,
                onQueryChange = { searchQuery = it },
                onSearch = onSearch,
                favoriteNames = favoriteNames,
                onPlantClick = onPlantClick,
                onToggleFavorite = { name ->
                    if (name in favoriteNames) favoriteNames.remove(name)
                    else favoriteNames.add(name)
                }
            )
            1 -> FavoritesTab(
                innerPadding = innerPadding,
                favoriteNames = favoriteNames,
                onPlantClick = onPlantClick,
                onToggleFavorite = { name ->
                    if (name in favoriteNames) favoriteNames.remove(name)
                    else favoriteNames.add(name)
                }
            )
            2 -> ProfileTab(innerPadding = innerPadding)
        }
    }
}

// ─────────────────────────────────────────────────────────────
//  HOME TAB
// ─────────────────────────────────────────────────────────────

@Composable
private fun HomeTab(
    innerPadding: PaddingValues,
    searchQuery: String,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    favoriteNames: List<String>,
    onPlantClick: (String) -> Unit,
    onToggleFavorite: (String) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .verticalScroll(rememberScrollState())
    ) {
        AppHeader()
        GreetingText()
        SearchField(
            query = searchQuery,
            onQueryChange = onQueryChange,
            onSearch = onSearch
        )
        if (favoriteNames.isNotEmpty()) {
            RecentFavoritesSection(
                favoriteNames = favoriteNames,
                onPlantClick = onPlantClick
            )
        }
        MedicinalPlantsSection(
            onPlantClick = onPlantClick,
            favoriteNames = favoriteNames,
            onToggleFavorite = onToggleFavorite
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
}

// ─────────────────────────────────────────────────────────────
//  FAVORITES TAB
// ─────────────────────────────────────────────────────────────

@Composable
private fun FavoritesTab(
    innerPadding: PaddingValues,
    favoriteNames: List<String>,
    onPlantClick: (String) -> Unit,
    onToggleFavorite: (String) -> Unit,
) {
    val favoritePlants = homePlants.filter { it.name in favoriteNames }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Mis Favoritos",
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)
        )

        if (favoritePlants.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(48.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "💚", fontSize = 48.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Aún no tienes favoritos",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Toca el corazón en las plantas para guardarlas aquí",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        } else {
            Column(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                favoritePlants.forEach { plant ->
                    FavoritePlantCard(
                        plant = plant,
                        isFavorite = true,
                        onClick = { onPlantClick(plant.name) },
                        onToggleFavorite = { onToggleFavorite(plant.name) }
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

// ─────────────────────────────────────────────────────────────
//  PROFILE TAB
// ─────────────────────────────────────────────────────────────

@Composable
private fun ProfileTab(innerPadding: PaddingValues) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(48.dp))
        Box(
            modifier = Modifier.size(80.dp).clip(CircleShape).background(PrimaryContainer),
            contentAlignment = Alignment.Center
        ) { Text(text = "🌿", fontSize = 36.sp) }

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "NaturaMed",
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold, color = PrimaryGreen)
        )
        Text(
            text = "Tu guía de plantas medicinales",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 4.dp)
        )
        Spacer(modifier = Modifier.height(32.dp))

        Card(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "Acerca de",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "NaturaMed es una aplicación educativa sobre herbolaria medicinal tradicional de México, con información sobre propiedades, usos y preparación de plantas medicinales.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
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
//  SEARCH FIELD
// ─────────────────────────────────────────────────────────────

@Composable
fun SearchField(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit = {}
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        placeholder = {
            Text("¿Qué síntoma tienes? Ej: dolor de cabeza",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
        },
        leadingIcon = {
            Icon(Icons.Default.Search, contentDescription = "Buscar")
        },
        trailingIcon = {
            if (query.isNotBlank()) {
                IconButton(onClick = { onSearch(query) }) {
                    Icon(Icons.Default.Search, contentDescription = "Buscar")
                }
            }
        },
        singleLine = true,
        shape = RoundedCornerShape(28.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = PrimaryGreen,
            unfocusedBorderColor = Color.Transparent,
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
    )
}

// ─────────────────────────────────────────────────────────────
//  RECENT FAVORITES
// ─────────────────────────────────────────────────────────────

@Composable
fun RecentFavoritesSection(
    favoriteNames: List<String>,
    onPlantClick: (String) -> Unit = {}
) {
    val favoritePlants = homePlants.filter { it.name in favoriteNames }

    Column(modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Favoritos Recientes",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "Ver todos →",
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold, color = PrimaryGreen),
                modifier = Modifier.clickable { }
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            favoritePlants.take(2).forEach { plant ->
                FavoriteCard(
                    plantName = plant.name,
                    description = plant.description,
                    modifier = Modifier.weight(1f),
                    onClick = { onPlantClick(plant.name) }
                )
            }
        }
    }
}

@Composable
fun FavoriteCard(
    plantName: String,
    description: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = modifier.clickable { onClick() },
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
                Text(plantName, style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold))
                Text(description, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1, modifier = Modifier.padding(top = 2.dp))
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────
//  PLANTAS MEDICINALES
// ─────────────────────────────────────────────────────────────

@Composable
fun MedicinalPlantsSection(
    onPlantClick: (String) -> Unit = {},
    favoriteNames: List<String> = emptyList(),
    onToggleFavorite: (String) -> Unit = {}
) {
    Column(modifier = Modifier.padding(bottom = 8.dp)) {
        Text(
            text = "Plantas Medicinales",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
        )
        Column(
            modifier = Modifier
                .padding(horizontal = 24.dp, vertical = 4.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            homePlants.chunked(2).forEach { pair ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    pair.forEach { plant ->
                        HomePlantCard(
                            plant = plant,
                            isFavorite = plant.name in favoriteNames,
                            onClick = { onPlantClick(plant.name) },
                            onToggleFavorite = { onToggleFavorite(plant.name) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    if (pair.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────
//  FAVORITE PLANT CARD (for Favorites tab)
// ─────────────────────────────────────────────────────────────

@Composable
fun FavoritePlantCard(
    plant: HomePlant,
    isFavorite: Boolean,
    onClick: () -> Unit = {},
    onToggleFavorite: () -> Unit = {}
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(3.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(64.dp).clip(RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                androidx.compose.foundation.Image(
                    painter = painterResource(id = plant.imageRes),
                    contentDescription = plant.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(12.dp))
                )
            }
            Column(
                modifier = Modifier.weight(1f).padding(start = 12.dp)
            ) {
                Text(
                    text = plant.name,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = plant.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
            IconButton(onClick = onToggleFavorite) {
                Icon(
                    imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                    contentDescription = "Favorito",
                    tint = if (isFavorite) Color(0xFFE53935) else Color.Gray
                )
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────
//  HOME PLANT CARD
// ─────────────────────────────────────────────────────────────

@Composable
fun HomePlantCard(
    plant: HomePlant,
    isFavorite: Boolean = false,
    onClick: () -> Unit = {},
    onToggleFavorite: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.clickable { onClick() },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(3.dp)
    ) {
        Column {
            Box(modifier = Modifier.fillMaxWidth().height(128.dp)) {
                androidx.compose.foundation.Image(
                    painter = painterResource(id = plant.imageRes),
                    contentDescription = plant.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                )
                Card(
                    modifier = Modifier.align(Alignment.TopEnd).padding(8.dp).size(34.dp).clickable { onToggleFavorite() },
                    shape = CircleShape,
                    colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.85f)),
                    elevation = CardDefaults.cardElevation(0.dp)
                ) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = "Favorito",
                            tint = if (isFavorite) Color(0xFFE53935) else Color.Gray,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
            Column(modifier = Modifier.padding(16.dp)) {
                Text(plant.name, style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold))
                Text(plant.description, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(top = 4.dp))
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
            Triple(Icons.Default.Home,   Icons.Default.Home,              "Inicio"),
            Triple(Icons.Outlined.FavoriteBorder, Icons.Filled.Favorite, "Favoritos"),
            Triple(Icons.Default.Person, Icons.Default.Person,            "Perfil"),
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
//  PREVIEWS
// ─────────────────────────────────────────────────────────────

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun NaturaMedScreenPreview() {
    MaterialTheme { NaturaMedScreen() }
}

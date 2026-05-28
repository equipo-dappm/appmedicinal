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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.burelo.appmedicinal.data.FavoritesRepository
import com.burelo.appmedicinal.data.Planta
import com.burelo.appmedicinal.data.PlantasRepository
import com.burelo.appmedicinal.viewmodel.HomeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

val PrimaryGreen     = Color(0xFF2D6C48)
val PrimaryContainer = Color(0xFFBBF7CE)
val SurfaceColor     = Color(0xFFF6F9F5)

@Composable
fun NaturaMedScreen(
    viewModel: HomeViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    onSearch: (String) -> Unit = {},
    onPlantClick: (String) -> Unit = {}
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    var searchQuery by remember { mutableStateOf("") }
    val favoriteNames = remember { mutableStateListOf<String>() }
    val context = LocalContext.current
    val favRepo = remember { FavoritesRepository(context) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        favRepo.favoritesFlow.collect { names ->
            favoriteNames.clear()
            favoriteNames.addAll(names)
        }
    }

    fun onToggle(name: String) {
        scope.launch {
            favRepo.toggle(name)
        }
    }

    Scaffold(
        containerColor = SurfaceColor,
        bottomBar = {
            NaturaMedBottomNav(
                selectedTab = selectedTab,
                onTabSelected = { index -> selectedTab = index }
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
                onToggleFavorite = { onToggle(it) },
                plantas = viewModel.plantas,
                isLoading = viewModel.isLoading,
                error = viewModel.error
            )
            1 -> FavoritesTab(
                innerPadding = innerPadding,
                favoriteNames = favoriteNames,
                onPlantClick = onPlantClick,
                onToggleFavorite = { onToggle(it) }
            )
            2 -> ProfileTab(innerPadding = innerPadding)
        }
    }
}

@Composable
private fun HomeTab(
    innerPadding: PaddingValues,
    searchQuery: String,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    favoriteNames: List<String>,
    onPlantClick: (String) -> Unit,
    onToggleFavorite: (String) -> Unit,
    plantas: List<Planta>,
    isLoading: Boolean,
    error: String?,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .verticalScroll(rememberScrollState())
    ) {
        AppHeader()
        GreetingText()
        SearchField(query = searchQuery, onQueryChange = onQueryChange, onSearch = onSearch)
        if (favoriteNames.isNotEmpty()) {
            RecentFavoritesSection(
                favoriteNames = favoriteNames,
                onPlantClick = onPlantClick
            )
        }
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxWidth().padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = PrimaryGreen)
            }
        } else if (error != null) {
            Box(
                modifier = Modifier.fillMaxWidth().padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(error, color = MaterialTheme.colorScheme.error)
            }
        } else {
            MedicinalPlantsSection(
                plantas = plantas,
                onPlantClick = onPlantClick,
                favoriteNames = favoriteNames,
                onToggleFavorite = onToggleFavorite
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun FavoritesTab(
    innerPadding: PaddingValues,
    favoriteNames: List<String>,
    onPlantClick: (String) -> Unit,
    onToggleFavorite: (String) -> Unit,
) {
    var favoritedPlantas by remember { mutableStateOf<List<Planta>>(emptyList()) }
    var favLoading by remember { mutableStateOf(false) }

    LaunchedEffect(favoriteNames.toList()) {
        if (favoriteNames.isNotEmpty()) {
            favLoading = true
            withContext(Dispatchers.IO) {
                favoritedPlantas = PlantasRepository().getPlantasByNames(favoriteNames)
            }
            favLoading = false
        } else {
            favoritedPlantas = emptyList()
        }
    }

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

        if (favLoading) {
            Box(
                modifier = Modifier.fillMaxWidth().padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = PrimaryGreen)
            }
        } else if (favoriteNames.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxWidth().padding(48.dp),
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
                favoritedPlantas.forEach { plant ->
                    HomePlantCard(
                        plant = plant,
                        isFavorite = true,
                        onClick = { onPlantClick(plant.nombre_comun) },
                        onToggleFavorite = { onToggleFavorite(plant.nombre_comun) }
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

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

@Composable
fun GreetingText() {
    Text(
        text = "¡Hola! ¿Cómo te sientes hoy?",
        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.SemiBold),
        modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
    )
}

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
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Buscar") },
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
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
    )
}

@Composable
fun RecentFavoritesSection(
    favoriteNames: List<String>,
    onPlantClick: (String) -> Unit = {}
) {
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
            favoriteNames.take(2).forEach { name ->
                FavoriteCard(
                    plantName = name,
                    modifier = Modifier.weight(1f),
                    onClick = { onPlantClick(name) }
                )
            }
        }
    }
}

@Composable
fun FavoriteCard(
    plantName: String,
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
            }
        }
    }
}

@Composable
fun MedicinalPlantsSection(
    plantas: List<Planta>,
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
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 4.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            plantas.chunked(2).forEach { pair ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    pair.forEach { plant ->
                        HomePlantCard(
                            plant = plant,
                            isFavorite = plant.nombre_comun in favoriteNames,
                            onClick = { onPlantClick(plant.nombre_comun) },
                            onToggleFavorite = { onToggleFavorite(plant.nombre_comun) },
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

@Composable
fun HomePlantCard(
    plant: Planta,
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
                if (!plant.imagen_url.isNullOrBlank()) {
                    AsyncImage(
                        model = plant.imagen_url,
                        contentDescription = plant.nombre_comun,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                    )
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize().background(PrimaryContainer).clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)),
                        contentAlignment = Alignment.Center
                    ) { Text("🌿", fontSize = 40.sp) }
                }
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
                Text(plant.nombre_comun, style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold))
                plant.descripcion_uso?.let { desc ->
                    Text(
                        text = desc.take(60) + if (desc.length > 60) "..." else "",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 4.dp),
                        maxLines = 2
                    )
                }
            }
        }
    }
}

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

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun NaturaMedScreenPreview() {
    MaterialTheme { NaturaMedScreen() }
}

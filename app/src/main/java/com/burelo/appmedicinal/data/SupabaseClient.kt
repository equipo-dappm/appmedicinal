package com.burelo.appmedicinal.data

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage

object SupabaseClient {
    private const val SUPABASE_URL = "https://camujzaqqfqhwtxkqmdm.supabase.co"
    private const val SUPABASE_ANON_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImNhbXVqemFxcWZxaHd0eGtxbWRtIiwicm9sZSI6ImFub24iLCJpYXQiOjE3Nzk5Mjc0MTIsImV4cCI6MjA5NTUwMzQxMn0.VrjZFXMhGSepUFdzZnzwZBvsUnKTJ1hBuy-EPLEsloU"

    val client = createSupabaseClient(
        supabaseUrl = SUPABASE_URL,
        supabaseKey = SUPABASE_ANON_KEY
    ) {
        install(Postgrest)
        install(Storage)
    }
}

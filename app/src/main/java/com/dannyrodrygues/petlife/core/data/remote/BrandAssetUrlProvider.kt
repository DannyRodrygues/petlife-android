package com.dannyrodrygues.petlife.core.data.remote

import io.github.jan.supabase.storage.storage

object BrandAssetUrlProvider {

    private const val BRANDING_BUCKET =
        "tenant-branding"

    fun getPublicUrl(
        path: String?,
    ): String? {

        if (path.isNullOrBlank()) {
            return null
        }

        return SupabaseProvider
            .client
            .storage
            .from(BRANDING_BUCKET)
            .publicUrl(path)
    }
}
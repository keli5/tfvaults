package io.github.keli5.tfvaults.data

import kotlinx.serialization.Serializable

@Serializable
data class PersonalVault(
    val owner: String,         // UUID as string, UUID has no serializer...
    val items: List<ByteArray?> // Pre-serialized ItemStacks
)
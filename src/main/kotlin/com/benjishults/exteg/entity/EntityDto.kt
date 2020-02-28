package com.benjishults.exteg.entity

data class EntityDto(
        val type: String,
        val case: String,
        val options: List<String> = emptyList()
)

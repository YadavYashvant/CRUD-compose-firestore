package com.example.crud_compose_firestore.presentation.models

data class User(
    val name: String?,
    val branch: String?,
    val skill: String?
) {
    constructor() : this("Not added yet", "Empty", "Empty")
}
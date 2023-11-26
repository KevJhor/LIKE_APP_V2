package com.example.like_app.model

data class CartModel(
    val orderName: String = "",
    val imageUrl: String = "",
    val quantity: Int = 0,
    val price: String = ""
) {
    // Constructor sin argumentos requerido por Firebase Firestore
    constructor() : this( "", "", 0, "")
}
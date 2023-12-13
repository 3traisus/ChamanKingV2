package com.example.chamanking

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class itemProducto(
    var id: Int,
    var name: String,
    val descripcion: String,
    val tags: String,
    val presentacion: String,
    val cantidad: Int,
    val preVenta: Double,
    val descuento: String,
    val imagen: String,
    val pop: Boolean,
) : Parcelable

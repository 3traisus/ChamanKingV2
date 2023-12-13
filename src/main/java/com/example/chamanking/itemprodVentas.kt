package com.example.chamanking

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class itemprodVentas(
    val id:Int,
    val name:String,
    var precioOriginal:Double,
    val descuentoBase:String,
    var descuentoPersonal: String= "0$",
    val cantidadLimite:Int,
    var cantidad:Int = 1,
    var total: Double
):Parcelable

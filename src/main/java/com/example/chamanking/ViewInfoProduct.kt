package com.example.chamanking

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.chamanking.databinding.InfoproductoBinding

class ViewInfoProduct:AppCompatActivity() {
    private lateinit var binding: InfoproductoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = InfoproductoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val lista: ArrayList<itemProducto>?= intent.getParcelableArrayListExtra("listaProductos")
        val lista2: ArrayList<itemprodVentas>?= intent.getParcelableArrayListExtra("listaProductos2")
        println("entra"+lista2)
        val elemento: itemProducto? = intent.getParcelableExtra("elemento")
        if(elemento!=null) {
            binding.infoTxtNombre.text = elemento.name
            binding.infoTxtDesc.text = elemento.descripcion
            binding.infoTxtPresentacion.text = elemento.presentacion
            Glide
                .with(binding.root.context)
                .load(elemento.imagen)
                .into(binding.infoImgProducto)

            val presentacion = elemento.presentacion.toUpperCase()
            if (presentacion.contains("CAP")){
                Glide
                    .with(binding.root.context)
                    .load(R.mipmap.capsula)
                    .into(binding.imgPresentacion)
            }
            else if (presentacion.contains("TAB")){
                Glide
                    .with(binding.root.context)
                    .load(R.mipmap.pildora)
                    .into(binding.imgPresentacion)
            }
            else if (presentacion.contains("GOTAS")){
                Glide
                    .with(binding.root.context)
                    .load(R.mipmap.gotas)
                    .into(binding.imgPresentacion)
            }
            else if (presentacion.contains("POMADA")||presentacion.contains("GEL")){
                Glide
                    .with(binding.root.context)
                    .load(R.mipmap.pomada)
                    .into(binding.imgPresentacion)
            }
            else if (presentacion.contains("AMP")){
                Glide
                    .with(binding.root.context)
                    .load(R.mipmap.durazno)
                    .into(binding.imgPresentacion)
            }
            else if (presentacion.contains("INY")){
                Glide
                    .with(binding.root.context)
                    .load(R.mipmap.inyeccion)
                    .into(binding.imgPresentacion)
            }
            else if (presentacion.contains("SUP")||presentacion.contains("BEBIBLE")){
                Glide
                    .with(binding.root.context)
                    .load(R.mipmap.proteinas)
                    .into(binding.imgPresentacion)
            }
            else{
                Glide
                    .with(binding.root.context)
                    .load(R.mipmap.farmaco)
                    .into(binding.imgPresentacion)
            }

        }

        val intent = Intent(this, Principal::class.java)
        binding.infoBtnReturn.setOnClickListener {
            println(lista2)
            intent.putExtra("listaProductos",lista)
            intent.putExtra("listaProductos2",lista2)
            startActivity(intent)
        }

        binding.infoBtnAgregar.setOnClickListener {
            intent.putExtra("elemento",elemento)
            showToast("${elemento?.name}->Agregado")
        }

    }

    private fun showToast(message: String) {
        val toast = Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT)
        toast.show()
    }
}
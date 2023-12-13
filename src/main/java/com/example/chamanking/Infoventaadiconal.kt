package com.example.chamanking

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.set
import com.bumptech.glide.Glide
import com.example.chamanking.databinding.InfoVentaAdiconalBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Infoventaadiconal:AppCompatActivity() {
    private var listaprodvent = mutableListOf<itemprodVentas>()
    private var listaprodvent2 = mutableListOf<itemProducto>()
    private val db = Firebase.firestore
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: InfoVentaAdiconalBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        binding = InfoVentaAdiconalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /////////////////////////////////////////////////////////////////////////////////////////
        //Obtenemos los valores pasados de la otra activity
        var listaProductos2: ArrayList<itemProducto>? = intent.getParcelableArrayListExtra("listaProductos")
        var listaProductos: ArrayList<itemprodVentas>? = intent.getParcelableArrayListExtra("listaProductos2")
        if(listaProductos!=null)
            listaprodvent = listaProductos.toMutableList()

        if(listaProductos2!=null)
            listaprodvent2 = listaProductos2.toMutableList()

        ////////////////////////////////////////////////////////////////////////////////

        binding.infoAddTxtDes.setOnClickListener {
            if(binding.infoAddTxtDes.hint == "%")
                binding.infoAddTxtDes.hint = "$"
            else
                binding.infoAddTxtDes.hint = "%"
        }
        paint()

        binding.infoAddBtnIzq.setOnClickListener {
            var ind:Int = intent.getIntExtra("indice",-1)
            var item: itemprodVentas? =  intent.getParcelableExtra("elemento")
            listaprodvent.add(ind,item!!)
            redirectConfirm()
        }

        binding.ventaBtnAceptar.setOnClickListener {
            if(verificar())
            {
                var item: itemprodVentas? =  intent.getParcelableExtra("elemento")
                var ind:Int = intent.getIntExtra("indice",-1)
                println("Indice:"+ind)
                //println(item)
                //println(ind)
                if(item!=null && ind!=-1) {
                    item?.cantidad = binding.infoAddEtCant.text.toString().toInt()
                    item?.descuentoPersonal = binding.infoAddEtDescvalor.text.toString()+binding.infoAddTxtDes.hint.toString()
                    println(item?.descuentoPersonal)
                    if(item?.descuentoPersonal!!.contains("%"))
                    {
                        item.precioOriginal = item.precioOriginal - (item.precioOriginal * item.descuentoPersonal.substring(0,item.descuentoPersonal.length-1).toInt() / 100)
                    }else{
                        item.precioOriginal = item.precioOriginal - item.descuentoPersonal.substring(0,item.descuentoPersonal.length-1).toInt()
                    }
                    item.total = item.precioOriginal * item.cantidad
                }
                listaprodvent.add(ind,item!!)
                redirectConfirm()
            }
        }

    }

    private fun paint(){
        Glide
            .with(binding.root.context)
            .load(intent.getStringExtra("url"))
            .into(binding.infoImgProducto)

        val elemento: itemprodVentas? = intent.getParcelableExtra("elemento")
        //println(elemento)
        if(elemento!=null)
        {
            binding.infoAddTxtNombre.text = elemento?.name
            binding.infoAddTxtPre.text = elemento.precioOriginal.toString()
            binding.infoAddEtCant.setText(elemento.cantidad.toString())
            binding.infoAddTxtCantotal.text = elemento.cantidadLimite.toString()
            val valor = elemento.descuentoPersonal.substring(0,elemento.descuentoPersonal.length-1)
            binding.infoAddEtDescvalor.setText(valor)
        }
    }

    private fun redirectConfirm(){
        val intent = Intent(this, Viewenta::class.java)
        //intent.putExtra("elementoCorregido",item)
        //intent.putExtra("indice",ind)
        println("lista productos"+listaprodvent2)
        println("lista productos venta"+listaprodvent)
        intent.putParcelableArrayListExtra("listaProductos", ArrayList(listaprodvent2))
        intent.putParcelableArrayListExtra("listaProductos2", ArrayList(listaprodvent))
        startActivity(intent)
    }

    private fun verificar():Boolean{
        val elemento: itemprodVentas? = intent.getParcelableExtra("elemento")
        if(binding.infoAddEtCant.text.toString().toInt() > elemento!!.cantidadLimite) {
            binding.infoAddEtCant.setBackgroundColor(Color.RED)
            showToast("Cantidad Excedida")
            return false
        }
        else
            binding.infoAddEtCant.setBackgroundColor(resources.getColor(R.color.datos_white))

        if(binding.infoAddTxtDes.text.contains("%"))
        {
            if(binding.infoAddEtDescvalor.text.toString().toInt() > 100) {
                binding.infoAddEtDescvalor.setBackgroundColor(Color.RED)
                showToast("Producto Gratis??")
                return false
            }
            else
                binding.infoAddEtDescvalor.setBackgroundColor(resources.getColor(R.color.datos_white))
        }
        else{
            if(binding.infoAddEtDescvalor.text.toString().toInt() >= elemento.precioOriginal) {
                binding.infoAddEtDescvalor.setBackgroundColor(Color.RED)
                showToast("Producto Gratis??")
                return false
            }
            else
                binding.infoAddEtDescvalor.setBackgroundColor(resources.getColor(R.color.datos_white))
        }
        return true
    }

    private fun showToast(message: String) {
        val toast = Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT)
        toast.show()
    }

}
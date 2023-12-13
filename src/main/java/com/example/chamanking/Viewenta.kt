package com.example.chamanking

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chamanking.databinding.ViewentaBinding
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.Date
import java.util.HashMap

class Viewenta: AppCompatActivity(),OnclickListener {
    private var listaprodvent = mutableListOf<itemProducto>()
    private var listaprodvent2 = mutableListOf<itemprodVentas>()
    private lateinit var itemadapter:adapterprodVentas
    private var ind = -1
    private val db = Firebase.firestore
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ViewentaBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        binding = ViewentaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /////////////////////////////////////////////////////////////////////////////////////////
        /////////////////////////////Obtenemos los valores pasados de la otra activity
        /////////////////////////////si listaProductos tiene elementos significa que venimos de la activity:Principal
        /////////////////////////////si por otro lado la que tiene info es ListaProductos2 venimos de la vista Infoventaadcional
        var listaProductos: ArrayList<itemProducto>? = intent.getParcelableArrayListExtra("listaProductos")
        var listaProductos2: ArrayList<itemprodVentas>? = intent.getParcelableArrayListExtra("listaProductos2")

        if(listaProductos!=null)
            listaprodvent = listaProductos.toMutableList()
        if(listaProductos2!=null)
            listaprodvent2 = listaProductos2.toMutableList()

        /////////////////////////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////////////////////////
        binding.ventaBtnBorrar.setOnClickListener {
            showToast("Carrito Borrado")
            listaProductos?.clear()
            listaProductos2?.clear()
            listaprodvent2.clear()
            listaprodvent.clear()
            this.onResume()
        }

        binding.ventaImgReturn.setOnClickListener {
            redirectOut(listaProductos,listaProductos2)
            //redirectOut()
        }

        binding.ventaBtnAceptar.setOnClickListener {
            val totalventa = Total()
            val listaproduc = extraerdatos()
            val data = hashMapOf(
                "fecha" to Timestamp(Date()),
                "productos" to listaproduc,
                "total" to totalventa,
                "Empleado" to auth.uid,
            )

            db.collection("Ventas")
                .add(data)
                .addOnSuccessListener { documentReference ->
                    showToast("Venta realizada con exito")
                    Log.d(TAG, "DocumentSnapshot written with ID: ${documentReference.id}")
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding document", e)
                }

            actualizarProd()
            val sfDocRef = db.collection("Caja").document("Unique")
            db.runTransaction { transaction ->
                val snapshot = transaction.get(sfDocRef)
                val caja = snapshot.data!!.get("valor").toString().toInt() + totalventa.toInt()
                transaction.update(sfDocRef,"valor",caja)
            }.addOnSuccessListener { result ->
                showToast("Caja actualizada")
                Log.d(TAG, "Transaction success: $result")
            }.addOnFailureListener { e ->

                Log.w(TAG, "Transaction failure.", e)
            }

        }
    }

    override fun onResume() {
        super.onResume()
        itemadapter = adapterprodVentas(listaprodvent2,this)
        val linearLayoutManager = LinearLayoutManager(this)
        binding.ventaRecycler.apply {
            layoutManager = linearLayoutManager
            adapter = itemadapter
        }

    }

    private fun redirectOut(lista:ArrayList<itemProducto>?,lista2:ArrayList<itemprodVentas>?){
    //private fun redirectOut(){
        val intent = Intent(this, Principal::class.java)
        //intent.putParcelableArrayListExtra("listaProductos", ArrayList(listaprodvent))
        //intent.putParcelableArrayListExtra("listaProductos2", ArrayList(listaprodvent2))
        intent.putParcelableArrayListExtra("listaProductos", ArrayList(listaprodvent))
        intent.putParcelableArrayListExtra("listaProductos2", ArrayList(listaprodvent2))
        //intent.putParcelableArrayListExtra("listaProductos2", ArrayList(listaprodvent2))
        startActivity(intent)
    }

    private fun Transformar(){
        //println("Entro a transformar")
        if (!listaprodvent.isEmpty()) {
            for (producto in listaprodvent) {
                var item = itemprodVentas(
                    id = producto.id,
                    name = producto.name,
                    precioOriginal = producto.preVenta,
                    descuentoBase = producto.descuento,
                    cantidadLimite = producto.cantidad,
                    total = 0.0,
                )
                if(producto.descuento.contains("%"))
                {
                    item.precioOriginal = (item.precioOriginal -  item.precioOriginal * item.descuentoBase.substring(0,item.descuentoBase.length-1).toInt() / 100)
                }else{
                    item.precioOriginal = (item.precioOriginal - item.descuentoBase.substring(0,item.descuentoBase.length-1).toInt())
                }
                item.total = item.precioOriginal * item.cantidad
                listaprodvent2.add(item)
            }
        }
        else{
            println("No entro al transformar")
        }
    }

    private fun showToast(message: String) {
        val toast = Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT)
        toast.show()
    }

    private fun Total():Double{
        var total = 0.0
        for (productos in listaprodvent2){
            total += productos.total
        }
        return total
    }

    private fun actualizarProd(){
        for (producto in listaprodvent2){
            val sfDocRef = db.collection("Productos").document(producto.id.toString())
            db.runTransaction { transaction ->
                val snapshot = transaction.get(sfDocRef)
                val cantidad = snapshot.data?.get("cantidad").toString().toInt() - producto.cantidad
                transaction.update(sfDocRef,"cantidad",cantidad)
            }.addOnSuccessListener { result ->
                showToast("${producto.name} Actualizado")
                Log.d(TAG, "Transaction success: $result")
            }.addOnFailureListener { e ->
                Log.w(TAG, "Transaction failure.", e)
            }
        }
    }

    private fun extraerdatos():MutableList<HashMap<String,Any>>{
        var lista = mutableListOf<HashMap<String, Any>>()
        for (producto in listaprodvent2){
              val data = hashMapOf<String,Any>(
                  "id" to producto.id,
                  "precio" to producto.precioOriginal,
                  "descuentoBase" to producto.descuentoBase,
                  "descuentoPersonal" to producto.descuentoPersonal,
                  "cantidad" to producto.cantidad,
                  "total" to producto.total
              )
            lista.add(data)
        }
        return lista
    }

    override fun onClick(item: itemProducto) {
        TODO("Not yet implemented")
    }

    override fun onClickEvent(item: itemProducto) {
        TODO("Not yet implemented")
    }

    override fun onClickPopular(item: itemProducto) {
        TODO("Not yet implemented")
    }

    override fun onClickprodVent(item: itemprodVentas) {
        ///se nesito mandar el indice para poder insertar en la activity:infoventaAdicional
        //tambien se mando el elemento que se iba a modificar
        //y se mando la lista para que la regrese con el elemento modificado
        //y se mando la imagen para no tener que agregarla al TDA
        ind = listaprodvent2.indexOf(item)
        if (ind != -1) {
            val elementoEliminado = listaprodvent2.removeAt(ind)
            println(ind.toString()+"/"+item)
            val intent = Intent(this, Infoventaadiconal::class.java)
            intent.putExtra("indice",ind)
            intent.putExtra("elemento", item)
            intent.putExtra("url",listaprodvent.get(ind).imagen)
            intent.putParcelableArrayListExtra("listaProductos", ArrayList(listaprodvent))
            intent.putParcelableArrayListExtra("listaProductos2", ArrayList(listaprodvent2))
            startActivity(intent)
        }
    }

    override fun eventopress(item: itemprodVentas) {
        val ind = listaprodvent2.indexOf(item)
        val ele = listaprodvent2.removeAt(ind)
        listaprodvent.removeAt(ind)
        showToast(ele.name+"Eliminado")
        this.onResume()
    }


}

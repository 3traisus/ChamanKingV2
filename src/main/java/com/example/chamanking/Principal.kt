package com.example.chamanking

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chamanking.databinding.PrincipalBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await

class Principal: AppCompatActivity(),OnclickListener {
    private val campos = listOf("id","name","descripcion","tags",
        "presentacion","cantidad","preVenta","descuento","imagen","popular")
    private lateinit var itemprodAdapter:adapterProduct
    private lateinit var itemprodAdapter2:adapterPopular
    private lateinit var linearLayoutManager: RecyclerView.LayoutManager
    private var listaitemProductos = mutableListOf<itemProducto>()
    private var listaitemProdVentas = mutableListOf<itemProducto>()
    private var listaitemProdVentas2 = mutableListOf<itemprodVentas>()
    private var listaitempopulares = mutableListOf<itemProducto>()
    private val db = Firebase.firestore
    private lateinit var auth: FirebaseAuth
    private var filtropopular = ""
    private lateinit var binding: PrincipalBinding
    private var banevento = false
    //private lateinit var listaProductos2: ArrayList<itemprodVentas>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        binding = PrincipalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //binding.prinTxtCaja.text = intent.getStringExtra("clave_string")
        //showToast(intent.getStringExtra("clave_name").toString())


        runBlocking {
            binding.prinTxtCaja.text = "$"+ valorcaja()
        }

        ///////////////////////////////////////////////////////////////
        val listaProductos: ArrayList<itemProducto>? = intent.getParcelableArrayListExtra("listaProductos")
        val listaProductos2:ArrayList<itemprodVentas>? =intent.getParcelableArrayListExtra("listaProductos2")
        //////////////////////////PRINCIPAL////////////////////////////////////////////////////////
        if(listaProductos!=null)
            listaitemProdVentas = listaProductos.toMutableList()

        if(listaProductos2!=null)
            listaitemProdVentas2 = listaProductos2.toMutableList()



        ////////////////////////////////////////////////////////////////

        //////////////////////////////////////////////////////////////
        binding.prinImgSesionOf.setOnClickListener {
            auth.signOut()
            redirectOut()
        }
        //////////////////////////////////////////////////////////////

        //////////////////////////////////////////////////////////////
        binding.prinImgStore.setOnClickListener {
            Transformar()
            redirectSucces(listaProductos2)
        }
        //////////////////////////////////////////////////////////////
        binding.prinImgTableta.setOnClickListener {
            runBlocking {
                listaitemProductos = listaProductos(campos,listaFiltradas("Productos",campos,"presentacion","Tabletas",listOf<String>()))
                filtropopular = ""
                banevento = true
            }
            this.onResume()
        }

        binding.prinImgCapsula.setOnClickListener {
            runBlocking {
                listaitemProductos = listaProductos(campos,listaFiltradas("Productos",campos,"presentacion","Capsulas",listOf<String>()))
                filtropopular = ""
                banevento = true
            }
            this.onResume()
        }

        binding.prinImgGotas.setOnClickListener {
            runBlocking {
                listaitemProductos = listaProductos(campos,listaFiltradas("Productos",campos,"presentacion","Gotas",listOf<String>()))
                filtropopular = ""
                banevento = true
            }
            this.onResume()
        }

        binding.prinImgPomada.setOnClickListener {
            runBlocking {
                listaitemProductos = listaProductos(campos,listaFiltradas("Productos",campos,"presentacion","Pomada",listOf<String>()))
                listaitemProductos.addAll(listaProductos(campos,listaFiltradas("Productos",campos,"presentacion","Gel",listOf<String>())))
                filtropopular = ""
                banevento = true
            }
            this.onResume()
        }

        binding.prinImgAmpolleta.setOnClickListener {
            runBlocking {
                listaitemProductos = listaProductos(campos,listaFiltradas("Productos",campos,"presentacion","Ampolletas",listOf<String>()))
                filtropopular = ""
                banevento = true
            }
            this.onResume()
        }

        binding.prinImgInyec.setOnClickListener {
            runBlocking {
                listaitemProductos = listaProductos(campos,listaFiltradas("Productos",campos,"presentacion","Inyectables",listOf<String>()))
                filtropopular = ""
                banevento = true
            }
            this.onResume()
        }

        binding.prinImgSuplementosBebibles.setOnClickListener {
            runBlocking {
                listaitemProductos = listaProductos(campos,listaFiltradas("Productos",campos,"presentacion","Bebible",listOf<String>()))
                listaitemProductos.addAll(listaProductos(campos,listaFiltradas("Productos",campos,"presentacion","Suplemento",listOf<String>())))
                filtropopular = ""
                banevento = true
            }
            this.onResume()
        }

        binding.prinImgOtros.setOnClickListener {
            runBlocking {
                listaitemProductos = listaProductos(campos,listaFiltradas("Productos",campos,"presentacion","",
                    listOf<String>("Suplemento","Bebible","Inyectable","Ampolletas","Gel","Pomada","Gotas","Capsulas","Tabletas")))
                filtropopular = ""
                banevento = true
            }
            this.onResume()
        }

        binding.prinImgReloj.setOnClickListener {
            runBlocking {
                filtropopular = ""
                banevento = false
            }
            this.onResume()
        }


    }

    override fun onResume() {
        super.onResume()
        runBlocking {
            try {
                listaitempopulares = listaProductos(campos,listaFiltradas("Productos",campos,"popular",true,listOf<String>())

                )

                if(filtropopular==""){
                    if(!banevento)
                        listaitemProductos = listaProductos(campos,listaConsulta("Productos",campos))
                    else
                    {

                    }
                }
                else{
                    listaitemProductos = filtroPopular(filtropopular.toString())
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error getting documents: $e")
            }
        }


        itemprodAdapter = adapterProduct(listaitemProductos,this)
        linearLayoutManager = LinearLayoutManager(this)
        binding.recyclerprod.apply {
            layoutManager = linearLayoutManager
            adapter = itemprodAdapter
        }

        itemprodAdapter2 = adapterPopular(listaitempopulares,this)
        val linearLayoutManager2 = LinearLayoutManager(this,RecyclerView.HORIZONTAL,false)
        binding.recycler.apply {
            layoutManager = linearLayoutManager2
            adapter = itemprodAdapter2
        }
    }

    private suspend fun listaConsulta(collection:String, campos:List<String>):MutableList<Map<String, Any>>{
        var listalista = mutableListOf<Map<String, Any>>()

        val documents = db.collection(collection).get().await()

        for (document in documents) {
            //var listaDatos = mutableListOf<String>()
            var listaDatos = HashMap<String, Any>()

            //Log.d(TAG, "${document.id} => ${document.data}")
            listaDatos[campos[0]] = document.id
            for (i in 1 until campos.size)
            {
                listaDatos[campos[i]] = document.data[campos[i]].toString()
                //listaDatos.add(document.data[campo].toString())
            }
            listalista.add(listaDatos)
        }
        //Log.d(TAG, "Product List: $listainfo")

        return listalista
    }

    private suspend fun listaFiltradas(collection:String, campos:List<String>,field:String,value:Any,lista: List<String>):MutableList<Map<String, Any>>{
        var listalista = mutableListOf<Map<String, Any>>()
        if(lista.isEmpty()) {
            val documents = db.collection(collection).whereEqualTo(field, value).get().await()
            for (document in documents!!) {
                //var listaDatos = mutableListOf<String>()
                var listaDatos = HashMap<String, Any>()

                //Log.d(TAG, "${document.id} => ${document.data}")
                listaDatos[campos[0]] = document.id
                for (i in 1 until campos.size)
                {
                    listaDatos[campos[i]] = document.data[campos[i]].toString()
                    //listaDatos.add(document.data[campo].toString())
                }
                listalista.add(listaDatos)
            }
        }
        else {
            val documents2 = db.collection(collection).whereNotIn(field,lista).get().await()
            for (document in documents2) {
                //var listaDatos = mutableListOf<String>()
                var listaDatos = HashMap<String, Any>()
                //Log.d(TAG, "${document.id} => ${document.data}")
                listaDatos[campos[0]] = document.id
                for (i in 1 until campos.size)
                {
                    listaDatos[campos[i]] = document.data[campos[i]].toString()
                    //listaDatos.add(document.data[campo].toString())
                }
                listalista.add(listaDatos)
            }
        }
        return listalista
    }

    private fun listaProductos(campos:List<String>,lista:MutableList<Map<String, Any>>):
            MutableList<itemProducto>{

        var listaprod = mutableListOf<itemProducto>()
        //val lista = listaConsulta(collection,campos)
        for (ele in lista)
        {
            val item = itemProducto(
                id = ele[campos[0]].toString().toInt(),
                name = ele[campos[1]].toString(),
                descripcion = ele[campos[2]].toString(),
                tags = ele[campos[3]].toString(),
                presentacion = ele[campos[4]].toString(),
                cantidad = ele[campos[5]].toString().toInt(),
                preVenta = ele[campos[6]].toString().toDouble(),
                descuento = ele[campos[7]].toString(),
                imagen = ele[campos[8]].toString(),
                pop = ele[campos[9]].toString().toBoolean()
            )
            listaprod.add(item)
        }
        return listaprod
    }

    private fun showToast(message: String) {
        val toast = Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT)
        toast.show()
    }

    private fun redirectOut(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun redirectSucces(listaProductos2: ArrayList<itemprodVentas>?) {
        val intent = Intent(this, Viewenta::class.java)
        //intent.putParcelableArrayListExtra("listaProductos", ArrayList(listaitemProdVentas))
        //intent.putParcelableArrayListExtra("listaProductos2", listaProductos2)
        intent.putParcelableArrayListExtra("listaProductos", ArrayList(listaitemProdVentas))
        intent.putParcelableArrayListExtra("listaProductos2", ArrayList(listaitemProdVentas2))
        startActivity(intent)
    }

    private fun redirectInfo(item:itemProducto){
        val intent = Intent(this, ViewInfoProduct::class.java)
        intent.putExtra("elemento",item)
        intent.putParcelableArrayListExtra("listaProductos", ArrayList(listaitemProdVentas))
        intent.putParcelableArrayListExtra("listaProductos2", ArrayList(listaitemProdVentas2))
        startActivity(intent)
    }

    private suspend fun dataUser(campo:String):String{
        val docRef = db.collection("Empleado").document(auth.uid.toString()).get().await()
        return docRef.data?.get(campo).toString()
    }

    private suspend fun valorcaja():String{
        val docRef = db.collection("Caja").document("Unique").get().await()
        return docRef.data?.get("valor").toString()
    }

    private suspend fun filtroPopular(id:String):MutableList<itemProducto>{
        var listaprod = mutableListOf<itemProducto>()
        val docRef = db.collection("Productos").document(id).get().await()

        val item = itemProducto(
            id = docRef.id.toInt(),
            name = docRef.data?.get(campos[1]).toString(),
            descripcion = docRef.data?.get(campos[2]).toString(),
            tags = docRef.data?.get(campos[3]).toString(),
            presentacion = docRef.data?.get(campos[4]).toString(),
            cantidad = docRef.data?.get(campos[5]).toString().toInt(),
            preVenta = docRef.data?.get(campos[6]).toString().toDouble(),
            descuento = docRef.data?.get(campos[7]).toString(),
            imagen = docRef.data?.get(campos[8]).toString(),
            pop = docRef.data?.get(campos[9]).toString().toBoolean()
        )
        listaprod.add(item)
        return listaprod
    }

    override fun onClick(item: itemProducto) {
        listaitemProdVentas.add(item)
        showToast("${item.name}->Agregado")
    }

    override fun onClickEvent(item: itemProducto) {
        redirectInfo(item)
    }

    override fun onClickPopular(item: itemProducto) {
        showToast("${item.name}->filtro")
        filtropopular = item.id.toString()
        this.onResume()
    }

    override fun onClickprodVent(item: itemprodVentas) {
        TODO("Not yet implemented")
    }

    override fun eventopress(item: itemprodVentas) {
        TODO("Not yet implemented")
    }

    private fun Transformar(){
        //println("Entro a transformar")
        val listaDif = listaitemProdVentas.subList(listaitemProdVentas2.size,listaitemProdVentas.size)
        if (!listaDif.isEmpty()) {
            for (producto in listaDif) {
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
                listaitemProdVentas2.add(item)
            }
        }
        else{
            println("No entro al transformar")
        }
    }
}
package com.example.chamanking


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.chamanking.databinding.ViewproductosBinding

class adapterProduct(private val items: MutableList<itemProducto>,private val listener: OnclickListener):RecyclerView.Adapter<adapterProduct.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.viewproductos,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount():Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder){
            Glide
                .with(binding.root.context)
                .load(items[position].imagen)
                .into(binding.viewprodImg)

            if(!items[position].pop)
            {
                binding.viewprodCctopImg2.isVisible = false
                val layoutParams = binding.viewprodCctopImg1.layoutParams as ViewGroup.MarginLayoutParams
                layoutParams.setMargins(0, 0, 30, 0)
                binding.viewprodCctopImg1.requestLayout()
            }

            binding.viewprodCctopTxt.text = items[position].name
            binding.viewprodCccontentCont.text = items[position].tags
            binding.viewprodCccontentPz.text = "pz"+items[position].cantidad.toString()
            binding.viewprodCccontentPre.text = "$"+items[position].preVenta.toString()
            binding.viewprodBtnAdd.setOnClickListener {
                setListener(items[position])
            }
            binding.viewprodCctop.setOnClickListener {
                setEvent(items[position])
            }
            binding.viewprodCccontent.setOnClickListener {
                setEvent(items[position])
            }

            val presentacion = items[position].presentacion.toUpperCase()
            if (presentacion.contains("CAP")){
                Glide
                    .with(binding.root.context)
                    .load(R.mipmap.capsula)
                    .into(binding.viewprodCctopImg1)
            }
            else if (presentacion.contains("TAB")){
                Glide
                    .with(binding.root.context)
                    .load(R.mipmap.pildora)
                    .into(binding.viewprodCctopImg1)
            }
            else if (presentacion.contains("GOTAS")){
                Glide
                    .with(binding.root.context)
                    .load(R.mipmap.gotas)
                    .into(binding.viewprodCctopImg1)
            }
            else if (presentacion.contains("POMADA")||presentacion.contains("GEL")){
                Glide
                    .with(binding.root.context)
                    .load(R.mipmap.pomada)
                    .into(binding.viewprodCctopImg1)
            }
            else if (presentacion.contains("AMP")){
                Glide
                    .with(binding.root.context)
                    .load(R.mipmap.durazno)
                    .into(binding.viewprodCctopImg1)
            }
            else if (presentacion.contains("INY")){
                Glide
                    .with(binding.root.context)
                    .load(R.mipmap.inyeccion)
                    .into(binding.viewprodCctopImg1)
            }
            else if (presentacion.contains("SUP")||presentacion.contains("BEBIBLE")){
                Glide
                    .with(binding.root.context)
                    .load(R.mipmap.proteinas)
                    .into(binding.viewprodCctopImg1)
            }
            else{
                Glide
                    .with(binding.root.context)
                    .load(R.mipmap.farmaco)
                    .into(binding.viewprodCctopImg1)
            }


        }
    }

    inner class ViewHolder(view: View):RecyclerView.ViewHolder(view) {
        val binding = ViewproductosBinding.bind(view)


        fun setListener(item:itemProducto){
            listener.onClick(item)
        }

        fun setEvent(item:itemProducto){
            listener.onClickEvent(item)
        }


    }

}
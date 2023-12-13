package com.example.chamanking


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.chamanking.databinding.PrincipalBinding
import com.example.chamanking.databinding.ViewpopularBinding
import com.example.chamanking.databinding.ViewproductosBinding

class adapterPopular(private val items: MutableList<itemProducto>,private val listener: OnclickListener):RecyclerView.Adapter<adapterPopular.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.viewpopular,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int= items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder){
            setListener(items[position])
            Glide
                .with(binding.root.context)
                .load(items[position].imagen)
                .into(binding.img)
        }
    }

    inner class ViewHolder(view: View):RecyclerView.ViewHolder(view) {
        val binding = ViewpopularBinding.bind(view)

        fun setListener(item:itemProducto){
            binding.root.setOnClickListener {
                listener.onClickPopular(item)
            }
        }
    }
}
package com.example.chamanking

import android.os.Handler
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.chamanking.databinding.VentadatosBinding

class adapterprodVentas(private val items: MutableList<itemprodVentas>,private val listener: OnclickListener):RecyclerView.Adapter<adapterprodVentas.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): adapterprodVentas.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.ventadatos,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: adapterprodVentas.ViewHolder, position: Int) {
        with(holder){
            binding.datosTxtnombre.text = items[position].name
            binding.datosTxtprecio.text = items[position].precioOriginal.toString()
            binding.datosTxtcantidad.text = items[position].cantidad.toString()
            //binding.datosTxtsubtotal.text = Subtotal(items[position].total , items[position].cantidad ).toString()
            binding.datosTxtsubtotal.text = items[position].total.toString()
            press(items[position])
        }
    }

    override fun getItemCount(): Int= items.size

    inner class ViewHolder(view: View):RecyclerView.ViewHolder(view) {
        val binding = VentadatosBinding.bind(view)
        private val handler = Handler()

        fun press(item: itemprodVentas) {
            //selectedElementId = item.id
            binding.Container.setOnLongClickListener {
                handler.postDelayed({
                    listener?.eventopress(item)
                }, 5000) // 5000 milisegundos (5 segundos)
                true  // Devuelve true para indicar que el evento ha sido manejado
            }

            binding.Container.setOnClickListener {
                handler.removeCallbacksAndMessages(null) // Cancela todas las ejecuciones pendientes del handler
                listener?.onClickprodVent(item)
            }
        }
    }
}
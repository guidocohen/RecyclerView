package com.guido.recyclerview

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.template_plato.view.*

class CustomAdapter(
    private val context: Context, private var platos: ArrayList<Plato>,
    private var listener: RecyclerPlatoListener
) :
    RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    private lateinit var viewHolder: ViewHolder
    private var multiSelect = false
    private var itemsSeleccionados = ArrayList<Int>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        viewHolder = ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.template_plato,
                parent,
                false
            )
        )
        return viewHolder
    }

    override fun getItemCount() = platos.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(platos[position], listener)
        val color = if (itemsSeleccionados.contains(position)) Color.LTGRAY
        else Color.WHITE
        holder.itemView.setBackgroundColor(color)
    }

    class ViewHolder(vista: View) : RecyclerView.ViewHolder(vista) {
        fun bind(plato: Plato, listener: RecyclerPlatoListener) = with(itemView) {
            tvNombre.text = plato.nombre
            ivFoto.setImageResource(plato.foto)

            // Clicks Events
            setOnClickListener { listener.onClick(plato, adapterPosition) }
            setOnLongClickListener {
                listener.onLongClick(plato, adapterPosition)
                true
            }
        }
    }

    fun initActionMode() {
        multiSelect = true
    }

    fun destroyActionMode() {
        multiSelect = false
        itemsSeleccionados.clear()
        notifyDataSetChanged()
    }

    fun finishActionMode() {
        // Eliminar elementos seleccionados
        for (item in itemsSeleccionados) {
            itemsSeleccionados.remove(item)
        }
        multiSelect = false
        notifyDataSetChanged()
    }

    fun selectItem(index: Int) {
        if (multiSelect) {
            if (itemsSeleccionados.contains(index)) itemsSeleccionados.removeAt(index)
            else itemsSeleccionados.add(index)
            notifyDataSetChanged()
        }
    }

    fun getCountSelectedItems(): String {
        return itemsSeleccionados.size.toString()
    }

    fun deleteSelectedItems() {
        if (itemsSeleccionados.size > 0) {
            var itemsAEliminar = ArrayList<Plato>()

            for (index in itemsSeleccionados) {
                itemsAEliminar.add(platos[index])
            }

            platos.removeAll(itemsAEliminar)
            itemsSeleccionados.clear()
        }
    }

}

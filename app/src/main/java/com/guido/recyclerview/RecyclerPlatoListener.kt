package com.guido.recyclerview

interface RecyclerPlatoListener {
    fun onClick(plato: Plato, position: Int)
    fun onLongClick(plato: Plato, position: Int)
}

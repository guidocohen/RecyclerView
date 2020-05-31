package com.guido.recyclerview

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import androidx.appcompat.view.ActionMode.Callback
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: CustomAdapter
    private var isActionMode = false
    private lateinit var actionMode: ActionMode
    private val layoutManager by lazy {
        LinearLayoutManager(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val callback = object : Callback {
            override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
                when (item?.itemId) {
                    R.id.iEliminar -> {
                        adapter.deleteSelectedItems()
                    }
                    else -> return true
                }

                adapter.finishActionMode()
                mode?.finish()
                isActionMode = false
                Toast.makeText(applicationContext, "Eliminación exitosa", Toast.LENGTH_SHORT).show()

                return true
            }

            override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                // Inicializar Action Mode
                adapter.initActionMode()
                actionMode = mode!!
                menuInflater.inflate(R.menu.menu_context, menu!!)
                return true
            }

            override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                mode?.title = "0 seleccionados"
                return false
            }

            override fun onDestroyActionMode(mode: ActionMode?) {
                // Destruir Action Mode
                adapter.destroyActionMode()
                isActionMode = false
            }
        }

        val platos = ArrayList<Plato>(
            listOf(
                Plato("Plato 1", 250.5, 3.5F, R.drawable.calabresa),
                Plato("Plato 2", 250.5, 3.5F, R.drawable.especial),
                Plato("Plato 3", 250.5, 3.5F, R.drawable.mozzarella),
                Plato("Plato 4", 250.5, 3.5F, R.drawable.napolitana),
                Plato("Plato 5", 250.5, 3.5F, R.drawable.calabresa),
                Plato("Plato 6", 250.5, 3.5F, R.drawable.especial),
                Plato("Plato 7", 250.5, 3.5F, R.drawable.mozzarella),
                Plato("Plato 8", 250.5, 3.5F, R.drawable.napolitana)
            )
        )
        recyclerView.setHasFixedSize(true)

        recyclerView.layoutManager = layoutManager

        adapter = CustomAdapter(this, platos,
            object : RecyclerPlatoListener {
                override fun onClick(plato: Plato, position: Int) {
                    Toast.makeText(applicationContext, plato.nombre, Toast.LENGTH_SHORT).show()
                }

                override fun onLongClick(plato: Plato, position: Int) {
                    if (!isActionMode) {
                        startSupportActionMode(callback)
                        isActionMode = true
                    }
                    adapter.selectItem(position)
                    actionMode?.title = adapter.getCountSelectedItems().plus(" seleccionados")
                }

            })
        recyclerView.adapter = adapter

        swipeToRefresh.setOnRefreshListener {
            swipeToRefresh.isRefreshing = false
            Toast.makeText(applicationContext, "Vista actualizada", Toast.LENGTH_SHORT).show()

            /*platos.add(Plato("Nugets", 400.5, 4.5F, R.drawable.calabresa))
            adapter.notifyDataSetChanged()*/
        }
    }
}

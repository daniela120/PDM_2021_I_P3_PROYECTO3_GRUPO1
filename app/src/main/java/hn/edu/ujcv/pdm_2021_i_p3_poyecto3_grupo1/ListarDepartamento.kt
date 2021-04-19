package hn.edu.ujcv.pdm_2021_i_p3_poyecto3_grupo1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import hn.edu.ujcv.pdm_2021_i_p3_poyecto3_grupo1.CLIENTES.ClienteService
import hn.edu.ujcv.pdm_2021_i_p3_poyecto3_grupo1.entities.ClienteDataCollectionItem
import hn.edu.ujcv.pdm_2021_i_p3_poyecto3_grupo1.entities.DepartamentoDataCollectionItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class ListarDepartamento : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listar_departamento)
        callServiceGetDepto()
    }


    private fun callServiceGetDepto() {
        var lista: java.util.HashSet<String> = hashSetOf()

        val deptoService: DepartamentoService = RestEngine.buildService().create(DepartamentoService::class.java)
        var result: Call<List<DepartamentoDataCollectionItem>> = deptoService.listDepartamentos()

        result.enqueue(object : Callback<List<DepartamentoDataCollectionItem>> {
            override fun onFailure(call: Call<List<DepartamentoDataCollectionItem>>, t: Throwable) {
                Toast.makeText(this@ListarDepartamento, "Error", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                    call: Call<List<DepartamentoDataCollectionItem>>,
                    response: Response<List<DepartamentoDataCollectionItem>>
            ) {
                try {
                    for (i in response.body()!!) {
                        lista.add("   "+i.id.toString()+"                  " + i.nombre.toString())
                    }


                    iniciar2(lista)

                } catch (e: Exception) {
                    println("No hay datos del producto")

                }

            }
        })

    }

    fun iniciar2(a: java.util.HashSet<String>){
        val list = findViewById<ListView>(R.id.list_dpto)

        var valor:String

        var A: java.util.ArrayList<String> = java.util.ArrayList()

        for(i in a){
            val data = i.toString().split("|")
            valor=data[0].toString()
            A.add(valor)

        }




        val adaptador = ArrayAdapter(this,android.R.layout.simple_list_item_1,A)

        list.adapter =adaptador
        list.onItemSelectedListener = object:
                AdapterView.OnItemSelectedListener { override fun onNothingSelected(parent: AdapterView<*>?) {
        }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long)
            {



            }
        }



    }

}


package hn.edu.ujcv.pdm_2021_i_p3_poyecto3_grupo1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import hn.edu.ujcv.pdm_2021_i_p3_poyecto3_grupo1.CLIENTES.ClienteService
import hn.edu.ujcv.pdm_2021_i_p3_poyecto3_grupo1.entities.ClienteDataCollectionItem
import kotlinx.android.synthetic.main.activity_listar_cliente.*
import kotlinx.android.synthetic.main.activity_mostrar_cliente.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class ListarCliente : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listar_cliente)
        btn_regresarListarCliente.setOnClickListener { regresar() }




        callServiceGetProducto()


    }
    private fun regresar(){
        val intent = Intent(this,MostrarCLiente::class.java)
        startActivity(intent)
    }


    private fun callServiceGetProducto() {
        var lista: java.util.HashSet<String> = hashSetOf()
        var lista1: java.util.HashSet<String> = hashSetOf()
        val tipoService: ClienteService = RestEngine.buildService().create(ClienteService::class.java)
        var result: Call<List<ClienteDataCollectionItem>> = tipoService.listClientes()

        result.enqueue(object : Callback<List<ClienteDataCollectionItem>> {
            override fun onFailure(call: Call<List<ClienteDataCollectionItem>>, t: Throwable) {
                Toast.makeText(this@ListarCliente, "Error", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(
                    call: Call<List<ClienteDataCollectionItem>>,
                    response: Response<List<ClienteDataCollectionItem>>
            ) {
                try {
                    for (i in response.body()!!) {
                        lista.add(i.id.toString())
                    }

                    for (i in response.body()!!) {
                        lista1.add(i.nombrecompleto)
                    }

                    iniciar2(lista,lista1)

                } catch (e: Exception) {
                    println("No hay datos del producto")

                }

            }
        })

    }

    fun iniciar2(a: java.util.HashSet<String>, b:HashSet<String>){
        val list = findViewById<ListView>(R.id.list_cliente1)
        var list_s = findViewById<ListView>(R.id.list_cliente2)
        var valor:String
        var valor1:String
        var A: java.util.ArrayList<String> = java.util.ArrayList()
        var B: java.util.ArrayList<String> = java.util.ArrayList()
        for(i in a){
            val data = i.toString().split("|")
            valor=data[0].toString()
            A.add(valor)

        }

        for(i in b){
            val data = i.toString().split("|")
            valor1=data[0].toString()
            B.add(valor1)

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


        val adaptador1 = ArrayAdapter(this,android.R.layout.simple_list_item_1,B)

        list_s.adapter =adaptador1
        list_s.onItemSelectedListener = object:
                AdapterView.OnItemSelectedListener { override fun onNothingSelected(parent: AdapterView<*>?) {
        }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long)
            {



            }
        }


    }

}
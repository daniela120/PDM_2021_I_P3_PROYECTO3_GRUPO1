package hn.edu.ujcv.pdm_2021_i_p3_poyecto3_grupo1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.getbase.floatingactionbutton.FloatingActionButton
import hn.edu.ujcv.pdm_2021_i_p3_poyecto3_grupo1.CLIENTES.ClienteService
import hn.edu.ujcv.pdm_2021_i_p3_poyecto3_grupo1.entities.*
import kotlinx.android.synthetic.main.activity_cliente.*
import kotlinx.android.synthetic.main.activity_compras.*
import kotlinx.android.synthetic.main.activity_departamento.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlinx.android.synthetic.main.activity_produccion.*
import kotlinx.android.synthetic.main.activity_ventas.*
import java.lang.StringBuilder

class Produccion : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_produccion)
        btn_regresarProduccion.setOnClickListener { Regresar() }
        findViewById<FloatingActionButton>(R.id.idFabListar_Produccion).setOnClickListener {
            Mostrar() }
        findViewById<FloatingActionButton>(R.id.idFabConfirmar_Produccion).setOnClickListener {
            validacion()
        }
        findViewById<FloatingActionButton>(R.id.idListarPRODUCI).setOnClickListener {
            ir()
        }

       callServiceGetDepartamentos()
        callServiceGetProductos()
        callServiceEmpleados()
    }
    private fun ir() {
        val intent = Intent(this, ListarProduccion::class.java)
        startActivity(intent)
    }



    private fun callServicePostProduccion() {
        try {


            val Info = ProduccionDataCollectionItem(id = null,
                    idproducto = spinnerIdProducto.selectedItem.toString().toLong(),
                    idempleado = spinnerIdEmpleado.selectedItem.toString().toLong(),
                    iddepto = spinnerdepto.selectedItem.toString().toLong(),
                    descripcion = txt_DescripcionProduccion.text.toString(),
                    tiempo = txt_TiempoProduccion.text.toString()
            )



            addProduccion(Info) {
                if (it?.id != null) {
                    android.widget.Toast.makeText(this@Produccion, "ORDEN DE PRODUCCION GUARDA" , android.widget.Toast.LENGTH_LONG).show()
                } else {
                    android.widget.Toast.makeText(this@Produccion, "Error", android.widget.Toast.LENGTH_LONG).show()
                }
            }
        }catch (e:Exception){
            Toast.makeText(this, e.message+" ERROR,POR FAVOR VERIFICAR LOS DATOS", Toast.LENGTH_SHORT).show()

        }
    }

    fun  addProduccion(produccionData: ProduccionDataCollectionItem, onResult: (ProduccionDataCollectionItem?) -> Unit) {
        val retrofit = RestEngine.buildService().create(ProduccionService::class.java)
        var result: Call<ProduccionDataCollectionItem> = retrofit.addProduccion(produccionData)

        result.enqueue(object : Callback<ProduccionDataCollectionItem> {
            override fun onFailure(call: Call<ProduccionDataCollectionItem>, t: Throwable) {
                onResult(null)
            }

            override fun onResponse(call: Call<ProduccionDataCollectionItem>,
                                    response: Response<ProduccionDataCollectionItem>) {
                if (response.isSuccessful) {
                    val addedProduccion= response.body()!!
                    onResult(addedProduccion)
                } else if (response.code() == 401) {
                    Toast.makeText(this@Produccion, "Sesion expirada", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this@Produccion, "Fallo al traer el item", Toast.LENGTH_LONG).show()
                }
            }

        }
        )
    }

/*SPINNER DEPTO*/
    private fun callServiceGetDepartamentos() {
        var lista: HashSet<String> = hashSetOf()
        val departamentoService:DepartamentoService = RestEngine.buildService().create(DepartamentoService::class.java)
        var result: Call<List<DepartamentoDataCollectionItem>> = departamentoService.listDepartamentos()

        result.enqueue(object : Callback<List<DepartamentoDataCollectionItem>> {
            override fun onFailure(call: Call<List<DepartamentoDataCollectionItem>>, t: Throwable) {
                Toast.makeText(this@Produccion, "Error", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(
                    call: Call<List<DepartamentoDataCollectionItem>>,
                    response: Response<List<DepartamentoDataCollectionItem>>
            ) {
                try {
                    for (i in response.body()!!) {
                        lista.add(i.id.toString())
                    }

                    iniciar2(lista)

                } catch (e: Exception) {
                    println("No hay datos de tipo de pago")

                }

            }
        })

    }
    fun iniciar2(a:HashSet<String>){
        val spinner_Puestos = findViewById<Spinner>(R.id.spinnerdepto)
        var valor:String
        println("THIS IS"+a.toString())
        var A:ArrayList<String> = ArrayList()
        for(i in a){
            val data = i.toString().split("|")
            valor=data[0].toString()
            A.add(valor)

        }

        println("ESTO ES LO QUE SE VA A GUARDAR" + A.toString())

        val adaptador = ArrayAdapter(this,android.R.layout.simple_spinner_item,A)

        spinner_Puestos.adapter =adaptador
        spinner_Puestos.onItemSelectedListener = object:
                AdapterView.OnItemSelectedListener { override fun onNothingSelected(parent: AdapterView<*>?) {
        }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long)
            {
                callServiceGetDepartamentoById(A[position].toString().toLong())
            }
        }


    }
    private  fun  callServiceGetDepartamentoById(a:Long) {
        var nombrep=""
        val departamentoService:DepartamentoService =  RestEngine.buildService().create(DepartamentoService::class.java)
        var result: Call<DepartamentoDataCollectionItem> =departamentoService.getDepartamentoById(a)

        result.enqueue(object : Callback<DepartamentoDataCollectionItem> {
            override fun onFailure(call: Call<DepartamentoDataCollectionItem>, t: Throwable) {
                Toast.makeText(this@Produccion,"Error",Toast.LENGTH_LONG).show()
            }

            override fun onResponse(
                    call: Call<DepartamentoDataCollectionItem>,
                    response: Response<DepartamentoDataCollectionItem>
            ) {
                nombrep = response.body()!!.nombre
                txv_select4.text = nombrep.toString()

            }
        })


    }

    /*SPINNER PRODUCTO*/
    private fun callServiceGetProductos() {
        var lista: java.util.HashSet<String> = hashSetOf()

        val tipoService:ProductoService = RestEngine.buildService().create(ProductoService::class.java)
        var result: Call<List<ProductoDataCollectionItem>> = tipoService.listProductos()

        result.enqueue(object : Callback<List<ProductoDataCollectionItem>> {
            override fun onFailure(call: Call<List<ProductoDataCollectionItem>>, t: Throwable) {
                Toast.makeText(this@Produccion, "Error", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(
                    call: Call<List<ProductoDataCollectionItem>>,
                    response: Response<List<ProductoDataCollectionItem>>
            ) {
                try {
                    for (i in response.body()!!) {
                        lista.add(i.id.toString())
                    }

                    iniciar4(lista)

                } catch (e: Exception) {
                    println("No hay datos de tipo de pago")

                }

            }
        })

    }

    fun iniciar4(a: java.util.HashSet<String>){
        val spinner_Puestos = findViewById<Spinner>(R.id.spinnerIdProducto)
        var valor:String
        var A: java.util.ArrayList<String> = java.util.ArrayList()
        for(i in a){
            val data = i.toString().split("|")
            valor=data[0].toString()
            A.add(valor)

        }

        val adaptador = ArrayAdapter(this,android.R.layout.simple_spinner_item,A)

        spinner_Puestos.adapter =adaptador
        spinner_Puestos.onItemSelectedListener = object:
                AdapterView.OnItemSelectedListener { override fun onNothingSelected(parent: AdapterView<*>?) {
        }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long)
            {
                callServiceGetProductoById(A[position].toString().toLong())


            }
        }

    }

    private fun callServiceGetProductoById(a:Long) {
        var nombrep=""
        val pagoservice:ProductoService = RestEngine.buildService().create(ProductoService::class.java)
        var result: Call<ProductoDataCollectionItem> = pagoservice.getProductoById(a)

        result.enqueue(object : Callback<ProductoDataCollectionItem> {
            override fun onFailure(call: Call<ProductoDataCollectionItem>, t: Throwable) {
                Toast.makeText(this@Produccion,"Error",Toast.LENGTH_LONG).show()
            }

            override fun onResponse(
                    call: Call<ProductoDataCollectionItem>,
                    response: Response<ProductoDataCollectionItem>
            ) {
                nombrep = response.body()!!.nombre.toString()
                txv_select.text = nombrep

            }
        })


    }
/*SPINNER EMPLEADOS*/

    private fun callServiceEmpleados() {
        var lista: java.util.HashSet<String> = hashSetOf()

        val tipoService: EmpleadoService = RestEngine.buildService().create(EmpleadoService::class.java)
        var result: Call<List<EmpleadoDataCollectionItem>> = tipoService.listEmpleados()

        result.enqueue(object : Callback<List<EmpleadoDataCollectionItem>> {
            override fun onFailure(call: Call<List<EmpleadoDataCollectionItem>>, t: Throwable) {
                Toast.makeText(this@Produccion, "Error", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(
                    call: Call<List<EmpleadoDataCollectionItem>>,
                    response: Response<List<EmpleadoDataCollectionItem>>
            ) {
                try {
                    for (i in response.body()!!) {
                        lista.add(i.id.toString())
                    }

                    iniciar5(lista)

                } catch (e: Exception) {
                    println("No hay datos de tipo de pago")

                }

            }
        })

    }

    fun iniciar5(a: java.util.HashSet<String>){
        val spinner_Puestos = findViewById<Spinner>(R.id.spinnerIdEmpleado)
        var valor:String
        var A: java.util.ArrayList<String> = java.util.ArrayList()
        for(i in a){
            val data = i.toString().split("|")
            valor=data[0].toString()
            A.add(valor)

        }

        val adaptador = ArrayAdapter(this,android.R.layout.simple_spinner_item,A)

        spinner_Puestos.adapter =adaptador
        spinner_Puestos.onItemSelectedListener = object:
                AdapterView.OnItemSelectedListener { override fun onNothingSelected(parent: AdapterView<*>?) {
        }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long)
            {
                callServiceGetEmpleadoById(A[position].toString().toLong())


            }
        }

    }

    private fun callServiceGetEmpleadoById(a:Long) {
        var nombrep=""
        val pagoservice: EmpleadoService = RestEngine.buildService().create(EmpleadoService::class.java)
        var result: Call<EmpleadoDataCollectionItem> = pagoservice.getEmpleadoById(a)

        result.enqueue(object : Callback<EmpleadoDataCollectionItem> {
            override fun onFailure(call: Call<EmpleadoDataCollectionItem>, t: Throwable) {
                Toast.makeText(this@Produccion,"Error",Toast.LENGTH_LONG).show()
            }

            override fun onResponse(
                    call: Call<EmpleadoDataCollectionItem>,
                    response: Response<EmpleadoDataCollectionItem>
            ) {
                nombrep = response.body()!!.nombrecompleto.toString()
                txv_select3.text = nombrep

            }
        })


    }


    private  fun guardar():Boolean {
        var a = true


        if (txt_DescripcionProduccion.text.toString().isEmpty()) {
            Toast.makeText(this, "Ingrese la Descripcion", Toast.LENGTH_SHORT).show()
            a = false
        } else {
            if (txt_TiempoProduccion.text.toString().isEmpty()) {
                Toast.makeText(this, "Ingrese el Tiempo", Toast.LENGTH_SHORT).show()
                a = false
                if (spinnerIdEmpleado.isSelected.toString().isEmpty()) {
                    a = false
                    Toast.makeText(this, "Ingrese ID de Empleado que realizo la venta", Toast.LENGTH_SHORT).show()
                } else {
                    if (spinnerIdProducto2.isSelected.toString().isEmpty()) {
                        a = false
                        Toast.makeText(this, "Seleccione el producto", Toast.LENGTH_SHORT).show()
                    } else {
                        if (spinnerdepto.isSelected.toString().isEmpty()) {
                            a = false
                            Toast.makeText(this, "Seleccione el departamento", Toast.LENGTH_SHORT).show()
                        } else {
                            a = true
                        }
                    }
                }

            }
        }
        return a
    }

    fun validacion(){
        if(guardar().equals(true)){
            callServicePostProduccion()
            Toast.makeText(this, "DATO EXITOSO", Toast.LENGTH_SHORT).show()
        }

    }


    private fun Regresar() {
            val intent = Intent(this, Menu::class.java)
            startActivity(intent)
        }
    private fun Mostrar() {
        val intent = Intent(this, MostrarProduccion::class.java)
        startActivity(intent)
    }

    }

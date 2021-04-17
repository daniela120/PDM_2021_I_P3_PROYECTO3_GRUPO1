package hn.edu.ujcv.pdm_2021_i_p3_poyecto3_grupo1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.getbase.floatingactionbutton.FloatingActionButton
import hn.edu.ujcv.pdm_2021_i_p3_poyecto3_grupo1.entities.ClienteDataCollectionItem
import hn.edu.ujcv.pdm_2021_i_p3_poyecto3_grupo1.entities.ComprasDataCollectionItem
import kotlinx.android.synthetic.main.activity_cliente.*

import kotlinx.android.synthetic.main.activity_compras.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Compras : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compras)
        btn_regresarCompras.setOnClickListener { Regresar() }

        findViewById<FloatingActionButton>(R.id.idFabListar_Compras).setOnClickListener {
            Mostrar() }
        txt_FechaCompra.setOnClickListener{showDatePickerDialog()}
    }

    fun showDatePickerDialog() {
        val datePicker = DatePickerFragment{day, month, year -> onDateSelected( day, month, year) }
        datePicker.show(supportFragmentManager, "datePicker")
    }
    fun onDateSelected(day: Int, month: Int, year: Int) {
        txt_FechaCompra.setText("$day / $month / $year")
    }

    private fun callServicePostCompra() {

        val compraInfo = ComprasDataCollectionItem(
                cai = txt_CaiCompra.text.toString(),
                proveedores = txt_TelefonoCliente.text.toString().toLong(),
                numerotarjeta = txt_dniCliente.text.toString().toLong(),
                formapago = txt_dniCliente.text.toString(),
                fechaentrega = txt_CorreoCliente2.text.toString(),
                fechacompra = txt_DireccionCliente.text.toString(),
                insumos = txt_ins
        )

            addPerson(personInfo) {
                if (it?.id != null) {
                    Toast.makeText(this@MainActivity,"OK"+it?.id,Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this@MainActivity,"Error",Toast.LENGTH_LONG).show()
                }
            }
        }


    fun addPerson(clienteData: ClienteDataCollectionItem, onResult: (ClienteDataCollectionItem?) -> Unit){
        val retrofit = RestEngine.buildService().create(Cliente::class.java)
        var result: Call<PersonDataCollectionItem> = retrofit.addPerson(personData)

        result.enqueue(object : Callback<PersonDataCollectionItem> {
            override fun onFailure(call: Call<PersonDataCollectionItem>, t: Throwable) {
                onResult(null)
            }

            override fun onResponse(call: Call<PersonDataCollectionItem>,
                                    response: Response<PersonDataCollectionItem>) {
                if (response.isSuccessful) {
                    val addedPerson = response.body()!!
                    onResult(addedPerson)
                }
                else if (response.code() == 401){
                    Toast.makeText(this@MainActivity,"Sesion expirada",Toast.LENGTH_LONG).show()
                }
                else{
                    Toast.makeText(this@MainActivity,"Fallo al traer el item",Toast.LENGTH_LONG).show()
                }
            }

        }
        )
    }




    private fun Regresar() {
        val intent = Intent(this, Menu::class.java)
        startActivity(intent)
    }
    private fun Mostrar() {
        val intent = Intent(this, MostrarCompras::class.java)
        startActivity(intent)
    }
}
package hn.edu.ujcv.pdm_2021_i_p3_poyecto3_grupo1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_productos.*


class Productos : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_productos)

    }

    /*private  fun guardar() {

        if (txtId2.text.toString().isEmpty()) {
            Toast.makeText(this, "Ingrese ID del Producto", Toast.LENGTH_SHORT).show()
        }else {
            if (txtNombre2.text.toString().isEmpty()) {
                Toast.makeText(this, "Ingrese un Nombre", Toast.LENGTH_SHORT).show()
            } else {
                if (txt_TipoTela.text.toString().isEmpty()) {
                    Toast.makeText(this, "Ingrese el tipo de tela", Toast.LENGTH_SHORT).show()
                } else {
                    if (txt_DescripcionP.text.toString().isEmpty()) {
                        Toast.makeText(this, "Ingrese una Descripcion", Toast.LENGTH_SHORT).show()
                    } else {
                        if (txt_PrecioP.text.toString().isEmpty()) {
                            Toast.makeText(this, "Inreges el Precio", Toast.LENGTH_SHORT).show()
                        }else{
                            Toast.makeText(this, "Realizada con exito!", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    private fun Regresar() {
        val intent = Intent(this, Menu::class.java)
        startActivity(intent)
    }*/
}
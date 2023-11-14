package com.example.like_app.ui.servicios

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.like_app.R
import com.example.like_app.adapter.RestauranteAdapter
import com.example.like_app.databinding.FragmentListaRestaurantesBinding
import com.example.like_app.model.RestauranteModel
import com.google.firebase.firestore.FirebaseFirestore


class ListaRestaurantes : Fragment() , RestauranteAdapter.RecyclerViewEvent {
    private lateinit var navController: NavController
    private var _binding: FragmentListaRestaurantesBinding? = null
    private val binding get() = _binding!!
    var lstRest: List<RestauranteModel> =emptyList()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding=FragmentListaRestaurantesBinding.inflate(inflater, container, false)
        val view: View = binding.root
        //val view: View = inflater.inflate(R.layout.fragment_lista_restaurantes, container, false)
        //CONEXION A FIRE BASE
        val db = FirebaseFirestore.getInstance()
        val rvRest: RecyclerView = view.findViewById(R.id.rc_rest)
        val etBuscar = view.findViewById<EditText>(R.id.etBuscar)
        val containerBtn:RelativeLayout = view.findViewById(R.id.relativeLayout3)
        val btnCancelar : ImageButton = view.findViewById(R.id.btnCancelar)

        /* Usar un LinearLayoutManager
        val layoutManager = LinearLayoutManager(requireContext())
        rvRest.layoutManager = layoutManager

        // Ajustar el tamaño del RecyclerView fijándolo
        rvRest.setHasFixedSize(true)*/

        db.collection("Restaurante")
            .addSnapshotListener { snap, e ->
                if (e != null) {
                    Log.i("ERROR", "Ocurrio un Error")
                    return@addSnapshotListener
                }
                //LLENO lstREST
                lstRest = snap!!.documents.map { document ->
                    RestauranteModel(
                        document["nombre"].toString(),
                        document["tiempo"].toString(),
                        document["precio_envio"].toString(),
                        document["imageUrl"].toString()

                    )
                }
                Log.i("Print", lstRest.size.toString())
                rvRest.adapter = RestauranteAdapter(lstRest, this)
                rvRest.layoutManager = LinearLayoutManager(requireContext())

            }

        etBuscar.setOnFocusChangeListener { _, hasFocus ->
            // Ocultar containerBtn cada vez que etBuscar obtiene o pierde el foco
            containerBtn.visibility = if (hasFocus) View.INVISIBLE else View.VISIBLE
            btnCancelar.visibility = if (hasFocus) View.VISIBLE else View.INVISIBLE
        }
        etBuscar.setOnClickListener {
            // Cuando se hace clic en etBuscar, deselecciona y muestra containerBtn
            etBuscar.clearFocus()
            containerBtn.visibility = View.INVISIBLE
            btnCancelar.visibility = View.VISIBLE
        }

        btnCancelar.setOnClickListener {
            // Restaurar la visibilidad de containerBtn y btnCancelar cuando se presiona btnCancelar
            containerBtn.visibility = View.VISIBLE
            btnCancelar.visibility = View.INVISIBLE
            etBuscar.clearFocus() // Desenfocar el EditText
            etBuscar.text.clear() // Borrar el contenido de etBuscar

            // Ocultar el teclado suavemente
            val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(etBuscar.windowToken, 0)

        }
        etBuscar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                charSequence: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
                // No se usa en este ejemplo
            }

            override fun onTextChanged(
                charSequence: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                // Filtrar la lista en función del texto ingresado
                val listaFiltrada = lstRest.filter { restaurante ->
                    restaurante.nombre.contains(charSequence.toString(), ignoreCase = true)
                }

                // Actualizar el RecyclerView con la lista filtrada
                (rvRest.adapter as RestauranteAdapter).actualizarLista(listaFiltrada)

                // Ajustar la visibilidad de containerBtn y btnCancelar
                containerBtn.visibility = View.INVISIBLE
                btnCancelar.visibility = View.VISIBLE

            }

            override fun afterTextChanged(editable: Editable?) {
                // No se usa en este ejemplo
            }
        })


        // Agregar un observador para el teclado
        etBuscar.viewTreeObserver.addOnGlobalLayoutListener {
            if (!etBuscar.isFocused) {
                // Si etBuscar no está enfocado, mostrar containerBtn
                containerBtn.visibility = View.VISIBLE
                btnCancelar.visibility = View.INVISIBLE
            }
        }

        // Agregar un listener para el botón de retroceso
        etBuscar.setOnKeyListener { _, keyCode, _ ->
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                // Cuando se presiona el botón de retroceso, deseleccionar etBuscar y mostrar containerBtn
                etBuscar.clearFocus()
                containerBtn.visibility = View.VISIBLE
                btnCancelar.visibility = View.INVISIBLE
                true // Consumir el evento
            } else {
                false // No consumir el evento
            }
        }

        etBuscar.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                // Ocultar el teclado suavemente
                val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(etBuscar.windowToken, 0)

                // Realizar cualquier acción adicional que desees al presionar el botón de confirmación
                // ...

                true // Consumir el evento
            } else {
                false // No consumir el evento
            }
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }

    override fun onItemClick(position: Int) {
        val rest=lstRest[position]
        Toast.makeText(
            context,
            rest.nombre,
            Toast.LENGTH_SHORT
        ).show()
        val bundle = Bundle()
        bundle.putString("clave", "TITULO RESTAURANTE")
        findNavController().navigate(R.id.action_listaRestaurantes_to_restFragment, bundle)

    }

}
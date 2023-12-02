package com.example.like_app.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.like_app.R


class EstadoFragment : Fragment() {

    private lateinit var imageEstado: ConstraintLayout

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view:View =  inflater.inflate(R.layout.fragment_chkdepo, container, false)
        imageEstado = view.findViewById(R.id.constraintLayout4)



        return view
    }

    fun changeBackground(backgroundResource: Int) {
        // Verificar que constraintLayout esté inicializado antes de usarlo
        if (::imageEstado.isInitialized) {
            imageEstado.setBackgroundResource(backgroundResource)
        }
    }

}
package com.example.like_app.ui.fragmen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import com.example.like_app.R




class AtenClientFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_aten_client, container, false)

        val ratingBar = view.findViewById<RatingBar>(R.id.ratingBar)
        ratingBar.contentDescription = "Calificación del servicio"

        return view
    }




}
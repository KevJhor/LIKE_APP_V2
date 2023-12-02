package com.example.like_app.ui.payonline

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.example.like_app.R


class PayonlineFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_payonline, container, false)

        val btnPagar: Button = view.findViewById(R.id.btnaddtarjeta)

        btnPagar.setOnClickListener {
            findNavController().navigate(R.id.action_payonlineFragment_to_depositoFragment)
        }

        return view
    }


}
package com.example.like_app.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.like_app.R
import com.example.like_app.databinding.FragmentHomeBinding


class RestFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view:View=inflater.inflate(R.layout.fragment_rest, container, false)
        val arguments = requireArguments()
        val valor = arguments.getString("clave")
        val txtNombreRest:TextView=view.findViewById(R.id.tvNombreRest)
        txtNombreRest.text=valor
        return view
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
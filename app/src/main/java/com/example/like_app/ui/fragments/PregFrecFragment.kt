package com.example.like_app.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.findNavController
import com.example.like_app.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

class PregFrecFragment : Fragment() {


    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view:View = inflater.inflate(R.layout.fragment_preg_frec, container, false)
        val btnPed:Button = view.findViewById(R.id.btnPpedi)
        val btnEnt:Button = view.findViewById(R.id.btnPentre)
        val btnPag:Button = view.findViewById(R.id.btnPpag)
        val btnPerf:Button = view.findViewById(R.id.btnPperf)
        val btnPrec:Button = view.findViewById(R.id.btnPpreci)
        val btnContactar:Button = view.findViewById(R.id.btnContAs)
        btnPed.setOnClickListener{

            val navController = requireActivity().findNavController(R.id.nav_host_fragment_content_main) // Asegúrate de que el ID sea el correcto
            navController.navigate(R.id.fgPpedi)

        }
        btnEnt.setOnClickListener{

            val navController = requireActivity().findNavController(R.id.nav_host_fragment_content_main) // Asegúrate de que el ID sea el correcto
            navController.navigate(R.id.fgPEntrega)

        }
        btnPag.setOnClickListener{

            val navController = requireActivity().findNavController(R.id.nav_host_fragment_content_main) // Asegúrate de que el ID sea el correcto
            navController.navigate(R.id.fgPpag)
        }

        btnPerf.setOnClickListener{

            val navController = requireActivity().findNavController(R.id.nav_host_fragment_content_main) // Asegúrate de que el ID sea el correcto
            navController.navigate(R.id.fgPperfi)
        }
        btnPrec.setOnClickListener{

            val navController = requireActivity().findNavController(R.id.nav_host_fragment_content_main) // Asegúrate de que el ID sea el correcto
            navController.navigate(R.id.fgPpreci)

        }

        btnContactar.setOnClickListener{

            val navController = requireActivity().findNavController(R.id.nav_host_fragment_content_main) // Asegúrate de que el ID sea el correcto
            navController.navigate(R.id.fgChat)

        }

        return view
    }


}
package com.example.like_app.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.like_app.R
import com.example.like_app.databinding.FragmentHomeBinding
import com.example.like_app.ui.fragmen.List_Rest_Fragment
import com.example.like_app.ui.gallery.GalleryFragment

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val imgRelativeLayout = root.findViewById<RelativeLayout>(R.id.imgR)
        val imgRelativeLayoutT = root.findViewById<RelativeLayout>(R.id.imgT)


        imgRelativeLayout.setOnClickListener {
            // Obtén una referencia al NavController
            val navController = Navigation.findNavController(root)

            // Realiza la transición al otro fragmento (reemplaza 'fragmentB' con el ID de tu fragmento de destino)
            navController.navigate(R.id.listaRestaurantes)
        }

        imgRelativeLayoutT.setOnClickListener {
            // Obtén una referencia al NavController
            val navController = Navigation.findNavController(root)

            // Realiza la transición al otro fragmento (reemplaza 'fragmentB' con el ID de tu fragmento de destino)
            navController.navigate(R.id.listaTiendas2)
        }



        val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}
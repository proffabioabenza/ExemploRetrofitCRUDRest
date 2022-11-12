package com.example.androidcrudrest.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.androidcrudrest.R
import com.example.androidcrudrest.databinding.FragmentListaClientesBinding

class ListaClientesFragment : Fragment() {
    lateinit var binding: FragmentListaClientesBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentListaClientesBinding.inflate(inflater)



        return binding.root
    }

}
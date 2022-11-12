package com.example.androidcrudrest.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.androidcrudrest.R
import com.example.androidcrudrest.databinding.ActivityMainBinding
import com.example.androidcrudrest.databinding.FragmentAdicionarClienteBinding
import com.example.androidcrudrest.view.fragment.AdicionarClienteFragment
import com.example.androidcrudrest.view.fragment.ListaClientesFragment

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNavigation.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.adicionar -> {
                    val frag = AdicionarClienteFragment(null)
                    supportFragmentManager.beginTransaction().replace(R.id.container, frag).addToBackStack("Adicionar").commit()
                }
                R.id.listar -> {
                    val frag = ListaClientesFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.container, frag).addToBackStack("Adicionar").commit()
                }
            }

            true
        }
    }
}
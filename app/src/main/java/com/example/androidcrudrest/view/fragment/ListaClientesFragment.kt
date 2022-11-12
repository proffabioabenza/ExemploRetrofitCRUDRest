package com.example.androidcrudrest.view.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.example.androidcrudrest.R
import com.example.androidcrudrest.databinding.FragmentListaClientesBinding
import com.example.androidcrudrest.databinding.ItemClienteBinding
import com.example.androidcrudrest.model.Cliente
import com.example.androidcrudrest.service.API
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.zip.Inflater

class ListaClientesFragment : Fragment() {
    lateinit var binding: FragmentListaClientesBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentListaClientesBinding.inflate(inflater)

        listarClientes(inflater)

        return binding.root
    }

    fun listarClientes(inflater: LayoutInflater) {

        val callback = object : Callback<List<Cliente>> {
            override fun onResponse(call: Call<List<Cliente>>, response: Response<List<Cliente>>) {
                if (response.isSuccessful) {
                    val listaClientes = response.body()
                    atualizarTela(listaClientes, inflater)
                }
                else {
                    Snackbar.make(binding.root, "Não foi possível recuperar a lista de clientes", Snackbar.LENGTH_LONG).show()
                    val erro = response.errorBody()?.string()
                    erro?.let { Log.e("Erro", it) }
                }
            }

            override fun onFailure(call: Call<List<Cliente>>, t: Throwable) {
                Snackbar.make(binding.root, "Não foi possível recuperar a lista de clientes", Snackbar.LENGTH_LONG).show()
                val erro = t.message.toString()
                erro?.let { Log.e("Erro", it) }
            }

        }

        API().cliente.listar().enqueue(callback)
    }

    fun atualizarTela(listaClientes: List<Cliente>?, inflater: LayoutInflater) {
        listaClientes?.forEach { cliente ->
            val itemBinding = ItemClienteBinding.inflate(inflater)

            itemBinding.textNome.text = cliente.nome
            itemBinding.textEmail.text = cliente.email

            itemBinding.buttonEditar.setOnClickListener {
                abrirFragmentoEdicao(cliente.id)
            }

            binding.container.addView(itemBinding.root)
        }
    }

    fun abrirFragmentoEdicao(idCli: Int?) {
        if (idCli == null) {
            return
        }

        val supportFragmentManager = activity?.supportFragmentManager
        supportFragmentManager?.let {
            val frag = AdicionarClienteFragment(idCli)
            it.beginTransaction().replace(R.id.container, frag).addToBackStack("Editar").commit()
        }
    }

    fun excluir(id: Int) {

    }

}


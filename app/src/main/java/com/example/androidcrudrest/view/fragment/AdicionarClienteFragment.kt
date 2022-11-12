package com.example.androidcrudrest.view.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.androidcrudrest.R
import com.example.androidcrudrest.databinding.FragmentAdicionarClienteBinding
import com.example.androidcrudrest.model.Cliente
import com.example.androidcrudrest.service.API
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdicionarClienteFragment : Fragment() {
    lateinit var binding: FragmentAdicionarClienteBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentAdicionarClienteBinding.inflate(inflater)

        binding.buttonAdicionar.setOnClickListener {
            inserirCliente()
        }

        return binding.root
    }

    fun inserirCliente() {
        val nome = binding.editNome.text.toString()
        val email = binding.editEmail.text.toString()
        val tel = binding.editTelefone.text.toString()

        val cliente = Cliente(null, nome, email, tel)

        val callback = object : Callback<Cliente> {
            override fun onResponse(call: Call<Cliente>, response: Response<Cliente>) {
                if (response.isSuccessful) {
                    Snackbar.make(binding.root, "Dados salvos", Snackbar.LENGTH_LONG)
                    limparTela()
                    voltarParaListagem()
                }
                else {
                    Snackbar.make(binding.root, "Ocorreu um problema ao salvar", Snackbar.LENGTH_LONG)
                    val erro = response.errorBody()?.string()
                    erro?.let { Log.e("Erro", it) }
                }
            }

            override fun onFailure(call: Call<Cliente>, t: Throwable) {
                Snackbar.make(binding.root, "Ocorreu um problema ao salvar", Snackbar.LENGTH_LONG)
                val erro = t.message.toString()
                erro?.let { Log.e("Erro", it) }
            }

        }

        API().cliente.inserir(cliente).enqueue(callback)
    }

    fun limparTela() {
        binding.editNome.text.clear()
        binding.editEmail.text.clear()
        binding.editTelefone.text.clear()
    }

    fun voltarParaListagem() {
        val frag = ListaClientesFragment()
        activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.container, frag)?.addToBackStack("Lista")?.commit()
    }
}
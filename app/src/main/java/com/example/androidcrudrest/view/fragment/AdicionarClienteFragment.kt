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

class AdicionarClienteFragment(val idEdicao: Int?) : Fragment() {
    lateinit var binding: FragmentAdicionarClienteBinding
    var estaEditando = false;

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentAdicionarClienteBinding.inflate(inflater)

        if (idEdicao != null) {
            estaEditando = true
            obterDadosCliente(idEdicao)
        }

        binding.buttonSalvar.setOnClickListener {
            if (!estaEditando) {
                inserirCliente()
            }
            else {
                editarCliente()
            }
        }

        return binding.root
    }

    fun obterClienteDaTela(): Cliente {
        val nome = binding.editNome.text.toString()
        val email = binding.editEmail.text.toString()
        val tel = binding.editTelefone.text.toString()

        return Cliente(null, nome, email, tel)
    }

    fun inserirCliente() {
        val cliente = obterClienteDaTela()

        val callback = object : Callback<Cliente> {
            override fun onResponse(call: Call<Cliente>, response: Response<Cliente>) {
                if (response.isSuccessful) {
                    mostrarSucesso()
                }
                else {
                    mostrarErro(response)
                }
            }

            override fun onFailure(call: Call<Cliente>, t: Throwable) {
                mostrarFalha(t)
            }

        }

        API().cliente.inserir(cliente).enqueue(callback)
    }

    private fun mostrarFalha(t: Throwable) {
        Snackbar.make(binding.root, "Ocorreu um problema ao salvar", Snackbar.LENGTH_LONG).show()
        val erro = t.message.toString()
        erro?.let { Log.e("Erro", it) }
    }

    private fun mostrarErro(response: Response<Cliente>) {
        Snackbar.make(binding.root, "Ocorreu um problema ao salvar", Snackbar.LENGTH_LONG).show()
        val erro = response.errorBody()?.string()
        erro?.let { Log.e("Erro", it) }
    }

    private fun mostrarSucesso() {
        Snackbar.make(binding.root, "Dados salvos", Snackbar.LENGTH_LONG).show()
        limparTela()
        voltarParaListagem()
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

    fun obterDadosCliente(idCliente: Int) {

        val callback = object : Callback<Cliente> {
            override fun onResponse(call: Call<Cliente>, response: Response<Cliente>) {
                val cliente = response.body()

                if (response.isSuccessful && cliente != null) {
                    atualizarTelaComDados(cliente)
                }
                else {
                    mostrarErro(response)
                }
            }

            override fun onFailure(call: Call<Cliente>, t: Throwable) {
                mostrarFalha(t)
            }

        }

        API().cliente.obter(idCliente).enqueue(callback)
    }

    fun atualizarTelaComDados(cliente: Cliente) {
        binding.editNome.setText(cliente.nome)
        binding.editEmail.setText(cliente.email)
        binding.editTelefone.setText(cliente.telefone)
    }

    fun editarCliente() {
        val cliente = obterClienteDaTela()

        val callback = object : Callback<Cliente> {
            override fun onResponse(call: Call<Cliente>, response: Response<Cliente>) {
                if (response.isSuccessful) {
                    mostrarSucesso()
                }
                else {
                    mostrarErro(response)
                }
            }

            override fun onFailure(call: Call<Cliente>, t: Throwable) {
                mostrarFalha(t)
            }

        }

        idEdicao?.let {
            API().cliente.atualizar(cliente, idEdicao).enqueue(callback)
        }
    }
}
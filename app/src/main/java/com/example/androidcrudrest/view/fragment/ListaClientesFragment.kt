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

/*
    Fragmento de listagem de clientes

    Utiliza elementos dinâmicos para listagem
 */
class ListaClientesFragment : Fragment() {
    /*
        Variável do binding automático, o tipo é
        criado automaticamente pelo android com base no XML
        do fragmento
    */
    lateinit var binding: FragmentListaClientesBinding

    //Função chamada quando o fragmento é criado e sua parte visual
    //está pronta para ser carregada
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //Carrega (infla) o layout usando a classe de binding
        binding = FragmentListaClientesBinding.inflate(inflater)

        /*
            Chama a função para fazer a listagem de clientes
        */
        listarClientes()

        /*
            Diferente da atividade, onde é necessário usar a função setContentView para
            definir o layout para que apareça na tela, no fragmento basta retornar o
            conteúdo da tela carregada, disponível na variável root do binding
        */
        return binding.root
    }

    /*
        Obtém a lista de clientes da API (GET)
        Quando os dados forem obtidos, serão colocados na
        tela usando elementos dinâmicos (função atualizarTela)
     */
    fun listarClientes() {

        /*
            Define o callback a ser executado quando o back end responder a
            chamada da função de obtenção da lista de clientes (GET)

            O tipo da função de callback (generics, diamond ou <>) precisa
            ser IDÊNTICO ao definido na classe de serviço onde esse callback
            será utilizado (mais abaixo, no último comando da função)
        */
        val callback = object : Callback<List<Cliente>> {
            /*
                O onResponse é chamado caso o retrofit consiga se conectar no servidor
                e obtenha uma resposta (que pode ser de sucesso ou de erro)
            */
            override fun onResponse(call: Call<List<Cliente>>, response: Response<List<Cliente>>) {
                //Verifica se a requisição deu certo (HTTP 2XX) ou erros
                //ocorreram (HTTP 4XX, 5XX, etc)
                if (response.isSuccessful) {
                    /*
                        Se a requisição teve sucesso, chama a função para atualizar
                        a tela, fornecendo a lista de clientes com os dados para ela

                        response.body() contém a lista de clientes que voltou da API
                        já convertida em lista do kotlin (List<Cliente>)
                    */
                    val listaClientes = response.body()
                    atualizarTela(listaClientes)
                }
                else {
                    //Se a chamada obteve erro, mostra uma mensagem para o usuário e loga o erro
                    Snackbar.make(binding.root,"Não foi possível recuperar a lista de clientes",Snackbar.LENGTH_LONG).show()
                    //Como o corpo do erro pode ser nulo, é preciso fazer uma verificação
                    val erro = response.errorBody()?.string()
                    erro?.let { Log.e("Erro", it) }
                }
            }

            /*
                O onFailure é chamado caso não seja possível bater no servidor,
                seja por problemas de conexão, endereço incorreto ou bloqueio de proxy
            */
            override fun onFailure(call: Call<List<Cliente>>, t: Throwable) {
                //Se não conseguiu se conectar ao servidor, mostra uma mensagem para o usuário e loga o erro
                Snackbar.make(binding.root, "Não foi possível recuperar a lista de clientes", Snackbar.LENGTH_LONG).show()
                //Como o corpo do erro pode ser nulo, é preciso fazer uma verificação
                val erro = t.message.toString()
                erro?.let { Log.e("Erro", it) }
            }

        }

        /*
            Solicita a execução do serviço de obtenção da lista de clientes pelo Retrofit
            Chamará o serviço respectivo no back end (GET) e executará
            o callback definido em callback quando concluído
        */
        API().cliente.listar().enqueue(callback)
    }

    /*
        Função dos elementos dinâmicos

        Recebe a lista de clientes que vieram do retrofit por parâmetro e,
        para cada cliente da lista, instancia um cartão do tipo item_cliente.xml,
        configura o conteúdo do cartão com os dados do cliente, bem como
        as closures dos botões, e coloca o cartão dentro do container do
        layout do fragmento fragment_lista_cliente.xml
     */
    fun atualizarTela(listaClientes: List<Cliente>?) {
        //Apaga todos os cartões já adicionados no container, se houver
        binding.container.removeAllViews()

        /*
            Percorre a lista de clientes e, para cada cliente
            executa a função closure abaixo. A variável cliente
            vai receber cada cliente da lista
        */
        listaClientes?.forEach { cliente ->
            //Cria um cartão dinâmico com base no XML item_cliente.xml
            val itemBinding = ItemClienteBinding.inflate(layoutInflater)

            //Configura as views do cartão com os dados do item da lista
            itemBinding.textNome.text = cliente.nome
            itemBinding.textEmail.text = cliente.email

            /*
                Configura as ações dos botões de edição e remoção
                para que chamem as funções respectivas, fornecendo
                o id do cliente para elas
            */
            itemBinding.buttonEditar.setOnClickListener {
                abrirFragmentoEdicao(cliente.id)
            }
            itemBinding.buttonExcluir.setOnClickListener {
                confirmarExclusão(cliente.id)
            }

            //Adiciona o cartão no container para que ele apareça na tela
            binding.container.addView(itemBinding.root)
        }
    }

    /*
        Função chamada para abrir o fragmento de edição,
        passando para o fragmento o id do cliente cujo cartão
        teve o botão de edição clicado

        idCli pode ser null (?) para facilitar a chamada nos elementos dinâmicos (acima)
     */
    fun abrirFragmentoEdicao(idCli: Int?) {
        //Se não foi fornecido um id por alguma razão, cancela a função
        if (idCli == null) {
            return
        }

        //Obtém a instância do gestor de fragmentos da atividade onde o fragmento atual está
        val supportFragmentManager = activity?.supportFragmentManager
        /*
            Como activity pode ser nula, o gesto de fragmento também pode ser, por
            isso fazemos essa verificação de nulo usando o let (caso supportFragmentManager
            seja null, o conteúdo da closure do let é ignorado
        */
        supportFragmentManager?.let {
            /*
                Cria uma instância do fragmento de inserção e edição
                fornecendo o id do cliente que deve ser exibido como parâmetro
            */
            val frag = AdicionarClienteFragment(idCli)
            /*
                Solicita que o gestor de fragmentos troque o fragmento atualmente no
                container pelo fragmento criado acima. Também adiciona a operação na pilha
                de retorno do Android, permitindo retornar ao fragmento anterior quando
                o botão voltar do sistema for pressionado
             */
            it.beginTransaction().replace(R.id.container, frag).addToBackStack("Editar").commit()
        }
    }

    /*
        Abre um diálogo para questionar o usuário da exclusão
        Caso ele clique em sim, chama a fuinção excluir para
        bater no endpoint de exclução. Caso clique em não, não
        faz nada e o diálogo fecha
     */
    fun confirmarExclusão(idCliente: Int?) {
        //Se não foi fornecido um id por alguma razão, cancela a função
        if (idCliente == null) {
            return
        }

        //Abre o diálogo de confirmação
        AlertDialog.Builder(requireContext())
            .setTitle("Confirmar Exclusão")
            .setMessage("Tem certeza que deseja excluir esse cliente? Essa operação não pode ser desfeita")
            .setPositiveButton("Sim") { dialgo, id ->
                //Chama a função de exclusão caso clique em sim
                //passando o ID do cliente a excluir pra ela
                excluir(idCliente)
            }
            //Caso clique em não, não faz nada (null), só fecha o diálogo
            .setNegativeButton("Não", null)
            .create()
            .show()
    }

    /*
        Função de exclusão, chamará a API solicitando a exclusão de um cliente (DELETE)
        O parâmetro da função indicará o cliente a excluir para API via parâmetro de URL
     */
    fun excluir(idCliente: Int) {

        /*
            Define o callback a ser executado quando o back end responder a
            chamada da função de remoção de cliente (DELETE)

            O tipo da função de callback (generics, diamond ou <>) precisa
            ser IDÊNTICO ao definido na classe de serviço onde esse callback
            será utilizado (mais abaixo, no último comando da função)
        */
        var callback = object : Callback<Void> {
            /*
                O onResponse é chamado caso o retrofit consiga se conectar no servidor
                e obtenha uma resposta (que pode ser de sucesso ou de erro)
            */
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                //Verifica se a requisição deu certo (HTTP 2XX) ou erros
                //ocorreram (HTTP 4XX, 5XX, etc)
                if (response.isSuccessful) {
                    /*
                        Se a chamada obteve sucesso, o cliente foi excluído, então
                        mostra uma mensagem de sucesso e solicita a execução da
                        função de listagem, para que a lista seja obtida novamente da
                        API (sem o elemento excluído) e a tela seja recarregada para
                        mostrar que o item foi realmente apagado
                    */
                    Snackbar.make(binding.root, "Dados apagados", Snackbar.LENGTH_LONG).show()
                    listarClientes()
                }
                else {
                    //Se a chamada obteve erro, mostra uma mensagem para o usuário e loga o erro
                    Snackbar.make(binding.root, "Não foi possível excluir", Snackbar.LENGTH_LONG).show()
                    //Como o corpo do erro pode ser nulo, é preciso fazer uma verificação
                    val erro = response.errorBody()?.string()
                    erro?.let { Log.e("Erro", it) }
                }
            }

            /*
                O onFailure é chamado caso não seja possível bater no servidor,
                seja por problemas de conexão, endereço incorreto ou bloqueio de proxy

                Mostrará uma mensagem para o usuário e logará o erro
            */
            override fun onFailure(call: Call<Void>, t: Throwable) {
                Snackbar.make(binding.root, "Não foi possível excluir", Snackbar.LENGTH_LONG).show()
                //Como o corpo do erro pode ser nulo, é preciso fazer uma verificação
                val erro = t.message.toString()
                erro?.let { Log.e("Erro", it) }
            }
        }

        /*
            Solicita a execução do serviço de exclusão pelo Retrofit
            Chamará o serviço respectivo no back end e executará
            o callback definido em callback quando concluído
         */
        API().cliente.excluir(idCliente).enqueue(callback)
    }
}


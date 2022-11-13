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

/*
    Fragmento de adição e edição de clientes

    Espera um parâmetro que indica o ID do cliente a ser
    exibido para edição. O parâmetro é nulável (?) para
    que, caso o fragmento esteja sendo aberto para inserção
    de um novo cliente, seja possível fornecer null como
    parâmetro.
 */
class AdicionarClienteFragment(val idEdicao: Int?) : Fragment() {
    /*
        Variável do binding automático, o tipo é
        criado automaticamente pelo android com base no XML
        do fragmento
    */
    lateinit var binding: FragmentAdicionarClienteBinding

    //Função chamada quando o fragmento é criado e sua parte visual
    //está pronta para ser carregada
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, state: Bundle?): View? {
        //Carrega (infla) o layout usando a classe de binding
        binding = FragmentAdicionarClienteBinding.inflate(inflater)

        /*
            Se idEdicao não for nulo, está ocorrendo uma edição. Nesse caso,
            será preciso obter os dados do cliente chamando a API com o id
            que veio como parâmetro para o fragmento (idEdicao), visando
            colocar os dados que retornarem na tela para que o usuário
            possa editá-los
        */
        if (idEdicao != null) {
            obterDadosCliente(idEdicao)
        }

        //Configra o listener do evento de clique no botão de salvar
        binding.buttonSalvar.setOnClickListener {
            /*
                Se idEdicao for nulo, está ocorrendo uma inserção.
                Nesse caso, o botão salvar deve chamar a função que
                irá solicitar à API a inserção desse cliente

                Se idEdicao tiver valor (else), está ocorrendo uma edição.
                Nesse caso, o botão salvar deve chamar a função que
                irá solicitar à API a edição dos dados do cliente
                cujo ID foi fornecido como parâmetro para o fragmento
             */
            if (idEdicao == null) {
                inserirCliente()
            }
            else {
                editarCliente()
            }
        }

        /*
            Diferente da atividade, onde é necessário usar a função setContentView para
            definir o layout para que apareça na tela, no fragmento basta retornar o
            conteúdo da tela carregada, disponível na variável root do binding
        */
        return binding.root
    }

    /*
        Função chamada para executar a API de inserção de clientes (POST) e exibir
        uma mensagem de sucesso ao final (ou de erro caso ocorra algum problema)
     */
    fun inserirCliente() {
        /*
            Chama a função obterClienteDaTela() para obter uma instância de
            cliente com base nos dados que estão digitados nas caixas de texto
        */
        val cliente = obterClienteDaTela()

        /*
            Define o callback a ser executado quando o back end responder a
            chamada da função de inserção (POST)

            O tipo da função de callback (generics, diamond ou <>) precisa
            ser IDÊNTICO ao definido na classe de serviço onde esse callback
            será utilizado (mais abaixo, no último comando da função)
        */
        val callback = object : Callback<Cliente> {

            /*
                O onResponse é chamado caso o retrofit consiga se conectar no servidor
                e obtenha uma resposta (que pode ser de sucesso ou de erro)
            */
            override fun onResponse(call: Call<Cliente>, response: Response<Cliente>) {
                //Verifica se a requisição deu certo (HTTP 2XX) ou erros
                //ocorreram (HTTP 4XX, 5XX, etc)
                if (response.isSuccessful) {
                    /*
                        Se a chamada obteve sucesso, o cliente foi inserido, então
                        chama a função para tratar essa situação de sucesso
                    */
                    tratarSucesso()
                }
                else {
                    //Se a chamada obteve erro, chama a função para tratar essa situação
                    mostrarErro(response)
                }
            }

            /*
                O onFailure é chamado caso não seja possível bater no servidor,
                seja por problemas de conexão, endereço incorreto ou bloqueio de proxy
            */
            override fun onFailure(call: Call<Cliente>, t: Throwable) {
                //Se a chamada não obteve resposta, chama a função para tratar essa situação
                mostrarFalha(t)
            }

        }

        /*
            Solicita a execução do serviço de inserção pelo Retrofit
            Chamará o serviço respectivo no back end e executará
            o callback definido em callback quando concluído
        */
        API().cliente.inserir(cliente).enqueue(callback)
    }

    /*
        Obtém os dados de cliente da API (GET)
        Usado quando um id foi fornecido, visto que é
        preciso obter os dados para permitir que o usuário os edite
     */
    fun obterDadosCliente(idCliente: Int) {

        /*
            Define o callback a ser executado quando o back end responder a
            chamada da função de obtenção de um cliente (GET)

            O tipo da função de callback (generics, diamond ou <>) precisa
            ser IDÊNTICO ao definido na classe de serviço onde esse callback
            será utilizado (mais abaixo, no último comando da função)
        */
        val callback = object : Callback<Cliente> {

            /*
                O onResponse é chamado caso o retrofit consiga se conectar no servidor
                e obtenha uma resposta (que pode ser de sucesso ou de erro)
            */
            override fun onResponse(call: Call<Cliente>, response: Response<Cliente>) {

                //Verifica se a requisição deu certo (HTTP 2XX) ou erros
                //ocorreram (HTTP 4XX, 5XX, etc)
                if (response.isSuccessful) {
                    /*
                        Se a requisição teve sucesso, verifica se há um corpo
                        na resposta e, caso tenha, chama a função para atualizar
                        a tela, fornecendo o cliente com os dados para ela
                    */
                    response.body()?.let {
                        atualizarTelaComDados(it)
                    }
                }
                else {
                    //Se a chamada obteve erro, chama a função para tratar essa situação
                    mostrarErro(response)
                }
            }

            /*
                O onFailure é chamado caso não seja possível bater no servidor,
                seja por problemas de conexão, endereço incorreto ou bloqueio de proxy
            */
            override fun onFailure(call: Call<Cliente>, t: Throwable) {
                //Se a chamada não obteve resposta, chama a função para tratar essa situação
                mostrarFalha(t)
            }

        }

        /*
            Solicita a execução do serviço de obtenção de dados pelo Retrofit
            Chamará o serviço respectivo no back end e executará
            o callback definido em callback quando concluído
        */
        API().cliente.obter(idCliente).enqueue(callback)
    }

    /*
        Função chamada para executar a API de edição de clientes (PUT) e exibir
        uma mensagem de sucesso ao final (ou de erro caso ocorra algum problema)
     */
    fun editarCliente() {
        /*
            Chama a função obterClienteDaTela() para obter uma instância de
            cliente com base nos dados que estão digitados nas caixas de texto
        */
        val cliente = obterClienteDaTela()

        /*
            Define o callback a ser executado quando o back end responder a
            chamada da função de edição (PUT)
            O tipo da função de callback (generics, diamond ou <>) precisa
            ser IDÊNTICO ao definido na classe de serviço onde esse callback
            será utilizado (mais abaixo, no último comando da função)
        */
        val callback = object : Callback<Cliente> {

            /*
                O onResponse é chamado caso o retrofit consiga se conectar no servidor
                e obtenha uma resposta (que pode ser de sucesso ou de erro)
            */
            override fun onResponse(call: Call<Cliente>, response: Response<Cliente>) {
                //Verifica se a requisição deu certo (HTTP 2XX) ou erros
                //ocorreram (HTTP 4XX, 5XX, etc)
                if (response.isSuccessful) {
                    /*
                        Se a chamada obteve sucesso, o cliente foi editado, então
                        chama a função para tratar essa situação de sucesso
                    */
                    tratarSucesso()
                }
                else {
                    //Se a chamada obteve erro, chama a função para tratar essa situação
                    mostrarErro(response)
                }
            }

            /*
                O onFailure é chamado caso não seja possível bater no servidor,
                seja por problemas de conexão, endereço incorreto ou bloqueio de proxy
            */
            override fun onFailure(call: Call<Cliente>, t: Throwable) {
                //Se a chamada não obteve resposta, chama a função para tratar essa situação
                mostrarFalha(t)
            }

        }

        /*
            Solicita a execução do serviço de edição pelo Retrofit
            Chamará o serviço respectivo no back end e executará
            o callback definido em callback quando concluído
            É necessário verificar se idEdicao não é nulo
            com um let, visto que a variável é opcional (?)
         */
        idEdicao?.let {
            API().cliente.atualizar(cliente, it).enqueue(callback)
        }
    }

    //Funções secundárias e auxiliares

    /*
        Obtém os dados dos campos de texto da tela e os transforma
        em um objeto da classe de modelo de cliente, a ser utilizado
        para enviar à API no corpo da requisição, tanto na inserção
        quanto na atualização
    */
    fun obterClienteDaTela(): Cliente {
        val nome = binding.editNome.text.toString()
        val email = binding.editEmail.text.toString()
        val tel = binding.editTelefone.text.toString()

        return Cliente(null, nome, email, tel)
    }

    /*
        Pega os dados do objeto de cliente e os coloca na tela
        Utilizado quando a API de obtenção de clientes responder
    */
    private fun atualizarTelaComDados(cliente: Cliente) {
        binding.editNome.setText(cliente.nome)
        binding.editEmail.setText(cliente.email)
        binding.editTelefone.setText(cliente.telefone)
    }

    //Limpa os campos de texto da tela
    private fun limparTela() {
        binding.editNome.text.clear()
        binding.editEmail.text.clear()
        binding.editTelefone.text.clear()
    }

    //Mostra uma mensagem de falha de conexão e loga no console o problema
    //Chamado pelo onFailer
    private fun mostrarFalha(t: Throwable) {
        Snackbar.make(binding.root, "Ocorreu um problema ao salvar", Snackbar.LENGTH_LONG).show()
        //Como o corpo do erro pode ser nulo, é preciso fazer uma verificação
        val erro = t.message.toString()
        erro?.let { Log.e("Erro", it) }
    }

    //Mostra uma mensagem de erro e loga no console o problema
    //Chamado caso isSuccessful retorne false
    private fun mostrarErro(response: Response<Cliente>) {
        Snackbar.make(binding.root, "Ocorreu um problema ao salvar", Snackbar.LENGTH_LONG).show()
        //Como o corpo do erro pode ser nulo, é preciso fazer uma verificação
        val erro = response.errorBody()?.string()
        erro?.let { Log.e("Erro", it) }
    }

    //Mostra uma mensagem de sucesso, limpa os campos da tela e volta para a listagem
    //Chamado caso o salvamento ocorra com sucesso (isSuccessful)
    private fun tratarSucesso() {
        Snackbar.make(binding.root, "Dados salvos", Snackbar.LENGTH_LONG).show()
        limparTela()
        voltarParaListagem()
    }

    //Recarrega o fragmento de listagem
    private fun voltarParaListagem() {
        /*
            Cria uma instância do fragmento de listagem fornecendo
            o id do cliente que deve ser exibido como parâmetro
        */

        val frag = ListaClientesFragment()
        /*
            Solicita que o gestor de fragmentos troque o fragmento atualmente no
            container pelo fragmento criado acima. Também adiciona a operação na pilha
            de retorno do Android, permitindo retornar ao fragmento anterior quando
            o botão voltar do sistema for pressionado
         */
        activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.container, frag)?.addToBackStack("Lista")?.commit()
    }
}
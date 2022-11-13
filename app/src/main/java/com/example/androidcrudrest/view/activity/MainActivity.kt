package com.example.androidcrudrest.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.androidcrudrest.R
import com.example.androidcrudrest.databinding.ActivityMainBinding
import com.example.androidcrudrest.databinding.FragmentAdicionarClienteBinding
import com.example.androidcrudrest.view.fragment.AdicionarClienteFragment
import com.example.androidcrudrest.view.fragment.ListaClientesFragment

/*
    Atividade principal, onde há o menu da bottom navigation e
    o container para carregamento dos fragmentos
 */
class MainActivity : AppCompatActivity() {
    //Variável do binding automático, o tipo é
    //criado automaticamente pelo android com base no XML
    //da atividade
    lateinit var binding: ActivityMainBinding

    //Função chamada quando a atividade é criada
    override fun onCreate(savedInstanceState: Bundle?) {
        //Chama função de criação da atividade pai
        super.onCreate(savedInstanceState)
        //Carrega (infla) o layout usando a classe de binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        //Configura o layout para que apareça na tela
        setContentView(binding.root)

        //Listener chamado quando uma oção da bottom navigation é selecionada
        binding.bottomNavigation.setOnItemSelectedListener {

            /*
                it, o parâmetro dessa closure, é o item de menu que foi clicado
                O itemId de it contém o id desse menu. Usamos o id para verificar
                qual foi o item clicado através de uma estrutura when
            */
            when(it.itemId) {
                //Se o id do item clicado foi adicionar...
                R.id.adicionar -> {
                    /*
                        Cria uma instância do fragmento AdicionarClienteFragment. Esse fragmento
                        é utilizado tanto para inserção quanto para edição. Isso é feito através
                        do parâmetro idEdicao em seu construtor, que indica se ele deve
                        tratar sua abertura como uma edição (caso o parâmetro seja fornecido,
                        o utilizando como base para obter o cliente com aquele ID e permitir
                        a edição dos dados) ou como inserção (caso não seja fornecido, ou seja,
                        seja nulo).

                        Como queremos abrir o fragmento para inserção nesse caso, fornecemos null
                     */
                    val frag = AdicionarClienteFragment(null)
                    /*
                        Solicite que o gestor de fragmento troque o fragmento atualmente no
                        container pelo fragmento criado acima. Também adiciona a operação na pilha
                        de retorno do Android, permitindo retornar ao fragmento anterior quando
                        o botão voltar do sistema for pressionado
                     */
                    supportFragmentManager.beginTransaction().replace(R.id.container, frag).addToBackStack("Adicionar").commit()
                }
                R.id.listar -> {
                    //Cria uma instância do fragmento de listagem
                    val frag = ListaClientesFragment()
                    /*
                        Solicite que o gestor de fragmento troque o fragmento atualmente no
                        container pelo fragmento criado acima. Também adiciona a operação na pilha
                        de retorno do Android, permitindo retornar ao fragmento anterior quando
                        o botão voltar do sistema for pressionado
                     */
                    supportFragmentManager.beginTransaction().replace(R.id.container, frag).addToBackStack("Listar").commit()
                }
            }

            //Retorna true para o Android, indicando que esse menu foi utilizado
            //Não é necessário o comando return em closures
            true
        }
    }
}
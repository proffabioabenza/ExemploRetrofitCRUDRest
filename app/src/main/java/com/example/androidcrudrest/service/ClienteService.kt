package com.example.androidcrudrest.service

import com.example.androidcrudrest.model.Cliente
import retrofit2.Call
import retrofit2.http.*
/*
    Interface de serviço, usada para mapear todos os endpoints
    de determinada entidade da API. Cada função é mapeada para
    um endpoint, possui a definição de qual método HTTP será
    chamado no endpoint, os parâmetros (de URL (/endereco/valor),
    GET (?chave=valor) ou de Body (corpo do post) e o tipo de
    retorno da API.

    Todas as funções de serviços retornam Call, cujo subtipo
    (generics, diamond ou <>) especifica o tipo do retorno.

    Por exemplo, se o endpoint retorna um array de clientes em JSON,
    o tipo de Call deve ser Call<List<Cliente>. Se o endpoint retorna
    apenas um cliente, o tipo do retorno deve ser Call<Cliente> e assim
    por diante.

    Não é necessário especificar o endereço base nos funções, apenas
    o que vem após a primeira barra depois da URL base. Por exemplo,
    caso a URL completa do endpoint fosse http://minhaapi.com/cliente,
    http://minhaapi.com/ seria a URL base e "cliente" o endereço específico
    do endpoint, mapeado nas funções abaixo

    As funções a seguir utilizam parâmetros de caminho (path params).
    Dependendo do serviço, pode ser necessário fornecer os parâmetros em
    formato de parâmetros GET (http://minhaapi.com/cliente?chave=valor),
    o que é feito com a ajuda da anotação @Query("chave") no lugar de
    @Path("chave"). Nesse caso não é preciso colocar os {parametro} na
    URL também.
 */
interface ClienteService {

    /*
        Listar, função GET da API chamada para listagem de todas
        as entidades

        Nesse caso, o get é chamado sem parâmetros de URL, o que
        faz com que o serviço da API (json-server por exemplo)
        retorne um array JSON de clientes, que será convertido
        para uma lista de clientes no Kotlin, como indica o tipo
        do retorno de Call (List<Cliente>)
     */
    @GET("cliente")
    fun listar(): Call<List<Cliente>>

    /*
        Obter, função GET da API com o id como parâmetro de URL,
        chamada para obtenção de uma única entidade

        Nesse caso, o get é chamado com um parâmetro de URL, o id
        o que faz com que o serviço da API (json-server por exemplo)
        retorne um único objeto JSON de cliente que contenha o id do
        parâmetro. O objeto JSON será convertido para um objeto da
        data class Cliente do Kotlin, como indica o tipo
        do retorno de Call (<Cliente>)

        Parâmetros de caminho (path params) fazem parte da própria URL,
        por exemplo http://minhaapi.com/cliente/1, onde 1 é o ID do
        cliente que deve ser recuperado. Para mapear um parâmetro de
        caminho, inserimos o nome do parâmetro entre chaves, como
        {parametro} nos locais adequados da URL, criamos um parâmetro
        na função do Kotlin do tipo desejado e mapeamos esse parâmetro
        da função Kotlin para o {parametro} na URL através da anotação
        @Path("parametro") na frente do parâmetro da função, onde
        "parametro" é o nome do parâmetro mapeado em {parametro}.

        O retrofit fará a substituição do {parametro} na URL pelo valor
        fornecido no parâmetro da função kotlin automaticamente.

        Podem haver mais de um parâmetro
        de URL por endpoint, bastando haver múltiplos {parametro} na URL
        e mais de um parâmetro na função, todos eles com a anotação @Path.
     */
    @GET("cliente/{id}")
    fun obter(@Path("id") id: Int): Call<Cliente>

    /*
        Inserir, função POST da API chamada para inserção de uma nova
        instância da entidade

        As funções posts normalmente armazenam os dados a serem inseridos
        no corpo da requisição, também chamado de body

        Para especificar um corpo da requisição, basta criar um parâmetro
        na função kotlin do tipo desejado e mapeá-lo com a anotação (@)
        @Body na sua frente

        Normalmente, os tipos de parâmetros body costumam ser as classes de
        modelo do kotlin (data classes) que representam as entidades

        Métodos do tipo post normalmente retornam o próprio item que foi
        inserido, por isso o mapeamento de Call com <Cliente>
     */
    @POST("cliente")
    fun inserir(@Body cliente: Cliente): Call<Cliente>

    /*
        Atualizar, função PUT da API, chamada para editar os dados de uma
        entidade com os dados e o id fornecido como parâmetro

        Funções do tipo put são como uma mescla dos gets para obtenção
        de um item e dos posts para inserção. É necessário configurar um
        parâmetro com os dados atualizados no formato da classe de modelo
        kotlin (classe de entidade) para que seja configurado no corpo da
        requisição (usando a anotação @Body na frente do parâmetro) e um
        parâmetro de caminho (path param) com o id do item a ser editado,
        mapeado através da anotação @Path("id") e o item da URL {id}

        Funções do tipo put normalmente retornam a entidade atualizada,
        por isso o retorno está configurado como Call<Cliente>
     */
    @PUT("cliente/{id}")
    fun atualizar(@Body cliente: Cliente, @Path("id") id: Int): Call<Cliente>

    /*
        Excluir, função DELETE da API, chamada para excluir uma entidade
        por id, fornecido como parâmetro

        A estrutura de funções do tipo delete são semelhante a funções get
        de obtenção de um único item, com um parâmetro de URL que indica o
        ID do elemento a excluir. A principal diferença é que não há retorno
        do endpoint (já que o item já foi excluído). Para isso, é possível
        utilizar o tipo de retorno especial do retrofit Void, que indica que
        não haverá dados no JSON de retorno da chamada da API
     */
    @DELETE("/cliente/{id}")
    fun excluir(@Path("id") id: Int): Call<Void>
}
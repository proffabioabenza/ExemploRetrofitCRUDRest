package com.example.androidcrudrest.model

/*
    Dados do modelo, classe equivalente ao retorno
    JSON da API

    O modelo de objeto do Kotlin precisa ter
    estrutura (nomes, tipos, hierarquia) IDÊNTICA
    ao JSON que retorna do json-server/Laravel, caso
    contrário ocorrerão erros diversos na chamada
    do Retrofit
 */
data class Cliente(
    /*
        O id aqui é definido como opcional (ou podendo
        ser nulo) para que seja possível criar uma instância
        de cliente sem a necessidade de fornecer um id e para
        que o JSON enviado para o backend em situações de inserção
        ou atualização não contenha um valor padrão para id, o qu
        poderia causar problemas nas funções do back
     */
    var id: Int? = null,

    var nome: String,
    var email: String,

    /*
        Caso algum parâmetero seja opcional, é possível fornecer um
        valor padrão a variável, como abaixo (semelhante ao Angular)

        Está aqui apenas para exemplo
     */
    var telefone: String = ""
)

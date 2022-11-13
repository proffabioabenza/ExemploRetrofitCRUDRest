package com.example.androidcrudrest.model

/*Dados do modelo
    O modelo de objeto do Kotlin precisa ter
    estrutura (nomes, tipos, hierarquia) IDÊNTICA
    ao JSON que retorna do Laravel, caso contrário
    ocorrerão erros diversos na chamada do Retrofit
 */
data class Cliente(
    /*
        O id aqui é definido como opcional (ou podendo
        ser nulo) para que seja possível criar uma instância
        de cliente sem a necessidade de fornecer um id e para
        que o JSON enviado para o backend em situações de inserção
        não contenha um valor padrão para id, o que poderia causar
        problemas nas funções de atualização do back end
     */
    var id: Int? = null,

    var nome: String,
    var email: String,

    /*
        Caso algum parâmetero seja opcional, é possível fornecer um
        valor padrão a variável, como abaixo (semelhante ao Angular)
     */
    var telefone: String = ""
)

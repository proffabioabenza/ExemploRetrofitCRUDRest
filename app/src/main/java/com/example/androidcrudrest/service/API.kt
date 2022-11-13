package com.example.androidcrudrest.service

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/*
    Classe usada para centralizar a instância
    do retrofit num único local, bem como todos as
    instâncias dos serviços que fazem parte da aplicação,
    facilitando as chamadas do Retrofit nos fragmentos
    e nas atividades
 */
class API {

    /*
        Instância do Retrofit usada pelas chamadas
        Aqui, a instância do retrofit será criada,
        especificando o endereço base da API,
        o tipo de dado que a API retorna (ex. JSON)
        e configurações, como timeout e log
    */
    private val retrofit: Retrofit
        /*
            O get é usado aqui para que, quando a variável
            retrofit acima for chamada, o código abaixo
            seja executado e o que é devolvido no return
            (a instância do Retrofit) retorne a quem chamou
        */
        get() {
            /*
                Aqui, estamos utilizando um interceptador para registrar
                todas as chamadas ao Laravel e colocar no console (Logcat
                ou Runtime/Debug) todos os dados da chamada, para facilitar
                o log de informações. O interceptador é configurado usando
                um cliente HTTP, aqui criado com auxílio da classe OkHttpClient
                e é completamente opcional (mas pode ajudar a debugar problemas)
             */
            val builder = OkHttpClient.Builder()
            builder.networkInterceptors().add(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            val client = builder.build()

            /*
                Aqui a instância do retrofit é criada, configurando o endereço da API,
                o cliente HTTP (com o interceptador acima, na variável client), o
                endereço base (endereço da API até a primeira barra após http://)
                e especificando  que JSON será usado pela API (com o GsonConverterFactory)
             */
            return Retrofit
                .Builder()
                    /*
                        10.0.2.2 é a ponte para localhost pelo emulador
                        Substituir pelo IP da máquina caso esteja num celular Android
                        A máquina onde está o JSON Server e o dispositivo Android
                        precisam estar na mesma rede wifi para que funcione
                    */

                    //TROQUE AQUI PARA SEU IP CASO USE UM CELULAR PARA TESTAR
                .baseUrl("http://10.0.2.2:3000/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }


    //Retorna os serviços para possibilitar a chamada da API

    //Serviço de cliente
    val cliente: ClienteService
        get() {
            /*
                Pede para o retrofit criar o serviço de cliente
                e o retorna quando a propriedade cliente acima
                for utilizada
            */
            return retrofit.create(ClienteService::class.java)
        }
}
package com.example.androidcrudrest.service

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class API {

    //Instância do Retrofit usada pelas chamadas
    private val retrofit: Retrofit
        get() {
            val builder = OkHttpClient.Builder()
            builder.networkInterceptors().add(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))

            val client = builder.build()


            return Retrofit
                .Builder()
                    //10.0.2.2 é a ponte para localhost pelo emulador
                    //Substituir pelo IP da máquina caso esteja num celular Android
                    //A máquina onde está o JSON Server e o dispositivo Android
                    //precisam estar na mesma rede wifi para que funcione
                .baseUrl("http://10.0.2.2:3000/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

    //Retorna os serviços para possibilitar a chamada
    //Serviço de cliente
    val cliente: ClienteService
        get() {
            return retrofit.create(ClienteService::class.java)
        }
}
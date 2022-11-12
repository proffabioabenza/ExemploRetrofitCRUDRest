package com.example.androidcrudrest.service

import com.example.androidcrudrest.model.Cliente
import retrofit2.Call
import retrofit2.http.*

interface ClienteService {

    //Listar, função GET da API do json-server
    @GET("/cliente")
    fun listar(): Call<List<Cliente>>

    //Obter, função GET da API do json-server com o id como parâmetro de URL
    @GET("/cliente/{id}")
    fun get(@Path("id") id: Int): Call<Cliente>

    //Inserir, função POST da API do json-server
    @POST("/cliente")
    fun inserir(@Body cliente: Cliente): Call<Cliente>

    //Atualizar, função PUT da API do json-server
    @PUT("/cliente/{id}")
    fun atualizar(@Body cliente: Cliente, @Path("id") id: Int): Call<Cliente>

    //Excluir, função DELETE da API do json-server
    @DELETE("/cliente/{id}")
    fun excluir(@Path("id") id: Int): Call<Cliente>
}
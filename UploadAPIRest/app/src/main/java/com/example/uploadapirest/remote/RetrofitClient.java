package com.example.uploadapirest.remote;

import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitClient
{
    private static Retrofit retrofit = null;
    //static porque retrofit vai ser usado S-E-M-P-R-E, então é bom já deixar disponível. Substitui a conexão com o banco SQLite
    //representa o retrofitCliente

    // ACESSO AO CLIENT
    public static Retrofit getClient(String url) //essa parte é como a barra de endereco do postman ou dos nav.
    {//devolve o retrofit instanciado, logo, retorna um cliente.

        if (retrofit == null)
        {
            retrofit = new Retrofit.Builder().baseUrl(url).addConverterFactory(ScalarsConverterFactory.create()).build();
            //se não existir, ele cria um retrofit; diz onde ele deve bater e quem vai lidar com os dados de forms (scalars)
        }

        return retrofit;
    }
}

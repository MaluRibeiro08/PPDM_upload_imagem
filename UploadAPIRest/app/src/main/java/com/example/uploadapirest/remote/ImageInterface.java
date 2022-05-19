package com.example.uploadapirest.remote;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ImageInterface
{

    @FormUrlEncoded // necessario para que os dadoas possam ser enviados via form (tipo o enctype de fgrotn web)
    @POST ("/testeUpload")
    Call<String> uploadImage(@Field("file") String file);
    //passsaremos campos por campo //o nome dos parentesis de file tem que ser o mesmo do que se pega na API. // nao usamos gson, porque nao vamos usar json
}

package com.example.uploadapirest;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.uploadapirest.remote.APIUtil;
import com.example.uploadapirest.remote.ImageInterface;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
{

    ImageView imgLivro;
    EditText txtTituloLivro;
    Button btnCadastrarLivro;
    private final int GALLERY = 1;

    ImageInterface imageInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imgLivro = findViewById(R.id.img_livro);
        txtTituloLivro = findViewById(R.id.txt_titulo);
        btnCadastrarLivro = findViewById(R.id.btn_cadastrar_livro);

        imageInterface = APIUtil.getAPIInterface();

        btnCadastrarLivro.setOnClickListener(
            view ->
            {
                Intent intent = new Intent
                                    (
                                        Intent.ACTION_PICK, //o pick permite abrir uma parte especifica do smartphone
                                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI //o priveder permite acessar funcionalidades do Sistema Operacional
                                    );
                intent.setType("image/*"); //tipo de arquivo a ser aceito

                startActivityForResult(Intent.createChooser(intent, "Selecione a imagem do livro"), GALLERY);

            }
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == this.RESULT_CANCELED)
        {
            return; //para a execucao dxo metodo
        }
        if (requestCode == GALLERY)
        {
            if (data != null) //veio alguma coisa?
            {
                Uri uri = data.getData(); //uri é responsável por manipular enderecos do recurso

                try
                {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                    //esse bitmap representa o recurso de imagem vindo do local URI que foi convertido para um MediaStote

                    imgLivro.setImageBitmap(bitmap);
                    Log.d("IMAGEM", "Imagem alterada");


                    //MANDANDO A IMAGEM - UPLOAD
                        uploadImageRetrofit(bitmap);
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                    Log.d("IMAGEM", e.getMessage());
                }
            }
        }//if galeria

    }//on actresul

    private void uploadImageRetrofit(Bitmap bitmapImagem)
    {
        //pegar o bitmap e dividir em fila de bits pra enviar pedacinho a pedacinho
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            //cria um array de bytes para saida

        bitmapImagem.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream); //formato, qualidade (100 máxima), bytearray

        String file = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT); //transforma o bitmap para string, representadpo pelo array de bytes

        Call<String> call = imageInterface.uploadImage(file, txtTituloLivro.getText().toString());

        call.enqueue(
                new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (response.isSuccessful())
                        {
                            Toast.makeText(MainActivity.this, "Upload de imagem realizado!", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t)
                    {
                        Log.d("upload-erro", t.getMessage());
                    }
                }
        );
    }

}
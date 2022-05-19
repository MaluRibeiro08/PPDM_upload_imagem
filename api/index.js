const express = require("express");
const fs = require("fs");
const Livro = require("./model/Livro");
const modelLivro = require("./model/Livro")

//const { off } = require("process");

const app = express(); 


app.use(express.json());
app.use(express.urlencoded //Mexer com arquivos enviados a partir de formularios/body
    (
        {
            extended : true, limit: '5MB'
        }
    )
);

app.post('/testeUpload', 
    (req, res) =>
    {
        const arquivo_enviado = req.body.file;
        let buffer = new Buffer.from(arquivo_enviado, 'base64'); //buffer é algo que a gente usa para traduzir aquivos que vieram em formatos "estranhos" ele é um espaco de memoria

        let imageName = './uploads/' + Date.now().toString() + '.jpg' //parametros: onde sera salvo, o nome e a extensao

        let titulo = req.body.titulo;


        console.log(arquivo_enviado); //base 64
        console.log(buffer);

        //REALIZANDO O UPLOAD COM FS
        fs.writeFileSync
        (
            imageName, //nome, destino e extensao da imagem
            buffer, //dados da imagem, imagem
            'base64', //o tipo de codificacao do argumento anterior
            (error)=> //callback - faz qualquer coisa
            {
                if(error)
                {
                    console.log (error);
                }
            }
        )  

        Livro.create
        (
            {
                titulo: titulo,
                imagem: imageName
            }
        ).then
        (
            ()=>
            {
                res.status(200);
            }
        );


    }
)


app.listen(3000, ()=>
{
    console.log("Servidor já está rodando em http://localhost:3000")
})
const express = require("express");
const fs = require("fs");
const { off } = require("process");

const app = express(); 


app.use(express.json());
app.use(express.urlencoded //Mexer com arquivos enviados a partir de formularios
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
        let buffer = new Buffer.from(arquivo_enviado, 'base64'); //buffer é algo que a gente usa para traduzir aquivos que vieram em formatos "estranhos"

        let imageName = './uploads/' + Date.now().toString() + '.jpg' //onde sera salvo, o nome e a extensao

        //REALIZANDO O UPLOAD COM FS
        fs.writeFileSync
        (
            imageName, //nome, destino e extensao da imagem
            buffer, //dados da imagem
            'base64', //o tipo de codificacao do argumento anterior
            (error)=> //callback
            {
                if(error)
                {
                    console.log (error);
                }
            }
        )
    }
)


app.listen(3000, ()=>
{
    console.log("Servidor já está rodando em http://localhost:3000")
})
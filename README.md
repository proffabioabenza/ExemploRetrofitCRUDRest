# Aplicação Android em Kotlin com chamadas de API usando o Retrofit

## Sobre

A aplicação desse repositório é um CRUD simples feito em Kotlin que usa Retrofit para chamadas de API.
Para simplificar a configuração, o json-server é usado como exemplo de API e os dados do json-server
se encontram no arquivo db.json, na raiz do repositório.

## Como subir o back-end dessa aplicação

1. Obtenha o instalador do Node.js em https://nodejs.org/en/ (pule se estiver no Mac)
2. Instale o Node.js no seu sistema (se estiver usando Mac, instale usando o brew, obtendo-o em https://brew.sh/index_pt-br)
3. Abra um terminal (CMD no Windows) e instale o json-server usando o NPM com o comando abaixo:
```
npm install -g json-server
```
4. Clone o projeto desse respositório usando o Android Studio, o git por linha de comando, o
Github Desktop ou baixando o zip em Code > Download Zip do canto superior direito (extraia o
zip também nesse caso)
5. No terminal, vá até a pasta do projeto que acabou de clonar/baixar usando o comando CD
```
cd C:/Usuários/SEU_USUARIO/LUGAR_ONDE_ESTA_O_PROJETO/ExemploRetrofitCRUDRest
```
6. Inicie o json-server usando o arquivo db.json que está nessa pasta e os parâmetros a seguir:
```
json-server --host 0.0.0.0 --port 3000 --watch db.json
```
O significado dos parâmetros é:
- host 0.0.0.0 - Permite que o serviço seja chamado por outra máquina. Necesário para o emulador do Android
- port 3000 - Configura a porta do serviço como 3000
- watch db.json - O arquivo onde estão os dados
7. A API estará pronta para ser chamada pelo Postman ou pela aplicação Android. O recurso que utilizamos encontra-se em "cliente" (http://localhost:3000/cliente)

## Como abrir e configurar a aplicação Android (front-end)

1. Obtenha o instalador do Android Studio em https://developer.android.com/. Caso já tenha o Android Studio, pule para 4
2. Instale o Android Studio
3. Inicie o Android Studio e faça as configurações iniciais necessárias. Se já clonou ou puxou a aplicação na configuração do back-end, pule para 8
4. Configure o Github no Android Studio (File ou o Ícone da Engrenagem na Tela Inicial > Preferences > Version Control > Github). Caso já tenha configurado, pule esse passo
5. Faça um fork desse repositório através do botão Fork acima desta página
6. Vá até File > New > Project From Version Control ou, na tela inicial do Android Studio, clique no botão Get From VCS
7. Escolha o fork desse repositório na lista e clique em Clone
8. Quando o Android Studio terminar de configurar o projeto, clique em Device Manager e crie um novo emulador. Se já tiver um emulador, pule esse passo
9. Clique no botão "Run 'app'" para executar a aplicação. O json-server precisa ficar ativo para que ela funcione

Obs.: Para que a aplicação funcione num celular Android, será preciso colocar o endereço IP do seu computador (descubra com o comando ipconfig num terminal)
na classe API, substituindo o endereço padrão do localhost (10.0.2.2). O celular e o computador precisam estar conectados na mesma rede wifi

Projeto criado no Android Studio versão 2021.2.1

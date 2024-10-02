# Projeto de Exemplo com treinamento Local

Autor: José Henrique Siqueira Brandão

- Este projeto demonstra como configurar e testar uma aplicação Java com o PostgreSQL Local e Kafka utilizando Docker. 
- Para iniciar o Kafka utilize o docker-compose irá subir o Kafka e o PostgreSQL.
- O Kafka Drop utilizamos para visualizar os tópicos pelo navegador. (http://localhost:9000/)
- URL para utilizar no postman e startar a aplicação (POST e o BODY): http://localhost:8080/start

Body:
  {
    "nome": "Fulano",
    "telefone": "(11) 91234-5678"
  }

## Pré-requisitos

- [Docker](https://docs.docker.com/get-docker/) baixar o Docker desktop.
- Java 11 ou superior instalado
- Para iniciar execute o comando docker-compose up no diretório do arquivo docker-compose.yaml

>------------------------------------------------------------------------------------------------------------------------------
### ALGUNS COMANDOS PARA TESTES (Kafka):

### Acesse a conexão do Kafka primeiro:
docker exec -it kafka bash

### Listar tópicos:
kafka-topics --list --bootstrap-server localhost:9092

### Criar tópico:
kafka-topics --create --topic test-topic --bootstrap-server localhost:9092 --partitions 1 --replication-factor 1

------------------------------------------------------------------------------------------------------------------------------

Última atualização: 01/10/2024

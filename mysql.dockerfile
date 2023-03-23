# Use a imagem oficial do MySQL como imagem base
FROM mysql:latest

# Defina a senha do root do MySQL
ENV MYSQL_ROOT_PASSWORD password

# Defina o nome do banco de dados
ENV MYSQL_DATABASE pedido

# Copie um arquivo SQL para ser executado na inicialização do contêiner
COPY database.sql /database.sql
COPY database.sql /docker-entrypoint-initdb.d/

# Exponha a porta padrão do MySQL
EXPOSE 3306

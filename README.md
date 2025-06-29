# Close To Us
![Anotação 2025-06-29 175517](https://github.com/user-attachments/assets/fe1c984b-6079-4346-93bd-991b9c426b5e)

## Sobre o Projeto

"Close To Us" é uma aplicação web que permite aos usuários consultar informações sobre asteroides próximos à Terra, utilizando dados da API NeoWs (Near Earth Object Web Service) da NASA. Com uma interface moderna e intuitiva, os usuários podem selecionar um intervalo de datas para visualizar detalhes como diâmetro estimado, distância de aproximação, velocidade e se o asteroide é potencialmente perigoso.

O projeto é dividido em duas partes principais:
* **Frontend**: Uma aplicação web construída com HTML, CSS (utilizando Tailwind CSS para estilização rápida e responsiva) e JavaScript puro, responsável pela interface do usuário e pela comunicação com o backend.
* **Backend**: Uma API REST desenvolvida com Spring Boot (Java), que atua como um proxy para a API da NASA, processando as requisições do frontend e fornecendo os dados de forma estruturada.

## Funcionalidades

* **Consulta por Intervalo de Datas**: Permite buscar asteroides em um período específico (limitado a 7 dias devido à API da NASA).
* **Detalhes do Asteroide**: Exibe nome, diâmetro estimado, distância de aproximação, velocidade relativa e status de periculosidade.
* **Interface Intuitiva**: Design limpo e moderno com uma paleta de cores em tons de preto, branco e cinza.
* **Mensagens de Erro**: Exibe mensagens claras em caso de falha na comunicação ou na API.

## Tecnologias Utilizadas

**Frontend:**
* `HTML5`
* `CSS3` (com `Tailwind CSS`)
* `JavaScript` (ES6+)

**Backend (API - Spring Boot):**
* `Java`
* `Spring Boot`
* `Maven` (para gerenciamento de dependências)
* `WebClient` (para consumo da API externa da NASA)

**Outras Ferramentas/APIs:**
* `NASA NeoWs API` (API de asteroides próximos à Terra)
### 1. Obtenha uma Chave de API da NASA:**
    * Vá para o [site da API da NASA](https://api.nasa.gov/).
    * Inscreva-se para obter uma chave de API gratuita.

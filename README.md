# 🔥 The Flame Realm 2.0

![Linguagem](https://img.shields.io/badge/linguagem-Java%2017-orange)
![Framework](https://img.shields.io/badge/framework-libGDX%201.14.2-red)
![Build](https://img.shields.io/badge/build-Gradle-green)
![Status](https://img.shields.io/badge/status-em%20desenvolvimento-yellow)

> RPG de turnos com combate em perspectiva *side-view* e exploração em *bird-view*, feito em Java com libGDX. É o port de um jogo original escrito em Python/Pygame.

**🔗 [Demo](#)** • **💼 [LinkedIn](#)**

---

## 📌 Sobre o Projeto

The Flame Realm 2.0 é um RPG de turnos onde o jogador explora um mapa em visão superior (*bird-view*) e, ao alcançar pontos de encontro, entra em batalhas por turnos apresentadas em visão lateral (*side-view*). O jogo inclui menu principal, tela de história, tutorial, exploração, combate contra múltiplos inimigos — incluindo um chefe — além de telas de pausa e de morte.

O projeto nasceu como um **port de um jogo original em Python/Pygame para Java + libGDX**. A motivação foi reescrever a base de código em uma stack tipada e orientada a objetos, preservando fielmente o comportamento do original enquanto se aplicava uma arquitetura mais limpa e extensível. Boa parte dos arquivos são portes diretos, e os comentários no código citam o arquivo Python de origem — o que torna o projeto um exercício prático de tradução entre paradigmas e de engenharia de software aplicada a jogos.

### 🎥 Demonstração

![Screenshot da Aplicação](https://via.placeholder.com/800x400.png?text=The+Flame+Realm+2.0)

---

## 🛠️ Tecnologias Utilizadas

* **Linguagem:** Java 17
* **Framework de jogo:** libGDX 1.14.2
* **Renderização de fontes:** gdx-freetype (BitmapFont via FreeType)
* **Backend desktop:** LWJGL3
* **Build:** Gradle (com wrapper incluso)
* **Ferramentas:** Git

---

## ✨ Funcionalidades

- [x] Menu principal com navegação entre telas (história, tutorial, jogo).
- [x] Exploração do mapa em perspectiva *bird-view* com movimento do jogador.
- [x] Combate por turnos em perspectiva *side-view*.
- [x] Sistema de inimigos *data-driven*: adicionar um inimigo é declarar uma constante, sem criar classes novas.
- [x] Quatro inimigos declarados (Fire/Ice/Dark Phanton e o chefe Necromancer), cada um com HP, corpo e ataques próprios.
- [x] Sistema de animações por estado (idle, ataque, dano, morte) compartilhado entre jogador e inimigos.
- [x] Ataques do jogador e dos chefes com barras de HP e mana.
- [x] Telas de pausa e de morte.
- [x] Movimento e animação independentes da taxa de atualização (calibrados a 60 fps).

---

## 📐 Arquitetura e Decisões Técnicas

* **Composition root:** a classe `GameInstances` monta todos os objetos do jogo (textos, botões, ataques, personagens, mapa) numa ordem de dependência fixa, centralizando a construção do estado.
* **Template Method nas telas:** `BaseScreen` define o fluxo `handleInput → update(delta) → draw(delta)`. Toda mutação de estado de mundo vai em `update`; `draw` apenas desenha — separando lógica de renderização.
* **Inimigos data-driven:** o `Bestiary` é um registro estático de `EnemySpec` (nome, HP, corpo e ataques). Um `SpecEncounterManifest` genérico substitui manifestos por-chefe, de modo que novos inimigos são apenas dados declarativos.
* **Independência de FPS:** todo avanço de animação e movimento usa `update(float delta)` escalado por `REFERENCE_FPS`, preservando a velocidade calibrada a 60 fps independentemente do refresh rate do monitor.
* **Gerência de assets por contagem de referência:** o carregamento e a liberação de texturas/fontes aproveitam o `AssetManager` do libGDX, com liberação idempotente de encontros de combate para suportar assets compartilhados entre grupos.
* **Fidelidade ao original (câmera yDown):** a câmera mantém o eixo Y crescendo para baixo, imitando o Pygame original; sprites são invertidos verticalmente e a UI se posiciona a partir do canto superior-esquerdo. A escolha prioriza a fidelidade ao comportamento do jogo original em detrimento da convenção padrão (yUp) do libGDX.

---

## 🚀 Como Executar o Projeto

### Pré-requisitos

* Git instalado
* JDK 17 (ou superior)

O Gradle não precisa estar instalado globalmente — o projeto inclui o *wrapper* (`gradlew`).

### Passo a passo

1. **Clone o repositório:**
   ```bash
   git clone https://github.com/seu-usuario/TheFlameRealmJava.git
   cd TheFlameRealmJava
   ```

2. **Compile o projeto:**
   ```bash
   ./gradlew build
   ```

3. **Execute o jogo:**
   ```bash
   ./gradlew desktop:run
   ```

   A janela do jogo abre em modo *windowed* (900×600). No Windows, use `gradlew.bat` no lugar de `./gradlew`.

---

## 🧠 O Que Eu Aprendi / Desafios Enfrentados

* **Portar de Python/Pygame para Java/libGDX** exigiu traduzir código dinâmico e procedural para uma arquitetura tipada e orientada a objetos, mantendo o comportamento idêntico ao original.
* **Refatorar os inimigos para um modelo *data-driven*** (`Bestiary` + `EnemySpec`) eliminou a necessidade de uma classe por chefe, tornando a adição de conteúdo muito mais simples.
* **Desacoplar lógica de renderização** via Template Method (`update`/`draw`) e tornar o movimento independente de FPS foram passos importantes para um loop de jogo previsível em diferentes monitores.
* **Preservar *quirks* do original de propósito**: alguns comportamentos e coordenadas "calibradas no olho" foram mantidos e marcados por comentários, priorizando fidelidade sobre "correção" arbitrária.

---

## 📄 Licença

*A definir.*

---

## 📬 Contato

**Alessandro**

[![LinkedIn](https://img.shields.io/badge/LinkedIn-0077B5?logo=linkedin&logoColor=white)](#)
[![Email](https://img.shields.io/badge/Email-D14836?logo=gmail&logoColor=white)](mailto:alegifi2004@gmail.com)
[![GitHub](https://img.shields.io/badge/GitHub-100000?logo=github&logoColor=white)](#)

# Refatoração: separar `update` de `draw` (passo de update no Template Method)

## Objetivo

Eliminar a violação do Princípio da Responsabilidade Única no `CombatScreen`, onde
o método `draw` — que deveria só pintar — hoje também toma decisões de jogo (turno,
dano, HP/mana, revive, seleção de ataque do inimigo, avanço de animações e troca de
tela).

A solução é introduzir um passo `update(delta)` no Template Method do `BaseScreen`,
transformando o esqueleto do frame de **input → draw** em **input → update → draw**,
com cada passo tendo uma responsabilidade única:

- `handleInput` — reage ao toque (já existe hoje).
- `update(delta)` — faz o mundo avançar (lógica).
- `draw(delta)` — pinta o estado já resolvido.

## Regras de trabalho (do `CLAUDE.md`)

- Uma tarefa de cada vez: **não** misturar refatoração estrutural com mudança visual.
- Fidelidade ao comportamento do original é prioridade.
- Ao terminar cada fase, rodar `./gradlew build` e mostrar o `git diff` antes de fechar.
- Preservar o estado transiente dos singletons de tela (turno, timers, offset do mapa).

---

## Fase 0 — Andaime no `BaseScreen` (estrutural, sem mudança de comportamento)

Introduzir o passo `update` **sem** mudar nada de comportamento em nenhuma tela.
Nesta fase, o `CombatScreen` continua com toda a lógica dentro do `draw`.

### Novo hook

Adicionar ao `BaseScreen` um hook com implementação padrão vazia, com o **mesmo
contrato** do `handleInput`:

- `protected boolean update(float delta)` — retorna `true` se chamou `game.setScreen`
  (frame deve ser abortado). Default: `return false;`.

### Esqueleto do `render` (antes → depois)

Antes:

```
public final void render(float delta) {
    if (handleInput()) return;   // aborta se trocou de tela
    batch.begin();
    batch.setColor(Color.WHITE);
    draw(delta);
    batch.end();
}
```

Depois:

```
public final void render(float delta) {
    if (handleInput()) return;   // aborta se trocou de tela
    if (update(delta)) return;   // aborta se trocou de tela
    batch.begin();
    batch.setColor(Color.WHITE);
    draw(delta);
    batch.end();
}
```

### Pontos de projeto

- `update` roda **fora** do par `batch.begin()`/`batch.end()`, antes dele — é lógica,
  não deve tocar no `SpriteBatch`.
- Nenhuma das 7 telas sobrescreve `update` nesta fase → comportamento idêntico.

### Validação da Fase 0

- `./gradlew build` passa.
- `git diff` mostra que a única mudança real é no `BaseScreen`.
- Nenhuma diferença visível ao rodar o jogo.

---

## Fase 1 — Separação dentro do `CombatScreen` (estrutural, comportamento preservado)

Mover a lógica de jogo de dentro do `draw` para o novo `update`, deixando o `draw`
puramente visual. **Nada muda na tela nem na jogabilidade.**

### Inventário: o que vai para cada lado

**Vai para `update(delta)`** (decisões, mutação de estado e avanço de animações):

- Resolução de turno pelo timeout (tempo de espera estourou → volta a vez ao jogador
  e zera o ataque do inimigo).
- Detecção de **derrota** (HP do jogador em zero → revive ambos, reseta mana e HP,
  volta o ponto no mapa, `setScreen(death)`).
- Detecção de **vitória** (HP do chefe em zero → revive, reseta, desativa o ponto
  atual, grava o ponto anterior, `setScreen(play)`).
- Seleção e aplicação do ataque do inimigo (sortear ataque, sortear hit-kill, aplicar
  dano ao jogador, atualizar texto de HP).
- **Avanço** de todas as animações: `.update()` das duas entidades de combate, do
  ataque do jogador, do ataque do inimigo e da "fury of the eye".

**Fica em `draw(delta)`** (só pintura do estado já resolvido):

- Fundo da batalha, os dois personagens, barras de HP/mana, textos, botões de ataque
  e o botão secreto.
- Pintura do **frame atual** de cada animação ativa: imagem do ataque do jogador se
  não terminou, do ataque do inimigo dentro da janela de tempo, e da "fury of the eye"
  se ativa.
- O `draw` passa a apenas **ler** `getImage()` e os flags `isOver`, nunca a avançá-los.

Divisão limpa: o `update` decide qual é o quadro e se a animação acabou; o `draw` só
mostra o quadro que o `update` deixou pronto.

### Pontos críticos a preservar

1. **Ordem das operações.** A lógica movida deve manter exatamente a mesma sequência
   de cima para baixo do `draw` atual — o comportamento de turno depende dela.

2. **Avança-depois-lê das animações.** Hoje o padrão é "pinta a imagem, depois chama
   `.update()`" (lê-depois-passa). Ao mover o avanço para o `update` (que roda antes
   do `draw`), preservar **deliberadamente** essa ordem para não deslocar em ±1 quadro
   o começo/fim de uma animação nem antecipar em 1 frame o disparo das transições que
   leem `isOver`. Feito com cuidado, fica idêntico frame a frame.

3. **Timer intocado nesta fase.** O incremento de `elapsedSinceLastClick` (e o reset
   para 0 no ataque) fica onde está, no `handleInput`. Movê-lo mudaria a ordem
   incremento/reset dentro do frame e poderia alterar o timing. Migração do timer é
   tarefa futura separada.

4. **Transição sem desenho da tela velha (melhoria de brinde).** Como o `update` decide
   a troca de tela antes de qualquer desenho, no frame da transição nada da tela velha
   é pintado. É uma melhoria (evita flicker), mas é uma diferença de comportamento
   naquele único frame — esperado, não é regressão.

### Fora de escopo (tarefas futuras, cada uma separada)

- Extrair classe de controle / máquina de estados de combate.
- Unificar as duas checagens de "combate terminou" hoje espalhadas no `draw`.
- Transformar o `handleInput` em "só registrar intenção" (aplicação no `update`).
- Migrar o timer para o `update`.

### Validação da Fase 1

- `./gradlew build` passa.
- `git diff`: o `draw` só **perdeu** lógica — não ganhou nem alterou pintura.
- Jogar o combate cobrindo os 4 cenários que exercitam a lógica movida:
  1. Vencer um combate → vai para o mapa (`play`).
  2. Perder um combate → vai para a tela de morte (`death`).
  3. Gastar mana até o limite.
  4. Usar a "fury of the eye".
- Conferir visualmente que as animações de ataque rodam iguais (sem engasgo nem
  deslocamento perceptível).

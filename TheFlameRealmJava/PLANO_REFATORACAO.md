# Plano de refatoração arquitetural — The Flame Realm 2.0

Documento de handoff para o Claude Code. Descreve as **quatro** mudanças
faltantes (o item 4 — enum `Direction` — já foi implementado). São refatorações
de **melhoria estrutural**, não correção de bug: o comportamento do jogo deve
permanecer idêntico.

## Regras que valem para TODAS as tarefas

- **Fidelidade ao comportamento do original é prioridade absoluta.** Nenhuma
  mudança pode alterar timings, ordem de operações, posições de UI ou fluxo de
  telas. Todas as quatro tarefas são comportamento-preservantes.
- **Uma tarefa de cada vez.** Não misturar tarefas num mesmo commit. Ao final de
  cada uma: rodar `./gradlew build` e mostrar o `git diff` antes de finalizar.
- **Preservar o estado dos singletons.** `PlayScreen` e `CombatScreen` são
  singletons reaproveitados que guardam estado entre frames (offset do mapa,
  turno, timers). Não recriar nem zerar esse estado.
- **Preservar referências compartilhadas de `GameText`.** Em `GameInstances`, o
  mesmo objeto `GameText` é compartilhado entre um texto e o botão que o exibe,
  para que `setMessage(...)` reflita nos dois. Nunca duplicar esses objetos.
- **Não tocar na câmera yDown** nem no `SpriteBatch` compartilhado.
- **Ordem de execução:** Item 1 → Item 2 → Item 3a → Item 3b. O item 2 depende
  dos caminhos de acesso definidos pelo item 1; o 3b depende do 3a.

---

## Item 1 — Reagrupar `GameInstances` em sub-holders coesos

### Problema
`GameInstances` é um "composition root" legítimo (monta tudo em ordem de
dependência), mas expõe ~40 campos `public final` num namespace plano. Toda tela
alcança o bag inteiro via `instances.xxx`, gerando acoplamento alto.

### Objetivo
Manter o composition root, mas quebrar a superfície plana em holders por domínio.
`instances.playButton` passa a ser, por exemplo, `instances.menu.playButton`.

### Abordagem
1. Criar holders simples no pacote `instances` (classes com campos `public final`,
   sem lógica — apenas contêineres). Cada holder recebe seus objetos já
   construídos via construtor.
2. `GameInstances` continua construindo tudo na **mesma ordem** (Text → Buttons →
   Attacks → Characters → Map) e, ao final, monta os holders e os expõe como
   `public final`.
3. Atualizar os acessos `instances.xxx` em todas as 7 telas para o novo caminho.

### Agrupamento sugerido
Baseado no uso real por tela (ajuste se achar melhor, mas mantenha a coesão):

- **`menu`** — `playButton`, `historyButton`, `tutorialButton`, `quitButton`,
  seus textos (`playButtonText`, `historyButtonText`, `tutorialButtonText`,
  `quitButtonText`) e `mainMenuScreen`.
- **`nav`** (navegação compartilhada) — `returnToMainMenuButton`,
  `returnToGameButton`, `pauseButton`, `returnToMainMenuText`, `returnToGameText`.
- **`movement`** — `moveUpButton`, `moveDownButton`, `moveRightButton`,
  `moveLeftButton`.
- **`story`** (telas History/Tutorial) — `historyText`, `historySecretText`,
  `secretButton`, `tutorialMainText`, `tutorialManaText`, `tutorialMoveText`.
- **`combat`** — `playerCombatForm`, `playerCombatFormHpText`,
  `playerCombatFormManaText`, os quatro botões de ataque (`vortexButton`,
  `azuringButton`, `nebulaButton`, `razorButton`) e seus textos, `secretEyeButton`,
  os quatro `PlayerAttack` (`azuring`, `nebula`, `vortex`, `razor`),
  `playerAtkList`, `furyOfTheEye` e `battleScreen`.
- **`map`** — `gameMap`, `gameMapImage`, os cinco `FightPoint` (`startPoint`,
  `centralFightPoint`, `rightFightPoint`, `leftFightPoint`, `highFightPoint`),
  `playerObject` e os sprites `playerSpriteUp/Down/Right/Left`.
- **`overlays`** — `pauseText`, `deadText`.

### Atenção
- `TutorialScreen` usa `playerCombatFormManaText` (grupo `combat`) e os botões de
  movimento — acesso cruzado entre grupos é normal e aceitável.
- Vários `*Text` de botão (ex.: `vortexText`, `playButtonText`) só existem como
  texto do botão e nunca são lidos direto pela tela. Mantê-los no mesmo grupo do
  botão preserva a referência compartilhada. **Não demova para local nesta
  tarefa** (mudança separada, se desejada).

### Validação
`./gradlew build` limpo; abrir o jogo e percorrer todas as telas e botões,
confirmando comportamento idêntico. É um refactor mecânico: se compilou e navega
igual, está correto.

---

## Item 2 — Command nos botões de navegação

### Problema
`GameButton` só sabe testar hover (`mouseInButton()`) e desenhar. As ações vivem
nos `handleInput()` das telas como cadeias de `if`, e a lógica "voltar ao menu /
voltar ao jogo" está **duplicada idêntica** em `PauseScreen` e `DeathScreen`.

### Escopo
Aplica-se **apenas aos botões de navegação** (que só trocam de tela):
`returnToMainMenuButton`, `returnToGameButton`, `pauseButton`, `playButton`,
`historyButton`, `tutorialButton`, `quitButton`.

**Fora de escopo:** botões de ataque do combate (`vortexButton` etc.) e o
`secretEyeButton` — estão entrelaçados com turno/mana/estado de combate e NÃO
devem virar Command. O `secretButton` da History apenas alterna um `boolean`
local (`secretReveal`), não troca de tela — também fica manual.

### Abordagem
1. Em `GameButton`, adicionar uma ação opcional que devolve se houve troca de tela
   (para respeitar o contrato do Template Method de abortar o frame):

   ```java
   import java.util.function.BooleanSupplier;
   // ...
   private BooleanSupplier action; // retorna true se trocou de tela

   public void setAction(BooleanSupplier action) { this.action = action; }
   public boolean fire() { return action != null && action.getAsBoolean(); }
   ```

2. Em `BaseScreen`, um helper que dispara o primeiro botão sob o mouse:

   ```java
   protected boolean clickFirst(GameButton... buttons) {
       if (!Gdx.input.justTouched()) return false;
       for (GameButton b : buttons)
           if (b.mouseInButton()) return b.fire();
       return false;
   }
   ```

3. Ligar as ações **no fim de `FlameRealmGame.onCoreLoaded()`**, depois das telas
   existirem (as ações referenciam `game.play`, `game.mainMenu`, etc.). Como os
   botões de nav são objetos compartilhados, liga-se uma vez só:

   ```java
   instances.nav.returnToMainMenuButton.setAction(() -> { setScreen(mainMenu); return true; });
   instances.nav.returnToGameButton.setAction(()   -> { setScreen(play);     return true; });
   instances.nav.pauseButton.setAction(()          -> { setScreen(pause);    return true; });
   instances.menu.playButton.setAction(()          -> { setScreen(play);     return true; });
   instances.menu.historyButton.setAction(()       -> { setScreen(history);  return true; });
   instances.menu.tutorialButton.setAction(()      -> { setScreen(tutorial); return true; });
   instances.menu.quitButton.setAction(()          -> { Gdx.app.exit();      return true; });
   ```
   (Caminhos `instances.nav.*` / `instances.menu.*` assumem o item 1 já feito.)

4. Simplificar os `handleInput()`:
   - **PauseScreen** e **DeathScreen** (hoje idênticos):
     `return clickFirst(returnToMainMenuButton, returnToGameButton);`
   - **MainMenuScreen:**
     `return clickFirst(quitButton, playButton, historyButton, tutorialButton);`
   - **TutorialScreen:** `return clickFirst(returnToMainMenuButton);`
   - **HistoryScreen:** disparar a nav via Command e manter o toggle manual:
     ```java
     if (clickFirst(returnToMainMenuButton)) return true;
     if (!Gdx.input.justTouched()) return false;
     if (instances.story.secretButton.mouseInButton()) secretReveal = !secretReveal;
     return false;
     ```
   - **PlayScreen:** preservar o **quirk** do `returnToMainMenuButton` (verificado
     mas nunca desenhado) e a ordem original. Substituir só a parte de navegação:
     ```java
     if (clickFirst(returnToMainMenuButton, pauseButton)) return true;
     ```
     e manter TODA a lógica de movimento como está. Conferir que a ordem de
     verificação (returnToMainMenu antes de pause antes de movimento) não muda.

### Validação
`./gradlew build` limpo. Clicar cada botão de navegação em cada tela e confirmar
o destino. Conferir que o easter-egg do texto secreto (History) e o movimento
(Play) seguem funcionando. Confirmar que `PauseScreen`/`DeathScreen` ficaram sem
duplicação.

---

## Item 3a — Fatiar `CombatScreen.update()` em fases (extração de métodos)

### Problema
`CombatScreen.update()` é um método-Deus: mistura turno, timers, sequência de
morte do player, sequência de vitória (morte do boss), animação e `release` de
assets, tudo controlado por limiares implícitos de `elapsedSinceLastClick` e
`turnoAtual`.

### Objetivo
Tornar as fases legíveis **sem mudar comportamento**. Extração mecânica de
métodos privados nomeados; nada de nova lógica.

### Abordagem
Manter todos os campos de estado atuais (`turnoAtual`, `playerLastAtk`,
`enemyLastAtk`, `elapsedSinceLastClick`, `usedFuryOfTheEye`, `encounter`). Quebrar
`update()` em métodos privados que **preservam exatamente a ordem atual**:

```java
protected boolean update(float delta) {
    elapsedSinceLastClick += delta;
    updateFury(delta);
    if (resolveTurnTimeout(delta)) return true;   // reset p/ player + checagem de morte -> DeathScreen
    if (resolvePlayerAttack(delta)) return true;  // anima ataque OU vitória (boss morto) -> loading/play
    resolveEnemyTurn(delta);                       // janela de contra-ataque do inimigo
    encounter.boss.update(delta);
    instances.combat.playerCombatForm.update(delta);
    return false;
}
```

- Cada método recebe `delta`, devolve `boolean` (true = `setScreen` chamado, frame
  abortado) e propaga o retorno.
- **Não alterar** os thresholds (`WAIT_TIME`, `WAIT_TIME / 2f`), nem a ordem, nem
  as mensagens de HP/mana, nem os `setState(AnimState.*)`.
- Um `enum CombatPhase` explícito é **opcional** aqui; se ajudar a leitura, pode
  entrar, mas o essencial é a extração de métodos. `draw()` fica intacto.

### Validação
`./gradlew build` limpo. Testar o combate ponta a ponta: atacar com cada golpe,
usar o Fury of the Eye, morrer (ir para DeathScreen), matar o boss (transição via
loading para o mapa). Conferir que os tempos de turno e as animações ficaram
idênticos ao de antes.

---

## Item 3b — Extrair `CombatController` de domínio

### Objetivo
Separar **regra de combate** de **renderização**, como o resto do projeto já faz
(o `draw` que só desenha). A `CombatScreen` vira uma casca fina que dirige um
`CombatController`.

### Abordagem
1. Criar `CombatController` (pacote novo `combat` ou dentro de `characters`),
   dono do estado de combate hoje na tela: `turnoAtual`, `playerLastAtk`,
   `enemyLastAtk`, `elapsedSinceLastClick`, `usedFuryOfTheEye`, `encounter`, mais
   as referências de que precisa (`playerCombatForm`, textos, botões de ataque).
2. Mover para o controller a lógica de `setEncounter(...)`, o tratamento de clique
   (o que hoje é `handleInput`) e o `update(delta)` já fatiado no item 3a.
3. **Navegação continua na tela.** O controller não chama `game.setScreen`. Ele
   devolve um resultado e a tela decide:
   ```java
   enum CombatOutcome { ONGOING, DEFEAT, VICTORY }
   ```
   - `DEFEAT` → a tela faz `game.setScreen(game.death)` (com o `release`/revive
     como hoje).
   - `VICTORY` → a tela dispara `game.loading.beginTransition(...)` para `game.play`.
   Assim a regra de combate fica testável e livre de libGDX de navegação.
4. `CombatScreen.draw()` passa a ler o estado do controller (getters) em vez de
   campos próprios.

### Atenção
- Preservar o comportamento de singleton: o controller deve sobreviver entre
  frames junto com a tela.
- Manter os efeitos colaterais na **mesma ordem** (mensagens de HP/mana, `setState`,
  `release` idempotente do encontro, `revive`, atualização do `gameMap`).
- Fazer **depois** do 3a e validar o 3a antes, para as fronteiras do controller já
  estarem desenhadas.

### Validação
Igual ao 3a (combate ponta a ponta), mais conferir que os dois caminhos de saída
(derrota e vitória) preservam o revive do player, o revive do boss, o
`disableCurrentPoint`/`setPreviousPoint` e o `release` dos assets do encontro.

---

## Checklist final (para cada item)
- [ ] `./gradlew build` sem erros nem warnings novos
- [ ] `git diff` revisado e coeso (um item por commit)
- [ ] Jogo aberto e o fluxo afetado testado manualmente
- [ ] Comportamento idêntico ao anterior (fidelidade)

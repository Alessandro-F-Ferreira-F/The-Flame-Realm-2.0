package com.flamerealm.game.characters;

import com.flamerealm.game.attacks.BossAttackSpec;

import java.util.List;

/** Dados declarativos de um inimigo: nome, vida maxima, corpo e ataques. */
public record EnemySpec(String name, int maxHp, CombatBodySpec body, List<BossAttackSpec> attacks) {}

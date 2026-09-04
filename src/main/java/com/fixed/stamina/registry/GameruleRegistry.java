package com.fixed.stamina.registry;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.serialization.Codec;
import com.fixed.stamina.FixedStamina;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.Registry;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.level.gamerules.GameRule;
import net.minecraft.world.level.gamerules.GameRuleCategory;
import net.minecraft.world.level.gamerules.GameRuleType;
import net.minecraft.world.level.gamerules.GameRuleTypeVisitor;
import net.minecraft.world.level.gamerules.GameRules;

import java.util.function.ToIntFunction;

public class GameruleRegistry {

    public static GameRule<Integer> STAMINA_DRAIN_SPEED;

    public static GameRule<Integer> RESPAWN_HEALTH_PERCENTAGE;
    public static GameRule<Integer> RESPAWN_HUNGER_PERCENTAGE;
    public static GameRule<Integer> RESPAWN_XP_PERCENTAGE;

    public static GameRule<Integer> FOOD_EAT_TIME_INCREASE;
    public static GameRule<Integer> FOOD_INITIAL_EAT_TIME;


    public static void register() {
        STAMINA_DRAIN_SPEED = registerIntRule("stamina_drain_speed", GameRuleCategory.PLAYER, 160, 1);

        RESPAWN_HEALTH_PERCENTAGE = registerIntRule("respawn_health_percentage", GameRuleCategory.PLAYER, 0, 0, 100);
        RESPAWN_HUNGER_PERCENTAGE = registerIntRule("respawn_hunger_percentage", GameRuleCategory.PLAYER, 0, 0, 100);
        RESPAWN_XP_PERCENTAGE = registerIntRule("respawn_xp_percentage", GameRuleCategory.PLAYER, 0, 0, 100);

        FOOD_INITIAL_EAT_TIME = registerIntRule("initial_eat_time", GameRuleCategory.PLAYER, 10, 1);
        FOOD_EAT_TIME_INCREASE = registerIntRule("eat_time_scaling", GameRuleCategory.PLAYER, 6, 0);
    }

    /*
    private static GameRule<Boolean> registerBooleanRule(String name, GameRuleCategory category, boolean defaultValue) {
        return register(
                name, category, GameRuleType.BOOL, BoolArgumentType.bool(), Codec.BOOL, defaultValue, FeatureFlagSet.of(), GameRuleTypeVisitor::visitBoolean, value -> value ? 1 : 0
        );
    }
    */

    private static GameRule<Integer> registerIntRule(String name, GameRuleCategory category, int defaultValue, int min) {
        return registerIntRule(name, category, defaultValue, min, Integer.MAX_VALUE, FeatureFlagSet.of());
    }

    private static GameRule<Integer> registerIntRule(String name, GameRuleCategory category, int defaultValue, int min, int max) {
        return registerIntRule(name, category, defaultValue, min, max, FeatureFlagSet.of());
    }

    private static GameRule<Integer> registerIntRule(String name, GameRuleCategory category, int defaultValue, int min, int max, FeatureFlagSet requiredFeatures) {
        return register(name, category, GameRuleType.INT, IntegerArgumentType.integer(min, max), Codec.intRange(min, max), defaultValue, requiredFeatures, GameRuleTypeVisitor::visitInteger,/* method_76194 */ value -> value);
    }

    private static GameRule<Integer> registerIntRule(String name, GameRuleCategory category, int defaultValue, int min, int max, GameRules.VisitorCaller<Integer> acceptor) {
        return register(name, category, GameRuleType.INT, IntegerArgumentType.integer(min, max), Codec.intRange(min, max), defaultValue, FeatureFlagSet.of(), acceptor,/* method_76194 */ value -> value);
    }

    private static <T> GameRule<T> register(
            String name,
            GameRuleCategory category,
            GameRuleType type,
            ArgumentType<T> argumentType,
            Codec<T> codec,
            T defaultValue,
            FeatureFlagSet requiredFeatures,
            GameRules.VisitorCaller<T> acceptor,
            ToIntFunction<T> commandResultSupplier
    ) {
        return Registry.register(
                BuiltInRegistries.GAME_RULE, FixedStamina.id(name), new GameRule<>(category, type, argumentType, acceptor, codec, commandResultSupplier, defaultValue, requiredFeatures)
        );
    }

}

package com.fixed.stamina.docs.datagen;

import com.fixed.stamina.FixedStamina;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.util.Util;

import java.util.concurrent.CompletableFuture;

public class EnglishLangProvider extends FabricLanguageProvider {
    protected EnglishLangProvider(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        // Specifying en_us is optional, as it's the default language code
        super(dataOutput, "en_us", registryLookup);
    }

    @Override
    public void generateTranslations(HolderLookup.Provider holderLookup, TranslationBuilder translationBuilder) {
        translationBuilder.add(FixedStamina.id("stamina_drain_speed"), "Stamina Drain Speed");
        translationBuilder.add(
                Util.makeDescriptionId("gamerule", FixedStamina.id("stamina_drain_speed")),
                "How fast stamina drains"
        );

        translationBuilder.add(FixedStamina.id("respawn_health_percentage"), "Respawn Health Percentage");
        translationBuilder.add(
                Util.makeDescriptionId("gamerule", FixedStamina.id("respawn_health_percentage")),
                "Percentage of health players spawn with"
        );

        translationBuilder.add(FixedStamina.id("respawn_hunger_percentage"), "Respawn Hunger Percentage");
        translationBuilder.add(
                Util.makeDescriptionId("gamerule", FixedStamina.id("respawn_hunger_percentage")),
                "Percentage of hunger players spawn with"
        );

        translationBuilder.add(FixedStamina.id("respawn_xp_percentage"), "Respawn Experience Percentage");
        translationBuilder.add(
                Util.makeDescriptionId("gamerule", FixedStamina.id("respawn_xp_percentage")),
                "Percentage of experience players spawn with"
        );

        translationBuilder.add(FixedStamina.id("initial_eat_time"), "Initial Eat Time");
        translationBuilder.add(
                Util.makeDescriptionId("gamerule", FixedStamina.id("initial_eat_time")),
                "Base time for eating food (in ticks)"
        );

        translationBuilder.add(FixedStamina.id("eat_time_scaling"), "Eating Time Scaling");
        translationBuilder.add(
                Util.makeDescriptionId("gamerule", FixedStamina.id("eat_time_scaling")),
                "Additional time to eat food per hunger restored (in ticks)"
        );
    }
}

package com.fixed.stamina.docs.datagen;

import com.fixed.stamina.FixedStamina;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.concurrent.CompletableFuture;

public class ItemTagProvider extends FabricTagProvider.ItemTagProvider {
    public static TagKey<Item> BAD_FOODS = TagKey.create(Registries.ITEM, FixedStamina.id("bad_foods"));
    public static TagKey<Item> RAW_MEAT = TagKey.create(Registries.ITEM, FixedStamina.id("meat/raw"));
    public static TagKey<Item> COOKED_MEAT = TagKey.create(Registries.ITEM, FixedStamina.id("meat/cooked"));

    public ItemTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider wrapperLookup) {
        valueLookupBuilder(BAD_FOODS)
                .add(Items.ROTTEN_FLESH)
                .add(Items.SPIDER_EYE)
                .add(Items.POISONOUS_POTATO)
                .add(Items.PUFFERFISH);

        valueLookupBuilder(RAW_MEAT)
                .add(Items.BEEF)
                .add(Items.CHICKEN)
                .add(Items.COD)
                .add(Items.MUTTON)
                .add(Items.PORKCHOP)
                .add(Items.RABBIT)
                .add(Items.SALMON)
                .add(Items.TROPICAL_FISH);

        valueLookupBuilder(COOKED_MEAT)
                .add(Items.COOKED_BEEF)
                .add(Items.COOKED_CHICKEN)
                .add(Items.COOKED_COD)
                .add(Items.COOKED_MUTTON)
                .add(Items.COOKED_PORKCHOP)
                .add(Items.COOKED_RABBIT)
                .add(Items.COOKED_SALMON);

    }
}

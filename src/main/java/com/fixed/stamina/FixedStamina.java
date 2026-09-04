package com.fixed.stamina;

import com.fixed.stamina.network.SyncHandler;
import com.fixed.stamina.registry.GameruleRegistry;
import com.fixed.stamina.registry.ItemTagRegistry;
import net.fabricmc.api.ModInitializer;

import net.minecraft.resources.Identifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FixedStamina implements ModInitializer {
	public static final String MOD_ID = "fixed-stamina";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
        LOGGER.info("Initializing " + MOD_ID);


        GameruleRegistry.register();
        ItemTagRegistry.register();
        SyncHandler.init();
    }

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}
}

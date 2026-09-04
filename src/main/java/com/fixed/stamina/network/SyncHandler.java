package com.fixed.stamina.network;

import com.fixed.stamina.FixedStamina;
import com.fixed.stamina.registry.GameruleRegistry;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.impl.gamerule.GameRuleEventsImpl;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SyncHandler  {
    public static void init() {
        PayloadTypeRegistry.playS2C().register(SaturationSyncPayload.ID, SaturationSyncPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(EatTimeSyncPayload.ID, EatTimeSyncPayload.CODEC);

        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            ServerLevel level = server.overworld();
            if (level == null) return;

            int initial = level.getGameRules().get(GameruleRegistry.FOOD_INITIAL_EAT_TIME);
            int scaling = level.getGameRules().get(GameruleRegistry.FOOD_EAT_TIME_INCREASE);
            ServerPlayNetworking.send(handler.player, new EatTimeSyncPayload(initial, scaling));
        });

        GameRuleEvents.changeCallback(GameruleRegistry.FOOD_INITIAL_EAT_TIME).register(
                (value, server) -> {
                    broadcastEatTime(server);
                }
        );

        GameRuleEvents.changeCallback(GameruleRegistry.FOOD_EAT_TIME_INCREASE).register(
                (value, server) -> {
                    broadcastEatTime(server);
                }
        );
    }

    private static final Map<UUID, Float> lastSaturationLevels = new HashMap<>();

    public static void onPlayerUpdate(ServerPlayer player)
    {
        Float lastSaturationLevel = lastSaturationLevels.get(player.getUUID());

        float saturation = player.getFoodData().getSaturationLevel();
        if (lastSaturationLevel == null || lastSaturationLevel != saturation)
        {
            ServerPlayNetworking.send(player, new SaturationSyncPayload(saturation));
            lastSaturationLevels.put(player.getUUID(), saturation);
        }
    }
    public static void onPlayerLoggedIn(ServerPlayer player)
    {
        lastSaturationLevels.remove(player.getUUID());
    }

    public static void broadcastEatTime(MinecraftServer server) {
        ServerLevel level = server.overworld();
        int initial = level.getGameRules().get(GameruleRegistry.FOOD_INITIAL_EAT_TIME);
        int scaling = level.getGameRules().get(GameruleRegistry.FOOD_EAT_TIME_INCREASE);
        EatTimeSyncPayload payload = new EatTimeSyncPayload(initial, scaling);

        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            ServerPlayNetworking.send(player, payload);
        }
    }
}

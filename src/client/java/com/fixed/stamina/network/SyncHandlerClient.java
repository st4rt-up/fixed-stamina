package com.fixed.stamina.network;

import com.fixed.stamina.FixedStamina;
import com.fixed.stamina.accessors.EatTimeGameruleAccessor;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.world.entity.player.Player;

public class SyncHandlerClient {
    public static void initClient() {

        // == EAT TIME
        ClientPlayNetworking.registerGlobalReceiver(
                EatTimeSyncPayload.ID,
                (payload, context) -> {
                    Player player = context.client().player;
                    EatTimeGameruleAccessor ga = (EatTimeGameruleAccessor) player;

                    ga.setInitialEatTime_stamina(payload.initial());
                    ga.setEatTimeScaling_stamina(payload.scaling());
                }
        );

        // == SATURATION
        ClientPlayNetworking.registerGlobalReceiver(SaturationSyncPayload.ID, (payload, context) ->
                context.client().execute(() ->
                        context.client().player.getFoodData().setSaturation(payload.getSaturation())));
    }
}

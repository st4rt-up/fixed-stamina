package com.fixed.stamina.mixin;

import com.fixed.stamina.network.SyncHandler;
import com.fixed.stamina.registry.GameruleRegistry;
import net.minecraft.network.Connection;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.players.PlayerList;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Difficulty;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerList.class)
public class PlayerListMixin
{
    @Inject(at = @At("TAIL"), method = "placeNewPlayer")
    private void onPlayerConnect(Connection conn, ServerPlayer player, CommonListenerCookie clientData, CallbackInfo info)
    {
        SyncHandler.onPlayerLoggedIn(player);
    }
    @Redirect(method = "respawn", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/server/level/ServerPlayer;getHealth()F"
    ))
    private float dontSpawnAtMaxHealth(ServerPlayer player){
        ServerLevel server = player.level();

        Difficulty difficulty = server.getDifficulty();
        float defaultDifficultyMultiplier = difficulty == Difficulty.NORMAL ? 0.5f
                : difficulty == Difficulty.HARD ? 0.3f
                : 1f;

        int hungerGamerule = server.getGameRules().get(GameruleRegistry.RESPAWN_HUNGER_PERCENTAGE);
        int healthGamerule = server.getGameRules().get(GameruleRegistry.RESPAWN_HEALTH_PERCENTAGE);
        int xpGamerule = server.getGameRules().get(GameruleRegistry.RESPAWN_XP_PERCENTAGE);

        float foodMultiplier = hungerGamerule != 0 ? ((float) hungerGamerule / 100.0f) : defaultDifficultyMultiplier;
        player.getFoodData().setFoodLevel((int) (20 * foodMultiplier));
        player.getFoodData().setSaturation((int) (20 * foodMultiplier));


        float xpMultiplier = xpGamerule != 0 ? ((float) xpGamerule / 100.0f) : defaultDifficultyMultiplier;
        player.experienceLevel *= xpMultiplier;
        player.experienceProgress *= xpMultiplier;

        float healthMultiplier = healthGamerule != 0 ? (float) healthGamerule / 100.0f : defaultDifficultyMultiplier;
        return player.getHealth() * healthMultiplier;
    }
}

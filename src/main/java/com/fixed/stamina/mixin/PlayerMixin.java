package com.fixed.stamina.mixin;

import com.fixed.stamina.accessors.EatTimeGameruleAccessor;
import com.fixed.stamina.registry.GameruleRegistry;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public class PlayerMixin implements EatTimeGameruleAccessor {

    int initial_eat_time = 10;
    int eat_time_scaling = 6;

    
    public void setInitialEatTime_stamina(int initial) { initial_eat_time =  initial; }
    public int getInitialEatTime_stamina() { return initial_eat_time; }
    public void setEatTimeScaling_stamina(int scaling) { eat_time_scaling =  scaling; }
    public int getEatTimeScaling_stamina() { return eat_time_scaling; }

    @ModifyVariable(method = "causeFoodExhaustion", at = @At(value = "HEAD"), argsOnly = true)
    private float exhaustionGamerule(float value) {
        Player PE = (Player)(Object) this;

        if (!PE.level().isClientSide()) {
            ServerLevel server = (ServerLevel) PE.level();
            return value * server.getGameRules().get(GameruleRegistry.STAMINA_DRAIN_SPEED) / 100f;
        }
        return value;
    }

    @Redirect(method = "causeExtraKnockback", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;setSprinting(Z)V"))
    private void removeServerClientDesync(Player instance, boolean b) {}

    @Inject(method = "canEat", at = @At("HEAD"), cancellable = true)
    private void alwaysEatInPeaceful(boolean ignoreHunger, CallbackInfoReturnable<Boolean> cir) {
        Player PE = (Player)(Object) this;
        boolean isPeacefulMode = PE.level().getDifficulty().getId()==0;
        if (isPeacefulMode) cir.setReturnValue(true);
    }

    /*
    @Redirect(method = "lungeForwardMaybe", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;hasEnoughFoodToDoExhaustiveManoeuvres()Z"))
    private boolean staminaCanLunge(Player PE) {
        if (!(PE.level() instanceof ServerLevel)) return true;

        ItemStack weapon = PE.getWeaponItem();
        if (weapon.isEmpty()) return true;

        int lungeLevel = FixedMinecraftEnchantmentHelper.enchantLevel(weapon, "lunge");
        if (lungeLevel <= 0) return true;

        float stamina = PE.getFoodData().getSaturationLevel();
        return !(stamina < lungeLevel * 2);
    }
    */
}

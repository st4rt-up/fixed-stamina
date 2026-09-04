package com.fixed.stamina.mixin;

import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Consumable.class)
public class ConsumableMixin {

    @Inject(method = "onConsume", at = @At("HEAD"))
    private void eatingPausesStaminaRegen(Level world, LivingEntity user, ItemStack stack, CallbackInfoReturnable<ItemStack> cir) {
        if (user instanceof ServerPlayer serverPlayerEntity) {
            serverPlayerEntity.causeFoodExhaustion(0.01F);
        }
    }

    @Inject(method = "shouldEmitParticlesAndSounds", at = @At("HEAD"), cancellable = true)
    private void playSoundForLongFoods(int remainingUseTicks, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(remainingUseTicks % 4==0);
        cir.cancel();
    }
}

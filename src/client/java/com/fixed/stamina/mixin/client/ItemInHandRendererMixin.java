package com.fixed.stamina.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.core.component.DataComponents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemInHandRenderer.class)
public class ItemInHandRendererMixin {
    @Inject(method = "applyEatTransform", at = @At("HEAD"), cancellable = true)
    private void fixEatingAnimation(
            PoseStack poseStack, float delta, HumanoidArm humanoidArm, ItemStack itemStack, Player player, CallbackInfo ci
    ) {
        if (!itemStack.getComponents().has(DataComponents.FOOD)) return;
        ci.cancel();

        int itemUseTime = itemStack.getUseDuration(player);
        float total = player.getUseItemRemainingTicks() - delta + 1.0F;
        float progress = total / itemUseTime;

        boolean shouldRaiseArm = progress < 0.8F || itemUseTime > 4;
        if (shouldRaiseArm) { // raise arm animation
            float i = Mth.abs(
                    Mth.cos(total / 4.0F * (float) Math.PI) * 0.1F);
            poseStack.translate(0.0F, i, 0.0F);
        }

        // same as vanilla but here so I can understand it
        float i = 1.0F - (float)Math.pow(progress, 27.0);
        int armDirection = humanoidArm == HumanoidArm.RIGHT ? 1 : -1;

        poseStack.translate(i * 0.6F * armDirection, i * -0.5F, i * 0.0F);
        poseStack.mulPose(Axis.YP.rotationDegrees(armDirection * i * 90.0F));
        poseStack.mulPose(Axis.XP.rotationDegrees(i * 10.0F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(armDirection * i * 30.0F));

    }
}

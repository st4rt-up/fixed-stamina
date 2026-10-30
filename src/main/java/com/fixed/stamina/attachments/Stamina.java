package com.fixed.stamina.attachments;

import com.fixed.stamina.FixedStamina;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentTarget;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;

public class Stamina {
    public static AttachmentType<Integer> STAMINA = AttachmentRegistry.create(FixedStamina.id("stamina_attachment"),
            builder -> builder
                    .initializer(() -> 0)
                    .syncWith(
                            ByteBufCodecs.INT,
                            AttachmentSyncPredicate.all()
                    )
    );

    public static AttachmentType<Float> LAST_EXHAUSTION = AttachmentRegistry.create(FixedStamina.id("last_exhaustion_attachment"),
            builder -> builder
                    .initializer(() -> 0.0f)
                    .syncWith(
                            ByteBufCodecs.FLOAT,
                            AttachmentSyncPredicate.all()
                    )
    );

    public static AttachmentType<Integer> TICKS_SINCE_LAST_EXHAUSTION = AttachmentRegistry.create(FixedStamina.id("ticks_since_last_exhaustion_attachment"),
            builder -> builder
                    .initializer(() -> 0)
                    .syncWith(
                            ByteBufCodecs.INT,
                            AttachmentSyncPredicate.all()
                    )
    );

    public static void applyNoStaminaEffects(Player player) {
        player.addEffect(new MobEffectInstance(MobEffects.MINING_FATIGUE, 10, 99, true, false, true));
        player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 10, 99, true, false, true));
        if (player.isBlocking()) player.releaseUsingItem();
        player.getCooldowns().addCooldown(Items.SHIELD.getDefaultInstance(), 30);
    }

    public static StaminaData get(AttachmentTarget target) {
        return new StaminaData(target);
    }

    public record StaminaData(AttachmentTarget target) {

        public int getTicksSinceLastExhaustion() { return target.getAttachedOrElse(TICKS_SINCE_LAST_EXHAUSTION,0); }
        public void setTicksSinceLastExhaustion(int ticks) {
            target.setAttached(TICKS_SINCE_LAST_EXHAUSTION, ticks);
        }


    }


}

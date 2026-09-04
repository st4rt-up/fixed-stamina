package com.fixed.stamina.mixin;

import com.fixed.stamina.FixedStamina;
import com.fixed.stamina.accessors.EatTimeGameruleAccessor;
import com.fixed.stamina.registry.GameruleRegistry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.tags.ItemTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public class ItemMixin {

    @Inject(method = "getUseDuration", at = @At("HEAD"), cancellable = true)
    private void modifyFoodEatTimes (ItemStack stack, LivingEntity user, CallbackInfoReturnable<Integer> cir) {
        if (!stack.getItem().components().has(DataComponents.FOOD)) return;
        FoodProperties foodComponent = stack.getItem().components().get(DataComponents.FOOD);

        // fix later
        if (stack.is(ItemTags.PIGLIN_LOVED)) {
            cir.setReturnValue(60);
            return;
        }

        int initialEatTime = 10;
        int eatTimeScaling = 6;

        if (user.level() instanceof ServerLevel level) {
            initialEatTime = level.getGameRules().get(GameruleRegistry.FOOD_INITIAL_EAT_TIME);
            eatTimeScaling = level.getGameRules().get(GameruleRegistry.FOOD_EAT_TIME_INCREASE);
        } else {
            EatTimeGameruleAccessor player = (EatTimeGameruleAccessor) (Player) user;

            initialEatTime = player.getInitialEatTime_stamina();
            eatTimeScaling = player.getEatTimeScaling_stamina();
        }

        int totalEatTime = initialEatTime + eatTimeScaling * foodComponent.nutrition();
        cir.setReturnValue(totalEatTime);


    }
}

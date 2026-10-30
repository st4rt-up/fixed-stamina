package com.fixed.stamina;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.world.scores.criteria.ObjectiveCriteria;
import net.minecraft.network.chat.Component;

public class CustomData {
    public static void setData(LivingEntity entity, String name, int data) {
        Scoreboard scoreboard = entity.level().getScoreboard();
        if (scoreboard.getObjective(name) == null) {
            scoreboard.addObjective(
                    name,
                    ObjectiveCriteria.DUMMY,
                    Component.nullToEmpty(name),
                    ObjectiveCriteria.DUMMY.getDefaultRenderType(),
                    false,
                    null
            );
        }
        /*
        if(name.contains("airTime")) {
            if (scoreboard
                    .getDisplayObjective(DisplaySlot.TEAM_AQUA) == null) {
                scoreboard
                        .setDisplayObjective(DisplaySlot.TEAM_AQUA, entity.level().getScoreboard().getObjective(name));
            } else if (!scoreboard
                    .getDisplayObjective(DisplaySlot.TEAM_AQUA)
                    .getDisplayName()
                    .tryCollapseToString().contains(name)) {

                entity.level()
                        .getScoreboard()
                        .setDisplayObjective(DisplaySlot.TEAM_AQUA, entity.level().getScoreboard().getObjective(name));
            }
        }
        */

        scoreboard.getOrCreatePlayerScore(entity, scoreboard.getObjective(name)).set(data);
    }

    public static int getData(LivingEntity entity, String name)  {
        Scoreboard scoreboard = entity.level().getScoreboard();
        if (scoreboard.getObjective(name) == null) return 0;

        return scoreboard.getOrCreatePlayerScore(entity, scoreboard.getObjective(name)).get();
    }
}

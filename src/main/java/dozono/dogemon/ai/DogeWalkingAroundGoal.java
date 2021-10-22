package dozono.dogemon.ai;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;

public class DogeWalkingAroundGoal extends Goal {
    private WolfEntity entity;

    public DogeWalkingAroundGoal(WolfEntity entity) {
        this.entity = entity;
    }

    @Override
    public boolean canUse() {
        LivingEntity owner = this.entity.getOwner();

        return false;
    }

}

package dozono.dogemon.ai;

import java.util.EnumSet;
import java.util.List;

import net.minecraft.entity.Pose;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.item.Item;
import net.minecraft.util.SoundEvents;

public class DogSeekingFoodGoal extends Goal {
    private WolfEntity entity;

    private List<Item> allowedItems;

    private boolean isReachedTarget = false;
    private ItemEntity target = null;

    public DogSeekingFoodGoal(WolfEntity wolf, List<Item> allowedItems) {
        this.entity = wolf;
        this.allowedItems = allowedItems;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (entity.getHealth() >= 20) {
            return false;
        }
        if (entity.getTarget() != null && entity.getLastHurtByMob() != null) {
            return false;
        }
        List<ItemEntity> list = entity.level.getEntitiesOfClass(ItemEntity.class,
                entity.getBoundingBox().inflate(8.0D, 8.0D, 8.0D),
                (e) -> !e.hasPickUpDelay() && e.isAlive() && allowedItems.contains(e.getItem().getItem()));

        if (!list.isEmpty()) {
            this.target = list.get(0);
        }

        return true;
    }

    @Override
    public boolean canContinueToUse() {
        if (entity.getTarget() != null && entity.getLastHurtByMob() != null) {
            return false;
        }
        if (this.target == null) {
            this.isReachedTarget = false;
            return false;
        }
        if (!this.target.isAlive()) {
            this.isReachedTarget = false;
            this.target = null;
            return false;
        }
        return true;
    }

    @Override
    public void tick() {
        if (this.target == null) {
            this.isReachedTarget = false;
            return;
        }
        if (!this.target.isAlive()) {
            this.isReachedTarget = false;
            this.target = null;
            return;
        }
        if (this.isReachedTarget) {
            this.target.remove();
            this.target = null;
            this.isReachedTarget = false;
            // TODO: add item stack to inventory
            // ItemStack stack = this.target.getItem();e
            entity.heal(2);
            entity.playSound(SoundEvents.LLAMA_EAT, 1.0F, 1.0F);
            return;
        }
        this.entity.getLookControl().setLookAt(this.target.getX(), this.target.getEyeY(), this.target.getZ());
        entity.getNavigation().moveTo(this.target, (double) 1.2F);
        if (this.target.distanceTo(entity) < 2D) {
            this.isReachedTarget = true;
        } else {
            this.isReachedTarget = false;
        }
    }
}

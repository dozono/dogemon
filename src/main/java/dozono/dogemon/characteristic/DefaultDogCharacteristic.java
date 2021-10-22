package dozono.dogemon.characteristic;

import com.google.common.collect.ImmutableList;
import dozono.dogemon.ai.DogSeekingFoodGoal;
import dozono.dogemon.ai.DogeWalkingAroundGoal;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.item.Items;

public enum DefaultDogCharacteristic implements IDogCharacteristic {
    Wrath("wrath") {
        @Override
        public void onApply(WolfEntity entity) {
            entity.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(6);
            entity.getAttribute(Attributes.MAX_HEALTH).setBaseValue(35);
        }
    },
    Envy("envy") {
        @Override
        public void onApply(WolfEntity entity) {
            // TODO Auto-generated method stub
        }
    },
    Lazy("lazy") {
        @Override
        public void onApply(WolfEntity entity) {
            // TODO Auto-generated method stub
        }
    },
    Curious("curious") {
        @Override
        public void onApply(WolfEntity entity) {
            entity.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(5);
            entity.goalSelector.addGoal(3,new DogeWalkingAroundGoal(entity));
        }
    },
    Gluttony("gluttony") {
        @Override
        public void onApply(WolfEntity entity) {
            entity.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(10);
            entity.goalSelector.addGoal(3, new DogSeekingFoodGoal(entity, ImmutableList.of(Items.ROTTEN_FLESH, Items.BONE)));
        }
    },
    Tough("tough") {
        @Override
        public void onApply(WolfEntity entity) {
            // TODO Auto-generated method stub
        }
    };

    private String name;

    DefaultDogCharacteristic(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}

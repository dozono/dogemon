package dozono.dogemon.characteristic;

import net.minecraft.entity.passive.WolfEntity;

public interface IDogCharacteristic {
    String getName();

    void onApply(WolfEntity entity);
}

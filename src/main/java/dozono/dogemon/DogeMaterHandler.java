package dozono.dogemon;

import com.google.common.collect.ImmutableList;
import dozono.dogemon.DogeCapability;
import dozono.dogemon.characteristic.DefaultDogCharacteristic;
import dozono.dogemon.characteristic.IDogCharacteristic;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.item.Items;

public class DogeMaterHandler {
    public static void onDogMatted(WolfEntity wolfEntity, IDogCharacteristic ic) {
        wolfEntity.getCapability(DogeCapability.DOG_CAPABILITY)
                .ifPresent((c) -> {
                    c.dogCharacteristic.add(ic);
                });
    }

    public static void onAttachWolfCapability() {

    }
}

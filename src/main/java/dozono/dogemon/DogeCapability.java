package dozono.dogemon;

import dozono.dogemon.characteristic.DefaultDogCharacteristic;
import dozono.dogemon.characteristic.IDogCharacteristic;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DogeCapability {
    @CapabilityInject(DogeCapability.class)
    public static Capability<DogeCapability> DOG_CAPABILITY = null;

    public List<IDogCharacteristic> dogCharacteristic = new ArrayList<>();

    public static void register() {
        CapabilityManager.INSTANCE.register(DogeCapability.class, new Capability.IStorage<DogeCapability>() {
            @Nullable
            @Override
            public INBT writeNBT(Capability<DogeCapability> capability, DogeCapability instance, Direction side) {
                CompoundNBT nbt = new CompoundNBT();
                nbt.putIntArray("characteristic", instance.dogCharacteristic.stream()
                        .mapToInt(v -> ((DefaultDogCharacteristic) v).ordinal())
                        .toArray());
                return nbt;
            }

            @Override
            public void readNBT(Capability<DogeCapability> capability, DogeCapability instance, Direction side, INBT nbt) {
                if (nbt.getType() == CompoundNBT.TYPE) {
                    CompoundNBT compoundNBT = (CompoundNBT) nbt;
                    instance.dogCharacteristic = Arrays.stream(compoundNBT.getIntArray("characteristic"))
                            .mapToObj(v -> DefaultDogCharacteristic.values()[v])
                            .collect(Collectors.toList());
                }
            }
        }, DogeCapability::new);
    }
}
 

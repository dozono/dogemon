package dozono.dogemon;

import dozono.dogemon.characteristic.DefaultDogCharacteristic;
import dozono.dogemon.characteristic.IDogCharacteristic;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(DogemonMod.MODID)
public class DogemonMod {
    public static final String MODID = "dogemon";
    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();

    private DeferredRegister<Item> items = DeferredRegister.create(Item.class, MODID);


    private RegistryObject<Item> itemWrathBone = items.register("wrath-bone",
            () -> new Item(new Item.Properties().tab(ItemGroup.TAB_FOOD)) {
                public ActionResultType interactLivingEntity(ItemStack stack, PlayerEntity player, LivingEntity entity,
                                                             Hand hand) {
                    if (entity.level.isClientSide) {
                        return net.minecraft.util.ActionResultType.PASS;
                    }

                    if (entity instanceof WolfEntity && ((WolfEntity) entity).isTame()) {
                        DogeMaterHandler.onDogMatted((WolfEntity) entity,DefaultDogCharacteristic.Wrath);
                    }

                    return ActionResultType.PASS;
                }

                ;
            });


    public DogemonMod() {
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        // Register the doClientStuff method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        items.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    @SubscribeEvent
    public void onPlayerInteractWolf(PlayerInteractEvent.EntityInteractSpecific event) {
        if (event.getSide().isClient()) {
            return;
        }
        Item item = event.getItemStack().getItem();
        if (item == itemWrathBone.get() && event.getTarget() instanceof WolfEntity) {
            WolfEntity wolfEntity = (WolfEntity) event.getTarget();

            // TODO: filter master
            if (wolfEntity.isTame()) {
                DogeMaterHandler.onDogMatted(wolfEntity, DefaultDogCharacteristic.Wrath);
            }
        }
    }

    @SubscribeEvent
    public void onAttachDogCapability(AttachCapabilitiesEvent<Entity> event) {
        Entity e = event.getObject();
        if (e instanceof WolfEntity) {
            event.addCapability(new ResourceLocation("doge_capability", MODID), new ICapabilityProvider() {
                private final DogeCapability capability = new DogeCapability();

                @Nonnull
                @Override
                public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
                    if (cap == DogeCapability.DOG_CAPABILITY) {
                        return (LazyOptional<T>) LazyOptional.of(() -> capability);
                    }
                    return LazyOptional.empty();
                }
            });
        }
    }

    private void setup(final FMLCommonSetupEvent event) {
        // some preinit code
        LOGGER.info("Register dog capability");
        DogeCapability.register();
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        // do something that can only be done on the client
        LOGGER.info("Got game settings {}", event.getMinecraftSupplier().get().options);
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        // do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    // You can use EventBusSubscriber to automatically subscribe events on the
    // contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
            // register a new block here
            LOGGER.info("HELLO from Register Block");
        }
    }
}
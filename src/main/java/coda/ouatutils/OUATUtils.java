package coda.ouatutils;

import coda.ouatutils.terrablender.OUATBiomes;
import coda.ouatutils.terrablender.OUATOverworldBiomes;
import coda.ouatutils.terrablender.TestRegion;
import coda.ouatutils.terrablender.TestSurfaceRuleData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.IForgeRegistry;
import terrablender.api.Regions;
import terrablender.api.SurfaceRuleManager;

@Mod(OUATUtils.MOD_ID)
public class OUATUtils {
    public static final String MOD_ID = "ouatutils";

    public OUATUtils() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        IEventBus forgeBus = MinecraftForge.EVENT_BUS;

        bus.addListener(this::commonSetup);
        bus.addGenericListener(Biome.class, this::registerBiomes);
    }

    private void registerBiomes(RegistryEvent.Register<Biome> event) {
        IForgeRegistry<Biome> registry = event.getRegistry();
        registry.register(OUATOverworldBiomes.regalMeadow().setRegistryName(OUATBiomes.REGAL_MEADOW.location()));
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            // Given we only add two biomes, we should keep our weight relatively low.
            Regions.register(new TestRegion(new ResourceLocation(MOD_ID, "overworld"), 2));

            // Register our surface rules
            SurfaceRuleManager.addSurfaceRules(SurfaceRuleManager.RuleCategory.OVERWORLD, MOD_ID, TestSurfaceRuleData.makeRules());
        });
    }
}
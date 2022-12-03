package coda.ouatutils;

import coda.ouatutils.terrablender.OUATBiomes;
import coda.ouatutils.terrablender.OUATOverworldBiomes;
import coda.ouatutils.terrablender.OUATRegion;
import coda.ouatutils.terrablender.OUATSurfaceRuleData;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.TickEvent;
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

        forgeBus.addListener(this::addWeather);

        bus.addListener(this::commonSetup);
        bus.addGenericListener(Biome.class, this::registerBiomes);
    }

    private void registerBiomes(RegistryEvent.Register<Biome> event) {
        IForgeRegistry<Biome> registry = event.getRegistry();
        registry.register(OUATOverworldBiomes.regalMeadow().setRegistryName(OUATBiomes.REGAL_MEADOW.location()));
        registry.register(OUATOverworldBiomes.stormySea().setRegistryName(OUATBiomes.STORMY_SEA.location()));
    }

    // todo - region-specific rain for this biome?
    private void addWeather(TickEvent.PlayerTickEvent e) {
        Player player = e.player;
        BlockPos pos = player.blockPosition();
        Level level = player.level;
        Minecraft mc = Minecraft.getInstance();

        if (level instanceof ServerLevel serverLevel && serverLevel.getBiome(pos).is(OUATBiomes.STORMY_SEA) && !serverLevel.serverLevelData.isThundering()) {
            level.rainLevel = 1.0F;
            level.oRainLevel = 1.0F;
            mc.level.rainLevel = 1.0F;
            mc.level.oRainLevel = 1.0F;
            mc.level.thunderLevel = 1.0F;
            mc.level.oThunderLevel = 1.0F;
            serverLevel.serverLevelData.setThundering(true);
            serverLevel.serverLevelData.setRaining(true);
       }
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            // Given we only add two biomes, we should keep our weight relatively low.
            Regions.register(new OUATRegion(new ResourceLocation(MOD_ID, "overworld"), 2));

            // Register our surface rules
            SurfaceRuleManager.addSurfaceRules(SurfaceRuleManager.RuleCategory.OVERWORLD, MOD_ID, OUATSurfaceRuleData.makeRules());
        });
    }
}
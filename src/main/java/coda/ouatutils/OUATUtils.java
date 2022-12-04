package coda.ouatutils;

import coda.ouatutils.terrablender.OUATBiomes;
import coda.ouatutils.terrablender.OUATOverworldBiomes;
import coda.ouatutils.terrablender.OUATRegion;
import coda.ouatutils.terrablender.OUATSurfaceRuleData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.phys.Vec3;
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
        Minecraft minecraft = Minecraft.getInstance();
        Player player = e.player;
        BlockPos pos = player.blockPosition();
        ClientLevel cLevel = minecraft.level;

        if (cLevel != null && cLevel.getBiome(pos).is(OUATBiomes.STORMY_SEA)) {
            Vec3 camPos = minecraft.cameraEntity.position();
            //minecraft.levelRenderer.renderSnowAndRain(minecraft.gameRenderer.lightTexture(), 1.0F, camPos.x, camPos.y, camPos.z);
        }
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            Regions.register(new OUATRegion(new ResourceLocation(MOD_ID, "overworld"), 2));

            SurfaceRuleManager.addSurfaceRules(SurfaceRuleManager.RuleCategory.OVERWORLD, MOD_ID, OUATSurfaceRuleData.makeRules());
        });
    }
}
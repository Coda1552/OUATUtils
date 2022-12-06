package coda.ouatutils;

import coda.ouatutils.terrablender.OUATBiomes;
import coda.ouatutils.terrablender.OUATOverworldBiomes;
import coda.ouatutils.terrablender.OUATRegion;
import coda.ouatutils.terrablender.OUATSurfaceRuleData;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
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
        forgeBus.addListener(this::checkSpawns);

        bus.addListener(this::commonSetup);
        bus.addGenericListener(Biome.class, this::registerBiomes);
    }

    private void registerBiomes(RegistryEvent.Register<Biome> event) {
        IForgeRegistry<Biome> registry = event.getRegistry();
        registry.register(OUATOverworldBiomes.regalMeadow().setRegistryName(OUATBiomes.REGAL_MEADOW.location()));
        registry.register(OUATOverworldBiomes.stormySea().setRegistryName(OUATBiomes.STORMY_SEA.location()));
        registry.register(OUATOverworldBiomes.rollingHills().setRegistryName(OUATBiomes.ROLLING_HILLS.location()));
    }

    private void checkSpawns(LivingSpawnEvent.CheckSpawn e) {
        Entity entity = e.getEntity();
        LevelAccessor level = e.getWorld();

        if (e.getSpawnReason() == MobSpawnType.NATURAL && entity.getType().is(OUATTags.FARM_ANIMALS) && (!level.getBiome(entity.blockPosition()).is(OUATBiomes.ROLLING_HILLS) || !level.getBiome(entity.blockPosition()).is(Biomes.PLAINS))) {
            e.setCanceled(true);
        }
    }

    private void addWeather(TickEvent.PlayerTickEvent e) {
        Player player = e.player;
        BlockPos pos = player.blockPosition();
        Level level = player.level;

        if (level.getBiome(pos).is(OUATBiomes.STORMY_SEA) && level.isClientSide && !((ClientLevelExtension)level).isDark()) {
            ((ClientLevelExtension)level).setDark(true);
        } else if (!level.getBiome(pos).is(OUATBiomes.STORMY_SEA) && level.isClientSide && ((ClientLevelExtension)level).isDark()){
            ((ClientLevelExtension)level).setDark(false);
        }

        if (level.random.nextInt(1000) == 0 && level.isClientSide && level.getBiome(pos).is(OUATBiomes.STORMY_SEA)) {
            level.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.LIGHTNING_BOLT_THUNDER, SoundSource.WEATHER, 10000.0F, 0.8F + level.random.nextFloat() * 0.2F, false);
            level.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.LIGHTNING_BOLT_IMPACT, SoundSource.WEATHER, 2.0F, 0.5F + level.random.nextFloat() * 0.2F, false);
        }

        if (level.getBiome(pos).is(OUATBiomes.STORMY_SEA) && e.player.isPassenger() && e.player.getVehicle() instanceof Boat boat && player.getRandom().nextFloat() > 0.65F) {
            boat.onAboveBubbleCol(true);
        }
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            Regions.register(new OUATRegion(new ResourceLocation(MOD_ID, "overworld"), 2));

            SurfaceRuleManager.addSurfaceRules(SurfaceRuleManager.RuleCategory.OVERWORLD, MOD_ID, OUATSurfaceRuleData.makeRules());
        });
    }
}
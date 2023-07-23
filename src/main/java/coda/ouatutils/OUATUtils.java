package coda.ouatutils;

import coda.ouatutils.registry.ModBiomes;
import coda.ouatutils.terrablender.OUATBiomes;
import coda.ouatutils.terrablender.OUATRegion;
import coda.ouatutils.terrablender.OUATSurfaceRuleData;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import terrablender.api.Regions;
import terrablender.api.SurfaceRuleManager;

@Mod(OUATUtils.MOD_ID)
public class OUATUtils {
    public static final String MOD_ID = "ouatutils";

    public OUATUtils() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        IEventBus forgeBus = MinecraftForge.EVENT_BUS;

        forgeBus.addListener(this::addWeather);
        forgeBus.addListener(this::removeSpawns);

        bus.addListener(this::commonSetup);

        ModBiomes.BIOMES.register(bus);
    }

    private void removeSpawns(LivingSpawnEvent.CheckSpawn e) {
        if (e.getEntity().getType().is(OUATTags.FARM_ANIMALS) && (e.getSpawnReason().equals(MobSpawnType.CHUNK_GENERATION) || e.getSpawnReason().equals(MobSpawnType.NATURAL))) {
            e.setResult(Event.Result.DENY);
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
package coda.ouatutils.registry;

import coda.ouatutils.OUATUtils;
import coda.ouatutils.terrablender.OUATBiomes;
import coda.ouatutils.terrablender.OUATOverworldBiomes;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModBiomes {
    public static final DeferredRegister<Biome> BIOMES = DeferredRegister.create(Registry.BIOME_REGISTRY, OUATUtils.MOD_ID);

    public static final RegistryObject<Biome> REGAL_MEADOW = register(OUATBiomes.REGAL_MEADOW, OUATOverworldBiomes::regalMeadow);
    public static final RegistryObject<Biome> STORMY_SEA = register(OUATBiomes.STORMY_SEA, OUATOverworldBiomes::regalMeadow);
    public static final RegistryObject<Biome> ROLLING_HILLS = register(OUATBiomes.ROLLING_HILLS, OUATOverworldBiomes::regalMeadow);

    public static RegistryObject<Biome> register(ResourceKey<Biome> key, Supplier<Biome> biomeSupplier) {
        return BIOMES.register(key.location().getPath(), biomeSupplier);
    }
}

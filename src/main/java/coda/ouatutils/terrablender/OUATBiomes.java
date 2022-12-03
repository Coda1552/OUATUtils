package coda.ouatutils.terrablender;

import coda.ouatutils.OUATUtils;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;

public class OUATBiomes {
    public static final ResourceKey<Biome> REGAL_MEADOW = register("regal_meadow");

    private static ResourceKey<Biome> register(String name) {
        return ResourceKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(OUATUtils.MOD_ID, name));
    }
}
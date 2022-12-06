package coda.ouatutils.terrablender;

import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.data.worldgen.placement.AquaticPlacements;
import net.minecraft.sounds.Music;
import net.minecraft.sounds.Musics;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraftforge.fml.ModList;

import javax.annotation.Nullable;

public class OUATOverworldBiomes {
    @Nullable
    private static final Music NORMAL_MUSIC = null;

    protected static int calculateSkyColor(float color) {
        float $$1 = color / 3.0F;
        $$1 = Mth.clamp($$1, -1.0F, 1.0F);
        return Mth.hsvToRgb(0.62222224F - $$1 * 0.05F, 0.5F + $$1 * 0.1F, 1.0F);
    }

    private static Biome biome(Biome.Precipitation precipitation, Biome.BiomeCategory category, float temperature, float downfall, MobSpawnSettings.Builder spawnBuilder, BiomeGenerationSettings.Builder biomeBuilder, @Nullable Music music) {
        return biome(precipitation, category, temperature, downfall, 4159204, 329011, spawnBuilder, biomeBuilder, music);
    }

    private static Biome biome(Biome.Precipitation precipitation, Biome.BiomeCategory category, float temperature, float downfall, int waterColor, int waterFogColor, MobSpawnSettings.Builder spawnBuilder, BiomeGenerationSettings.Builder biomeBuilder, @Nullable Music music) {
        return (new Biome.BiomeBuilder()).precipitation(precipitation).biomeCategory(category).temperature(temperature).downfall(downfall).specialEffects((new BiomeSpecialEffects.Builder()).waterColor(waterColor).waterFogColor(waterFogColor).fogColor(12638463).skyColor(calculateSkyColor(temperature)).ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS).backgroundMusic(music).build()).mobSpawnSettings(spawnBuilder.build()).generationSettings(biomeBuilder.build()).build();
    }

    private static void globalOverworldGeneration(BiomeGenerationSettings.Builder builder) {
        BiomeDefaultFeatures.addDefaultCarversAndLakes(builder);
        BiomeDefaultFeatures.addDefaultCrystalFormations(builder);
        BiomeDefaultFeatures.addDefaultMonsterRoom(builder);
        BiomeDefaultFeatures.addDefaultUndergroundVariety(builder);
        BiomeDefaultFeatures.addDefaultSprings(builder);
        BiomeDefaultFeatures.addSurfaceFreezing(builder);
    }

    public static void regalPlainsSpawns(MobSpawnSettings.Builder builder) {
        boolean naturalist = ModList.get().isLoaded("naturalist");

        if (naturalist) {
            builder.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.byString("naturalist:bluejay").get(), 10, 1, 4));
            builder.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.byString("naturalist:canary").get(), 10, 1, 4));
            builder.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.byString("naturalist:cardinal").get(), 10, 1, 4));
            builder.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.byString("naturalist:robin").get(), 10, 1, 4));
            builder.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.byString("naturalist:butterfly").get(), 6, 1, 3));
            builder.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.byString("naturalist:boar").get(), 15, 4, 4));
        }
    }

    public static Biome regalMeadow() {
        MobSpawnSettings.Builder spawnBuilder = new MobSpawnSettings.Builder();
        regalPlainsSpawns(spawnBuilder);
        BiomeDefaultFeatures.commonSpawns(spawnBuilder);

        BiomeGenerationSettings.Builder biomeBuilder = new BiomeGenerationSettings.Builder();
        globalOverworldGeneration(biomeBuilder);
        BiomeDefaultFeatures.addDefaultOres(biomeBuilder);
        BiomeDefaultFeatures.addDefaultSoftDisks(biomeBuilder);
        BiomeDefaultFeatures.addExtraEmeralds(biomeBuilder);
        BiomeDefaultFeatures.addPlainGrass(biomeBuilder);
        BiomeDefaultFeatures.addMeadowVegetation(biomeBuilder);
        BiomeDefaultFeatures.addMountainTrees(biomeBuilder);
        BiomeDefaultFeatures.addDefaultGrass(biomeBuilder);
        BiomeDefaultFeatures.addDefaultMushrooms(biomeBuilder);
        BiomeDefaultFeatures.addMossyStoneBlock(biomeBuilder);

        Music music = Musics.createGameMusic(SoundEvents.MUSIC_BIOME_MEADOW);
        
        return biome(Biome.Precipitation.RAIN, Biome.BiomeCategory.PLAINS, 0.5F, 0.5F, spawnBuilder, biomeBuilder, music);
    }

    public static Biome stormySea() {
        MobSpawnSettings.Builder spawnBuilder = new MobSpawnSettings.Builder();
        BiomeDefaultFeatures.oceanSpawns(spawnBuilder, 3, 4, 15);
        spawnBuilder.addSpawn(MobCategory.WATER_AMBIENT, new MobSpawnSettings.SpawnerData(EntityType.SALMON, 15, 1, 5));

        BiomeGenerationSettings.Builder biomeBuilder = new BiomeGenerationSettings.Builder();
        globalOverworldGeneration(biomeBuilder);
        BiomeDefaultFeatures.addDefaultOres(biomeBuilder);
        BiomeDefaultFeatures.addDefaultSoftDisks(biomeBuilder);
        BiomeDefaultFeatures.addWaterTrees(biomeBuilder);
        BiomeDefaultFeatures.addDefaultFlowers(biomeBuilder);
        BiomeDefaultFeatures.addDefaultGrass(biomeBuilder);
        BiomeDefaultFeatures.addDefaultMushrooms(biomeBuilder);
        BiomeDefaultFeatures.addDefaultExtraVegetation(biomeBuilder);
        biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, AquaticPlacements.SEAGRASS_DEEP_COLD);
        BiomeDefaultFeatures.addDefaultSeagrass(biomeBuilder);
        BiomeDefaultFeatures.addColdOceanExtraVegetation(biomeBuilder);

        return biome(Biome.Precipitation.RAIN, Biome.BiomeCategory.OCEAN, 0.25F, 1.0F, 0x4452b3, 0x100e1d, spawnBuilder, biomeBuilder, NORMAL_MUSIC);
    }

    public static void rollingHillsSpawns(MobSpawnSettings.Builder builder) {
        boolean naturalist = ModList.get().isLoaded("naturalist");

        if (naturalist) {
            builder.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.byString("naturalist:deer").get(), 6, 1, 3));
            builder.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.byString("naturalist:boar").get(), 15, 4, 4));
            builder.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.byString("naturalist:butterfly").get(), 6, 1, 3));
        }
    }

    public static Biome rollingHills() {
        MobSpawnSettings.Builder spawnBuilder = new MobSpawnSettings.Builder();
        BiomeDefaultFeatures.plainsSpawns(spawnBuilder);
        rollingHillsSpawns(spawnBuilder);

        BiomeGenerationSettings.Builder biomeBuilder = new BiomeGenerationSettings.Builder();
        globalOverworldGeneration(biomeBuilder);
        BiomeDefaultFeatures.addDefaultOres(biomeBuilder);
        BiomeDefaultFeatures.addDefaultSoftDisks(biomeBuilder);
        BiomeDefaultFeatures.addPlainGrass(biomeBuilder);
        BiomeDefaultFeatures.addMeadowVegetation(biomeBuilder);
        BiomeDefaultFeatures.addDefaultGrass(biomeBuilder);
        BiomeDefaultFeatures.addDefaultMushrooms(biomeBuilder);

        return biome(Biome.Precipitation.RAIN, Biome.BiomeCategory.PLAINS, 0.6F, 0.4F, spawnBuilder, biomeBuilder, NORMAL_MUSIC);
    }
}
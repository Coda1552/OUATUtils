package coda.ouatutils;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

public class OUATTags {

    public static final TagKey<EntityType<?>> FARM_ANIMALS = create("farm_animals");

    private static TagKey<EntityType<?>> create(String pName) {
        return TagKey.create(Registry.ENTITY_TYPE_REGISTRY, new ResourceLocation(OUATUtils.MOD_ID, pName));
    }
}

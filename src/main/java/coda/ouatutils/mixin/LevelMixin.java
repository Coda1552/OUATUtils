package coda.ouatutils.mixin;

import coda.ouatutils.terrablender.OUATBiomes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.biome.BiomeManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Level.class)
public abstract class LevelMixin extends net.minecraftforge.common.capabilities.CapabilityProvider<Level> implements LevelAccessor, AutoCloseable, net.minecraftforge.common.extensions.IForgeLevel {

    protected LevelMixin(Class<Level> baseClass) {
        super(baseClass);
    }

    @Shadow
    public abstract BiomeManager getBiomeManager();

    @Inject(at = @At("HEAD"), method = "isRainingAt", cancellable = true)
    public void moreRain(BlockPos pPosition, CallbackInfoReturnable<Boolean> cir) {
        if (getBiomeManager().getBiome(pPosition).is(OUATBiomes.STORMY_SEA) && canSeeSky(pPosition)) {
            cir.setReturnValue(true);
            cir.cancel();
        }
    }
}

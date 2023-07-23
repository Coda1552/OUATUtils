package coda.ouatutils.mixin;

import coda.ouatutils.ClientLevelExtension;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.storage.WritableLevelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.function.Supplier;

@Mixin(ClientLevel.class)
public abstract class ClientLevelMixin extends Level implements ClientLevelExtension {

    @Unique
    private boolean dark;

    protected ClientLevelMixin(WritableLevelData pLevelData, ResourceKey<Level> pDimension, Holder<DimensionType> pDimensionTypeRegistration, Supplier<ProfilerFiller> pProfiler, boolean pIsClientSide, boolean pIsDebug, long pBiomeZoomSeed, int pMaxChainedNeighborUpdates) {
        super(pLevelData, pDimension, pDimensionTypeRegistration, pProfiler, pIsClientSide, pIsDebug, pBiomeZoomSeed, pMaxChainedNeighborUpdates);
    }

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientLevel;getRainLevel(F)F"), method = "getSkyDarken")
    public float OUA$darkSkyRain(ClientLevel instance, float v) {
        return dark? 1 : instance.getRainLevel(v);
    }

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientLevel;getThunderLevel(F)F"), method = "getSkyDarken")
    public float OUA$darkSkyThunder(ClientLevel instance, float v) {
        return dark? 1 : instance.getThunderLevel(v);
    }

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientLevel;getRainLevel(F)F"), method = "getSkyColor")
    public float OUA$colorSkyRain(ClientLevel instance, float v) {
        return dark? 1 : instance.getRainLevel(v);
    }

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientLevel;getThunderLevel(F)F"), method = "getSkyColor")
    public float OUA$colorSkyThunder(ClientLevel instance, float v) {
        return dark? 1 : instance.getThunderLevel(v);
    }

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientLevel;getRainLevel(F)F"), method = "getCloudColor")
    public float OUA$colorCloudRain(ClientLevel instance, float v) {
        return dark? 1 : instance.getRainLevel(v);
    }

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientLevel;getThunderLevel(F)F"), method = "getCloudColor")
    public float OUA$colorCloudThunder(ClientLevel instance, float v) {
        return dark? 1 : instance.getThunderLevel(v);
    }

    @Override
    public boolean isDark() {
        return dark;
    }

    @Override
    public void setDark(boolean dark) {
        this.dark = dark;
    }
}

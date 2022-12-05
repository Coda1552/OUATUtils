package coda.ouatutils.mixin;

import coda.ouatutils.terrablender.OUATBiomes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(FogRenderer.class)
public class FogRendererMixin {

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientLevel;getRainLevel(F)F"), method = "setupColor")
    private static float rainColor(ClientLevel instance, float v) {
        if (instance.getBiome(Minecraft.getInstance().player.blockPosition()).is(OUATBiomes.STORMY_SEA)) {
            return 1;
        }
        return instance.getRainLevel(v);
    }

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientLevel;getThunderLevel(F)F"), method = "setupColor")
    private static float thunderColor(ClientLevel instance, float v) {
        if (instance.getBiome(Minecraft.getInstance().player.blockPosition()).is(OUATBiomes.STORMY_SEA)) {
            return 1;
        }
        return instance.getThunderLevel(v);
    }
}

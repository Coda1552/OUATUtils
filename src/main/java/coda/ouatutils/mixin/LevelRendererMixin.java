package coda.ouatutils.mixin;

import coda.ouatutils.terrablender.OUATBiomes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraftforge.client.IWeatherRenderHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin {

    @Inject(at = @At("HEAD"), method = "Lnet/minecraft/client/renderer/LevelRenderer;renderSnowAndRain(Lnet/minecraft/client/renderer/LightTexture;FDDD)V")
    private void renderSnowAndRain(LightTexture p_109704_, float p_109705_, double p_109706_, double p_109707_, double p_109708_, CallbackInfo ci) {
/*        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer player = minecraft.player;
        ClientLevel level = minecraft.level;

        if (level.getBiomeManager().getBiome(player.blockPosition()).is(OUATBiomes.STORMY_SEA)) {
            IWeatherRenderHandler renderHandler = level.effects().getWeatherRenderHandler();

            if (renderHandler == null) {
                level.effects().setWeatherRenderHandler(renderHandler);

                renderHandler.render(minecraft.levelRenderer.ticks, p_109705_, level, minecraft, p_109704_, p_109706_, p_109707_, p_109708_);
            }
        }*/
    }

}

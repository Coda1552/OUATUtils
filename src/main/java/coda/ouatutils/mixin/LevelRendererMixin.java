package coda.ouatutils.mixin;

import coda.ouatutils.terrablender.OUATBiomes;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.Heightmap;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;
import static net.minecraft.client.renderer.LevelRenderer.getLightColor;

@Debug(print = true, export = true)
@Mixin(LevelRenderer.class)
public class LevelRendererMixin {

    private static final ResourceLocation RAIN_LOCATION = new ResourceLocation("textures/environment/rain.png");
    @Shadow
    public int ticks;
    @Final
    @Shadow
    private float[] rainSizeX;
    @Final
    @Shadow
    private float[] rainSizeZ;

    @Inject(at = @At("TAIL"), method = "Lnet/minecraft/client/renderer/LevelRenderer;renderSnowAndRain(Lnet/minecraft/client/renderer/LightTexture;FDDD)V")
    private void OUAT$renderRain(LightTexture pLightTexture, float pPartialTick, double pCamX, double pCamY, double pCamZ, CallbackInfo ci) {
        pLightTexture.turnOnLightLayer();
        Level level = Minecraft.getInstance().level;
        int i = Mth.floor(pCamX);
        int j = Mth.floor(pCamY);
        int k = Mth.floor(pCamZ);
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferbuilder = tesselator.getBuilder();
        RenderSystem.disableCull();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        int l = 1;
        if (Minecraft.useFancyGraphics()) {
            l = 2;
        }

        RenderSystem.depthMask(Minecraft.useShaderTransparency());
        int i1 = -1;
        RenderSystem.setShader(GameRenderer::getParticleShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

        for(int j1 = k - l; j1 <= k + l; ++j1) {
            for(int k1 = i - l; k1 <= i + l; ++k1) {
                int l1 = (j1 - k + 16) * 32 + k1 - i + 16;
                double d0 = (double)this.rainSizeX[l1] * 0.5D;
                double d1 = (double)this.rainSizeZ[l1] * 0.5D;
                blockpos$mutableblockpos.set((double)k1, pCamY, (double)j1);
                Biome biome = level.getBiome(blockpos$mutableblockpos).value();
                if (!OUATBiomes.STORMY_SEA.getRegistryName().equals(biome.getRegistryName())) {
                    continue;
                }
                int i2 = level.getHeight(Heightmap.Types.MOTION_BLOCKING, k1, j1);
                int j2 = j - l;
                int k2 = j + l;
                if (j2 < i2) {
                    j2 = i2;
                }

                if (k2 < i2) {
                    k2 = i2;
                }

                int l2 = Math.max(i2, j);

                if (j2 != k2) {
                    Random random = new Random((long) (k1 * k1 * 3121 + k1 * 45238971 ^ j1 * j1 * 418711 + j1 * 13761));
                    blockpos$mutableblockpos.set(k1, j2, j1);
                    if (i1 != 0) {
                        if (i1 >= 0) {
                            tesselator.end();
                        }

                        i1 = 0;
                        RenderSystem.setShaderTexture(0, RAIN_LOCATION);
                        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
                    }

                    int i3 = this.ticks + k1 * k1 * 3121 + k1 * 45238971 + j1 * j1 * 418711 + j1 * 13761 & 31;
                    float f2 = -((float) i3 + pPartialTick) / 32.0F * (3.0F + random.nextFloat());
                    double d2 = (double) k1 + 0.5D - pCamX;
                    double d4 = (double) j1 + 0.5D - pCamZ;
                    float f3 = (float) Math.sqrt(d2 * d2 + d4 * d4) / (float) l;
                    float f4 = ((1.0F - f3 * f3) * 0.5F + 0.5F);
                    blockpos$mutableblockpos.set(k1, l2, j1);
                    int j3 = getLightColor(level, blockpos$mutableblockpos);
                    bufferbuilder.vertex((double) k1 - pCamX - d0 + 0.5D, (double) k2 - pCamY, (double) j1 - pCamZ - d1 + 0.5D).uv(0.0F, (float) j2 * 0.25F + f2).color(1.0F, 1.0F, 1.0F, f4).uv2(j3).endVertex();
                    bufferbuilder.vertex((double) k1 - pCamX + d0 + 0.5D, (double) k2 - pCamY, (double) j1 - pCamZ + d1 + 0.5D).uv(1.0F, (float) j2 * 0.25F + f2).color(1.0F, 1.0F, 1.0F, f4).uv2(j3).endVertex();
                    bufferbuilder.vertex((double) k1 - pCamX + d0 + 0.5D, (double) j2 - pCamY, (double) j1 - pCamZ + d1 + 0.5D).uv(1.0F, (float) k2 * 0.25F + f2).color(1.0F, 1.0F, 1.0F, f4).uv2(j3).endVertex();
                    bufferbuilder.vertex((double) k1 - pCamX - d0 + 0.5D, (double) j2 - pCamY, (double) j1 - pCamZ - d1 + 0.5D).uv(0.0F, (float) k2 * 0.25F + f2).color(1.0F, 1.0F, 1.0F, f4).uv2(j3).endVertex();
                }
            }
        }

        if (i1 >= 0) {
            tesselator.end();
        }

        RenderSystem.enableCull();
        RenderSystem.disableBlend();
        pLightTexture.turnOffLightLayer();
    }

}

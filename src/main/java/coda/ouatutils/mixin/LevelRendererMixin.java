package coda.ouatutils.mixin;

import coda.ouatutils.terrablender.OUATBiomes;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.ParticleStatus;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
import java.util.Random;

import static net.minecraft.client.renderer.LevelRenderer.getLightColor;

@Mixin(LevelRenderer.class)
public abstract class LevelRendererMixin {

    private static final ResourceLocation RAIN_LOCATION = new ResourceLocation("textures/environment/rain.png");
    @Shadow
    public int ticks;
    @Final
    @Shadow
    private float[] rainSizeX;
    @Final
    @Shadow
    private float[] rainSizeZ;
    @Final
    @Shadow
    private Minecraft minecraft;
    @Shadow
    @Nullable
    private ClientLevel level;
    @Unique
    private int rainSoundTime;

    @Inject(at = @At("HEAD"), method = "Lnet/minecraft/client/renderer/LevelRenderer;renderSnowAndRain(Lnet/minecraft/client/renderer/LightTexture;FDDD)V")
    private void OUAT$renderRain(LightTexture pLightTexture, float pPartialTick, double pCamX, double pCamY, double pCamZ, CallbackInfo ci) {
        pLightTexture.turnOnLightLayer();
        if (level.getRainLevel(pPartialTick) > 0) {
           return;
        }
        int i = Mth.floor(pCamX);
        int j = Mth.floor(pCamY);
        int k = Mth.floor(pCamZ);
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferbuilder = tesselator.getBuilder();
        RenderSystem.disableCull();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        int l = 5;
        if (Minecraft.useFancyGraphics()) {
            l = 10;
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
                    if (level.getBiomeManager().getBiome(blockpos$mutableblockpos).is(OUATBiomes.STORMY_SEA)) {
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
        }

        if (i1 >= 0) {
            tesselator.end();
        }

        RenderSystem.enableCull();
        RenderSystem.disableBlend();
        pLightTexture.turnOffLightLayer();
    }

    @Inject(at = @At("HEAD"), method = "tickRain")
    public void rainSound(Camera pCamera, CallbackInfo ci) {
        Random random = new Random((long)this.ticks * 312987231L);
        LevelReader levelreader = this.minecraft.level;
        BlockPos blockpos = new BlockPos(pCamera.getPosition());
        BlockPos blockpos1 = null;
        int i = (int)(100.0F) / (this.minecraft.options.particles().get() == ParticleStatus.DECREASED ? 2 : 1);

        for(int j = 0; j < i; ++j) {
            int k = random.nextInt(21) - 10;
            int l = random.nextInt(21) - 10;
            BlockPos blockpos2 = levelreader.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, blockpos.offset(k, 0, l));
            if (blockpos2.getY() > levelreader.getMinBuildHeight() && blockpos2.getY() <= blockpos.getY() + 10 && blockpos2.getY() >= blockpos.getY() - 10 && level.getBiome(blockpos2).is(OUATBiomes.STORMY_SEA)) {
                blockpos1 = blockpos2.below();
                if (this.minecraft.options.particles().get() == ParticleStatus.MINIMAL) {
                    break;
                }

                double d0 = random.nextDouble();
                double d1 = random.nextDouble();
                BlockState blockstate = levelreader.getBlockState(blockpos1);
                FluidState fluidstate = levelreader.getFluidState(blockpos1);
                VoxelShape voxelshape = blockstate.getCollisionShape(levelreader, blockpos1);
                double d2 = voxelshape.max(Direction.Axis.Y, d0, d1);
                double d3 = fluidstate.getHeight(levelreader, blockpos1);
                double d4 = Math.max(d2, d3);
                ParticleOptions particleoptions = !fluidstate.is(FluidTags.LAVA) && !blockstate.is(Blocks.MAGMA_BLOCK) && !CampfireBlock.isLitCampfire(blockstate) ? ParticleTypes.RAIN : ParticleTypes.SMOKE;
                this.minecraft.level.addParticle(particleoptions, (double)blockpos1.getX() + d0, (double)blockpos1.getY() + d4, (double)blockpos1.getZ() + d1, 0.0D, 0.0D, 0.0D);
            }
        }

        if (blockpos1 != null && random.nextInt(3) < this.rainSoundTime++) {
            this.rainSoundTime = 0;
            if (blockpos1.getY() > blockpos.getY() + 1 && levelreader.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, blockpos).getY() > Mth.floor((float)blockpos.getY())) {
                this.minecraft.level.playLocalSound(blockpos1, SoundEvents.WEATHER_RAIN_ABOVE, SoundSource.WEATHER, 0.1F, 0.5F, false);
            } else {
                this.minecraft.level.playLocalSound(blockpos1, SoundEvents.WEATHER_RAIN, SoundSource.WEATHER, 0.2F, 1.0F, false);
            }
        }

    }
}

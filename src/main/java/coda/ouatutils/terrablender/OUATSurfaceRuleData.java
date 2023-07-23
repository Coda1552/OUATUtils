package coda.ouatutils.terrablender;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.SurfaceRules;
import terrablender.api.SurfaceRuleManager;

public class OUATSurfaceRuleData {

    public static SurfaceRules.RuleSource makeRules() {

        return SurfaceRules.sequence(
            SurfaceRules.ifTrue(SurfaceRules.isBiome(OUATBiomes.STORMY_SEA), SurfaceRuleManager.getDefaultSurfaceRules(SurfaceRuleManager.RuleCategory.OVERWORLD))
        );
    }

    private static SurfaceRules.RuleSource makeStateRule(Block block) {
        return SurfaceRules.state(block.defaultBlockState());
    }
}
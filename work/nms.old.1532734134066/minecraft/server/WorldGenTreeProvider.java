package net.minecraft.server;

import java.util.Random;
import javax.annotation.Nullable;
import org.bukkit.TreeType; // CraftBukkit

public abstract class WorldGenTreeProvider {

    public WorldGenTreeProvider() {}

    @Nullable
    protected abstract WorldGenTreeAbstract<WorldGenFeatureEmptyConfiguration> b(Random random);

    public boolean a(GeneratorAccess generatoraccess, BlockPosition blockposition, IBlockData iblockdata, Random random) {
        WorldGenTreeAbstract worldgentreeabstract = this.b(random);

        if (worldgentreeabstract == null) {
            return false;
        } else {
            // CraftBukkit start
            if (worldgentreeabstract instanceof WorldGenAcaciaTree) {
                BlockSapling.treeType = TreeType.ACACIA;
            } else if (worldgentreeabstract instanceof WorldGenBigTree) {
                BlockSapling.treeType = TreeType.BIG_TREE;
            } else if (worldgentreeabstract instanceof WorldGenForest) {
                BlockSapling.treeType = TreeType.BIRCH;
            } else if (worldgentreeabstract instanceof WorldGenForestTree) {
                BlockSapling.treeType = TreeType.DARK_OAK;
            } else if (worldgentreeabstract instanceof WorldGenJungleTree) {
                BlockSapling.treeType = TreeType.JUNGLE;
            } else if (worldgentreeabstract instanceof WorldGenMegaTree) {
                BlockSapling.treeType = TreeType.MEGA_REDWOOD;
            } else if (worldgentreeabstract instanceof WorldGenTaiga1) {
                BlockSapling.treeType = TreeType.REDWOOD;
            } else if (worldgentreeabstract instanceof WorldGenTaiga2) {
                BlockSapling.treeType = TreeType.REDWOOD;
            } else if (worldgentreeabstract instanceof WorldGenTrees) {
                BlockSapling.treeType = TreeType.TREE;
            } else {
                throw new IllegalArgumentException("Unknown tree generator " + worldgentreeabstract);
            }
            // CraftBukkit end
            generatoraccess.setTypeAndData(blockposition, Blocks.AIR.getBlockData(), 4);
            if (worldgentreeabstract.generate(generatoraccess, generatoraccess.getChunkProvider().getChunkGenerator(), random, blockposition, WorldGenFeatureConfiguration.e)) {
                return true;
            } else {
                generatoraccess.setTypeAndData(blockposition, iblockdata, 4);
                return false;
            }
        }
    }
}

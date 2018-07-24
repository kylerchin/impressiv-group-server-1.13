package net.minecraft.server;

import java.util.Random;
import javax.annotation.Nullable;

public class BlockSeaPickle extends BlockPlant implements IBlockFragilePlantElement, IFluidSource, IFluidContainer {

    public static final BlockStateInteger a = BlockProperties.aj;
    public static final BlockStateBoolean b = BlockProperties.x;
    protected static final VoxelShape c = Block.a(6.0D, 0.0D, 6.0D, 10.0D, 6.0D, 10.0D);
    protected static final VoxelShape p = Block.a(3.0D, 0.0D, 3.0D, 13.0D, 6.0D, 13.0D);
    protected static final VoxelShape q = Block.a(2.0D, 0.0D, 2.0D, 14.0D, 6.0D, 14.0D);
    protected static final VoxelShape r = Block.a(2.0D, 0.0D, 2.0D, 14.0D, 7.0D, 14.0D);

    protected BlockSeaPickle(Block.Info block_info) {
        super(block_info);
        this.v((IBlockData) ((IBlockData) ((IBlockData) this.blockStateList.getBlockData()).set(BlockSeaPickle.a, Integer.valueOf(1))).set(BlockSeaPickle.b, Boolean.valueOf(true)));
    }

    public int m(IBlockData iblockdata) {
        return this.k(iblockdata) ? 0 : super.m(iblockdata) + 3 * ((Integer) iblockdata.get(BlockSeaPickle.a)).intValue();
    }

    @Nullable
    public IBlockData getPlacedState(BlockActionContext blockactioncontext) {
        IBlockData iblockdata = blockactioncontext.getWorld().getType(blockactioncontext.getClickPosition());

        if (iblockdata.getBlock() == this) {
            return (IBlockData) iblockdata.set(BlockSeaPickle.a, Integer.valueOf(Math.min(4, ((Integer) iblockdata.get(BlockSeaPickle.a)).intValue() + 1)));
        } else {
            Fluid fluid = blockactioncontext.getWorld().b(blockactioncontext.getClickPosition());
            boolean flag = fluid.a(TagsFluid.a) && fluid.g() == 8;

            return (IBlockData) super.getPlacedState(blockactioncontext).set(BlockSeaPickle.b, Boolean.valueOf(flag));
        }
    }

    private boolean k(IBlockData iblockdata) {
        return !((Boolean) iblockdata.get(BlockSeaPickle.b)).booleanValue();
    }

    protected boolean b(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition) {
        return !VoxelShapes.a(iblockdata.h(iblockaccess, blockposition), EnumDirection.UP).b();
    }

    public boolean canPlace(IBlockData iblockdata, IWorldReader iworldreader, BlockPosition blockposition) {
        BlockPosition blockposition1 = blockposition.down();

        return this.b(iworldreader.getType(blockposition1), (IBlockAccess) iworldreader, blockposition1);
    }

    public IBlockData updateState(IBlockData iblockdata, EnumDirection enumdirection, IBlockData iblockdata1, GeneratorAccess generatoraccess, BlockPosition blockposition, BlockPosition blockposition1) {
        if (!iblockdata.canPlace(generatoraccess, blockposition)) {
            return Blocks.AIR.getBlockData();
        } else {
            if (((Boolean) iblockdata.get(BlockSeaPickle.b)).booleanValue()) {
                generatoraccess.H().a(blockposition, FluidTypes.c, FluidTypes.c.a((IWorldReader) generatoraccess));
            }

            return super.updateState(iblockdata, enumdirection, iblockdata1, generatoraccess, blockposition, blockposition1);
        }
    }

    public boolean a(IBlockData iblockdata, BlockActionContext blockactioncontext) {
        return blockactioncontext.getItemStack().getItem() == this.getItem() && ((Integer) iblockdata.get(BlockSeaPickle.a)).intValue() < 4 ? true : super.a(iblockdata, blockactioncontext);
    }

    public VoxelShape a(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition) {
        switch (((Integer) iblockdata.get(BlockSeaPickle.a)).intValue()) {
        case 1:
        default:
            return BlockSeaPickle.c;

        case 2:
            return BlockSeaPickle.p;

        case 3:
            return BlockSeaPickle.q;

        case 4:
            return BlockSeaPickle.r;
        }
    }

    public FluidType a(GeneratorAccess generatoraccess, BlockPosition blockposition, IBlockData iblockdata) {
        if (((Boolean) iblockdata.get(BlockSeaPickle.b)).booleanValue()) {
            generatoraccess.setTypeAndData(blockposition, (IBlockData) iblockdata.set(BlockSeaPickle.b, Boolean.valueOf(false)), 3);
            return FluidTypes.c;
        } else {
            return FluidTypes.a;
        }
    }

    public Fluid h(IBlockData iblockdata) {
        return ((Boolean) iblockdata.get(BlockSeaPickle.b)).booleanValue() ? FluidTypes.c.a(false) : super.h(iblockdata);
    }

    public boolean a(IBlockAccess iblockaccess, BlockPosition blockposition, IBlockData iblockdata, FluidType fluidtype) {
        return !((Boolean) iblockdata.get(BlockSeaPickle.b)).booleanValue() && fluidtype == FluidTypes.c;
    }

    public boolean a(GeneratorAccess generatoraccess, BlockPosition blockposition, IBlockData iblockdata, Fluid fluid) {
        if (!((Boolean) iblockdata.get(BlockSeaPickle.b)).booleanValue() && fluid.c() == FluidTypes.c) {
            if (!generatoraccess.e()) {
                generatoraccess.setTypeAndData(blockposition, (IBlockData) iblockdata.set(BlockSeaPickle.b, Boolean.valueOf(true)), 3);
                generatoraccess.H().a(blockposition, fluid.c(), fluid.c().a((IWorldReader) generatoraccess));
            }

            return true;
        } else {
            return false;
        }
    }

    protected void a(BlockStateList.a<Block, IBlockData> blockstatelist_a) {
        blockstatelist_a.a(new IBlockState[] { BlockSeaPickle.a, BlockSeaPickle.b});
    }

    public int a(IBlockData iblockdata, Random random) {
        return ((Integer) iblockdata.get(BlockSeaPickle.a)).intValue();
    }

    public boolean a(IBlockAccess iblockaccess, BlockPosition blockposition, IBlockData iblockdata, boolean flag) {
        return true;
    }

    public boolean a(World world, Random random, BlockPosition blockposition, IBlockData iblockdata) {
        return true;
    }

    public void b(World world, Random random, BlockPosition blockposition, IBlockData iblockdata) {
        if (!this.k(iblockdata) && world.getType(blockposition.down()).a(TagsBlock.A)) {
            boolean flag = true;
            int i = 1;
            boolean flag1 = true;
            int j = 0;
            int k = blockposition.getX() - 2;
            int l = 0;

            for (int i1 = 0; i1 < 5; ++i1) {
                for (int j1 = 0; j1 < i; ++j1) {
                    int k1 = 2 + blockposition.getY() - 1;

                    for (int l1 = k1 - 2; l1 < k1; ++l1) {
                        BlockPosition blockposition1 = new BlockPosition(k + i1, l1, blockposition.getZ() - l + j1);

                        if (blockposition1 != blockposition && random.nextInt(6) == 0 && world.getType(blockposition1).getBlock() == Blocks.WATER) {
                            IBlockData iblockdata1 = world.getType(blockposition1.down());

                            if (iblockdata1.a(TagsBlock.A)) {
                                world.setTypeAndData(blockposition1, (IBlockData) Blocks.SEA_PICKLE.getBlockData().set(BlockSeaPickle.a, Integer.valueOf(random.nextInt(4) + 1)), 3);
                            }
                        }
                    }
                }

                if (j < 2) {
                    i += 2;
                    ++l;
                } else {
                    i -= 2;
                    --l;
                }

                ++j;
            }

            world.setTypeAndData(blockposition, (IBlockData) iblockdata.set(BlockSeaPickle.a, Integer.valueOf(4)), 2);
        }

    }
}

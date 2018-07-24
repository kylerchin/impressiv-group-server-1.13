package net.minecraft.server;

import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Predicate;

public class BlockTall extends Block implements IFluidSource, IFluidContainer {

    public static final BlockStateBoolean NORTH = BlockSprawling.a;
    public static final BlockStateBoolean EAST = BlockSprawling.b;
    public static final BlockStateBoolean SOUTH = BlockSprawling.c;
    public static final BlockStateBoolean WEST = BlockSprawling.p;
    public static final BlockStateBoolean q = BlockProperties.x;
    protected static final Map<EnumDirection, BlockStateBoolean> r = (Map) BlockSprawling.s.entrySet().stream().filter((entry) -> {
        return ((EnumDirection) entry.getKey()).k().c();
    }).collect(SystemUtils.a());
    protected final VoxelShape[] s;
    protected final VoxelShape[] t;

    protected BlockTall(float f, float f1, float f2, float f3, float f4, Block.Info block_info) {
        super(block_info);
        this.s = this.a(f, f1, f4, 0.0F, f4);
        this.t = this.a(f, f1, f2, 0.0F, f3);
    }

    protected VoxelShape[] a(float f, float f1, float f2, float f3, float f4) {
        float f5 = 8.0F - f;
        float f6 = 8.0F + f;
        float f7 = 8.0F - f1;
        float f8 = 8.0F + f1;
        VoxelShape voxelshape = Block.a((double) f5, 0.0D, (double) f5, (double) f6, (double) f2, (double) f6);
        VoxelShape voxelshape1 = Block.a((double) f7, (double) f3, 0.0D, (double) f8, (double) f4, (double) f8);
        VoxelShape voxelshape2 = Block.a((double) f7, (double) f3, (double) f7, (double) f8, (double) f4, 16.0D);
        VoxelShape voxelshape3 = Block.a(0.0D, (double) f3, (double) f7, (double) f8, (double) f4, (double) f8);
        VoxelShape voxelshape4 = Block.a((double) f7, (double) f3, (double) f7, 16.0D, (double) f4, (double) f8);
        VoxelShape voxelshape5 = VoxelShapes.a(voxelshape1, voxelshape4);
        VoxelShape voxelshape6 = VoxelShapes.a(voxelshape2, voxelshape3);
        VoxelShape[] avoxelshape = new VoxelShape[] { VoxelShapes.a(), voxelshape2, voxelshape3, voxelshape6, voxelshape1, VoxelShapes.a(voxelshape2, voxelshape1), VoxelShapes.a(voxelshape3, voxelshape1), VoxelShapes.a(voxelshape6, voxelshape1), voxelshape4, VoxelShapes.a(voxelshape2, voxelshape4), VoxelShapes.a(voxelshape3, voxelshape4), VoxelShapes.a(voxelshape6, voxelshape4), voxelshape5, VoxelShapes.a(voxelshape2, voxelshape5), VoxelShapes.a(voxelshape3, voxelshape5), VoxelShapes.a(voxelshape6, voxelshape5)};

        for (int i = 0; i < 16; ++i) {
            avoxelshape[i] = VoxelShapes.a(voxelshape, avoxelshape[i]);
        }

        return avoxelshape;
    }

    public VoxelShape a(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition) {
        return this.t[this.k(iblockdata)];
    }

    public VoxelShape f(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition) {
        return this.s[this.k(iblockdata)];
    }

    private static int a(EnumDirection enumdirection) {
        return 1 << enumdirection.get2DRotationValue();
    }

    protected int k(IBlockData iblockdata) {
        int i = 0;

        if (((Boolean) iblockdata.get(BlockTall.NORTH)).booleanValue()) {
            i |= a(EnumDirection.NORTH);
        }

        if (((Boolean) iblockdata.get(BlockTall.EAST)).booleanValue()) {
            i |= a(EnumDirection.EAST);
        }

        if (((Boolean) iblockdata.get(BlockTall.SOUTH)).booleanValue()) {
            i |= a(EnumDirection.SOUTH);
        }

        if (((Boolean) iblockdata.get(BlockTall.WEST)).booleanValue()) {
            i |= a(EnumDirection.WEST);
        }

        return i;
    }

    public FluidType a(GeneratorAccess generatoraccess, BlockPosition blockposition, IBlockData iblockdata) {
        if (((Boolean) iblockdata.get(BlockTall.q)).booleanValue()) {
            generatoraccess.setTypeAndData(blockposition, (IBlockData) iblockdata.set(BlockTall.q, Boolean.valueOf(false)), 3);
            return FluidTypes.c;
        } else {
            return FluidTypes.a;
        }
    }

    public Fluid h(IBlockData iblockdata) {
        return ((Boolean) iblockdata.get(BlockTall.q)).booleanValue() ? FluidTypes.c.a(false) : super.h(iblockdata);
    }

    public boolean a(IBlockAccess iblockaccess, BlockPosition blockposition, IBlockData iblockdata, FluidType fluidtype) {
        return !((Boolean) iblockdata.get(BlockTall.q)).booleanValue() && fluidtype == FluidTypes.c;
    }

    public boolean a(GeneratorAccess generatoraccess, BlockPosition blockposition, IBlockData iblockdata, Fluid fluid) {
        if (!((Boolean) iblockdata.get(BlockTall.q)).booleanValue() && fluid.c() == FluidTypes.c) {
            if (!generatoraccess.e()) {
                generatoraccess.setTypeAndData(blockposition, (IBlockData) iblockdata.set(BlockTall.q, Boolean.valueOf(true)), 3);
                generatoraccess.H().a(blockposition, fluid.c(), fluid.c().a((IWorldReader) generatoraccess));
            }

            return true;
        } else {
            return false;
        }
    }

    public boolean a(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition, PathMode pathmode) {
        return false;
    }

    public IBlockData a(IBlockData iblockdata, EnumBlockRotation enumblockrotation) {
        switch (enumblockrotation) {
        case CLOCKWISE_180:
            return (IBlockData) ((IBlockData) ((IBlockData) ((IBlockData) iblockdata.set(BlockTall.NORTH, iblockdata.get(BlockTall.SOUTH))).set(BlockTall.EAST, iblockdata.get(BlockTall.WEST))).set(BlockTall.SOUTH, iblockdata.get(BlockTall.NORTH))).set(BlockTall.WEST, iblockdata.get(BlockTall.EAST));

        case COUNTERCLOCKWISE_90:
            return (IBlockData) ((IBlockData) ((IBlockData) ((IBlockData) iblockdata.set(BlockTall.NORTH, iblockdata.get(BlockTall.EAST))).set(BlockTall.EAST, iblockdata.get(BlockTall.SOUTH))).set(BlockTall.SOUTH, iblockdata.get(BlockTall.WEST))).set(BlockTall.WEST, iblockdata.get(BlockTall.NORTH));

        case CLOCKWISE_90:
            return (IBlockData) ((IBlockData) ((IBlockData) ((IBlockData) iblockdata.set(BlockTall.NORTH, iblockdata.get(BlockTall.WEST))).set(BlockTall.EAST, iblockdata.get(BlockTall.NORTH))).set(BlockTall.SOUTH, iblockdata.get(BlockTall.EAST))).set(BlockTall.WEST, iblockdata.get(BlockTall.SOUTH));

        default:
            return iblockdata;
        }
    }

    public IBlockData a(IBlockData iblockdata, EnumBlockMirror enumblockmirror) {
        switch (enumblockmirror) {
        case LEFT_RIGHT:
            return (IBlockData) ((IBlockData) iblockdata.set(BlockTall.NORTH, iblockdata.get(BlockTall.SOUTH))).set(BlockTall.SOUTH, iblockdata.get(BlockTall.NORTH));

        case FRONT_BACK:
            return (IBlockData) ((IBlockData) iblockdata.set(BlockTall.EAST, iblockdata.get(BlockTall.WEST))).set(BlockTall.WEST, iblockdata.get(BlockTall.EAST));

        default:
            return super.a(iblockdata, enumblockmirror);
        }
    }
}

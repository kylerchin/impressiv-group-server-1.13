package net.minecraft.server;

import javax.annotation.Nullable;
import org.bukkit.event.block.BlockRedstoneEvent; // CraftBukkit

public class BlockTrapdoor extends BlockFacingHorizontal implements IFluidSource, IFluidContainer {

    public static final BlockStateBoolean OPEN = BlockProperties.r;
    public static final BlockStateEnum<BlockPropertyHalf> HALF = BlockProperties.P;
    public static final BlockStateBoolean c = BlockProperties.t;
    public static final BlockStateBoolean p = BlockProperties.x;
    protected static final VoxelShape q = Block.a(0.0D, 0.0D, 0.0D, 3.0D, 16.0D, 16.0D);
    protected static final VoxelShape r = Block.a(13.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    protected static final VoxelShape s = Block.a(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 3.0D);
    protected static final VoxelShape t = Block.a(0.0D, 0.0D, 13.0D, 16.0D, 16.0D, 16.0D);
    protected static final VoxelShape u = Block.a(0.0D, 0.0D, 0.0D, 16.0D, 3.0D, 16.0D);
    protected static final VoxelShape v = Block.a(0.0D, 13.0D, 0.0D, 16.0D, 16.0D, 16.0D);

    protected BlockTrapdoor(Block.Info block_info) {
        super(block_info);
        this.v((IBlockData) ((IBlockData) ((IBlockData) ((IBlockData) ((IBlockData) ((IBlockData) this.blockStateList.getBlockData()).set(BlockTrapdoor.FACING, EnumDirection.NORTH)).set(BlockTrapdoor.OPEN, Boolean.valueOf(false))).set(BlockTrapdoor.HALF, BlockPropertyHalf.BOTTOM)).set(BlockTrapdoor.c, Boolean.valueOf(false))).set(BlockTrapdoor.p, Boolean.valueOf(false)));
    }

    public VoxelShape a(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition) {
        if (!((Boolean) iblockdata.get(BlockTrapdoor.OPEN)).booleanValue()) {
            return iblockdata.get(BlockTrapdoor.HALF) == BlockPropertyHalf.TOP ? BlockTrapdoor.v : BlockTrapdoor.u;
        } else {
            switch ((EnumDirection) iblockdata.get(BlockTrapdoor.FACING)) {
            case NORTH:
            default:
                return BlockTrapdoor.t;

            case SOUTH:
                return BlockTrapdoor.s;

            case WEST:
                return BlockTrapdoor.r;

            case EAST:
                return BlockTrapdoor.q;
            }
        }
    }

    public boolean a(IBlockData iblockdata) {
        return false;
    }

    public boolean a(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition, PathMode pathmode) {
        switch (pathmode) {
        case LAND:
            return ((Boolean) iblockdata.get(BlockTrapdoor.OPEN)).booleanValue();

        case WATER:
            return ((Boolean) iblockdata.get(BlockTrapdoor.p)).booleanValue();

        case AIR:
            return ((Boolean) iblockdata.get(BlockTrapdoor.OPEN)).booleanValue();

        default:
            return false;
        }
    }

    public boolean interact(IBlockData iblockdata, World world, BlockPosition blockposition, EntityHuman entityhuman, EnumHand enumhand, EnumDirection enumdirection, float f, float f1, float f2) {
        if (this.material == Material.ORE) {
            return false;
        } else {
            iblockdata = (IBlockData) iblockdata.a((IBlockState) BlockTrapdoor.OPEN);
            world.setTypeAndData(blockposition, iblockdata, 2);
            if (((Boolean) iblockdata.get(BlockTrapdoor.p)).booleanValue()) {
                world.H().a(blockposition, FluidTypes.c, FluidTypes.c.a((IWorldReader) world));
            }

            this.a(entityhuman, world, blockposition, ((Boolean) iblockdata.get(BlockTrapdoor.OPEN)).booleanValue());
            return true;
        }
    }

    protected void a(@Nullable EntityHuman entityhuman, World world, BlockPosition blockposition, boolean flag) {
        int i;

        if (flag) {
            i = this.material == Material.ORE ? 1037 : 1007;
            world.a(entityhuman, i, blockposition, 0);
        } else {
            i = this.material == Material.ORE ? 1036 : 1013;
            world.a(entityhuman, i, blockposition, 0);
        }

    }

    public void doPhysics(IBlockData iblockdata, World world, BlockPosition blockposition, Block block, BlockPosition blockposition1) {
        if (!world.isClientSide) {
            boolean flag = world.isBlockIndirectlyPowered(blockposition);

            if (flag != ((Boolean) iblockdata.get(BlockTrapdoor.c)).booleanValue()) {
                // CraftBukkit start
                org.bukkit.World bworld = world.getWorld();
                org.bukkit.block.Block bblock = bworld.getBlockAt(blockposition.getX(), blockposition.getY(), blockposition.getZ());

                int power = bblock.getBlockPower();
                int oldPower = (Boolean) iblockdata.get(OPEN) ? 15 : 0;

                if (oldPower == 0 ^ power == 0 || block.getBlockData().isPowerSource()) {
                    BlockRedstoneEvent eventRedstone = new BlockRedstoneEvent(bblock, oldPower, power);
                    world.getServer().getPluginManager().callEvent(eventRedstone);
                    flag = eventRedstone.getNewCurrent() > 0;
                }
                // CraftBukkit end
                if (((Boolean) iblockdata.get(BlockTrapdoor.OPEN)).booleanValue() != flag) {
                    iblockdata = (IBlockData) iblockdata.set(BlockTrapdoor.OPEN, Boolean.valueOf(flag));
                    this.a((EntityHuman) null, world, blockposition, flag);
                }

                world.setTypeAndData(blockposition, (IBlockData) iblockdata.set(BlockTrapdoor.c, Boolean.valueOf(flag)), 2);
                if (((Boolean) iblockdata.get(BlockTrapdoor.p)).booleanValue()) {
                    world.H().a(blockposition, FluidTypes.c, FluidTypes.c.a((IWorldReader) world));
                }
            }

        }
    }

    public IBlockData getPlacedState(BlockActionContext blockactioncontext) {
        IBlockData iblockdata = this.getBlockData();
        Fluid fluid = blockactioncontext.getWorld().b(blockactioncontext.getClickPosition());
        EnumDirection enumdirection = blockactioncontext.getClickedFace();

        if (!blockactioncontext.c() && enumdirection.k().c()) {
            iblockdata = (IBlockData) ((IBlockData) iblockdata.set(BlockTrapdoor.FACING, enumdirection)).set(BlockTrapdoor.HALF, blockactioncontext.n() > 0.5F ? BlockPropertyHalf.TOP : BlockPropertyHalf.BOTTOM);
        } else {
            iblockdata = (IBlockData) ((IBlockData) iblockdata.set(BlockTrapdoor.FACING, blockactioncontext.f().opposite())).set(BlockTrapdoor.HALF, enumdirection == EnumDirection.UP ? BlockPropertyHalf.BOTTOM : BlockPropertyHalf.TOP);
        }

        if (blockactioncontext.getWorld().isBlockIndirectlyPowered(blockactioncontext.getClickPosition())) {
            iblockdata = (IBlockData) ((IBlockData) iblockdata.set(BlockTrapdoor.OPEN, Boolean.valueOf(true))).set(BlockTrapdoor.c, Boolean.valueOf(true));
        }

        return (IBlockData) iblockdata.set(BlockTrapdoor.p, Boolean.valueOf(fluid.c() == FluidTypes.c));
    }

    public TextureType c() {
        return TextureType.CUTOUT;
    }

    protected void a(BlockStateList.a<Block, IBlockData> blockstatelist_a) {
        blockstatelist_a.a(new IBlockState[] { BlockTrapdoor.FACING, BlockTrapdoor.OPEN, BlockTrapdoor.HALF, BlockTrapdoor.c, BlockTrapdoor.p});
    }

    public EnumBlockFaceShape a(IBlockAccess iblockaccess, IBlockData iblockdata, BlockPosition blockposition, EnumDirection enumdirection) {
        return (enumdirection == EnumDirection.UP && iblockdata.get(BlockTrapdoor.HALF) == BlockPropertyHalf.TOP || enumdirection == EnumDirection.DOWN && iblockdata.get(BlockTrapdoor.HALF) == BlockPropertyHalf.BOTTOM) && !((Boolean) iblockdata.get(BlockTrapdoor.OPEN)).booleanValue() ? EnumBlockFaceShape.SOLID : EnumBlockFaceShape.UNDEFINED;
    }

    public FluidType a(GeneratorAccess generatoraccess, BlockPosition blockposition, IBlockData iblockdata) {
        if (((Boolean) iblockdata.get(BlockTrapdoor.p)).booleanValue()) {
            generatoraccess.setTypeAndData(blockposition, (IBlockData) iblockdata.set(BlockTrapdoor.p, Boolean.valueOf(false)), 3);
            return FluidTypes.c;
        } else {
            return FluidTypes.a;
        }
    }

    public Fluid h(IBlockData iblockdata) {
        return ((Boolean) iblockdata.get(BlockTrapdoor.p)).booleanValue() ? FluidTypes.c.a(false) : super.h(iblockdata);
    }

    public boolean a(IBlockAccess iblockaccess, BlockPosition blockposition, IBlockData iblockdata, FluidType fluidtype) {
        return !((Boolean) iblockdata.get(BlockTrapdoor.p)).booleanValue() && fluidtype == FluidTypes.c;
    }

    public boolean a(GeneratorAccess generatoraccess, BlockPosition blockposition, IBlockData iblockdata, Fluid fluid) {
        if (!((Boolean) iblockdata.get(BlockTrapdoor.p)).booleanValue() && fluid.c() == FluidTypes.c) {
            if (!generatoraccess.e()) {
                generatoraccess.setTypeAndData(blockposition, (IBlockData) iblockdata.set(BlockTrapdoor.p, Boolean.valueOf(true)), 3);
                generatoraccess.H().a(blockposition, fluid.c(), fluid.c().a((IWorldReader) generatoraccess));
            }

            return true;
        } else {
            return false;
        }
    }

    public IBlockData updateState(IBlockData iblockdata, EnumDirection enumdirection, IBlockData iblockdata1, GeneratorAccess generatoraccess, BlockPosition blockposition, BlockPosition blockposition1) {
        if (((Boolean) iblockdata.get(BlockTrapdoor.p)).booleanValue()) {
            generatoraccess.H().a(blockposition, FluidTypes.c, FluidTypes.c.a((IWorldReader) generatoraccess));
        }

        return super.updateState(iblockdata, enumdirection, iblockdata1, generatoraccess, blockposition, blockposition1);
    }
}

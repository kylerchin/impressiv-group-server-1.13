package net.minecraft.server;

public class BlockFenceGate extends BlockFacingHorizontal {

    public static final BlockStateBoolean OPEN = BlockProperties.r;
    public static final BlockStateBoolean POWERED = BlockProperties.t;
    public static final BlockStateBoolean IN_WALL = BlockProperties.n;
    protected static final VoxelShape p = Block.a(0.0D, 0.0D, 6.0D, 16.0D, 16.0D, 10.0D);
    protected static final VoxelShape q = Block.a(6.0D, 0.0D, 0.0D, 10.0D, 16.0D, 16.0D);
    protected static final VoxelShape r = Block.a(0.0D, 0.0D, 6.0D, 16.0D, 13.0D, 10.0D);
    protected static final VoxelShape s = Block.a(6.0D, 0.0D, 0.0D, 10.0D, 13.0D, 16.0D);
    protected static final VoxelShape t = Block.a(0.0D, 0.0D, 6.0D, 16.0D, 24.0D, 10.0D);
    protected static final VoxelShape u = Block.a(6.0D, 0.0D, 0.0D, 10.0D, 24.0D, 16.0D);
    protected static final VoxelShape v = VoxelShapes.a(Block.a(0.0D, 5.0D, 7.0D, 2.0D, 16.0D, 9.0D), Block.a(14.0D, 5.0D, 7.0D, 16.0D, 16.0D, 9.0D));
    protected static final VoxelShape w = VoxelShapes.a(Block.a(7.0D, 5.0D, 0.0D, 9.0D, 16.0D, 2.0D), Block.a(7.0D, 5.0D, 14.0D, 9.0D, 16.0D, 16.0D));
    protected static final VoxelShape x = VoxelShapes.a(Block.a(0.0D, 2.0D, 7.0D, 2.0D, 13.0D, 9.0D), Block.a(14.0D, 2.0D, 7.0D, 16.0D, 13.0D, 9.0D));
    protected static final VoxelShape y = VoxelShapes.a(Block.a(7.0D, 2.0D, 0.0D, 9.0D, 13.0D, 2.0D), Block.a(7.0D, 2.0D, 14.0D, 9.0D, 13.0D, 16.0D));

    public BlockFenceGate(Block.Info block_info) {
        super(block_info);
        this.v((IBlockData) ((IBlockData) ((IBlockData) ((IBlockData) this.blockStateList.getBlockData()).set(BlockFenceGate.OPEN, Boolean.valueOf(false))).set(BlockFenceGate.POWERED, Boolean.valueOf(false))).set(BlockFenceGate.IN_WALL, Boolean.valueOf(false)));
    }

    public VoxelShape a(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition) {
        return ((Boolean) iblockdata.get(BlockFenceGate.IN_WALL)).booleanValue() ? (((EnumDirection) iblockdata.get(BlockFenceGate.FACING)).k() == EnumDirection.EnumAxis.X ? BlockFenceGate.s : BlockFenceGate.r) : (((EnumDirection) iblockdata.get(BlockFenceGate.FACING)).k() == EnumDirection.EnumAxis.X ? BlockFenceGate.q : BlockFenceGate.p);
    }

    public IBlockData updateState(IBlockData iblockdata, EnumDirection enumdirection, IBlockData iblockdata1, GeneratorAccess generatoraccess, BlockPosition blockposition, BlockPosition blockposition1) {
        EnumDirection.EnumAxis enumdirection_enumaxis = enumdirection.k();

        if (((EnumDirection) iblockdata.get(BlockFenceGate.FACING)).e().k() != enumdirection_enumaxis) {
            return super.updateState(iblockdata, enumdirection, iblockdata1, generatoraccess, blockposition, blockposition1);
        } else {
            boolean flag = this.k(iblockdata1) || this.k(generatoraccess.getType(blockposition.shift(enumdirection.opposite())));

            return (IBlockData) iblockdata.set(BlockFenceGate.IN_WALL, Boolean.valueOf(flag));
        }
    }

    public VoxelShape f(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition) {
        return ((Boolean) iblockdata.get(BlockFenceGate.OPEN)).booleanValue() ? VoxelShapes.a() : (((EnumDirection) iblockdata.get(BlockFenceGate.FACING)).k() == EnumDirection.EnumAxis.Z ? BlockFenceGate.t : BlockFenceGate.u);
    }

    public VoxelShape g(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition) {
        return ((Boolean) iblockdata.get(BlockFenceGate.IN_WALL)).booleanValue() ? (((EnumDirection) iblockdata.get(BlockFenceGate.FACING)).k() == EnumDirection.EnumAxis.X ? BlockFenceGate.y : BlockFenceGate.x) : (((EnumDirection) iblockdata.get(BlockFenceGate.FACING)).k() == EnumDirection.EnumAxis.X ? BlockFenceGate.w : BlockFenceGate.v);
    }

    public boolean a(IBlockData iblockdata) {
        return false;
    }

    public boolean a(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition, PathMode pathmode) {
        switch (pathmode) {
        case LAND:
            return ((Boolean) iblockdata.get(BlockFenceGate.OPEN)).booleanValue();

        case WATER:
            return false;

        case AIR:
            return ((Boolean) iblockdata.get(BlockFenceGate.OPEN)).booleanValue();

        default:
            return false;
        }
    }

    public IBlockData getPlacedState(BlockActionContext blockactioncontext) {
        World world = blockactioncontext.getWorld();
        BlockPosition blockposition = blockactioncontext.getClickPosition();
        boolean flag = world.isBlockIndirectlyPowered(blockposition);
        EnumDirection enumdirection = blockactioncontext.f();
        EnumDirection.EnumAxis enumdirection_enumaxis = enumdirection.k();
        boolean flag1 = enumdirection_enumaxis == EnumDirection.EnumAxis.Z && (this.k(world.getType(blockposition.west())) || this.k(world.getType(blockposition.east()))) || enumdirection_enumaxis == EnumDirection.EnumAxis.X && (this.k(world.getType(blockposition.north())) || this.k(world.getType(blockposition.south())));

        return (IBlockData) ((IBlockData) ((IBlockData) ((IBlockData) this.getBlockData().set(BlockFenceGate.FACING, enumdirection)).set(BlockFenceGate.OPEN, Boolean.valueOf(flag))).set(BlockFenceGate.POWERED, Boolean.valueOf(flag))).set(BlockFenceGate.IN_WALL, Boolean.valueOf(flag1));
    }

    private boolean k(IBlockData iblockdata) {
        return iblockdata.getBlock() == Blocks.COBBLESTONE_WALL || iblockdata.getBlock() == Blocks.MOSSY_COBBLESTONE_WALL;
    }

    public boolean interact(IBlockData iblockdata, World world, BlockPosition blockposition, EntityHuman entityhuman, EnumHand enumhand, EnumDirection enumdirection, float f, float f1, float f2) {
        if (((Boolean) iblockdata.get(BlockFenceGate.OPEN)).booleanValue()) {
            iblockdata = (IBlockData) iblockdata.set(BlockFenceGate.OPEN, Boolean.valueOf(false));
            world.setTypeAndData(blockposition, iblockdata, 10);
        } else {
            EnumDirection enumdirection1 = entityhuman.getDirection();

            if (iblockdata.get(BlockFenceGate.FACING) == enumdirection1.opposite()) {
                iblockdata = (IBlockData) iblockdata.set(BlockFenceGate.FACING, enumdirection1);
            }

            iblockdata = (IBlockData) iblockdata.set(BlockFenceGate.OPEN, Boolean.valueOf(true));
            world.setTypeAndData(blockposition, iblockdata, 10);
        }

        world.a(entityhuman, ((Boolean) iblockdata.get(BlockFenceGate.OPEN)).booleanValue() ? 1008 : 1014, blockposition, 0);
        return true;
    }

    public void doPhysics(IBlockData iblockdata, World world, BlockPosition blockposition, Block block, BlockPosition blockposition1) {
        if (!world.isClientSide) {
            boolean flag = world.isBlockIndirectlyPowered(blockposition);

            if (((Boolean) iblockdata.get(BlockFenceGate.POWERED)).booleanValue() != flag) {
                world.setTypeAndData(blockposition, (IBlockData) ((IBlockData) iblockdata.set(BlockFenceGate.POWERED, Boolean.valueOf(flag))).set(BlockFenceGate.OPEN, Boolean.valueOf(flag)), 2);
                if (((Boolean) iblockdata.get(BlockFenceGate.OPEN)).booleanValue() != flag) {
                    world.a((EntityHuman) null, flag ? 1008 : 1014, blockposition, 0);
                }
            }

        }
    }

    protected void a(BlockStateList.a<Block, IBlockData> blockstatelist_a) {
        blockstatelist_a.a(new IBlockState[] { BlockFenceGate.FACING, BlockFenceGate.OPEN, BlockFenceGate.POWERED, BlockFenceGate.IN_WALL});
    }

    public EnumBlockFaceShape a(IBlockAccess iblockaccess, IBlockData iblockdata, BlockPosition blockposition, EnumDirection enumdirection) {
        return enumdirection != EnumDirection.UP && enumdirection != EnumDirection.DOWN ? (((EnumDirection) iblockdata.get(BlockFenceGate.FACING)).k() == enumdirection.e().k() ? EnumBlockFaceShape.MIDDLE_POLE : EnumBlockFaceShape.UNDEFINED) : EnumBlockFaceShape.UNDEFINED;
    }
}

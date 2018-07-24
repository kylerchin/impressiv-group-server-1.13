package net.minecraft.server;

import com.google.common.base.Predicates;

public class BlockEnderPortalFrame extends Block {

    public static final BlockStateDirection FACING = BlockFacingHorizontal.FACING;
    public static final BlockStateBoolean EYE = BlockProperties.g;
    protected static final VoxelShape c = Block.a(0.0D, 0.0D, 0.0D, 16.0D, 13.0D, 16.0D);
    protected static final VoxelShape p = Block.a(4.0D, 13.0D, 4.0D, 12.0D, 16.0D, 12.0D);
    protected static final VoxelShape q = VoxelShapes.a(BlockEnderPortalFrame.c, BlockEnderPortalFrame.p);
    private static ShapeDetector r;

    public BlockEnderPortalFrame(Block.Info block_info) {
        super(block_info);
        this.v((IBlockData) ((IBlockData) ((IBlockData) this.blockStateList.getBlockData()).set(BlockEnderPortalFrame.FACING, EnumDirection.NORTH)).set(BlockEnderPortalFrame.EYE, Boolean.valueOf(false)));
    }

    public VoxelShape a(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition) {
        return ((Boolean) iblockdata.get(BlockEnderPortalFrame.EYE)).booleanValue() ? BlockEnderPortalFrame.q : BlockEnderPortalFrame.c;
    }

    public IMaterial getDropType(IBlockData iblockdata, World world, BlockPosition blockposition, int i) {
        return Items.AIR;
    }

    public IBlockData getPlacedState(BlockActionContext blockactioncontext) {
        return (IBlockData) ((IBlockData) this.getBlockData().set(BlockEnderPortalFrame.FACING, blockactioncontext.f().opposite())).set(BlockEnderPortalFrame.EYE, Boolean.valueOf(false));
    }

    public boolean isComplexRedstone(IBlockData iblockdata) {
        return true;
    }

    public int a(IBlockData iblockdata, World world, BlockPosition blockposition) {
        return ((Boolean) iblockdata.get(BlockEnderPortalFrame.EYE)).booleanValue() ? 15 : 0;
    }

    public IBlockData a(IBlockData iblockdata, EnumBlockRotation enumblockrotation) {
        return (IBlockData) iblockdata.set(BlockEnderPortalFrame.FACING, enumblockrotation.a((EnumDirection) iblockdata.get(BlockEnderPortalFrame.FACING)));
    }

    public IBlockData a(IBlockData iblockdata, EnumBlockMirror enumblockmirror) {
        return iblockdata.a(enumblockmirror.a((EnumDirection) iblockdata.get(BlockEnderPortalFrame.FACING)));
    }

    protected void a(BlockStateList.a<Block, IBlockData> blockstatelist_a) {
        blockstatelist_a.a(new IBlockState[] { BlockEnderPortalFrame.FACING, BlockEnderPortalFrame.EYE});
    }

    public boolean a(IBlockData iblockdata) {
        return false;
    }

    public static ShapeDetector d() {
        if (BlockEnderPortalFrame.r == null) {
            BlockEnderPortalFrame.r = ShapeDetectorBuilder.a().a(new String[] { "?vvv?", ">???<", ">???<", ">???<", "?^^^?"}).a('?', ShapeDetectorBlock.a(BlockStatePredicate.a)).a('^', ShapeDetectorBlock.a(BlockStatePredicate.a(Blocks.END_PORTAL_FRAME).a(BlockEnderPortalFrame.EYE, Predicates.equalTo(Boolean.valueOf(true))).a(BlockEnderPortalFrame.FACING, Predicates.equalTo(EnumDirection.SOUTH)))).a('>', ShapeDetectorBlock.a(BlockStatePredicate.a(Blocks.END_PORTAL_FRAME).a(BlockEnderPortalFrame.EYE, Predicates.equalTo(Boolean.valueOf(true))).a(BlockEnderPortalFrame.FACING, Predicates.equalTo(EnumDirection.WEST)))).a('v', ShapeDetectorBlock.a(BlockStatePredicate.a(Blocks.END_PORTAL_FRAME).a(BlockEnderPortalFrame.EYE, Predicates.equalTo(Boolean.valueOf(true))).a(BlockEnderPortalFrame.FACING, Predicates.equalTo(EnumDirection.NORTH)))).a('<', ShapeDetectorBlock.a(BlockStatePredicate.a(Blocks.END_PORTAL_FRAME).a(BlockEnderPortalFrame.EYE, Predicates.equalTo(Boolean.valueOf(true))).a(BlockEnderPortalFrame.FACING, Predicates.equalTo(EnumDirection.EAST)))).b();
        }

        return BlockEnderPortalFrame.r;
    }

    public EnumBlockFaceShape a(IBlockAccess iblockaccess, IBlockData iblockdata, BlockPosition blockposition, EnumDirection enumdirection) {
        return enumdirection == EnumDirection.DOWN ? EnumBlockFaceShape.SOLID : EnumBlockFaceShape.UNDEFINED;
    }

    public boolean a(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition, PathMode pathmode) {
        return false;
    }
}

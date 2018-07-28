package net.minecraft.server;

import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;

// CraftBukkit start
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.entity.EntityInteractEvent;
// CraftBukkit end

public abstract class BlockButtonAbstract extends BlockAttachable {

    public static final BlockStateBoolean POWERED = BlockProperties.t;
    protected static final VoxelShape b = Block.a(6.0D, 14.0D, 5.0D, 10.0D, 16.0D, 11.0D);
    protected static final VoxelShape c = Block.a(5.0D, 14.0D, 6.0D, 11.0D, 16.0D, 10.0D);
    protected static final VoxelShape p = Block.a(6.0D, 0.0D, 5.0D, 10.0D, 2.0D, 11.0D);
    protected static final VoxelShape q = Block.a(5.0D, 0.0D, 6.0D, 11.0D, 2.0D, 10.0D);
    protected static final VoxelShape r = Block.a(5.0D, 6.0D, 14.0D, 11.0D, 10.0D, 16.0D);
    protected static final VoxelShape s = Block.a(5.0D, 6.0D, 0.0D, 11.0D, 10.0D, 2.0D);
    protected static final VoxelShape t = Block.a(14.0D, 6.0D, 5.0D, 16.0D, 10.0D, 11.0D);
    protected static final VoxelShape u = Block.a(0.0D, 6.0D, 5.0D, 2.0D, 10.0D, 11.0D);
    protected static final VoxelShape v = Block.a(6.0D, 15.0D, 5.0D, 10.0D, 16.0D, 11.0D);
    protected static final VoxelShape w = Block.a(5.0D, 15.0D, 6.0D, 11.0D, 16.0D, 10.0D);
    protected static final VoxelShape x = Block.a(6.0D, 0.0D, 5.0D, 10.0D, 1.0D, 11.0D);
    protected static final VoxelShape y = Block.a(5.0D, 0.0D, 6.0D, 11.0D, 1.0D, 10.0D);
    protected static final VoxelShape z = Block.a(5.0D, 6.0D, 15.0D, 11.0D, 10.0D, 16.0D);
    protected static final VoxelShape A = Block.a(5.0D, 6.0D, 0.0D, 11.0D, 10.0D, 1.0D);
    protected static final VoxelShape B = Block.a(15.0D, 6.0D, 5.0D, 16.0D, 10.0D, 11.0D);
    protected static final VoxelShape C = Block.a(0.0D, 6.0D, 5.0D, 1.0D, 10.0D, 11.0D);
    private final boolean F;

    protected BlockButtonAbstract(boolean flag, Block.Info block_info) {
        super(block_info);
        this.v((IBlockData) ((IBlockData) ((IBlockData) ((IBlockData) this.blockStateList.getBlockData()).set(BlockButtonAbstract.FACING, EnumDirection.NORTH)).set(BlockButtonAbstract.POWERED, Boolean.valueOf(false))).set(BlockButtonAbstract.FACE, BlockPropertyAttachPosition.WALL));
        this.F = flag;
    }

    public int a(IWorldReader iworldreader) {
        return this.F ? 30 : 20;
    }

    public boolean a(IBlockData iblockdata) {
        return false;
    }

    public VoxelShape a(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition) {
        EnumDirection enumdirection = (EnumDirection) iblockdata.get(BlockButtonAbstract.FACING);
        boolean flag = ((Boolean) iblockdata.get(BlockButtonAbstract.POWERED)).booleanValue();

        switch ((BlockPropertyAttachPosition) iblockdata.get(BlockButtonAbstract.FACE)) {
        case FLOOR:
            if (enumdirection.k() == EnumDirection.EnumAxis.X) {
                return flag ? BlockButtonAbstract.x : BlockButtonAbstract.p;
            }

            return flag ? BlockButtonAbstract.y : BlockButtonAbstract.q;

        case WALL:
            switch (enumdirection) {
            case EAST:
                return flag ? BlockButtonAbstract.C : BlockButtonAbstract.u;

            case WEST:
                return flag ? BlockButtonAbstract.B : BlockButtonAbstract.t;

            case SOUTH:
                return flag ? BlockButtonAbstract.A : BlockButtonAbstract.s;

            case NORTH:
            default:
                return flag ? BlockButtonAbstract.z : BlockButtonAbstract.r;
            }

        case CEILING:
        default:
            return enumdirection.k() == EnumDirection.EnumAxis.X ? (flag ? BlockButtonAbstract.v : BlockButtonAbstract.b) : (flag ? BlockButtonAbstract.w : BlockButtonAbstract.c);
        }
    }

    public boolean interact(IBlockData iblockdata, World world, BlockPosition blockposition, EntityHuman entityhuman, EnumHand enumhand, EnumDirection enumdirection, float f, float f1, float f2) {
        if (((Boolean) iblockdata.get(BlockButtonAbstract.POWERED)).booleanValue()) {
            return true;
        } else {
            // CraftBukkit start
            boolean powered = ((Boolean) iblockdata.get(POWERED));
            org.bukkit.block.Block block = world.getWorld().getBlockAt(blockposition.getX(), blockposition.getY(), blockposition.getZ());
            int old = (powered) ? 15 : 0;
            int current = (!powered) ? 15 : 0;

            BlockRedstoneEvent eventRedstone = new BlockRedstoneEvent(block, old, current);
            world.getServer().getPluginManager().callEvent(eventRedstone);

            if ((eventRedstone.getNewCurrent() > 0) != (!powered)) {
                return true;
            }
            // CraftBukkit end
            world.setTypeAndData(blockposition, (IBlockData) iblockdata.set(BlockButtonAbstract.POWERED, Boolean.valueOf(true)), 3);
            this.a(entityhuman, world, blockposition, true);
            this.c(iblockdata, world, blockposition);
            world.I().a(blockposition, this, this.a((IWorldReader) world));
            return true;
        }
    }

    protected void a(@Nullable EntityHuman entityhuman, GeneratorAccess generatoraccess, BlockPosition blockposition, boolean flag) {
        generatoraccess.a(flag ? entityhuman : null, blockposition, this.a(flag), SoundCategory.BLOCKS, 0.3F, flag ? 0.6F : 0.5F);
    }

    protected abstract SoundEffect a(boolean flag);

    public void remove(IBlockData iblockdata, World world, BlockPosition blockposition, IBlockData iblockdata1, boolean flag) {
        if (!flag && iblockdata.getBlock() != iblockdata1.getBlock()) {
            if (((Boolean) iblockdata.get(BlockButtonAbstract.POWERED)).booleanValue()) {
                this.c(iblockdata, world, blockposition);
            }

            super.remove(iblockdata, world, blockposition, iblockdata1, flag);
        }
    }

    public int a(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition, EnumDirection enumdirection) {
        return ((Boolean) iblockdata.get(BlockButtonAbstract.POWERED)).booleanValue() ? 15 : 0;
    }

    public int b(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition, EnumDirection enumdirection) {
        return ((Boolean) iblockdata.get(BlockButtonAbstract.POWERED)).booleanValue() && k(iblockdata) == enumdirection ? 15 : 0;
    }

    public boolean isPowerSource(IBlockData iblockdata) {
        return true;
    }

    public void a(IBlockData iblockdata, World world, BlockPosition blockposition, Random random) {
        if (!world.isClientSide && ((Boolean) iblockdata.get(BlockButtonAbstract.POWERED)).booleanValue()) {
            if (this.F) {
                this.b(iblockdata, world, blockposition);
            } else {
                // CraftBukkit start
                org.bukkit.block.Block block = world.getWorld().getBlockAt(blockposition.getX(), blockposition.getY(), blockposition.getZ());

                BlockRedstoneEvent eventRedstone = new BlockRedstoneEvent(block, 15, 0);
                world.getServer().getPluginManager().callEvent(eventRedstone);

                if (eventRedstone.getNewCurrent() > 0) {
                    return;
                }
                // CraftBukkit end
                world.setTypeAndData(blockposition, (IBlockData) iblockdata.set(BlockButtonAbstract.POWERED, Boolean.valueOf(false)), 3);
                this.c(iblockdata, world, blockposition);
                this.a((EntityHuman) null, world, blockposition, false);
            }

        }
    }

    public void a(IBlockData iblockdata, World world, BlockPosition blockposition, Entity entity) {
        if (!world.isClientSide && this.F && !((Boolean) iblockdata.get(BlockButtonAbstract.POWERED)).booleanValue()) {
            this.b(iblockdata, world, blockposition);
        }
    }

    private void b(IBlockData iblockdata, World world, BlockPosition blockposition) {
        List list = world.a(EntityArrow.class, iblockdata.g(world, blockposition).a().a(blockposition));
        boolean flag = !list.isEmpty();
        boolean flag1 = ((Boolean) iblockdata.get(BlockButtonAbstract.POWERED)).booleanValue();

        // CraftBukkit start - Call interact event when arrows turn on wooden buttons
        if (flag1 != flag && flag) {
            org.bukkit.block.Block block = world.getWorld().getBlockAt(blockposition.getX(), blockposition.getY(), blockposition.getZ());
            boolean allowed = false;

            // If all of the events are cancelled block the button press, else allow
            for (Object object : list) {
                if (object != null) {
                    EntityInteractEvent event = new EntityInteractEvent(((Entity) object).getBukkitEntity(), block);
                    world.getServer().getPluginManager().callEvent(event);

                    if (!event.isCancelled()) {
                        allowed = true;
                        break;
                    }
                }
            }

            if (!allowed) {
                return;
            }
        }
        // CraftBukkit end

        if (flag != flag1) {
            // CraftBukkit start
            boolean powered = flag1;
            org.bukkit.block.Block block = world.getWorld().getBlockAt(blockposition.getX(), blockposition.getY(), blockposition.getZ());
            int old = (powered) ? 15 : 0;
            int current = (!powered) ? 15 : 0;

            BlockRedstoneEvent eventRedstone = new BlockRedstoneEvent(block, old, current);
            world.getServer().getPluginManager().callEvent(eventRedstone);

            if ((flag && eventRedstone.getNewCurrent() <= 0) || (!flag && eventRedstone.getNewCurrent() > 0)) {
                return;
            }
            // CraftBukkit end
            world.setTypeAndData(blockposition, (IBlockData) iblockdata.set(BlockButtonAbstract.POWERED, Boolean.valueOf(flag)), 3);
            this.c(iblockdata, world, blockposition);
            this.a((EntityHuman) null, world, blockposition, flag);
        }

        if (flag) {
            world.I().a(new BlockPosition(blockposition), this, this.a((IWorldReader) world));
        }

    }

    private void c(IBlockData iblockdata, World world, BlockPosition blockposition) {
        world.applyPhysics(blockposition, this);
        world.applyPhysics(blockposition.shift(k(iblockdata).opposite()), this);
    }

    protected void a(BlockStateList.a<Block, IBlockData> blockstatelist_a) {
        blockstatelist_a.a(new IBlockState[] { BlockButtonAbstract.FACING, BlockButtonAbstract.POWERED, BlockButtonAbstract.FACE});
    }

    public EnumBlockFaceShape a(IBlockAccess iblockaccess, IBlockData iblockdata, BlockPosition blockposition, EnumDirection enumdirection) {
        return EnumBlockFaceShape.UNDEFINED;
    }
}

package net.minecraft.server;

import com.google.common.collect.Maps;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
// CraftBukkit start
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.event.block.BlockFromToEvent;
// CraftBukkit end

public abstract class FluidTypeFlowing extends FluidType {

    public static final BlockStateBoolean FALLING = BlockProperties.h;
    public static final BlockStateInteger LEVEL = BlockProperties.af;

    public FluidTypeFlowing() {}

    protected void a(BlockStateList.a<FluidType, Fluid> blockstatelist_a) {
        blockstatelist_a.a(new IBlockState[] { FluidTypeFlowing.FALLING});
    }

    public Vec3D a(IWorldReader iworldreader, BlockPosition blockposition, Fluid fluid) {
        double d0 = 0.0D;
        double d1 = 0.0D;
        BlockPosition.b blockposition_b = BlockPosition.b.r();
        Throwable throwable = null;

        Vec3D vec3d;

        try {
            Iterator iterator = EnumDirection.EnumDirectionLimit.HORIZONTAL.iterator();

            while (iterator.hasNext()) {
                EnumDirection enumdirection = (EnumDirection) iterator.next();

                blockposition_b.j(blockposition).d(enumdirection);
                Fluid fluid1 = iworldreader.b(blockposition_b);

                if (this.g(fluid1)) {
                    float f = fluid1.f();
                    float f1 = 0.0F;

                    if (f == 0.0F) {
                        if (!iworldreader.getType(blockposition_b).getMaterial().isSolid()) {
                            Fluid fluid2 = iworldreader.b(blockposition_b.down());

                            if (this.g(fluid2)) {
                                f = fluid2.f();
                                if (f > 0.0F) {
                                    f1 = fluid.f() - (f - 0.8888889F);
                                }
                            }
                        }
                    } else if (f > 0.0F) {
                        f1 = fluid.f() - f;
                    }

                    if (f1 != 0.0F) {
                        d0 += (double) ((float) enumdirection.getAdjacentX() * f1);
                        d1 += (double) ((float) enumdirection.getAdjacentZ() * f1);
                    }
                }
            }

            Vec3D vec3d1 = new Vec3D(d0, 0.0D, d1);

            if (((Boolean) fluid.get(FluidTypeFlowing.FALLING)).booleanValue()) {
                Iterator iterator1 = EnumDirection.EnumDirectionLimit.HORIZONTAL.iterator();

                while (iterator1.hasNext()) {
                    EnumDirection enumdirection1 = (EnumDirection) iterator1.next();

                    blockposition_b.j(blockposition).d(enumdirection1);
                    if (this.a((IBlockAccess) iworldreader, (BlockPosition) blockposition_b, enumdirection1) || this.a((IBlockAccess) iworldreader, blockposition_b.up(), enumdirection1)) {
                        vec3d1 = vec3d1.a().add(0.0D, -6.0D, 0.0D);
                        break;
                    }
                }
            }

            vec3d = vec3d1.a();
        } catch (Throwable throwable1) {
            throwable = throwable1;
            throw throwable1;
        } finally {
            if (blockposition_b != null) {
                if (throwable != null) {
                    try {
                        blockposition_b.close();
                    } catch (Throwable throwable2) {
                        throwable.addSuppressed(throwable2);
                    }
                } else {
                    blockposition_b.close();
                }
            }

        }

        return vec3d;
    }

    private boolean g(Fluid fluid) {
        return fluid.e() || fluid.c().a((FluidType) this);
    }

    protected boolean a(IBlockAccess iblockaccess, BlockPosition blockposition, EnumDirection enumdirection) {
        IBlockData iblockdata = iblockaccess.getType(blockposition);
        Block block = iblockdata.getBlock();
        Fluid fluid = iblockaccess.b(blockposition);

        if (fluid.c().a((FluidType) this)) {
            return false;
        } else if (enumdirection == EnumDirection.UP) {
            return true;
        } else if (iblockdata.getMaterial() == Material.ICE) {
            return false;
        } else {
            boolean flag = Block.b(block) || block instanceof BlockStairs;

            return !flag && iblockdata.c(iblockaccess, blockposition, enumdirection) == EnumBlockFaceShape.SOLID;
        }
    }

    protected void a(GeneratorAccess generatoraccess, BlockPosition blockposition, Fluid fluid) {
        if (!fluid.e()) {
            IBlockData iblockdata = generatoraccess.getType(blockposition);
            BlockPosition blockposition1 = blockposition.down();
            IBlockData iblockdata1 = generatoraccess.getType(blockposition1);
            Fluid fluid1 = this.a((IWorldReader) generatoraccess, blockposition1, iblockdata1);

            if (this.a(generatoraccess, blockposition, iblockdata, EnumDirection.DOWN, blockposition1, iblockdata1, generatoraccess.b(blockposition1), fluid1.c())) {
                // CraftBukkit start
                org.bukkit.block.Block source = CraftBlock.at(generatoraccess, blockposition);
                BlockFromToEvent event = new BlockFromToEvent(source, BlockFace.DOWN);
                generatoraccess.getMinecraftWorld().getServer().getPluginManager().callEvent(event);

                if (event.isCancelled()) {
                    return;
                }
                // CraftBukkit end
                this.a(generatoraccess, blockposition1, iblockdata1, EnumDirection.DOWN, fluid1);
                if (this.a((IWorldReader) generatoraccess, blockposition) >= 3) {
                    this.a(generatoraccess, blockposition, fluid, iblockdata);
                }
            } else if (fluid.d() || !this.a(generatoraccess, fluid1.c(), blockposition, iblockdata, blockposition1, iblockdata1)) {
                this.a(generatoraccess, blockposition, fluid, iblockdata);
            }

        }
    }

    private void a(GeneratorAccess generatoraccess, BlockPosition blockposition, Fluid fluid, IBlockData iblockdata) {
        int i = fluid.g() - this.c((IWorldReader) generatoraccess);

        if (((Boolean) fluid.get(FluidTypeFlowing.FALLING)).booleanValue()) {
            i = 7;
        }

        if (i > 0) {
            Map map = this.b(generatoraccess, blockposition, iblockdata);
            Iterator iterator = map.entrySet().iterator();

            while (iterator.hasNext()) {
                Entry entry = (Entry) iterator.next();
                EnumDirection enumdirection = (EnumDirection) entry.getKey();
                Fluid fluid1 = (Fluid) entry.getValue();
                BlockPosition blockposition1 = blockposition.shift(enumdirection);
                IBlockData iblockdata1 = generatoraccess.getType(blockposition1);

                if (this.a(generatoraccess, blockposition, iblockdata, enumdirection, blockposition1, iblockdata1, generatoraccess.b(blockposition1), fluid1.c())) {
                    // CraftBukkit start
                    org.bukkit.block.Block source = CraftBlock.at(generatoraccess, blockposition);
                    BlockFromToEvent event = new BlockFromToEvent(source, org.bukkit.craftbukkit.block.CraftBlock.notchToBlockFace(enumdirection));
                    generatoraccess.getMinecraftWorld().getServer().getPluginManager().callEvent(event);

                    if (event.isCancelled()) {
                        continue;
                    }
                    // CraftBukkit end
                    this.a(generatoraccess, blockposition1, iblockdata1, enumdirection, fluid1);
                }
            }

        }
    }

    protected Fluid a(IWorldReader iworldreader, BlockPosition blockposition, IBlockData iblockdata) {
        int i = 0;
        int j = 0;
        Iterator iterator = EnumDirection.EnumDirectionLimit.HORIZONTAL.iterator();

        while (iterator.hasNext()) {
            EnumDirection enumdirection = (EnumDirection) iterator.next();
            BlockPosition blockposition1 = blockposition.shift(enumdirection);
            IBlockData iblockdata1 = iworldreader.getType(blockposition1);
            Fluid fluid = iworldreader.b(blockposition1);

            if (fluid.c().a((FluidType) this) && this.a(enumdirection, iblockdata.h(iworldreader, blockposition), iblockdata1.h(iworldreader, blockposition1))) {
                if (fluid.d()) {
                    ++j;
                }

                i = Math.max(i, fluid.g());
            }
        }

        if (this.g() && j >= 2) {
            IBlockData iblockdata2 = iworldreader.getType(blockposition.down());
            Fluid fluid1 = iworldreader.b(blockposition.down());

            if (iblockdata2.getMaterial().isBuildable() || this.h(fluid1)) {
                return this.a(false);
            }
        }

        BlockPosition blockposition2 = blockposition.up();
        IBlockData iblockdata3 = iworldreader.getType(blockposition2);
        Fluid fluid2 = iblockdata3.s();

        if (!fluid2.e() && fluid2.c().a((FluidType) this) && this.a(EnumDirection.UP, iblockdata.h(iworldreader, blockposition), iblockdata3.h(iworldreader, blockposition2))) {
            return this.a(8, true);
        } else {
            int k = i - this.c(iworldreader);

            return k <= 0 ? FluidTypes.a.i() : this.a(k, fluid2.c().a((FluidType) this));
        }
    }

    private boolean a(EnumDirection enumdirection, VoxelShape voxelshape, VoxelShape voxelshape1) {
        return !VoxelShapes.b(voxelshape, voxelshape1, enumdirection);
    }

    public abstract FluidType e();

    public Fluid a(int i, boolean flag) {
        return (Fluid) ((Fluid) this.e().i().set(FluidTypeFlowing.LEVEL, Integer.valueOf(i))).set(FluidTypeFlowing.FALLING, Boolean.valueOf(flag));
    }

    public abstract FluidType f();

    public Fluid a(boolean flag) {
        return (Fluid) this.f().i().set(FluidTypeFlowing.FALLING, Boolean.valueOf(flag));
    }

    protected abstract boolean g();

    protected void a(GeneratorAccess generatoraccess, BlockPosition blockposition, IBlockData iblockdata, EnumDirection enumdirection, Fluid fluid) {
        if (iblockdata.getBlock() instanceof IFluidContainer) {
            ((IFluidContainer) iblockdata.getBlock()).a(generatoraccess, blockposition, iblockdata, fluid);
        } else {
            if (!iblockdata.isAir()) {
                this.a(generatoraccess, blockposition, iblockdata);
            }

            generatoraccess.setTypeAndData(blockposition, fluid.i(), 3);
        }

    }

    protected abstract void a(GeneratorAccess generatoraccess, BlockPosition blockposition, IBlockData iblockdata);

    protected int a(IWorldReader iworldreader, BlockPosition blockposition, int i, EnumDirection enumdirection, IBlockData iblockdata) {
        int j = 1000;
        Iterator iterator = EnumDirection.EnumDirectionLimit.HORIZONTAL.iterator();

        while (iterator.hasNext()) {
            EnumDirection enumdirection1 = (EnumDirection) iterator.next();

            if (enumdirection1 != enumdirection) {
                BlockPosition blockposition1 = blockposition.shift(enumdirection1);
                IBlockData iblockdata1 = iworldreader.getType(blockposition1);
                Fluid fluid = iworldreader.b(blockposition1);

                if (this.a(iworldreader, this.e(), blockposition, iblockdata, enumdirection1, blockposition1, iblockdata1, fluid)) {
                    BlockPosition blockposition2 = blockposition1.down();

                    if (this.a(iworldreader, this.e(), blockposition1, iblockdata1, blockposition2, iworldreader.getType(blockposition2))) {
                        return i;
                    }

                    if (i < this.b(iworldreader)) {
                        int k = this.a(iworldreader, blockposition1, i + 1, enumdirection1.opposite(), iblockdata1);

                        if (k < j) {
                            j = k;
                        }
                    }
                }
            }
        }

        return j;
    }

    private boolean a(IBlockAccess iblockaccess, FluidType fluidtype, BlockPosition blockposition, IBlockData iblockdata, BlockPosition blockposition1, IBlockData iblockdata1) {
        return !this.a(EnumDirection.DOWN, iblockdata.h(iblockaccess, blockposition), iblockdata1.h(iblockaccess, blockposition1)) ? false : (iblockdata1.s().c().a((FluidType) this) ? true : this.a(iblockaccess, blockposition1, iblockdata1, fluidtype));
    }

    private boolean a(IBlockAccess iblockaccess, FluidType fluidtype, BlockPosition blockposition, IBlockData iblockdata, EnumDirection enumdirection, BlockPosition blockposition1, IBlockData iblockdata1, Fluid fluid) {
        return !this.h(fluid) && this.a(enumdirection, iblockdata.h(iblockaccess, blockposition), iblockdata1.h(iblockaccess, blockposition1)) && this.a(iblockaccess, blockposition1, iblockdata1, fluidtype);
    }

    private boolean h(Fluid fluid) {
        return fluid.c().a((FluidType) this) && fluid.d();
    }

    protected abstract int b(IWorldReader iworldreader);

    private int a(IWorldReader iworldreader, BlockPosition blockposition) {
        int i = 0;
        Iterator iterator = EnumDirection.EnumDirectionLimit.HORIZONTAL.iterator();

        while (iterator.hasNext()) {
            EnumDirection enumdirection = (EnumDirection) iterator.next();
            BlockPosition blockposition1 = blockposition.shift(enumdirection);
            Fluid fluid = iworldreader.b(blockposition1);

            if (this.h(fluid)) {
                ++i;
            }
        }

        return i;
    }

    protected Map<EnumDirection, Fluid> b(IWorldReader iworldreader, BlockPosition blockposition, IBlockData iblockdata) {
        int i = 1000;
        EnumMap enummap = Maps.newEnumMap(EnumDirection.class);
        Iterator iterator = EnumDirection.EnumDirectionLimit.HORIZONTAL.iterator();

        while (iterator.hasNext()) {
            EnumDirection enumdirection = (EnumDirection) iterator.next();
            BlockPosition blockposition1 = blockposition.shift(enumdirection);
            IBlockData iblockdata1 = iworldreader.getType(blockposition1);
            Fluid fluid = iworldreader.b(blockposition1);
            Fluid fluid1 = this.a(iworldreader, blockposition1, iblockdata1);

            if (this.a(iworldreader, fluid1.c(), blockposition, iblockdata, enumdirection, blockposition1, iblockdata1, fluid)) {
                BlockPosition blockposition2 = blockposition1.down();
                int j;

                if (this.a(iworldreader, this.e(), blockposition1, iblockdata1, blockposition2, iworldreader.getType(blockposition2))) {
                    j = 0;
                } else {
                    j = this.a(iworldreader, blockposition1, 1, enumdirection.opposite(), iblockdata1);
                }

                if (j < i) {
                    enummap.clear();
                }

                if (j <= i) {
                    enummap.put(enumdirection, fluid1);
                    i = j;
                }
            }
        }

        return enummap;
    }

    private boolean a(IBlockAccess iblockaccess, BlockPosition blockposition, IBlockData iblockdata, FluidType fluidtype) {
        Block block = iblockdata.getBlock();

        if (block instanceof IFluidContainer) {
            return ((IFluidContainer) block).a(iblockaccess, blockposition, iblockdata, fluidtype);
        } else if (!(block instanceof BlockDoor) && block != Blocks.SIGN && block != Blocks.LADDER && block != Blocks.SUGAR_CANE && block != Blocks.BUBBLE_COLUMN) {
            Material material = iblockdata.getMaterial();

            return material != Material.PORTAL && material != Material.STRUCTURE_VOID && material != Material.WATER_PLANT && material != Material.REPLACEABLE_WATER_PLANT ? !material.isSolid() : false;
        } else {
            return false;
        }
    }

    protected boolean a(IBlockAccess iblockaccess, BlockPosition blockposition, IBlockData iblockdata, EnumDirection enumdirection, BlockPosition blockposition1, IBlockData iblockdata1, Fluid fluid, FluidType fluidtype) {
        return fluid.a(fluidtype, enumdirection) && this.a(enumdirection, iblockdata.h(iblockaccess, blockposition), iblockdata1.h(iblockaccess, blockposition1)) && this.a(iblockaccess, blockposition1, iblockdata1, fluidtype);
    }

    protected abstract int c(IWorldReader iworldreader);

    protected int a(World world, Fluid fluid, Fluid fluid1) {
        return this.a((IWorldReader) world);
    }

    public void a(World world, BlockPosition blockposition, Fluid fluid) {
        if (!fluid.d()) {
            Fluid fluid1 = this.a((IWorldReader) world, blockposition, world.getType(blockposition));
            int i = this.a(world, fluid, fluid1);

            if (fluid1.e()) {
                fluid = fluid1;
                world.setTypeAndData(blockposition, Blocks.AIR.getBlockData(), 3);
            } else if (!fluid1.equals(fluid)) {
                fluid = fluid1;
                IBlockData iblockdata = fluid1.i();

                world.setTypeAndData(blockposition, iblockdata, 2);
                world.H().a(blockposition, fluid1.c(), i);
                world.applyPhysics(blockposition, iblockdata.getBlock());
            }
        }

        this.a((GeneratorAccess) world, blockposition, fluid);
    }

    protected static int e(Fluid fluid) {
        return fluid.d() ? 0 : 8 - Math.min(fluid.g(), 8) + (((Boolean) fluid.get(FluidTypeFlowing.FALLING)).booleanValue() ? 8 : 0);
    }

    public float a(Fluid fluid) {
        return (float) fluid.g() / 9.0F;
    }
}

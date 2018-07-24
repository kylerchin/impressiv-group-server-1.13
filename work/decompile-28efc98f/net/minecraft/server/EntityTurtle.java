package net.minecraft.server;

import com.google.common.collect.Sets;
import java.util.Random;
import java.util.Set;
import java.util.function.Predicate;
import javax.annotation.Nullable;

public class EntityTurtle extends EntityAnimal {

    private static final DataWatcherObject<BlockPosition> bD = DataWatcher.a(EntityTurtle.class, DataWatcherRegistry.l);
    private static final DataWatcherObject<Boolean> bE = DataWatcher.a(EntityTurtle.class, DataWatcherRegistry.i);
    private static final DataWatcherObject<Boolean> bG = DataWatcher.a(EntityTurtle.class, DataWatcherRegistry.i);
    private static final DataWatcherObject<BlockPosition> bH = DataWatcher.a(EntityTurtle.class, DataWatcherRegistry.l);
    private static final DataWatcherObject<Boolean> bI = DataWatcher.a(EntityTurtle.class, DataWatcherRegistry.i);
    private static final DataWatcherObject<Boolean> bJ = DataWatcher.a(EntityTurtle.class, DataWatcherRegistry.i);
    private int bK;
    public static final Predicate<Entity> bC = (entity) -> {
        return !(entity instanceof EntityLiving) ? false : ((EntityLiving) entity).isBaby() && !entity.isInWater();
    };

    public EntityTurtle(World world) {
        super(EntityTypes.TURTLE, world);
        this.setSize(1.2F, 0.4F);
        this.moveController = new EntityTurtle.e(this);
        this.bF = Blocks.SAND;
        this.Q = 1.0F;
    }

    public void g(BlockPosition blockposition) {
        this.datawatcher.set(EntityTurtle.bD, blockposition);
    }

    private BlockPosition dC() {
        return (BlockPosition) this.datawatcher.get(EntityTurtle.bD);
    }

    private void h(BlockPosition blockposition) {
        this.datawatcher.set(EntityTurtle.bH, blockposition);
    }

    private BlockPosition dD() {
        return (BlockPosition) this.datawatcher.get(EntityTurtle.bH);
    }

    public boolean dz() {
        return ((Boolean) this.datawatcher.get(EntityTurtle.bE)).booleanValue();
    }

    private void s(boolean flag) {
        this.datawatcher.set(EntityTurtle.bE, Boolean.valueOf(flag));
    }

    public boolean dA() {
        return ((Boolean) this.datawatcher.get(EntityTurtle.bG)).booleanValue();
    }

    private void t(boolean flag) {
        this.bK = flag ? 1 : 0;
        this.datawatcher.set(EntityTurtle.bG, Boolean.valueOf(flag));
    }

    private boolean dI() {
        return ((Boolean) this.datawatcher.get(EntityTurtle.bI)).booleanValue();
    }

    private void u(boolean flag) {
        this.datawatcher.set(EntityTurtle.bI, Boolean.valueOf(flag));
    }

    private boolean dJ() {
        return ((Boolean) this.datawatcher.get(EntityTurtle.bJ)).booleanValue();
    }

    private void v(boolean flag) {
        this.datawatcher.set(EntityTurtle.bJ, Boolean.valueOf(flag));
    }

    protected void x_() {
        super.x_();
        this.datawatcher.register(EntityTurtle.bD, BlockPosition.ZERO);
        this.datawatcher.register(EntityTurtle.bE, Boolean.valueOf(false));
        this.datawatcher.register(EntityTurtle.bH, BlockPosition.ZERO);
        this.datawatcher.register(EntityTurtle.bI, Boolean.valueOf(false));
        this.datawatcher.register(EntityTurtle.bJ, Boolean.valueOf(false));
        this.datawatcher.register(EntityTurtle.bG, Boolean.valueOf(false));
    }

    public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
        nbttagcompound.setInt("HomePosX", this.dC().getX());
        nbttagcompound.setInt("HomePosY", this.dC().getY());
        nbttagcompound.setInt("HomePosZ", this.dC().getZ());
        nbttagcompound.setBoolean("HasEgg", this.dz());
        nbttagcompound.setInt("TravelPosX", this.dD().getX());
        nbttagcompound.setInt("TravelPosY", this.dD().getY());
        nbttagcompound.setInt("TravelPosZ", this.dD().getZ());
    }

    public void a(NBTTagCompound nbttagcompound) {
        int i = nbttagcompound.getInt("HomePosX");
        int j = nbttagcompound.getInt("HomePosY");
        int k = nbttagcompound.getInt("HomePosZ");

        this.g(new BlockPosition(i, j, k));
        super.a(nbttagcompound);
        this.s(nbttagcompound.getBoolean("HasEgg"));
        int l = nbttagcompound.getInt("TravelPosX");
        int i1 = nbttagcompound.getInt("TravelPosY");
        int j1 = nbttagcompound.getInt("TravelPosZ");

        this.h(new BlockPosition(l, i1, j1));
    }

    @Nullable
    public GroupDataEntity prepare(DifficultyDamageScaler difficultydamagescaler, @Nullable GroupDataEntity groupdataentity, @Nullable NBTTagCompound nbttagcompound) {
        this.g(new BlockPosition(this.locX, this.locY, this.locZ));
        this.h(BlockPosition.ZERO);
        return super.prepare(difficultydamagescaler, groupdataentity, nbttagcompound);
    }

    public boolean a(GeneratorAccess generatoraccess) {
        BlockPosition blockposition = new BlockPosition(this.locX, this.getBoundingBox().b, this.locZ);

        return blockposition.getY() < generatoraccess.getSeaLevel() + 4 && super.a(generatoraccess);
    }

    protected void n() {
        this.goalSelector.a(0, new EntityTurtle.f(this, 1.2D));
        this.goalSelector.a(1, new EntityTurtle.a(this, 1.0D));
        this.goalSelector.a(1, new EntityTurtle.d(this, 1.0D));
        this.goalSelector.a(2, new EntityTurtle.i(this, 1.1D, Blocks.SEAGRASS.getItem()));
        this.goalSelector.a(3, new EntityTurtle.c(this, 1.0D, null));
        this.goalSelector.a(4, new EntityTurtle.b(this, 1.0D));
        this.goalSelector.a(7, new EntityTurtle.j(this, 1.0D));
        this.goalSelector.a(8, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
        this.goalSelector.a(9, new EntityTurtle.h(this, 1.0D, 100, null));
    }

    protected void initAttributes() {
        super.initAttributes();
        this.getAttributeInstance(GenericAttributes.maxHealth).setValue(30.0D);
        this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.25D);
    }

    public boolean bw() {
        return false;
    }

    public boolean ca() {
        return true;
    }

    public EnumMonsterType getMonsterType() {
        return EnumMonsterType.e;
    }

    public int z() {
        return 200;
    }

    @Nullable
    protected SoundEffect D() {
        return !this.isInWater() && this.onGround && !this.isBaby() ? SoundEffects.ENTITY_TURTLE_AMBIENT_LAND : super.D();
    }

    protected void d(float f) {
        super.d(f * 1.5F);
    }

    protected SoundEffect ad() {
        return SoundEffects.ENTITY_TURTLE_SWIM;
    }

    @Nullable
    protected SoundEffect d(DamageSource damagesource) {
        return this.isBaby() ? SoundEffects.ENTITY_TURTLE_HURT_BABY : SoundEffects.ENTITY_TURTLE_HURT;
    }

    @Nullable
    protected SoundEffect cs() {
        return this.isBaby() ? SoundEffects.ENTITY_TURTLE_DEATH_BABY : SoundEffects.ENTITY_TURTLE_DEATH;
    }

    protected void a(BlockPosition blockposition, IBlockData iblockdata) {
        SoundEffect soundeffect = this.isBaby() ? SoundEffects.ENTITY_TURTLE_SHAMBLE_BABY : SoundEffects.ENTITY_TURTLE_SHAMBLE;

        this.a(soundeffect, 0.15F, 1.0F);
    }

    public boolean dE() {
        return super.dE() && !this.dz();
    }

    protected float ab() {
        return this.L + 0.15F;
    }

    public void a(boolean flag) {
        this.a(flag ? 0.3F : 1.0F);
    }

    protected NavigationAbstract b(World world) {
        return new EntityTurtle.g(this, world);
    }

    @Nullable
    public EntityAgeable createChild(EntityAgeable entityageable) {
        return new EntityTurtle(this.world);
    }

    public boolean f(ItemStack itemstack) {
        return itemstack.getItem() == Blocks.SEAGRASS.getItem();
    }

    public float a(BlockPosition blockposition, IWorldReader iworldreader) {
        return !this.dI() && iworldreader.b(blockposition).a(TagsFluid.a) ? 10.0F : super.a(blockposition, iworldreader);
    }

    public void k() {
        super.k();
        if (this.dA() && this.bK >= 1 && this.bK % 5 == 0) {
            BlockPosition blockposition = new BlockPosition(this);

            if (this.world.getType(blockposition.down()).getBlock() == Blocks.SAND) {
                this.world.triggerEffect(2001, blockposition, Block.getCombinedId(Blocks.SAND.getBlockData()));
            }
        }

    }

    protected void l() {
        super.l();
        this.a((IMaterial) Items.SCUTE, 1);
    }

    public void a(float f, float f1, float f2) {
        if (this.cP() && this.isInWater()) {
            this.a(f, f1, f2, 0.1F);
            this.move(EnumMoveType.SELF, this.motX, this.motY, this.motZ);
            this.motX *= 0.8999999761581421D;
            this.motY *= 0.8999999761581421D;
            this.motZ *= 0.8999999761581421D;
            if (this.getGoalTarget() == null && (!this.dI() || this.c(this.dC()) >= 400.0D)) {
                this.motY -= 0.005D;
            }
        } else {
            super.a(f, f1, f2);
        }

    }

    public boolean a(EntityHuman entityhuman) {
        return false;
    }

    @Nullable
    protected MinecraftKey G() {
        return LootTables.aI;
    }

    public void onLightningStrike(EntityLightning entitylightning) {
        this.damageEntity(DamageSource.LIGHTNING, Float.MAX_VALUE);
    }

    public void die(DamageSource damagesource) {
        super.die(damagesource);
        if (damagesource == DamageSource.LIGHTNING) {
            this.a(new ItemStack(Items.BOWL, 1), 0.0F);
        }

    }

    protected boolean dB() {
        BlockPosition blockposition = this.getNavigation().i();

        return blockposition != null ? this.c(blockposition) < 64.0D : false;
    }

    static int g(EntityTurtle entityturtle) {
        return entityturtle.bK++;
    }

    static class g extends NavigationGuardian {

        g(EntityTurtle entityturtle, World world) {
            super(entityturtle, world);
        }

        protected boolean b() {
            return true;
        }

        protected Pathfinder a() {
            return new Pathfinder(new PathfinderTurtle());
        }

        public boolean a(BlockPosition blockposition) {
            if (this.a instanceof EntityTurtle) {
                EntityTurtle entityturtle = (EntityTurtle) this.a;

                if (entityturtle.dJ()) {
                    return this.b.getType(blockposition).getBlock() == Blocks.WATER;
                }
            }

            return !this.b.getType(blockposition.down()).isAir();
        }
    }

    static class e extends ControllerMove {

        private final EntityTurtle i;

        e(EntityTurtle entityturtle) {
            super(entityturtle);
            this.i = entityturtle;
        }

        private void g() {
            if (this.i.isInWater()) {
                this.i.motY += 0.005D;
                if (this.i.c(this.i.dC()) > 256.0D) {
                    this.i.o(Math.max(this.i.cK() / 2.0F, 0.08F));
                }

                if (this.i.isBaby()) {
                    this.i.o(Math.max(this.i.cK() / 3.0F, 0.06F));
                }
            } else if (this.i.onGround) {
                this.i.o(Math.max(this.i.cK() / 2.0F, 0.06F));
            }

        }

        public void a() {
            this.g();
            if (this.h == ControllerMove.Operation.MOVE_TO && !this.i.getNavigation().p()) {
                double d0 = this.b - this.i.locX;
                double d1 = this.c - this.i.locY;
                double d2 = this.d - this.i.locZ;
                double d3 = (double) MathHelper.sqrt(d0 * d0 + d1 * d1 + d2 * d2);

                d1 /= d3;
                float f = (float) (MathHelper.c(d2, d0) * 57.2957763671875D) - 90.0F;

                this.i.yaw = this.a(this.i.yaw, f, 90.0F);
                this.i.aQ = this.i.yaw;
                float f1 = (float) (this.e * this.i.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).getValue());

                this.i.o(this.i.cK() + (f1 - this.i.cK()) * 0.125F);
                this.i.motY += (double) this.i.cK() * d1 * 0.1D;
            } else {
                this.i.o(0.0F);
            }
        }
    }

    static class c extends PathfinderGoalGotoTarget {

        private final EntityTurtle f;

        private c(EntityTurtle entityturtle, double d0) {
            super(entityturtle, entityturtle.isBaby() ? 2.0D : d0, 24);
            this.f = entityturtle;
            this.e = -1;
        }

        public boolean b() {
            return !this.f.isInWater() && this.c <= 1200 && this.a(this.f.world, this.d);
        }

        public boolean a() {
            return this.f.isBaby() && !this.f.isInWater() ? super.a() : (!this.f.dI() && !this.f.isInWater() && !this.f.dz() ? super.a() : false);
        }

        public int j() {
            return 1;
        }

        public boolean i() {
            return this.c % 160 == 0;
        }

        protected boolean a(IWorldReader iworldreader, BlockPosition blockposition) {
            Block block = iworldreader.getType(blockposition).getBlock();

            return block == Blocks.WATER;
        }

        c(EntityTurtle entityturtle, double d0, Object object) {
            this(entityturtle, d0);
        }
    }

    static class h extends PathfinderGoalRandomStroll {

        private final EntityTurtle h;

        private h(EntityTurtle entityturtle, double d0, int i) {
            super(entityturtle, d0, i);
            this.h = entityturtle;
        }

        public boolean a() {
            return !this.a.isInWater() && !this.h.dI() && !this.h.dz() ? super.a() : false;
        }

        h(EntityTurtle entityturtle, double d0, int i, Object object) {
            this(entityturtle, d0, i);
        }
    }

    static class d extends PathfinderGoalGotoTarget {

        private final EntityTurtle f;

        d(EntityTurtle entityturtle, double d0) {
            super(entityturtle, d0, 16);
            this.f = entityturtle;
        }

        public boolean a() {
            return this.f.dz() && this.f.c(this.f.dC()) < 81.0D ? super.a() : false;
        }

        public boolean b() {
            return super.b() && this.f.dz() && this.f.c(this.f.dC()) < 81.0D;
        }

        public void e() {
            super.e();
            BlockPosition blockposition = new BlockPosition(this.f);

            if (!this.f.isInWater() && this.k()) {
                if (this.f.bK < 1) {
                    this.f.t(true);
                } else if (this.f.bK > 200) {
                    World world = this.f.world;

                    world.a((EntityHuman) null, blockposition, SoundEffects.ENTITY_TURTLE_LAY_EGG, SoundCategory.BLOCKS, 0.3F, 0.9F + world.random.nextFloat() * 0.2F);
                    world.setTypeAndData(this.d.up(), (IBlockData) Blocks.TURTLE_EGG.getBlockData().set(BlockTurtleEgg.b, Integer.valueOf(this.f.random.nextInt(4) + 1)), 3);
                    this.f.s(false);
                    this.f.t(false);
                    this.f.e(600);
                }

                if (this.f.dA()) {
                    EntityTurtle.g(this.f);
                }
            }

        }

        protected boolean a(IWorldReader iworldreader, BlockPosition blockposition) {
            if (!iworldreader.isEmpty(blockposition.up())) {
                return false;
            } else {
                Block block = iworldreader.getType(blockposition).getBlock();

                return block == Blocks.SAND;
            }
        }
    }

    static class a extends PathfinderGoalBreed {

        private final EntityTurtle d;

        a(EntityTurtle entityturtle, double d0) {
            super(entityturtle, d0);
            this.d = entityturtle;
        }

        public boolean a() {
            return super.a() && !this.d.dz();
        }

        protected void g() {
            EntityPlayer entityplayer = this.animal.getBreedCause();

            if (entityplayer == null && this.partner.getBreedCause() != null) {
                entityplayer = this.partner.getBreedCause();
            }

            if (entityplayer != null) {
                entityplayer.a(StatisticList.ANIMALS_BRED);
                CriterionTriggers.o.a(entityplayer, this.animal, this.partner, (EntityAgeable) null);
            }

            this.d.s(true);
            this.animal.resetLove();
            this.partner.resetLove();
            Random random = this.animal.getRandom();

            if (this.b.getGameRules().getBoolean("doMobLoot")) {
                this.b.addEntity(new EntityExperienceOrb(this.b, this.animal.locX, this.animal.locY, this.animal.locZ, random.nextInt(7) + 1));
            }

        }
    }

    static class i extends PathfinderGoal {

        private final EntityTurtle a;
        private final double b;
        private EntityHuman c;
        private int d;
        private final Set<Item> e;

        i(EntityTurtle entityturtle, double d0, Item item) {
            this.a = entityturtle;
            this.b = d0;
            this.e = Sets.newHashSet(new Item[] { item});
            this.a(3);
        }

        public boolean a() {
            if (this.d > 0) {
                --this.d;
                return false;
            } else {
                this.c = this.a.world.findNearbyPlayer(this.a, 10.0D);
                return this.c == null ? false : this.a(this.c.getItemInMainHand()) || this.a(this.c.getItemInOffHand());
            }
        }

        private boolean a(ItemStack itemstack) {
            return this.e.contains(itemstack.getItem());
        }

        public boolean b() {
            return this.a();
        }

        public void d() {
            this.c = null;
            this.a.getNavigation().q();
            this.d = 100;
        }

        public void e() {
            this.a.getControllerLook().a(this.c, (float) (this.a.L() + 20), (float) this.a.K());
            if (this.a.h((Entity) this.c) < 6.25D) {
                this.a.getNavigation().q();
            } else {
                this.a.getNavigation().a((Entity) this.c, this.b);
            }

        }
    }

    static class b extends PathfinderGoal {

        private final EntityTurtle a;
        private final double b;
        private boolean c;
        private int d;

        b(EntityTurtle entityturtle, double d0) {
            this.a = entityturtle;
            this.b = d0;
        }

        public boolean a() {
            return this.a.isBaby() ? false : (this.a.dz() ? true : (this.a.getRandom().nextInt(700) != 0 ? false : this.a.c(this.a.dC()) >= 4096.0D));
        }

        public void c() {
            this.a.u(true);
            this.c = false;
            this.d = 0;
        }

        public void d() {
            this.a.u(false);
        }

        public boolean b() {
            return this.a.c(this.a.dC()) >= 49.0D && !this.c && this.d <= 600;
        }

        public void e() {
            BlockPosition blockposition = this.a.dC();
            boolean flag = this.a.c(blockposition) <= 256.0D;

            if (flag) {
                ++this.d;
            }

            if (this.a.dB() || this.a.getNavigation().p()) {
                Vec3D vec3d = RandomPositionGenerator.a((EntityCreature) this.a, 16, 3, new Vec3D((double) blockposition.getX(), (double) blockposition.getY(), (double) blockposition.getZ()), 0.3141592741012573D);

                if (vec3d == null) {
                    vec3d = RandomPositionGenerator.a(this.a, 8, 7, new Vec3D((double) blockposition.getX(), (double) blockposition.getY(), (double) blockposition.getZ()));
                }

                if (vec3d != null && !flag && this.a.world.getType(new BlockPosition(vec3d)).getBlock() != Blocks.WATER) {
                    vec3d = RandomPositionGenerator.a(this.a, 16, 5, new Vec3D((double) blockposition.getX(), (double) blockposition.getY(), (double) blockposition.getZ()));
                }

                if (vec3d == null) {
                    this.c = true;
                    return;
                }

                this.a.getNavigation().a(vec3d.x, vec3d.y, vec3d.z, this.b);
            }

        }
    }

    static class j extends PathfinderGoal {

        private final EntityTurtle a;
        private final double b;
        private boolean c;

        j(EntityTurtle entityturtle, double d0) {
            this.a = entityturtle;
            this.b = d0;
        }

        public boolean a() {
            return !this.a.dI() && !this.a.dz() && this.a.isInWater();
        }

        public void c() {
            boolean flag = true;
            boolean flag1 = true;
            Random random = this.a.random;
            int i = random.nextInt(1025) - 512;
            int j = random.nextInt(9) - 4;
            int k = random.nextInt(1025) - 512;

            if ((double) j + this.a.locY > (double) (this.a.world.getSeaLevel() - 1)) {
                j = 0;
            }

            BlockPosition blockposition = new BlockPosition((double) i + this.a.locX, (double) j + this.a.locY, (double) k + this.a.locZ);

            this.a.h(blockposition);
            this.a.v(true);
            this.c = false;
        }

        public void e() {
            if (this.a.dB() || this.a.getNavigation().p()) {
                BlockPosition blockposition = this.a.dD();
                Vec3D vec3d = RandomPositionGenerator.a((EntityCreature) this.a, 16, 3, new Vec3D((double) blockposition.getX(), (double) blockposition.getY(), (double) blockposition.getZ()), 0.3141592741012573D);

                if (vec3d == null) {
                    vec3d = RandomPositionGenerator.a(this.a, 8, 7, new Vec3D((double) blockposition.getX(), (double) blockposition.getY(), (double) blockposition.getZ()));
                }

                if (vec3d != null) {
                    int i = MathHelper.floor(vec3d.x);
                    int j = MathHelper.floor(vec3d.z);
                    boolean flag = true;
                    StructureBoundingBox structureboundingbox = new StructureBoundingBox(i - 34, 0, j - 34, i + 34, 0, j + 34);

                    if (!this.a.world.a(structureboundingbox)) {
                        vec3d = null;
                    }
                }

                if (vec3d == null) {
                    this.c = true;
                    return;
                }

                this.a.getNavigation().a(vec3d.x, vec3d.y, vec3d.z, this.b);
            }

        }

        public boolean b() {
            return this.a.c(this.a.dD()) >= 36.0D && !this.c && !this.a.dI() && !this.a.isInLove() && !this.a.dz();
        }

        public void d() {
            this.a.v(false);
            super.d();
        }
    }

    static class f extends PathfinderGoalPanic {

        f(EntityTurtle entityturtle, double d0) {
            super(entityturtle, d0);
        }

        public boolean a() {
            if (this.a.getLastDamager() == null && !this.a.isBurning()) {
                return false;
            } else {
                BlockPosition blockposition = this.a(this.a.world, this.a, 7, 4);

                if (blockposition != null) {
                    this.c = (double) blockposition.getX();
                    this.d = (double) blockposition.getY();
                    this.e = (double) blockposition.getZ();
                    return true;
                } else {
                    return this.g();
                }
            }
        }
    }
}

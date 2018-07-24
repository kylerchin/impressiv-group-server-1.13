package net.minecraft.server;

import java.util.List;

public abstract class EntityFish extends EntityWaterAnimal implements IAnimal {

    private static final DataWatcherObject<Boolean> b = DataWatcher.a(EntityFish.class, DataWatcherRegistry.i);
    private boolean c;
    public int a;
    private boolean bC;

    public EntityFish(EntityTypes<?> entitytypes, World world) {
        super(entitytypes, world);
        this.moveController = new EntityFish.a(this);
        this.c = false;
        this.a = 0;
        this.bC = false;
    }

    public float getHeadHeight() {
        return this.length * 0.65F;
    }

    protected void initAttributes() {
        super.initAttributes();
        this.getAttributeInstance(GenericAttributes.maxHealth).setValue(3.0D);
    }

    public boolean a(GeneratorAccess generatoraccess) {
        BlockPosition blockposition = new BlockPosition(this);

        return generatoraccess.getType(blockposition).getBlock() == Blocks.WATER && generatoraccess.getType(blockposition.up()).getBlock() == Blocks.WATER ? super.a(generatoraccess) : false;
    }

    protected boolean isTypeNotPersistent() {
        return !this.dE() && !this.hasCustomName();
    }

    public boolean l() {
        return this.c && this.a < this.dz();
    }

    protected int dz() {
        return 8;
    }

    public int dh() {
        return this.dz();
    }

    public void a(boolean flag) {
        this.bC = flag;
    }

    public boolean dA() {
        return this.bC;
    }

    protected void x_() {
        super.x_();
        this.datawatcher.register(EntityFish.b, Boolean.valueOf(false));
    }

    private boolean dE() {
        return ((Boolean) this.datawatcher.get(EntityFish.b)).booleanValue();
    }

    public void s(boolean flag) {
        this.datawatcher.set(EntityFish.b, Boolean.valueOf(flag));
    }

    public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
        nbttagcompound.setBoolean("FromBucket", this.dE());
    }

    public void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
        this.s(nbttagcompound.getBoolean("FromBucket"));
    }

    protected void n() {
        super.n();
        this.goalSelector.a(0, new PathfinderGoalPanic(this, 1.25D));
        this.goalSelector.a(2, new PathfinderGoalAvoidTarget(this, EntityHuman.class, 8.0F, 1.6D, 1.4D, IEntitySelector.e));
        this.goalSelector.a(4, new EntityFish.b(this));
    }

    protected NavigationAbstract b(World world) {
        return new NavigationGuardian(this, world);
    }

    public boolean a(IWorldReader iworldreader) {
        return iworldreader.b(this, this.getBoundingBox()) && iworldreader.getCubes(this, this.getBoundingBox());
    }

    public void tick() {
        super.tick();
        if (this.c && this.world.random.nextInt(200) == 1) {
            List list = this.world.a(this.getClass(), this.getBoundingBox().grow(8.0D, 8.0D, 8.0D));

            if (list.size() <= 1) {
                this.t(false);
                this.a = 0;
            }
        }

    }

    public void a(float f, float f1, float f2) {
        if (this.cP() && this.isInWater()) {
            this.a(f, f1, f2, 0.01F);
            this.move(EnumMoveType.SELF, this.motX, this.motY, this.motZ);
            this.motX *= 0.8999999761581421D;
            this.motY *= 0.8999999761581421D;
            this.motZ *= 0.8999999761581421D;
            if (this.getGoalTarget() == null) {
                this.motY -= 0.005D;
            }
        } else {
            super.a(f, f1, f2);
        }

    }

    public void k() {
        if (!this.isInWater() && this.onGround && this.C) {
            this.motY += 0.4000000059604645D;
            this.motX += (double) ((this.random.nextFloat() * 2.0F - 1.0F) * 0.05F);
            this.motZ += (double) ((this.random.nextFloat() * 2.0F - 1.0F) * 0.05F);
            this.onGround = false;
            this.impulse = true;
            this.a(this.dD(), this.cD(), this.cE());
        }

        super.k();
    }

    protected boolean a(EntityHuman entityhuman, EnumHand enumhand) {
        ItemStack itemstack = entityhuman.b(enumhand);

        if (itemstack.getItem() == Items.WATER_BUCKET && this.isAlive()) {
            this.a(SoundEffects.ITEM_BUCKET_FILL_FISH, 1.0F, 1.0F);
            itemstack.subtract(1);
            ItemStack itemstack1 = this.dB();

            this.f(itemstack1);
            if (!this.world.isClientSide) {
                CriterionTriggers.j.a((EntityPlayer) entityhuman, itemstack1);
            }

            if (itemstack.isEmpty()) {
                entityhuman.a(enumhand, itemstack1);
            } else if (!entityhuman.inventory.pickup(itemstack1)) {
                entityhuman.drop(itemstack1, false);
            }

            this.die();
            return true;
        } else {
            return super.a(entityhuman, enumhand);
        }
    }

    protected void f(ItemStack itemstack) {
        if (this.hasCustomName()) {
            itemstack.a(this.getCustomName());
        }

    }

    protected abstract ItemStack dB();

    public void t(boolean flag) {
        this.c = flag;
    }

    public boolean dC() {
        return this.c;
    }

    protected abstract SoundEffect dD();

    protected SoundEffect ad() {
        return SoundEffects.ENTITY_FISH_SWIM;
    }

    static class a extends ControllerMove {

        private final EntityFish i;

        a(EntityFish entityfish) {
            super(entityfish);
            this.i = entityfish;
        }

        public void a() {
            if (this.i.a(TagsFluid.a)) {
                this.i.motY += 0.005D;
            }

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

    static class b extends PathfinderGoalRandomSwim {

        private final EntityFish h;

        public b(EntityFish entityfish) {
            super(entityfish, 1.0D, 40);
            this.h = entityfish;
        }

        public boolean a() {
            return super.a() && !this.h.dA();
        }
    }
}

package net.minecraft.server;

import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import io.netty.buffer.Unpooled;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EntityPlayer extends EntityHuman implements ICrafting {

    private static final Logger cc = LogManager.getLogger();
    private static final IChatBaseComponent cd = (new ChatMessage("multiplayer.message_not_delivered", new Object[0])).a(EnumChatFormat.RED);
    public String locale = "en_US";
    public PlayerConnection playerConnection;
    public final MinecraftServer server;
    public final PlayerInteractManager playerInteractManager;
    public double d;
    public double e;
    public final List<Integer> removeQueue = Lists.newLinkedList();
    private final AdvancementDataPlayer cg;
    private final ServerStatisticManager ch;
    private float ci = Float.MIN_VALUE;
    private int cj = Integer.MIN_VALUE;
    private int ck = Integer.MIN_VALUE;
    private int cl = Integer.MIN_VALUE;
    private int cm = Integer.MIN_VALUE;
    private int cn = Integer.MIN_VALUE;
    private float lastHealthSent = -1.0E8F;
    private int cp = -99999999;
    private boolean cq = true;
    public int lastSentExp = -99999999;
    public int invulnerableTicks = 60;
    private EntityHuman.EnumChatVisibility ct;
    private boolean cu = true;
    private long cv = SystemUtils.b();
    private Entity cw;
    public boolean worldChangeInvuln;
    private boolean cy;
    private final RecipeBookServer cz;
    private Vec3D cA;
    private int cB;
    private boolean cC;
    private Vec3D cD;
    private int containerCounter;
    public boolean f;
    public int ping;
    public boolean viewingCredits;

    public EntityPlayer(MinecraftServer minecraftserver, WorldServer worldserver, GameProfile gameprofile, PlayerInteractManager playerinteractmanager) {
        super((World) worldserver, gameprofile);
        playerinteractmanager.player = this;
        this.playerInteractManager = playerinteractmanager;
        this.server = minecraftserver;
        this.cz = new RecipeBookServer(minecraftserver.getCraftingManager());
        this.ch = minecraftserver.getPlayerList().getStatisticManager(this);
        this.cg = minecraftserver.getPlayerList().h(this);
        this.Q = 1.0F;
        this.a(worldserver);
    }

    private void a(WorldServer worldserver) {
        BlockPosition blockposition = worldserver.getSpawn();

        if (worldserver.worldProvider.g() && worldserver.getWorldData().getGameType() != EnumGamemode.ADVENTURE) {
            int i = Math.max(0, this.server.a(worldserver));
            int j = MathHelper.floor(worldserver.getWorldBorder().b((double) blockposition.getX(), (double) blockposition.getZ()));

            if (j < i) {
                i = j;
            }

            if (j <= 1) {
                i = 1;
            }

            int k = (i * 2 + 1) * (i * 2 + 1);
            int l = this.s(k);
            int i1 = (new Random()).nextInt(k);

            for (int j1 = 0; j1 < k; ++j1) {
                int k1 = (i1 + l * j1) % k;
                int l1 = k1 % (i * 2 + 1);
                int i2 = k1 / (i * 2 + 1);
                BlockPosition blockposition1 = worldserver.o().a(blockposition.getX() + l1 - i, blockposition.getZ() + i2 - i, false);

                if (blockposition1 != null) {
                    this.setPositionRotation(blockposition1, 0.0F, 0.0F);
                    if (worldserver.getCubes(this, this.getBoundingBox())) {
                        break;
                    }
                }
            }
        } else {
            this.setPositionRotation(blockposition, 0.0F, 0.0F);

            while (!worldserver.getCubes(this, this.getBoundingBox()) && this.locY < 255.0D) {
                this.setPosition(this.locX, this.locY + 1.0D, this.locZ);
            }
        }

    }

    private int s(int i) {
        return i <= 16 ? i - 1 : 17;
    }

    public void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
        if (nbttagcompound.hasKeyOfType("playerGameType", 99)) {
            if (this.bK().getForceGamemode()) {
                this.playerInteractManager.setGameMode(this.bK().getGamemode());
            } else {
                this.playerInteractManager.setGameMode(EnumGamemode.getById(nbttagcompound.getInt("playerGameType")));
            }
        }

        if (nbttagcompound.hasKeyOfType("enteredNetherPosition", 10)) {
            NBTTagCompound nbttagcompound1 = nbttagcompound.getCompound("enteredNetherPosition");

            this.cD = new Vec3D(nbttagcompound1.getDouble("x"), nbttagcompound1.getDouble("y"), nbttagcompound1.getDouble("z"));
        }

        this.cy = nbttagcompound.getBoolean("seenCredits");
        if (nbttagcompound.hasKeyOfType("recipeBook", 10)) {
            this.cz.a(nbttagcompound.getCompound("recipeBook"));
        }

    }

    public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
        nbttagcompound.setInt("playerGameType", this.playerInteractManager.getGameMode().getId());
        nbttagcompound.setBoolean("seenCredits", this.cy);
        if (this.cD != null) {
            NBTTagCompound nbttagcompound1 = new NBTTagCompound();

            nbttagcompound1.setDouble("x", this.cD.x);
            nbttagcompound1.setDouble("y", this.cD.y);
            nbttagcompound1.setDouble("z", this.cD.z);
            nbttagcompound.set("enteredNetherPosition", nbttagcompound1);
        }

        Entity entity = this.getRootVehicle();
        Entity entity1 = this.getVehicle();

        if (entity1 != null && entity != this && entity.bR()) {
            NBTTagCompound nbttagcompound2 = new NBTTagCompound();
            NBTTagCompound nbttagcompound3 = new NBTTagCompound();

            entity.d(nbttagcompound3);
            nbttagcompound2.a("Attach", entity1.getUniqueID());
            nbttagcompound2.set("Entity", nbttagcompound3);
            nbttagcompound.set("RootVehicle", nbttagcompound2);
        }

        nbttagcompound.set("recipeBook", this.cz.e());
    }

    public void a(int i) {
        float f = (float) this.getExpToLevel();
        float f1 = (f - 1.0F) / f;

        this.exp = MathHelper.a((float) i / f, 0.0F, f1);
        this.lastSentExp = -1;
    }

    public void b(int i) {
        this.expLevel = i;
        this.lastSentExp = -1;
    }

    public void levelDown(int i) {
        super.levelDown(i);
        this.lastSentExp = -1;
    }

    public void enchantDone(ItemStack itemstack, int i) {
        super.enchantDone(itemstack, i);
        this.lastSentExp = -1;
    }

    public void syncInventory() {
        this.activeContainer.addSlotListener(this);
    }

    public void enterCombat() {
        super.enterCombat();
        this.playerConnection.sendPacket(new PacketPlayOutCombatEvent(this.getCombatTracker(), PacketPlayOutCombatEvent.EnumCombatEventType.ENTER_COMBAT));
    }

    public void exitCombat() {
        super.exitCombat();
        this.playerConnection.sendPacket(new PacketPlayOutCombatEvent(this.getCombatTracker(), PacketPlayOutCombatEvent.EnumCombatEventType.END_COMBAT));
    }

    protected void a(IBlockData iblockdata) {
        CriterionTriggers.d.a(this, iblockdata);
    }

    protected ItemCooldown g() {
        return new ItemCooldownPlayer(this);
    }

    public void tick() {
        this.playerInteractManager.a();
        --this.invulnerableTicks;
        if (this.noDamageTicks > 0) {
            --this.noDamageTicks;
        }

        this.activeContainer.b();
        if (!this.world.isClientSide && !this.activeContainer.canUse(this)) {
            this.closeInventory();
            this.activeContainer = this.defaultContainer;
        }

        while (!this.removeQueue.isEmpty()) {
            int i = Math.min(this.removeQueue.size(), Integer.MAX_VALUE);
            int[] aint = new int[i];
            Iterator iterator = this.removeQueue.iterator();
            int j = 0;

            while (iterator.hasNext() && j < i) {
                aint[j++] = ((Integer) iterator.next()).intValue();
                iterator.remove();
            }

            this.playerConnection.sendPacket(new PacketPlayOutEntityDestroy(aint));
        }

        Entity entity = this.getSpecatorTarget();

        if (entity != this) {
            if (entity.isAlive()) {
                this.setLocation(entity.locX, entity.locY, entity.locZ, entity.yaw, entity.pitch);
                this.server.getPlayerList().d(this);
                if (this.isSneaking()) {
                    this.setSpectatorTarget(this);
                }
            } else {
                this.setSpectatorTarget(this);
            }
        }

        CriterionTriggers.w.a(this);
        if (this.cA != null) {
            CriterionTriggers.u.a(this, this.cA, this.ticksLived - this.cB);
        }

        this.cg.b(this);
    }

    public void playerTick() {
        try {
            super.tick();

            for (int i = 0; i < this.inventory.getSize(); ++i) {
                ItemStack itemstack = this.inventory.getItem(i);

                if (itemstack.getItem().W_()) {
                    Packet packet = ((ItemWorldMapBase) itemstack.getItem()).a(itemstack, this.world, (EntityHuman) this);

                    if (packet != null) {
                        this.playerConnection.sendPacket(packet);
                    }
                }
            }

            if (this.getHealth() != this.lastHealthSent || this.cp != this.foodData.getFoodLevel() || this.foodData.getSaturationLevel() == 0.0F != this.cq) {
                this.playerConnection.sendPacket(new PacketPlayOutUpdateHealth(this.getHealth(), this.foodData.getFoodLevel(), this.foodData.getSaturationLevel()));
                this.lastHealthSent = this.getHealth();
                this.cp = this.foodData.getFoodLevel();
                this.cq = this.foodData.getSaturationLevel() == 0.0F;
            }

            if (this.getHealth() + this.getAbsorptionHearts() != this.ci) {
                this.ci = this.getHealth() + this.getAbsorptionHearts();
                this.a(IScoreboardCriteria.g, MathHelper.f(this.ci));
            }

            if (this.foodData.getFoodLevel() != this.cj) {
                this.cj = this.foodData.getFoodLevel();
                this.a(IScoreboardCriteria.h, MathHelper.f((float) this.cj));
            }

            if (this.getAirTicks() != this.ck) {
                this.ck = this.getAirTicks();
                this.a(IScoreboardCriteria.i, MathHelper.f((float) this.ck));
            }

            if (this.getArmorStrength() != this.cl) {
                this.cl = this.getArmorStrength();
                this.a(IScoreboardCriteria.j, MathHelper.f((float) this.cl));
            }

            if (this.expTotal != this.cn) {
                this.cn = this.expTotal;
                this.a(IScoreboardCriteria.k, MathHelper.f((float) this.cn));
            }

            if (this.expLevel != this.cm) {
                this.cm = this.expLevel;
                this.a(IScoreboardCriteria.l, MathHelper.f((float) this.cm));
            }

            if (this.expTotal != this.lastSentExp) {
                this.lastSentExp = this.expTotal;
                this.playerConnection.sendPacket(new PacketPlayOutExperience(this.exp, this.expTotal, this.expLevel));
            }

            if (this.ticksLived % 20 == 0) {
                CriterionTriggers.p.a(this);
            }

        } catch (Throwable throwable) {
            CrashReport crashreport = CrashReport.a(throwable, "Ticking player");
            CrashReportSystemDetails crashreportsystemdetails = crashreport.a("Player being ticked");

            this.appendEntityCrashDetails(crashreportsystemdetails);
            throw new ReportedException(crashreport);
        }
    }

    private void a(IScoreboardCriteria iscoreboardcriteria, int i) {
        this.getScoreboard().getObjectivesForCriteria(iscoreboardcriteria, this.getName(), (scoreboardscore) -> {
            scoreboardscore.setScore(i);
        });
    }

    public void die(DamageSource damagesource) {
        boolean flag = this.world.getGameRules().getBoolean("showDeathMessages");

        this.playerConnection.sendPacket(new PacketPlayOutCombatEvent(this.getCombatTracker(), PacketPlayOutCombatEvent.EnumCombatEventType.ENTITY_DIED, flag));
        if (flag) {
            ScoreboardTeamBase scoreboardteambase = this.be();

            if (scoreboardteambase != null && scoreboardteambase.getDeathMessageVisibility() != ScoreboardTeamBase.EnumNameTagVisibility.ALWAYS) {
                if (scoreboardteambase.getDeathMessageVisibility() == ScoreboardTeamBase.EnumNameTagVisibility.HIDE_FOR_OTHER_TEAMS) {
                    this.server.getPlayerList().a((EntityHuman) this, this.getCombatTracker().getDeathMessage());
                } else if (scoreboardteambase.getDeathMessageVisibility() == ScoreboardTeamBase.EnumNameTagVisibility.HIDE_FOR_OWN_TEAM) {
                    this.server.getPlayerList().b((EntityHuman) this, this.getCombatTracker().getDeathMessage());
                }
            } else {
                this.server.getPlayerList().sendMessage(this.getCombatTracker().getDeathMessage());
            }
        }

        this.releaseShoulderEntities();
        if (!this.world.getGameRules().getBoolean("keepInventory") && !this.isSpectator()) {
            this.dj();
            this.inventory.q();
        }

        this.getScoreboard().getObjectivesForCriteria(IScoreboardCriteria.d, this.getName(), ScoreboardScore::incrementScore);
        EntityLiving entityliving = this.cv();

        if (entityliving != null) {
            this.b(StatisticList.ENTITY_KILLED_BY.b(entityliving.P()));
            entityliving.a(this, this.be, damagesource);
        }

        this.a(StatisticList.DEATHS);
        this.a(StatisticList.CUSTOM.b(StatisticList.TIME_SINCE_DEATH));
        this.a(StatisticList.CUSTOM.b(StatisticList.n));
        this.extinguish();
        this.setFlag(0, false);
        this.getCombatTracker().g();
    }

    public void a(Entity entity, int i, DamageSource damagesource) {
        if (entity != this) {
            super.a(entity, i, damagesource);
            this.addScore(i);
            String s = this.getName();
            String s1 = entity.getName();

            this.getScoreboard().getObjectivesForCriteria(IScoreboardCriteria.f, s, ScoreboardScore::incrementScore);
            if (entity instanceof EntityHuman) {
                this.a(StatisticList.PLAYER_KILLS);
                this.getScoreboard().getObjectivesForCriteria(IScoreboardCriteria.e, s, ScoreboardScore::incrementScore);
            } else {
                this.a(StatisticList.MOB_KILLS);
            }

            this.a(s, s1, IScoreboardCriteria.m);
            this.a(s1, s, IScoreboardCriteria.n);
            CriterionTriggers.b.a(this, entity, damagesource);
        }
    }

    private void a(String s, String s1, IScoreboardCriteria[] aiscoreboardcriteria) {
        ScoreboardTeam scoreboardteam = this.getScoreboard().getPlayerTeam(s1);

        if (scoreboardteam != null) {
            int i = scoreboardteam.getColor().b();

            if (i >= 0 && i < aiscoreboardcriteria.length) {
                this.getScoreboard().getObjectivesForCriteria(aiscoreboardcriteria[i], s, ScoreboardScore::incrementScore);
            }
        }

    }

    public boolean damageEntity(DamageSource damagesource, float f) {
        if (this.isInvulnerable(damagesource)) {
            return false;
        } else {
            boolean flag = this.server.S() && this.canPvP() && "fall".equals(damagesource.translationIndex);

            if (!flag && this.invulnerableTicks > 0 && damagesource != DamageSource.OUT_OF_WORLD) {
                return false;
            } else {
                if (damagesource instanceof EntityDamageSource) {
                    Entity entity = damagesource.getEntity();

                    if (entity instanceof EntityHuman && !this.a((EntityHuman) entity)) {
                        return false;
                    }

                    if (entity instanceof EntityArrow) {
                        EntityArrow entityarrow = (EntityArrow) entity;

                        if (entityarrow.shooter instanceof EntityHuman && !this.a((EntityHuman) entityarrow.shooter)) {
                            return false;
                        }
                    }
                }

                return super.damageEntity(damagesource, f);
            }
        }
    }

    public boolean a(EntityHuman entityhuman) {
        return !this.canPvP() ? false : super.a(entityhuman);
    }

    private boolean canPvP() {
        return this.server.getPVP();
    }

    @Nullable
    public Entity d(int i) {
        this.worldChangeInvuln = true;
        if (this.dimension == 0 && i == -1) {
            this.cD = new Vec3D(this.locX, this.locY, this.locZ);
        } else if (this.dimension != -1 && i != 0) {
            this.cD = null;
        }

        if (this.dimension == 1 && i == 1) {
            this.world.kill(this);
            if (!this.viewingCredits) {
                this.viewingCredits = true;
                this.playerConnection.sendPacket(new PacketPlayOutGameStateChange(4, this.cy ? 0.0F : 1.0F));
                this.cy = true;
            }

            return this;
        } else {
            if (this.dimension == 0 && i == 1) {
                i = 1;
            }

            this.server.getPlayerList().a(this, i);
            this.playerConnection.sendPacket(new PacketPlayOutWorldEvent(1032, BlockPosition.ZERO, 0, false));
            this.lastSentExp = -1;
            this.lastHealthSent = -1.0F;
            this.cp = -1;
            return this;
        }
    }

    public boolean a(EntityPlayer entityplayer) {
        return entityplayer.isSpectator() ? this.getSpecatorTarget() == this : (this.isSpectator() ? false : super.a(entityplayer));
    }

    private void a(TileEntity tileentity) {
        if (tileentity != null) {
            PacketPlayOutTileEntityData packetplayouttileentitydata = tileentity.getUpdatePacket();

            if (packetplayouttileentitydata != null) {
                this.playerConnection.sendPacket(packetplayouttileentitydata);
            }
        }

    }

    public void receive(Entity entity, int i) {
        super.receive(entity, i);
        this.activeContainer.b();
    }

    public EntityHuman.EnumBedResult a(BlockPosition blockposition) {
        EntityHuman.EnumBedResult entityhuman_enumbedresult = super.a(blockposition);

        if (entityhuman_enumbedresult == EntityHuman.EnumBedResult.OK) {
            this.a(StatisticList.SLEEP_IN_BED);
            PacketPlayOutBed packetplayoutbed = new PacketPlayOutBed(this, blockposition);

            this.getWorldServer().getTracker().a((Entity) this, (Packet) packetplayoutbed);
            this.playerConnection.a(this.locX, this.locY, this.locZ, this.yaw, this.pitch);
            this.playerConnection.sendPacket(packetplayoutbed);
            CriterionTriggers.q.a(this);
        }

        return entityhuman_enumbedresult;
    }

    public void a(boolean flag, boolean flag1, boolean flag2) {
        if (this.isSleeping()) {
            this.getWorldServer().getTracker().sendPacketToEntity(this, new PacketPlayOutAnimation(this, 2));
        }

        super.a(flag, flag1, flag2);
        if (this.playerConnection != null) {
            this.playerConnection.a(this.locX, this.locY, this.locZ, this.yaw, this.pitch);
        }

    }

    public boolean a(Entity entity, boolean flag) {
        Entity entity1 = this.getVehicle();

        if (!super.a(entity, flag)) {
            return false;
        } else {
            Entity entity2 = this.getVehicle();

            if (entity2 != entity1 && this.playerConnection != null) {
                this.playerConnection.a(this.locX, this.locY, this.locZ, this.yaw, this.pitch);
            }

            return true;
        }
    }

    public void stopRiding() {
        Entity entity = this.getVehicle();

        super.stopRiding();
        Entity entity1 = this.getVehicle();

        if (entity1 != entity && this.playerConnection != null) {
            this.playerConnection.a(this.locX, this.locY, this.locZ, this.yaw, this.pitch);
        }

    }

    public boolean isInvulnerable(DamageSource damagesource) {
        return super.isInvulnerable(damagesource) || this.H();
    }

    protected void a(double d0, boolean flag, IBlockData iblockdata, BlockPosition blockposition) {}

    protected void b(BlockPosition blockposition) {
        if (!this.isSpectator()) {
            super.b(blockposition);
        }

    }

    public void a(double d0, boolean flag) {
        int i = MathHelper.floor(this.locX);
        int j = MathHelper.floor(this.locY - 0.20000000298023224D);
        int k = MathHelper.floor(this.locZ);
        BlockPosition blockposition = new BlockPosition(i, j, k);
        IBlockData iblockdata = this.world.getType(blockposition);

        if (iblockdata.isAir()) {
            BlockPosition blockposition1 = blockposition.down();
            IBlockData iblockdata1 = this.world.getType(blockposition1);
            Block block = iblockdata1.getBlock();

            if (block instanceof BlockFence || block instanceof BlockCobbleWall || block instanceof BlockFenceGate) {
                blockposition = blockposition1;
                iblockdata = iblockdata1;
            }
        }

        super.a(d0, flag, iblockdata, blockposition);
    }

    public void openSign(TileEntitySign tileentitysign) {
        tileentitysign.a((EntityHuman) this);
        this.playerConnection.sendPacket(new PacketPlayOutOpenSignEditor(tileentitysign.getPosition()));
    }

    public void nextContainerCounter() {
        this.containerCounter = this.containerCounter % 100 + 1;
    }

    public void openTileEntity(ITileEntityContainer itileentitycontainer) {
        if (itileentitycontainer instanceof ILootable && ((ILootable) itileentitycontainer).Q_() != null && this.isSpectator()) {
            this.a((new ChatMessage("container.spectatorCantOpen", new Object[0])).a(EnumChatFormat.RED), true);
        } else {
            this.nextContainerCounter();
            this.playerConnection.sendPacket(new PacketPlayOutOpenWindow(this.containerCounter, itileentitycontainer.getContainerName(), itileentitycontainer.getScoreboardDisplayName()));
            this.activeContainer = itileentitycontainer.createContainer(this.inventory, this);
            this.activeContainer.windowId = this.containerCounter;
            this.activeContainer.addSlotListener(this);
        }
    }

    public void openContainer(IInventory iinventory) {
        if (iinventory instanceof ILootable && ((ILootable) iinventory).Q_() != null && this.isSpectator()) {
            this.a((new ChatMessage("container.spectatorCantOpen", new Object[0])).a(EnumChatFormat.RED), true);
        } else {
            if (this.activeContainer != this.defaultContainer) {
                this.closeInventory();
            }

            if (iinventory instanceof ITileInventory) {
                ITileInventory itileinventory = (ITileInventory) iinventory;

                if (itileinventory.isLocked() && !this.a(itileinventory.getLock()) && !this.isSpectator()) {
                    this.playerConnection.sendPacket(new PacketPlayOutChat(new ChatMessage("container.isLocked", new Object[] { iinventory.getScoreboardDisplayName()}), ChatMessageType.GAME_INFO));
                    this.playerConnection.sendPacket(new PacketPlayOutNamedSoundEffect(SoundEffects.BLOCK_CHEST_LOCKED, SoundCategory.BLOCKS, this.locX, this.locY, this.locZ, 1.0F, 1.0F));
                    return;
                }
            }

            this.nextContainerCounter();
            if (iinventory instanceof ITileEntityContainer) {
                this.playerConnection.sendPacket(new PacketPlayOutOpenWindow(this.containerCounter, ((ITileEntityContainer) iinventory).getContainerName(), iinventory.getScoreboardDisplayName(), iinventory.getSize()));
                this.activeContainer = ((ITileEntityContainer) iinventory).createContainer(this.inventory, this);
            } else {
                this.playerConnection.sendPacket(new PacketPlayOutOpenWindow(this.containerCounter, "minecraft:container", iinventory.getScoreboardDisplayName(), iinventory.getSize()));
                this.activeContainer = new ContainerChest(this.inventory, iinventory, this);
            }

            this.activeContainer.windowId = this.containerCounter;
            this.activeContainer.addSlotListener(this);
        }
    }

    public void openTrade(IMerchant imerchant) {
        this.nextContainerCounter();
        this.activeContainer = new ContainerMerchant(this.inventory, imerchant, this.world);
        this.activeContainer.windowId = this.containerCounter;
        this.activeContainer.addSlotListener(this);
        InventoryMerchant inventorymerchant = ((ContainerMerchant) this.activeContainer).d();
        IChatBaseComponent ichatbasecomponent = imerchant.getScoreboardDisplayName();

        this.playerConnection.sendPacket(new PacketPlayOutOpenWindow(this.containerCounter, "minecraft:villager", ichatbasecomponent, inventorymerchant.getSize()));
        MerchantRecipeList merchantrecipelist = imerchant.getOffers(this);

        if (merchantrecipelist != null) {
            PacketDataSerializer packetdataserializer = new PacketDataSerializer(Unpooled.buffer());

            packetdataserializer.writeInt(this.containerCounter);
            merchantrecipelist.a(packetdataserializer);
            this.playerConnection.sendPacket(new PacketPlayOutCustomPayload(PacketPlayOutCustomPayload.a, packetdataserializer));
        }

    }

    public void openHorseInventory(EntityHorseAbstract entityhorseabstract, IInventory iinventory) {
        if (this.activeContainer != this.defaultContainer) {
            this.closeInventory();
        }

        this.nextContainerCounter();
        this.playerConnection.sendPacket(new PacketPlayOutOpenWindow(this.containerCounter, "EntityHorse", iinventory.getScoreboardDisplayName(), iinventory.getSize(), entityhorseabstract.getId()));
        this.activeContainer = new ContainerHorse(this.inventory, iinventory, entityhorseabstract, this);
        this.activeContainer.windowId = this.containerCounter;
        this.activeContainer.addSlotListener(this);
    }

    public void a(ItemStack itemstack, EnumHand enumhand) {
        Item item = itemstack.getItem();

        if (item == Items.WRITTEN_BOOK) {
            PacketDataSerializer packetdataserializer = new PacketDataSerializer(Unpooled.buffer());

            packetdataserializer.a((Enum) enumhand);
            this.playerConnection.sendPacket(new PacketPlayOutCustomPayload(PacketPlayOutCustomPayload.c, packetdataserializer));
        }

    }

    public void a(TileEntityCommand tileentitycommand) {
        tileentitycommand.c(true);
        this.a((TileEntity) tileentitycommand);
    }

    public void a(Container container, int i, ItemStack itemstack) {
        if (!(container.getSlot(i) instanceof SlotResult)) {
            if (container == this.defaultContainer) {
                CriterionTriggers.e.a(this, this.inventory);
            }

            if (!this.f) {
                this.playerConnection.sendPacket(new PacketPlayOutSetSlot(container.windowId, i, itemstack));
            }
        }
    }

    public void updateInventory(Container container) {
        this.a(container, container.a());
    }

    public void a(Container container, NonNullList<ItemStack> nonnulllist) {
        this.playerConnection.sendPacket(new PacketPlayOutWindowItems(container.windowId, nonnulllist));
        this.playerConnection.sendPacket(new PacketPlayOutSetSlot(-1, -1, this.inventory.getCarried()));
    }

    public void setContainerData(Container container, int i, int j) {
        this.playerConnection.sendPacket(new PacketPlayOutWindowData(container.windowId, i, j));
    }

    public void setContainerData(Container container, IInventory iinventory) {
        for (int i = 0; i < iinventory.h(); ++i) {
            this.playerConnection.sendPacket(new PacketPlayOutWindowData(container.windowId, i, iinventory.getProperty(i)));
        }

    }

    public void closeInventory() {
        this.playerConnection.sendPacket(new PacketPlayOutCloseWindow(this.activeContainer.windowId));
        this.m();
    }

    public void broadcastCarriedItem() {
        if (!this.f) {
            this.playerConnection.sendPacket(new PacketPlayOutSetSlot(-1, -1, this.inventory.getCarried()));
        }
    }

    public void m() {
        this.activeContainer.b((EntityHuman) this);
        this.activeContainer = this.defaultContainer;
    }

    public void a(float f, float f1, boolean flag, boolean flag1) {
        if (this.isPassenger()) {
            if (f >= -1.0F && f <= 1.0F) {
                this.bh = f;
            }

            if (f1 >= -1.0F && f1 <= 1.0F) {
                this.bj = f1;
            }

            this.bg = flag;
            this.setSneaking(flag1);
        }

    }

    public void a(Statistic<?> statistic, int i) {
        this.ch.b(this, statistic, i);
        this.getScoreboard().getObjectivesForCriteria(statistic, this.getName(), (scoreboardscore) -> {
            scoreboardscore.addScore(i);
        });
    }

    public void a(Statistic<?> statistic) {
        this.ch.setStatistic(this, statistic, 0);
        this.getScoreboard().getObjectivesForCriteria(statistic, this.getName(), ScoreboardScore::c);
    }

    public int a(Collection<IRecipe> collection) {
        return this.cz.a(collection, this);
    }

    public void a(MinecraftKey[] aminecraftkey) {
        ArrayList arraylist = Lists.newArrayList();
        MinecraftKey[] aminecraftkey1 = aminecraftkey;
        int i = aminecraftkey.length;

        for (int j = 0; j < i; ++j) {
            MinecraftKey minecraftkey = aminecraftkey1[j];
            IRecipe irecipe = this.server.getCraftingManager().a(minecraftkey);

            if (irecipe != null) {
                arraylist.add(irecipe);
            }
        }

        this.a((Collection) arraylist);
    }

    public int b(Collection<IRecipe> collection) {
        return this.cz.b(collection, this);
    }

    public void giveExp(int i) {
        super.giveExp(i);
        this.lastSentExp = -1;
    }

    public void n() {
        this.cC = true;
        this.ejectPassengers();
        if (this.sleeping) {
            this.a(true, false, false);
        }

    }

    public boolean o() {
        return this.cC;
    }

    public void triggerHealthUpdate() {
        this.lastHealthSent = -1.0E8F;
    }

    public void a(IChatBaseComponent ichatbasecomponent, boolean flag) {
        this.playerConnection.sendPacket(new PacketPlayOutChat(ichatbasecomponent, flag ? ChatMessageType.GAME_INFO : ChatMessageType.CHAT));
    }

    protected void q() {
        if (!this.activeItem.isEmpty() && this.isHandRaised()) {
            this.playerConnection.sendPacket(new PacketPlayOutEntityStatus(this, (byte) 9));
            super.q();
        }

    }

    public void a(ArgumentAnchor.Anchor argumentanchor_anchor, Vec3D vec3d) {
        super.a(argumentanchor_anchor, vec3d);
        this.playerConnection.sendPacket(new PacketPlayOutLookAt(argumentanchor_anchor, vec3d.x, vec3d.y, vec3d.z));
    }

    public void a(ArgumentAnchor.Anchor argumentanchor_anchor, Entity entity, ArgumentAnchor.Anchor argumentanchor_anchor1) {
        Vec3D vec3d = argumentanchor_anchor1.a(entity);

        super.a(argumentanchor_anchor, vec3d);
        this.playerConnection.sendPacket(new PacketPlayOutLookAt(argumentanchor_anchor, entity, argumentanchor_anchor1));
    }

    public void copyFrom(EntityPlayer entityplayer, boolean flag) {
        if (flag) {
            this.inventory.a(entityplayer.inventory);
            this.setHealth(entityplayer.getHealth());
            this.foodData = entityplayer.foodData;
            this.expLevel = entityplayer.expLevel;
            this.expTotal = entityplayer.expTotal;
            this.exp = entityplayer.exp;
            this.setScore(entityplayer.getScore());
            this.aq = entityplayer.aq;
            this.ar = entityplayer.ar;
            this.as = entityplayer.as;
        } else if (this.world.getGameRules().getBoolean("keepInventory") || entityplayer.isSpectator()) {
            this.inventory.a(entityplayer.inventory);
            this.expLevel = entityplayer.expLevel;
            this.expTotal = entityplayer.expTotal;
            this.exp = entityplayer.exp;
            this.setScore(entityplayer.getScore());
        }

        this.bZ = entityplayer.bZ;
        this.enderChest = entityplayer.enderChest;
        this.getDataWatcher().set(EntityPlayer.bx, entityplayer.getDataWatcher().get(EntityPlayer.bx));
        this.lastSentExp = -1;
        this.lastHealthSent = -1.0F;
        this.cp = -1;
        this.cz.a((RecipeBook) entityplayer.cz);
        this.removeQueue.addAll(entityplayer.removeQueue);
        this.cy = entityplayer.cy;
        this.cD = entityplayer.cD;
        this.setShoulderEntityLeft(entityplayer.getShoulderEntityLeft());
        this.setShoulderEntityRight(entityplayer.getShoulderEntityRight());
    }

    protected void a(MobEffect mobeffect) {
        super.a(mobeffect);
        this.playerConnection.sendPacket(new PacketPlayOutEntityEffect(this.getId(), mobeffect));
        if (mobeffect.getMobEffect() == MobEffects.LEVITATION) {
            this.cB = this.ticksLived;
            this.cA = new Vec3D(this.locX, this.locY, this.locZ);
        }

        CriterionTriggers.A.a(this);
    }

    protected void a(MobEffect mobeffect, boolean flag) {
        super.a(mobeffect, flag);
        this.playerConnection.sendPacket(new PacketPlayOutEntityEffect(this.getId(), mobeffect));
        CriterionTriggers.A.a(this);
    }

    protected void b(MobEffect mobeffect) {
        super.b(mobeffect);
        this.playerConnection.sendPacket(new PacketPlayOutRemoveEntityEffect(this.getId(), mobeffect.getMobEffect()));
        if (mobeffect.getMobEffect() == MobEffects.LEVITATION) {
            this.cA = null;
        }

        CriterionTriggers.A.a(this);
    }

    public void enderTeleportTo(double d0, double d1, double d2) {
        this.playerConnection.a(d0, d1, d2, this.yaw, this.pitch);
    }

    public void a(Entity entity) {
        this.getWorldServer().getTracker().sendPacketToEntity(this, new PacketPlayOutAnimation(entity, 4));
    }

    public void b(Entity entity) {
        this.getWorldServer().getTracker().sendPacketToEntity(this, new PacketPlayOutAnimation(entity, 5));
    }

    public void updateAbilities() {
        if (this.playerConnection != null) {
            this.playerConnection.sendPacket(new PacketPlayOutAbilities(this.abilities));
            this.C();
        }
    }

    public WorldServer getWorldServer() {
        return (WorldServer) this.world;
    }

    public void a(EnumGamemode enumgamemode) {
        this.playerInteractManager.setGameMode(enumgamemode);
        this.playerConnection.sendPacket(new PacketPlayOutGameStateChange(3, (float) enumgamemode.getId()));
        if (enumgamemode == EnumGamemode.SPECTATOR) {
            this.releaseShoulderEntities();
            this.stopRiding();
        } else {
            this.setSpectatorTarget(this);
        }

        this.updateAbilities();
        this.cR();
    }

    public boolean isSpectator() {
        return this.playerInteractManager.getGameMode() == EnumGamemode.SPECTATOR;
    }

    public boolean u() {
        return this.playerInteractManager.getGameMode() == EnumGamemode.CREATIVE;
    }

    public void sendMessage(IChatBaseComponent ichatbasecomponent) {
        this.a(ichatbasecomponent, ChatMessageType.SYSTEM);
    }

    public void a(IChatBaseComponent ichatbasecomponent, ChatMessageType chatmessagetype) {
        this.playerConnection.a((Packet) (new PacketPlayOutChat(ichatbasecomponent, chatmessagetype)), (future) -> {
            if (!future.isSuccess() && (chatmessagetype == ChatMessageType.GAME_INFO || chatmessagetype == ChatMessageType.SYSTEM)) {
                this.playerConnection.sendPacket(new PacketPlayOutChat(EntityPlayer.cd, ChatMessageType.SYSTEM));
            }

        });
    }

    public String v() {
        String s = this.playerConnection.networkManager.getSocketAddress().toString();

        s = s.substring(s.indexOf("/") + 1);
        s = s.substring(0, s.indexOf(":"));
        return s;
    }

    public void a(PacketPlayInSettings packetplayinsettings) {
        this.locale = packetplayinsettings.b();
        this.ct = packetplayinsettings.d();
        this.cu = packetplayinsettings.e();
        this.getDataWatcher().set(EntityPlayer.bx, Byte.valueOf((byte) packetplayinsettings.f()));
        this.getDataWatcher().set(EntityPlayer.by, Byte.valueOf((byte) (packetplayinsettings.getMainHand() == EnumMainHand.LEFT ? 0 : 1)));
    }

    public EntityHuman.EnumChatVisibility getChatFlags() {
        return this.ct;
    }

    public void setResourcePack(String s, String s1) {
        this.playerConnection.sendPacket(new PacketPlayOutResourcePackSend(s, s1));
    }

    protected int y() {
        return this.server.a(this.getProfile());
    }

    public void resetIdleTimer() {
        this.cv = SystemUtils.b();
    }

    public ServerStatisticManager getStatisticManager() {
        return this.ch;
    }

    public RecipeBookServer B() {
        return this.cz;
    }

    public void c(Entity entity) {
        if (entity instanceof EntityHuman) {
            this.playerConnection.sendPacket(new PacketPlayOutEntityDestroy(new int[] { entity.getId()}));
        } else {
            this.removeQueue.add(Integer.valueOf(entity.getId()));
        }

    }

    public void d(Entity entity) {
        this.removeQueue.remove(Integer.valueOf(entity.getId()));
    }

    protected void C() {
        if (this.isSpectator()) {
            this.cl();
            this.setInvisible(true);
        } else {
            super.C();
        }

        this.getWorldServer().getTracker().a(this);
    }

    public Entity getSpecatorTarget() {
        return (Entity) (this.cw == null ? this : this.cw);
    }

    public void setSpectatorTarget(Entity entity) {
        Entity entity1 = this.getSpecatorTarget();

        this.cw = (Entity) (entity == null ? this : entity);
        if (entity1 != this.cw) {
            this.playerConnection.sendPacket(new PacketPlayOutCamera(this.cw));
            this.enderTeleportTo(this.cw.locX, this.cw.locY, this.cw.locZ);
        }

    }

    protected void E() {
        if (this.portalCooldown > 0 && !this.worldChangeInvuln) {
            --this.portalCooldown;
        }

    }

    public void attack(Entity entity) {
        if (this.playerInteractManager.getGameMode() == EnumGamemode.SPECTATOR) {
            this.setSpectatorTarget(entity);
        } else {
            super.attack(entity);
        }

    }

    public long F() {
        return this.cv;
    }

    @Nullable
    public IChatBaseComponent getPlayerListName() {
        return null;
    }

    public void a(EnumHand enumhand) {
        super.a(enumhand);
        this.dH();
    }

    public boolean H() {
        return this.worldChangeInvuln;
    }

    public void I() {
        this.worldChangeInvuln = false;
    }

    public void J() {
        this.setFlag(7, true);
    }

    public void K() {
        this.setFlag(7, true);
        this.setFlag(7, false);
    }

    public AdvancementDataPlayer getAdvancementData() {
        return this.cg;
    }

    @Nullable
    public Vec3D M() {
        return this.cD;
    }

    public void a(WorldServer worldserver, double d0, double d1, double d2, float f, float f1) {
        this.setSpectatorTarget(this);
        this.stopRiding();
        if (worldserver == this.world) {
            this.playerConnection.a(d0, d1, d2, f, f1);
        } else {
            WorldServer worldserver1 = this.getWorldServer();

            this.dimension = worldserver.worldProvider.getDimensionManager().getDimensionID();
            this.playerConnection.sendPacket(new PacketPlayOutRespawn(this.dimension, worldserver1.getDifficulty(), worldserver1.getWorldData().getType(), this.playerInteractManager.getGameMode()));
            this.server.getPlayerList().f(this);
            worldserver1.removeEntity(this);
            this.dead = false;
            this.setPositionRotation(d0, d1, d2, f, f1);
            if (this.isAlive()) {
                worldserver1.entityJoinedWorld(this, false);
                worldserver.addEntity(this);
                worldserver.entityJoinedWorld(this, false);
            }

            this.spawnIn(worldserver);
            this.server.getPlayerList().a(this, worldserver1);
            this.playerConnection.a(d0, d1, d2, f, f1);
            this.playerInteractManager.a(worldserver);
            this.server.getPlayerList().b(this, worldserver);
            this.server.getPlayerList().updateClient(this);
        }

    }
}

package net.minecraft.server;

public class TileEntityJukeBox extends TileEntity {

    private ItemStack a;

    public TileEntityJukeBox() {
        super(TileEntityTypes.f);
        this.a = ItemStack.a;
    }

    public void load(NBTTagCompound nbttagcompound) {
        super.load(nbttagcompound);
        if (nbttagcompound.hasKeyOfType("RecordItem", 10)) {
            this.setRecord(ItemStack.a(nbttagcompound.getCompound("RecordItem")));
        }

    }

    public NBTTagCompound save(NBTTagCompound nbttagcompound) {
        super.save(nbttagcompound);
        if (!this.getRecord().isEmpty()) {
            nbttagcompound.set("RecordItem", this.getRecord().save(new NBTTagCompound()));
        }

        return nbttagcompound;
    }

    public ItemStack getRecord() {
        return this.a;
    }

    public void setRecord(ItemStack itemstack) {
        // CraftBukkit start - There can only be one
        if (!itemstack.isEmpty()) {
            itemstack.setCount(1);
        }
        // CraftBukkit end
        this.a = itemstack;
        this.update();
    }
}

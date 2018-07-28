package net.minecraft.server;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.stream.Stream;

public class RecipeRepair extends ShapelessRecipes implements IRecipe { // CraftBukkit - added extends

    // CraftBukkit start - Delegate to new parent class
    public RecipeRepair(MinecraftKey minecraftkey) {
        super(minecraftkey, "", new ItemStack(Items.LEATHER_HELMET), NonNullList.a(RecipeItemStack.a, RecipeItemStack.a(Items.LEATHER_HELMET)));
    }
    // CraftBukkit end

    public boolean a(IInventory iinventory, World world) {
        if (!(iinventory instanceof InventoryCrafting)) {
            return false;
        } else {
            ArrayList arraylist = Lists.newArrayList();

            for (int i = 0; i < iinventory.getSize(); ++i) {
                ItemStack itemstack = iinventory.getItem(i);

                if (!itemstack.isEmpty()) {
                    arraylist.add(itemstack);
                    if (arraylist.size() > 1) {
                        ItemStack itemstack1 = (ItemStack) arraylist.get(0);

                        if (itemstack.getItem() != itemstack1.getItem() || itemstack1.getCount() != 1 || itemstack.getCount() != 1 || !itemstack1.getItem().usesDurability()) {
                            return false;
                        }
                    }
                }
            }

            return arraylist.size() == 2;
        }
    }

    public ItemStack craftItem(IInventory iinventory) {
        ArrayList arraylist = Lists.newArrayList();

        ItemStack itemstack;

        for (int i = 0; i < iinventory.getSize(); ++i) {
            itemstack = iinventory.getItem(i);
            if (!itemstack.isEmpty()) {
                arraylist.add(itemstack);
                if (arraylist.size() > 1) {
                    ItemStack itemstack1 = (ItemStack) arraylist.get(0);

                    if (itemstack.getItem() != itemstack1.getItem() || itemstack1.getCount() != 1 || itemstack.getCount() != 1 || !itemstack1.getItem().usesDurability()) {
                        return ItemStack.a;
                    }
                }
            }
        }

        if (arraylist.size() == 2) {
            ItemStack itemstack2 = (ItemStack) arraylist.get(0);

            itemstack = (ItemStack) arraylist.get(1);
            if (itemstack2.getItem() == itemstack.getItem() && itemstack2.getCount() == 1 && itemstack.getCount() == 1 && itemstack2.getItem().usesDurability()) {
                Item item = itemstack2.getItem();
                int j = item.getMaxDurability() - itemstack2.getDamage();
                int k = item.getMaxDurability() - itemstack.getDamage();
                int l = j + k + item.getMaxDurability() * 5 / 100;
                int i1 = item.getMaxDurability() - l;

                if (i1 < 0) {
                    i1 = 0;
                }

                ItemStack itemstack3 = new ItemStack(itemstack2.getItem());

                itemstack3.setDamage(i1);
                // CraftBukkit start - Construct a dummy repair recipe
                if (iinventory instanceof InventoryCrafting) {
                    InventoryCrafting inventorycrafting = (InventoryCrafting) iinventory;
                    NonNullList<RecipeItemStack> ingredients = NonNullList.a();
                    ingredients.add(new RecipeItemStack(Stream.of(new RecipeItemStack.StackProvider(itemstack2.cloneItemStack()))));
                    ingredients.add(new RecipeItemStack(Stream.of(new RecipeItemStack.StackProvider(itemstack.cloneItemStack()))));
                    ShapelessRecipes recipe = new ShapelessRecipes(new MinecraftKey("repairitem"), "", itemstack3.cloneItemStack(), ingredients);
                    inventorycrafting.setCurrentRecipe(recipe);
                    itemstack3 = org.bukkit.craftbukkit.event.CraftEventFactory.callPreCraftEvent(inventorycrafting, inventorycrafting.resultInventory, itemstack3, inventorycrafting.container.getBukkitView(), true);
                }
                // CraftBukkit end
                return itemstack3;
            }
        }

        return ItemStack.a;
    }

    public RecipeSerializer<?> a() {
        return RecipeSerializers.j;
    }
}

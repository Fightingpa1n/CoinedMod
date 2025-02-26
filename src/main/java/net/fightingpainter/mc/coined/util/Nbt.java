package net.fightingpainter.mc.coined.util;

import java.util.stream.Stream;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;

public class Nbt { //base nbt class
    
    //============================== ItemStack ==============================\\
    //=============== Conversion ===============\\
    /**
     * Converts an itemstack to nbt
     * @param itemStack the itemstack to convert
     * @return the nbt tag
    */
    public static Tag itemToNbt(ItemStack itemStack) {
        HolderLookup.Provider provider = HolderLookup.Provider.create(Stream.of());
        return itemStack.save(provider, new CompoundTag());
    }

    /**
     * Converts an nbt tag to an itemstack
     * @param tag the nbt tag to convert
     * @return the itemstack
    */
    public static ItemStack nbtToItem(Tag tag) {
        ItemStack stack = ItemStack.EMPTY;
        stack = ItemStack.CODEC.parse(NbtOps.INSTANCE, tag).result().orElse(stack);
        return stack;
    }

    //=============== ItemStack Nbt ===============\\
    /**
     * Get the nbt data from an itemstack
     * @param itemStack the itemstack to get the nbt data from
     * @return the nbt data
    */
    public static CompoundTag getNbt(ItemStack itemStack, String key) {
        return new CompoundTag(); //TODO
    }

    /**
     * Set the nbt data of an itemstack
     * @param itemStack the itemstack to set the nbt data of
     * @param nbt the nbt data to set
    */
    public static void setNbt(ItemStack itemStack, String key, CompoundTag nbt) {
        //TODO
    }




}

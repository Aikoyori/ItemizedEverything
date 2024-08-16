package xyz.aikoyori.itemized_everything.utils;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class UnobtainableItem extends BlockItem {
    String translationKey;

    public UnobtainableItem(Block block, Settings settings, String translationKey) {
        super(block, settings);
        this.translationKey = translationKey;
    }


    @Override
    public String getTranslationKey(ItemStack stack) {
        return translationKey;
    }
}

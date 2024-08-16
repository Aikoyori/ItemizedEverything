package xyz.aikoyori.itemized_everything.api;

import com.google.common.base.Suppliers;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class ItemizedEverythingBlockItemLists {
    static Map<Block, Item> blocksToItemHashMap = new HashMap<>();
    static Map<Item, Block> itemsToBlockHashMap = new HashMap<>();
    public static ArrayList<Item> itemList = new ArrayList<>();
    /**
     *
     * @param block
     * @return
     */
    @Nullable
    public static Supplier<Item> getItemFromBlock(Block block){
        return Suppliers.memoize(() -> {
            return blocksToItemHashMap.getOrDefault(block,null);
        });
    }

    /**
     *
     * @param item
     * @return
     */
    @Nullable
    public static Supplier<Block> getBlockFromItem(Item item){
        return Suppliers.memoize(() -> {
            return itemsToBlockHashMap.getOrDefault(item,null);
        });
    }

    public static boolean isItemInList(Item item)
    {
        return itemsToBlockHashMap.containsKey(item);
    }
    public static boolean isBlockInList(Block block)
    {
        return blocksToItemHashMap.containsKey(block);
    }
    /**
     * This is used internally
     */
    public static boolean addItemsToBlockLists(Block bl, Item it)
    {
        if(blocksToItemHashMap.containsKey(bl) || itemsToBlockHashMap.containsKey(it)) return false;
        else{
            blocksToItemHashMap.put(bl,it);
            itemsToBlockHashMap.put(it,bl);
            itemList.add(it);
            return true;
        }
    }

}

package xyz.aikoyori.itemized_everything.api;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import xyz.aikoyori.itemized_everything.utils.ItemizedEverythingUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.function.Consumer;
import java.util.function.Function;

public class ItemizedEverythingHook {
    public static Map<Block, PriorityQueue<ItemizedEverythingUtils.Queueable<Consumer<Block>>>> registryModifiers = new HashMap<>();
    public static Map<Block, PriorityQueue<ItemizedEverythingUtils.Queueable<Function<BlockState,BlockState>>>> renderModifiers = new HashMap<>();


    /**
     * Hook for every entries
     * @param block Block to run function on
     * @param consumer Function to run on such block
     * @param priority higher priority runs first (more value run first), same value should relatively run at the same
     *                 time but with some randomization me think
     */
    public static void onAllUnobtainableRegister(Block block, Consumer<Block> consumer,int priority)
    {
        if(!registryModifiers.containsKey(block)){
            registryModifiers.put(block,new PriorityQueue<>(
                    (o1, o2) -> Integer.compare(o2.getPriority(),o1.getPriority()))
            );
        }
        registryModifiers.get(block).add(new ItemizedEverythingUtils.Queueable<>(consumer,priority));
    }

    /**
     * Read {@link ItemizedEverythingHook#onAllUnobtainableRegister(Block, Consumer, int)}
     * @param block
     * @param consumer
     */
    public static void onAllUnobtainableRegister(Block block, Consumer<Block> consumer)
    {
        onAllUnobtainableRegister(block, consumer, 0);
    }

    /**
     * For changing rendering state of a block
     * @param block
     * @param function
     */
    public static void onRenderGetState(Block block, Function<BlockState,BlockState> function, int priority){

        if(!renderModifiers.containsKey(block)){
            renderModifiers.put(block,new PriorityQueue<>(
                    (o1, o2) -> Integer.compare(o2.getPriority(),o1.getPriority()))
            );

        }
        renderModifiers.get(block).add(new ItemizedEverythingUtils.Queueable<>(function,priority));
    }


}

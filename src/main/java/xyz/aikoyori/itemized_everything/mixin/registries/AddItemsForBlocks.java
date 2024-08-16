package xyz.aikoyori.itemized_everything.mixin.registries;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import xyz.aikoyori.itemized_everything.api.ItemizedEverythingBlockItemLists;
import xyz.aikoyori.itemized_everything.utils.ItemizedEverythingUtils;
import xyz.aikoyori.itemized_everything.api.ItemizedEverythingHook;
import xyz.aikoyori.itemized_everything.utils.UnobtainableItem;

@Mixin(Registries.class)
public class AddItemsForBlocks {
    @Inject(method = "bootstrap",
            at=@At(value = "INVOKE",target = "Lnet/minecraft/registry/Registries;freezeRegistries()V",shift = At.Shift.BEFORE),locals = LocalCapture.CAPTURE_FAILSOFT
            ,cancellable = true)
    private static void itemizedeverything$addItems(CallbackInfo ci){
        for(Block block : Registries.BLOCK)
            if((Registries.ITEM.getOrEmpty(Registries.BLOCK.getId(block))).isEmpty())
            {
                Item it = new UnobtainableItem(block,new Item.Settings(),block.getTranslationKey());
                ItemizedEverythingBlockItemLists.addItemsToBlockLists(block,it);
                if(ItemizedEverythingHook.registryModifiers.containsKey(block)){
                    ItemizedEverythingHook.registryModifiers.get(block).forEach(consumerQueueable -> {
                        consumerQueueable.get().accept(block);
                    });
                }
                Registry.register(Registries.ITEM,new Identifier(ItemizedEverythingUtils.getModID(),Registries.BLOCK.getId(block).getNamespace()+"/"+Registries.BLOCK.getId(block).getPath()),it);
            }
    }
}
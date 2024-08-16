package xyz.aikoyori.itemized_everything;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.block.Blocks;
import net.minecraft.block.PowderSnowCauldronBlock;
import net.minecraft.block.RedstoneBlock;
import net.minecraft.block.RedstoneWireBlock;
import net.minecraft.block.enums.WireConnection;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import org.joml.Matrix4f;
import xyz.aikoyori.itemized_everything.api.ItemizedEverythingBlockItemLists;
import xyz.aikoyori.itemized_everything.api.ItemizedEverythingHook;
import xyz.aikoyori.itemized_everything.utils.ItemizedEverythingUtils;

public class ItemizedEverything implements ModInitializer {

    public static final ItemGroup UNOBTAINABLES = FabricItemGroup.builder()
            .icon(Items.BARRIER::getDefaultStack).displayName(Text.translatable("itemGroup.itemized_everything.item_from_blocks"))
            .entries((displayContext, entries) -> {
                for (Item item : ItemizedEverythingBlockItemLists.itemList) {
                    entries.add(item);
                }
            }).build();

    @Override
    public void onInitialize() {
        Registry.register(Registries.ITEM_GROUP, ItemizedEverythingUtils.makeID("item_from_blocks"),UNOBTAINABLES);
        ItemizedEverythingHook.onRenderGetState(Blocks.POWDER_SNOW_CAULDRON,blockState -> {
            blockState = blockState.with(PowderSnowCauldronBlock.LEVEL,3);
            return blockState;
        },0);
    }

}

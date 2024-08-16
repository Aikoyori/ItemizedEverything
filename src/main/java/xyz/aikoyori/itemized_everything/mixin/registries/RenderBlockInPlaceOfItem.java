package xyz.aikoyori.itemized_everything.mixin.registries;

import net.fabricmc.fabric.api.transfer.v1.client.fluid.FluidVariantRendering;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.Blocks;
import net.minecraft.block.FluidBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.render.model.json.Transformation;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RotationAxis;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.aikoyori.itemized_everything.api.ItemizedEverythingBlockItemLists;
import xyz.aikoyori.itemized_everything.api.ItemizedEverythingHook;
import xyz.aikoyori.itemized_everything.mixin.accessors.DrawContextAccessor;
import xyz.aikoyori.itemized_everything.utils.ItemizedEverythingUtils;

import java.util.function.Consumer;
import java.util.function.Function;

@Mixin(ItemRenderer.class)
public class RenderBlockInPlaceOfItem {
    @Shadow @Final private MinecraftClient client;

    @Shadow @Final private BuiltinModelItemRenderer builtinModelItemRenderer;

    @Inject(method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/render/model/BakedModel;)V",
            at=@At("HEAD"),cancellable = true)
    void itemizedEverything$renderBlockInPlaceOfItem(ItemStack stack, ModelTransformationMode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, BakedModel model, CallbackInfo ci){
        if(ItemizedEverythingBlockItemLists.isItemInList(stack.getItem())){
            DrawContext drawContext = DrawContextAccessor.createDrawContext(client, matrices, client.getBufferBuilders().getEntityVertexConsumers());
            Transformation display = ItemizedEverythingUtils.getRotationForModelTransformation(renderMode);
            Identifier identifier = Registries.ITEM.getId(stack.getItem());

            BlockState state = ItemizedEverythingBlockItemLists.getBlockFromItem(stack.getItem()).get().getDefaultState();
            if(ItemizedEverythingHook.renderModifiers.containsKey(state.getBlock())){
                for (ItemizedEverythingUtils.Queueable<Function<BlockState, BlockState>> queueable : ItemizedEverythingHook.renderModifiers.get(state.getBlock())) {
                    state = queueable.get().apply(state);
                }
            }
            //Identifier modelid = new Identifier(identifier.getPath().split("/",2)[0],"block/"+identifier.getPath().split("/",2)[1].split("#",2)[0]);
            matrices.push();
            matrices.push();
            display.apply(leftHanded,matrices);
            matrices.translate(-0.5F, -0.5F, -0.5F);


            if(state.hasBlockEntity()){
                /*
                BlockWithEntity blockWithEntity = (BlockWithEntity) state.getBlock();
                BlockEntity be = blockWithEntity.createBlockEntity(BlockPos.ORIGIN,state);
                if(be != null){
                    client.getBlockEntityRenderDispatcher().render(be, client.getTickDelta(),matrices,vertexConsumers);
                }*/
                matrices.pop();
                matrices.push();
                display.apply(leftHanded,matrices);
                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180));
                matrices.translate(-0.5F, -0.5F, -0.5F);
                builtinModelItemRenderer.render(new ItemStack(state.getBlock()),ModelTransformationMode.FIXED,matrices,vertexConsumers,light,overlay);
            }
            else if (state.getBlock() instanceof FluidBlock)
            {
                matrices.pop();
                matrices.push();
                matrices.scale(1f,-1f,1f);
                //display.apply(leftHanded,matrices);
                FluidVariant variant = FluidVariant.of(state.getBlock().getFluidState(state).getFluid());
                Sprite sprite = FluidVariantRendering.getSprite(variant);
                int color = FluidVariantRendering.getColor(variant);
                float r = ((color >> 16) & 255) / 256f;
                float g = ((color >> 8) & 255) / 256f;
                float b = (color & 255) / 256f;
                matrices.push();
                matrices.translate(-0.5F, -0.5F, 0F);
                matrices.scale(0.06f,0.06f,0.06f);
                drawContext.drawSprite(0,0,0,16,16,sprite,r,g,b,1);
                matrices.pop();
                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180));
                matrices.push();
                matrices.translate(-0.5, -0.5, 0F);
                matrices.scale(0.06f,0.06f,0.06f);
                drawContext.drawSprite(0,0,0,16,16,sprite,r,g,b,1);
                matrices.pop();

            }
            else
            {
                client.getBlockRenderManager().renderBlockAsEntity(
                        state,
                        matrices,vertexConsumers,
                        state.getLuminance() | light,
                        overlay);
            }
            matrices.pop();
            matrices.pop();
            ci.cancel();

        }
    }

}

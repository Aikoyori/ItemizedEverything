package xyz.aikoyori.itemized_everything.mixin.accessors;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(DrawContext.class)
public interface DrawContextAccessor {
    @Invoker("<init>")
    static DrawContext createDrawContext(MinecraftClient client, MatrixStack matrices, VertexConsumerProvider.Immediate vertexConsumers) {
        return null;
    }
}

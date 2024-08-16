package xyz.aikoyori.itemized_everything.mixin.registries;

import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.aikoyori.itemized_everything.utils.ItemizedEverythingUtils;

@Mixin(ModelLoader.class)
public class AddModelsToBlocks {

    @Inject(method="getOrLoadModel",at = @At(value = "HEAD",shift = At.Shift.AFTER),cancellable = true)
    void itemizedeverything$modelOverride(Identifier identifier, CallbackInfoReturnable<UnbakedModel> cir){
        if(ItemizedEverythingUtils.modRandom.nextFloat(1) > 0.999) System.out.println(identifier);
        if(identifier.getNamespace().equals(ItemizedEverythingUtils.getModID())){
            Identifier modelid = new Identifier(identifier.getPath().split("/",2)[0],"block/"+identifier.getPath().split("/",2)[1].split("#",2)[0]);
            System.out.println(modelid);
            cir.setReturnValue(((ModelLoader)(Object)this).getOrLoadModel(modelid));
        }//*/
    }
}
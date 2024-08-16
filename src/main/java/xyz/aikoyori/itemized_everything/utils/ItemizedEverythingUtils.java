package xyz.aikoyori.itemized_everything.utils;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.*;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.render.model.json.Transformation;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.Random;

public class ItemizedEverythingUtils {

    public static Random modRandom = new Random();
    private static String MOD_ID = "itemized_everything";
    public static String getModID(){
        return MOD_ID;
    }

    public static Identifier makeID(String identifier){
        return new Identifier(getModID(),identifier);
    }
    public static class Queueable<T>{
        T obj;
        int priority;

        public Queueable(T obj, int priority) {
            this.obj = obj;
            this.priority = priority;
        }

        public int getPriority() {
            return priority;
        }

        public T get() {
            return obj;
        }
    }
    public static class ModelDisplay{
        Vector3f translation,rotation,scale;
        public ModelDisplay(Vector3f translation,Vector3f rotation, Vector3f scale){
            this.translation = translation;
            this.rotation = rotation;
            this.scale = scale;
        }

        public Vector3f getRotation() {
            return rotation;
        }

        public Vector3f getScale() {
            return scale;
        }

        public Vector3f getTranslation() {
            return translation;
        }
    }



    public static Transformation getRotationForModelTransformation(ModelTransformationMode modelTransformationMode){
        return switch (modelTransformationMode)
        {
            case GUI -> new Transformation(new Vector3f(30,225,0),new Vector3f(0,0,0),new Vector3f(0.625f, 0.625f, 0.625f));
            case GROUND -> new Transformation(new Vector3f(0,0,0),new Vector3f(0, 0, 0),new Vector3f(0.25f, 0.25f, 0.25f));
            case FIXED -> new Transformation(new Vector3f(0,0,0),new Vector3f(0,0,0),new Vector3f(0.5f, 0.5f, 0.5f));
            case THIRD_PERSON_RIGHT_HAND -> new Transformation(new Vector3f(75, 45, 0),new Vector3f(0, 0, 0),new Vector3f(0.375f, 0.375f, 0.375f));
            case FIRST_PERSON_RIGHT_HAND -> new Transformation(new Vector3f(0, 45, 0),new Vector3f( 0, 0, 0),new Vector3f(0.40f, 0.40f, 0.40f ));
            case NONE, HEAD -> new Transformation(new Vector3f(0,0,0),new Vector3f(0,0,0),new Vector3f(1,1,1));
            case THIRD_PERSON_LEFT_HAND -> new Transformation(new Vector3f(75, 225, 0),new Vector3f(0, 0, 0),new Vector3f(0.375f, 0.375f, 0.375f));
            case FIRST_PERSON_LEFT_HAND -> new Transformation(new Vector3f(0,225,0),new Vector3f(0,0,0),new Vector3f(0.40f, 0.40f, 0.40f));
        };
    }
}

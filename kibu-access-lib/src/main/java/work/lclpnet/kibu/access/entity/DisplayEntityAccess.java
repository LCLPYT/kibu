package work.lclpnet.kibu.access.entity;

import com.mojang.math.Transformation;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Brightness;
import net.minecraft.world.entity.Display;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import work.lclpnet.kibu.access.mixin.BlockDisplayAccessor;
import work.lclpnet.kibu.access.mixin.DisplayAccessor;
import work.lclpnet.kibu.access.mixin.ItemDisplayAccessor;
import work.lclpnet.kibu.access.mixin.TextDisplayAccessor;
import work.lclpnet.kibu.access.type.KibuDisplayEntity;

public class DisplayEntityAccess {

    private DisplayEntityAccess() {}

    // BlockDisplayEntity
    public static void setBlockState(Display.BlockDisplay entity, BlockState state) {
        entity.setBlockState(state);
    }

    public static BlockState getBlockState(Display.BlockDisplay entity) {
        return ((BlockDisplayAccessor) entity).invokeGetBlockState();
    }

    // ItemDisplayEntity
    public static void setItemStack(Display.ItemDisplay entity, ItemStack stack) {
        entity.setItemStack(stack);
    }

    public static ItemStack getItemStack(Display.ItemDisplay entity) {
        return ((ItemDisplayAccessor) entity).invokeGetItemStack();
    }

    public static void setTransformationMode(Display.ItemDisplay entity, ItemDisplayContext mode) {
        entity.setItemTransform(mode);
    }

    public static ItemDisplayContext getTransformationMode(Display.ItemDisplay entity) {
        return ((ItemDisplayAccessor) entity).invokeGetItemTransform();
    }

    // TextDisplayEntity
    public static void setText(Display.TextDisplay entity, Component text) {
        entity.setText(text);
    }

    public static Component getText(Display.TextDisplay entity) {
        return ((TextDisplayAccessor) entity).invokeGetText();
    }

    public static void setLineWidth(Display.TextDisplay entity, int lineWidth) {
        entity.setLineWidth(lineWidth);
    }

    public static int getLineWidth(Display.TextDisplay entity) {
        return ((TextDisplayAccessor) entity).invokeGetLineWidth();
    }

    public static void setTextOpacity(Display.TextDisplay entity, byte textOpacity) {
        entity.setTextOpacity(textOpacity);
    }

    public static byte getTextOpacity(Display.TextDisplay entity) {
        return ((TextDisplayAccessor) entity).invokeGetTextOpacity();
    }

    public static void setBackground(Display.TextDisplay entity, int background) {
        entity.setBackgroundColor(background);
    }

    public static int getBackground(Display.TextDisplay entity) {
        return ((TextDisplayAccessor) entity).invokeGetBackgroundColor();
    }

    public static void setDisplayFlags(Display.TextDisplay entity, byte displayFlags) {
        entity.setFlags(displayFlags);
    }

    public static byte getDisplayFlags(Display.TextDisplay entity) {
        return ((TextDisplayAccessor) entity).invokeGetFlags();
    }

    // common DisplayEntity
    public static void setTransformation(Display entity, Transformation transformation) {
        entity.setTransformation(transformation);
    }

    public static Transformation getTransformation(Display entity) {
        return DisplayAccessor.invokeCreateTransformation(entity.getEntityData());
    }

    public static void setInterpolationDuration(Display entity, int interpolationDuration) {
        entity.setTransformationInterpolationDuration(interpolationDuration);
    }

    public static int getInterpolationDuration(Display entity) {
        return ((DisplayAccessor) entity).invokeGetTransformationInterpolationDuration();
    }

    public static void setStartInterpolation(Display entity, int startDuration) {
        entity.setTransformationInterpolationDelay(startDuration);
    }

    public static int getStartInterpolation(Display entity) {
        return ((DisplayAccessor) entity).invokeGetTransformationInterpolationDelay();
    }

    public static void setBillboardMode(Display entity, Display.BillboardConstraints billboardMode) {
        entity.setBillboardConstraints(billboardMode);
    }

    public static Display.BillboardConstraints getBillboardMode(Display entity) {
        return ((DisplayAccessor) entity).invokeGetBillboardConstraints();
    }

    public static void setBrightness(Display entity, Brightness brightness) {
        entity.setBrightnessOverride(brightness);
    }

    public static int getBrightness(Display entity) {
        return ((DisplayAccessor) entity).invokeGetPackedBrightnessOverride();
    }

    public static void setViewRange(Display entity, float viewRange) {
        entity.setViewRange(viewRange);
    }

    public static float getViewRange(Display entity) {
        return ((DisplayAccessor) entity).invokeGetViewRange();
    }

    public static void setShadowRadius(Display entity, float shadowRadius) {
        entity.setShadowRadius(shadowRadius);
    }

    public static float getShadowRadius(Display entity) {
        return ((DisplayAccessor) entity).invokeGetShadowRadius();
    }

    public static void setShadowStrength(Display entity, float shadowStrength) {
        entity.setShadowStrength(shadowStrength);
    }

    public static float getShadowStrength(Display entity) {
        return ((DisplayAccessor) entity).invokeGetShadowStrength();
    }

    public static void setDisplayWidth(Display entity, float displayWidth) {
        entity.setWidth(displayWidth);
    }

    public static float getDisplayWidth(Display entity) {
        return ((DisplayAccessor) entity).invokeGetWidth();
    }

    public static void setDisplayHeight(Display entity, float displayHeight) {
        entity.setHeight(displayHeight);
    }

    public static float getDisplayHeight(Display entity) {
        return ((DisplayAccessor) entity).invokeGetHeight();
    }

    public static void setGlowColorOverride(Display entity, int glowColorOverride) {
        entity.setGlowColorOverride(glowColorOverride);
    }

    public static int getGlowColorOverride(Display entity) {
        return ((DisplayAccessor) entity).invokeGetGlowColorOverride();
    }

    public static void setTranslation(Display entity, Vector3f translation) {
        ((KibuDisplayEntity) entity).kibu$setTranslation(translation);
    }

    public static Vector3f getTranslation(Display entity) {
        return ((KibuDisplayEntity) entity).kibu$getTranslation();
    }

    public static void setLeftRotation(Display entity, Quaternionf leftRotation) {
        ((KibuDisplayEntity) entity).kibu$setLeftRotation(leftRotation);
    }

    public static Quaternionf getLeftRotation(Display entity) {
        return ((KibuDisplayEntity) entity).kibu$getLeftRotation();
    }

    public static void setScale(Display entity, Vector3f scale) {
        ((KibuDisplayEntity) entity).kibu$setScale(scale);
    }

    public static Vector3f getScale(Display entity) {
        return ((KibuDisplayEntity) entity).kibu$getScale();
    }

    public static void setRightRotation(Display entity, Quaternionf rightRotation) {
        ((KibuDisplayEntity) entity).kibu$setRightRotation(rightRotation);
    }

    public static Quaternionf getRightRotation(Display entity) {
        return ((KibuDisplayEntity) entity).kibu$getRightRotation();
    }
}

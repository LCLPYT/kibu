package work.lclpnet.kibu.access.entity;

import net.minecraft.block.BlockState;
import net.minecraft.entity.decoration.Brightness;
import net.minecraft.entity.decoration.DisplayEntity;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.AffineTransformation;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import work.lclpnet.kibu.access.mixin.BlockDisplayEntityAccessor;
import work.lclpnet.kibu.access.mixin.DisplayEntityAccessor;
import work.lclpnet.kibu.access.mixin.ItemDisplayEntityAccessor;
import work.lclpnet.kibu.access.mixin.TextDisplayEntityAccessor;
import work.lclpnet.kibu.access.type.KibuDisplayEntity;

public class DisplayEntityAccess {

    private DisplayEntityAccess() {}

    // BlockDisplayEntity
    public static void setBlockState(DisplayEntity.BlockDisplayEntity entity, BlockState state) {
        entity.setBlockState(state);
    }

    public static BlockState getBlockState(DisplayEntity.BlockDisplayEntity entity) {
        return ((BlockDisplayEntityAccessor) entity).invokeGetBlockState();
    }

    // ItemDisplayEntity
    public static void setItemStack(DisplayEntity.ItemDisplayEntity entity, ItemStack stack) {
        entity.setItemStack(stack);
    }

    public static ItemStack getItemStack(DisplayEntity.ItemDisplayEntity entity) {
        return ((ItemDisplayEntityAccessor) entity).invokeGetItemStack();
    }

    public static void setTransformationMode(DisplayEntity.ItemDisplayEntity entity, ItemDisplayContext mode) {
        entity.setItemDisplayContext(mode);
    }

    public static ItemDisplayContext getTransformationMode(DisplayEntity.ItemDisplayEntity entity) {
        return ((ItemDisplayEntityAccessor) entity).invokeGetItemDisplayContext();
    }

    // TextDisplayEntity
    public static void setText(DisplayEntity.TextDisplayEntity entity, Text text) {
        entity.setText(text);
    }

    public static Text getText(DisplayEntity.TextDisplayEntity entity) {
        return ((TextDisplayEntityAccessor) entity).invokeGetText();
    }

    public static void setLineWidth(DisplayEntity.TextDisplayEntity entity, int lineWidth) {
        entity.setLineWidth(lineWidth);
    }

    public static int getLineWidth(DisplayEntity.TextDisplayEntity entity) {
        return ((TextDisplayEntityAccessor) entity).invokeGetLineWidth();
    }

    public static void setTextOpacity(DisplayEntity.TextDisplayEntity entity, byte textOpacity) {
        entity.setTextOpacity(textOpacity);
    }

    public static byte getTextOpacity(DisplayEntity.TextDisplayEntity entity) {
        return ((TextDisplayEntityAccessor) entity).invokeGetTextOpacity();
    }

    public static void setBackground(DisplayEntity.TextDisplayEntity entity, int background) {
        entity.setBackground(background);
    }

    public static int getBackground(DisplayEntity.TextDisplayEntity entity) {
        return ((TextDisplayEntityAccessor) entity).invokeGetBackground();
    }

    public static void setDisplayFlags(DisplayEntity.TextDisplayEntity entity, byte displayFlags) {
        entity.setDisplayFlags(displayFlags);
    }

    public static byte getDisplayFlags(DisplayEntity.TextDisplayEntity entity) {
        return ((TextDisplayEntityAccessor) entity).invokeGetDisplayFlags();
    }

    // common DisplayEntity
    public static void setTransformation(DisplayEntity entity, AffineTransformation transformation) {
        entity.setTransformation(transformation);
    }

    public static AffineTransformation getTransformation(DisplayEntity entity) {
        return DisplayEntityAccessor.invokeGetTransformation(entity.getDataTracker());
    }

    public static void setInterpolationDuration(DisplayEntity entity, int interpolationDuration) {
        entity.setInterpolationDuration(interpolationDuration);
    }

    public static int getInterpolationDuration(DisplayEntity entity) {
        return ((DisplayEntityAccessor) entity).invokeGetInterpolationDuration();
    }

    public static void setStartInterpolation(DisplayEntity entity, int startDuration) {
        entity.setStartInterpolation(startDuration);
    }

    public static int getStartInterpolation(DisplayEntity entity) {
        return ((DisplayEntityAccessor) entity).invokeGetStartInterpolation();
    }

    public static void setBillboardMode(DisplayEntity entity, DisplayEntity.BillboardMode billboardMode) {
        entity.setBillboardMode(billboardMode);
    }

    public static DisplayEntity.BillboardMode getBillboardMode(DisplayEntity entity) {
        return ((DisplayEntityAccessor) entity).invokeGetBillboardMode();
    }

    public static void setBrightness(DisplayEntity entity, Brightness brightness) {
        entity.setBrightness(brightness);
    }

    public static int getBrightness(DisplayEntity entity) {
        return ((DisplayEntityAccessor) entity).invokeGetBrightness();
    }

    public static void setViewRange(DisplayEntity entity, float viewRange) {
        entity.setViewRange(viewRange);
    }

    public static float getViewRange(DisplayEntity entity) {
        return ((DisplayEntityAccessor) entity).invokeGetViewRange();
    }

    public static void setShadowRadius(DisplayEntity entity, float shadowRadius) {
        entity.setShadowRadius(shadowRadius);
    }

    public static float getShadowRadius(DisplayEntity entity) {
        return ((DisplayEntityAccessor) entity).invokeGetShadowRadius();
    }

    public static void setShadowStrength(DisplayEntity entity, float shadowStrength) {
        entity.setShadowStrength(shadowStrength);
    }

    public static float getShadowStrength(DisplayEntity entity) {
        return ((DisplayEntityAccessor) entity).invokeGetShadowStrength();
    }

    public static void setDisplayWidth(DisplayEntity entity, float displayWidth) {
        entity.setDisplayWidth(displayWidth);
    }

    public static float getDisplayWidth(DisplayEntity entity) {
        return ((DisplayEntityAccessor) entity).invokeGetDisplayWidth();
    }

    public static void setDisplayHeight(DisplayEntity entity, float displayHeight) {
        entity.setDisplayHeight(displayHeight);
    }

    public static float getDisplayHeight(DisplayEntity entity) {
        return ((DisplayEntityAccessor) entity).invokeGetDisplayHeight();
    }

    public static void setGlowColorOverride(DisplayEntity entity, int glowColorOverride) {
        entity.setGlowColorOverride(glowColorOverride);
    }

    public static int getGlowColorOverride(DisplayEntity entity) {
        return ((DisplayEntityAccessor) entity).invokeGetGlowColorOverride();
    }

    public static void setTranslation(DisplayEntity entity, Vector3f translation) {
        ((KibuDisplayEntity) entity).kibu$setTranslation(translation);
    }

    public static Vector3f getTranslation(DisplayEntity entity) {
        return ((KibuDisplayEntity) entity).kibu$getTranslation();
    }

    public static void setLeftRotation(DisplayEntity entity, Quaternionf leftRotation) {
        ((KibuDisplayEntity) entity).kibu$setLeftRotation(leftRotation);
    }

    public static Quaternionf getLeftRotation(DisplayEntity entity) {
        return ((KibuDisplayEntity) entity).kibu$getLeftRotation();
    }

    public static void setScale(DisplayEntity entity, Vector3f scale) {
        ((KibuDisplayEntity) entity).kibu$setScale(scale);
    }

    public static Vector3f getScale(DisplayEntity entity) {
        return ((KibuDisplayEntity) entity).kibu$getScale();
    }

    public static void setRightRotation(DisplayEntity entity, Quaternionf rightRotation) {
        ((KibuDisplayEntity) entity).kibu$setRightRotation(rightRotation);
    }

    public static Quaternionf getRightRotation(DisplayEntity entity) {
        return ((KibuDisplayEntity) entity).kibu$getRightRotation();
    }
}

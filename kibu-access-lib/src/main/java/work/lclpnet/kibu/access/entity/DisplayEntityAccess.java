package work.lclpnet.kibu.access.entity;

import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.entity.decoration.Brightness;
import net.minecraft.entity.decoration.DisplayEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.AffineTransformation;
import work.lclpnet.kibu.access.mixin.BlockDisplayEntityAccessor;
import work.lclpnet.kibu.access.mixin.DisplayEntityAccessor;
import work.lclpnet.kibu.access.mixin.ItemDisplayEntityAccessor;
import work.lclpnet.kibu.access.mixin.TextDisplayEntityAccessor;

public class DisplayEntityAccess {

    private DisplayEntityAccess() {}

    // BlockDisplayEntity
    public static void setBlockState(DisplayEntity.BlockDisplayEntity entity, BlockState state) {
        ((BlockDisplayEntityAccessor) entity).invokeSetBlockState(state);
    }

    public static BlockState getBlockState(DisplayEntity.BlockDisplayEntity entity) {
        return ((BlockDisplayEntityAccessor) entity).invokeGetBlockState();
    }

    // ItemDisplayEntity
    public static void setItemStack(DisplayEntity.ItemDisplayEntity entity, ItemStack stack) {
        ((ItemDisplayEntityAccessor) entity).invokeSetItemStack(stack);
    }

    public static ItemStack getItemStack(DisplayEntity.ItemDisplayEntity entity) {
        return ((ItemDisplayEntityAccessor) entity).invokeGetItemStack();
    }

    public static void setTransformationMode(DisplayEntity.ItemDisplayEntity entity, ModelTransformationMode mode) {
        ((ItemDisplayEntityAccessor) entity).invokeSetTransformationMode(mode);
    }

    public static ModelTransformationMode getTransformationMode(DisplayEntity.ItemDisplayEntity entity) {
        return ((ItemDisplayEntityAccessor) entity).invokeGetTransformationMode();
    }

    // TextDisplayEntity
    public static void setText(DisplayEntity.TextDisplayEntity entity, Text text) {
        ((TextDisplayEntityAccessor) entity).invokeSetText(text);
    }

    public static Text getText(DisplayEntity.TextDisplayEntity entity) {
        return ((TextDisplayEntityAccessor) entity).invokeGetText();
    }

    public static void setLineWidth(DisplayEntity.TextDisplayEntity entity, int lineWidth) {
        ((TextDisplayEntityAccessor) entity).invokeSetLineWidth(lineWidth);
    }

    public static int getLineWidth(DisplayEntity.TextDisplayEntity entity) {
        return ((TextDisplayEntityAccessor) entity).invokeGetLineWidth();
    }

    public static void setTextOpacity(DisplayEntity.TextDisplayEntity entity, byte textOpacity) {
        ((TextDisplayEntityAccessor) entity).invokeSetTextOpacity(textOpacity);
    }

    public static byte getTextOpacity(DisplayEntity.TextDisplayEntity entity) {
        return ((TextDisplayEntityAccessor) entity).invokeGetTextOpacity();
    }

    public static void setBackground(DisplayEntity.TextDisplayEntity entity, int background) {
        ((TextDisplayEntityAccessor) entity).invokeSetBackground(background);
    }

    public static int getBackground(DisplayEntity.TextDisplayEntity entity) {
        return ((TextDisplayEntityAccessor) entity).invokeGetBackground();
    }

    public static void setDisplayFlags(DisplayEntity.TextDisplayEntity entity, byte displayFlags) {
        ((TextDisplayEntityAccessor) entity).invokeSetDisplayFlags(displayFlags);
    }

    public static byte getDisplayFlags(DisplayEntity.TextDisplayEntity entity) {
        return ((TextDisplayEntityAccessor) entity).invokeGetDisplayFlags();
    }

    // common DisplayEntity
    public static void setTransformation(DisplayEntity entity, AffineTransformation transformation) {
        ((DisplayEntityAccessor) entity).invokeSetTransformation(transformation);
    }

    public static AffineTransformation getTransformation(DisplayEntity entity) {
        return DisplayEntityAccessor.invokeGetTransformation(entity.getDataTracker());
    }

    public static void setInterpolationDuration(DisplayEntity entity, int interpolationDuration) {
        ((DisplayEntityAccessor) entity).invokeSetInterpolationDuration(interpolationDuration);
    }

    public static int getInterpolationDuration(DisplayEntity entity) {
        return ((DisplayEntityAccessor) entity).invokeGetInterpolationDuration();
    }

    public static void setStartInterpolation(DisplayEntity entity, int startDuration) {
        ((DisplayEntityAccessor) entity).invokeSetStartInterpolation(startDuration);
    }

    public static int getStartInterpolation(DisplayEntity entity) {
        return ((DisplayEntityAccessor) entity).invokeGetStartInterpolation();
    }

    public static void setBillboardMode(DisplayEntity entity, DisplayEntity.BillboardMode billboardMode) {
        ((DisplayEntityAccessor) entity).invokeSetBillboardMode(billboardMode);
    }

    public static DisplayEntity.BillboardMode getBillboardMode(DisplayEntity entity) {
        return ((DisplayEntityAccessor) entity).invokeGetBillboardMode();
    }

    public static void setBrightness(DisplayEntity entity, Brightness brightness) {
        ((DisplayEntityAccessor) entity).invokeSetBrightness(brightness);
    }

    public static int getBrightness(DisplayEntity entity) {
        return ((DisplayEntityAccessor) entity).invokeGetBrightness();
    }

    public static void setViewRange(DisplayEntity entity, float viewRange) {
        ((DisplayEntityAccessor) entity).invokeSetViewRange(viewRange);
    }

    public static float getViewRange(DisplayEntity entity) {
        return ((DisplayEntityAccessor) entity).invokeGetViewRange();
    }

    public static void setShadowRadius(DisplayEntity entity, float shadowRadius) {
        ((DisplayEntityAccessor) entity).invokeSetShadowRadius(shadowRadius);
    }

    public static float getShadowRadius(DisplayEntity entity) {
        return ((DisplayEntityAccessor) entity).invokeGetShadowRadius();
    }

    public static void setShadowStrength(DisplayEntity entity, float shadowStrength) {
        ((DisplayEntityAccessor) entity).invokeSetShadowStrength(shadowStrength);
    }

    public static float getShadowStrength(DisplayEntity entity) {
        return ((DisplayEntityAccessor) entity).invokeGetShadowStrength();
    }

    public static void setDisplayWidth(DisplayEntity entity, float displayWidth) {
        ((DisplayEntityAccessor) entity).invokeSetDisplayWidth(displayWidth);
    }

    public static float getDisplayWidth(DisplayEntity entity) {
        return ((DisplayEntityAccessor) entity).invokeGetDisplayWidth();
    }

    public static void setDisplayHeight(DisplayEntity entity, float displayHeight) {
        ((DisplayEntityAccessor) entity).invokeSetDisplayHeight(displayHeight);
    }

    public static float getDisplayHeight(DisplayEntity entity) {
        return ((DisplayEntityAccessor) entity).invokeGetDisplayHeight();
    }

    public static void setGlowColorOverride(DisplayEntity entity, int glowColorOverride) {
        ((DisplayEntityAccessor) entity).invokeSetGlowColorOverride(glowColorOverride);
    }

    public static int getGlowColorOverride(DisplayEntity entity) {
        return ((DisplayEntityAccessor) entity).invokeGetGlowColorOverride();
    }
}

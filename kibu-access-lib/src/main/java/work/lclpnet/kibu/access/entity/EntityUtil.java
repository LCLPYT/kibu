package work.lclpnet.kibu.access.entity;

import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class EntityUtil {

    private EntityUtil() {}

    public static void setAttribute(LivingEntity entity, Holder<Attribute> attribute, double value) {
        AttributeInstance instance = entity.getAttribute(attribute);

        if (instance != null) {
            instance.setBaseValue(value);
        }
    }

    public static void resetAttribute(LivingEntity entity, Holder<Attribute> attribute) {
        if (attribute == Attributes.MOVEMENT_SPEED && entity instanceof ServerPlayer player) {
            setAttribute(player, attribute, player.getAbilities().getWalkingSpeed());
            return;
        }

        setAttribute(entity, attribute, attribute.value().getDefaultValue());
    }

    public static void addAttributeModifier(LivingEntity entity, Holder<Attribute> attribute, Identifier id, double value, AttributeModifier.Operation operation) {
        AttributeInstance instance = entity.getAttribute(attribute);

        if (instance == null || instance.hasModifier(id)) return;

        instance.addTransientModifier(new AttributeModifier(id, value, operation));
    }

    public static void removeAttributeModifier(LivingEntity entity, Holder<Attribute> attribute, Identifier id) {
        AttributeInstance instance = entity.getAttribute(attribute);

        if (instance == null) return;

        instance.removeModifier(id);
    }
}

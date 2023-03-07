package work.lclpnet.kibu.hook.entity;

import net.fabricmc.fabric.api.event.player.*;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.TypedActionResult;
import work.lclpnet.mplugins.hook.Hook;
import work.lclpnet.mplugins.hook.HookFactory;

/**
 * Hook versions of player interaction events from fabric-events-interaction.
 */
public class PlayerInteractionHooks {

    public static final Hook<AttackEntityCallback> ATTACK_ENTITY = HookFactory.createArrayBacked(AttackEntityCallback.class,
            (listeners) -> (player, world, hand, entity, hitResult) -> {
                for (AttackEntityCallback event : listeners) {
                    ActionResult result = event.interact(player, world, hand, entity, hitResult);

                    if (result != ActionResult.PASS) {
                        return result;
                    }
                }

                return ActionResult.PASS;
            }
    );

    public static final Hook<AttackBlockCallback> ATTACK_BLOCK = HookFactory.createArrayBacked(AttackBlockCallback.class,
            (listeners) -> (player, world, hand, pos, direction) -> {
                for (AttackBlockCallback event : listeners) {
                    ActionResult result = event.interact(player, world, hand, pos, direction);

                    if (result != ActionResult.PASS) {
                        return result;
                    }
                }

                return ActionResult.PASS;
            }
    );

    public static final Hook<PlayerBlockBreakEvents.Before> BREAK_BLOCK = HookFactory.createArrayBacked(PlayerBlockBreakEvents.Before.class,
            (listeners) -> (world, player, pos, state, entity) -> {
                for (PlayerBlockBreakEvents.Before event : listeners) {
                    boolean result = event.beforeBlockBreak(world, player, pos, state, entity);

                    if (!result) {
                        return false;
                    }
                }

                return true;
            }
    );

    public static final Hook<UseBlockCallback> USE_BLOCK = HookFactory.createArrayBacked(UseBlockCallback.class,
            (listeners) -> (player, world, hand, hitResult) -> {
                for (UseBlockCallback event : listeners) {
                    ActionResult result = event.interact(player, world, hand, hitResult);

                    if (result != ActionResult.PASS) {
                        return result;
                    }
                }

                return ActionResult.PASS;
            }
    );

    public static final Hook<UseEntityCallback> USE_ENTITY = HookFactory.createArrayBacked(UseEntityCallback.class,
            (listeners) -> (player, world, hand, entity, hitResult) -> {
                for (UseEntityCallback event : listeners) {
                    ActionResult result = event.interact(player, world, hand, entity, hitResult);

                    if (result != ActionResult.PASS) {
                        return result;
                    }
                }

                return ActionResult.PASS;
            }
    );

    public static final Hook<UseItemCallback> USE_ITEM = HookFactory.createArrayBacked(UseItemCallback.class,
            listeners -> (player, world, hand) -> {
                for (UseItemCallback event : listeners) {
                    TypedActionResult<ItemStack> result = event.interact(player, world, hand);

                    if (result.getResult() != ActionResult.PASS) {
                        return result;
                    }
                }

                return TypedActionResult.pass(ItemStack.EMPTY);
            }
    );

    static {
        AttackEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> ATTACK_ENTITY.invoker().interact(player, world, hand, entity, hitResult));
        AttackBlockCallback.EVENT.register((player, world, hand, pos, direction) -> ATTACK_BLOCK.invoker().interact(player, world, hand, pos, direction));
        PlayerBlockBreakEvents.BEFORE.register((world, player, pos, state, blockEntity) -> BREAK_BLOCK.invoker().beforeBlockBreak(world, player, pos, state, blockEntity));
        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> USE_BLOCK.invoker().interact(player, world, hand, hitResult));
        UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> USE_ENTITY.invoker().interact(player, world, hand, entity, hitResult));
        UseItemCallback.EVENT.register((player, world, hand) -> USE_ITEM.invoker().interact(player, world, hand));
    }
}

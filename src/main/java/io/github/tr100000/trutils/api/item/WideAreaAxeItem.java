package io.github.tr100000.trutils.api.item;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.context.UseOnContext;

public class WideAreaAxeItem extends AxeItem implements WideAreaTool {
    public final int breakRadius;
    public final int depth;

    public WideAreaAxeItem(ToolMaterial material, float attackDamage, float attackSpeed, Item.Properties settings, int breakRadius) {
        this(material, attackDamage, attackSpeed, settings, breakRadius, 1);
    }

    public WideAreaAxeItem(ToolMaterial material, float attackDamage, float attackSpeed, Item.Properties settings, int breakRadius, int depth) {
        super(material, attackDamage, attackSpeed, settings);
        this.breakRadius = breakRadius;
        this.depth = depth;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (playerHasBlockingItemUseIntent(context)) return InteractionResult.PASS;
        return useOnAllBlocks(context, super::useOn);
    }

    @Override
    public int getBreakRadius(ItemStack stack) {
        return breakRadius;
    }

    @Override
    public int getDepth(ItemStack stack) {
        return depth;
    }
}

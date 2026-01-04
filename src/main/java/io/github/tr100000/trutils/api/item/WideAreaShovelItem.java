package io.github.tr100000.trutils.api.item;

import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.context.UseOnContext;

public class WideAreaShovelItem extends ShovelItem implements WideAreaTool {
    public final int breakRadius;
    public final int depth;

    public WideAreaShovelItem(ToolMaterial material, float attackDamage, float attackSpeed, Item.Properties settings, int breakRadius) {
        this(material, attackDamage, attackSpeed, settings, breakRadius, 1);
    }

    public WideAreaShovelItem(ToolMaterial material, float attackDamage, float attackSpeed, Item.Properties settings, int breakRadius, int depth) {
        super(material, attackDamage, attackSpeed, settings);
        this.breakRadius = breakRadius;
        this.depth = depth;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (context.getClickedFace() == Direction.DOWN) return InteractionResult.PASS;
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

package io.github.tr100000.trutils.api.gui;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

public record ItemIcon(ItemStack stack) implements Icon {
    public static final MapCodec<ItemIcon> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    ItemStack.CODEC.fieldOf("item").forGetter(ItemIcon::stack)
            ).apply(instance, ItemIcon::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, ItemIcon> PACKET_CODEC = ItemStack.STREAM_CODEC.map(
            ItemIcon::new,
            ItemIcon::stack
    );

    public static final IconType<ItemIcon> TYPE = new IconType<>("item", CODEC, PACKET_CODEC);

    public ItemIcon(Item item) {
        this(new ItemStack(item));
    }

    public ItemIcon(ItemLike item) {
        this(new ItemStack(item));
    }

    @Override
    public IconType<?> getType() {
        return TYPE;
    }
}

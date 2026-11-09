package com.takoy3466.infChestReborn.core.original;

import com.takoy3466.infChestReborn.core.InfChestConfig;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionContents;

import java.util.Set;

/**
 * チェストへ入れられるアイテムと、在庫へ合流できるアイテムの判定規則。
 */
public final class InfChestItemRules {

    private static final Set<String> UNIFICATION_NAMESPACES = Set.of("c", "forge");

    /**
     * 鉱石辞書の代替として扱う共通タグの分類。
     * {@code c:foods/fruit} や {@code c:dyed/red} のように別物を束ねるタグで合流させないよう、
     * 素材名で一意に定まる分類だけを許可している。
     */
    private static final Set<String> UNIFICATION_CATEGORIES = Set.of("ingots", "nuggets", "dusts", "gems", "ores", "raw_materials", "storage_blocks", "dyes");

    private InfChestItemRules() {
    }

    /**
     * プレイヤー操作で格納できるかを返す。
     *
     * <p>元 Mod の「NBT を持たず、耐久も減っていない」という条件を、1.21.1 のコンポーネント差分で表現している。
     * 当時メタデータで区別されていたポーションだけは現在コンポーネントで表現されるため、例外として許可する。
     */
    public static boolean isStorable(ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }
        DataComponentPatch patch = stack.getComponentsPatch();
        return patch.isEmpty() || isPlainPotion(stack, patch);
    }

    /**
     * {@code stack} が在庫 {@code stored} と同じ物として合流できるかを返す。
     */
    public static boolean isSameContents(ItemStack stored, ItemStack stack) {
        return ItemStack.isSameItemSameComponents(stored, stack) || (InfChestConfig.isOreNameConvertEnabled() && sharesUnificationTag(stored, stack));
    }

    /**
     * {@code tag} が素材の同一性を表す共通タグ（例: {@code c:ingots/copper}）かを返す。
     */
    static boolean isUnificationTag(ResourceLocation tag) {
        if (!UNIFICATION_NAMESPACES.contains(tag.getNamespace())) {
            return false;
        }
        String path = tag.getPath();
        int separator = path.indexOf('/');
        return separator > 0 && UNIFICATION_CATEGORIES.contains(path.substring(0, separator));
    }

    private static boolean isPlainPotion(ItemStack stack, DataComponentPatch patch) {
        PotionContents contents = stack.get(DataComponents.POTION_CONTENTS);
        return patch.size() == 1 && contents != null && contents.potion().isPresent() && contents.customColor().isEmpty() && contents.customEffects().isEmpty();
    }

    private static boolean sharesUnificationTag(ItemStack stored, ItemStack stack) {
        return stored.getTags().filter(InfChestItemRules::isUnificationTag).anyMatch(stack::is);
    }

    private static boolean isUnificationTag(TagKey<Item> tag) {
        return isUnificationTag(tag.location());
    }
}

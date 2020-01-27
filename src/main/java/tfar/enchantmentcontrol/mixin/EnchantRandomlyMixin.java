package tfar.enchantmentcontrol.mixin;

import com.google.common.collect.Lists;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.functions.EnchantRandomly;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tfar.enchantmentcontrol.EnchantmentControl;

import java.util.List;
import java.util.Random;

@Mixin(EnchantRandomly.class)
public class EnchantRandomlyMixin {

  @Shadow @Final private List<Enchantment> enchantments;

  @Shadow @Final private static Logger LOGGER;

  @Inject(method = "doApply",at = @At("RETURN"),cancellable = true)
  private void filter(ItemStack stack, LootContext context, CallbackInfoReturnable<ItemStack> callback){
    callback.setReturnValue(no(stack, context));
  }

  public ItemStack no(ItemStack stack,LootContext context){
    stack = removeEnchantments(stack);
    Random random = context.getRandom();
    Enchantment enchantment;
    if (this.enchantments.isEmpty()) {
      List<Enchantment> list = Lists.newArrayList();

      for (Enchantment enchantment1 : Registry.ENCHANTMENT) {
        if (!EnchantmentControl.blackisted_from_loot_tables.contains(enchantment1) &&
                (stack.getItem() == Items.BOOK || enchantment1.canApply(stack)))
          list.add(enchantment1);
      }

      if (list.isEmpty()) { LOGGER.warn("Couldn't find a compatible enchantment for {}", stack);
        return stack;
      }

      enchantment = list.get(random.nextInt(list.size()));
    } else {
      enchantment = this.enchantments.get(random.nextInt(this.enchantments.size()));
    }

    int i = MathHelper.nextInt(random, enchantment.getMinLevel(), enchantment.getMaxLevel());
    if (stack.getItem() == Items.BOOK) {
      stack = new ItemStack(Items.ENCHANTED_BOOK);
      EnchantedBookItem.addEnchantment(stack, new EnchantmentData(enchantment, i));
    } else {
      stack.addEnchantment(enchantment, i);
    }
    return stack;
  }

  private ItemStack removeEnchantments(ItemStack stack) {
    ItemStack itemstack = stack.copy();
    itemstack.removeChildTag("Enchantments");
    itemstack.removeChildTag("StoredEnchantments");

    itemstack.setRepairCost(0);
    if (itemstack.getItem() == Items.ENCHANTED_BOOK) {
      itemstack = new ItemStack(Items.BOOK);
      if (stack.hasDisplayName()) {
        itemstack.setDisplayName(stack.getDisplayName());
      }
    }

    return itemstack;
  }
}

package tfar.enchantmentcontrol.mixin;

import net.minecraft.enchantment.Enchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import tfar.enchantmentcontrol.EnchantmentControl;

@Mixin(targets = "net.minecraft.entity.merchant.villager.VillagerTrades$EnchantedBookForEmeraldsTrade")
public class EnchantedBooksForEmeraldsTradeMixin {

  @ModifyVariable(method = "getOffer(Lnet/minecraft/entity/Entity;Ljava/util/Random;)Lnet/minecraft/item/MerchantOffer;",at = @At(value = "INVOKE",
          target = "Lnet/minecraft/util/math/MathHelper;nextInt(Ljava/util/Random;II)I"))
  private Enchantment filter(Enchantment old){
  	return EnchantmentControl.filteredRandomTrade();
  }
}

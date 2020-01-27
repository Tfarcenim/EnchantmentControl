package tfar.enchantmentcontrol.mixin;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.util.registry.Registry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import tfar.enchantmentcontrol.EnchantmentControl;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Mixin(targets = "net.minecraft.entity.merchant.villager.VillagerTrades$EnchantedBookForEmeraldsTrade")
public class EnchantedBooksForEmeraldsTradeMixin {
  @Redirect(method = "getOffer",at = @At(value = "INVOKE",
          target = "net/minecraft/util/registry/Registry.getRandom(Ljava/util/Random;)Ljava/lang/Object;"))
  private Object filter(Registry<Enchantment> registry,Random rand){

    List<Enchantment> enchantmentList = Registry.ENCHANTMENT.stream().filter(e ->
            !EnchantmentControl.blackisted_from_trade.contains(e)).collect(Collectors.toList());

    return enchantmentList.get(rand.nextInt(enchantmentList.size()));
  }
}

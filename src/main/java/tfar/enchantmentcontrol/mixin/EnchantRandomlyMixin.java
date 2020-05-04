package tfar.enchantmentcontrol.mixin;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.world.storage.loot.conditions.ILootCondition;
import net.minecraft.world.storage.loot.functions.EnchantRandomly;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tfar.enchantmentcontrol.EnchantmentControl;

import java.util.Collection;
import java.util.List;

@Mixin(EnchantRandomly.class)
public class EnchantRandomlyMixin {

  @Mutable @Shadow @Final private List<Enchantment> enchantments;

  @Shadow @Final private static Logger LOGGER;
  @Inject(method = "<init>",at = @At("RETURN"))
  private void filter(ILootCondition[] p_i51238_1_, Collection<Enchantment> p_i51238_2_, CallbackInfo ci){
    enchantments = EnchantmentControl.filteredRandomLoot(p_i51238_2_);
  }
}

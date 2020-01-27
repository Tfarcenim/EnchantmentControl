package tfar.enchantmentcontrol;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

public class ModConfigs {

  public static final ServerConfig SERVER;
  public static final ForgeConfigSpec SERVER_SPEC;

  public ModConfigs(){
    FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onConfigChanged);
  }

  static {
    final Pair<ServerConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(ServerConfig::new);
    SERVER_SPEC = specPair.getRight();
    SERVER = specPair.getLeft();
  }


  public static class ServerConfig {

    public static ForgeConfigSpec.ConfigValue<List<?extends String>> blacklisted_from_trade;

    public static ForgeConfigSpec.ConfigValue<List<?extends String>> blacklisted_from_loot_tables;


    ServerConfig(ForgeConfigSpec.Builder builder) {
      builder.push("general");

      blacklisted_from_trade = builder
              .comment("enchantments blacklisted from villager trades")
              .defineList("blacklisted_from_trade", new ArrayList<>(), String.class::isInstance);
      blacklisted_from_loot_tables = builder
              .comment("enchantments blacklisted from loot tables")
              .defineList("blacklisted_from_loot_tables", new ArrayList<>(), String.class::isInstance);
      builder.pop();
    }
  }

  @SubscribeEvent
  public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
    if (!event.getModID().equals(EnchantmentControl.MODID)) {
      return;
    }
    EnchantmentControl.blackisted_from_loot_tables.clear();
    ServerConfig.blacklisted_from_loot_tables.get().forEach(
            o -> EnchantmentControl.blackisted_from_loot_tables
                    .add(ForgeRegistries.ENCHANTMENTS.getValue(new ResourceLocation(o))));

    EnchantmentControl.blackisted_from_trade.clear();
    ServerConfig.blacklisted_from_trade.get().forEach(
            o -> EnchantmentControl.blackisted_from_trade
                    .add(ForgeRegistries.ENCHANTMENTS.getValue(new ResourceLocation(o))));
  }
}

package tfar.enchantmentcontrol;

import com.google.common.collect.Lists;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.fml.common.Mod;
import tfar.extratags.api.tagtypes.EnchantmentTags;

import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(EnchantmentControl.MODID)
public class EnchantmentControl {
    public static final String MODID = "enchantmentcontrol";

    public static final Random rand = new Random();

    public EnchantmentControl() {
        if(!isMixinInClasspath())
            throw new IllegalStateException("install mixin bootstrap: https://www.curseforge.com/minecraft/mc-mods/mixinbootstrap");
    }

    public static Enchantment filteredRandomTrade(){
        List<Enchantment> enchantmentList = Registry.ENCHANTMENT.stream().filter(e ->
                !EnchantmentControl.blacklisted_from_trade.contains(e)).collect(Collectors.toList());
        //todo 0
        return enchantmentList.get(EnchantmentControl.rand.nextInt(enchantmentList.size()));
    }

    public static List<Enchantment> filteredRandomLoot(Collection<Enchantment> enchantments) {
        return Lists.newArrayList(enchantments).stream().filter(enchantment ->
                !blackisted_from_loot_tables.contains(enchantment)).collect(Collectors.toList());
    }

    public static final Tag<Enchantment> blacklisted_from_trade = EnchantmentTags.makeWrapperTag(
            new ResourceLocation(MODID,"blacklisted_from_trade"));

    public static final Tag<Enchantment> blackisted_from_loot_tables = EnchantmentTags.makeWrapperTag(
            new ResourceLocation(MODID,"blacklisted_from_loot_tables"));

    public static boolean isMixinInClasspath() {
        try {
            Class.forName("org.spongepowered.asm.launch.Phases");
            return true;
        }
        catch (ClassNotFoundException e) {
            return false;
        }
    }
}

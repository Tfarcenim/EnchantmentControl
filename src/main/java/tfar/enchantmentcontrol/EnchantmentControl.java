package tfar.enchantmentcontrol;

import net.minecraft.enchantment.Enchantment;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

import java.util.HashSet;
import java.util.Set;

import static tfar.enchantmentcontrol.ModConfigs.SERVER_SPEC;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(EnchantmentControl.MODID)
public class EnchantmentControl {
    public static final String MODID = "enchantmentcontrol";

    public EnchantmentControl() {
        if(!isMixinInClasspath())
            throw new IllegalStateException("install mixin bootstrap: https://github.com/LXGaming/MixinBootstrap/releases/download/v1.0.1/MixinBootstrap-1.0.1.jar");
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, SERVER_SPEC);
    }

    public static final Set<Enchantment> blackisted_from_trade = new HashSet<>();
    public static final Set<Enchantment> blackisted_from_loot_tables = new HashSet<>();

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

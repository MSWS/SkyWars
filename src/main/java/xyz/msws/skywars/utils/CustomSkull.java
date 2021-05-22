package xyz.msws.skywars.utils;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.Base64;
import java.util.UUID;

public class CustomSkull {
    private final byte[] encoded;
    private static Field pf;

    public CustomSkull(String url) {
        this.encoded = encode(String.format("{textures:{SKIN:{url:\"%s\"}}}", url));
    }

    public CustomSkull(byte[] b64) {
        this.encoded = b64;
    }

    public ItemStack getItemStack() {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        if (meta == null)
            throw new NullPointerException();
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        profile.getProperties().put("textures", new Property("textures", new String(encoded)));
        try {
            if (pf == null) {
                pf = meta.getClass().getDeclaredField("profile");
                pf.setAccessible(true);
            }
            pf.set(meta, profile);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        item.setItemMeta(meta);
        return item;
    }

    public static CustomSkull fromId(String id) {
        return new CustomSkull("http://textures.minecraft.net/texture/" + id);
    }

    private static byte[] encode(String s) {
        Base64.Encoder enc = Base64.getEncoder();
        return enc.encode(s.getBytes());
    }


}

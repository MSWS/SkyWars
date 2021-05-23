package xyz.msws.skywars.utils;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Skull;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

public class CustomSkull {
    private byte[] encoded;
    private String urlId, rawUrl;
    private static Field metaProfile, stateProfile;
    private ItemStack item;

    public CustomSkull(String url) {
        String part = "textures.minecraft.net/texture/";
        if (!url.contains(part))
            throw new IllegalArgumentException("URL is not a valid MC texture URL (" + url + ")");
        this.rawUrl = url;
        this.urlId = getUrlID(url);
        this.encoded = encode(String.format("{textures:{SKIN:{url:\"%s\"}}}", url));
    }

    public CustomSkull(byte[] b64) {
        this.encoded = b64;
        String textureString = decode(b64);
        this.rawUrl = getRawURL(textureString);
        this.urlId = getUrlID(rawUrl);
    }

    public static CustomSkull fromBlock(BlockState block) {
        if (block.getType() != Material.PLAYER_HEAD)
            throw new IllegalArgumentException("BlockState is " + block.getType() + " not " + Material.PLAYER_HEAD);
        return new CustomSkull((Skull) block);
    }

    public static CustomSkull fromBlock(Block block) {
        return fromBlock(block.getState());
    }

    public CustomSkull(Skull skull) {
        try {
            if (stateProfile == null) {
                stateProfile = skull.getClass().getDeclaredField("profile");
                stateProfile.setAccessible(true);
            }
            GameProfile profile = (GameProfile) stateProfile.get(skull);
            List<Property> properties = new ArrayList<>(profile.getProperties().get("textures"));
            for (Property p : properties) {
                if (!p.getName().equals("textures"))
                    continue;
                this.encoded = p.getValue().getBytes();
                this.rawUrl = getRawURL(this.encoded);
                this.urlId = getUrlID(rawUrl);
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public ItemStack getItemStack() {
        if (this.item != null)
            return item.clone();
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        if (meta == null)
            throw new NullPointerException();
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        profile.getProperties().put("textures", new Property("textures", new String(encoded)));
        try {
            if (metaProfile == null) {
                metaProfile = meta.getClass().getDeclaredField("profile");
                metaProfile.setAccessible(true);
            }
            metaProfile.set(meta, profile);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        item.setItemMeta(meta);
        this.item = item.clone();
        return item;
    }

    public byte[] getEncoded() {
        return encoded;
    }

    public String getEncodedString() {
        return new String(getEncoded());
    }

    public String getRawURL() {
        return rawUrl;
    }

    public String getURLId() {
        return urlId;
    }

    public static CustomSkull fromId(String id) {
        return new CustomSkull("http://textures.minecraft.net/texture/" + id);
    }

    public static CustomSkull fromB64(String b64) {
        return new CustomSkull(encode(b64));
    }

    private static byte[] encode(String s) {
        Base64.Encoder enc = Base64.getEncoder();
        return enc.encode(s.getBytes());
    }

    private static String decode(byte[] b64) {
        Base64.Decoder dec = Base64.getDecoder();
        return new String(dec.decode(b64));
    }

    private static String getRawURL(byte[] b64) {
        return getRawURL(decode(b64));
    }

    private static String getRawURL(String texture) {
        String part = "{textures:{SKIN:{url:";
        return texture.substring(texture.indexOf(part) + part.length(), texture.length() - 4);
    }

    private static String getUrlID(byte[] b64) {
        return getUrlID(decode(b64));
    }

    private static String getUrlID(String url) {
        String link = "textures.minecraft.net/texture/";
        return url.substring(url.indexOf(link) + link.length());
    }


}

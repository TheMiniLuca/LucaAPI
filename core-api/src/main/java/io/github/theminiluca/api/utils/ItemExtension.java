package io.github.theminiluca.api.utils;

import com.mojang.authlib.properties.Property;
import io.github.theminiluca.api.LucaAPI;
import io.github.theminiluca.api.event.ArmorType;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_19_R1.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.*;


public class ItemExtension extends ItemStack implements Serializable {

    public static @NotNull ItemExtension item(final ItemStack itemStack) {
        ItemStack is = (itemStack == null ? newAirItem() : itemStack);
        return new ItemExtension(is);
    }

    public static @NotNull ItemExtension item(final Material material) {
        Material mater = (material == null ? Material.AIR : material);
        return new ItemExtension(mater);
    }

    public static ItemExtension AIR = newAirItem();

    private ItemExtension(final Material material) {
        super(material);
    }


    public String toString() {
        StringBuilder toString = (new StringBuilder("ItemExtension{")).append(this.getType().name()).append(" x ").append(this.getAmount());
        if (this.hasItemMeta()) {
            toString.append(", ").append(this.getItemMeta());
        }

        return toString.append('}').toString();
    }

    private ItemExtension(final ItemStack itemStack) {
        super(itemStack);
    }

    public boolean hasDisplayName() {
        return !getDisplayName().isEmpty();
    }
    public boolean hasLore() {
        return !getLore().isEmpty();
    }

    public @NotNull ItemExtension setDisplayName(final String name) {
        ItemMeta im = getItemMeta();
        if (im == null) im = Bukkit.getItemFactory().getItemMeta(Material.STRUCTURE_VOID);
        im.setDisplayName(ChatColor.WHITE + name);
        setItemMeta(im);
        return this;
    }

    public @NotNull ItemExtension displayname(String name) {
        ItemMeta im = getItemMeta();
        im.setDisplayName(name);
        setItemMeta(im);
        return this;
    }


    public @NotNull String getDisplayName() {
        return getItemMeta() != null && getItemMeta().hasDisplayName() ? getItemMeta().getDisplayName() : "";
    }



    public @NotNull ItemExtension addLore(final List<String> args) {
        List<String> lore = new ArrayList<>(getLore());
        lore.addAll(args);
        ItemMeta im = getItemMeta();
        if (im == null) return this;
        im.setLore(lore);
        setItemMeta(im);
        return this;
    }

    public @NotNull ItemExtension addLore(final String... args) {
        List<String> lore = new ArrayList<>(getLore());
        lore.addAll(new ArrayList<>(List.of(args)));
        ItemMeta im = getItemMeta();
        if (im == null) return this;
        im.setLore(lore);
        setItemMeta(im);
        return this;
    }

    public ItemExtension color(DyeColor color) {
        ItemExtension item = new ItemExtension(this).clone();
        if (isWool()) {
            String[] split = getType().name().split("_");
            split[0] = color.name();
            item.setType(Objects.requireNonNull(Material.matchMaterial(split[0] + "_WOOL")));
        } else if (isTerracotta()) {
            String[] split = getType().name().split("_");
            split[0] = color.name();
            item.setType(Objects.requireNonNull(Material.matchMaterial(split[0] + "_TERRACOTTA")));
        } else if (isGlass()) {
            String[] split = getType().name().split("_");
            split[0] = color.name();
            item.setType(Objects.requireNonNull(Material.matchMaterial(split[0] + "_STAINED_GLASS")));
        }
        return item;
    }

    public @NotNull ItemExtension clearLore() {
        ItemMeta im = getItemMeta();
        if (im == null) return this;
        im.setLore(new ArrayList<>());
        setItemMeta(im);
        return this;
    }

    public @NotNull ItemExtension addLore(final String args) {
        List<String> lore = new ArrayList<>(getLore());
        lore.add(args);
        ItemMeta im = getItemMeta();
        if (im == null) return this;
        im.setLore(lore);
        setItemMeta(im);
        return this;
    }

    public @NotNull ItemExtension addLore() {
        List<String> lore = new ArrayList<>(getLore());
        lore.add(ChatColor.WHITE.toString());
        ItemMeta im = getItemMeta();
        if (im == null) return this;
        im.setLore(lore);
        setItemMeta(im);
        return this;
    }

    public ItemExtension enchantment(Enchantment enchantment, int level) {
        ItemMeta im = getItemMeta();
        assert im != null;
        if (level == 0) {
            im.removeEnchant(enchantment);
            setItemMeta(im);
            return this;
        }
        im.addEnchant(enchantment, level, true);
        setItemMeta(im);
        return this;
    }
    public @NotNull ItemExtension setDisplayMeta(final String... args) {
        setDisplayName(args[0]);
        setLore(Arrays.copyOfRange(args, 1, args.length));
        return this;
    }

    public @NotNull ItemExtension clearDisplayName() {
        ItemMeta im = getItemMeta();
        assert im != null;
        im.setDisplayName(ChatColor.WHITE.toString());
        setItemMeta(im);
        return this;
    }

    public boolean isLeather() {
        return switch (getType()) {
            case LEATHER_BOOTS, LEATHER_CHESTPLATE, LEATHER_HELMET, LEATHER_LEGGINGS -> true;
            default -> false;
        };
    }

    public boolean isMineral() {
        return switch (getType()) {
            case COAL, IRON_INGOT, GOLD_INGOT, DIAMOND, EMERALD -> true;
            default -> false;
        };
    }

    public boolean isWeapon() {
        return getType().name().contains("SWORD")
                || getType().name().contains("AXE")
                || getType().name().contains("BOW");
    }

    public boolean isSword() {
        return getType().name().contains("SWORD");
    }

    public boolean isAxe() {
        return getType().name().contains("AXE");
    }

    public boolean isArmor() {
        return ArmorType.matchType(this) != null;
    }

    public boolean isBow() {
        return getType().equals(Material.BOW);
    }
    public boolean isWool() {
        return getType().name().contains("WOOL");
    }

    public boolean isTerracotta() {
        return getType().name().contains("TERRACOTTA");
    }
    public boolean isGlass() {
        return getType().name().contains("GRASS");
    }

    public boolean isTool() {
        return getType().name().contains("PICKAXE")
                || getType().name().contains("AXE")
                || getType().name().contains("SHOVEL")
                || getType().name().contains("HOE");
    }
    public ItemExtension setColor(Color color) {
        if (this.isLeather()) {
            LeatherArmorMeta im = (LeatherArmorMeta) getItemMeta();
            assert im != null;
            im.setColor(color);
            setItemMeta(im);
        }
        return this;
    }

    public ItemExtension setGlowing(boolean flag) {
        ItemMeta im = getItemMeta();
        if (flag) {
            im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            im.addEnchant(Enchantment.OXYGEN, 1, true);
        } else {
            im.removeItemFlags(ItemFlag.HIDE_ENCHANTS);
            im.removeEnchant(Enchantment.OXYGEN);
        }
        setItemMeta(im);
        return this;
    }

    public ChatColor getColor() {
        return switch (getType()) {
            case DIAMOND, DIAMOND_ORE -> ChatColor.AQUA;
            case EMERALD, EMERALD_ORE -> ChatColor.GREEN;
            case GOLD_INGOT, GOLD_ORE -> ChatColor.GOLD;
            case COAL, COAL_BLOCK -> ChatColor.BLACK;
            default -> ChatColor.WHITE;
        };
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass() != this.getClass()) return false;

        ItemExtension is = (ItemExtension) obj;

        if (!is.getType().equals(getType())) return false;
        if (!is.getDisplayName().equals(getDisplayName())) return false;

        return is.getLore().equals(getLore());
    }

    @Deprecated
    public static ItemExtension newAirItem() {
        return new ItemExtension(Material.AIR);
    }


    public String extractName() {
        return getDisplayName().replaceFirst("§f", "");
    }

    public String extractLore() {
        if (getLore().isEmpty()) return "";
        return getLore().get(0).replace(Colour.DARK_GRAY, "").replace(Colour.ITALIC.toString(), "");
    }

    public ItemExtension setUnbreakable(boolean flag) {
        ItemMeta im = getItemMeta();
        if (im == null || isAir()) return this;
        im.setUnbreakable(flag);
        setItemMeta(im);
        return this;
    }



    //    public static String extractKitName(final ItemStack is) {
//        if (is.getItemMeta() == null) return "";
//        if (!is.getItemMeta().hasDisplayName()) return "";
//        if (is.getItemMeta().getLore().isEmpty()) return "";
//        List<String> lore = is.getItemMeta().getLore();
//        return lore.get(0).replace("§o", "").replace("§8", "");
//    }


    public boolean isAir() {
        return getType().isAir();
    }

    public ItemExtension setLore(final String... args) {
        ItemMeta im = getItemMeta();
        assert im != null;
        im.setLore(new ArrayList<>(Arrays.asList(args)));
        setItemMeta(im);
        return this;
    }

    public ItemExtension setNumberOfItem(final int amount) {
        setAmount(amount);
        return this;
    }

    public List<String> getLore() {
        if (getItemMeta() == null) return new ArrayList<>();
        if (!getItemMeta().hasLore()) return new ArrayList<>();
        return getItemMeta().getLore();
    }

    public static ItemStack setAmount(final ItemStack is) {
        if (is == null) return null;
        ItemStack itemStack = is.clone();
        itemStack.setAmount(1);
        return itemStack;
    }

    public static ItemStack setAmount(final ItemStack is, final int amount) {
        if (is == null) return null;
        ItemStack itemStack = is.clone();
        itemStack.setAmount(amount);
        return itemStack;
    }

    public static boolean equals(final ItemExtension v, final ItemExtension v1) {
        if (v == null) return false;
        if (v1 == null) return false;
        return v.equals(v1);
    }

    public static ItemStack replaceNull(final ItemStack is) {
        if (is == null) return new ItemStack(Material.AIR);
        return is.clone();
    }

    public static ItemExtension newWhitePane() {
        return new ItemExtension(Material.WHITE_STAINED_GLASS_PANE).clearDisplayName();
    }
    public static ItemExtension newGrayPane() {
        return new ItemExtension(Material.GRAY_STAINED_GLASS_PANE).clearDisplayName();
    }

    public static boolean equalsDisplayName(final ItemStack v, final String name) {
        if (v == null) return false;
        if (v.getType().equals(Material.AIR)) return false;
        if (!v.hasItemMeta()) return false;
        if (!Objects.requireNonNull(v.getItemMeta()).hasDisplayName()) return false;
        return v.getItemMeta().getDisplayName().equals(name);
    }
    public static boolean containsDisplayName(final ItemStack v, final String name) {
        if (v == null) return false;
        if (v.getType().equals(Material.AIR)) return false;
        if (!v.hasItemMeta()) return false;
        if (!Objects.requireNonNull(v.getItemMeta()).hasDisplayName()) return false;
        return v.getItemMeta().getDisplayName().contains(name);
    }

    public static Material material(String name) {
        return Material.matchMaterial(name);
    }

    @NotNull
    public static ItemExtension deserialize(@NotNull Map<String, Object> args) {
        return item(ItemStack.deserialize(args));
    }

    public NBTTagCompound getNBTTag() {
        return LucaAPI.nmsHandler.getNBTTag(this);
    }
    public Property getProperty() {
        Field field = null;
        try {
            Map<String, NBTBase> map = getNBT(getNBTTag());
            NBTTagCompound compound = (NBTTagCompound) getNBT(((NBTTagCompound) map.get("SkullOwner"))).get("Properties");
            Map<String, NBTBase> textures = getNBT((NBTTagCompound) getNBT((NBTTagList) getNBT(compound).get("textures")).get(0));
            String value = getNBT((NBTTagString) textures.get("Value"));
            String signature = null;
            try {
                signature = getNBT((NBTTagString) textures.get("Signature"));
            } catch (Exception ignored) {

            }
            return new Property("textures", value, signature);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @SuppressWarnings("unchecked")
    private Map<String, NBTBase> getNBT(NBTTagCompound tag) {
        Field field = null;
        try {
            field = NBTTagCompound.class.getDeclaredField("x");
            field.setAccessible(true);
            Map<String, NBTBase> map = (Map<String, NBTBase>) field.get(tag);
            return map;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    private List<NBTBase> getNBT(NBTTagList tag) {
        Field field = null;
        try {
            field = NBTTagList.class.getDeclaredField("c");
            field.setAccessible(true);
            List<NBTBase> list = (List<NBTBase>) field.get(tag);
            return list;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    private String getNBT(NBTTagString tag) {
        Field field = null;
        try {
            field = NBTTagString.class.getDeclaredField("A");
            field.setAccessible(true);
            return (String) field.get(tag);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @NotNull
    public ItemExtension clone() {
        return item(new ItemStack(this));
    }
}

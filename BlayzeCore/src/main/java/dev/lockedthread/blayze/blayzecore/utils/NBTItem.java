package dev.lockedthread.blayze.blayzecore.utils;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.Set;

public class NBTItem {

    private static Method nmsItemStackHasTag;
    private static Method nmsItemStackGetTag;
    private static Method nmsItemStackSetTag;

    private static Class<?> nmsNBTTagCompound;
    private static Constructor<?> nmsNBTTagCompoundConstructor;

    private static Method nmsNBTTagCompoundHasKey;
    private static Method nmsNBTTagCompoundGetKeys;

    private static Method nmsNBTTagCompoundGetInt;
    private static Method nmsNBTTagCompoundGetBoolean;
    private static Method nmsNBTTagCompoundGetString;
    private static Method nmsNBTTagCompoundGetByte;
    private static Method nmsNBTTagCompoundGetDouble;
    private static Method nmsNBTTagCompoundGetLong;
    private static Method nmsNBTTagCompoundGetShort;

    private static Method nmsNBTTagCompoundSetInt;
    private static Method nmsNBTTagCompoundSetBoolean;
    private static Method nmsNBTTagCompoundSetString;
    private static Method nmsNBTTagCompoundSetByte;
    private static Method nmsNBTTagCompoundSetDouble;
    private static Method nmsNBTTagCompoundSetLong;
    private static Method nmsNBTTagCompoundSetShort;

    private static Method craftItemStackAsNMSCopy;
    private static Method craftItemStackAsBukkitCopy;

    private static void initReflectionFields() {
        String version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
        try {
            Class<?> nmsItemStackClass = Class.forName("net.minecraft.server." + version + ".ItemStack");

            nmsItemStackHasTag = nmsItemStackClass.getMethod("hasTag");
            nmsItemStackGetTag = nmsItemStackClass.getMethod("getTag");
            nmsItemStackSetTag = nmsItemStackClass.getMethod("setTag", nmsNBTTagCompound);

            nmsNBTTagCompound = Class.forName("net.minecraft.server." + version + ".NBTTagCompound");
            nmsNBTTagCompoundConstructor = nmsNBTTagCompound.getConstructor();

            nmsNBTTagCompoundHasKey = nmsNBTTagCompound.getMethod("hasKey", String.class);
            nmsNBTTagCompoundGetInt = nmsNBTTagCompound.getMethod("getInt", String.class);
            nmsNBTTagCompoundGetBoolean = nmsNBTTagCompound.getMethod("getBoolean", String.class);
            nmsNBTTagCompoundGetString = nmsNBTTagCompound.getMethod("getString", String.class);
            nmsNBTTagCompoundGetByte = nmsNBTTagCompound.getMethod("getByte", String.class);
            nmsNBTTagCompoundGetDouble = nmsNBTTagCompound.getMethod("getDouble", String.class);
            nmsNBTTagCompoundGetLong = nmsNBTTagCompound.getMethod("getLong", String.class);
            nmsNBTTagCompoundGetShort = nmsNBTTagCompound.getMethod("getShort", String.class);

            nmsNBTTagCompoundSetInt = nmsNBTTagCompound.getMethod("setInt", String.class, int.class);
            nmsNBTTagCompoundSetBoolean = nmsNBTTagCompound.getMethod("setBoolean", String.class, boolean.class);
            nmsNBTTagCompoundSetString = nmsNBTTagCompound.getMethod("setString", String.class, String.class);
            nmsNBTTagCompoundSetByte = nmsNBTTagCompound.getMethod("setByte", String.class, byte.class);
            nmsNBTTagCompoundSetDouble = nmsNBTTagCompound.getMethod("setDouble", String.class, double.class);
            nmsNBTTagCompoundSetLong = nmsNBTTagCompound.getMethod("setLong", String.class, long.class);
            nmsNBTTagCompoundSetShort = nmsNBTTagCompound.getMethod("setShort", String.class, short.class);

            nmsNBTTagCompoundGetKeys = nmsNBTTagCompound.getMethod("getKeys");

            Class<?> craftItemStack = Class.forName("org.bukkit.craftbukkit." + version + ".inventory.CraftItemStack");
            craftItemStackAsNMSCopy = craftItemStack.getMethod("getTag", ItemStack.class);

            craftItemStackAsBukkitCopy = craftItemStack.getMethod("asBukkitCopy", nmsItemStackClass);

        } catch (ClassNotFoundException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    private final Object nmsItemStack;
    private final Object rootCompound;

    public NBTItem(ItemStack startItemStack) {
        Objects.requireNonNull(startItemStack, "startItemStack can't be null in NBTItem Constructor");
        if (nmsNBTTagCompound == null) {
            initReflectionFields();
        }
        try {
            this.nmsItemStack = craftItemStackAsNMSCopy.invoke(null, startItemStack);
            this.rootCompound = (boolean) nmsItemStackHasTag.invoke(null) ? nmsItemStackGetTag.invoke(null) : nmsNBTTagCompoundConstructor.newInstance();
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException ex) {
            throw new RuntimeException(ex);
        }
    }

    public Set<String> getKeys() {
        try {
            return (Set<String>) nmsNBTTagCompoundGetKeys.invoke(nmsItemStack);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean hasKey(String key) {
        try {
            return (boolean) nmsNBTTagCompoundHasKey.invoke(nmsItemStack);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean getBoolean(String key) {
        try {
            return (boolean) nmsNBTTagCompoundGetBoolean.invoke(nmsItemStack);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getString(String key) {
        try {
            return (String) nmsNBTTagCompoundGetString.invoke(nmsItemStack);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public double getDouble(String key) {
        try {
            return (double) nmsNBTTagCompoundGetDouble.invoke(nmsItemStack);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    public int getInt(String key) {
        try {
            return (int) nmsNBTTagCompoundGetInt.invoke(nmsItemStack);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public long getLong(String key) {
        try {
            return (long) nmsNBTTagCompoundGetLong.invoke(nmsItemStack);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public short getShort(String key) {
        try {
            return (short) nmsNBTTagCompoundGetShort.invoke(nmsItemStack);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public short getByte(String key) {
        try {
            return (short) nmsNBTTagCompoundGetByte.invoke(nmsItemStack);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public NBTItem set(String key, Object value) {
        PrimitiveClass primitiveClass = PrimitiveClass.get(value.getClass());
        Objects.requireNonNull(primitiveClass, "Unable to find primitive class \"" + value.toString() + "\"");
        try {
            switch (primitiveClass) {
                case BOOLEAN:
                    nmsNBTTagCompoundSetBoolean.invoke(rootCompound, key, value);
                    break;
                case BYTE:
                    nmsNBTTagCompoundSetByte.invoke(rootCompound, key, value);
                    break;
                case DOUBLE:
                    nmsNBTTagCompoundSetDouble.invoke(rootCompound, key, value);
                    break;
                case INT:
                    nmsNBTTagCompoundSetInt.invoke(rootCompound, key, value);
                    break;
                case LONG:
                    nmsNBTTagCompoundSetLong.invoke(rootCompound, key, value);
                    break;
                case STRING:
                    nmsNBTTagCompoundSetString.invoke(rootCompound, key, value);
                    break;
                case SHORT:
                    nmsNBTTagCompoundSetShort.invoke(rootCompound, key, value);
                    break;
            }
            nmsItemStackSetTag.invoke(rootCompound);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return this;
    }

    public ItemStack buildItemStack() {
        try {
            return (ItemStack) craftItemStackAsBukkitCopy.invoke(nmsItemStack);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}

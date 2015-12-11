package com.j0ach1mmall3.ultimatecosmetics.internal.mounts;

import com.j0ach1mmall3.jlib.methods.ReflectionAPI;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

/**
 * Created by j0ach1mmall3 on 17:18 6/09/2015 using IntelliJ IDEA.
 */
final class MountReflection {
    private static final Class<?> NAVIGATIONABSTRACTCLASS = ReflectionAPI.getNmsClass("NavigationAbstract");
    private static final Class<?> PGSCLASS = ReflectionAPI.getNmsClass("PathfinderGoalSelector");
    private static final Class<?> ENTITYINSENTIENTCLASS = ReflectionAPI.getNmsClass("EntityInsentient");

    private MountReflection() {
    }

    private static Object getGoalSelector(Object o) {
        Object obj = null;
        try {
            Field goalSelector = ENTITYINSENTIENTCLASS.getDeclaredField("goalSelector");
            goalSelector.setAccessible(true);
            obj = goalSelector.get(o);
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return obj;
    }

    private static Object getTargetSelector(Object o) {
        Object obj = null;
        try {
            Field targetSelector = ENTITYINSENTIENTCLASS.getDeclaredField("targetSelector");
            targetSelector.setAccessible(true);
            obj = targetSelector.get(o);
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return obj;
    }

    static void removeGoalSelectors(Entity ent) {
        Object o = ReflectionAPI.getHandle(ent);
        try {
            Field b = PGSCLASS.getDeclaredField("b");
            Field c = PGSCLASS.getDeclaredField("c");
            b.setAccessible(true);
            c.setAccessible(true);
            ((Collection) b.get(getGoalSelector(o))).clear();
            ((Collection) b.get(getTargetSelector(o))).clear();
            ((Collection) c.get(getGoalSelector(o))).clear();
            ((Collection) c.get(getTargetSelector(o))).clear();
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    static void setNavigation(Entity ent, Location l, double speed) {
        Object o = ReflectionAPI.getHandle(ent);
        try {
            Object navigationAbstract = ENTITYINSENTIENTCLASS.getMethod("getNavigation").invoke(o);
            NAVIGATIONABSTRACTCLASS.getMethod("a", double.class, double.class, double.class, double.class).invoke(navigationAbstract, l.getX(), l.getY(), l.getZ(), speed);
        } catch (SecurityException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}

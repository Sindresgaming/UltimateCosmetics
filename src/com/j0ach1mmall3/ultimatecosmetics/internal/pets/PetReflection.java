package com.j0ach1mmall3.ultimatecosmetics.internal.pets;

import com.j0ach1mmall3.jlib.methods.ReflectionAPI;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;

/**
 * Created by j0ach1mmall3 on 15:56 23/08/2015 using IntelliJ IDEA.
 */
final class PetReflection {
    private static final Class<?> PGSCLASS = ReflectionAPI.getNmsClass("PathfinderGoalSelector");
    private static final Class<?> PATHFINDERGOALCLASS = ReflectionAPI.getNmsClass("PathfinderGoal");
    private static final Class<?> HUMANCLASS = ReflectionAPI.getNmsClass("EntityHuman");
    private static final Class<?> ENTITYINSENTIENTCLASS = ReflectionAPI.getNmsClass("EntityInsentient");
    private static final Class<?> ENTITYCREATURECLASS = ReflectionAPI.getNmsClass("EntityCreature");
    private static final Class<?> PGFCLASS = ReflectionAPI.getNmsClass("PathfinderGoalFloat");
    private static final Class<?> PGMACLASS = ReflectionAPI.getNmsClass("PathfinderGoalMeleeAttack");
    private static final Class<?> PGLAPCLASS = ReflectionAPI.getNmsClass("PathfinderGoalLookAtPlayer");

    private PetReflection() {
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

    static void addGoalSelectors(Entity ent) {
        Object o = ReflectionAPI.getHandle(ent);
        try {
            Method a = PGSCLASS.getMethod("a", int.class, PATHFINDERGOALCLASS);
            a.invoke(getGoalSelector(o), 0, PGFCLASS.getConstructor(ENTITYINSENTIENTCLASS).newInstance(o));
            a.invoke(getGoalSelector(o), 2, PGMACLASS.getConstructor(ENTITYCREATURECLASS, Class.class, double.class, boolean.class).newInstance(o, HUMANCLASS, 1.0D, true));
            a.invoke(getGoalSelector(o), 8, PGLAPCLASS.getConstructor(ENTITYINSENTIENTCLASS, Class.class, float.class).newInstance(o, HUMANCLASS, 8.0F));
        } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException | InstantiationException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }
    }
}

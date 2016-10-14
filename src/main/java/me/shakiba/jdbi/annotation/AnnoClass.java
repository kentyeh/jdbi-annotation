package me.shakiba.jdbi.annotation;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class AnnoClass<C> {

    private final static WeakHashMap<Class<?>, AnnoClass<?>> cache = new WeakHashMap<Class<?>, AnnoClass<?>>();

    @SuppressWarnings("unchecked")
    public static <C> AnnoClass<C> get(Class<C> clazz) {
        AnnoClass<?> get = null;
        if ((get = cache.get(clazz)) == null) {
            synchronized (cache) {
                if ((get = cache.get(clazz)) == null) {
                    cache.put(clazz, get = new AnnoClass<C>(clazz));
                }
            }
        }
        return (AnnoClass<C>) get;
    }

    private final List<AnnoMember> setters = new ArrayList<AnnoMember>();
    private final List<AnnoMember> getters = new ArrayList<AnnoMember>();

    private AnnoClass(Class<C> clazz) {
        if (logger.isDebugEnabled()) {
            logger.debug("init " + clazz);
        }
        inspectClass(clazz);
        inspectSuperclasses(clazz);
        if (logger.isDebugEnabled()) {
            logger.debug("init " + clazz + ": " + setters.size()
                    + " setters and " + getters.size() + " getters.");
        }
    }

    private void inspectSuperclasses(Class<? super C> clazz) {
        while ((clazz = clazz.getSuperclass()) != null) {
            if (clazz.isAnnotationPresent(MappedSuperclass.class)) {
                inspectClass(clazz);
            }
        }
    }

    private void inspectClass(Class<? super C> clazz) {
        for (Field member : clazz.getDeclaredFields()) {
            if (member.getAnnotation(Column.class) != null) {
                setters.add(new AnnoMember(clazz, member));
                getters.add(new AnnoMember(clazz, member));
            }
        }
        for (Method member : clazz.getDeclaredMethods()) {
            if (member.getAnnotation(Column.class) == null) {
                continue;
            }
            if (member.getParameterTypes().length == 1) {
                setters.add(new AnnoMember(clazz, member));
            } else if (member.getParameterTypes().length == 0) {
                getters.add(new AnnoMember(clazz, member));
            }
        }
    }

    public List<AnnoMember> setters() {
        return setters;
    }

    public List<AnnoMember> getters() {
        return getters;
    }

    private static final Logger logger = LoggerFactory.getLogger(AnnoClass.class);
}
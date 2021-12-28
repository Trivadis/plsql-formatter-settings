package com.trivadis.plsql.formatter;

import org.graalvm.nativeimage.hosted.Feature;
import org.graalvm.nativeimage.hosted.RuntimeReflection;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class RuntimeReflectionRegistrationFeature implements Feature {
    private static final String[] SKIP_CLASS_NAMES = {
            "oracle.dbtools.util.Closeables",
    };

    private static final String PRINT_REFLECT_OBJECT = System.getenv("TVDFORMAT_PRINT_REFLECT_OBJECT");

    private HashSet<String> registeredClassNames = new HashSet<>();

    private void register(String packageName, boolean includeSubPackages, ClassLoader classLoader) {
        Reflections reflections = new Reflections(packageName);
        Set<String> allClassNames = reflections.getAll(Scanners.SubTypes);
        // allClassNames contains also inner classes
        for (String className : allClassNames) {
            if (className.startsWith((packageName))) {
                if (includeSubPackages || (!className.substring(packageName.length() + 1).contains("."))) {
                    if (validClass(className)) {
                        registerClass(className, classLoader);
                    }
                }
            }
        }
    }

    private boolean validClass(String className) {
        for (String skipClassName : SKIP_CLASS_NAMES) {
            if (className.startsWith(skipClassName)) {
                return false;
            }
        }
        return true;
    }

    private void registerClass(String className, ClassLoader classLoader) {
        try {
            Class<?> clazz = Class.forName(className, false, classLoader);
            // calling getClass() on a clazz throws an Exception when not found on the classpath
            RuntimeReflection.register(clazz.getClass());
            for (Constructor<?> constructor : clazz.getDeclaredConstructors()) {
                RuntimeReflection.register(constructor);
            }
            for (Method method : clazz.getDeclaredMethods()) {
                RuntimeReflection.register((method));
            }
            for (Field field : clazz.getDeclaredFields()) {
                RuntimeReflection.register(field);
            }
            registeredClassNames.add(className);
        } catch (Throwable t) {
            // ignore
        }
    }

    private void printReflectObjects() {
        if (PRINT_REFLECT_OBJECT != null && PRINT_REFLECT_OBJECT.trim().equalsIgnoreCase("true")) {
            System.out.println("[");
            String template = "  {\n" +
                    "    \"name\" : \"#CLASS_NAME#\",\n" +
                    "    \"allDeclaredConstructors\": true,\n" +
                    "    \"allDeclaredMethods\": true,\n" +
                    "    \"allDeclaredFields\": true,\n" +
                    "    \"allPublicConstructors\": true,\n" +
                    "    \"allPublicMethods\": true,\n" +
                    "    \"allPublicFields\": true\n" +
                    "  }";
            boolean first = true;
            for (String registeredClassName : registeredClassNames.stream().sorted().collect(Collectors.toList())) {
                if (first) {
                    first = false;
                } else {
                    System.out.println(",");
                }
                System.out.print(template.replace("#CLASS_NAME#", registeredClassName));
            }
            System.out.println("]");
        }
    }

    public void beforeAnalysis(BeforeAnalysisAccess access) {
        ClassLoader classLoader = access.getApplicationClassLoader();
        // register all classes in a package
        register("oracle.dbtools.app", true, classLoader);
        register("oracle.dbtools.arbori", true, classLoader);
        register("oracle.dbtools.parser", true, classLoader);
        register("oracle.dbtools.raptor", false, classLoader);
        register("oracle.dbtools.util", true, classLoader);
    }

    @Override
    public void afterAnalysis(AfterAnalysisAccess access) {
        Feature.super.afterAnalysis(access);
        printReflectObjects();
    }
}

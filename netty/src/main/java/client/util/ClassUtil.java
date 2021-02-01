package client.util;

import java.io.File;
import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

public class ClassUtil {


    /**
     * 扫描某包下所有的子类
     * @param basePackage
     * @param clazz
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> Set<Class<? extends T>> getAllSubClasses(String basePackage, Class<T> clazz) {

        String filePath = basePackage.replace('.', '/');
        filePath = ClassUtil.class.getResource("/" + filePath).getPath();
        File file = new File(filePath);

        File[] files = file.listFiles();
        Set<Class<?>> classSet = new HashSet<>();
        getClasses(basePackage, files, classSet);

        Set<Class<? extends T>> returnSet = new HashSet<>();
        for (Class<?> clz : classSet) {
            if (clz.isAssignableFrom(clazz)) {
                returnSet.add((Class<? extends T>)clz);
            }
        } 

        return returnSet;
    }

    /**
     * 扫描某包下所有被注解标示的类
     * @param basePackage
     * @param clazz
     * @return
     */
    public static Set<Class<?>> getAllAnnoTopClasses(String basePackage, Class<? extends Annotation> clazz) {

        String filePath = basePackage.replace('.', '/');
        filePath = ClassUtil.class.getResource("/" + filePath).getPath();
        File file = new File(filePath);

        File[] files = file.listFiles();
        Set<Class<?>> classSet = new HashSet<>();
        getClasses(basePackage, files, classSet);

        Set<Class<?>> returnSet = new HashSet<>();
        for (Class<?> clz : classSet) {
            if (clz.isAnnotationPresent(clazz)) {
                returnSet.add(clz);
            }
        }

        return returnSet;
    }

    private static void getClasses(String basePackage, File[] files, Set<Class<?>> classSet) {
        if (files == null) {
            return;
        }
        for (File file : files) {
            if (file.isFile() && file.getName().endsWith(".class")) {
                try {
                    classSet.add(Class.forName(basePackage + "." + file.getName().substring(0, file.getName().length()-6)));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            } else if (file.isDirectory()) {
                String pkg = basePackage + "." + file.getName();
                getClasses(pkg, file.listFiles(), classSet);
            }
        }
    }
}

import java.io.*;
import java.lang.reflect.Method;

public class HelloClassLoader extends ClassLoader {
    public static void main(String[] args) throws Exception {
        HelloClassLoader myClassLoader = new HelloClassLoader();
        final Class<?> hello = myClassLoader.findClass("Hello");
        Method sayHello = hello.getMethod("hello");
        Object hello1 = hello.newInstance();
        System.out.println(sayHello.invoke(hello1));
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        Class clazz = null;
        String classFilename = name + ".xlass";
        File classFile = new File(classFilename);
        if (classFile.exists()) {
            try {
                InputStream in = new FileInputStream(classFilename);
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024 * 4];
                int n = 0;
                while ((n = in.read(buffer)) != -1) {
                    out.write(buffer, 0, n);
                }
                byte[] bytes = out.toByteArray();
                for (int i = 0; i < bytes.length; i++) {
                    bytes[i] = (byte) (255 - bytes[i]);
                }

                clazz = defineClass(name, bytes, 0, bytes.length);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }


        }

        if (clazz == null) {
            throw new ClassNotFoundException(name);
        }
        return clazz;
    }

}

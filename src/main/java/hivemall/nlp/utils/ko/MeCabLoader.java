package hivemall.nlp.utils.ko;

import org.chasen.mecab.Model;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Map;
import java.util.WeakHashMap;

public final class MeCabLoader {
    private static Map<String, Model> models = new WeakHashMap<>();

    static {
        AccessController.doPrivileged(new PrivilegedAction<Void>() {
            public Void run() {
                try {
                    System.load("/usr/local/lib/libMeCab.so");
                } catch (UnsatisfiedLinkError e) {
                    System.err.println(
                            "Cannot load the native code.\n"
                                    + "Make sure your LD_LIBRARY_PATH contains MeCab.so path.\n" + e);
                    System.exit(1);
                }
                return null;
            }
        });
    }

    public static synchronized Model getModel(String args) throws RuntimeException {
        Model model = models.get(args);
        if (model == null) {
            model = new Model(args);
            models.put(args, model);
        }
        return model;
    }

    public static int getModelCount() {
        return models.size();
    }
}

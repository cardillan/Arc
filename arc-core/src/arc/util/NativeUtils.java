package arc.util;

public class NativeUtils{

    /*JNI

    #include <stdlib.h>
    #include <locale.h>
    #if defined(__linux__) && !defined(__ANDROID__)
        #include <langinfo.h>
    #endif

     */

    /** @return 0 on success, -1 on failure (errno set on the native side). Does nothing on Windows. */
    public static native int setEnv(String name, String value, boolean overwrite); /*
        #if defined(__linux__) && !defined(__ANDROID__)
            return setenv(name, value, overwrite ? 1 : 0);
        #else
            return -1;
        #endif
    */

    /** @return 0 on success, -1 on failure. Does nothing on Windows. */
    public static native int unsetEnv(String name); /*
        #if defined(__linux__) && !defined(__ANDROID__)
            return unsetenv(name);
        #else
            return -1;
        #endif
    */

    /**
     * Wraps getenv. This may differ from System.getenv.
     * @return the environment variable, or the empty string.
     */
    public static native String getEnv(String name); /*
        #if defined(__linux__) && !defined(__ANDROID__)
            char* val = getenv(name);
            if(val == NULL){
                return env->NewStringUTF("");
            }
            return env->NewStringUTF(val);
        #else
            return env->NewStringUTF("");
        #endif
    */

    /**
     * Wraps setlocale. Pass LC_ALL for category.
     * @return the locale actually in effect after the call, or the empty string if the requested locale was rejected (setlocale returned NULL).
     */
    public static native String setLocale(int category, String locale); /*
        #if defined(__linux__) && !defined(__ANDROID__)
            char* result = setlocale(category, locale);
            if(result == NULL){
                return env->NewStringUTF("");
            }
            return env->NewStringUTF(result);
        #else
            return env->NewStringUTF("");
        #endif
    */

    /**
     * Wraps nl_langinfo(CODESET).
     * @return the current codeset, e.g. "UTF-8", or "ANSI_X3.4-1968" if stuck in the C locale.
     */
    public static native String getCodeset(); /*
        #if defined(__linux__) && !defined(__ANDROID__)
            char* result = nl_langinfo(CODESET);
            if(result == NULL){
                return env->NewStringUTF("");
            }
            return env->NewStringUTF(result);
        #else
            return env->NewStringUTF("");
        #endif
    */

    /** Fixes LC_ALL and LANG on Steam to avoid issues with Zenity.*/
    public static void forceUtf8Locale(){
        if(!OS.isLinux) return;
        setEnv("LC_ALL", "C.UTF-8", true);
        setEnv("LANG", "C.UTF-8", true);
        setEnv("LC_CTYPE", "C.UTF-8", true);
        //LC_ALL
        setLocale(6, "");
    }
}

package im.vector.app.eachchat.utils;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import java.lang.reflect.Method;
import java.util.Map;

import im.vector.app.eachchat.base.BaseModule;


/**
 * Created by zhouguanjie on 2019/8/10.
 */
public class SPUtils {
    public static final String FILE_NAME = "cache";
    public static SharedPreferences sp;

    private static SharedPreferences init(Context context) {
        return sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
    }

    /**
     * 插件间和宿主共用数据 必须 传入context
     */
    public static <E> void put(@NonNull String key, @NonNull E value) {
        SharedPreferences.Editor editor = init(BaseModule.getContext()).edit();
        if (value instanceof String || value instanceof Integer || value instanceof Boolean ||
                value instanceof Float || value instanceof Long || value instanceof Double) {
            editor.putString(key, String.valueOf(value));
        } else {
            editor.putString(key, new Gson().toJson(value));
        }
        SPCompat.apply(editor);
    }


    /**
     * 插件间和宿主共用数据 必须 传入context
     */
    public static <E> E get(@NonNull String key, @NonNull E defaultValue) {
        key = key;
        String value;
        if (defaultValue instanceof String || defaultValue instanceof Integer || defaultValue instanceof Boolean ||
                defaultValue instanceof Float || defaultValue instanceof Long || defaultValue instanceof Double) {
            value = init(BaseModule.getContext()).getString(key, String.valueOf(defaultValue));
        } else {
            value = init(BaseModule.getContext()).getString(key, new Gson().toJson(defaultValue));
        }

        if (defaultValue instanceof String) {
            return (E) value;
        }
        if (defaultValue instanceof Integer) {
            return (E) Integer.valueOf(value);
        }
        if (defaultValue instanceof Boolean) {
            return (E) Boolean.valueOf(value);
        }
        if (defaultValue instanceof Float) {
            return (E) Float.valueOf(value);
        }
        if (defaultValue instanceof Long) {
            return (E) Long.valueOf(value);
        }
        if (defaultValue instanceof Double) {
            return (E) Double.valueOf(value);
        }
        //json为null的时候返回对象为null,gson已处理
        return (E) new Gson().fromJson(value, defaultValue.getClass());
    }


    /**
     * 移除某个key值已经对应的值
     */
    public static void remove(String key) {
        SharedPreferences.Editor editor = init(BaseModule.getContext()).edit();
        editor.remove(key);
        SPCompat.apply(editor);
    }

    /**
     * 清除所有数据
     */
    public static void clear(Context context) {
        SharedPreferences.Editor editor = init(context).edit();
        editor.clear();
        SPCompat.apply(editor);
    }

    /**
     * 查询某个key是否已经存在
     */
    public static boolean contains(Context context, String key) {
        key = key;
        return init(context).contains(key);
    }

    /**
     * 返回所有的键值对
     */
    public static Map<String, ?> getAll(Context context) {
        return init(context).getAll();
    }

    /**
     * 保存对象到sp文件中 被保存的对象须要实现 Serializable 接口
     */
    public static void saveObject(Context context, String key, Object value) {
        put(key, value);
    }

    /**
     * desc:获取保存的Object对象
     */
    public static <T> T readObject(Context context, String key, Class<T> clazz) {
        try {
            return (T) get(key, clazz.newInstance());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 创建一个解决SharedPreferencesCompat.apply方法的一个兼容类
     */
    private static class SPCompat {
        private static final Method S_APPLY_METHOD = findApplyMethod();

        /**
         * 反射查找apply的方法
         */
        @SuppressWarnings({"unchecked", "rawtypes"})
        private static Method findApplyMethod() {
            try {
                Class clz = SharedPreferences.Editor.class;
                return clz.getMethod("apply");
            } catch (NoSuchMethodException e) {
            }
            return null;
        }

        /**
         * 如果找到则使用apply执行，否则使用commit
         */
        public static void apply(SharedPreferences.Editor editor) {
            try {
                if (S_APPLY_METHOD != null) {
                    S_APPLY_METHOD.invoke(editor);
                    return;
                }
            } catch (Exception e) {
            }
            editor.commit();
        }
    }
}

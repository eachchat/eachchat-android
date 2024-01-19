package im.vector.app.eachchat.push;

import android.os.Build;
import android.text.TextUtils;

import com.facebook.stetho.common.LogUtil;
//import com.heytap.msp.push.HeytapPushManager;
//import com.vivo.push.PushClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import im.vector.app.eachchat.base.BaseModule;

/**
 * Created by zhouguanjie on 2020/1/10.
 */
public class RomUtils {
    private static final String TAG = "Rom";

    public static final String ROM_MIUI = "MIUI";
    public static final String ROM_EMUI = "EMUI";
    public static final String ROM_OPPO = "OPPO";
    public static final String ROM_ONEPLUS = "ONEPLUS";
    public static final String ROM_VIVO = "VIVO";
    public static final String ROM_FIREBASE = "FIREBASE";


    public static final String ROM_SMARTISAN = "SMARTISAN";
    public static final String ROM_QIKU = "QIKU";
    public static final String ROM_FLYME = "FLYME";


    private static final String KEY_VERSION_MIUI = "ro.miui.ui.version.name";
    private static final String KEY_VERSION_EMUI = "ro.build.version.emui";
    private static final String KEY_VERSION_OPPO = "ro.build.version.opporom";
    private static final String KEY_VERSION_SMARTISAN = "ro.smartisan.version";
    private static final String KEY_VERSION_VIVO = "ro.vivo.os.version";

    private static String sName;
    private static String sVersion;
    private static String rom;
    public static boolean isDowngrading = false;

    public static void init() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                getRom();
            }
        }.start();
    }

    public static boolean isEmui() {
        return check(ROM_EMUI);
    }

    public static boolean isOnePlus() {
        String brand = Build.BRAND.trim().toUpperCase();
        String model = Build.MODEL.trim().toUpperCase();
        return brand.contains("ONEPLUS") || model.contains("ONEPLUS");
    }

    /**
     * is GMS available
     */
//    public static boolean isGoogleService() {
//        int googlePlayServicesCheck = GoogleApiAvailability.getInstance()
//                .isGooglePlayServicesAvailable(BaseModule.getContext());
//        return googlePlayServicesCheck == ConnectionResult.SUCCESS;
//    }

    public static boolean isGoogle() {
        String brand = Build.BRAND.trim().toUpperCase();
        String model = Build.MODEL.trim().toUpperCase();
        return brand.contains("GOOGLE") || model.contains("GOOGLE");
    }

    public static boolean isMiui() {
        return check(ROM_MIUI);
    }

//    public static boolean isVivo() {
//        return PushClient.getInstance(BaseModule.getContext()).isSupport() && check(ROM_VIVO);
//    }
//
//    public static boolean isOppo() {
//        if (!HeytapPushManager.isSupportPush(BaseModule.getContext())) {
//            return false;
//        }
//        return check(ROM_OPPO);
//    }

    public static boolean isFlyme() {
        return check(ROM_FLYME);
    }

    public static boolean is360() {
        return check(ROM_QIKU) || check("360");
    }

    public static boolean isSmartisan() {
        return check(ROM_SMARTISAN);
    }

    public static String getName() {
        if (sName == null) {
            check("");
        }
        return sName;
    }

    public static String getVersion() {
        if (sVersion == null) {
            check("");
        }
        return sVersion;
    }

    public static void resetRom() {
        rom = null;
    }

    public static String getRom() {
        if (!TextUtils.isEmpty(rom)) {
            return rom;
        }
        if (!isDowngrading && isGoogle()) {
            rom = ROM_FIREBASE;
            return ROM_FIREBASE;
        }
//        if (isVivo()) {
//            rom = ROM_VIVO;
//            return ROM_VIVO;
//        }
        if (isMiui()) {
            rom = ROM_MIUI;
            return ROM_MIUI;
        }
        if (isEmui() && canHuaWeiPush()) {
            rom = ROM_EMUI;
            return ROM_EMUI;
        }
//        if (isOppo()) {
//            rom = ROM_OPPO;
//            return ROM_OPPO;
//        }

        if (isOnePlus()) {
            rom = ROM_ONEPLUS;
            return ROM_ONEPLUS;
        }
//        if (isFlyme()) {
//            return ROM_FLYME;
//        }
//
//        if (is360()) {
//            return ROM_QIKU;
//        }
//
//        if (isSmartisan()) {
//            return ROM_SMARTISAN;
//        }

        rom = "OTHER";
        return "OTHER";
    }

    public static boolean check(String rom) {
        if (sName != null) {
            return sName.equals(rom);
        }

        if (!TextUtils.isEmpty(sVersion = getProp(KEY_VERSION_MIUI))) {
            sName = ROM_MIUI;
        } else if (!TextUtils.isEmpty(sVersion = getProp(KEY_VERSION_EMUI))) {
            sName = ROM_EMUI;
        } else if (!TextUtils.isEmpty(sVersion = getProp(KEY_VERSION_OPPO))) {
            sName = ROM_OPPO;
        } else if (!TextUtils.isEmpty(sVersion = getProp(KEY_VERSION_VIVO))) {
            sName = ROM_VIVO;
        } else if (!TextUtils.isEmpty(sVersion = getProp(KEY_VERSION_SMARTISAN))) {
            sName = ROM_SMARTISAN;
        } else {
            sVersion = Build.DISPLAY;
            if (sVersion.toUpperCase().contains(ROM_FLYME)) {
                sName = ROM_FLYME;
            } else {
                sVersion = Build.UNKNOWN;
                sName = Build.MANUFACTURER.toUpperCase();
            }
        }
        return sName.equals(rom);
    }

    public static String getProp(String name) {
        String line = null;
        BufferedReader input = null;
        try {
            Process p = Runtime.getRuntime().exec("getprop " + name);
            input = new BufferedReader(new InputStreamReader(p.getInputStream()), 1024);
            line = input.readLine();
            input.close();
        } catch (IOException ex) {
            LogUtil.e(TAG, "Unable to read prop " + name);
            return null;
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return line;
    }

    public static Boolean canHuaWeiPush() {
        String emuiVersion;
        float currentVersion = 0.0f;
        try {
            Class cls = Class.forName("android.os.SystemProperties");
            Method method = cls.getDeclaredMethod("get", new Class[]{String.class});
            emuiVersion = (String) method.invoke(cls, new Object[]{"ro.build.version.emui"});
            emuiVersion = emuiVersion.replace("EmotionUI_", "");
            String[] temps = emuiVersion.split("\\.");
            currentVersion = Float.valueOf(temps[0]);
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
        float targetVersion = 5.0f;
        return currentVersion > targetVersion;

    }

    public static int getEmuiVersionCode() {
        Object returnObj = null;
        int emuiVersionCode = 0;
        try {
            Class<?> targetClass = Class.forName("com.huawei.android.os.BuildEx$VERSION");
            Field field = targetClass.getDeclaredField("EMUI_SDK_INT");
            returnObj = field.get(targetClass);
            if (null != returnObj) {
                emuiVersionCode = (int) returnObj;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return emuiVersionCode;
    }

}

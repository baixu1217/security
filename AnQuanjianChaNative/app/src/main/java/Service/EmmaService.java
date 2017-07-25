package Service;

import android.app.Service;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import android.app.Instrumentation;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by baixu3 on 2017/7/20.
 */


    public class EmmaService extends Service {

        private static final String TAG = "EmmaService";
        private static final String DEFAULT_COVERAGE_FILE_PATH = "/mnt/sdcard/coverage.ec";
        private final Bundle mResults = new Bundle();

        @Override
        public IBinder onBind(Intent intent) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void onCreate() {

            Log.i(TAG, "EmmaService==>>onCreate");
            super.onCreate();
            init();
            register();
        }

        private void init() {}

        private void register(){
            IntentFilter sdCarMonitorFilter = new IntentFilter();
            sdCarMonitorFilter.addAction(Intent.ACTION_MEDIA_MOUNTED);
            sdCarMonitorFilter.addAction(Intent.ACTION_MEDIA_UNMOUNTED);
            sdCarMonitorFilter.addDataScheme("file");
        }

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {

            Log.i(TAG, "EmmaService==>>onStartCommand");
            return super.onStartCommand(intent, flags, startId);
        }

        @Override
        public void onDestroy() {

            generateCoverageReport();
            Log.i(TAG, "EmmaService==>>onDestroy");
            super.onDestroy();
        }

        private void generateCoverageReport() {

            Log.i(TAG, "generateCoverageReport()");

            java.io.File coverageFile = new java.io.File(getCoverageFilePath());

            try {
                Class<?> emmaRTClass = Class.forName("com.vladium.emma.rt.RT");
                Method dumpCoverageMethod = emmaRTClass.getMethod(
                        "dumpCoverageData", coverageFile.getClass(), boolean.class,
                        boolean.class);
                dumpCoverageMethod.invoke(null, coverageFile, true, false);
            } catch (ClassNotFoundException e) {
                reportEmmaError("Emma.jar not in the class path?", e);
            } catch (SecurityException e) {
                reportEmmaError(e);
            } catch (NoSuchMethodException e) {
                reportEmmaError(e);
            } catch (IllegalArgumentException e) {
                reportEmmaError(e);
            } catch (IllegalAccessException e) {
                reportEmmaError(e);
            } catch (InvocationTargetException e) {
                reportEmmaError(e);
            }
        }

        private String getCoverageFilePath() {
            return DEFAULT_COVERAGE_FILE_PATH;
        }

        private void reportEmmaError(Exception e) {
            reportEmmaError("", e);
        }

        private void reportEmmaError(String hint, Exception e) {
            String msg = "Failed to generate emma coverage. " + hint;
            Log.e(TAG, msg, e);
            mResults.putString(Instrumentation.REPORT_KEY_STREAMRESULT, "\nError: " + msg);
        }

}

package com.example.compass;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mydictionary.R;

public class CompassMainActivity extends Activity implements SurfaceHolder.Callback , ActivityCompat.OnRequestPermissionsResultCallback{

    public static final String TAG = "Compass";
    public static final String SP_NAME = "Compass";
    private static final String SP_KEY_MAGNETIC_SWITCH = "MAGNETIC_SWITCH";
    private static final boolean SP_DEFAULT_MAGNETIC_SWITCH = false;
    private static final String SP_KEY_ALTI_UNIT_SWITCH = "ALTI_UNIT_SWITCH";
    private static final String SP_KEY_BARO_UNIT_SWITCH = "BARO_UNIT_SWITCH";
    // 1158862 -xin.zhao, modify -001 , begin
    private static final double ALTI_EXCHANGE_RATE = 3.2808399d;
    private static final double BARO_EXCHANGE_RATE = 0.1450377d;
    // 1158862 -xin.zhao, modify -001 , begin
    private static final int DEGREE_THRESHOLE = 60;
    private static int NEED_ACCURACY = SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM;
    private static final int DAILOG_ID_SELECT_MAGNETIC = 100;
    private static final float MAX_DEGREE = 360;
    private static final float PART_DEGREE = MAX_DEGREE / 8;
    private static final float HALF_PART_DEGREE = PART_DEGREE / 2;
    private static float SMOOTH_VALUE = 0.3f;
    private static final float MIN_REFRESH_VALUE = 0.2f;
    private static final int DISTANCE = 2;

    // 1414762 -lin.zhou, modify -001 , begin
    private static int SETTING_TYPE_LOCATION = 1;
    private static int SETTING_TYPE_CAMERA = 2;
    // 1414762 -lin.zhou, modify -001 , begin

    private ImageView mDial, mCalibrate;
    private Floatview mFloatview;
    private TextView mLable,mDegreenNumber,mTextLocation, mTextCalibrateDes;
    private SensorManager mSensorManager;
    private Sensor mOrientationSensor, mMagneticSensor, mAccelerometerSensor;
    private SensorEventListener mOrientationSensorEventListener,mMagneticSensorEventListener, 
	mAccelerometerSensorEventListener;
    private float mLastDegree = 0;
    private float mCurrentDegree = 0;
    private boolean isManuelCali = false;
    private Vibrator mVibrator = null;
    private String[] mDirectionText;
    @SuppressWarnings("unused")
    private int mMagneticOffset = 0;
    private boolean mMagneticChanged = false;
    private SharedPreferences mSharePrefrences;
    private SharedPreferences.Editor mEditor;
    private LocationListener netListener = null;
    private LocationListener gpsListener = null;
    private Location mLocation;
    private LocationManager mLocationManager;
    private NetworkConnectChangedReceiver mNetworkConnectChangedReceiver;
    private ConnectivityManager mConnectivityManager;
    private float mHistoryDegree;
    private boolean mLevelFlag = false;
    private boolean isInChina = false;
    private boolean isTablet = false;
    /**
     * 0:LANDSCAPE 1:PORTRAIT 2:AUTO
     */
    private String defaultOrientation = "1";
    private float[] mMagneticValue = new float[3], mAccelerometerValue = new float[3];
    private int mMagneticAccuracy = SensorManager.SENSOR_STATUS_ACCURACY_HIGH,
                mAccelerometerAccuracy = SensorManager.SENSOR_STATUS_ACCURACY_HIGH;
    private boolean orientationAbnormal = true;
    private static boolean USE_ORIENTATION = true;
    private Handler mHandler = new Handler();
    private int overCount;
    private static CompassMainActivity instance;
    private long startManuelCaliTime;
    private static long CHECK_ACCURACY_HIGH_TIME = 0;

    public static CompassMainActivity getInstance(){
        return instance;
    }
    private RelativeLayout degreeBgLayout;
    private OverlayArcView arcView;
    private int left;
    private int top;
    private int right;
    private int bottom;
    private int width;
    private int height;
    private int animationId;
    private boolean isNight = false;//CR907885-WEI.PENG
    private static final int SWITHC_TO_REAL_MODE=0;
    private static final int SWITHC_TO_COMPASS_MODE=1;
    private static final int SEARCH_ADRESS=2;
    private static final int SWITHC_FLAG=45;
    private static final int SWITHC_FLAG_BUFFER=2;
    private static final int MAX_ANGLE=30;
    private boolean isRealMode = false;
    //private FrameLayout mRealLayout;
    private RelativeLayout mCompassLayout,mTextDirection;
    private LinearLayout mLocationInfoLayout,mLocationInfoArea,mAddress;
    // 1121009 -xin.zhao, modify -001 , begin
    private RelativeLayout mFooter;
    // 1121009 -xin.zhao, modify -001 , end
    private TextView mLAT;
    private TextView mLON;
    private TextView mALTI;
    private TextView mBARO;

    private ImageView mRefreshAlti;
    private ImageView mRefreshBaro;

    private  LinearLayout mRefreshAltiLayout;
    private  LinearLayout mRefreshBaroLayout;
    private  LinearLayout mRefreshNotGpsLayout;

    private TextView mLocality;
    private TextView mThoroughfare;
    private TextView mSubLocality;
    private ImageView mLeftArrow,mRightArrow;
    private TextView mLeftArrowDirection,mRightArrowDirection;
    private SurfaceView mSurfaceView;
    private SurfaceHolder holder;
    private Camera mCamera;
    private int mRealHeight;
    private int mRealWidth;
    private boolean isGpsReady = false;
    private boolean isGoogleNetReady = false;
    private boolean isWindowFocusChanged = false;
    private Animation mAnimationIn,mAnimationInA;
    private Animation mAnimationOut;
    private int lastRotatin;
    // 1490222 -lin.zhou, add -001 , begin
    private static final String PERMISSIONBLOCK_ISFIRST = "isFirst";
    // 1490222 -lin.zhou, add -001 , end
    // 1694437 -lin.zhou, add -001 , begin
    private float screenRotation;
    // 1694437 -lin.zhou, add -001 , begin

    @SuppressLint("NewApi") @Override
    protected void onCreate(Bundle savedInstanceState) {
        //must before super.oncreate,if not the app will crash modify jiang
        //add by yunwen.jiang@tcl.com, fix bug 673107
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        instance = this;
        isRealMode = false;
        isInChina = Utils.getBoolean(this, "def_is_in_china");
        isTablet = Utils.getBoolean(this, "def_is_tablet");
        defaultOrientation = Utils.getString(this, "def_screen_orientation");
        USE_ORIENTATION = Utils.getBoolean(this, "def_use_orientation_sensor");
        String checkAccuracyHighTimeStr = Utils.getString(this, "def_check_accuracy_high_time");
        String needAccuracyStr = Utils.getString(this, "def_need_accuracy");//PR913987-WEi.PENG
        Log.i(TAG, "def_is_in_china: " + isInChina);
        Log.i(TAG, "def_is_tablet: " + isTablet);
        Log.i(TAG, "def_screen_orientation: " + defaultOrientation);
        Log.i(TAG, "def_use_orientation_sensor: " + USE_ORIENTATION);
        Log.i(TAG, "def_check_accuracy_high_time: " + checkAccuracyHighTimeStr);
        Log.i(TAG, "def_need_accuracy: " + needAccuracyStr);
        if(defaultOrientation != null && defaultOrientation.contains("\"")){
            defaultOrientation = defaultOrientation.replace("\"", "");
            Log.i(TAG, "modify def_screen_orientation: " + defaultOrientation);
        }
        if(checkAccuracyHighTimeStr != null && checkAccuracyHighTimeStr.contains("\"")){
            checkAccuracyHighTimeStr = checkAccuracyHighTimeStr.replace("\"", "");
            Log.i(TAG, "modify def_check_accuracy_high_time: " + checkAccuracyHighTimeStr);
        }
        try {
            CHECK_ACCURACY_HIGH_TIME = Long.valueOf(checkAccuracyHighTimeStr);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
        if(needAccuracyStr != null && needAccuracyStr.contains("\"")){
            needAccuracyStr = needAccuracyStr.replace("\"", "");
            Log.i(TAG, "modify def_need_accuracy: " + needAccuracyStr);
        }
        try {
            NEED_ACCURACY = Integer.valueOf(needAccuracyStr);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
        if(NEED_ACCURACY < SensorManager.SENSOR_STATUS_ACCURACY_LOW){
            NEED_ACCURACY = SensorManager.SENSOR_STATUS_ACCURACY_LOW;
        }
        if(NEED_ACCURACY > SensorManager.SENSOR_STATUS_ACCURACY_HIGH){
            NEED_ACCURACY = SensorManager.SENSOR_STATUS_ACCURACY_HIGH;
        }
        Log.i(TAG, "NEED_ACCURACY：" + NEED_ACCURACY);
        Log.i(TAG, "DEVICE: " + Build.DEVICE + ", isAutoStartManuelCali()= " + isAutoStartManuelCali());
        mSharePrefrences = getSharedPreferences(SP_NAME, MODE_PRIVATE);
        mEditor = mSharePrefrences.edit();
        setContentView(R.layout.activity_compass);
        initRersource();
        initView();
        Display d = this.getWindowManager().getDefaultDisplay();
        DisplayMetrics realDisplayMetrics = new DisplayMetrics();
        d.getRealMetrics(realDisplayMetrics);
        mRealHeight = realDisplayMetrics.heightPixels;
        mRealWidth = realDisplayMetrics.widthPixels;
        lastRotatin = d.getRotation();
        mVibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (mSensorManager != null) {
            mOrientationSensor = mSensorManager
                    .getDefaultSensor(Sensor.TYPE_ORIENTATION);
        }
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mConnectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        mOrientationSensorEventListener = new OrientationSensorEventListener();
        Log.i(TAG, "onCreate, mSensorManager: " + mSensorManager
                + ", mOrientationSensor: " + mOrientationSensor
                + ", mMagneticSensor: " + mMagneticSensor
                + ", mAccelerometerSensor: " + mAccelerometerSensor
                + ", mLocationManager: " + mLocationManager
                + ", mConnectivityManager: " + mConnectivityManager);

        // 1490222 -lin.zhou, add -002 , begin
        SharedPreferences mSharedPreferences = getSharedPreferences(CompassMainActivity.SP_NAME, Context.MODE_PRIVATE);
        boolean isFirst = mSharedPreferences.getBoolean(PERMISSIONBLOCK_ISFIRST,true);

        if(isFirst){
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putBoolean(PERMISSIONBLOCK_ISFIRST,false);
            editor.commit();
        }
        // 1490222 -lin.zhou, add -002 , begin

    }


    // 1414762 -lin.zhou, add -002 , begin
    private static final int MSGREQUESTLOCATIONUPDATE = 0x1001;
    private static final int MSGREMOVELOCATEIONUPDATE = 0x1002;

    private Handler mLocationHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSGREQUESTLOCATIONUPDATE: {
                    Log.d(TAG, "requestLocationUpdate checkpermission:"+(ActivityCompat.checkSelfPermission(CompassMainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)+","+(ActivityCompat.checkSelfPermission(CompassMainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED));
                    try {
                        if (ActivityCompat.checkSelfPermission(CompassMainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)
                                == PackageManager.PERMISSION_GRANTED) {
                            if(netListener == null){
                                netListener = new NetWorkLocationListener();
                            }
                            mLocationManager.requestLocationUpdates(
                                    LocationManager.NETWORK_PROVIDER, 1000, 0.1f, netListener);
                        }

                        if (ActivityCompat.checkSelfPermission(CompassMainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                                == PackageManager.PERMISSION_GRANTED) {

                            if (gpsListener == null) {
                                gpsListener = new GpsLocationListener();
                            }
                            mLocationManager.requestLocationUpdates(
                                    LocationManager.GPS_PROVIDER, 1000, 0.1f, gpsListener);
                        }
                    }catch (Exception e){
                        Log.e(TAG,"e:"+e.getMessage());
                    }
                    refreshLocation();
                    break;
                }
                case MSGREMOVELOCATEIONUPDATE: {
                    Log.e(TAG, "removeLocationUpdate");
                    if (mLocationManager != null) {
                        if(netListener != null){
                            mLocationManager.removeUpdates(netListener);
                            netListener = null;
                        }
                        if(gpsListener != null){
                            mLocationManager.removeUpdates(gpsListener);
                            gpsListener = null;
                        }
                    }
                    break;
                }
            }
        }
    };
    // 1414762 -lin.zhou, modify -002 , end

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mFloatview.recycleBitmap();
        instance = null;
    }


    @Override
    protected void onStart() {
        Log.i(TAG, "onStart");
        super.onStart();
        mHistoryDegree = 0;
        if (USE_ORIENTATION && mOrientationSensor != null) {
            Log.i(TAG, "register orientationSensor listener");
            mSensorManager.registerListener(mOrientationSensorEventListener,
                    mOrientationSensor, SensorManager.SENSOR_DELAY_FASTEST);
        } else {
            mOrientationSensor = null;
            registerMagneticSensor();
        }

        // 1414762 -lin.zhou, add -003 , begin
        mLocationHandler.sendEmptyMessage(MSGREQUESTLOCATIONUPDATE);
        // 1414762 -lin.zhou, add -003 , end

        if(degreeBgLayout != null){
            degreeBgLayout.invalidate();//PR929256-WEI.PENG
        }


        // 1537481 -lin.zhou, modify -001 , begin
        if(mNetworkConnectChangedReceiver ==null ){
            mNetworkConnectChangedReceiver = new NetworkConnectChangedReceiver();
        }
        IntentFilter filter=new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        this.registerReceiver(mNetworkConnectChangedReceiver, filter);
         //1537481 -lin.zhou, modify -001 , end

    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        Log.d(TAG, " onWindowFocusChanged ");
        isWindowFocusChanged = true;
        initFloatView();
        // 1537481 -lin.zhou, modify -002 , begin
        refreshLocation();
        //1537481 -lin.zhou, modify -002 , end
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 1414762 -lin.zhou, add -004 , begin
        mLocationHandler.sendEmptyMessage(MSGREMOVELOCATEIONUPDATE);
        // 1414762 -lin.zhou, add -004 , end
    }

    @Override
    protected void onStop() {
      //protected void onPause() { //PR861413-WEI.PENG
        Log.i(TAG, "onStop");
        super.onStop();
        isWindowFocusChanged = false;
        if (mOrientationSensor != null) {
            Log.i(TAG, "unrregister orientationSensor listener");
            mSensorManager.unregisterListener(mOrientationSensorEventListener,
                    mOrientationSensor);
        }
        if(mMagneticSensor != null){
            Log.i(TAG, "unrregister magneticSensor listener");
            mSensorManager.unregisterListener(mMagneticSensorEventListener,
                    mMagneticSensor);
        }
        if(mAccelerometerSensor != null){
            Log.i(TAG, "unrregister accelerometerSensor listener");
            mSensorManager.unregisterListener(mAccelerometerSensorEventListener,
                    mAccelerometerSensor);
        }
        if (mNetworkConnectChangedReceiver != null) {
            unregisterReceiver(mNetworkConnectChangedReceiver);
        }
    }


    public void setforceOrientation(int forceOrientation){
        Log.i(TAG, "setforceOrientation: " + forceOrientation);
        if(forceOrientation == ForceOrientationReceiver.ORIENTATION_DEFAULT){
            if ("0".equals(defaultOrientation)) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            } else if ("1".equals(defaultOrientation)) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            } else if("2".equals(defaultOrientation)) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
            } else{
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }

    private void startManuelCali() {

        // 1396658 -xin.zhao, modify -001 , begin
        mRefreshNotGpsLayout.setVisibility(View.GONE);
        // 1396658 -xin.zhao, modify -001 , begin

        if (isManuelCali) {
            return;
        }
        Log.i(TAG, "startManuelCali");
        isManuelCali = true;
        mLastDegree = mCurrentDegree;
        isRealMode = false;
        updateViewInCompassMode(isRealMode);
        updateViewInCalibrateMode(isManuelCali);
        AnimationDrawable anim = (AnimationDrawable) mCalibrate.getDrawable();
        anim.start();
        startManuelCaliTime = System.currentTimeMillis();
    }

    private void stopManuelCali() {
        if (!isManuelCali) {
            return;
        }
        Log.i(TAG, "stopManuelCali");
        mVibrator.vibrate(300);
        isManuelCali = false;
        updateViewInCalibrateMode(isManuelCali);
        AnimationDrawable anim = (AnimationDrawable) mCalibrate.getDrawable();
        anim.stop();
        updateViewInCompassMode(false);
        refreshLocation();
    }

    private void switchMagic(boolean on_off) {
        if (on_off) {
            mMagneticOffset = DISTANCE;
        } else {
            mMagneticOffset = 0;
        }
        mMagneticChanged = true;
    }


    private void refreshTextViewDegree(float degree,float historyDegree){
            if (isRealMode){
                mTextDirection.setBackgroundResource(R.drawable.round_black);
                mDegreenNumber.setTextColor(getResources().getColor(R.color.real_mode_fon));
                mLable.setTextColor(getResources().getColor(R.color.real_mode_fon));
                //[BUGFIX]-Add-BEGIN by TSCD.xing.zhao,12/08/2015,1061368,“< N” is  redundant in real view mode UI when MS is at true north direction
                // 1452389 -lin.zhou, modify -001 , start
                int degreeTemp = ((int) Math.floor(degree));
                if((degreeTemp>=0&&degreeTemp<=22)||(degreeTemp>=337&&degreeTemp<=359)){
                    mLeftArrow.setVisibility(View.INVISIBLE);
                    mLeftArrowDirection.setVisibility(View.INVISIBLE);
                    mRightArrow.setVisibility(View.INVISIBLE);
                    mRightArrowDirection.setVisibility(View.INVISIBLE);
                }/*//[BUGFIX]-Add-END by TSCD.xing.zhao**/
                else if (22< degreeTemp && degreeTemp <= 180 ) {
                    mLeftArrow.setVisibility(View.VISIBLE);
                    mLeftArrowDirection.setVisibility(View.VISIBLE);
                    mRightArrow.setVisibility(View.INVISIBLE);
                    mRightArrowDirection.setVisibility(View.INVISIBLE);
                } else if (180 < degreeTemp && degreeTemp < 337 ){
                    mRightArrow.setVisibility(View.VISIBLE);
                    mRightArrowDirection.setVisibility(View.VISIBLE);
                    mLeftArrow.setVisibility(View.INVISIBLE);
                    mLeftArrowDirection.setVisibility(View.INVISIBLE);
                }
                // 1452389 -lin.zhou, modify -001 , end
            }else {
                mDegreenNumber.setTextColor(getResources().getColor(R.color.compass_degreen_default));
                mLable.setTextColor(getResources().getColor(R.color.compass_label_default));
                mLeftArrow.setVisibility(View.INVISIBLE);
                mLeftArrowDirection.setVisibility(View.INVISIBLE);
                mRightArrow.setVisibility(View.INVISIBLE);
                mRightArrowDirection.setVisibility(View.INVISIBLE);
                mTextDirection.setBackgroundResource(0);
            }
        mDegreenNumber.setText((int) Math.floor(degree) + getString(R.string.degree_suffix));
        mLable.setText(getDirectionText(historyDegree));
        if (0 == (int) Math.floor(degree)) {
            mDegreenNumber.setTextColor(getResources().getColor(R.color.compass_degreen_noth));
            mLable.setTextColor(getResources().getColor(R.color.compass_degreen_noth));
        }
    }

    private void refreshDegree(float degree) {
        float offset1 = 0f;
        float offset2 = 0f;
        offset1 = (degree - mHistoryDegree) > MAX_DEGREE / 2 ? MAX_DEGREE : 0f;
        offset2 = (mHistoryDegree - degree) > MAX_DEGREE / 2 ? MAX_DEGREE : 0f;
        float historyDegree = (mHistoryDegree + offset1)
                * (1.0f - SMOOTH_VALUE) + (degree + offset2) * SMOOTH_VALUE;
        historyDegree %= MAX_DEGREE;
        if (Math.abs(historyDegree - mHistoryDegree) < MIN_REFRESH_VALUE
                && !mMagneticChanged) {
            return;
        } else {
            mHistoryDegree = historyDegree;
            mMagneticChanged = false;
        }

        screenRotation = getScreenDegree();
        historyDegree += screenRotation;
        historyDegree %= MAX_DEGREE;
        mDial.setRotation(-historyDegree);
        mDial.invalidate();
        if (arcView != null) {
            arcView.setProgerss((int) historyDegree);
        }

        //[BUGFIX]-Add-BEGIN by TSCD.lin.zhou,12/28/2015,1240203
        if(screenRotation == MAX_DEGREE / 2){
            degree += MAX_DEGREE;
            degree -= MAX_DEGREE / 2;
            degree %= MAX_DEGREE;
        }
        //[BUGFIX]-Add-BEGIN by TSCD.lin.zhou,12/28/2015,1240203

        refreshTextViewDegree(degree, historyDegree);
    }
    private void refreshFloatView(float[] values) {
        //float[] values = event.values;
        float yAngle = values[1];
        float zAngle = values[2];

        // 1694437 -lin.zhou, add -002 , begin
        if(screenRotation == MAX_DEGREE / 2){
            yAngle = -yAngle;
            zAngle = -zAngle;
        }
        // 1694437 -lin.zhou, add -002 , begin

        int [] ints =  new int [] {0,0};
        if (Math.sqrt(yAngle*yAngle*1.2*1.2 + zAngle*zAngle*1.2*1.2) > mFloatview.getWidth()/3){
            return;
        }
        if (Math.abs(yAngle) <=2 && Math.abs(zAngle) <= 2 && !mLevelFlag) {
            mLevelFlag = true;
            //mSoundPool.play(1,1,1,0,0,0);
            degreeBgLayout.setBackgroundResource(R.drawable.dial_bg_b);
        } else if (Math.abs(yAngle) >2 || Math.abs(zAngle) > 2){
            mLevelFlag = false;
            degreeBgLayout.setBackgroundResource(R.drawable.dial_bg_a);
        }
        Rect floatViewRect = new Rect();
        mFloatview.getLocationInWindow(ints);
        mFloatview.getLocalVisibleRect(floatViewRect);
        mFloatview.centerX = floatViewRect.width()/2;
        mFloatview.centerY = floatViewRect.height()/2;
        mFloatview.mRadius = mFloatview.getWidth()/6;
        mFloatview.bubbleX = floatViewRect.width()/2 - (int)(zAngle*1.2);
        mFloatview.bubbleY = floatViewRect.height()/2 - (int)(yAngle*1.2);
        mFloatview.postInvalidate();
    }

    private float getScreenDegree(){
        float screenRotation = 0;
        int rotation = getWindowManager().getDefaultDisplay().getRotation();
        switch (rotation) {
        case Surface.ROTATION_0:
            screenRotation = 0;
            break;
        case Surface.ROTATION_90:
            screenRotation = 90;
            break;
        case Surface.ROTATION_180:
            screenRotation = 180;
            break;
        case Surface.ROTATION_270:
            screenRotation = 270;
            break;
        }
        return screenRotation;
    }

    private void checkAccuracy(int accuracy) {
        if(isManuelCali){
            if (Math.abs(mCurrentDegree - mLastDegree) > DEGREE_THRESHOLE){
                long time = System.currentTimeMillis() - startManuelCaliTime;
                Log.i(TAG, "checkAccuracy, mCurrentDegree: " + mCurrentDegree
                        + ", mLastDegree: " + mLastDegree + ", accuracy: " + accuracy + ", time: " + time);
                mLastDegree = mCurrentDegree;
                if(accuracy >= SensorManager.SENSOR_STATUS_ACCURACY_HIGH
                        || (time > CHECK_ACCURACY_HIGH_TIME && accuracy >= NEED_ACCURACY)){
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            stopManuelCali();
                        }
                    }, 1000);
                }
            }
        }else if (isWindowFocusChanged){
            //PR910050-WEI.PENG BEGIN
            if (accuracy < NEED_ACCURACY) {
                if(isAutoStartManuelCali()){
                    Log.i(TAG, "Auto start manuel cali");
                    startManuelCali();
                }else{
                    Log.i(TAG, "Block auto start manuel cali, DEVICE: " + Build.DEVICE);
                }
            }
          //PR910050-WEI.PENG END
        } else if(accuracy < NEED_ACCURACY) {
            Log.d(TAG," checkAccuracy accuracy = " + accuracy + " isManuelCali = " + isManuelCali);
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startManuelCali();
                }
            }, 500);
        }
    }

    private String getDirectionText(float degree) {
        int i = (int) ((degree + HALF_PART_DEGREE) % MAX_DEGREE / PART_DEGREE);
        return mDirectionText[i];
    }

    private boolean isGpsEnabled() {
        return mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    private boolean isWifiEnabled() {
        NetworkInfo netInfo = mConnectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (netInfo == null) {
            return false;
        }
        return netInfo.getState() == State.CONNECTED;
    }

    private boolean isMobileNetEnabled() {
        NetworkInfo netInfo = mConnectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (netInfo == null) {
            return false;
        }
        return netInfo.getState() == State.CONNECTED;
    }


    private String getLatitudeString(double latitude){
        latitude = Math.abs(latitude);
        int d = (int) latitude;
        int m = (int) ((latitude - d) * 60);
        int s = (int) ((latitude - d - m/60f) * 3600);
        return " " + d + "° " + m + "′ " + s + "″ ";
    }


    private void registerMagneticSensor() {
        Log.i(TAG, "register magnetic sensor listener");
        SMOOTH_VALUE = 0.03f;
        if(mMagneticSensor == null){
            mMagneticSensor = mSensorManager
                .getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
            mMagneticSensorEventListener = new MagneticSensorEventListener();
        }
        if(mAccelerometerSensor == null){
            mAccelerometerSensor = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            mAccelerometerSensorEventListener = new AccelerometerSensorEventListener();
        }
        if(mMagneticSensor == null || mAccelerometerSensor == null){
            Log.i(TAG, "device_not_support");
            Toast.makeText(this, R.string.device_not_support, Toast.LENGTH_LONG).show();
            return;
        }
        mSensorManager.registerListener(mMagneticSensorEventListener,
                mMagneticSensor, SensorManager.SENSOR_DELAY_FASTEST);
        mSensorManager.registerListener(mAccelerometerSensorEventListener,
                mAccelerometerSensor, SensorManager.SENSOR_DELAY_FASTEST);
    }

    private void calculateMagnetic(){
        float[] values = new float[3];
        float[] R = new float[9];
        boolean result = SensorManager.getRotationMatrix(R, null, mAccelerometerValue, mMagneticValue);
        if(result){
            SensorManager.getOrientation(R, values);
            float degree = (float) Math.toDegrees(values[0]);
            degree = degree < 0?degree%MAX_DEGREE+MAX_DEGREE:degree;
            if (mHistoryDegree == 0) {
                mHistoryDegree = degree;
            }
            if(Math.abs(degree - mCurrentDegree) > 60 && overCount < 5){
                overCount++;
                return;
            }
            overCount = 0;
            mCurrentDegree = degree;
            checkAccuracy(Math.min(mMagneticAccuracy, mAccelerometerAccuracy));
            if (!isManuelCali) {
                refreshDegree(degree);
            }
        }else{
            Log.v(TAG, "getRotationMatrix fail!!!");
        }
    }

  //PR910050-WEI.PENG
    private boolean isNexus6(){
        return "shamu".equals(Build.DEVICE);
    }

  //PR910050-WEI.PENG
    private boolean isAutoStartManuelCali(){
        return !isNexus6();
    }

    /**
     * CR907885-WEI.PENG
     *
     * @param night true:white false:black
     */
    private Bitmap getDialImg(boolean night){
        long t1 = System.currentTimeMillis();
        try {
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setFilterBitmap(true);
            Bitmap img = ((BitmapDrawable)getResources().getDrawable(
                    night?R.drawable.dial_pointer_black:R.drawable.dial_pointer_white)).getBitmap();
            Bitmap outImt = Bitmap.createBitmap(img.getWidth(), img.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(outImt);
            canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG));
            canvas.drawBitmap(img, 0, 0, paint);
            float density = getResources().getDisplayMetrics().density;
            float scale = canvas.getWidth()/(322 * density);
            int bigSize = (int) (20 * density * scale + 0.5f);
            int smallSize = (int) (12 * density * scale + 0.5f);
            float bigY = img.getHeight()*0.152f;
            float smallY = img.getHeight()*0.133f;
            paint.setTextAlign(Paint.Align.CENTER);
            paint.setColor(night?0XFFF44336:0XFFF44336);
            paint.setTextSize(bigSize);
            canvas.drawText(mDirectionText[0], img.getWidth()*0.5f, bigY, paint);
            for(int i=1;i<mDirectionText.length;i++){
                boolean isBig = i%2==0;
                int textSize;
                float textY;
                if(isBig){
                    textSize = bigSize;
                    textY = bigY;
                    paint.setColor(night?0XFFB0BEC5:0XFF7A7A7A);
                }else{
                    textSize = smallSize;
                    textY = smallY;
                    paint.setColor(night?0XFF607D8B:0XFFAFAFAF);
                }
                paint.setTextSize(textSize);
                canvas.rotate(PART_DEGREE, canvas.getWidth()/2, canvas.getHeight()/2);
                canvas.drawText(mDirectionText[i], img.getWidth()*0.5f, textY, paint);
            }
            canvas.rotate(PART_DEGREE, canvas.getWidth()/2, canvas.getHeight()/2);
            long t2 = System.currentTimeMillis();
            Log.i(TAG, "getDialImg time: " + (t2 - t1));
            return outImt;
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return null;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d(TAG,"surfaceCreated");
        try {
            if (mCamera == null) {
                mCamera = Camera.open(0);
                // 967226 -yunwen.jiang, modify -001 , begin
//                mCamera.setDisplayOrientation(90);
                setCameraDisplayOrientation();
                // 967226 -yunwen.jiang, modify -001 , end
                mCamera.setPreviewDisplay(holder);
            }
        }catch (Exception exception) {
            exception.printStackTrace();
            Log.e(TAG, exception.getMessage());
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.d(TAG,"surfaceChanged");
        try {
            if (mCamera != null) {
                // 967226 -yunwen.jiang, modify -001 , begin
                setCameraDisplayOrientation();
                // 967226 -yunwen.jiang, modify -001 , end
                mCamera.startPreview();
            }
        }catch (Exception exception) {
            Log.d("pmt111","mCamera = " + mCamera);
            exception.printStackTrace();
            Log.e(TAG, exception.getMessage());
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d(TAG,"surfaceDestroyed");
        if (mCamera != null) {
            try {
                mCamera.stopPreview();
                mCamera.release();
                mCamera = null;
            } catch (Exception e) {
                Log.e(TAG,e.getMessage());
                e.printStackTrace();
            }
        }
    }

    // 967226 -yunwen.jiang, modify -001 , begin
    private void setCameraDisplayOrientation(){
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo( 0 , info);
        int rotation = CompassMainActivity.this.getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0: degrees = 0; break;
            case Surface.ROTATION_90: degrees = 90; break;
            case Surface.ROTATION_180: degrees = 180; break;
            case Surface.ROTATION_270: degrees = 270; break;
        }
        int result;
        if(info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT){
            result = (info.orientation + degrees) % 360 ;
            result = (360 -result) % 360;
        } else {
            result = (info.orientation - degrees + 360) % 360;
        }
        if (mCamera != null) {
                mCamera.setDisplayOrientation(result);
        }
    }
    // 967226 -yunwen.jiang, modify -001 , end
    private class GpsLocationListener implements LocationListener {

        public void onLocationChanged(Location location) {
            Log.i(TAG, "gps onLocationChanged: " + location);
            refreshLocation();
        }

        public void onProviderDisabled(String provider) {
            Log.i(TAG, "gps onProviderDisabled");
            refreshLocation();
        }

        public void onProviderEnabled(String provider) {
            Log.i(TAG, "gps onProviderEnabled");
            refreshLocation();
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };

    private class NetWorkLocationListener implements LocationListener {

        public void onLocationChanged(Location location) {
            Log.i(TAG, "network onLocationChanged: " + location);
            refreshLocation();
        }

        public void onProviderDisabled(String provider) {
            Log.i(TAG, "network onProviderDisabled");
            refreshLocation();
        }

        public void onProviderEnabled(String provider) {
            Log.i(TAG, "network onProviderEnabled");
            refreshLocation();
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };

    private class NetworkConnectChangedReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                refreshLocation();
            }
        }
    }

    private class OrientationSensorEventListener implements SensorEventListener {
        @Override
        public void onSensorChanged(SensorEvent event) {
            if(orientationAbnormal && event.values[0] != 0 && !Float.isNaN(event.values[0])){
                Log.i(TAG, "onOrientationSensor changed");
                orientationAbnormal = false;
            }
            if(Float.isNaN(event.values[0]) || null == arcView || !isWindowFocusChanged){
                return;
            }
            int accuracy = event.accuracy;
            float degree = event.values[0];
            degree = degree < 0?degree%MAX_DEGREE + MAX_DEGREE : degree;
            //[BUGFIX]-Add-BEGIN by TSCD.xing.zhao,12/07/2015,977593,[Compass][Android 6.0]Traditional mode screen doesn't have data and blue slider fills whole circle
            refreshDegree(degree);
            //[BUGFIX]-Add-END by TSCD.xing.zhao
            if (mHistoryDegree == 0) {
                mHistoryDegree = degree;
            }
            mCurrentDegree = degree;
            //[BUGFIX]-Add-BEGIN by TSCD.xing.zhao,12/02/2015,981671,[Compass][Android 6.0]The image is inverted in real view mode after reversing Mobile
            int rotation = CompassMainActivity.this.getWindowManager().getDefaultDisplay().getRotation();
            if (rotation == Surface.ROTATION_180 || rotation == Surface.ROTATION_0) {
                if (lastRotatin != rotation) {
                    Log.i(TAG, "Orientation changed setCameraDisplayOrientation");
                    lastRotatin = rotation;
                    setCameraDisplayOrientation();
                }
            }
            //[BUGFIX]-Add-END by TSCD.xing.zhao
            checkAccuracy(accuracy);
            if (!isManuelCali) {
                float[] values = event.values;
                float yAngle = values[1];
                float zAngle = values[2];
                refreshDegree(degree);
                if (!isRealMode){
                    refreshFloatView(event.values);
                }
                if((Math.abs(yAngle) > SWITHC_FLAG + SWITHC_FLAG_BUFFER || Math.abs(zAngle) > SWITHC_FLAG + SWITHC_FLAG_BUFFER) && !isRealMode) {
                    // 1095228 -yunwen.jiang, modify -001 , begin
                    if(PermissionChecker.checkSelfPermission(CompassMainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED && !requestPermissions){
//                        requestPermissions = true;
//                        ActivityCompat.requestPermissions(Compass.this, new String[]{Manifest.permission.CAMERA}, PermissionUtil.CHECK_REQUEST_PERMISSION_RESULT);
                        if(!later) {
                            showTryRealView();
                        }
                    }else if(!requestPermissions){
                        isRealMode = true;
                        updateViewInCompassMode(isRealMode);
                        if (!mAnimationIn.hasEnded()) {
                            mAnimationIn.cancel();
                        }
                        mCompassLayout.startAnimation(mAnimationOut);
                    }
                    // 1095228 -yunwen.jiang, modify -001 , end
                } else if ((Math.abs(yAngle) <= SWITHC_FLAG - SWITHC_FLAG_BUFFER && Math.abs(zAngle) <= SWITHC_FLAG -SWITHC_FLAG_BUFFER) && isRealMode) {
                    isRealMode = false;
                    if (!mAnimationOut.hasEnded()) {
                        mAnimationOut.cancel();
                    }
                    mCompassLayout.startAnimation(mAnimationIn);
                    updateViewInCompassMode(isRealMode);
/*                    mAnimationIn.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            updateViewInCompassMode(!isRealMode);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });*/
                }
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            Log.i(TAG, "onAccuracyChanged: " + accuracy);
            checkAccuracy(accuracy);
        }
    }

    private class MagneticSensorEventListener implements SensorEventListener {

        @Override
        public void onSensorChanged(SensorEvent event) {
            if(!isWindowFocusChanged){
                return;
            }
            mMagneticValue = event.values;
            mMagneticAccuracy = event.accuracy;
            calculateMagnetic();
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            Log.i(TAG, "Magnetic accuracy changed: " + accuracy);
            if(!isWindowFocusChanged){
                return;
            }
            mMagneticAccuracy = accuracy;
            checkAccuracy(Math.min(mMagneticAccuracy, mAccelerometerAccuracy));
        }
    }

    private class AccelerometerSensorEventListener implements SensorEventListener {

        @Override
        public void onSensorChanged(SensorEvent event) {
            if(!isWindowFocusChanged){
                return;
            }
            mAccelerometerValue = event.values;
            mAccelerometerAccuracy = event.accuracy;
            calculateMagnetic();
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            Log.i(TAG, "Accelerometer accuracy changed: " + accuracy);
            if(!isWindowFocusChanged){
                return;
            }
            mAccelerometerAccuracy = accuracy;
            checkAccuracy(Math.min(mMagneticAccuracy, mAccelerometerAccuracy));
        }
    }

    class UpdateAddressTask extends AsyncTask<double [], Integer, Address> {

        @Override
        protected Address doInBackground(double[]... params) {
            Address nowAddress = null;
            try {
                Geocoder geocoder = new Geocoder(CompassMainActivity.getInstance(),Locale.getDefault());
                if(geocoder.isPresent()){
                    List address = geocoder.getFromLocation(mLocation.getLatitude(),mLocation.getLongitude(), 5);
                    if (address != null && address.size() != 0) {
                        nowAddress = (Address) address.get(0);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return nowAddress;
        }

        @Override
        protected void onPostExecute(Address address) {
            if (address != null) {
                if (mCompassLayout.getVisibility() == View.VISIBLE){
                    mAddress.setVisibility(View.GONE);
                } else {
                    mAddress.setVisibility(View.VISIBLE);
                }
                //mLocality.setText(((address.getAdminArea() == null ? "":address.getAdminArea())) + (address.getSubAdminArea() == null ? "":address.getSubAdminArea()) + (address.getLocality() == null ? "":address.getLocality()));
                //mSubLocality.setText(address.getAddressLine(0));
                //add by yunwen.jiang@tcl.com, fix bug 721170
                if(address.getMaxAddressLineIndex() < 1) return;
                //add by yunwen.jiang@tcl.com, fix bug 721170
                String [] locacity =  address.getAddressLine(1).split(",");
                if (locacity.length<=2){
                    mSubLocality.setText(locacity[0].trim());
                    mLocality.setText(locacity[locacity.length-1].trim());
                } else {
                    mLocality.setText(address.getLocality());
                    StringBuffer stringBuffer= new StringBuffer("");
                    for (int i=0;i<locacity.length-2;i++){
                        stringBuffer.append(locacity[i]);
                    }
                    mSubLocality.setText(stringBuffer.toString());
                }
                mThoroughfare.setText(address.getAddressLine(0));
            }
        }
    }
    void updateViewInCalibrateMode( boolean isCalibrateMode){
        if (isCalibrateMode){
            // 1121009 -xin.zhao, modify -001 , begin
            setFooterHeight(false);
            // 1121009 -xin.zhao, modify -001 , end
            mTextLocation.setVisibility(View.GONE);
            mLocationInfoArea.setVisibility(View.GONE);
            mAddress.setVisibility(View.GONE);
            mTextDirection.setVisibility(View.GONE);
            degreeBgLayout.setVisibility(View.GONE);
            mDial.setVisibility(View.INVISIBLE);
            mCalibrate.setVisibility(View.VISIBLE);
            mTextCalibrateDes.setVisibility(View.VISIBLE);
            if (arcView != null) {
                degreeBgLayout.removeView(arcView);
            }
        } else {
            mCalibrate.setVisibility(View.INVISIBLE);
            degreeBgLayout.setVisibility(View.VISIBLE);
            mTextDirection.setVisibility(View.VISIBLE);
            // 1121009 -xin.zhao, modify -001 , begin
            setFooterHeight(true);
            // 1121009 -xin.zhao, modify -001 , end
            mTextLocation.setVisibility(View.VISIBLE);
            mLocationInfoArea.setVisibility(View.VISIBLE);
            mTextDirection.setVisibility(View.VISIBLE);
            mDial.setVisibility(View.VISIBLE);
            mTextCalibrateDes.setVisibility(View.INVISIBLE);
            if (arcView != null) {
                degreeBgLayout.addView(arcView);
            }
        }
    }

    void updateViewInCompassMode(boolean isRealMode){
        if(!isRealMode) {
            mCompassLayout.setVisibility(View.VISIBLE);
            mTextDirection.setBackgroundResource(0);
            mRightArrow.setVisibility(View.GONE);
            mRightArrowDirection.setVisibility(View.GONE);
            mLeftArrow.setVisibility(View.GONE);
            mLeftArrowDirection.setVisibility(View.GONE);
            mAddress.setVisibility(View.GONE);
            ((TextView)this.findViewById(R.id.latitude)).setTextColor(getResources().getColor(R.color.compass_gps_info));;
            ((TextView)this.findViewById(R.id.longitude)).setTextColor(getResources().getColor(R.color.compass_gps_info));
            ((TextView)this.findViewById(R.id.altitude)).setTextColor(getResources().getColor(R.color.compass_gps_info));
            ((TextView)this.findViewById(R.id.baro)).setTextColor(getResources().getColor(R.color.compass_gps_info));
            mLAT.setTextColor(getResources().getColor(R.color.compass_gps_info_value));
            mLON.setTextColor(getResources().getColor(R.color.compass_gps_info_value));
            mALTI.setTextColor(getResources().getColor(R.color.compass_gps_info_value));
            mBARO.setTextColor(getResources().getColor(R.color.compass_gps_info_value));
            //mRealLayout.setVisibility(View.GONE);
           // mSurfaceView.setVisibility(View.GONE);
        } else {
            //mRealLayout.setVisibility(View.VISIBLE);
            mSurfaceView.setVisibility(View.VISIBLE);
            ((TextView)this.findViewById(R.id.latitude)).setTextColor(getResources().getColor(R.color.real_mode_fon));;
            ((TextView)this.findViewById(R.id.longitude)).setTextColor(getResources().getColor(R.color.real_mode_fon));
            ((TextView)this.findViewById(R.id.altitude)).setTextColor(getResources().getColor(R.color.real_mode_fon));
            ((TextView)this.findViewById(R.id.baro)).setTextColor(getResources().getColor(R.color.real_mode_fon));
            mLAT.setTextColor(getResources().getColor(R.color.real_mode_fon));
            mLON.setTextColor(getResources().getColor(R.color.real_mode_fon));
            mALTI.setTextColor(getResources().getColor(R.color.real_mode_fon));
            mBARO.setTextColor(getResources().getColor(R.color.real_mode_fon));
            mCompassLayout.setVisibility(View.GONE);
            mAddress.setVisibility(mLocationInfoArea.getVisibility());
        }
    }
    void initView(){
       // mRealLayout =  (FrameLayout) findViewById(R.id.real);
        mTextDirection = (RelativeLayout) findViewById(R.id.textDirection);
        mLocationInfoArea = (LinearLayout) findViewById(R.id.locationInfoArea);
        mCompassLayout = (RelativeLayout) findViewById(R.id.compass);
        mSurfaceView = (SurfaceView)this.findViewById(R.id.SurfaceView);
        // 1121009 -xin.zhao, modify -001 , begin
        mFooter = (RelativeLayout)this.findViewById(R.id.footer);
        // 1121009 -xin.zhao, modify -001 , end
        mLAT = (TextView)this.findViewById(R.id.latitudeValue);
        mLON = (TextView)this.findViewById(R.id.longitudeValue);
        mALTI = (TextView)this.findViewById(R.id.altitudValue);
        mBARO = (TextView)this.findViewById(R.id.baroValue);

        mRefreshAlti = (ImageView)this.findViewById(R.id.refresh_altitude);
        mRefreshBaro = (ImageView)this.findViewById(R.id.refresh_baroValue);

        mRefreshAltiLayout = (LinearLayout) findViewById(R.id.layout_refresh1);
        mRefreshBaroLayout = (LinearLayout) findViewById(R.id.layout_refresh2);
        mRefreshNotGpsLayout = (LinearLayout) findViewById(R.id.refresh_textLocation_line);

        mALTI.setOnClickListener(clickListener);
        mBARO.setOnClickListener(clickListener);
        mRefreshAltiLayout.setOnClickListener(clickListener);
        mRefreshBaroLayout.setOnClickListener(clickListener);
        mRefreshNotGpsLayout.setOnClickListener(clickListener);
        // 1392506 -xin.zhao, modify -001 , begin
        // mRefreshAlti.setOnClickListener(clickListener);
        // mRefreshBaro.setOnClickListener(clickListener);
        // 1392506 -xin.zhao, modify -001 , end

        mLeftArrow = (ImageView)this.findViewById(R.id.leftArrow);
        mRightArrow = (ImageView)this.findViewById(R.id.rightArrow);
        // 1552176 -lin.zhou, add -001 , begin
        mLeftArrowDirection = (TextView)this.findViewById(R.id.leftArrowDirection);
        mRightArrowDirection = (TextView)this.findViewById(R.id.rightArrowDirection);
        mLeftArrowDirection.setText(mDirectionText[0]);
        mRightArrowDirection.setText(mDirectionText[0]);
        // 1552176 -lin.zhou, add -001 , end
        holder = mSurfaceView.getHolder();
        holder.addCallback(this);
        mFloatview = (Floatview) this.findViewById(R.id.floatview);
        mCalibrate = (ImageView) findViewById(R.id.calibrate);
        mDegreenNumber = (TextView) findViewById(R.id.degreenNumber);
        mLable = (TextView) findViewById(R.id.lable);
        degreeBgLayout = (RelativeLayout) findViewById(R.id.degreeBg);
        mTextLocation = (TextView) findViewById(R.id.textLocation);
        mDial = (ImageView) findViewById(R.id.dial);
        mAddress = (LinearLayout) findViewById(R.id.address);
        mLocality = (TextView) findViewById(R.id.locality);
        mSubLocality = (TextView) findViewById(R.id.subLocality);
        mThoroughfare = (TextView) findViewById(R.id.thoroughfare);
        mTextCalibrateDes = (TextView)findViewById(R.id.textCalibrateDes);
        if(isTablet){
            mTextCalibrateDes.setText(getString(R.string.note_tablet));
        }else{
            mTextCalibrateDes.setText(getString(R.string.note));
        }
        degreeBgLayout.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onGlobalLayout() {
                        try {
                            mCalibrate=(ImageView)findViewById(R.id.calibrate);
//                            mCalibrate.setImageResource(animationId);
                            //CR907885-WEI.PENG BEGIN
                            Bitmap dialImg = getDialImg(isNight);
                            if(dialImg == null){
                                mDial.setImageResource(isNight?R.drawable.dial_pointer_black:R.drawable.dial_pointer_white);
                            }else{
                                mDial.setImageBitmap(dialImg);
                            }
                            degreeBgLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                            if (arcView != null) {
                                degreeBgLayout.removeView(arcView);
                            }
                            left = degreeBgLayout.getLeft();
                            top  = degreeBgLayout.getTop();
                            right = degreeBgLayout.getRight();
                            bottom = degreeBgLayout.getBottom();
                            width = degreeBgLayout.getWidth();
                            height = degreeBgLayout.getHeight();
                            arcView = new OverlayArcView(CompassMainActivity.this);
                            DisplayMetrics dm = getResources().getDisplayMetrics();
                            int density = (int) dm.density;
                            int densityDpi = dm.densityDpi;
                            arcView.setArcPosition((int) (left + (width / 2) * 39 / 322),
                                    (int) (top + (height / 2) * 39 / 322),
                                    (int) (right - (width / 2) * 39 / 322),
                                    (int) (bottom - (height / 2) * 39 / 322));
                            if (mDial.getVisibility() == View.VISIBLE) {
                                degreeBgLayout.addView(arcView);
                            }
                            arcView.setProgerss((int)-mDial.getRotation());//PR929256-WEI.PENG
                            degreeBgLayout.invalidate();//PR929256-WEI.PENG
                        } catch (Exception e) {
                            Log.e(TAG, e.getMessage(), e);
                        }
                    }
                });
        refreshFloatView(new float[] {0,0,0});
    }
    void initRersource(){
        mDirectionText = getResources().getStringArray(R.array.text_direction_short);
        animationId = R.drawable.calibrate_dark;
        mAnimationIn = AnimationUtils.loadAnimation(CompassMainActivity.this, R.anim.compassviewin);
        mAnimationInA = AnimationUtils.loadAnimation(CompassMainActivity.this, R.anim.compassviewina);
        mAnimationOut = AnimationUtils.loadAnimation(CompassMainActivity.this, R.anim.compassviewout);
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.altitudValue:
                    mEditor.putBoolean(SP_KEY_ALTI_UNIT_SWITCH, !mSharePrefrences.getBoolean(SP_KEY_ALTI_UNIT_SWITCH, false));
                    break;
                case R.id.baroValue:
                    mEditor.putBoolean(SP_KEY_BARO_UNIT_SWITCH, !mSharePrefrences.getBoolean(SP_KEY_BARO_UNIT_SWITCH, false));
                    break;
                case R.id.layout_refresh1:
                    checkLocationMode();
                    break;
                case R.id.layout_refresh2:
                    checkLocationMode();
                    break;
                case R.id.refresh_textLocation_line:
                    setRefreshNotGpaAnimation(true);
                    break;
//                case R.id.refresh_altitude:
//                    checkLocationMode();
//                    break;
//                case R.id.refresh_baroValue:
//                    checkLocationMode();
//                    break;
                default:
                    break;
            }
            mEditor.commit();
            refreshLocation();

        }
    };


    private void checkLocationMode(){

        // 1397485 -xin.zhao, modify -001 , begin
        setRefreshAnimation();
        // 1397485 -xin.zhao, modify -001 , end
    }


    void refreshLocation(){
        if(isManuelCali){
            return;
        }
        Location currentLocation = null;
        boolean isGpsEnabled = isGpsEnabled();
        boolean isWifiEnabled = isWifiEnabled();
        boolean isMobileNetEnabled = isMobileNetEnabled();
        Log.i(TAG, "refreshLocation, isGpsEnabled: " + isGpsEnabled
                + ", isWifiEnabled: " + isWifiEnabled
                + ", isMobileNetEnabled: " + isMobileNetEnabled);
        if (!isGpsEnabled && !isWifiEnabled && !isMobileNetEnabled) {
            // 1121009 -xin.zhao, modify -001 , begin
            setFooterHeight(true);
            // 1121009 -xin.zhao, modify -001 , end
            mTextLocation.setVisibility(View.VISIBLE);
            mLocationInfoArea.setVisibility(View.GONE);
            mRefreshNotGpsLayout.setVisibility(View.GONE);
            // 1429929 -xinlei.sheng, modify -001 , begin
            // 1429929 -xinlei.sheng, modify -001 , end

            // 1480402 -lin.zhou, modify -001 , begin
            int locat_mode = Settings.Secure.getInt(getContentResolver(), Settings.Secure.LOCATION_MODE,-1);
            if(locat_mode == Settings.Secure.LOCATION_MODE_BATTERY_SAVING){
                mTextLocation.setText(getString(R.string.message_improve_accuracy));
            }else{
                mTextLocation.setText(getString(R.string.message_turn));
            }
            // 1480402 -lin.zhou, modify -001 , end
            // 1783375 -xing.zhao, modify -001 , begin
            mAddress.setVisibility(View.GONE);
            // 1783375 -xing.zhao, modify -001 , end
            return;
        }
        mLocationInfoArea.setVisibility(View.GONE);
        // 1121009 -xin.zhao, modify -001 , begin
        setFooterHeight(true);
        // 1121009 -xin.zhao, modify -001 , end
        mTextLocation.setVisibility(View.VISIBLE);

        // 1414762 -lin.zhou, modify -005 , begin
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            if (isGpsEnabled) {
                currentLocation = mLocationManager
                        .getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }
            Log.d(TAG, "GPS_PROVIDER currentLocation = " + currentLocation);
            if (currentLocation == null && (isWifiEnabled | isMobileNetEnabled )) {
                currentLocation = mLocationManager
                        .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }
            Log.d(TAG, "NETWORK_PROVIDER currentLocation = " + currentLocation);
        }

        Log.d(TAG, "currentLocation = " + currentLocation);
        // 1414762 -lin.zhou, modify -005 , end

        if (currentLocation != null) {
            mRefreshNotGpsLayout.setVisibility(View.GONE);
            mLocation = currentLocation;
            updateLocationView();
        } else {
            // 1239472 -lin.zhou, modify -001 , start
            int locat_mode = Settings.Secure.getInt(getContentResolver(), Settings.Secure.LOCATION_MODE,-1);
            // 1429929 -xinlei.sheng, modify -002 , begin
            if(locat_mode == Settings.Secure.LOCATION_MODE_OFF ){
                mRefreshNotGpsLayout.setVisibility(View.GONE);
                mTextLocation.setText(getString(R.string.message_turn));
            }
            else if(locat_mode == Settings.Secure.LOCATION_MODE_BATTERY_SAVING || locat_mode == Settings.Secure.LOCATION_MODE_SENSORS_ONLY){
                mRefreshNotGpsLayout.setVisibility(View.GONE);
                mTextLocation.setText(getString(R.string.message_improve_accuracy));
            }
            else if(locat_mode == Settings.Secure.LOCATION_MODE_HIGH_ACCURACY){
                mRefreshNotGpsLayout.setVisibility(View.VISIBLE);
                mTextLocation.setText(getString(R.string.message_high_accuracy_try));
            }
            // 1429929 -xinlei.sheng, modify -002 , end
            // 1239472 -lin.zhou, modify -001 , end

            mLocationInfoArea.setVisibility(View.GONE);
            mAddress.setVisibility(View.GONE);
            // 1121009 -xin.zhao, modify -001 , begin
            setFooterHeight(true);
            // 1121009 -xin.zhao, modify -001 , end
            mTextLocation.setVisibility(View.VISIBLE);
        }
    }





    // 1121009 -xin.zhao, modify -001 , begin
    private void setFooterHeight(boolean isHeight){
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)mFooter.getLayoutParams();
        if(isHeight){
            layoutParams.height = getResources().getDimensionPixelOffset(R.dimen.real_mode_hight);
            mFooter.setLayoutParams(mFooter.getLayoutParams());
        } else {
            layoutParams.height = getResources().getDimensionPixelOffset(R.dimen.real_mode_low);
            mFooter.setLayoutParams(mFooter.getLayoutParams());
        }
    }
    // 1121009 -xin.zhao, modify -001 , end

    private void updateLocationView(){

        if(isManuelCali){
            return;
        }
        // 1121009 -xin.zhao, modify -001 , begin
        setFooterHeight(false);
        // 1121009 -xin.zhao, modify -001 , end
        mTextLocation.setVisibility(View.GONE);
        mLocationInfoArea.setVisibility(View.VISIBLE);
        DecimalFormat df = new DecimalFormat("#0.00");
        String locationInfoLatitude = getLatitudeString(mLocation.getLatitude());
        String locationInfoLongitude = getLatitudeString(mLocation.getLongitude());
        if (mLocation.getLatitude() > 0) {
            locationInfoLatitude += "N";
        } else if (mLocation.getLatitude() < 0) {
            locationInfoLatitude += "S";
        }
        if (mLocation.getLongitude() > 0) {
            locationInfoLongitude += "E";
        } else if (mLocation.getLongitude() < 0) {
            locationInfoLongitude += "W";
        }
        mLAT.setText(locationInfoLatitude);
        mLON.setText(locationInfoLongitude);
        if (mLocation.getProvider().equals(LocationManager.GPS_PROVIDER)) {

            mRefreshAltiLayout.setVisibility(View.GONE);
            mRefreshBaroLayout.setVisibility(View.GONE);
            mRefreshAlti.setVisibility(View.GONE);
            mRefreshBaro.setVisibility(View.GONE);
            mALTI.setVisibility(View.VISIBLE);
            mBARO.setVisibility(View.VISIBLE);

            double alti = mLocation.getAltitude();
            double baro = Math.pow(Math.E, 0 - (alti / 7924)) * 101;
            if (mSharePrefrences.getBoolean(SP_KEY_BARO_UNIT_SWITCH, false)) {
                baro = baro * BARO_EXCHANGE_RATE;
                mBARO.setText(" " + df.format(baro) + "psi");
            } else {
                mBARO.setText(" " + df.format(baro) + "kPa");
            }
            if (mSharePrefrences.getBoolean(SP_KEY_ALTI_UNIT_SWITCH, false)) {
                alti = alti * ALTI_EXCHANGE_RATE;
                mALTI.setText(" " + df.format(alti) + "ft");
            } else {
                mALTI.setText(" " + df.format(alti) + "M");
            }
        }
        if ((!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) || mLocationManager
                .getLastKnownLocation(LocationManager.GPS_PROVIDER) == null) {

            mRefreshAltiLayout.setVisibility(View.VISIBLE);
            mRefreshBaroLayout.setVisibility(View.VISIBLE);
            mRefreshAlti.setVisibility(View.VISIBLE);
            mRefreshBaro.setVisibility(View.VISIBLE);
            mALTI.setVisibility(View.GONE);
            mBARO.setVisibility(View.GONE);
        }
        new UpdateAddressTask().execute();
    }


    private void initFloatView() {
        Rect floatViewRect = new Rect();
        int [] ints =  new int [] {0,0};
        mFloatview.getLocationInWindow(ints);
        mFloatview.getLocalVisibleRect(floatViewRect);
        mFloatview.centerX = floatViewRect.width()/2;
        mFloatview.centerY = floatViewRect.height()/2;
        mFloatview.mRadius = mFloatview.getWidth()/6;
        mFloatview.bubbleX = floatViewRect.width()/2;
        mFloatview.bubbleY = floatViewRect.height()/2;
        mFloatview.postInvalidate();
    }
    // 1095228 -yunwen.jiang, modify -001 , begin
    private boolean requestPermissions = false;
    private boolean later = false;
    private boolean showSettings = false;
    private View ll_permisstionWarning , ll_dilog_try_realview;
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults){
        requestPermissions = false;
        Log.i(TAG, "No permission, " + "onRequestPermissionsResult");
        if(Build.VERSION.SDK_INT < 23){
            return;
        }
        if(PermissionUtil.CHECK_REQUEST_PERMISSION_RESULT == requestCode){
            for(String permission : permissions){
                if(permission.equals(Manifest.permission.CAMERA)){
                    if(PermissionChecker.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED){
                        Log.i(TAG, "No permission, " + permission);
//                        Toast.makeText(this,permission+" Denied",Toast.LENGTH_LONG).show();
                        // 1414762 -lin.zhou, modify -006 , begin
                        showSetting(SETTING_TYPE_CAMERA);
                        // 1414762 -lin.zhou, modify -006 , end
                    }else{
                        if (isManuelCali) {
                            return;
                        } else {
                            isRealMode = true;
                            updateViewInCompassMode(isRealMode);
                            if (!mAnimationIn.hasEnded()) {
                                mAnimationIn.cancel();
                            }
                            mCompassLayout.startAnimation(mAnimationOut);
                        }
                    }
                }
            }

        }
        // 1414762 -lin.zhou, modify -007 , begin
        else if(PermissionUtil.CHECK_REQUEST_PERMISSION_LOCATION == requestCode){
            for(String permission : permissions){
                if(permission.equals(Manifest.permission.ACCESS_COARSE_LOCATION)){
                    if(PermissionChecker.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED){
                        Log.i(TAG, "No permission, " + permission);
//                        Toast.makeText(this,permission+" Denied",Toast.LENGTH_LONG).show();
                        showSetting(SETTING_TYPE_LOCATION);
                    }else{
                        mLocationHandler.sendEmptyMessage(MSGREQUESTLOCATIONUPDATE);
                    }
                }
            }
        }
        // 1414762 -lin.zhou, modify -007 , end

    }
    private void startHideAni(final View view){
        final TranslateAnimation mhideAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 1.0f);
        mhideAction.setDuration(500);
        mhideAction.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.setAnimation(mhideAction);
    }

    // 1414762 -lin.zhou, modify -008 , begin
    private void showSetting(int mType) {
    // 1414762 -lin.zhou, modify -008 , end
        showSettings = true;
        if(ll_permisstionWarning == null){
            ll_permisstionWarning = findViewById(R.id.ll_permisstionWarning);
        }

        // 1414762 -lin.zhou, add -009 , begin
        ((Button)ll_permisstionWarning.findViewById(R.id.bt_permiss_setting)).setText(getResources().getString(R.string.settings).toUpperCase());
        if(mType == SETTING_TYPE_CAMERA){
            ((TextView)ll_permisstionWarning.findViewById(R.id.tv_permiss_label)).setText(getResources().getString(R.string.turn_camera_comment));
        }else{
            ((TextView)ll_permisstionWarning.findViewById(R.id.tv_permiss_label)).setText(getResources().getString(R.string.turn_camera_location));
        }
        // 1414762 -lin.zhou, add -009 , end

        ll_permisstionWarning.setVisibility(View.VISIBLE);
        TranslateAnimation mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        mShowAction.setDuration(500);
        ll_permisstionWarning.setAnimation(mShowAction);
        mShowAction.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        showSettings = false;
                        startHideAni(ll_permisstionWarning);
                    }
                }, 3000);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }
    private void showTryRealView() {
        if(showSettings || isManuelCali)return;
        requestPermissions = true;
        if(ll_dilog_try_realview == null){
            ll_dilog_try_realview = findViewById(R.id.ll_dilog_try_realview);
        }
        ((Button)ll_dilog_try_realview.findViewById(R.id.bt_later)).setText(getResources().getString(R.string.later_bt).toUpperCase());
        ((Button)ll_dilog_try_realview.findViewById(R.id.bt_try)).setText(getResources().getString(R.string.try_bt).toUpperCase());
        ll_dilog_try_realview.setVisibility(View.VISIBLE);
        TranslateAnimation mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        mShowAction.setDuration(500);
        ll_dilog_try_realview.setAnimation(mShowAction);
    }
    private void hideTryRealView() {
        if (ll_dilog_try_realview == null) return;
        startHideAni(ll_dilog_try_realview);
    }
    public void gotoSettings(View v){
        ForwardUtil.gotoSettings(CompassMainActivity.this);
    }
    public void clickLater(View v){
        later = true;
        requestPermissions = false;
        hideTryRealView();
    }
    public void clickTry(View v){
        if(PermissionChecker.checkSelfPermission(CompassMainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            requestPermissions = true;
            ActivityCompat.requestPermissions(CompassMainActivity.this, new String[]{Manifest.permission.CAMERA}, PermissionUtil.CHECK_REQUEST_PERMISSION_RESULT);
        }
        hideTryRealView();
    }
    // 1095228 -yunwen.jiang, modify -001 , end


    // 1274620 -lin.zhou, modify -001 , begin
    public void openGps(View v) {
        // 1414762 -lin.zhou, add -010 , begin
        if(PermissionChecker.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PermissionUtil.CHECK_REQUEST_PERMISSION_LOCATION);
            return;
        }
        // 1414762 -lin.zhou, add -010 , end
        // 1429929 -xinlei.sheng, modify -003 , begin
        int locat_mode = Settings.Secure.getInt(getContentResolver(), Settings.Secure.LOCATION_MODE, -1);
        if(locat_mode == Settings.Secure.LOCATION_MODE_OFF){
            showUseLocationDialog();
        }else if(locat_mode == Settings.Secure.LOCATION_MODE_BATTERY_SAVING || locat_mode == Settings.Secure.LOCATION_MODE_SENSORS_ONLY){
            showImproveLocationDialog();
        }
        // 1429929 -xinlei.sheng, modify -003 , end
    }
    // 1274620 -lin.zhou, modify -001 , end

    // 1397485 -xin.zhao, modify -002 , begin
    public void setRefreshAnimation() {

        Animation animation1=AnimationUtils.loadAnimation(this, R.anim.refresh_rotation);
        Animation animation2=AnimationUtils.loadAnimation(this, R.anim.refresh_rotation);
        animation1.setRepeatCount(3);
        animation2.setRepeatCount(3);
        LayoutAnimationController lac=new LayoutAnimationController(animation1);
        LayoutAnimationController lac2=new LayoutAnimationController(animation2);
        animation1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //        Toast.makeText(this,"GPS is Enable : "+mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)+","+(mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER) ==null),Toast.LENGTH_LONG).show();
                if(!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || mLocationManager
                        .getLastKnownLocation(LocationManager.GPS_PROVIDER) == null){
//            Toast.makeText(this,"GPS is Enable : "+mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)+","+(mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER) ==null),Toast.LENGTH_LONG).show();
                    int locat_mode = Settings.Secure.getInt(getContentResolver(), Settings.Secure.LOCATION_MODE,-1);
                    if(locat_mode == Settings.Secure.LOCATION_MODE_BATTERY_SAVING){
                        // 1429929 -xinlei.sheng, modify -004 , begin
                        showImproveLocationDialog();
                        // 1429929 -xinlei.sheng, modify -004 , end
                    }else{
                        Toast.makeText(CompassMainActivity.this, getString(R.string.opengps_accuracy_try), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mRefreshAltiLayout.setLayoutAnimation(lac);
        mRefreshBaroLayout.setLayoutAnimation(lac2);
    }
    // 1397485 -xin.zhao, modify -002 , begin

    public void setRefreshNotGpaAnimation(boolean toStart) {
        Animation animation=AnimationUtils.loadAnimation(this, R.anim.refresh_rotation);
        animation.setRepeatCount(3);
        findViewById(R.id.refresh_textLocation).startAnimation(animation);
    }

    // 1429929 -xinlei.sheng, add -005 , begin
    private void showImproveLocationDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(CompassMainActivity.this);
        View inflater = LayoutInflater.from(this).inflate(R.layout.dilog_location_improve,null);
        builder.setView(inflater);
        builder.setNegativeButton(getString(R.string.no).toUpperCase(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton(getString(R.string.yes).toUpperCase(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    Settings.Secure.putInt(CompassMainActivity.this.getContentResolver(), Settings.Secure.LOCATION_MODE, Settings.Secure.LOCATION_MODE_HIGH_ACCURACY);
                    // 1427236 -lin.zhou, add -001 , start
                    refreshLocation();
                    // 1427236 -lin.zhou, add -001 , end
                } catch (Exception e) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    CompassMainActivity.this.startActivity(intent);
                }
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private void showUseLocationDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(CompassMainActivity.this);
        View inflater = LayoutInflater.from(this).inflate(R.layout.dilog_location_use,null);
        builder.setView(inflater);
        builder.setNegativeButton(getString(R.string.no).toUpperCase(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton(getString(R.string.yes).toUpperCase(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    Settings.Secure.putInt(CompassMainActivity.this.getContentResolver(), Settings.Secure.LOCATION_MODE, Settings.Secure.LOCATION_MODE_HIGH_ACCURACY);
                    // 1427236 -lin.zhou, add -002 , start
                    refreshLocation();
                    // 1427236 -lin.zhou, add -002 , end
                } catch (Exception e) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    CompassMainActivity.this.startActivity(intent);
                }
                dialog.dismiss();
            }
        });
        builder.create().show();
    }
    // 1429929 -xinlei.sheng, add -005 , end
}

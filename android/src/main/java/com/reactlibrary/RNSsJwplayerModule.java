
package com.reactlibrary;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.modules.core.DeviceEventManagerModule;

public class RNSsJwplayerModule extends ReactContextBaseJavaModule {

	private final ReactApplicationContext mReactContext;

	private LocalBroadcastForJWPlayerErrorEventReceiver mLocalBroadcastForJWPlayerErrorEventReceiver;
    private LocalBroadcastForJWPlayerPlayEventReceiver mLocalBroadcastForJWPlayerPlayEventReceiver;
    private LocalBroadcastForJWPlayerPauseEventReceiver mLocalBroadcastForJWPlayerPauseEventReceiver;
    private LocalBroadcastForJWPlayerTickEventReceiver mLocalBroadcastForJWPlayerTickEventReceiver;
    private LocalBroadcastForJWPlayerCompleteEventReceiver mLocalBroadcastForJWPlayerCompleteEventReceiver;
    private LocalBroadcastForJWPlayerCloseEventReceiver mLocalBroadcastForJWPlayerCloseEventReceiver;

    private Callback onEvent;

	public RNSsJwplayerModule(ReactApplicationContext reactContext) {
		super(reactContext);
		this.mReactContext = reactContext;

		LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(reactContext);

        // register JWPlayerErrorEvent receiver
        this.mLocalBroadcastForJWPlayerErrorEventReceiver = new LocalBroadcastForJWPlayerErrorEventReceiver();
        localBroadcastManager.registerReceiver(mLocalBroadcastForJWPlayerErrorEventReceiver,
                new IntentFilter("JWPlayerErrorEvent"));

        // register JWPlayerPlayEvent receiver
        this.mLocalBroadcastForJWPlayerPlayEventReceiver = new LocalBroadcastForJWPlayerPlayEventReceiver();
        localBroadcastManager.registerReceiver(mLocalBroadcastForJWPlayerPlayEventReceiver,
                new IntentFilter("JWPlayerPlayEvent"));

        // register JWPlayerPauseEvent receiver
        this.mLocalBroadcastForJWPlayerPauseEventReceiver = new LocalBroadcastForJWPlayerPauseEventReceiver();
        localBroadcastManager.registerReceiver(mLocalBroadcastForJWPlayerPauseEventReceiver,
                new IntentFilter("JWPlayerPauseEvent"));

        // register JWPlayerTickEvent receiver
        this.mLocalBroadcastForJWPlayerTickEventReceiver = new LocalBroadcastForJWPlayerTickEventReceiver();
        localBroadcastManager.registerReceiver(mLocalBroadcastForJWPlayerTickEventReceiver,
                new IntentFilter("JWPlayerTickEvent"));
        
        // register JWPlayerCompleteEvent receiver
        this.mLocalBroadcastForJWPlayerCompleteEventReceiver = new LocalBroadcastForJWPlayerCompleteEventReceiver();
        localBroadcastManager.registerReceiver(mLocalBroadcastForJWPlayerCompleteEventReceiver,
                new IntentFilter("JWPlayerCompleteEvent"));

        // register JWPlayerCloseEvent receiver
        this.mLocalBroadcastForJWPlayerCloseEventReceiver = new LocalBroadcastForJWPlayerCloseEventReceiver();
        localBroadcastManager.registerReceiver(mLocalBroadcastForJWPlayerCloseEventReceiver,
                new IntentFilter("JWPlayerCloseEvent"));
	}

	@Override
	public String getName() {
		return "RNSsJwplayer";
	}

	@ReactMethod
    public void showToast(String message) {
        Toast.makeText(getReactApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

	@ReactMethod
	public void playVideo(String streamUrl, int time, String vastUrl) {
		this.onEvent = onEvent;

		Activity currentActivity = getReactApplicationContext().getCurrentActivity();

        Intent intent = new Intent(currentActivity, JWPlayerActivity.class);
        intent.putExtra("streamUrl", streamUrl);
        intent.putExtra("time", time);
        intent.putExtra("vastUrl", vastUrl);
        
        currentActivity.startActivity(intent);
	}

	public class LocalBroadcastForJWPlayerErrorEventReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
        	// Toast.makeText(getReactApplicationContext(), "error", Toast.LENGTH_SHORT).show();
            Log.d("[RNSsJwplayerModule]", "LocalBroadcastForJWPlayerErrorEventReceiver");
            String errorData = intent.getStringExtra("errorMessage");
            mReactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                    .emit("JWPlayerErrorEvent", errorData);
        }
    }

    public class LocalBroadcastForJWPlayerPlayEventReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
     		// Toast.makeText(getReactApplicationContext(), "play", Toast.LENGTH_SHORT).show();
            Log.d("[RNSsJwplayerModule]", "LocalBroadcastForJWPlayerPlayEventReceiver");
            String durationData = intent.getStringExtra("duration");
            mReactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                    .emit("JWPlayerPlayEvent", durationData);
        }
    }

    public class LocalBroadcastForJWPlayerPauseEventReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
     		// Toast.makeText(getReactApplicationContext(),"paused", Toast.LENGTH_SHORT).show();
            Log.d("[RNSsJwplayerModule]", "LocalBroadcastForJWPlayerPauseEventReceiver");
            String timeData = intent.getStringExtra("pausedAt");
            mReactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                    .emit("JWPlayerPauseEvent", timeData);
        }
    }

    public class LocalBroadcastForJWPlayerTickEventReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
     		// Toast.makeText(getReactApplicationContext(),"paused", Toast.LENGTH_SHORT).show();
            Log.d("[RNSsJwplayerModule]", "LocalBroadcastForJWPlayerTickEventReceiver");
            mReactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                    .emit("JWPlayerTickEvent", null);
        }
    }

    public class LocalBroadcastForJWPlayerCompleteEventReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
     		// Toast.makeText(getReactApplicationContext(),"paused", Toast.LENGTH_SHORT).show();
            Log.d("[RNSsJwplayerModule]", "LocalBroadcastForJWPlayerCompleteEventReceiver");
            mReactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                    .emit("JWPlayerCompleteEvent", null);
        }
    }

    public class LocalBroadcastForJWPlayerCloseEventReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
     		// Toast.makeText(getReactApplicationContext(),"paused", Toast.LENGTH_SHORT).show();
            Log.d("[RNSsJwplayerModule]", "LocalBroadcastForJWPlayerCloseEventReceiver");
            mReactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                    .emit("JWPlayerCloseEvent", null);
        }
    }
}
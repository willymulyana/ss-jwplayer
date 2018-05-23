package com.reactlibrary;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;

import com.longtailvideo.jwplayer.JWPlayerFragment;
import com.longtailvideo.jwplayer.JWPlayerView;
import com.longtailvideo.jwplayer.configuration.PlayerConfig;
import com.longtailvideo.jwplayer.core.PlayerState;
import com.longtailvideo.jwplayer.media.meta.Metadata;
import com.longtailvideo.jwplayer.events.ErrorEvent;
import com.longtailvideo.jwplayer.events.listeners.VideoPlayerEvents;
import com.longtailvideo.jwplayer.media.ads.AdSource;
import com.longtailvideo.jwplayer.media.ads.VMAPAdvertising;
import com.longtailvideo.jwplayer.media.playlists.PlaylistItem;
import com.reactlibrary.R;

import java.util.ArrayList;
import java.util.List;

public class JWPlayerActivity extends AppCompatActivity {

    private static final long TRACK_INTERVAL = 60 * 1000; // 1 minute in milliseconds

    private JWPlayerView mPlayerView = null;
    private JWPlayerFragment JWfragment;
    static JWPlayerActivity jwPlayerActivity;

    static Handler handler;
    static Runnable videoTrackRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jwplayer);

        final Activity activity = this;
        jwPlayerActivity = this;
        handler = new Handler();

        setupEveryMinuteEventDispatch();

        JWfragment = (JWPlayerFragment) getFragmentManager().findFragmentById(R.id.jwPlayerFragment);
        mPlayerView = JWfragment.getPlayer();
        mPlayerView.setFullscreen(true, true);

        mPlayerView.addOnPlayListener(new VideoPlayerEvents.OnPlayListener() {
            @Override
            public void onPlay(PlayerState playerState) {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                Log.d("[JWPlayerActivity]", "play");

                //send event to RN
                LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(activity);
                Intent customEvent = new Intent("JWPlayerPlayEvent");
                String duration = Long.toString(mPlayerView.getDuration());
                Log.d("[JWPlayerActivity]", duration);
                customEvent.putExtra("duration", duration);
                //attach extra intent
                localBroadcastManager.sendBroadcast(customEvent);

                handler.postDelayed(videoTrackRunnable, TRACK_INTERVAL);
            }
        });

        mPlayerView.addOnPauseListener(new VideoPlayerEvents.OnPauseListener() {
            @Override
            public void onPause(PlayerState playerState) {
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                Log.d("[JWPlayerActivity]", "pause");
                Log.d("[JWPlayerActivity]", Long.toString(mPlayerView.getPosition()));
                String times = Long.toString(mPlayerView.getPosition());
                Log.d("[JWPlayerActivity]", times);
                // mPlayerView.seek(videoTime);

                //send event to RN
                LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(activity);
                Intent customEvent = new Intent("JWPlayerPauseEvent");
                customEvent.putExtra("pausedAt", times);
                localBroadcastManager.sendBroadcast(customEvent);

                stopDispatcTickerEvent();
            }
        });

        mPlayerView.addOnErrorListener(new VideoPlayerEvents.OnErrorListenerV2() {
            @Override
            public void onError(ErrorEvent errorEvent) {
                //Utils.handleJwPlayerError(activity, errorEvent); //handled in react native
                Log.d("[JWPlayerActivity]", "error");

                // send error info to this activity caller with broadcast
                String errorMessage = errorEvent.getMessage();

                LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(activity);
                Intent customEvent = new Intent("JWPlayerErrorEvent");
                customEvent.putExtra("errorMessage", errorMessage);
                localBroadcastManager.sendBroadcast(customEvent);

                stopDispatcTickerEvent();

                // close this activity
                activity.finish();
            }
        });

        mPlayerView.addOnCompleteListener(new VideoPlayerEvents.OnCompleteListener() {
            @Override
            public void onComplete() {
                // getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                Log.d("[JWPlayerActivity]", "complete");

                //send event to RN
                LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(activity);
                Intent customEvent = new Intent("JWPlayerCompleteEvent");
                // customEvent.putExtra("pausedAt", times);
                localBroadcastManager.sendBroadcast(customEvent);

                stopDispatcTickerEvent();

                // close this activity
                activity.finish();
            }
        });

        
        Log.d("[JWPlayerActivity]", "time: " + getIntent().getIntExtra("time", 123));
        playStream(getIntent().getStringExtra("streamUrl"), getIntent().getIntExtra("time", 123), getIntent().getStringExtra("vastUrl"));
    }

    private void setupEveryMinuteEventDispatch() {
        final Activity activity = this;

        // clear existing (if exists)
        if (videoTrackRunnable != null) {
            handler.removeCallbacks(videoTrackRunnable);
            videoTrackRunnable = null;
        }

        // build new runnable
        videoTrackRunnable = new Runnable() {
            @Override
            public void run() {
                LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(activity);
                Intent customEvent = new Intent("JWPlayerTickEvent");
                localBroadcastManager.sendBroadcast(customEvent);

                handler.postDelayed(videoTrackRunnable, TRACK_INTERVAL);
            }
        };
    }

    public static void stopDispatcTickerEvent() {
        if (videoTrackRunnable != null) {
            handler.removeCallbacks(videoTrackRunnable);
        }
        videoTrackRunnable = null;
    }

    public static JWPlayerActivity getInstance() {
        return jwPlayerActivity;
    }

    public void playStream(String streamUrl, Integer time, String vastUrl) {
        Log.d("loading and resuming", "onPlayVideo: " + streamUrl + " at " + time);

        // Set the url to the VMAP tag
        // VMAPAdvertising vmapAdvertising = new VMAPAdvertising(AdSource.VAST,"https://s3-ap-southeast-1.amazonaws.com/ss-static-api/v1/jwplayerads/vmap-prerolls.xml");//TEST
        VMAPAdvertising vmapAdvertising = new VMAPAdvertising(AdSource.VAST, vastUrl);

        // Create a playlist, you'll need this to build your player config
        List<PlaylistItem> playlist = new ArrayList<>();

        PlaylistItem pi = new PlaylistItem.Builder()
                .file(streamUrl)
                .build();

        playlist.add(pi);

        // Create your player config
        PlayerConfig playerConfig = new PlayerConfig.Builder()
                .playlist(playlist)
                .advertising(vmapAdvertising)
                .build();

        mPlayerView.setup(playerConfig);

        mPlayerView.play();
        mPlayerView.seek(time);
    }

    @Override
    protected void onStart() {
        super.onStart();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //send event to RN
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this);
        Intent customEvent = new Intent("JWPlayerCloseEvent");
        localBroadcastManager.sendBroadcast(customEvent);

        stopDispatcTickerEvent();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    protected void onDestroy() { //app is being destroyed
        super.onDestroy();
        //mPlayerView.onDestroy(); // this causes java.lang.IllegalArgumentException: Receiver not registered: com.google.android.exoplayer.audio.AudioCapabilitiesReceiver$HdmiAudioPlugBroadcastReceiver@2330f743
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        stopDispatcTickerEvent();
    }

    @Override
    protected void onPause() { //app going to background
        super.onPause();
    }

}

package com.mke.mword.Fragment;

import static com.mke.mword.MainActivity.g_Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;
import java.util.Objects;

import com.mke.mword.Utils.MediaObject;
import com.mke.mword.R;

import at.huber.youtubeExtractor.VideoMeta;
import at.huber.youtubeExtractor.YouTubeExtractor;
import at.huber.youtubeExtractor.YtFile;

public class ExoPlayerRecyclerView extends RecyclerView {

  private static final String TAG = "ExoPlayerRecyclerView";
  private static final String AppName = "Android ExoPlayer";

  private ImageView mediaCoverImage, volumeControl;
  private ProgressBar progressBar;
  private View viewHolderParent;
  private FrameLayout mediaContainer;
  private PlayerView videoSurfaceView;
  private ExoPlayer videoPlayer;
  private Dialog mFullScreenDialog;
  private ImageView mFullScreenIcon;

  // Media List
  private ArrayList<MediaObject> mediaObjects = new ArrayList<>();
  private int videoSurfaceDefaultHeight = 0;
  private int screenDefaultHeight = 0;
  private Context context;
  private int playPosition = -1;
  private boolean isVideoViewAdded;
  private RequestManager requestManager;

  private VolumeState volumeState;
  private PlayState playState = PlayState.OFF;
  private FullScreenState fullScreenState;

  int targetPosition = 0;
  int nExoPlayerFullscreenPos = -1;
  boolean mExoPlayerFullscreen;
  Context mContext = null;
  int nOldSelection = -1;
  boolean bStopPlayer = false;

  FrameLayout frmlyt = null;

  private OnClickListener videoViewClickListener = new OnClickListener() {
    @Override
    public void onClick(View v) {
      if (bStopPlayer) {
        startPlayer();
        bStopPlayer = false;
      }
    }
  };

  private OnClickListener volumeControlViewClickListener = new OnClickListener() {
    @Override
    public void onClick(View v) {
      toggleVolume();
    }
  };

  private OnClickListener fullScreenViewClickListener = new OnClickListener() {
    @Override
    public void onClick(View v) {
      int orientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT;
      if (!mExoPlayerFullscreen) {
        orientation = getResources().getConfiguration().orientation;
        ((Activity) g_Activity).setRequestedOrientation(orientation);
        openFullscreenDialog();
      } else {
        ((Activity) g_Activity).setRequestedOrientation(orientation);
        closeFullscreenDialog();
      }
    }
  };

  public void pausePlayer() {
    if (videoPlayer != null) {
      videoPlayer.setPlayWhenReady(false);
      videoPlayer.getPlaybackState();
    }
  }

  public void startPlayer() {
    if (videoPlayer != null) {
      videoPlayer.setPlayWhenReady(true);
      videoPlayer.getPlaybackState();
    }
  }

  public ExoPlayerRecyclerView(@NonNull Context context) {
    super(context);
    init(context);
  }

  public ExoPlayerRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    init(context);
  }

  private void init(Context context) {
    this.context = context.getApplicationContext();
    Display display = ((WindowManager) Objects.requireNonNull(
        getContext().getSystemService(Context.WINDOW_SERVICE))).getDefaultDisplay();
    Point point = new Point();
    display.getSize(point);
    mContext = context;

    videoSurfaceDefaultHeight = point.x;
    screenDefaultHeight = point.y;

    videoSurfaceView = new PlayerView(this.context);
    videoSurfaceView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_ZOOM);

    TrackSelector trackSelector = new DefaultTrackSelector(context);

    // Create the player using ExoPlayer.Builder
    videoPlayer = new ExoPlayer.Builder(context)
        .setTrackSelector(trackSelector)
        .build();

    // Disable Player Control
    videoSurfaceView.setUseController(true);
    // Bind the player to the view.
    videoSurfaceView.setPlayer(videoPlayer);
    // Turn on Volume
    setVolumeControl(VolumeState.ON);

    addOnScrollListener(new OnScrollListener() {
      @Override
      public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);

        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
          if (mediaCoverImage != null) {
            // show the old thumbnail
            mediaCoverImage.setVisibility(VISIBLE);
          }

          if (!recyclerView.canScrollVertically(1)) {
            playVideo(true);
          } else {
            playVideo(false);
          }
        }
      }

      @Override
      public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
      }
    });

    addOnChildAttachStateChangeListener(new OnChildAttachStateChangeListener() {
      @Override
      public void onChildViewAttachedToWindow(@NonNull View view) {

      }

      @Override
      public void onChildViewDetachedFromWindow(@NonNull View view) {
        if (viewHolderParent != null && viewHolderParent.equals(view)) {
          resetVideoView();
        }
      }
    });

    videoPlayer.addListener(new Player.Listener() {
      @Override
      public void onPlaybackStateChanged(int playbackState) {
        switch (playbackState) {
          case Player.STATE_BUFFERING:
            if (progressBar != null) {
              progressBar.setVisibility(VISIBLE);
            }
            break;
          case Player.STATE_ENDED:
            if (videoPlayer != null) {
              videoPlayer.seekTo(0);
            }
            break;
          case Player.STATE_IDLE:
            break;
          case Player.STATE_READY:
            if (progressBar != null) {
              progressBar.setVisibility(GONE);
            }
            if (!isVideoViewAdded) {
              addVideoView();
            }
            break;
          default:
            break;
        }
      }
    });
  }

  private void initFullscreenDialog() {
    mFullScreenDialog = new Dialog(mContext, android.R.style.Theme_Black_NoTitleBar_Fullscreen) {
      public void onBackPressed() {
        if (mExoPlayerFullscreen) {
          closeFullscreenDialog();
        }
        super.onBackPressed();
      }
    };
  }

  private void openFullscreenDialog() {
    frmlyt = (FrameLayout) videoSurfaceView.getParent();
    ((ViewGroup) videoSurfaceView.getParent()).removeView(videoSurfaceView);
    mFullScreenDialog.addContentView(videoSurfaceView,
            new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));

    mFullScreenIcon.setImageResource(R.drawable.ic_exit_fullscreen);
    mExoPlayerFullscreen = true;
    mFullScreenDialog.show();
    nExoPlayerFullscreenPos = this.getVerticalScrollbarPosition();
  }

  private void closeFullscreenDialog() {
    if (frmlyt != null) {
      ((ViewGroup) videoSurfaceView.getParent()).removeView(videoSurfaceView);
      frmlyt.addView(videoSurfaceView);
    }
    mExoPlayerFullscreen = false;
    mFullScreenDialog.dismiss();
    mFullScreenIcon.setImageResource(R.drawable.ic_fullscreen);
  }

  public void playVideo(boolean isEndOfList) {

    targetPosition = 0;
    if (!isEndOfList) {
      int startPosition = ((LinearLayoutManager) getLayoutManager()).findFirstVisibleItemPosition();
      int endPosition = ((LinearLayoutManager) getLayoutManager()).findLastVisibleItemPosition();

      if (endPosition - startPosition > 1) {
        endPosition = startPosition + 1;
      }

      if (startPosition < 0 || endPosition < 0) {
        return;
      }

      if (startPosition != endPosition) {
        int startPositionVideoHeight = getVisibleVideoSurfaceHeight(startPosition);
        int endPositionVideoHeight = getVisibleVideoSurfaceHeight(endPosition);

        targetPosition =
            startPositionVideoHeight > endPositionVideoHeight ? startPosition : endPosition;
      } else {
        targetPosition = startPosition;
      }
    } else {
      targetPosition = mediaObjects.size() - 1;
    }

    if (targetPosition == playPosition) {
      return;
    }

    playPosition = targetPosition;
    if (videoSurfaceView == null) {
      return;
    }

    videoSurfaceView.setVisibility(INVISIBLE);
    removeVideoView(videoSurfaceView);

    int currentPosition =
        targetPosition - ((LinearLayoutManager) Objects.requireNonNull(
            getLayoutManager())).findFirstVisibleItemPosition();

    View child = getChildAt(currentPosition);
    if (child == null) {
      return;
    }
    nExoPlayerFullscreenPos = currentPosition;

    PlayerViewHolder holder = (PlayerViewHolder) child.getTag();
    if (holder == null) {
      playPosition = -1;
      return;
    }
    mediaCoverImage = holder.mediaCoverImage;
    progressBar = holder.progressBar;
    volumeControl = holder.volumeControl;
    viewHolderParent = holder.itemView;
    requestManager = holder.requestManager;
    mediaContainer = holder.mediaContainer;
    mFullScreenIcon = holder.mFullScreenIcon;
    videoSurfaceView.setPlayer(videoPlayer);

    volumeControl.setOnClickListener(volumeControlViewClickListener);
    mFullScreenIcon.setOnClickListener(fullScreenViewClickListener);
    videoSurfaceView.setOnClickListener(videoViewClickListener);
    initFullscreenDialog();

    extractYoutubeUrl();
  }

  public void playStream(String urlToPlay) {
    String uriString = urlToPlay;
    DefaultBandwidthMeter BANDWIDTH_METER = new DefaultBandwidthMeter();
    Uri mp4VideoUri = Uri.parse(uriString);

    DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(context,
            Util.getUserAgent(context, AppName), BANDWIDTH_METER);

    MediaSource videoSource = null;
    if (uriString.toUpperCase().contains("M3U8")) {
      videoSource = new HlsMediaSource.Factory(dataSourceFactory)
          .createMediaSource(MediaItem.fromUri(mp4VideoUri));
    } else {
      mp4VideoUri = Uri.parse(urlToPlay);
      videoSource = new ProgressiveMediaSource.Factory(dataSourceFactory)
          .createMediaSource(MediaItem.fromUri(mp4VideoUri));
    }

    videoPlayer.addListener(new Player.Listener() {
      @Override
      public void onPlaybackStateChanged(int playbackState) {
        boolean playWhenReady = videoPlayer != null && videoPlayer.getPlayWhenReady();
        if (playWhenReady && playbackState == Player.STATE_READY) {
          ViewHolder holder = findViewHolderForAdapterPosition(targetPosition);
          if (holder != null) {
            holder.itemView.findViewById(R.id.ivVolumeControl).setVisibility(View.VISIBLE);
            holder.itemView.findViewById(R.id.ivFullScreenControl).setVisibility(View.VISIBLE);
          }
          nOldSelection = targetPosition;
        } else if (playWhenReady) {
          if (nOldSelection != -1 && nOldSelection != targetPosition) {
            ViewHolder holder = findViewHolderForAdapterPosition(nOldSelection);
            if (holder != null) {
              holder.itemView.findViewById(R.id.ivVolumeControl).setVisibility(View.GONE);
              holder.itemView.findViewById(R.id.ivFullScreenControl).setVisibility(View.GONE);
            }
          }
        }
      }
    });
    videoPlayer.setMediaSource(videoSource);
    videoPlayer.prepare();
    videoPlayer.setPlayWhenReady(true);
  }

  private String YOUTUBE_VIDEO_ID = "uZnWUZW1hQo";
  private String BASE_URL = "https://www.youtube.com";

  private void extractYoutubeUrl() {
    String mYoutubeLink = BASE_URL + "/watch?v=" + mediaObjects.get(targetPosition).getUrl();
    @SuppressLint("StaticFieldLeak") YouTubeExtractor mExtractor = new YouTubeExtractor(context) {
      @Override
      protected void onExtractionComplete(SparseArray<YtFile> sparseArray, VideoMeta videoMeta) {
        if (sparseArray != null) {
          playStream(sparseArray.get(22).getUrl());
        }
      }
    };
    mExtractor.extract(mYoutubeLink, true, true);
  }

  private int getVisibleVideoSurfaceHeight(int playPosition) {
    int at = playPosition - ((LinearLayoutManager) Objects.requireNonNull(
        getLayoutManager())).findFirstVisibleItemPosition();

    View child = getChildAt(at);
    if (child == null) {
      return 0;
    }

    int[] location = new int[2];
    child.getLocationInWindow(location);

    if (location[1] < 0) {
      return location[1] + videoSurfaceDefaultHeight;
    } else {
      return screenDefaultHeight - location[1];
    }
  }

  private void removeVideoView(PlayerView videoView) {
    ViewGroup parent = (ViewGroup) videoView.getParent();
    if (parent == null) {
      return;
    }

    int index = parent.indexOfChild(videoView);
    if (index >= 0) {
      parent.removeViewAt(index);
      isVideoViewAdded = false;
      viewHolderParent.setOnClickListener(null);
    }
  }

  private void addVideoView() {
    mediaContainer.addView(videoSurfaceView);
    isVideoViewAdded = true;
    videoSurfaceView.requestFocus();
    videoSurfaceView.setVisibility(VISIBLE);
    videoSurfaceView.setAlpha(1);
    mediaCoverImage.setVisibility(GONE);
  }

  private void resetVideoView() {
    if (isVideoViewAdded) {
      removeVideoView(videoSurfaceView);
      playPosition = -1;
      videoSurfaceView.setVisibility(INVISIBLE);
      mediaCoverImage.setVisibility(VISIBLE);
    }
  }

  public void releasePlayer() {
    if (videoPlayer != null) {
      videoPlayer.release();
      videoPlayer = null;
    }
    viewHolderParent = null;
  }

  public void onPausePlayer() {
    if (videoPlayer != null) {
      videoPlayer.stop();
      bStopPlayer = true;
    }
  }

  private void toggleVolume() {
    if (videoPlayer != null) {
      if (volumeState == VolumeState.OFF) {
        setVolumeControl(VolumeState.ON);
      } else if (volumeState == VolumeState.ON) {
        setVolumeControl(VolumeState.OFF);
      }
    }
  }

  private void setVolumeControl(VolumeState state) {
    volumeState = state;
    if (state == VolumeState.OFF) {
      videoPlayer.setVolume(0f);
      animateVolumeControl();
    } else if (state == VolumeState.ON) {
      videoPlayer.setVolume(1f);
      animateVolumeControl();
    }
  }

  private void setPlayControl() {
    if (playState == PlayState.OFF) {
      playVideo(true);
      playState = PlayState.ON;
    } else if (playState == PlayState.ON) {
      playVideo(false);
      playState = PlayState.OFF;
    }
  }

  private void animateVolumeControl() {
    if (volumeControl != null) {
      if (volumeState == VolumeState.OFF) {
        requestManager.load(R.drawable.ic_volume_off).into(volumeControl);
      } else if (volumeState == VolumeState.ON) {
        requestManager.load(R.drawable.ic_volume_on).into(volumeControl);
      }
    }
  }

  public void setMediaObjects(ArrayList<MediaObject> mediaObjects) {
    this.mediaObjects = mediaObjects;
  }

  private enum VolumeState {
    ON, OFF
  }

  private enum PlayState {
    ON, OFF
  }

  private enum FullScreenState {
    ON, OFF
  }
}

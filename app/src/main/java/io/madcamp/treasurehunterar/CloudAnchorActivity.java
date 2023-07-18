package io.madcamp.treasurehunterar;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.GuardedBy;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.google.ar.core.Anchor;
import com.google.ar.core.Anchor.CloudAnchorState;
import com.google.ar.core.ArCoreApk;
import com.google.ar.core.Camera;
import com.google.ar.core.Config;
import com.google.ar.core.Config.CloudAnchorMode;
import com.google.ar.core.Frame;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.core.Point;
import com.google.ar.core.Point.OrientationMode;
import com.google.ar.core.PointCloud;
import com.google.ar.core.Session;
import com.google.ar.core.Trackable;
import com.google.ar.core.TrackingState;
import com.google.ar.core.exceptions.CameraNotAvailableException;
import com.google.ar.core.exceptions.UnavailableApkTooOldException;
import com.google.ar.core.exceptions.UnavailableArcoreNotInstalledException;
import com.google.ar.core.exceptions.UnavailableSdkTooOldException;
import com.google.common.base.Preconditions;
import com.google.firebase.database.DatabaseError;

import java.io.IOException;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import io.madcamp.treasurehunterar.PrivacyNoticeDialogFragment.HostResolveListener;
import io.madcamp.treasurehunterar.common.helpers.CameraPermissionHelper;
import io.madcamp.treasurehunterar.common.helpers.DisplayRotationHelper;
import io.madcamp.treasurehunterar.common.helpers.FullScreenHelper;
import io.madcamp.treasurehunterar.common.helpers.SnackbarHelper;
import io.madcamp.treasurehunterar.common.helpers.TrackingStateHelper;
import io.madcamp.treasurehunterar.common.rendering.BackgroundRenderer;
import io.madcamp.treasurehunterar.common.rendering.ObjectRenderer;
import io.madcamp.treasurehunterar.common.rendering.PlaneRenderer;
import io.madcamp.treasurehunterar.common.rendering.PointCloudRenderer;


public class CloudAnchorActivity extends AppCompatActivity
    implements GLSurfaceView.Renderer, PrivacyNoticeDialogFragment.NoticeDialogListener {
  private static final String TAG = CloudAnchorActivity.class.getSimpleName();
  private static final float[] OBJECT_COLOR = new float[] {139.0f, 195.0f, 74.0f, 255.0f};

  private enum HostResolveMode {
    NONE,
    HOSTING,
    RESOLVING,
  }

  // Rendering. The Renderers are created here, and initialized when the GL surface is created.
  private GLSurfaceView surfaceView;
  private final BackgroundRenderer backgroundRenderer = new BackgroundRenderer();
  private final ObjectRenderer virtualObject = new ObjectRenderer();
  private final PlaneRenderer planeRenderer = new PlaneRenderer();
  private final PointCloudRenderer pointCloudRenderer = new PointCloudRenderer();

  private boolean installRequested;

  // Temporary matrices allocated here to reduce number of allocations for each frame.
  private final float[] anchorMatrix = new float[16];
  private final float[] viewMatrix = new float[16];
  private final float[] projectionMatrix = new float[16];

  // Locks needed for synchronization
  private final Object singleTapLock = new Object();
  private final Object anchorLock = new Object();

  // Tap handling and UI.
  private GestureDetector gestureDetector;
  private final SnackbarHelper snackbarHelper = new SnackbarHelper();
  private DisplayRotationHelper displayRotationHelper;
  private final TrackingStateHelper trackingStateHelper = new TrackingStateHelper(this);
  private Button hostButton;
  private Button resolveButton;
  private TextView roomCodeText;
  private SharedPreferences sharedPreferences;
  private static final String PREFERENCE_FILE_KEY = "allow_sharing_images";
  private static final String ALLOW_SHARE_IMAGES_KEY = "ALLOW_SHARE_IMAGES";

  @GuardedBy("singleTapLock")
  public MotionEvent queuedSingleTap;

  private Session session;

  @GuardedBy("anchorLock")
  private Anchor anchor;

  // Cloud Anchor Components.
  private FirebaseManager firebaseManager;
  private final CloudAnchorManager cloudManager = new CloudAnchorManager();
  private HostResolveMode currentMode;
  private RoomCodeAndCloudAnchorIdListener hostListener;
  TextView scoreText;
  private int score;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    surfaceView = findViewById(R.id.surfaceview);
    displayRotationHelper = new DisplayRotationHelper(this);
    score = 0;

    // Set up touch listener.
    gestureDetector =
        new GestureDetector(
            this,
            new GestureDetector.SimpleOnGestureListener() {
              @Override
              public boolean onSingleTapUp(MotionEvent e) {
                synchronized (singleTapLock) {
                  if (currentMode == HostResolveMode.HOSTING) {
                    queuedSingleTap = e;
                  } else if (currentMode == HostResolveMode.RESOLVING) {
                    if (anchor != null) {
                      showSuccessDialog();
                    } else {
                      Toast.makeText(CloudAnchorActivity.this, "대상 없음", Toast.LENGTH_SHORT).show();
                    }
                  }
                  return true;
                }
              }
              @Override
              public boolean onDown(MotionEvent e) {
                return true;
              }
            });
    surfaceView.setOnTouchListener((v, event) -> gestureDetector.onTouchEvent(event));

    // Set up renderer.
    surfaceView.setPreserveEGLContextOnPause(true);
    surfaceView.setEGLContextClientVersion(2);
    surfaceView.setEGLConfigChooser(8, 8, 8, 8, 16, 0); // Alpha used for plane blending.
    surfaceView.setRenderer(this);
    surfaceView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    surfaceView.setWillNotDraw(false);
    installRequested = false;

    // Initialize UI components.
    hostButton = findViewById(R.id.host_button);
    hostButton.setOnClickListener((view) -> onHostButtonPress());
    resolveButton = findViewById(R.id.resolve_button);
    resolveButton.setOnClickListener((view) -> onResolveButtonPress());
    roomCodeText = findViewById(R.id.room_code_text);
    scoreText = findViewById(R.id.score);
    scoreText.setText("점수: 0");

    // Initialize Cloud Anchor variables.
    firebaseManager = new FirebaseManager(this);
    currentMode = HostResolveMode.NONE;
    sharedPreferences = getSharedPreferences(PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
  }

  @Override
  protected void onDestroy() {
    // Clear all registered listeners.
    resetMode();

    if (session != null) {
      // Explicitly close ARCore Session to release native resources.
      // Review the API reference for important considerations before calling close() in apps with
      // more complicated lifecycle requirements:
      // https://developers.google.com/ar/reference/java/arcore/reference/com/google/ar/core/Session#close()
      session.close();
      session = null;
    }

    super.onDestroy();
  }

  @Override
  protected void onResume() {
    super.onResume();

    if (sharedPreferences.getBoolean(ALLOW_SHARE_IMAGES_KEY, false)) {
      createSession();
    }
    snackbarHelper.showMessage(this, "환영합니다!");
    surfaceView.onResume();
    displayRotationHelper.onResume();
  }

  private void createSession() {
    if (session == null) {
      Exception exception = null;
      int messageId = -1;
      try {
        switch (ArCoreApk.getInstance().requestInstall(this, !installRequested)) {
          case INSTALL_REQUESTED:
            installRequested = true;
            return;
          case INSTALLED:
            break;
        }
        // ARCore requires camera permissions to operate. If we did not yet obtain runtime
        // permission on Android M and above, now is a good time to ask the user for it.
        if (!CameraPermissionHelper.hasCameraPermission(this)) {
          CameraPermissionHelper.requestCameraPermission(this);
          return;
        }
        session = new Session(this);
      } catch (UnavailableArcoreNotInstalledException e) {
        messageId = 1;
        exception = e;
      } catch (UnavailableApkTooOldException e) {
        messageId = 2;
        exception = e;
      } catch (UnavailableSdkTooOldException e) {
        messageId = 3;
        exception = e;
      } catch (Exception e) {
        messageId = 4;
        exception = e;
      }

      if (exception != null) {
        snackbarHelper.showError(this, "에러");
        Log.e(TAG, "세션 오류", exception);
        return;
      }

      // Create default config and check if supported.
      Config config = new Config(session);
      config.setCloudAnchorMode(CloudAnchorMode.ENABLED);
      session.configure(config);

      // Setting the session in the HostManager.
      cloudManager.setSession(session);
    }

    // Note that order matters - see the note in onPause(), the reverse applies here.
    try {
      session.resume();
    } catch (CameraNotAvailableException e) {
      snackbarHelper.showError(this, "카메라 권한 필요");
      session = null;
      return;
    }
  }

  @Override
  public void onPause() {
    super.onPause();
    if (session != null) {
      // Note that the order matters - GLSurfaceView is paused first so that it does not try
      // to query the session. If Session is paused before GLSurfaceView, GLSurfaceView may
      // still call session.update() and get a SessionPausedException.
      displayRotationHelper.onPause();
      surfaceView.onPause();
      session.pause();
    }
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] results) {
    super.onRequestPermissionsResult(requestCode, permissions, results);
    if (!CameraPermissionHelper.hasCameraPermission(this)) {
      Toast.makeText(this, "카메라 권한이 필요합니다", Toast.LENGTH_LONG)
          .show();
      if (!CameraPermissionHelper.shouldShowRequestPermissionRationale(this)) {
        CameraPermissionHelper.launchPermissionSettings(this);
      }
      finish();
    }
  }

  @Override
  public void onWindowFocusChanged(boolean hasFocus) {
    super.onWindowFocusChanged(hasFocus);
    FullScreenHelper.setFullScreenOnWindowFocusChanged(this, hasFocus);
  }

  private void showSuccessDialog() {
    AlertDialog.Builder builder = new AlertDialog.Builder(CloudAnchorActivity.this);
    builder.setMessage("상자를 여시겠습니까?");
    builder.setTitle("보물이다!");
    builder.setCancelable(false);
    builder.setNegativeButton("아니오", (DialogInterface.OnClickListener) (dialog, which) -> {
      dialog.cancel();
    });
    builder.setPositiveButton("네", (DialogInterface.OnClickListener) (dialog, which) -> {
      score++;
      scoreText.setText("점수: " + score);
      anchor = null;
      dialog.cancel();
    });
    AlertDialog alertDialog = builder.create();
    alertDialog.show();
  }
  /**
   * Handles the most recent user tap.
   *
   * <p>We only ever handle one tap at a time, since this app only allows for a single anchor.
   *
   * @param frame the current AR frame
   * @param cameraTrackingState the current camera tracking state
   */
  private void handleTap(Frame frame, TrackingState cameraTrackingState) {
    // Handle taps. Handling only one tap per frame, as taps are usually low frequency
    // compared to frame rate.
    synchronized (singleTapLock) {
      synchronized (anchorLock) {
        // Only handle a tap if the anchor is currently null, the queued tap is non-null and the
        // camera is currently tracking.
        if (anchor == null
            && queuedSingleTap != null
            && cameraTrackingState == TrackingState.TRACKING) {
          Preconditions.checkState(
              currentMode == HostResolveMode.HOSTING,
              "호스트가 필요합니다");
          for (HitResult hit : frame.hitTest(queuedSingleTap)) {
            if (shouldCreateAnchorWithHit(hit)) {
              Anchor newAnchor = hit.createAnchor();
              Preconditions.checkNotNull(hostListener, "리스너 오류");
              cloudManager.hostCloudAnchor(newAnchor, hostListener);
              setNewAnchor(newAnchor);
              snackbarHelper.showMessage(this, "로딩중..");
              break; // Only handle the first valid hit.
            }
          }
        }
      }
      queuedSingleTap = null;
    }
  }

  /** Returns {@code true} if and only if the hit can be used to create an Anchor reliably. */
  private static boolean shouldCreateAnchorWithHit(HitResult hit) {
    Trackable trackable = hit.getTrackable();
    if (trackable instanceof Plane) {
      // Check if the hit was within the plane's polygon.
      return ((Plane) trackable).isPoseInPolygon(hit.getHitPose());
    } else if (trackable instanceof Point) {
      // Check if the hit was against an oriented point.
      return ((Point) trackable).getOrientationMode() == OrientationMode.ESTIMATED_SURFACE_NORMAL;
    }
    return false;
  }

  @Override
  public void onSurfaceCreated(GL10 gl, EGLConfig config) {
    GLES20.glClearColor(0.1f, 0.1f, 0.1f, 1.0f);

    // Prepare the rendering objects. This involves reading shaders, so may throw an IOException.
    try {
      // Create the texture and pass it to ARCore session to be filled during update().
      backgroundRenderer.createOnGlThread(this);
      planeRenderer.createOnGlThread(this, "models/trigrid.png");
      pointCloudRenderer.createOnGlThread(this);
      //virtualObject.createOnGlThread(this, "models/andy.obj", "models/andy.png");
      virtualObject.createOnGlThread(this, "models/treasure_chest.obj", "models/treasure_chest.JPG");
      virtualObject.setMaterialProperties(0.0f, 2.0f, 0.5f, 6.0f);

    } catch (IOException ex) {
      Log.e(TAG, "Failed to read an asset file", ex);
    }
  }

  @Override
  public void onSurfaceChanged(GL10 gl, int width, int height) {
    displayRotationHelper.onSurfaceChanged(width, height);
    GLES20.glViewport(0, 0, width, height);
  }

  @Override
  public void onDrawFrame(GL10 gl) {
    // Clear screen to notify driver it should not load any pixels from previous frame.
    GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

    if (session == null) {
      return;
    }
    // Notify ARCore session that the view size changed so that the perspective matrix and
    // the video background can be properly adjusted.
    displayRotationHelper.updateSessionIfNeeded(session);

    try {
      session.setCameraTextureName(backgroundRenderer.getTextureId());

      // Obtain the current frame from ARSession. When the configuration is set to
      // UpdateMode.BLOCKING (it is by default), this will throttle the rendering to the
      // camera framerate.
      Frame frame = session.update();
      Camera camera = frame.getCamera();
      TrackingState cameraTrackingState = camera.getTrackingState();

      // Notify the cloudManager of all the updates.
      cloudManager.onUpdate();

      // Handle user input.
      handleTap(frame, cameraTrackingState);

      // If frame is ready, render camera preview image to the GL surface.
      backgroundRenderer.draw(frame);

      // Keep the screen unlocked while tracking, but allow it to lock when tracking stops.
      trackingStateHelper.updateKeepScreenOnFlag(camera.getTrackingState());

      // If not tracking, don't draw 3d objects.
      if (cameraTrackingState == TrackingState.PAUSED) {
        return;
      }

      // Get camera and projection matrices.
      camera.getViewMatrix(viewMatrix, 0);
      camera.getProjectionMatrix(projectionMatrix, 0, 0.1f, 100.0f);

      // Visualize tracked points.
      // Use try-with-resources to automatically release the point cloud.
      try (PointCloud pointCloud = frame.acquirePointCloud()) {
        pointCloudRenderer.update(pointCloud);
        pointCloudRenderer.draw(viewMatrix, projectionMatrix);
      }

      // Visualize planes.
      planeRenderer.drawPlanes(
          session.getAllTrackables(Plane.class), camera.getDisplayOrientedPose(), projectionMatrix);

      // Check if the anchor can be visualized or not, and get its pose if it can be.
      boolean shouldDrawAnchor = false;
      synchronized (anchorLock) {
        if (anchor != null && anchor.getTrackingState() == TrackingState.TRACKING) {
          // Get the current pose of an Anchor in world space. The Anchor pose is updated
          // during calls to session.update() as ARCore refines its estimate of the world.
          anchor.getPose().toMatrix(anchorMatrix, 0);
          shouldDrawAnchor = true;
        }
      }

      // Visualize anchor.
      if (shouldDrawAnchor) {
        float[] colorCorrectionRgba = new float[4];
        frame.getLightEstimate().getColorCorrection(colorCorrectionRgba, 0);

        // Update and draw the model and its shadow.
        float scaleFactor = 0.1f;
        virtualObject.updateModelMatrix(anchorMatrix, scaleFactor);
        virtualObject.draw(viewMatrix, projectionMatrix, colorCorrectionRgba, OBJECT_COLOR);
      }
    } catch (Throwable t) {
      // Avoid crashing the application due to unhandled exceptions.
      Log.e(TAG, "Exception on the OpenGL thread", t);
    }
  }

  /** Sets the new value of the current anchor. Detaches the old anchor, if it was non-null. */
  private void setNewAnchor(Anchor newAnchor) {
    synchronized (anchorLock) {
      if (anchor != null) {
        anchor.detach();
      }
      anchor = newAnchor;
    }
  }

  /** Callback function invoked when the Host Button is pressed. */
  private void onHostButtonPress() {
    if (currentMode == HostResolveMode.HOSTING) {
      resetMode();
      return;
    }

    if (!sharedPreferences.getBoolean(ALLOW_SHARE_IMAGES_KEY, false)) {
      showNoticeDialog(this::onPrivacyAcceptedForHost);
    } else {
      onPrivacyAcceptedForHost();
    }
  }

  private void onPrivacyAcceptedForHost() {
    if (hostListener != null) {
      return;
    }
    resolveButton.setEnabled(false);
    hostButton.setText("취소");
    snackbarHelper.showMessageWithDismiss(this, "취소");

    hostListener = new RoomCodeAndCloudAnchorIdListener();
    firebaseManager.getNewRoomCode(hostListener);
  }

  /** Callback function invoked when the Resolve Button is pressed. */
  private void onResolveButtonPress() {
    if (currentMode == HostResolveMode.RESOLVING) {
      resetMode();
      return;
    }

    if (!sharedPreferences.getBoolean(ALLOW_SHARE_IMAGES_KEY, false)) {
      showNoticeDialog(this::onPrivacyAcceptedForResolve);
    } else {
      onPrivacyAcceptedForResolve();
    }
  }

  private void onPrivacyAcceptedForResolve() {
    ResolveDialogFragment dialogFragment = new ResolveDialogFragment();
    dialogFragment.setOkListener(this::onRoomCodeEntered);
    dialogFragment.show(getSupportFragmentManager(), "ResolveDialog");
  }

  /** Resets the mode of the app to its initial state and removes the anchors. */
  private void resetMode() {
    hostButton.setText("숨기기");
    hostButton.setEnabled(true);
    resolveButton.setText("찾기");
    resolveButton.setEnabled(true);
    roomCodeText.setText("00");
    currentMode = HostResolveMode.NONE;
    firebaseManager.clearRoomListener();
    hostListener = null;
    setNewAnchor(null);
    snackbarHelper.hide(this);
    cloudManager.clearListeners();
  }

  /** Callback function invoked when the user presses the OK button in the Resolve Dialog. */
  private void onRoomCodeEntered(Long roomCode) {
    currentMode = HostResolveMode.RESOLVING;
    hostButton.setEnabled(false);
    resolveButton.setText("취소");
    roomCodeText.setText(String.valueOf(roomCode));
    snackbarHelper.showMessageWithDismiss(this, "보물을 찾아보세요!");

    // Register a new listener for the given room.
    firebaseManager.registerNewListenerForRoom(
        roomCode,
        cloudAnchorId -> {
          // When the cloud anchor ID is available from Firebase.
          CloudAnchorResolveStateListener resolveListener =
              new CloudAnchorResolveStateListener(roomCode);
          Preconditions.checkNotNull(resolveListener, "Listener cannot be null.");
          cloudManager.resolveCloudAnchor(
              cloudAnchorId, resolveListener, SystemClock.uptimeMillis());
        });
  }

  /**
   * Listens for both a new room code and an anchor ID, and shares the anchor ID in Firebase with
   * the room code when both are available.
   */
  private final class RoomCodeAndCloudAnchorIdListener
      implements CloudAnchorManager.CloudAnchorHostListener, FirebaseManager.RoomCodeListener {

    private Long roomCode;
    private String cloudAnchorId;

    @Override
    public void onNewRoomCode(Long newRoomCode) {
      Preconditions.checkState(roomCode == null, "Unknown room code");
      roomCode = newRoomCode;
      roomCodeText.setText(String.valueOf(roomCode));
      snackbarHelper.showMessageWithDismiss(
          CloudAnchorActivity.this, "보물을 숨기세요!");
      checkAndMaybeShare();
      synchronized (singleTapLock) {
        // Change currentMode to HOSTING after receiving the room code (not when the 'Host' button
        // is tapped), to prevent an anchor being placed before we know the room code and able to
        // share the anchor ID.
        currentMode = HostResolveMode.HOSTING;
      }
    }

    @Override
    public void onError(DatabaseError error) {
      Log.w(TAG, "Firebase database error happened.", error.toException());
      snackbarHelper.showError(
          CloudAnchorActivity.this, "Database error..");
    }

    @Override
    public void onCloudTaskComplete(String cloudId, CloudAnchorState cloudState) {
      if (cloudState.isError()) {
        Log.e(TAG, "Error hosting a cloud anchor, state " + cloudState);
        snackbarHelper.showMessageWithDismiss(
            CloudAnchorActivity.this, "클라우드 에러");
        return;
      }
      Preconditions.checkState(
          cloudAnchorId == null, "Data not found");
      cloudAnchorId = cloudId;
      checkAndMaybeShare();
    }

    private void checkAndMaybeShare() {
      if (roomCode == null || cloudAnchorId == null) {
        return;
      }
      firebaseManager.storeAnchorIdInRoom(roomCode, cloudAnchorId);
      snackbarHelper.showMessageWithDismiss(
          CloudAnchorActivity.this, "Data stored!");
    }
  }

  private final class CloudAnchorResolveStateListener
      implements CloudAnchorManager.CloudAnchorResolveListener {
    private final long roomCode;

    CloudAnchorResolveStateListener(long roomCode) {
      this.roomCode = roomCode;
    }

    @Override
    public void onCloudTaskComplete(Anchor anchor, CloudAnchorState cloudState) {
      // When the anchor has been resolved, or had a final error state.
      if (cloudState.isError()) {
        Log.w(
            TAG,
            "The anchor in room "
                + roomCode
                + " could not be resolved. The error state was "
                + cloudState);
        snackbarHelper.showMessageWithDismiss(
            CloudAnchorActivity.this, "Restoring error..");
        return;
      }
      snackbarHelper.showMessageWithDismiss(
          CloudAnchorActivity.this, "Data restored!");
      setNewAnchor(anchor);
    }

    @Override
    public void onShowResolveMessage() {
      snackbarHelper.setMaxLines(4);
      snackbarHelper.showMessageWithDismiss(
          CloudAnchorActivity.this, "Loading...");
    }
  }

  public void showNoticeDialog(HostResolveListener listener) {
    DialogFragment dialog = PrivacyNoticeDialogFragment.createDialog(listener);
    dialog.show(getSupportFragmentManager(), PrivacyNoticeDialogFragment.class.getName());
  }

  @Override
  public void onDialogPositiveClick(DialogFragment dialog) {
    if (!sharedPreferences.edit().putBoolean(ALLOW_SHARE_IMAGES_KEY, true).commit()) {
      throw new AssertionError("Could not save");
    }
    createSession();
  }
}

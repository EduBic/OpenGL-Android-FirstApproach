package mprogramming.sprites;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MainActivity extends AppCompatActivity implements GLSurfaceView.Renderer {

    List<Sprite> sprites = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        GLSurfaceView surfaceView = new GLSurfaceView(this);
        surfaceView.setEGLContextClientVersion(2);  // OpenGL version
        // R G B A set bit for each color + alpha
        surfaceView.setEGLConfigChooser(8, 8, 8, 8, 0, 0);
        surfaceView.setRenderer(this);

        setContentView(surfaceView);

        Sprite sprite = new Sprite();
        sprite.setWidth(1.0f);
        sprite.setHeight(1.9f);
        sprite.setCenterX(0.5f);
        sprite.setCenterY(0.0f);
        sprites.add(sprite);
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        // Sprite.initSprite(); -> init at startUp
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        // TODO: glVieport?
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        // analog for onDraw() of custom View, it perfoms animation

        // set background dark gray
        GLES20.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);  // RBGA
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        // TODO: Do this in an animator or timer thread
        Sprite aSprite = sprites.get(0);
        aSprite.setCenterX(aSprite.getCenterX() - 0.0005f);

        for (Sprite sprite : sprites) {
            sprite.draw();
        }
    }
}

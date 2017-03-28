package mprogramming.quadtexture;

import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MainActivity extends AppCompatActivity implements GLSurfaceView.Renderer {

    List<Sprite> sprites = new ArrayList<>();
    Date _gameLoopLastRunDate = new Date();

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
        sprite.setWidth(0.6f);
        sprite.setHeight(0.6f);
        sprite.setTexture(BitmapFactory.decodeResource(getResources(), R.drawable.texture));
        sprites.add(sprite);

        Sprite sprite2 = new Sprite();
        sprite2.setWidth(0.75f);
        sprite2.setHeight(0.55f);
        sprite2.setCenterY(0.4f);   // move up to screen
        sprite2.setTexture(BitmapFactory.decodeResource(getResources(), R.drawable.texture2));
        sprites.add(sprite2);


        // TODO: build a game loop

        // get time elapsed since last run
        /*if (_gameLoopLastRunDate == null) {
            _gameLoopLastRunDate = new Date();
        }
        Date now = new Date();
        float elapsedTime = (float)(now.getTime() - _gameLoopLastRunDate.getTime()) / 1000.0f;
        _gameLoopLastRunDate = now;*/

        // move sprite according to velocity
        //sprite.setCenterX(sprite.getCenterX() + sprite.getVelocityX() * elapsedTime);
        //sprite.setCenterY(sprite.getCenterY() + sprite.getVelocityY() * elapsedTime);

        // Detect collisions between two sprites
        /*float betweenVectorX = sprite.getCenterX() - sprite2.getCenterX();
        float betweenVectorY = sprite.getCenterY() - sprite2.getCenterY();
        float betweenVectorLenght = (float)Math.sqrt(betweenVectorX * betweenVectorX + betweenVectorY * betweenVectorY);

        boolean collided = betweenVectorLenght < sprite.getWidth() * 0.5f + sprite2.getWidth() * 0.5f;*/
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        GLES20.glClearColor(0.9f, 0.2f, 0.2f, 1.0f);
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
        aSprite.setCenterX(aSprite.getCenterX() + 0.0008f);

        Sprite aSprite2 = sprites.get(1);
        aSprite2.setCenterX(aSprite2.getCenterX() - 0.0008f);

        for (Sprite sprite : sprites) {
            sprite.draw();
        }
    }
}

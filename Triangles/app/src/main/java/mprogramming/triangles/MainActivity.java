package mprogramming.triangles;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MainActivity extends AppCompatActivity implements GLSurfaceView.Renderer {
    float[] _triangleColor = {
            0.0f, 0.0f,
            1.0f, 0.5f,
            -0.5f, 0.5f
    };

    float[] _triangleGeometry = {
            0.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 0.0f,
    };

    int program = -1;

    float _translateX = 0.0f;
    float _translateY = 0.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        GLSurfaceView surfaceView = new GLSurfaceView(this);
        surfaceView.setEGLContextClientVersion(2);  // OpenGL version
        // R G B A set bit for each color + alpha
        surfaceView.setEGLConfigChooser(8, 8, 8, 8, 0, 0);   // config buffer bit to dedicate
        surfaceView.setRenderer(this);

        setContentView(surfaceView);
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        // first OpenGL init
        // gl10 is for Open GL 1.0 don't use it
        // elgConfig -> configuration provide

        //TODO: create and complice vertex shader
        int vertexShader = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);

        // associate source code to shader and compile
        // N.B. every draw OpenGL execute this for every points
        String vertexShaderSource = "" +
                " \n" +
                " attribute vec2 position;\n" +
                " attribute vec4 color;\n" +
                " uniform vec2 translate;\n" +      // ask for Open GL location
                " \n" +
                " varying vec4 colorInterpolated;\n" +  // need to bind
                " \n" +
                " void main() { \n" +
                "    gl_Position = vec4(position.x + translate.x, position.y + translate.y, 0.0, 1.0); \n" +   // vec4 is a point
                "    colorInterpolated = color; \n" +
                "} \n" +
                " \n" +
                " \n" +
                " \n" +
                " \n";

        GLES20.glShaderSource(vertexShader, vertexShaderSource);
        GLES20.glCompileShader(vertexShader);
        String vertexShaderInfoLog = GLES20.glGetShaderInfoLog(vertexShader);
        Log.i("Vertex Shader", "Output: " + vertexShaderInfoLog); // understand the compile error, if not string is empty

        // TODO: create and compile fragment shader
        int fragmentShader = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);

        String fragmentShaderSource = "" +
                " \n" +
                " varying vec4 colorInterpolated; \n" +
                " \n" +
                " void main() {\n" +
                "    gl_FragColor = colorInterpolated;\n" +    // vec4 is RGBA
                "} \n" +
                " \n";

        GLES20.glShaderSource(fragmentShader, fragmentShaderSource);
        GLES20.glCompileShader(fragmentShader);
        String fragmentShaderInfoLog = GLES20.glGetShaderInfoLog(fragmentShader);
        Log.i("Fragment Shader", "Output: " + fragmentShaderInfoLog);

        // TODO: Link program, put together 2 small program
        /*int*/ program = GLES20.glCreateProgram();

        GLES20.glAttachShader(program, vertexShader);
        GLES20.glAttachShader(program, fragmentShader);
        // bind position of data and name of variable to the program
        GLES20.glBindAttribLocation(program, 0, "position");
        GLES20.glBindAttribLocation(program, 1, "color");

        GLES20.glLinkProgram(program);
        String programInfoLog = GLES20.glGetProgramInfoLog(program);
        Log.i("Linker", "Output: " + programInfoLog);

        GLES20.glUseProgram(program);           // program is store in GPU
        GLES20.glEnableVertexAttribArray(0);    // index array where get data
        GLES20.glEnableVertexAttribArray(1);
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        // When View Surface change his shape
        // TODO: glVieport?
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        // analog for onDraw() of custom View, it perfoms animation

        // set background dark gray
        GLES20.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);  // RBGA
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        // Translate with program in Open GL so we translate on GPU
        _translateX += 0.001f;
        int translateUniformLocation = GLES20.glGetUniformLocation(program, "translate"); // need for uniform variables
        // we are passing two float (uniform translate is a vec2) -> Uniform2f
        GLES20.glUniform2f(translateUniformLocation, _translateX, _translateY); /// each draw we change value of uniform variable


        // TODO: Provide OpenGL a triangle to draw

        // allocate byte to store elems; we have 3 elem, 2 float for elems (2D), 4 byte for float = 2*4*3
        ByteBuffer triangleGeometryByteBuffer = ByteBuffer.allocateDirect(24);
        triangleGeometryByteBuffer.order(ByteOrder.nativeOrder());

        FloatBuffer triangleGeometryBuffer = triangleGeometryByteBuffer.asFloatBuffer();
        // add information in this buffer
        /*float[] triangleGeometry = {
                0.0f, 0.0f,
                0.0f, 1.0f,
                1.0f, 0.0f,
        };*/

        triangleGeometryBuffer.put(_triangleGeometry);
        triangleGeometryBuffer.rewind();
        // move the triangle
        //_triangleGeometry[2] -= 0.003f; // each draw we change 2nd element value

        // allocate byte to store elems; we have 3 elem, 2 float for elems (2D), 4 byte for float = 2*4*3
        ByteBuffer triangleColorByteBuffer = ByteBuffer.allocateDirect(48);
        triangleColorByteBuffer.order(ByteOrder.nativeOrder());

        FloatBuffer triangleColorBuffer = triangleColorByteBuffer.asFloatBuffer();
        // add information in this buffer
        /*float[] triangleColor = {
                0.0f, 0.0f, 0.0f, 1.0f,
                0.0f, 1.0f, 0.0f, 1.0f,
                1.0f, 0.0f, 1.0f, 1.0f,
        };*/
        triangleColorBuffer.put(_triangleColor);
        triangleColorBuffer.rewind();
        // change dynamically color of triangle
        //_triangleColor[2] += 0.003f;    // each draw color + 0.003f to 2nd element

        // i2 how much data?
        // i3: what kind of data
        // i4: byte to shift
        // i5: index where take first data
        GLES20.glVertexAttribPointer(0, 2, GLES20.GL_FLOAT, false, 0, triangleGeometryBuffer);
        GLES20.glVertexAttribPointer(1, 4, GLES20.GL_FLOAT, false, 0, triangleColorBuffer);

        // i: points, lines or trinagles (there are different type)
        // i2: index of first point to start in the array
        // i3: how many vertices should it draws
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3);
    }
}

package mprogramming.sprites;

import android.opengl.GLES20;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by Eduard on 25/03/2017.
 */

public class Sprite {

    private static final int POSITION_ARRAY = 0;
    private static final int COLOR_ARRAY = 1;

    // final couse we pass in vertex shader program
    // static couse for each sprite the values is equal
    private static final float[] _quadGeometry = {
            -0.25f, -0.25f,   // ARBITRARY: bottom left
            0.25f, -0.25f,    // bottom right
            -0.25f, 0.25f,    // top left
            0.25f, 0.25f,     // top right
    };

    private static final float[] _quadColor = {
            0.0f, 0.0f, 0.0f, 1.0f,
            0.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 0.0f, 1.0f, 1.0f,
            1.0f, 0.0f, 1.0f, 1.0f,
    };

    private static int _program = -1;

    private static int _translateUniformLocation = -1;
    private static int _scaleUniformLocation = -1;


    private float _translateX = 0.0f;
    private float _translateY = 0.0f;
    private float _scaleX = 1.0f;
    private float _scaleY = 1.0f;


    public float getCenterX() {
        return _translateX;
    }

    public void setCenterX(float x) {
        _translateX = x;
    }

    public float getCenterY() {
        return _translateY;
    }

    public void setCenterY(float y) {
        _translateY = y;
    }

    public float getWidth() {
        return _scaleX;
    }

    public void setWidth(float width) {
        _scaleX = width;    // if we have geometry not in -1 to 1 here we adapt the change
    }

    public float getHeight() {
        return _scaleY;
    }

    public void setHeight(float height) {
        _scaleY = height;
    }

//    public float getRotation() {
//        return 0.0f;
//    }
//
//    public void setRotation(float theta) {
//
//    }

    // TODO: Animation get/set animation object

    /**
     *  We don't need instance for setup -> static
     *  private for lazy initialization and protect the method
     */
    private static void setUp() {
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
                " uniform vec2 scale;\n" +
                " \n" +
                " varying vec4 colorInterpolated;\n" +  // need to bind
                " \n" +
                " void main() { \n" +
                "    gl_Position = vec4(position.x * scale.x + translate.x, position.y * scale.y + translate.y, 0.0, 1.0); \n" +   // vec4 is a point
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
        _program = GLES20.glCreateProgram();

        GLES20.glAttachShader(_program, vertexShader);
        GLES20.glAttachShader(_program, fragmentShader);
        // bind position of data and name of variable to the program
        GLES20.glBindAttribLocation(_program, POSITION_ARRAY, "position");
        GLES20.glBindAttribLocation(_program, COLOR_ARRAY, "color");

        GLES20.glLinkProgram(_program);
        String programInfoLog = GLES20.glGetProgramInfoLog(_program);
        Log.i("Linker", "Output: " + programInfoLog);

        GLES20.glUseProgram(_program);           // program is store in GPU
        GLES20.glEnableVertexAttribArray(0);    // index array where get data
        GLES20.glEnableVertexAttribArray(1);

        // TODO: Provide OpenGL a quad to draw

        // allocate byte to store elems; we have 4 elem, 2 float for elems (2D), 4 byte for float
        ByteBuffer quadGeometryByteBuffer = ByteBuffer.allocateDirect(_quadGeometry.length * 4);
        quadGeometryByteBuffer.order(ByteOrder.nativeOrder());

        FloatBuffer quadGeometryBuffer = quadGeometryByteBuffer.asFloatBuffer();
        quadGeometryBuffer.put(_quadGeometry);
        quadGeometryBuffer.rewind();

        // allocate byte to store elems; 4 elem, 4 float for elems (RBGA), 4 byte for float = 4*2*4
        ByteBuffer quadColorByteBuffer = ByteBuffer.allocateDirect(_quadColor.length * 4);
        quadColorByteBuffer.order(ByteOrder.nativeOrder());

        FloatBuffer quadColorBuffer = quadColorByteBuffer.asFloatBuffer();
        quadColorBuffer.put(_quadColor);
        quadColorBuffer.rewind();


        // i2 how much data?
        // i3: what kind of data
        // i4: byte to shift
        // i5: index where take first data
        GLES20.glVertexAttribPointer(POSITION_ARRAY, 2, GLES20.GL_FLOAT, false, 0, quadGeometryBuffer);
        GLES20.glVertexAttribPointer(COLOR_ARRAY, 4, GLES20.GL_FLOAT, false, 0, quadColorBuffer);

        // need for uniform variables
        _translateUniformLocation = GLES20.glGetUniformLocation(_program, "translate");
        _scaleUniformLocation = GLES20.glGetUniformLocation(_program, "scale");
    }

    /**
     * This method need to be fast -> remove all computation that don't need to be done each draw
     */
    public void draw() {
        if (_program <= 0) {
            Sprite.setUp();    // lazy initialization
        }

        // Translate with program in Open GL so we translate on GPU
        // each draw we change value of uniform variable
        GLES20.glUniform2f(_translateUniformLocation, _translateX, _translateY);
        GLES20.glUniform2f(_scaleUniformLocation, _scaleX, _scaleY);

        // i: points, lines or trinagles (there are different type)
        // i2: index of first point to start in the array
        // i3: how many vertices should it draws (3 for a triangle, 6 for two triangle, 4 is a square)
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
    }

}

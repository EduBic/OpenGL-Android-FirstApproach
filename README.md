# OpenGL-Android-FirstApproach
Small example for first approach with OpenGL ES 2.0 in Android Environment

## Triangles
It is an application that show how to build a triangle with shader language, color it and translate it. The computation, in order to manipulate the triangle, is done on GPU.

Keys of process:
- Initialize the SurfaceView on the start of Activity and set it as View and activity as renderer.
- **Init Graphic** (onSurfaceCreated):  Build a vertexShader and a fragmentShader routine. Link them together in a program. Compile.
- **Draw** (onDrawFrame): appliy translate to elements (uniform variables), allocate floats for vertex and color. Draw all.


## Sprites
It is an application that start a process of optimization of other Triangle app work. The element that need to be created and manipulated on GPU is called Sprite and it all his behavior is encapsulated in the Sprite class. 
Sprite class offers these functionalities:
- the static variable needed for define the starter position and color of the sprite; program shader and also the uniform varaible ids.
- the instance variable that explain element's transformation.
- getter and setter methods.
- setUp() method that initialize vertex shader and fragment shader into GPU
- draw() method that is needed for draw the sprite in the screen. (draw is called by every OnDrawFrame() from activity that implements GLSurfaceView).

## QuadTexture
It is an application that extends the functionality of Sprites application and adds texture attach to two example of sprites.

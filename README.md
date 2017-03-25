# OpenGL-Android-FirstApproach
Small example for first approach with OpenGL ES 2.0 in Android Environment

## Triangles
It is an application that show how to build a triangle with shader language, color it and translate it. The computation, in order to manipulate the triangle, is done on GPU.

Keys of process:
- Initialize the SurfaceView on the start of Activity and set it as View and activity as renderer.
- **Init Graphic** (onSurfaceCreated):  Build a vertexShader and a fragmentShader routine. Link them together in a program. Compile.
- **Draw** (onDrawFrame): appliy translate to elements (uniform variables), allocate floats for vertex and color. Draw all.

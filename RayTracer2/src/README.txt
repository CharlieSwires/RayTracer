                        Ray Tracer by CEL Swires
Background
This ray tracer started life as a final year project at Bradford University. It took command line parameters to indicate the screen dimensions in the horizontal then the vertical number of pixels input file and output file names. The input file is a text file defining the scene in terms of light sources (point), spheres, triangles, materials (reflectance, transmission) and a view point. The output file is floating point brightness’s one pixel per line. As you might have figured out it is a monochromatic ray tracer only one frequency of light.

The disadvantage is that to achieve colour in the simplest form you need 3 scene files (RGB) and then you need to combine the RGB values for form a JPG, or PNG. You might wonder what the advantage is well it’s simple you can vary the refractive index for R, G and B which is what happens in glass, diamond and water and why we see rainbows.

I think the first thing I did was to convert the code from K&R C to ANSI C. I tried to do earth rise back then it took days and only used float for the math. The results were disappointing Earth was just plain and it was all grey. I converted the math to double, and Java. After this I sorted out antialiasing then added textures followed by a sky texture then I tried again to produce earth rise using passes for R, G and B combining at the end much better 5 days to render though. I added discs and cylinders for the Hubble anniversary you can see the resulting pictures on charlesswires.com/software. To improve earth rise I added craters to the LandscapeGen and bent the surface speed was an issue so I implemented bounding boxes and for the fractal nested them in a quad tree structure also used parallelism of the i7 processor time came down to 1.5hrs. This gives us today’s ray tracer.
  
BoundingParallelpiped.java
Bounding box data structure. A bit Cish.

CandleLight.java
A super program used to generate the candle light valentines animations don’t ask!!

Contactpt.java
Contact point data structure a bit Cish.

Controller.java
Not sure could be the Hubble super program.

CrossHair.java
Not related to the ray tracer used to generate the JPG texture for the HUD top camera sight for Moonoids.

Cylinder.java
Cylinder data structure.

Disk.java
Disc data structure.

Display.java
Given a list of JPGs of the form fred?.jpg where ? is 0-359 animate.

EarthRise.java
Earth rise super program.

Enviro.java
Environment data definition basically a root of all the lists.

Geoff1.java
Wooden table, blue glass ball and spinning earth animation super program.

InverseImage.java
Produces a negative jpg from another.

Lights.java
Light data structure.

Material.java
Material data structure.

MaterialCylinder.java
Texture data structure for Cylinder (cylindrical coordinates)

MaterialSphere.java
Texture data structure for Sphere (spherical coordinates)

MaterialTriangle.java
Texture data structure for triangles (flat, tiled)

RayGenerator.java
Generates the rays from the viewpoint origin into the scene.

Raynode.java
Data structure for a ray node.

RayTracerMain.java
Ray tracer main method if you want to use stand alone without a super program.

RayTracerUI.java
Probably the wireframe previewer.

RayTree.java
Implementation that creates the ray tree for the incoming ray to scene.

SceneReader.java
Reads the scene file into memory.

SkyMaker.java
Generates a starfield jpg according to spherical coordinate altered probabilities so you don’t have loads of stars at the poles!

Sphere.java
Data structure for the sphere.

SpriteSheet.java
Not related to the ray tracer used to generate a matrix of small tiles from a list of jpgs.

Tile.java
Tile a jpg with a single jpg not related to the ray tracer.

Triangle.java
Triangle data structure.

VectorAlgebra.java
Vector algebra library of methods.

View.java
View data structure.


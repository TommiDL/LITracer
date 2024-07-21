# Scene declaration file

## Pigments types
LITracer offer three different type of pigment. A pigment describe globally the spectrum of emissions of a surface.

*Notation: a Color is represented in RGB values using the format `<r, g, b>`*

 - **Uniform**: The surface has a single color spectrum of emissions coded in rgb values.
    - Declaration: `uniform(color)`
    - Example: `uniform(<0,1,3>)`

 - **Checkered**: The surface has a periodic repetition of squares with two different colors caractherized by a density set by an integer number
    - Declaration: `checkered(color_1, color_2, repetition)`
    - Example: `checkered(<1,0,0>, <0,1,0>, 4)`
 - **Image**: Copy the spectrum of a PFM file stretched out on the specific surface
    - Declaration: `image("pfm_file_path")`
    - Example: `image("pigment_images/gaia.pfm")`

## BRDF types
BRDF stands for 'Bidirectional reflectance distribution function' and encode the relation between the radiance that go out from a surface and the radiance that hit the surface.

**LITracer** support two different types of BRDFs:

- **Diffusive**: Every rays that hit the surface generate one or more scattered rays in a random direction inside a solid angle of 2 pi. the number of scattered rays from a single collision can be fixed from command line
   - Declaration: `diffuse(pigment)`
   - Example: `diffuse(uniform(<1,0,1>))`
- **Specular**: Every rays that hit the surface get reflected following the rules of optical geometry.
   - Declaration: `specular(pigment)`
   - Example: `specular(uniform(<1,1,1>))`

## Variables declaration
In the scene declaration file is possible to define 4 different families of variables:

- ## Float variables
   Define a float variable that can be re-used inside the declaration file:
   - Declaration: `float <variable_name>(<variable_value>)`
   - Example: `float x(10.3)`
- ## Material variables
   Define a material variable that describe the behavior of the object in light's interaction.

  A material is defined by two parameters:
   - Emitted Radiance:
   - BRDF:  
- ## Geometrical objects:
  In **LITracer** three different geometrical objects are avaiable 

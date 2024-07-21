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
 - **Image**: Copy the spectrum of a PFM file stretched out on the specific surface.
 
   Check the directory `pigment_images/` to start with some avaiable files.
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
   1. **BRDF**: The reflected radiance encoded with a BRDF (and her pigment)
   2. **Emitted Radiance**: The emission spectrum encoded with a pigment 
 
   - Declaration: `material material_name(brdf, emitted_radiance_pigment)`
   - Example: `material mirror(specular(uniform(<1,1,1>)), uniform(0,0,1))`
     
- ## Geometrical objects:
  In **LITracer** three different geometrical objects are avaiable
  - **Sphere**: declaration: `sphere(material, transformation)`
  - **Plane**: declaration: `plane(material, transformation)`
  - **Mesh of triangles**: declaration: `mesh(obj_file, material, transformation)`
    
    Check the directory `mesh_obj_files/` to start with some avaiable files.

   
  Every objects need a specified transformation, **LITracer** implement six different transformation:
 
  *Notation: a vector is encoded using the formalism [x,y,z]*
   - **Translation**
     - Declaration: `translation(vector)`
     - Example: `translation([1, 0, 0])`
   - **Scalar transformation**: scales along every specified direction of the selected factor 
     - Declaration: `scaling(vector)`
     - Example: `scaling([0.1, 2, 0.1])`
   - **Rotation along z axis**: angles in 360 degree
     - Declaration: `rotation_z(angle)`
     - Example: `rotation_z(180)`
   - **Rotation along y axis**: angles in 360 degree
     - Declaration: `rotation_y(angle)`
     - Example: `rotation_y(180)`
   - **Rotation along x axis**: angles in 360 degree
     - Declaration: `rotation_x(angle)`
     - Example: `rotation_x(180)`
   - **Identity**
     - Declaration: `identity`
     - Example: `identity`
   
  Transformation can be concateneted using the "*" symbol:
 
  `translation([1,0,0]) * scaling([0.1,0.1,0.1]) * rotation_z(45)`
 
    
- ## Camera type
  **LITracer** support two types of camera:
  - Perspective: perspective point of view of the observer
    - Declaration: `camera(perspective, transformation, aspect_ratio, screen_distance)` 
    - Example: `camera(perspective, translation([-1,0,0]), 1, 1)`
  - Orthogonal: planimetric point of view
    - Declaration: `camera(orthogonal, transformation, aspect_ratio)` 
    - Example: `camera(orthogonal, transformation, aspect_ratio)`
   
  If not defined the default value for the camera is `camera(perspective, identity, 1, 1)` 


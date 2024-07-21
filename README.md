![GitHub release (with filter)](https://img.shields.io/github/v/release/TommiDL/LITracer?include_prereleases&filter=v0.3.0)
![GitHub top language](https://img.shields.io/github/languages/top/TommiDL/LITracer)
![GitHub contributors](https://img.shields.io/github/contributors/TommiDL/LITracer)
![GitHub Release Date - Published_At](https://img.shields.io/github/release-date/TommiDL/LITracer)

# LITracer
**LITracer** is a photorealistic images generation software developed using [Ray-Tracing](https://developer.nvidia.com/discover/ray-tracing#:~:text=Ray%20tracing%20is%20a%20rendering,%2C%20shadows%2C%20and%20indirect%20lighting.) technique to simulate the realistic behavior of light reflection. 

**LITracer** allows the user to define a scene using different geometrical objects and use different rendering algorithms to generate a photorealistic image of the scene.

This raytracer was developed during Professor [Maurizio Tomasi](https://github.com/ziotom78)'s course [Numerical techniques for the generation of photorealistic images](https://ziotom78.github.io/raytracing_course/)

The code is written in [Kotlin](https://en.wikipedia.org/wiki/Kotlin_(programming_language)) language using [Gradle](https://en.wikipedia.org/wiki/Gradle) software.

## Installation

To obtain this code, in order to run or modify the program, download the zip file from the [latest](https://github.com/TommiDL/LITracer/releases/latest) version or clone this repository using the command `git clone https://github.com/TommiDL/LITracer `

The presence of **jdk version 21** is requested as a fundamental prerequisite for the correct functioning of the program


To satisfy the requested dependencies in order to run this code use the command `./gradlew build`.

Finally run the command `./gradlew test` to check the correct behavior of the code.

## Usage
**LITracer** can perform five tasks:

 - [Render](#render): render an image from a scene defined in a text file
 - [Demo](#demo): Create a demo image, useful to gain confidence with the software usage.
 - [pfm2png](#pfm2png): convert a PFM file into a PNG image using the requested conversion parameters
 - [png2pfm](#png2pfm): convert a PNG image into a PFM file usable to define the pigment of objects in scene's declarations
 
 - [Merge-Images](#merge-images): Merge multiple images into one,
   
   Useful to lower the noise using different importance sampling random seeds (see [Render](#render-usage))



## Render
   Reads a scene declaration from a text file  and create a pfm file and a png image of the scene using different rendering algorithm.

   The user is allowed to choose between perspective or orthognal point of view.

   The available rendering algorithms are the following:
   - onoff:         the objects of the scene are displayed in white color with a black background.
   
     Here some [examples](#on-off-renderer-examples) using onoff rendering algorithm.
     
   - flat:          the objects of the scene are displayed with their real colors without simulating the realistic behaviour of light

     Here some [examples](#flat-renderer-examples) using flat rendering algorithm.
   - path tracing:  the objects are displayed with their real colors and physical properties simulating the realistic behaviour of light

     Here some [examples](#pathtracing-renderer-examples) using pathtracing rendering algorithm.


See the [tutorial](Scene_File_Tutorial.md) to make a scene declaration file or try using the [example.txt]() file with the command:

`./gradlew run --args="render example.txt -pfm example  -png examples`

### Render usage:
 Basic Usage of render command:
 
    `./gradlew run --args="render <scene_file.txt> --algorithm=<render_alg> -pfm <output_pfm_file_path>  -png <output_png_file_path>"`

 All the images will be saved in the `images` folder.

   **Useful flags**:
   - `-pfm`, `--pfm-output`: set the name in wihch the software save the pfm output file [default value output.pfm]
   - `-png`, `--png-output`: set the name in which the software save the png output file [default value null]
   - `-alg`, `--algorithm`: Select rendering algorithm type [default value pathtracing]:
     - onoff -> rendering in black&white format
     - flat -> rendering in colored format
     - pathtracing -> rendering with pathtracing alg
   - `--bck-col`:set Background Color [default value black]
   - `-w`, `--width`: set the width of the PNG image [default value 480]
   - `-he`, `--height`: set the height of the PNG image [default value 480]

   **Pathtracing useful flags**:
   - `--nray`: set the number of scattered rays to generate after a surface collision in pathtracing algorithm [default value 10]
   
     (exponential growth of time complexity)

   - `-samples`, `--samples-per-pixel`: set the number of ray per pixels to process the color using importance sampling [default 1]
   
     (linear growth of time complexity)
   - `-seed`, `--samples-seed`: set the seed for the importance sampling rays production
   - `-md`, `--max-depth`: set max depth of bouncing per ray [default value 3]
   - `-rr`, `--russian-roul`: set the value of depth to start suppressing the ray bouncing probability [default value 3]

For further details execute `./gradlew run --args="render"` and get the complete usage documentation.

## Demo  
     
   Create a pfm file of a demo scene and (optionally) a PNG image. This functionality is meant to take confidence with the usage of the code.

   The demo command also allows the user to move inside the scene specifying the translation and rotation movements.

   #### Demo usage:
   Basic usage of demo command
   
    `./gradlew run --args="demo --camera=<camera_type> --algorithm=<render_alg> -pfm <output_pfm_file_path>  -png <output_png_file_path>"`


   For further details execute `./gradlew run --args="demo"` and get the complete usage documentation.
   

  
  ## pfm2png
   Execute conversion from a PFM file to a PNG image with the specified values of screen's gamma and clamp factor

   #### pfm2png usage:
   Usage of pfm2png
   
       `./gradlew run --args="pfm2png <input_PFM_file>.pfm <clamp value (float)> <gamma value of the screen (float)> <output_png_file>.png"`
   

   For further details execute `./gradlew run --args="pfm2png"` and get the complete usage documentation.


## png2pfm
   Execute conversion from a PNG image to a PFM file with the specified values of screen's gamma and clamp factor

#### png2pfm usage:

    `./gradlew run --args="png2pfm <output_PFM_file>.pfm <clamp value (float)> <gamma value of the screen (float)> <input_png_file>.png"`
   

   For further details execute `./gradlew run --args="png2pfm"` and get the complete usage documentation.

## Merge-Images
   The command take in input several PFM files of the same scene and merge them in a singular PFM file and PNG image to create a less noisy image of the scene.
   The single PFM file has to be created using the render command, passing it unique values for the importance sampling seed with the flag `--samples-seed` .

   See here an [example](#merge-images-examples).
   
### Merge-Images usage:

    `./gradlew run --args="image-merge [<input_pfm_files>] -pfm <output_pfm_path> -png <output_png_path>"`

   For further details execute `./gradlew run --args="image-merge"` and get the complete usage documentation.



## Gallery

<div align="center">
<img src="example_images/ludo_samples36_10ray.png", width="300">
<img src="example_images/gaia_plane.png", width="300">
<img src="example_images/cornel_10ray_36samples.png", width="300">
</div>

### Rendering algorithms examples

Here a comparison of the same image using the three different algorithms:
<div align="center">
  <img src=example_images/ludo_samples36_10ray.png width="300"/>
  &nbsp;&nbsp;&nbsp;&nbsp;
  <img src=example_images/ludo_flat.png width="300"/>
  &nbsp;&nbsp;&nbsp;&nbsp;
  <img src=example_images/ludo_onoff.png width="300"/>

</div>

<p align="center"><em>
 From the left to the right: (1) pathtracing algorithm, (2) flat algorithm, (3) onoff algorithm
</p></em>


#### On-Off renderer examples

Here is an example made with 11 spheres showing the behavior of on-off renderer using the two different choices of camera

<div align="center">
  <img src=example_images/onoff_example_perspective.png width="300"/>
  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  <img src=example_images/onoff_example_orthogonal.png alt="Orthogonal" width="300"/>
</div>

<p align="center"><em>
 The image on the left  was generated using perspective camera, the image on the right was generated using orthogonal camera
</p></em>

 

#### Flat renderer examples
Here some examples obtained using `./gradlew run --args="demo -alg flat"`

<div align="center">

<img src="example_images/flat_example.png" alt="Orthogonal" width="300"/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
<img src="example_images/flat_ortho.png" alt="Orthogonal" width="300"/>
</div>
<p align="center"><em>
Demo image obtained using flat tracing algorithm, on the left image generated using perspective camera, on the right image generated using orthogonal camera with a rotation of 45 degree relative to the z-axis 
</p></em>


#### Pathtracing renderer examples
Here some examples obtained using `./gradlew run --args="demo -alg pathtracing"`.

The following examples of the demo image were generated using the path-tracing algorithm for different values of the parameter `--nray` with perspective camera.

Each one of those images is obtained with a value of max depth fixed at 3.

<div align="center">
<img src="example_images/pt1_ray.png" alt="1 ray" width="300" title="1 ray"/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
<img src="example_images/pt_5ray.png" alt="5 ray" width="300"/>
</div>
<p align="center"><em>
On the left demo image obtained with nray=1, on the right demo image obtained with nray=5.
</em></p>


<div align="center">
<img src="example_images/pt15_ray.png" alt="15 ray" width="300"/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
<img src="example_images/pt_20ray.png" alt="20 ray" width="300"/>
</div>
<p align="center"><em>
On the left demo image obtained with nray=15, on the right demo image obtained with nray=20.
</em></p>

The following example is generated using orthogonal camera with parameters `--nray=10`, a traslation of 0.5 along z axis and of 10 along x axis and a rotation of 45 degrees relative to the z axis

<div align="center">
 <img src="example_images/pt_10nray_ortho.png", width="500">
</div>
<p align="center"><em>
 Demo image with orthogonal camera (800x500 pixels)
</em></p>

Finally some examples generated using a fixed value of `--nray=10` with different values of the max depth using perspective camera

<div align="center">
<img src="example_images/pt_1md.png", width="300">
<img src="example_images/pt_2md.png", width="300">
<img src="example_images/pt_3md.png", width="300">
</div>

<p align="center"><em>
From the left to the right: (1) max depth = 1, (2) max depth = 2, (3) max depth = 3
</em></p>

### Cornell Box
Here some examples of a cornell box like image using different values of `--nray` and `--samples-per-pixels`

**5 scattering rays per hit**
<div align="center">
<img src="example_images/cornel_5ray_1samples.png", width="300">
<img src="example_images/cornel_5ray_9samples.png", width="300">
<img src="example_images/cornel_5ray_16samples.png", width="300">
</div>
<p align="center"><em>
From the left to the right: (1) samples = 1, (2) samples = 9, (3) samples = 16
</em></p>
<div align="center">
<img src="example_images/cornel_5ray_25samples.png", width="300">
<img src="example_images/cornel_5ray_36samples.png", width="300">
</div>
<p align="center"><em>
From the left to the right: (1) samples = 25, (2) samples = 36
</em></p>


**7 scattering rays per hit**
<div align="center">
<img src="example_images/cornel_7ray_4samples.png", width="300">
<img src="example_images/cornel_7ray_9samples.png", width="300">
<img src="example_images/cornel_7ray_16samples.png", width="300">
</div>
<p align="center"><em>
From the left to the right: (1) samples = 4, (2) samples = 9, (3) samples = 16
</em></p>

### Merge Images examples

Here an example of several images generated using different samples-seed values merged using the command image-merge


<div align="center">
<img src="example_images/trial_seed100.png", width="200">
<img src="example_images/trial_seed15.png", width="200">
<img src="example_images/trial_seed7.png", width="200">
<img src="example_images/trial_seed70.png", width="200">
</div>
<p align="center"><em>
several images generated with different random seeds
</em></p>

<div align="center">
<img src="example_images/merge.png", width="300">
</div>

<p align="center"><em>
Merged image
</em></p>


## History

See the file [CHANGELOG.md](https://github.com/TommiDL/LITracer/blob/master/CHANGELOG.md) to have a full insight on LITracer's version history 

## License
The code is released under the Apache License version 2.0. See the file [LICENSE.md](https://github.com/TommiDL/LITracer/blob/master/LICENSE)

## Authors: 
[Tommaso Di Luciano](https://github.com/TommiDL),
[Maria Laura Ilisco](https://github.com/marialaurailisco),
[Ludovico Morabito](https://github.com/Ludovico-Morabito).

![GitHub release (with filter)](https://img.shields.io/github/v/release/TommiDL/LITracer?include_prereleases&filter=v0.3.0)
![GitHub top language](https://img.shields.io/github/languages/top/TommiDL/LITracer)
![GitHub contributors](https://img.shields.io/github/contributors/TommiDL/LITracer)
![GitHub Release Date - Published_At](https://img.shields.io/github/release-date/TommiDL/LITracer)
![GitHub](https://img.shields.io/github/license/TommiDL/LITracer)

# LITracer
**LITracer** is a photorealistic images generation software developed using [Ray-Tracing](https://developer.nvidia.com/discover/ray-tracing#:~:text=Ray%20tracing%20is%20a%20rendering,%2C%20shadows%2C%20and%20indirect%20lighting.) technique to simulate the realistic behavior of light reflection. 

The code is written in [Kotlin](https://en.wikipedia.org/wiki/Kotlin_(programming_language)) language using ![Gradle](https://en.wikipedia.org/wiki/Gradle) software.


## Installation

To obtain this code, in order to run or modify the program, download the zip file from the ![v0.3.0](https://github.com/TommiDL/LITracer/archive/refs/tags/v0.3.0.zip) version or clone this repository using the command `git clone https://github.com/TommiDL/LITracer `

The presence od **jdk version 21** is requested as a fundamental prerequisite for the correct functioning of the program


To satisfy the requested dependencies in order to run this code use the command `./gradlew build`.

Finally run the command `./gradlew test` to check the correct behavior of the code.

## Usage
**LITracer** can perform three tasks:

 - ### Demo  
     
   Create a pfm file of a demo scene and (optionally) a PNG image allowing the user to choose between a perspective or an orthogonal point of view
   (in fondo aggiungere due foto linkate) 

   The available rendering algorithms are the following:
   - onoff:         the objects of the scene are displayed in white color with a black background.
   
     Here some [examples](#on-off-renderer-examples) using onoff rendering algorithm.
     
   - flat:          the objects of the scene are displayed with their real colors without simulating the realistic behaviour of light

     Here some [examples](#flat-renderer-examples) using flat rendering algorithm.
   - path tracing:  the objects are displayed with their real colors and physical properties simulating the realistic behaviour of light

     Here some [examples](#pathtracing-renderer-examples) using pathtracing rendering algorithm.

   The demo command also allows the user to move inside the scene specifying the translation and rotation movements.

   #### Demo usage:
   Basic usage of demo command
   
       `./gradlew run --args="demo --camera=<camera_type> --algorithm=<render_alg> -pfm <output_pfm_file_path>  -png <output_png_file_path>"`


   For further details execute `./gradlew run --args="demo"` and get the complete usage documentation.
   

   
 - ### pfm2png
      Execute conversion from a PFM file to a PNG image with the specified values of screen's gamma and clamp factor

   #### pfm2png usage:
   Usage of pfm2png
   
       `./gradlew run --args="pfm2png <input_PFM_file>.pfm <clamp value (float)> <gamma value of the screen (float)> <output_png_file>.png"`
   

   For further details execute `./gradlew run --args="pfm2png"` and get the complete usage documentation.


 - ### png2pfm
    Execute conversion from a PNG image to a PFM file with the specified values of screen's gamma and clamp factor

    #### png2pfm usage:

       `./gradlew run --args="png2pfm <output_PFM_file>.pfm <clamp value (float)> <gamma value of the screen (float)> <input_png_file>.png"`
   

   For further details execute `./gradlew run --args="png2pfm"` and get the complete usage documentation.



### Examples

#### On-Off renderer examples

Here is an example made with 11 spheres showing the behavior of on-off renderer using the two different choices of camera

This image is not obtainable from the flag `--algorithm=onoff`, this is only an example of the renderer behavior


<div align="center">
  <img src="https://raw.githubusercontent.com/TommiDL/LITracer/pathtracing/example_images/onoff_example_perspective.png" width="300"/>
  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  <img src="https://raw.githubusercontent.com/TommiDL/LITracer/pathtracing/example_images/onoff_example_orthogonal.png" alt="Orthogonal" width="300"/>
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


## History

See the file [CHANGELOG.md](https://github.com/TommiDL/LITracer/blob/master/CHANGELOG.md) to have a full insight on LITracer's version history 

## License
The code is released under the Apache License version 2.0. See the file [LICENSE.md](https://github.com/TommiDL/LITracer/blob/master/LICENSE)

## Authors: 
[Tommaso Di Luciano](https://github.com/TommiDL),
[Maria Laura Ilisco](https://github.com/marialaurailisco),
[Ludovico Morabito](https://github.com/Ludovico-Morabito).

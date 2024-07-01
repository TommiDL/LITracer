# LITracer
This is a very simple [Ray-Tracer](https://developer.nvidia.com/discover/ray-tracing#:~:text=Ray%20tracing%20is%20a%20rendering,%2C%20shadows%2C%20and%20indirect%20lighting.) written in [Kotlin](https://en.wikipedia.org/wiki/Kotlin_(programming_language)) language 

## Installation

## Usage
The program was made using Gradle and can perform three tasks:

 - ### Demo  
     
   Create a pfm file of a demo scene and (optionally) a PNG image allowing the user to choose between a perspective or an orthogonal point of view
   (in fondo aggiungere due foto linkate) 

   The available rendering algorithms are the following:
   - onoff:         the objects of the scene are displayed in white color with a black background.
   
     Here an ![example](https://github.com/TommiDL/LITracer/blob/pathtracing/example_images/onoff_example_perspective.png) using onoff rendering algorithm.
     
   - flat:          the objects of the scene are displayed with their real colors without simulating the realistic behaviour of light

     Here an [example](###-examples) using flat rendering algorithm.
   - path tracing:  the objects are displayed with their real colors and physical properties simulating the realistic behaviour of light

     Here an [example](###-examples) using pathtracing rendering algorithm.

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

<p align="left">
Perspective Camera:
</p>
<p align="right">
 Orthogonal Camera:
<\p>
 
![](Spheres.png)     ![](Spheres.png)     


#### Flat renderer examples

#### Pathtracing renderer examples


## History

See the file [CHANGELOG.md](https://github.com/TommiDL/LITracer/blob/master/CHANGELOG.md)

## License
The code is released under the Apache License version 2.0. See the file [LICENSE.md](https://github.com/TommiDL/LITracer/blob/master/LICENSE)

## Authors: 
[Tommaso Di Luciano](https://github.com/TommiDL),
[Maria Laura Ilisco](https://github.com/marialaurailisco),
[Ludovico Morabito](https://github.com/Ludovico-Morabito).

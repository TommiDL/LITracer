## Head

## v1.0.0
- First public release of the LITracer code.
    
    The following functionalities are implemented:
    
    -  Render: render an image from a scene defined in a text file
    -  Demo: create a demo image, useful to gain confidence with the software usage.
    -  pfm2png: convert a PFM file into a PNG image using the requested conversion parameters
    -  png2pfm: convert a PNG image into a PFM file usable to define the pigment of objects in scene's declarations
    -  image-merge: merge multiple images into one


## v0.3.1
- Fix Issue [#18](https://github.com/TommiDL/LITracer/issues/18) in PR [#19](https://github.com/TommiDL/LITracer/pull/19) 


## v0.3.0
-   Third release of the code:
    Now 3 functionalities avaiable:
    -   demo: Generate a demo image with 3 different rendering algorithms
    -   pfm2png: convert a PFM file to a PNG image
    -   png2pfm: convert a PNG image to a PFM file

## v0.2.3
-   Fix an issue with the triangle shape [#17](https://github.com/TommiDL/LITracer/pull/17)

## v0.2.2
-   Fix an issue with the Transformation product [#15](https://github.com/TommiDL/LITracer/pull/15)

## v0.2.1
-   Fix an issue with the ortonormal basis creation in function create_onb_from_z [#11](https://github.com/TommiDL/LITracer/pull/11)

## v0.2.0

Second release of the code:

- The old functionality to convert from PFM file to PNG image is still available using the flag `--pfm2png`.
- The new feature demo is accessible using the flag `--demo` and creates a PNG image and related PFM file of multiple spheres in a 3D space
## v0.1.1

-   Fix an issue with the vertical order of the images [#6](https://github.com/TommiDL/LITracer/pull/6)

## v0.1.0
first version of the code
- implementation of the command to convert a .pfm image into a .png image

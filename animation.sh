#! /bin/bash

# -r 25: Number of frames per second
ffmpeg -r 25 -f image2 -s 640x480 -i ./animation/image%03d.png \
    -vcodec libx264 -pix_fmt yuv420p \
    spheres-perspective.mp4
    
rm animation/image*.png
rm animation/image*.pfm
>>>>>>> pathtracing

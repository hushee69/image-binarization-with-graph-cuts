# image-binraization-with-graph-cuts

### Separate image into black and white

Uses the Edmonds-Karp variant to find the maximum flow and minimum cut to divide the image pixels into foreground and background

### Compilation instructions

In the base directory of the project, enter the command

javac -d bin src/image/segmentation/*.java

This will create the `bin` folder if it doesn't already exist

Then while still in the base directory, execute the command below

java -cp ./bin image.segmentation.Application


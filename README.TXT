
#How to generate a binarized file 

1. make sure you have java on your machine and then change your directory to the directory of the mpd-binarizer.jar.Here it is in the dist directory.

2.copy the datasets to the current directory

3.open terminal and type

java -jar mpd-binarizer.jar -k <lookahead days> -r<lookback days> -i <datset file name> -cat <up or down>

.
for example
java -jar mpd-binarizer.jar -cat up -i ArtistDatePlaysMatrixGT60sTop1K.csv -k 5 -r 50


The program will create a directory named by song_bin_of_ArtistDatePlaysMatrixGT60sTop1K.csv or something similary according to the input file name and each .moses files in the directory are named by each song Id in the original dataset.


Caution
=======

If you want to run the jar file from other directory, make sure the dependecies in the jar file are in your java classpath. 



#!/bin/bash

echo "formatting binerized values in to tab separated values"
for file in $(ls $1/*.moses)
 do 
 sed -i 's/"//g' $file    #delete all "
 sed -i 's/\,/\t/g' $file #replace all , with tab
 done
echo "finished formatting"


#!/bin/sh
files=$(find ./code-examples/src/main/. -type f -name '*.java')
for f in $files;
do javac $f;
done

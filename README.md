# Secondary Sort using MapReduce and Spark
## Environment
HuaWei Cloud Server\
jdk 8\
Hadoop 2.7.7\
Spark 2.4.7\
sbt 1.4.4
## MapReduce
after git clone
```
cd Secondary-Sort
mkdir Secondsort
javac -d Secondsort src/Secondsort.java
jar -cvf secondsort.jar -C Secondsort .
hadoop jar secondsort.jar org.myorg.Secondsort input output
```
result is in output directory

## Spark
```
cd src/sparksort
sbt package
cd ../..
spark-submit --class "Sparksort" src/sparksort/target/scala-2.11/sparksort_2.11-1.0.jar
```
result is in spark-output\
you can run ```bash clean.sh``` to remove the output and intermediate file

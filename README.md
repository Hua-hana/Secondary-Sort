# Secondary Sort running on cluster
## Environment
HuaWei Cloud Server\

cluster:
* master
* slave1
* slave2

jdk 8\
Hadoop 2.7.7\
Spark 2.4.7\
sbt 1.4.4

**all add to environment**

## start cluster
you first need to create namenode
```
hdfs namenode -format
```
start hadoop cluster
```
 cd $HADOOP_HOME
 ./sbin/start-all.sh
```
start spark cluster
```
cd $SPARK_HOME
./sbin/start-all.sh
```

## MapReduce
generate .jar
```
git clone https://github.com/Hua-hana/Secondary-Sort.git
cd Secondary-Sort
mkdir Secondsort
javac -d Secondsort src/Secondsort.java
jar -cvf secondsort.jar -C Secondsort .
```
put input data to hdfs
```
hdfs dfs -mkdir /input
hdfs dfs -put input/* /input/
hdfs dfs -ls /input
```
run the program in cluster
```
hadoop jar secondsort.jar org.myorg.Secondsort /input/ /output/
```
result is in ```hdfs://master:9000/output``` directory, get it to local
```
hdfs dfs -get /output .
```
you can check the result in ```output```

## Spark
build it
```
cd src/sparksort
sbt package
cd ../..
```
submit it to sourceManager
```
spark-submit --master spark://master:7077  --class "Sparksort" src/sparksort/target/scala-2.11/sparksort_2.11-1.0.jar
```
the port is usually 7077, and you can change it

get the result to local
```
hdfs dfs -get /spark-output .
```
result is in ```spark-output```

### Clean up
you can run ```bash clean.sh``` to remove the output and intermediate file

package org.myorg;
	
	import java.io.IOException;
	import java.util.*;
	
	import org.apache.hadoop.fs.Path;
	import org.apache.hadoop.conf.*;
	import org.apache.hadoop.io.*;
	import org.apache.hadoop.mapred.*;
	import org.apache.hadoop.util.*;
	
	public class secondsort {
	
	   public static class Map extends MapReduceBase implements Mapper<LongWritable, Text, Text, IntWritable> {
	     private Text word = new Text();
	
	     public void map(LongWritable key, Text value, OutputCollector<Text, FloatWritable> output, Reporter reporter) throws IOException {
	       String line = value.toString();
	       StringTokenizer tokenizer = new StringTokenizer(line,",");
        
           String _date_time_ = tokenizer.nextToken();
           if (_date_time_.equals("\"Time\"")) return;
           String date = _date_time_.substring(1,11);
	       word.set(date);
           float t = Float.parseFloat(tokenizer.nextToken());
           output.collect(word,t);
	     }
	   }

	   public static class Reduce extends MapReduceBase implements Reducer<Text, FloatWritable, Text, FloatWritable> {
		public void reduce(Text key, Iterator<FloatWritable> values, OutputCollector<Text, FloatWritable> output, Reporter reporter) throws IOException {
			ArrayList<Float> result = new ArrayList<Float> ();
		  while (values.hasNext()) {
			result.add(values.next().get());
		  }
		  result.sort(Comparator.reverseOrder());
		  String sorted_tmp_list = result.toString();
		  output.collect(key, new Text(sorted_tmp_list));
		}
	  }

	  public static void main(String[] args) throws Exception {
		JobConf conf = new JobConf(secondsort.class);
		conf.setJobName("secondsort");
   
		conf.setOutputKeyClass(Text.class);
		conf.setOutputValueClass(Text.class);
   
		conf.setMapperClass(Map.class);
		//conf.setCombinerClass(Reduce.class);
		conf.setReducerClass(Reduce.class);
   
		conf.setInputFormat(TextInputFormat.class);
		conf.setOutputFormat(TextOutputFormat.class);
   
		FileInputFormat.setInputPaths(conf, new Path(args[0]));
		FileOutputFormat.setOutputPath(conf, new Path(args[1]));
   
		JobClient.runJob(conf);
	  }

	}

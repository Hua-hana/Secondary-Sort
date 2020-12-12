package org.myorg;
	
	import java.io.IOException;
	import java.util.*;
	
	import org.apache.hadoop.fs.Path;
	import org.apache.hadoop.conf.*;
	import org.apache.hadoop.io.*;
	import org.apache.hadoop.mapred.*;
	import org.apache.hadoop.util.*;
	
	public class Secondsort {
	
	   public static class Map extends MapReduceBase implements Mapper<LongWritable, Text, Text, FloatWritable> {
	     private Text word = new Text();
	
	     public void map(LongWritable key, Text value, OutputCollector<Text, FloatWritable> output, Reporter reporter) throws IOException {
	       String line = value.toString();
	       StringTokenizer tokenizer = new StringTokenizer(line,",");
        
           String _date_time_ = tokenizer.nextToken();
           if (_date_time_.equals("\"Time\"")) return;
           String date = _date_time_.substring(1,11);
	       word.set(date);
           float t = Float.parseFloat(tokenizer.nextToken());
           output.collect(word,new FloatWritable(t));
	     }
	   }

	   public static class Reduce extends MapReduceBase implements Reducer<Text, FloatWritable, Text, Text> {
		public void reduce(Text key, Iterator<FloatWritable> values, OutputCollector<Text, Text> output, Reporter reporter) throws IOException {
			ArrayList<Float> result = new ArrayList<Float> ();
		  while (values.hasNext()) {
			result.add(values.next().get());
		  }
		  result.sort(Comparator.reverseOrder());
		  String sorted_tmp_list = result.toString();
		  sorted_tmp_list = sorted_tmp_list.replace(",","");
		  sorted_tmp_list = sorted_tmp_list.substring(1,sorted_tmp_list.length()-2);
		  output.collect(key, new Text(sorted_tmp_list));
		}
	  }

	  public static void main(String[] args) throws Exception {
		JobConf conf = new JobConf(Secondsort.class);
		conf.setJobName("secondsort");

		conf.setOutputKeyClass(Text.class);
		conf.setOutputValueClass(FloatWritable.class);
 
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

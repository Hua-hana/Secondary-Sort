import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf

object Sparksort{
  def str_to_pair (str : String) : (String,Float) = {
        val strlist = str.split(",")
            val date = strlist(0).substring(1,11)
                val temp = strlist(1).toFloat
                    val re = new Tuple2(date,temp)
                        return re
  }
  def secondary_sort (ele : (String, Iterable[Float])) : (String, String) = {
        val (key,iter) = ele
            val sorted_array = iter.toArray.sorted.reverse.mkString(" ")
                return (key,sorted_array)
  }  
  def main(args: Array[String]) {
        val conf = new SparkConf().setAppName("Sparksort")
            val sc = new SparkContext(conf)
                val data = sc.textFile("input/London2013.csv")
                    val outputfile = "spark-output"
                        val filerdata = data.filter(x => x!="\"Time\",\"Temperature\"")
                            val pairs = filerdata.map(str_to_pair)
                                val group = pairs.groupByKey()
                                    val date_templist = group.map(secondary_sort).sortByKey()
                                        val out_str_list = date_templist.map(x => x.productIterator.mkString("  "))
                                            out_str_list.saveAsTextFile(outputfile)
  }
}

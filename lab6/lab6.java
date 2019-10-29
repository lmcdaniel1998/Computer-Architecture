// Luke McDaniel //
//  CPE 315-03   //
//     lab6      //


import java.util.*;
import java.lang.*;
import java.io.*;
import java.text.*;


public class lab6
{
  public static void main(String args[]) throws IOException
  {
    //long startTime = System.nanoTime();
    if (args.length != 1) {
      System.out.println(args.length);
      System.out.println("error incorrect command line syntax");
      System.exit(0);
    }

    // initialize all sets for storage
    Hashtable<String, Address> indicies_1 = new Hashtable<String, Address>();
    Hashtable<String, Address> indicies_2 = new Hashtable<String, Address>();    
    Hashtable<String, Address> indicies_3 = new Hashtable<String, Address>();
    Hashtable<String, Address> indicies_4 = new Hashtable<String, Address>();

    // Cache 1
    DMCache cache1 = new DMCache(4, 0, 512, 0, 0, indicies_1);
    // Cache 2
    DMCache cache2 = new DMCache(4, 2, 256, 0, 0, indicies_1);
    // Cache 3
    DMCache cache3 = new DMCache(4, 4, 128, 0, 0, indicies_1);
    // Cache 4
    SA2Cache cache4 = new SA2Cache(4, 0, 256, 0, 0, indicies_1, indicies_2);
    // Cache 5
    SA4Cache cache5 = new SA4Cache(4, 0, 128, 0, 0, indicies_1, indicies_2, indicies_3, indicies_4);
    // Cache 6
    SA4Cache cache6 = new SA4Cache(4, 4, 32, 0, 0, indicies_1, indicies_2, indicies_3, indicies_4);
    // Cache 7
    DMCache cache7 = new DMCache(4, 0, 1024, 0, 0, indicies_1);


    // turn file into a stream for more efficient processing
    File infile = new File(args[0]);
    InputStream is = new FileInputStream(infile);
    BufferedReader br = new BufferedReader(new InputStreamReader(is));

    // read file line by line until end of stream
    String line = br.readLine();
    int lineNum = 0;
    while (line != null) {
      // check proper format
      if (line.length() == 10) {
        String hexAddr = line.substring(line.length() - 8);
        
        // create address and access for cache 1
        Address addr1 = new Address("", "", "", "", lineNum);
        addr1.setAll(hexAddr, 1);
        cache1.accessCache(addr1);
        // create address and access for cache 2
        Address addr2 = new Address("", "", "", "", lineNum);
        addr2.setAll(hexAddr, 2);
        cache2.accessCache(addr2);
        // create address and access for cache 3
        Address addr3 = new Address("", "", "", "", lineNum);
        addr3.setAll(hexAddr, 3);
        cache3.accessCache(addr3);
        // create address and access for cache 4
        Address addr4 = new Address("", "", "", "", lineNum);
        addr4.setAll(hexAddr, 4);
        cache4.accessCache(addr4);
        // create address and access for cache 5
        Address addr5 = new Address("", "", "", "", lineNum);
        addr5.setAll(hexAddr, 5);
        cache5.accessCache(addr5);
        // create address and access for cache 6
        Address addr6 = new Address("", "", "", "", lineNum);
        addr6.setAll(hexAddr, 6);
        cache6.accessCache(addr6);
        // create address and access for cache 7
        Address addr7 = new Address("", "", "", "", lineNum);
        addr7.setAll(hexAddr, 7);
        cache7.accessCache(addr7);
      }
      line = br.readLine();
      lineNum++;
    }

    // call print function on all caches
    printData(cache1, 1);
    printData(cache2, 2);
    printData(cache3, 3);
    printData(cache4, 4);
    printData(cache5, 5);
    printData(cache6, 6);
    printData(cache7, 7);
    //long endTime = System.nanoTime();
    //long totalTime = endTime - startTime;
    //System.out.println("run time seconds: " + (0.000000001 * totalTime));
  }

  public static void printData(Cache cache, int cacheNum) {
    double hitRate = 0.0;
    hitRate = ((double)cache.getHits() / (double)cache.getAccesses()) * 100;
    DecimalFormat df = new DecimalFormat();
    df.setMaximumFractionDigits(2);
    if (cacheNum == 1) {
      System.out.println("Cache #1");
      System.out.println("Cache size: 2048B	Associativity: 1	Block size: 1");
      System.out.println("Hits: " + cache.getHits() + "	Hit Rate: " + df.format(hitRate) + "%");
      System.out.println("---------------------------");
    }
    else if (cacheNum == 2) {
      System.out.println("Cache #2");
      System.out.println("Cache size: 2048B	Associativity: 1	Block size: 2");
      System.out.println("Hits: " + cache.getHits() + "	Hit Rate: " + df.format(hitRate) + "%");
      System.out.println("---------------------------");
    }
    else if (cacheNum == 3) {
      System.out.println("Cache #3");
      System.out.println("Cache size: 2048B	Associativity: 1	Block size: 4");
      System.out.println("Hits: " + cache.getHits() + "	Hit Rate: " + df.format(hitRate) + "%");
      System.out.println("---------------------------");
    }
    else if (cacheNum == 4) {
      System.out.println("Cache #4");
      System.out.println("Cache size: 2048B	Associativity: 2	Block size: 1");
      System.out.println("Hits: " + cache.getHits() + "	Hit Rate: " + df.format(hitRate) + "%");
      System.out.println("---------------------------");
    }
    else if (cacheNum == 5) {
      System.out.println("Cache #5");
      System.out.println("Cache size: 2048B	Associativity: 4	Block size: 1");
      System.out.println("Hits: " + cache.getHits() + "	Hit Rate: " + df.format(hitRate) + "%");
      System.out.println("---------------------------");
    }
    else if (cacheNum == 6) {
      System.out.println("Cache #6");
      System.out.println("Cache size: 2048B	Associativity: 4	Block size: 4");
      System.out.println("Hits: " + cache.getHits() + "	Hit Rate: " + df.format(hitRate) + "%");
      System.out.println("---------------------------");
    }
    else if (cacheNum == 7) {
      System.out.println("Cache #7");
      System.out.println("Cache size: 4096B	Associativity: 1	Block size: 1");
      System.out.println("Hits: " + cache.getHits() + "	Hit Rate: " + df.format(hitRate) + "%");
      System.out.println("---------------------------");
    }
    else {
      System.out.println("cacheNum error");
      System.exit(0);
    }
  }
}

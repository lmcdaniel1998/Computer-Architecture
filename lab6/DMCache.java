import java.util.*;
import java.lang.*;
import java.io.*;
import java.text.*;

public class DMCache extends Cache
{
  public Hashtable<String, Address> cache;

  public DMCache(int byteLen, int blockLen, int indexLen, int hits, int accesses, Hashtable<String, Address> cache) {
    super(byteLen, blockLen, indexLen, hits, accesses);
    this.cache = cache;
  }


  void accessCache(Address addr) {
    //if (addr.getLineNum() < 10) {
      //System.out.println("byteOff: " + addr.getByte());
      //System.out.println("blockOff: " + addr.getBlock());
      //System.out.println("index: " + addr.getIndex());
      //System.out.println("tag: " + addr.getTag());
    //}
    // convert index into int and check if in proper range
    int index = Integer.parseInt(addr.getIndex(), 2);
    if (!(index >= 0 && index < super.getIndexLen())) {
      System.out.println("index out of bounds for cache1. Range 0 <= idx 511 actual: " + index + " at line " + addr.getLineNum());
      System.exit(0);
    }

    super.incAccesses();

    // check if the index is empty
    if (!cache.containsKey(addr.getIndex())) {
      // index is empty so fill
      cache.put(addr.getIndex(), addr);
    }
    else {
      // index is already occupied
      // check if tag is the same
      if (((cache.get(addr.getIndex())).getTag()).equals(addr.getTag())) {
        // if same tag just update line num
        (cache.get(addr.getIndex())).setLineNum(addr.getLineNum());
        super.incHits();
      }
      else {
        // if different tag then update whole block
        cache.put(addr.getIndex(), addr);
      }
    }
  }

  void initCache1() {
    super.setByteLen(4);
    super.setBlockLen(0);
    super.setIndexLen(512);
  }

  void initCache2() {
    super.setByteLen(4);
    super.setBlockLen(2);
    super.setIndexLen(256);
  }

  void initCache3() {
    super.setByteLen(4);
    super.setBlockLen(4);
    super.setIndexLen(128);
  }

  void initCache7() {
    super.setByteLen(4);
    super.setBlockLen(0);
    super.setIndexLen(1024);
  }

  Hashtable<String, Address> getCache() { return cache; }

  void setCache(Hashtable<String, Address> newCache) { cache = newCache; }
}

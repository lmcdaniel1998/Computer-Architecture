import java.util.*;
import java.lang.*;
import java.io.*;
import java.text.*;

public class Cache
{
  public int byteLen;
  public int blockLen;
  public int indexLen;
  public int hits;
  public int accesses;

  public Cache(int byteLen, int blockLen, int indexLen, int hits, int accesses) {
    this.byteLen = byteLen;
    this.blockLen = byteLen;
    this.indexLen = indexLen;
    this.hits = hits;
    this.accesses = accesses;
  }

  int getByteLen() { return byteLen; }

  void setByteLen(int newByte) { byteLen = newByte; }

  int getBlockLen() { return blockLen; }

  void setBlockLen(int newBlock) { blockLen = newBlock; }

  int getIndexLen() { return indexLen; }

  void setIndexLen(int newIndex) { indexLen = newIndex; }

  int getHits() { return hits; }

  void setHits(int newHC) { hits = newHC; }

  void incHits() { hits = hits + 1; }

  int getAccesses() { return accesses; }

  void setAccesses(int newAcc) { accesses = newAcc; }

  void incAccesses() { accesses = accesses + 1; }
}

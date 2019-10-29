import java.util.*;
import java.lang.*;
import java.io.*;
import java.text.*;
import java.math.BigInteger;


public class Address
{
  public String byteOff;
  public String blockOff;
  public String index;
  public String tag;
  public int lineNum;

  public Address(String byteOff, String blockOff, String index, String tag, int lineNum) {
    this.byteOff = byteOff;
    this.blockOff = blockOff;
    this.index = index;
    this.tag = tag;
    this.lineNum = lineNum;
  }
  
  String getByte() { return byteOff; }

  void setByte(String newByte) { byteOff = newByte; }

  String getBlock() { return blockOff; }

  void setBlock(String newBlock) { blockOff = newBlock; }

  String getIndex() { return index; }

  void setIndex(String newIdx) { index = newIdx; }

  String getTag() { return tag; }

  void setTag(String newTag) { tag = newTag; }

  int getLineNum() { return lineNum; }

  void setLineNum(int newLine) { lineNum = newLine; }

  void setAll(String addr, int cacheNum) {
    String binAddr = "";
    StringBuilder sb = new StringBuilder();

    if (addr.length() != 8) {
      System.out.println("address formatting error at line: " + lineNum + "	" + addr);
      System.exit(0);
    }
    //for (int x = 7; x >= 0; x --) {
      //sb.append(hexToBin(addr.charAt(x)));
    //}
    //sb.reverse();
    //binAddr = sb.toString();
    binAddr = new BigInteger(addr, 16).toString(2);
    if (binAddr.length() < 32) {
      int diff = 32 - binAddr.length();
      String pad = "";
      for (int c = 0; c < diff; c++) {
        pad = pad.concat("0");
      }
      binAddr = pad.concat(binAddr);
    }
    if (binAddr.length() != 32) {
      System.out.println("address binary incorrect length line: " + lineNum);
      System.exit(0);
    }

    // cache 1
    if (cacheNum == 1) {
      // byte offset: 2, block offset: 0, index: 9, tag 21
      byteOff = binAddr.substring(30, 32);
      blockOff = "";
      index = binAddr.substring(21, 30);
      tag = binAddr.substring(0, 21);
    }
    // cache 2
    else if (cacheNum == 2) {
      // byte offset: 2, block offset: 1, index: 8, tag 21
      byteOff = binAddr.substring(30, 32);
      blockOff = binAddr.substring(29, 30);
      index = binAddr.substring(21, 29);
      tag = binAddr.substring(0, 21);
    }
    else if (cacheNum == 3) {
      // byte offset: 2, block offset: 2, index: 7, tag 21
      byteOff = binAddr.substring(30, 32);
      blockOff = binAddr.substring(28, 30);
      index = binAddr.substring(21, 28);
      tag = binAddr.substring(0, 21);
    }
    else if (cacheNum == 4) {
      // byte offset: 2, block offset: 0, index: 8, tag 22
      byteOff = binAddr.substring(30, 32);
      blockOff = "";
      index = binAddr.substring(22, 30);
      tag = binAddr.substring(0, 22);
    }
    else if (cacheNum == 5) {
      // byte offset: 2, block offset: 0, index: 7, tag 23
      byteOff = binAddr.substring(30, 32);
      blockOff = "";
      index = binAddr.substring(23, 30);
      tag = binAddr.substring(0, 23);
    }
    else if (cacheNum == 6) {
      // byte offset: 2, block offset: 2, index: 5, tag 23
      byteOff = binAddr.substring(30, 32);
      blockOff = binAddr.substring(28, 30);
      index = binAddr.substring(23, 28);
      tag = binAddr.substring(0, 23);
    }
    else if (cacheNum == 7) {
      // byte offset: 2, block offset: 0, index: 10, tag 20
      byteOff = binAddr.substring(30, 32);
      blockOff = "";
      index = binAddr.substring(20, 30);
      tag = binAddr.substring(0, 20);
    }
    else {
      System.out.println("incorrect cache num");
      System.exit(0);
    }
    // printing out address when done converting to binary
    //if (lineNum < 10) {
      //System.out.println(lineNum + " raw address passed to setAll: " + addr);
      //System.out.println(lineNum + " binary address whole: " + binAddr);
    //}
  }

  String hexToBin(char hexNum) {
    StringBuilder sb = new StringBuilder(); 
    if (hexNum == '0') {
      sb.append("0000");
    }
    else if (hexNum == '1') {
      sb.append("1000");
    }
    else if (hexNum == '2') {
      sb.append("0100");
    }
    else if (hexNum == '3') {
      sb.append("1100");
    }
    else if (hexNum == '4') {
      sb.append("0010");
    }
    else if (hexNum == '5') {
      sb.append("1010");
    }
    else if (hexNum == '6') {
      sb.append("0110");
    }
    else if (hexNum == '7') {
      sb.append("1110");
    }
    else if (hexNum == '8') {
      sb.append("0001");
    }
    else if (hexNum == '9') {
      sb.append("1001");
    }
    else if (hexNum == 'a') {
      sb.append("0101");
    }
    else if (hexNum == 'b') {
      sb.append("1101");
    }
    else if (hexNum == 'c') {
      sb.append("1100");
    }
    else if (hexNum == 'd') {
      sb.append("1011");
    }
    else if (hexNum == 'e') {
      sb.append("0111");
    }
    else if (hexNum == 'f') {
      sb.append("1111");
    }
    else {
      System.out.println("line formatting error at line: " + lineNum);
      System.exit(0);
    }
    return sb.toString();
  }
}

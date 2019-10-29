import java.util.*;

public class Register extends Instruction
{
  public int rs;
  public int rt;
  public int rd;
  public int shamt;
  public int funct;
  
  public Register(String inst, int opcode, int pc, int rs, int rt, int rd, int shamt, int funct) 
  {
    super(inst, opcode, pc);
    this.rs = rs;
    this.rt = rt;
    this.rd = rd;
    this.shamt = shamt;
    this.funct = funct;
  }

  int getRS() { return rs; }
  int getRT() { return rt; }
  int getRD() { return rd; }
  int getShamt() { return shamt; }
  int getFunct() { return funct; }

  void setRS(int newRS) { rs = newRS; }
  void setRT(int newRT) { rt = newRT; }
  void setRD(int newRD) { rd = newRD; }
  void setShamt(int newShamt) { shamt = newShamt; }
  void setFunct(int newFunct) { funct = newFunct; }
}

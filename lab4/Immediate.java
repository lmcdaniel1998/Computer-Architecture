import java.util.*;

public class Immediate extends Instruction
{
  public int rs;
  public int rt;
  public int imm;
  public int taken;

  public Immediate(String inst, int opcode, int pc, int rs, int rt, int imm, int taken)
  {
    super(inst, opcode, pc);
    this.rs = rs;
    this.rt = rt;
    this.imm = imm;
    this.taken = taken;
  }

  int getRS() { return rs; }
  int getRT() { return rt; }
  int getIMM() { return imm; }
  int getTaken() { return taken; }

  void setRS(int newRS) { rs = newRS; }
  void setRT(int newRT) { rt = newRT; }
  void setIMM(int newIMM) { imm = newIMM; }
  void setTaken(int newTaken) { taken = newTaken; }
}

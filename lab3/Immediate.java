import java.util.*;

public class Immediate extends Instruction
{
  public int rs;
  public int rt;
  public int imm;

  public Immediate(String inst, int opcode, int pc, int rs, int rt, int imm)
  {
    super(inst, opcode, pc);
    this.rs = rs;
    this.rt = rt;
    this.imm = imm;
  }

  int getRS() { return rs; }
  int getRT() { return rt; }
  int getIMM() { return imm; }

  void setRS(int newRS) { rs = newRS; }
  void setRT(int newRT) { rt = newRT; }
  void setIMM(int newIMM) { imm = newIMM; }
}

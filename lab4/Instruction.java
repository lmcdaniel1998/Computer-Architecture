import java.util.*;

public class Instruction
{
  public String inst;
  public int opcode;
  public int pc;

  public Instruction(String inst, int opcode, int pc)
  {
    this.inst = inst;
    this.opcode = opcode;
    this.pc = pc;
  }

  String getInst() { return inst; }

  void setInst(String newInst) { inst = newInst; }

  int getOpcode() { return opcode; }

  void setOpcode(int newOpcode) { opcode = newOpcode; }

  int getPC() { return pc; }

  void setPC(int newPC) { pc = newPC; }
}

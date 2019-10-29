import java.util.*;

public class Jump extends Instruction
{
  public int address;

  public Jump(String inst, int opcode, int pc, int rs, int rt, int address)
  {
    super(inst, opcode, pc);
    this.address = address;
  }

  int getAddress() { return address; }
  void setAddress(int newAddress) { address = newAddress; }
}

import java.util.*;

public class Emulator
{
  public int pc;
  public int[] registers;
  public int[] dataMem;

  public Emulator(int pc, int[] registers, int[] dataMem)
  {
    this.pc = pc;
    this.registers = registers;
    this.dataMem = dataMem;
  }

  int getPC() { return pc; }

  void setPC(int newPC) { pc = newPC; }

  int[] getRegisters() { return registers; }

  void setRegisters(int[] newRegisters) { registers = newRegisters; }

  int getRegistersI(int idx) { return registers[idx]; }

  void setRegistersI(int idx, int value) { registers[idx] = value; }

  int[] getDataMem() { return dataMem; }

  void setDataMem(int[] newDataMem) { dataMem = newDataMem; }

  int getDataMemI(int idx) { return dataMem[idx]; }

  void setDataMemI(int idx, int value) { dataMem[idx] = value; }
}

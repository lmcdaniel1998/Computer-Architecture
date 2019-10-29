import java.util.*;

public class Emulator
{
  public int pc;
  public int prevPC;
  public int instRan;
  public int[] registers;
  public int[] dataMem;

  public Emulator(int pc, int PrevPC, int instRan, int[] registers, int[] dataMem)
  {
    this.pc = pc;
    this.prevPC = prevPC;
    this.registers = registers;
    this.dataMem = dataMem;
    this.instRan = instRan;
  }

  int getPC() { return pc; }

  void setPC(int newPC) { 
    prevPC = pc;
    pc = newPC;
  }

  int getPrevPC() { return prevPC; }

  void setPrevPC(int newPC) { prevPC = newPC; }
  
  int getInstRun() { return instRan; }

  void setInstRun(int newInst) { instRan = newInst; }

  int[] getRegisters() { return registers; }

  void setRegisters(int[] newRegisters) { registers = newRegisters; }

  int getRegistersI(int idx) { return registers[idx]; }

  void setRegistersI(int idx, int value) { registers[idx] = value; }

  int[] getDataMem() { return dataMem; }

  void setDataMem(int[] newDataMem) { dataMem = newDataMem; }

  int getDataMemI(int idx) { return dataMem[idx]; }

  void setDataMemI(int idx, int value) { dataMem[idx] = value; }
}

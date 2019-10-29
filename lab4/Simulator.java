import java.util.*;

public class Simulator
{
  public int cycles;
  public Instruction fetch;
  public Instruction decode;
  public Instruction execute;
  public Instruction memory;
  public Instruction writeBack;
  public Emulator emulator;
  public int instRun;

  public Simulator(int cycles, Instruction fetch, Instruction decode, Instruction execute, Instruction memory, Instruction writeBack, Emulator emulator, int instRun)
  {
    this.cycles = cycles;
    this.fetch = fetch;
    this.decode = decode;
    this.execute = execute;
    this.memory = memory;
    this.writeBack = writeBack;
    this.emulator = emulator;
    this.instRun = instRun;
  }

  int getPrevPC() { return emulator.getPrevPC(); }

  void setPrevPC(int newPC) { emulator.setPrevPC(newPC); }

  int getCyc() { return cycles; }

  void setCyc(int newCyc) { cycles = newCyc; }

  Instruction getIF() { return fetch; }

  void setIF(Instruction newIF) { fetch = newIF; }

  Instruction getID() { return decode; }

  void setID(Instruction newID) { decode = newID; }

  Instruction getEXE() { return execute; }

  void setEXE(Instruction newEXE) { execute = newEXE; }

  Instruction getMEM() { return memory; }

  void setMEM(Instruction newMEM) { memory = newMEM; }

  Instruction getWB() { return writeBack; }

  void setWB(Instruction newWB) { writeBack = newWB; }

  Emulator getEmulator() { return emulator; }

  void setEmulator(Emulator newEmulator) { emulator = newEmulator; }

  int getPC() { return emulator.getPC(); }

  void setPC(int newPC) { 
    emulator.setPrevPC(emulator.getPC());
    emulator.setPC(newPC); 
  }

  int [] getRegisters() { return emulator.getRegisters(); }

  void setRegisters(int[] newRegisters) { emulator.setRegisters(newRegisters); }

  int getRegistersI(int idx) { return emulator.getRegistersI(idx); }

  void setRegistersI(int idx, int value) { emulator.setRegistersI(idx, value); }

  int [] getDataMem() { return emulator.getDataMem(); }

  void setDataMem(int[] newDataMem) { emulator.setDataMem(newDataMem); }

  int getDataMemI(int idx) { return emulator.getDataMemI(idx); }

  void setDataMemI(int idx, int value) { emulator.setDataMemI(idx, value); }

  int getInstRun() { return instRun; }

  void setInstRun(int newC) { instRun = newC; }
}

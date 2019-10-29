// Luke McDaniel //
//  CPE 315-03   //
//     lab4      //


import java.util.*;
import java.lang.*;
import java.io.*;
import java.text.*;

public class lab4
{
  public static void main(String args[]) throws IOException
  {
    int isInteractive = -1;
    String command = "";
    Scanner scan;

    if (args.length == 1) {
      isInteractive = 1;
    }
    else if (args.length == 2) {
      isInteractive = 0;
    }
    else {
      System.out.println("error incorrect command line syntax");
      System.exit(0);
    }

    // hashtable that stores addresses of labels
    Hashtable<String, Integer> labelTable = new Hashtable<String, Integer>();
    // array list of all instructions in file
    ArrayList<Instruction> instArray = new ArrayList<Instruction>();

    labelTable = firstPass(args[0], labelTable);
    
    instArray = secondPass(args[0], labelTable, instArray);

    // create emulator object
    Emulator emulator = new Emulator(0, 0, 0, new int[32], new int[8192]);

    // create null instruction to initially populate pipeline
    Instruction nullInst = new Instruction("empty", -1, -1);

    // create simulator object
    Simulator simulator = new Simulator(0, nullInst, nullInst, nullInst, nullInst, nullInst, emulator, 0);

    if (isInteractive == 1) {
      scan = new Scanner(System.in);
    }
    else {
      File file = new File(args[1]);
      scan = new Scanner(file);
    }
    int printPC = 0;
    String printIF;
    String printID;
    String printEXE;
    String printMEM;

    while (true) {
        System.out.print("mips> ");
        command = scan.nextLine();
        if (isInteractive == 0) {
          System.out.println(command);
        }
        if (command.equals("h")) {
          System.out.println("");
          System.out.println("h = show help");
          System.out.println("d = dump register state");
          System.out.println("s = step through a single clock cycle step (i.e. simulate 1 cycle and stop)");
          System.out.println("s num = step through num clock cycles");
          System.out.println("r = run until the program ends and display timing summary");
          System.out.println("m num1 num2 = display data memory from location num1 to num2");
          System.out.println("c = clear all registers, memory, and the program counter to 0");
          System.out.println("q = exit the program");
          System.out.println("");
        }
        else if (command.equals("d")) {
          System.out.println("");
          System.out.println("pc = " + simulator.getPC());
          System.out.printf("$0 = %-10s", simulator.getRegistersI(0));
          System.out.printf(" $v0 = %-10s", simulator.getRegistersI(2));
          System.out.printf("$v1 = %-10s", simulator.getRegistersI(3));
          System.out.printf("$a0 = %-10s \n", simulator.getRegistersI(4));
          System.out.printf("$a1 = %-10s", simulator.getRegistersI(5));
          System.out.printf("$a2 = %-10s", simulator.getRegistersI(6));
          System.out.printf("$a3 = %-10s", simulator.getRegistersI(7));
          System.out.printf("$t0 = %-10s \n", simulator.getRegistersI(8));
          System.out.printf("$t1 = %-10s", simulator.getRegistersI(9));
          System.out.printf("$t2 = %-10s", simulator.getRegistersI(10));
          System.out.printf("$t3 = %-10s", simulator.getRegistersI(11));
          System.out.printf("$t4 = %-10s \n", simulator.getRegistersI(12));
          System.out.printf("$t5 = %-10s", simulator.getRegistersI(13));
          System.out.printf("$t6 = %-10s", simulator.getRegistersI(14));
          System.out.printf("$t7 = %-10s", simulator.getRegistersI(15));
          System.out.printf("$s0 = %-10s \n", simulator.getRegistersI(16));
          System.out.printf("$s1 = %-10s", simulator.getRegistersI(17));
          System.out.printf("$s2 = %-10s", simulator.getRegistersI(18));
          System.out.printf("$s3 = %-10s", simulator.getRegistersI(19));
          System.out.printf("$s4 = %-10s \n", simulator.getRegistersI(20));
          System.out.printf("$s5 = %-10s", simulator.getRegistersI(21));
          System.out.printf("$s6 = %-10s", simulator.getRegistersI(22));
          System.out.printf("$s7 = %-10s", simulator.getRegistersI(23));
          System.out.printf("$t8 = %-10s \n", simulator.getRegistersI(24));
          System.out.printf("$t9 = %-10s", simulator.getRegistersI(25));
          System.out.printf("$sp = %-10s", simulator.getRegistersI(29));
          System.out.printf("$ra = %-10s \n", simulator.getRegistersI(31));
          System.out.println("");
        }
        else if (command.charAt(0) == 'p') {
          System.out.println("pc		if/id	id/exe	exe/mem	mem/wb");
          System.out.println(simulator.getPC() + "		" + (simulator.getIF()).getInst() + "	" + (simulator.getID()).getInst() + "	" + (simulator.getEXE()).getInst() + "	" + (simulator.getMEM()).getInst());
        }
        else if (command.charAt(0) == 's') {
          if (command.equals("s") && command.length() == 1) {
            // run commands until entire pipeline is empty
            if (simulator.getPC() < instArray.size()) {
              // step through single clock cycle
              simulator = simulate_cpu_cycle(instArray, simulator);

              printPC = simulator.getPC();
              printMEM = (simulator.getMEM()).getInst();
              printEXE = (simulator.getEXE()).getInst();
              printID = (simulator.getID()).getInst();
              printIF = (simulator.getIF()).getInst();

              // printing jump
              if(((simulator.getIF()).getInst()).equals("j") || ((simulator.getIF()).getInst()).equals("jal") || ((simulator.getIF()).getInst()).equals("jr")) {
                printPC = simulator.getPrevPC() + 1;
              }
              // conditional in if/id register
              if (((simulator.getIF()).getInst()).equals("beq") || ((simulator.getIF()).getInst()).equals("bne")) {
                // if conditional is taken
                if (((Immediate)simulator.getIF()).getTaken() >= 0) {
                  printPC = ((Immediate)simulator.getIF()).getTaken() + 1;		// conditional pc + 1
                  printIF = (simulator.getIF()).getInst();				// if/id is conditional
                }
              }
              // conditional in id/exe register
              if (((simulator.getID()).getInst()).equals("beq") || ((simulator.getID()).getInst()).equals("bne")) {
                // conditional is taken
                if (((Immediate)simulator.getID()).getTaken() >= 0) {
                  printPC = ((Immediate)simulator.getID()).getTaken() + 2;			// conditional pc + 2
                  printID = (simulator.getID()).getInst();					// id/exe is conditional
                  printIF = instArray.get(((Immediate)simulator.getID()).getTaken() + 2).getInst();	// if/id is inst at conditional pc + 1
                }
              }
              // conditional in exe/mem register (when squashes are added)
              if (((simulator.getEXE()).getInst()).equals("beq") || ((simulator.getEXE()).getInst()).equals("bne")) {
                // conditional is taken
                if (((Immediate)simulator.getEXE()).getTaken() >= 0) {
                  printPC = ((Immediate)simulator.getEXE()).getTaken() + 3;			// conditional pc + 3
                  printEXE = (simulator.getEXE()).getInst();					// exe/mem is conditional
                  printID = instArray.get(((Immediate)simulator.getEXE()).getTaken() + 2).getInst();	// id/exe is inst at conditional pc + 1
                  printIF = instArray.get(((Immediate)simulator.getEXE()).getTaken() + 3).getInst();	// if/id is inst at conditional pc +2
                }
              }
              
              System.out.println("pc		if/id	id/exe	exe/mem	mem/wb");
              System.out.println(printPC + "		" + printIF + "	" + printID + "	" + printEXE + "	" + printMEM);
            }
            else {
              simulator = simulate_cpu_cycle(instArray, simulator);
            }
          }
          else {
            String[] tokens = command.split(" ");
            if (tokens.length == 2) {
              if (isInteger(tokens[1]) == 1) {
                // step through n clock cycles
                int startCyc = simulator.getCyc();
                while (simulator.getCyc() != startCyc + Integer.parseInt(tokens[1])) {
                  simulator = simulate_cpu_cycle(instArray, simulator);
                }
                System.out.println(tokens[1] + " instruction(s) executed");
              }
              else {
                System.out.println("invalid s instruction");
                System.exit(0);
              }
            }
            else {
              System.out.println("incorrect number of arguements s takes 2");
              System.exit(0);
            }
          }
        }
        else if (command.equals("r")) {
          // run entire program
          while (simulator.getPC() < instArray.size()) {
            simulator = simulate_cpu_cycle(instArray, simulator);
          }
          // count instructions still in pipeline
          if (!(((simulator.getIF()).getInst()).equals("stall") || ((simulator.getIF()).getInst()).equals("squash") || ((simulator.getIF()).getInst()).equals("empty"))) {
            simulator.setInstRun(simulator.getInstRun() + 1);
          }
          if (!(((simulator.getID()).getInst()).equals("stall") || ((simulator.getID()).getInst()).equals("squash") || ((simulator.getID()).getInst()).equals("empty"))) {
            simulator.setInstRun(simulator.getInstRun() + 1);
          }
          if (!(((simulator.getEXE()).getInst()).equals("stall") || ((simulator.getEXE()).getInst()).equals("squash") || ((simulator.getEXE()).getInst()).equals("empty"))) {
            simulator.setInstRun(simulator.getInstRun() + 1);
            simulator.setCyc(simulator.getCyc() + 1);
          }
          if (!(((simulator.getMEM()).getInst()).equals("stall") || ((simulator.getMEM()).getInst()).equals("squash") || ((simulator.getMEM()).getInst()).equals("empty"))) {
            simulator.setCyc(simulator.getCyc() + 1);
          }
          if (!(((simulator.getWB()).getInst()).equals("stall") || ((simulator.getWB()).getInst()).equals("squash") || ((simulator.getWB()).getInst()).equals("empty"))) {
            simulator.setCyc(simulator.getCyc() + 1);
          }
          // calculate CPI
          double cpi = (double)(simulator.getCyc() + 2) / simulator.getInstRun();
          DecimalFormat df = new DecimalFormat("#.###");
          System.out.println("Program complete");
          System.out.println("CPI = " + df.format(cpi) + "	Cycles = " + (simulator.getCyc() + 2) + "	Instructions = " + simulator.getInstRun());
        }
        else if (command.charAt(0) == 'm') {
          String[] tokens = command.split(" ");
          if (tokens.length == 3) {
            if (isInteger(tokens[1]) == 1 && isInteger(tokens[2]) == 1) {
              for (int i = Integer.parseInt(tokens[1]); i <= Integer.parseInt(tokens[2]); i++) {
                 System.out.println("[" + i + "]" + " = " + simulator.getDataMemI(i));
                 System.out.println("");
              }
            }
            else {
              System.out.println("invalid m instruction");
              System.exit(0);
            }
          }
          else {
            System.out.println("incorrect number of arguements m takes 3");
            System.exit(0);
          }
        }
        else if (command.equals("c")) {
          int[] newRegisters = new int[32];
          int[] newDataMem = new int[8192];
          emulator = new Emulator(0, 0, 0, newRegisters, newDataMem);
          // reset simulator
          simulator =new Simulator(0, nullInst,nullInst, nullInst, nullInst, nullInst, emulator, 0);
          System.out.println("Simulator reset\n");
        }
        else if (command.equals("q")) {
          break;
        }
        else {
          System.out.println("invalid instruction: " + command);
        }
      }
  }
 

  private static int useAfterLoad(Simulator simulator) {
    int isUseAfterLoad = 0;
    // check if id/exe is lw
    if (((simulator.getID()).getInst()).equals("lw")) {
    
      // determine instruction type of if/id arguement
      if (simulator.getIF() instanceof Register) {
        if (((Immediate)simulator.getID()).getRT() == ((Register)simulator.getIF()).getRS() || ((Immediate)simulator.getID()).getRT() == ((Register)simulator.getIF()).getRT()) {
          isUseAfterLoad = 1;
        }
      }
      else if (simulator.getIF() instanceof Immediate) {
          if (((Immediate)simulator.getID()).getRT() == ((Immediate)simulator.getIF()).getRS() || ((Immediate)simulator.getID()).getRT() == ((Immediate)simulator.getIF()).getRT()) {
          isUseAfterLoad = 1;
        }
      }
      else {
        isUseAfterLoad = 0;
      }
    }
    return isUseAfterLoad;
  }

 
  private static int conditionalTaken(Simulator simulator) {
    int branchTaken = 0;
    // check if instruction is beq or bne
    if (((simulator.getEXE()).getInst()).equals("beq")) {
      // check if branch is taken
      if (((Immediate)simulator.getEXE()).getTaken() >= 0) {
        branchTaken = 1;
      }
    }
    else if (((simulator.getEXE()).getInst()).equals("bne")) {
      // check if branch is taken
      if (((Immediate)simulator.getEXE()).getTaken() >= 0) {
        branchTaken = 1;
      }
    }
    else {
      branchTaken = 0;
    }
    return branchTaken;
  }


  private static int incondJump(Simulator simulator) {
    int incondJump = 0;
    // check if instruction is j, jal, jr
    if (((simulator.getIF()).getInst()).equals("j") || ((simulator.getIF()).getInst()).equals("jal") || ((simulator.getIF()).getInst()).equals("jr")) {
      incondJump = 1;
    }
    return incondJump;
  }


  private static Simulator simulate_cpu_cycle(ArrayList<Instruction> instArray, Simulator simulator) {

    Instruction stallInst = new Instruction("stall", -1, -1);
    Instruction squashInst = new Instruction("squash", -1, -1);
    Instruction nullInst = new Instruction("empty", -1, -1);

    if (!(((simulator.getEXE()).getInst()).equals("stall") || ((simulator.getEXE()).getInst()).equals("squash") || ((simulator.getEXE()).getInst()).equals("empty"))) {
      simulator.setInstRun(simulator.getInstRun() + 1);
    }

    simulator.setCyc(simulator.getCyc() + 1);
    
    // check for use after load
    if (useAfterLoad(simulator) == 1) {
      // move mem data to wb
      simulator.setWB(simulator.getMEM());
    
      // move ex data to mem
      simulator.setMEM(simulator.getEXE());

      // move lw to exe
      simulator.setEXE(simulator.getID());

      // put stall in pipeline
      simulator.setID(stallInst);

      // leave inst in if/id alone
      
      //simulator.setCyc(simulator.getCyc() + 1);
    }

    // check for branch taken
    else if (conditionalTaken(simulator) == 1) {

      simulator.setPC(((Immediate)simulator.getEXE()).getTaken() + 1 + ((Immediate)simulator.getEXE()).getIMM());

      // move mem data to wb
      simulator.setWB(simulator.getMEM());

      // move conditional to mem
      simulator.setMEM(simulator.getEXE());

      // squash three previous instructions
      simulator.setEXE(squashInst);
      simulator.setID(squashInst);
      simulator.setIF(squashInst);

      //simulator.setCyc(simulator.getCyc() + 3);
    }

    // check for jump instruction
    else if (incondJump(simulator) == 1) {
      // move mem data to wb
      simulator.setWB(simulator.getMEM());
    
      // move ex data to mem
      simulator.setMEM(simulator.getEXE());
    
      // move id to exe
      simulator.setEXE(simulator.getID());
    
      // move jump inst to id
      simulator.setID(simulator.getIF());

      // move squash into if
      simulator.setIF(squashInst);

      //simulator.setCyc(simulator.getCyc() + 1);
    }

    // regular inst
    else {
      // move mem data to wb
      simulator.setWB(simulator.getMEM());
    
      // move ex data to mem
      simulator.setMEM(simulator.getEXE());
    
      // move id to exe
      simulator.setEXE(simulator.getID());
    
      // move if to id
      simulator.setID(simulator.getIF());

      //simulator.setCyc(simulator.getCyc() + 1);

      if (simulator.getPC() <= instArray.size()) {
        // move next instruction into if
        simulator.setIF(instArray.get(simulator.getPC()));

        if (!(((simulator.getIF()).getInst()).equals("null") || ((simulator.getIF()).getInst()).equals("stall") || ((simulator.getIF()).getInst()).equals("squash"))) {
          // emulate next instruction (emulation occurs in if stage)
          simulator.setEmulator(emulate(instArray, simulator.getEmulator(), 1, 0));
        }
      }
      else {
        // move mem data to wb
        simulator.setWB(simulator.getMEM());
    
        // move ex data to mem
        simulator.setMEM(simulator.getEXE());
    
        // move id to exe
        simulator.setEXE(simulator.getID());
    
        // move if to id
        simulator.setID(simulator.getIF());

        // put empty instruction in pipeline
        simulator.setIF(nullInst);

        //simulator.setCyc(simulator.getCyc() + 1);
      }
    }

    return simulator;
  }


  private static Emulator emulate(ArrayList<Instruction> instArray, Emulator emulator, int instructions, int isR) {

    int instCount = instructions;

    while (instCount >= 1) {
      if (emulator.getPC() >= instArray.size()) {
        break;
      }
      // and
      if (((instArray.get(emulator.getPC())).getInst()).equals("and")) {
        emulator.setRegistersI(((Register)instArray.get(emulator.getPC())).getRD(), emulator.getRegistersI(((Register)instArray.get(emulator.getPC())).getRS()) & emulator.getRegistersI(((Register)instArray.get(emulator.getPC())).getRT()));
        emulator.setPC(emulator.getPC() + 1);
      }
      // or
      else if (((instArray.get(emulator.getPC())).getInst()).equals("or")) {
        emulator.setRegistersI(((Register)instArray.get(emulator.getPC())).getRD(), emulator.getRegistersI(((Register)instArray.get(emulator.getPC())).getRS()) | emulator.getRegistersI(((Register)instArray.get(emulator.getPC())).getRT()));
        emulator.setPC(emulator.getPC() + 1);
      }
      // add
      else if (((instArray.get(emulator.getPC())).getInst()).equals("add")) {
        emulator.setRegistersI(((Register)instArray.get(emulator.getPC())).getRD(), emulator.getRegistersI(((Register)instArray.get(emulator.getPC())).getRS()) + emulator.getRegistersI(((Register)instArray.get(emulator.getPC())).getRT()));
        emulator.setPC(emulator.getPC() + 1);
      }
      // addi
      else if (((instArray.get(emulator.getPC())).getInst()).equals("addi")) {
        emulator.setRegistersI(((Immediate)instArray.get(emulator.getPC())).getRT(), emulator.getRegistersI(((Immediate)instArray.get(emulator.getPC())).getRS()) + ((Immediate)instArray.get(emulator.getPC())).getIMM());
        emulator.setPC(emulator.getPC() + 1);
      }
      // sll
      else if (((instArray.get(emulator.getPC())).getInst()).equals("sll")) {
        emulator.setRegistersI(((Register)instArray.get(emulator.getPC())).getRD(), emulator.getRegistersI(((Register)instArray.get(emulator.getPC())).getRT()) << ((Register)instArray.get(emulator.getPC())).getShamt());
        emulator.setPC(emulator.getPC() + 1);
      }     
      // sub
      else if (((instArray.get(emulator.getPC())).getInst()).equals("sub")) {
        emulator.setRegistersI(((Register)instArray.get(emulator.getPC())).getRD(), emulator.getRegistersI(((Register)instArray.get(emulator.getPC())).getRS()) - emulator.getRegistersI(((Register)instArray.get(emulator.getPC())).getRT()));
        emulator.setPC(emulator.getPC() + 1);
      }
      // slt
      else if (((instArray.get(emulator.getPC())).getInst()).equals("slt")) {
        if (emulator.getRegistersI(((Register)instArray.get(emulator.getPC())).getRS()) < emulator.getRegistersI(((Register)instArray.get(emulator.getPC())).getRT())) {
          emulator.setRegistersI(((Register)instArray.get(emulator.getPC())).getRD(), 1);
        }
        else {
          emulator.setRegistersI(((Register)instArray.get(emulator.getPC())).getRD(), 0);
        }
        emulator.setPC(emulator.getPC() + 1);
      }
      // beq
      else if (((instArray.get(emulator.getPC())).getInst()).equals("beq")) {
        if (emulator.getRegistersI(((Immediate)instArray.get(emulator.getPC())).getRS()) == emulator.getRegistersI(((Immediate)instArray.get(emulator.getPC())).getRT())) {
          ((Immediate)instArray.get(emulator.getPC())).setTaken(emulator.getPC());
          emulator.setPC(emulator.getPC() + 1 + ((Immediate)instArray.get(emulator.getPC())).getIMM());
        }
        else {
          ((Immediate)instArray.get(emulator.getPC())).setTaken(-1);
          emulator.setPC(emulator.getPC() + 1);
        }
      }
      // bne
      else if (((instArray.get(emulator.getPC())).getInst()).equals("bne")) {
        if (emulator.getRegistersI(((Immediate)instArray.get(emulator.getPC())).getRS()) != emulator.getRegistersI(((Immediate)instArray.get(emulator.getPC())).getRT())) {
          ((Immediate)instArray.get(emulator.getPC())).setTaken(emulator.getPC());
          emulator.setPC(emulator.getPC() + 1 + ((Immediate)instArray.get(emulator.getPC())).getIMM());
        }
        else {
          ((Immediate)instArray.get(emulator.getPC())).setTaken(-1);
          emulator.setPC(emulator.getPC() + 1);
        }
      }
      // lw
      else if (((instArray.get(emulator.getPC())).getInst()).equals("lw")) {
        emulator.setRegistersI(((Immediate)instArray.get(emulator.getPC())).getRT(), emulator.getDataMemI(emulator.getRegistersI(((Immediate)instArray.get(emulator.getPC())).getRS()) + ((Immediate)instArray.get(emulator.getPC())).getIMM()));
        emulator.setPC(emulator.getPC() + 1);
      }
      // sw
      else if (((instArray.get(emulator.getPC())).getInst()).equals("sw")) {
        emulator.setDataMemI(emulator.getRegistersI(((Immediate)instArray.get(emulator.getPC())).getRS()) + ((Immediate)instArray.get(emulator.getPC())).getIMM(), emulator.getRegistersI(((Immediate)instArray.get(emulator.getPC())).getRT()));
        emulator.setPC(emulator.getPC() + 1);
      }
      // j
      else if (((instArray.get(emulator.getPC())).getInst()).equals("j")) {
        emulator.setPC(((Jump)instArray.get(emulator.getPC())).getAddress());
      }
      // jr
      else if (((instArray.get(emulator.getPC())).getInst()).equals("jr")) {
        emulator.setPC(emulator.getRegistersI(((Register)instArray.get(emulator.getPC())).getRS()));
      }
      // jal
      else if (((instArray.get(emulator.getPC())).getInst()).equals("jal")) {
        emulator.setRegistersI(31, emulator.getPC() + 1);
        emulator.setPC(((Jump)instArray.get(emulator.getPC())).getAddress());
      }
      else {
        System.out.println("error not real instruction");
        System.exit(0);
      }

      if (isR != 1) {
        instCount--;
      }
    }
    return emulator;
  }


  private static ArrayList<Instruction> secondPass(String filename, Hashtable<String, Integer> labelTable, ArrayList<Instruction> instArray) throws IOException
  {
    FileReader in = null;

    // read file until EOF is reached //
    try {

      in = new FileReader(filename);

      int lineNum = 0;
      int c;
      int afterPound = 0;

      StringBuilder lineContent = new StringBuilder("");

      while ((c = in.read()) != -1) {
        // newline char indicates new line //
        if (c != 10) {
          // if you hit comment everything is commented unti next newline //
          if (c == 35) {
            afterPound = 1;
          }
          if (afterPound == 0) {
	    lineContent.append((char)c);
          }
        }
        else {
          // ignore blank lines //
          if (isBlankLine(lineContent.toString()) == 1) {
            // extract instruction from line
            instArray = getInst(lineContent.toString(), labelTable, instArray, lineNum);
            // increment line number
            lineNum++;
          }
          afterPound = 0;
          lineContent = new StringBuilder("");
        }
      }
      return instArray;
    }
    finally {
      if (in != null) {
        in.close();
      }
    }
  }


  private static Hashtable<String, Integer> firstPass(String filename, Hashtable<String, Integer> labelTable) throws IOException
  {
    FileReader in = null;

    // read file until EOF is reached //
    try {

      in = new FileReader(filename);

      int lineNum = 0;
      int c;
      int afterPound = 0;

      StringBuilder lineContent = new StringBuilder("");

      while ((c = in.read()) != -1) {
        // newline char indicates new line //
        if (c != 10) {
          // if you hit comment everything is commented unti next newline //
          if (c == 35) {
            afterPound = 1;
          }
          if (afterPound == 0) {
	    lineContent.append((char)c);
          }
        }
        else {
          // ignore blank lines //
          if (isBlankLine(lineContent.toString()) == 1) {
            // determine if line has a label
            labelTable = getLabelLoc(lineContent.toString(), labelTable, lineNum);
            // increment line number
            lineNum++;
          }
          afterPound = 0;
          lineContent = new StringBuilder("");
        }
      }
      return labelTable;
    }
    finally {
      if (in != null) {
        in.close();
      }
    }
  }


  private static ArrayList<Instruction> getInst(String oneline, Hashtable<String, Integer> labelTable, ArrayList<Instruction> instArray, int lineNum)
  {
    // remove all whitespace from line
    String line = oneline.replaceAll("\\s+", "");
    String instruction = "";
    String label = "";
    String jumpLoc = "";

    int hasLabel = 0;
    int semiLoc = -1;
    int isJ = 0;
    int isJal = 0;

    int commaCount = 0;
    int firstComma= -1; 
    int secondComma = -1;

    int $count = 0;
    int $first = -1;
    int $second = -1;
    int $third = -1;

    for (int idx = 0; idx < line.length(); idx++) {
      // look for label
      if (line.charAt(idx) == ':') {
        if (hasLabel == 0) {
          label = line.substring(0, line.length() - idx);
        }
        else {
          // error too many : in line
        }
        hasLabel = 1;
        semiLoc = idx;
      }
      
      if (line.charAt(idx) == '$') {
        // hit first register
        if ($count == 0) {
          if (hasLabel == 0) {
            instruction = line.substring(0, idx);
          }
          else {
            instruction = line.substring(semiLoc + 1, idx);
          }
          $first = idx;
          $count = 1;
        }
        // hit second register
        else if ($count == 1) {
          $second = idx;
          $count = 2;
        }
        // hit thrid register
        else if ($count == 2) {
          $third = idx;
          $count = 3;
        }
        else {
          // error
          System.out.println("too many registers line: " + lineNum);
          System.exit(0);
        }
      }

      if (line.charAt(idx) == ',') {
        // first comma hit
        if (commaCount == 0) {
          firstComma = idx;
          commaCount = 1;
        }
        // second comma hit
        else if (commaCount == 1) {
          secondComma = idx;
          commaCount = 2;
        }
        else {
          // error too many commas
          System.out.println("comma error line: " + lineNum);
          System.exit(0);
        }        
      }
    }
    // no registers encountered in line
    if ($count == 0) {
      // line has a label
      if (hasLabel == 1) {
        // check if ther are enough chars for valid j
        if ((line.length() - semiLoc) > 1) {
          // check if j
          if ((line.charAt(semiLoc + 1)) == 'j') {
            // check if there are enough chars for a valid jal
            if ((line.length() - semiLoc) > 3) {
              // check if jal
              if ((line.substring(semiLoc + 1, semiLoc + 4)).equals("jal")) {
                // check if valid label behing jal
                if (labelTable.containsKey(line.substring(semiLoc + 4))) {
                  instruction = "jal";
                  jumpLoc = line.substring(semiLoc + 4);
                  isJal = 1;
                }
              }
            }
            // check if valid label behind j
            if (labelTable.containsKey(line.substring(semiLoc + 2))) {
              instruction = "j";
              jumpLoc = line.substring(semiLoc + 2);
              isJ = 1;
            }
            if (!((isJ != 1) ^ (isJal != 1))) {
              // error not real label
              System.out.println("1 invalid label line: " + lineNum);
              System.exit(0);
            }
          }
        }
      }
      // no label
      else {
        // check if ther are enough chars for valid j
        if (line.length() > 1) {
          // check if j
          if (line.charAt(0) == 'j') {
            // check if there are enough chars for a valid jal
            if (line.length() > 3) {
              // check if jal
              if ((line.substring(0, 3)).equals("jal")) {
                // check if valid label behing jal
                if (labelTable.containsKey(line.substring(3))) {
                  instruction = "jal";
                  jumpLoc = line.substring(3);
                  isJal = 1;
                }
              }
            }
            // check if valid label behind j
            if ((isJal != 1) && labelTable.containsKey(line.substring(1))) {
              instruction = "j";
              jumpLoc = line.substring(1);
              isJ = 1;
            }
            if (!((isJ != 1) ^ (isJal != 1))) {
              // error not real label
              System.out.println("3 invalid label line: " + line.substring(1));
              System.exit(0);
            }
          }
        }
      }
    }

    int rs, rt, rd, imm, address, shamt;
    int firstParen = 0;
    int idxFirst = 0;
    int secondParen = 0;
    int idxSecond = 0;
    int properF;

    // and
    if (instruction.equals("and")) {
      if (($count < 3) || (commaCount < 2)) {
        // error not enough registers or commas
        System.out.println("not enough registers or commas line: " + lineNum);
        System.exit(0);
      }
      // create new Instruction Object
      Register inst = new Register("and", 0, lineNum, 0, 0, 0, 0, 36);
      // get rd
      rd = getRegNum(line.substring($first, $second));
      if ( rd != 100) {
        inst.setRD(rd);
      }
      else {
        // error
        System.out.println("0 Syntax error on line: " + lineNum);
        System.exit(0);
      }
      // get rs
      rs = getRegNum(line.substring($second, $third));
      if (rs != 100) {
        inst.setRS(rs);
      }
      else {
        // error
        System.out.println("1 Syntax error on line: " + lineNum);
        System.exit(0);
      }
      // get rt
      rt = getRegNum(line.substring($third));
      if (rt != 100) {
        inst.setRT(rt);
      }
      else {
        // error
        System.out.println("2 Syntax error on line: " + lineNum);
        System.exit(0);
      }
      instArray.add(inst);
    }
    // or
    else if (instruction.equals("or")) {
      if (($count < 3) || (commaCount < 2)) {
        // error not enough registers or commas
        System.out.println("not enough registers or commas line: " + lineNum);
        System.exit(0);
      }
      // create new Instruction Object
      Register inst = new Register("or", 0, lineNum, 0, 0, 0, 0, 37);
      // get rd
      rd = getRegNum(line.substring($first, $second));
      if ( rd != 100) {
        inst.setRD(rd);
      }
      else {
        // error
        System.out.println("3 Syntax error on line: " + lineNum);
        System.exit(0);
      }
      // get rs
      rs = getRegNum(line.substring($second, $third));
      if (rs != 100) {
        inst.setRS(rs);
      }
      else {
        // error
        System.out.println("4 Syntax error on line: " + lineNum);
        System.exit(0);
      }
      // get rt
      rt = getRegNum(line.substring($third));
      if (rt != 100) {
        inst.setRT(rt);
      }
      else {
        // error
        System.out.println("5 Syntax error on line: " + lineNum);
        System.exit(0);
      }
      instArray.add(inst);
    }
    // add
    else if (instruction.equals("add")) {
      if (($count < 3) || (commaCount < 2)) {
        // error not enough registers or commas
        System.out.println("not enough registers or commas line: " + lineNum);
        System.exit(0);
      }
      // create new Instruction Object
      Register inst = new Register("add", 0, lineNum, 0, 0, 0, 0, 32);
      // get rd
      rd = getRegNum(line.substring($first, $second));
      if ( rd != 100) {
        inst.setRD(rd);
      }
      else {
        // error
        System.out.println("6 Syntax error on line: " + lineNum);
        System.exit(0);
      }
      // get rs
      rs = getRegNum(line.substring($second, $third));
      if (rs != 100) {
        inst.setRS(rs);
      }
      else {
        // error
        System.out.println("7 Syntax error on line: " + lineNum);
        System.exit(0);
      }
      // get rt
      rt = getRegNum(line.substring($third));
      if (rt != 100) {
        inst.setRT(rt);
      }
      else {
        // error
        System.out.println("8 Syntax error on line: " + lineNum);
        System.exit(0);
      }
      instArray.add(inst);
    }
    // addi
    else if (instruction.equals("addi")) {
      if (($count != 2) || (commaCount < 2)) {
        // error not enough registers or commas
        System.out.println("not enough registers or commas line: " + lineNum);
        System.exit(0);
      }

      // create new Instruction Object
      Immediate inst = new Immediate("addi", 8, lineNum, 0, 0, 0, 0);
      // get rt
      rt = getRegNum(line.substring($first, $second));
      if (rt != 100) {
        inst.setRT(rt);
      }
      else {
        // error
        System.out.println("9 Syntax error on line: " + lineNum);
        System.exit(0);
      }
      // get rs
      rs = getRegNum(line.substring($second, secondComma));
      if (rs != 100) {
        inst.setRS(rs);
      }
      else {
        // error
        System.out.println("10 Syntax error on line: " + lineNum);
        System.exit(0);
      }
      // get imm
      // check if hex
      if ((line.substring(secondComma + 1)).length() >= 3) {
        if (((line.substring(secondComma + 1)).charAt(0) == '0') && ((line.substring(secondComma + 1)).charAt(1) == 'x') || ((line.substring(secondComma + 1)).charAt(1) == 'X')) {
          // check if actual number after hex
          if (isInteger((line.substring(secondComma + 1)).substring(2)) == 1) {
            imm = Integer.parseInt((line.substring(secondComma + 1)).substring(2), 16);
            inst.setIMM(imm);
          }
          else {
            // error
            System.out.println("11 Syntax error on line: " + lineNum);
            System.exit(0);
          }
        }
      }
      // check if decimal
      if (isInteger(line.substring(secondComma + 1)) == 1) {
        imm = Integer.parseInt(line.substring(secondComma + 1), 10);
        inst.setIMM(imm);
      }
      else {
        // error
        System.out.println("13 Syntax error on line: " + lineNum);
        System.exit(0);
      }
      instArray.add(inst);
    }
    // sll
    else if (instruction.equals("sll")) {
      if (($count != 2) || (commaCount < 2)) {
        // error not enough registers or commas
        System.out.println("not enough registers or commas line: " + lineNum);
        System.exit(0);
      }

      // create new Instruction Object
      Register inst = new Register("sll", 0, lineNum, 0, 0, 0, 0, 0);
      // get rd
      rd = getRegNum(line.substring($first, $second));
      if ( rd != 100) {
        inst.setRD(rd);
      }
      else {
        // error
        System.out.println("14 Syntax error on line: " + lineNum);
        System.exit(0);
      }
      // get rt
      rt = getRegNum(line.substring($second, secondComma));
      if (rt != 100) {
        inst.setRT(rt);
      }
      else {
        // error
        System.out.println("15 Syntax error on line: " + lineNum);
        System.exit(0);
      }
      // get shift num
      // check if hex
      if ((line.substring(secondComma + 1)).length() >= 3) {
        if (((line.substring(secondComma + 1)).charAt(0) == '0') && ((line.substring(secondComma + 1)).charAt(1) == 'x') || ((line.substring(secondComma + 1)).charAt(1) == 'X')) {
          // check if actual number after hex
          if (isInteger((line.substring(secondComma + 1)).substring(2)) == 1) {
            shamt = Integer.parseInt((line.substring(secondComma + 1)).substring(2), 16);
            inst.setShamt(shamt);
          }
          else {
            // error
            System.out.println("16 Syntax error on line: " + lineNum);
            System.exit(0);
          }
        }
        else {
          // error
          System.out.println("17 Syntax error on line: " + lineNum);
          System.exit(0);
        }
      }
      // check if decimal
      else if (isInteger(line.substring(secondComma + 1)) == 1) {
        shamt = Integer.parseInt(line.substring(secondComma + 1), 10);
        inst.setShamt(shamt);
      }
      instArray.add(inst);
    }
    // sub
    else if (instruction.equals("sub")) {
      if (($count < 3) || (commaCount < 2)) {
        // error not enough registers or commas
        System.out.println("not enough registers or commas line: " + lineNum);
        System.exit(0);
      }
      // create new Instruction Object
      Register inst = new Register("sub", 0, lineNum, 0, 0, 0, 0, 34);
      // get rd
      rd = getRegNum(line.substring($first, $second));
      if ( rd != 100) {
        inst.setRD(rd);
      }
      else {
        // error
        System.out.println("18 Syntax error on line: " + lineNum);
        System.exit(0);
      }
      // get rs
      rs = getRegNum(line.substring($second, $third));
      if (rs != 100) {
        inst.setRS(rs);
      }
      else {
        // error
        System.out.println("19 Syntax error on line: " + lineNum);
        System.exit(0);
      }
      // get rt
      rt = getRegNum(line.substring($third));
      if (rt != 100) {
        inst.setRT(rt);
      }
      else {
        // error
        System.out.println("20 Syntax error on line: " + lineNum);
        System.exit(0);
      }
      instArray.add(inst);
    }
    // slt
    else if (instruction.equals("slt")) {
      if (($count < 3) || (commaCount < 2)) {
        // error not enough registers or commas
        System.out.println("not enough registers or commas line: " + lineNum);
        System.exit(0);
      }
      // create new Instruction Object
      Register inst = new Register("slt", 0, lineNum, 0, 0, 0, 0, 42);
      // get rd
      rd = getRegNum(line.substring($first, $second));
      if ( rd != 100) {
        inst.setRD(rd);
      }
      else {
        // error
        System.out.println("21 Syntax error on line: " + lineNum);
        System.exit(0);
      }
      // get rs
      rs = getRegNum(line.substring($second, $third));
      if (rs != 100) {
        inst.setRS(rs);
      }
      else {
        // error
        System.out.println("22 Syntax error on line: " + lineNum);
        System.exit(0);
      }
      // get rt
      rt = getRegNum(line.substring($third));
      if (rt != 100) {
        inst.setRT(rt);
      }
      else {
        // error
        System.out.println("23 Syntax error on line: " + lineNum);
        System.exit(0);
      }
      instArray.add(inst);
    }
    // beq
    else if (instruction.equals("beq")) {
      if (($count != 2) || (commaCount < 2)) {
        // error not enough registers or commas
        System.out.println("not enough registers or commas line: " + lineNum);
        System.exit(0);
      }
      Immediate inst = new Immediate("beq", 4, lineNum, 0, 0, 0, 0);
      // get rs
      rs = getRegNum(line.substring($first, $second));
      if (rs != 100) {
        inst.setRS(rs);
      }
      else {
        // error
        System.out.println("24 Syntax error on line: " + lineNum);
        System.exit(0);
      }
      // get rt
      rt = getRegNum(line.substring($second, secondComma));
      if (rt != 100) {
        inst.setRT(rt);
      }
      else {
        // error
        System.out.println("25 Syntax error on line: " + lineNum);
        System.exit(0);
      }
      // calculate beq offset
      if (labelTable.containsKey(line.substring(secondComma + 1))) {
        int labelAdd = labelTable.get(line.substring(secondComma + 1));
        //System.out.println("beq line: " + lineNum + "   label line: " + labelAdd);
        imm = (labelAdd - 1) - lineNum;
        //System.out.println("beq offset: " + imm);
        inst.setIMM(imm);
      }
      else {
        // error
        System.out.println("26 Syntax error on line: " + lineNum);
        System.exit(0);
      }
      instArray.add(inst);
    }
    // bne
    else if (instruction.equals("bne")) {
      if (($count != 2) || (commaCount < 2)) {
        // error not enough registers or commas
        System.out.println("not enough registers or commas line: " + lineNum);
        System.exit(0);
      }
      Immediate inst = new Immediate("bne", 5, lineNum, 0, 0, 0, 0);
      // get rs
      rs = getRegNum(line.substring($first, $second));
      if (rs != 100) {
        inst.setRS(rs);
      }
      else {
        // error
        System.out.println("27 Syntax error on line: " + lineNum);
        System.exit(0);
      }
      // get rt
      rt = getRegNum(line.substring($second, secondComma));
      if (rt != 100) {
        inst.setRT(rt);
      }
      else {
        // error
        System.out.println("28 Syntax error on line: " + lineNum);
        System.exit(0);
      }
      // calculate bne offset
      if (labelTable.containsKey(line.substring(secondComma + 1))) {
        int labelAdd = labelTable.get(line.substring(secondComma + 1));
        //System.out.println("bne line: " + lineNum + "    label line: " + labelAdd);
        imm = (labelAdd - 1) - lineNum;
        //System.out.println("bne offset: " + imm);
        inst.setIMM(imm);
      }
      else {
        // error
        System.out.println("29 Syntax error on line: " + lineNum);
        System.exit(0);
      }
      instArray.add(inst);
    }
    // lw
    else if (instruction.equals("lw")) {
      if (($count != 2) || (commaCount != 1)) {
        // error not enough registers or commas
        System.out.println("not enough registers or commas line: " + lineNum);
        System.exit(0);
      }
      // create new Instruction Object
      Immediate inst = new Immediate("lw", 35, lineNum, 0, 0, 0, 0);
      // get rt
      rt = getRegNum(line.substring($first, firstComma));
      if (rt != 100) {
        inst.setRT(rt);
      }
      else {
        // error
        System.out.println("30 Syntax error on line: " + lineNum);
        System.exit(0);
      }
      // get imm and rs
      // check if proper format
      if (line.length() >= 6) {
        for (int v = 0; v < line.length(); v++) {
          if (line.charAt(v) == '(') {
            if (firstParen == 1) {
              // error
              System.out.println("31 Syntax error on line: " + lineNum);
              System.exit(0);
            }
            firstParen = 1;
            idxFirst = v;
          }
          if (line.charAt(v) == ')') {
            if (secondParen == 1) {
              // error
              System.out.println("32 Syntax error on line: " + lineNum);
              System.exit(0);
            }
            secondParen = 1;
            idxSecond = v;
          }
        }
        properF = firstParen + secondParen;
        if (properF == 2 && idxFirst < idxSecond) {
          // check if hex
          if ((line.charAt(firstComma + 1) == '0') && ((line.charAt(firstComma + 2) == 'x') || (line.charAt(firstComma + 2) == 'X'))) {
            // check if actual number after hex
            if (isInteger(line.substring(firstComma + 3, idxFirst)) == 1) {
              // set imm
              imm = Integer.parseInt(line.substring(firstComma + 3, idxFirst), 16);
              inst.setIMM(imm);
            }
            else {
              // error
              System.out.println("33 Syntax error on line: " + lineNum);
              System.exit(0);
            }
          }
          // check if decimal
          else if (isInteger(line.substring(firstComma + 1, idxFirst)) == 1) {
            // set imm
            imm = Integer.parseInt(line.substring(firstComma + 1, idxFirst), 10);
            inst.setIMM(imm);
          }
          else {
            // error
            System.out.println("34 Syntax error on line: " + lineNum);
            System.exit(0);
          }
          // get rs
          rs = getRegNum(line.substring(idxFirst + 1, idxSecond));
          if (rs != 100) {
            inst.setRS(rs);
          }
          else {
            // error
            System.out.println("35 Syntax error on line: " + lineNum);
            System.exit(0);
          }
        }
        else {
          // error
          System.out.println("36 Syntax error on line: " + lineNum);
          System.exit(0);
        }
      }
      else {
        // error
        System.out.println("37 Syntax error on line: " + lineNum);
        System.exit(0);
      }
      instArray.add(inst);
    }
    // sw
    else if (instruction.equals("sw")) {
      if (($count != 2) || (commaCount != 1)) {
        // error not enough registers or commas
        System.out.println("not enough registers or commas line: " + lineNum);
        System.exit(0);
      }

      // create new Instruction Object
      Immediate inst = new Immediate("sw", 43, lineNum, 0, 0, 0, 0);
      // get rt
      rt = getRegNum(line.substring($first, firstComma));
      if (rt != 100) {
        inst.setRT(rt);
      }
      else {
        // error
        System.out.println("38 Syntax error on line: " + lineNum);
        System.exit(0);
      }
      // get imm and rs
      // check if proper format
      if (line.length() >= 6) {
        for (int v = 0; v < line.length(); v++) {
          if (line.charAt(v) == '(') {
            if (firstParen == 1) {
              // error
              System.out.println("39 Syntax error on line: " + lineNum);
              System.exit(0);
            }
            firstParen = 1;
            idxFirst = v;
          }
          if (line.charAt(v) == ')') {
            if (secondParen == 1) {
              // error
              System.out.println("40 Syntax error on line: " + lineNum);
              System.exit(0);
            }
            secondParen = 1;
            idxSecond = v;
          }
        }
        properF = firstParen + secondParen;
        if (properF == 2 && idxFirst < idxSecond) {
          // check if hex
          if ((line.charAt(firstComma + 1) == '0') && ((line.charAt(firstComma + 2) == 'x') || (line.charAt(firstComma + 2) == 'X'))) {
            // check if actual number after hex
            if (isInteger(line.substring(firstComma + 3, idxFirst)) == 1) {
              // set imm
              imm = Integer.parseInt(line.substring(firstComma + 3, idxFirst), 16);
              inst.setIMM(imm);
            }
            else {
              // error
              System.out.println("41 Syntax error on line: " + lineNum);
              System.exit(0);
            }
          }
          // check if decimal
          else if (isInteger(line.substring(firstComma + 1, idxFirst)) == 1) {
            // set imm
            imm = Integer.parseInt(line.substring(firstComma + 1, idxFirst), 10);
            inst.setIMM(imm);
          }
          else {
            // error
            System.out.println("42 Syntax error on line: " + lineNum);
            System.exit(0);
          }
          // get rs
          rs = getRegNum(line.substring(idxFirst + 1, idxSecond));
          if (rs != 100) {
            inst.setRS(rs);
          }
          else {
            // error
            System.out.println("43 Syntax error on line: " + lineNum);
            System.exit(0);
          }
        }
        else {
          // error
          System.out.println("44 Syntax error on line: " + lineNum);
          System.exit(0);
        }
      }
      else {
        // error
        System.out.println("45 Syntax error on line: " + lineNum);
        System.exit(0);
      }
      instArray.add(inst);
    }
    // j
    else if (instruction.equals("j")) {
      if (($count != 0) || (commaCount != 0)) {
        // error not enough registers or commas
        System.out.println("not enough registers or commas line: " + lineNum);
        System.exit(0);
      }
      // create new Instruction Object
      Jump inst = new Jump("j", 2, lineNum, 0, 0, 0);
      // calculate j target
      if (labelTable.containsKey(jumpLoc)) {
        address = labelTable.get(jumpLoc);
        inst.setAddress(address);
      }
      else {
        // error
        System.out.println("46 Syntax error on line: " + lineNum);
        System.exit(0);
      }
      instArray.add(inst);
    }
    // jr
    else if (instruction.equals("jr")) {
      if (($count != 1) || (commaCount != 0)) {
        // error not enough registers or commas
        System.out.println("not enough registers or commas line: " + lineNum);
        System.exit(0);
      }
      // create new Instruction Object
      Register inst = new Register("jr", 0, lineNum, 0, 0, 0, 0, 8);
      // get rs
      rs = getRegNum(line.substring($first));
      if (rs != 100) {
        inst.setRS(rs);
      }
      else {
        // error
        System.out.println("47 Syntax error on line: " + lineNum);
        System.exit(0);
      }
      instArray.add(inst);
    }
    // jal
    else if (instruction.equals("jal")) {
      if (($count != 0) || (commaCount != 0)) {
        // error not enough registers or commas
        System.out.println("not enough registers or commas line: " + lineNum);
        System.exit(0);
      }
      // create new Instruction Object
      Jump inst = new Jump("jal", 3, lineNum, 0, 0, 0);
      // calculate jal target
      if (labelTable.containsKey(jumpLoc)) {
        address = labelTable.get(jumpLoc);
        inst.setAddress(address);
      }
      else {
        // error
        System.out.println("48 Syntax error on line: " + lineNum);
        System.exit(0);
      } 
      instArray.add(inst);
    }
    else {
      if (!((instruction.equals("")) && (hasLabel == 1))) {
        System.out.println("invalid instruction: " + instruction);
        System.out.println("len: " + instruction.length());
        System.exit(0);
      }
    }
    // return instruction array
    return instArray;
  }


  // function to determine where every label is called
  private static Hashtable<String, Integer> getLabelLoc(String oneline, Hashtable<String, Integer> labelTable, int lineNum)
  {
    int labelEnd;
    int hasLabel = 0;
    char[] line = oneline.toCharArray();
    int len = line.length;

    for (int i = 0; i < len; i++ ) {
      // stop reading if comment is found
      if ((int)(line[i]) == 35) {
        break;
      }
      // if semicolon is hit before comment then line has label
      if ((int)(line[i]) == 58) {
        hasLabel = 1;
        labelEnd = i;
        break;
      }
    }
    // cycle through line again to get label
    if (hasLabel == 1) {
      int y = 0;
      int hitAN = 0;
      StringBuilder label = new StringBuilder("");
      while ((int)(line[y]) != 58) {
        // if alphanumerical has not already been reached
        if (hitAN == 0) {
          // not whitespace
          if (!Character.isWhitespace(line[y])) {
            // check if alphanumerical
            if (Character.isLetterOrDigit(line[y])) {
              // add alpha to label and set hitAN
              label.append((char)line[y]);
              hitAN = 1;
            }
            else {
              // error
              System.out.println("50 Syntax error on line: " + lineNum);
              System.exit(0);
            }
          }
          // character is whitespace skip before hitAN set
        }
        // if label has already started
        else {
          // if character isnt a whitespace add to label
          if (!Character.isWhitespace(line[y])) {
            label.append((char)line[y]);
          }
          else {
            // error
            System.out.println("51 Syntax error on line: " + lineNum);
            System.exit(0);
          }
        }
        y++;
      }
      // place line location of label in hashtable
      labelTable.put(label.toString(), lineNum);
    }
    // line has no label, skip
    return labelTable;
  }

  private static int getRegNum(String reg) 
  {
    if (reg.equals("$0") || reg.equals("$0,") || reg.equals("$zero") || reg.equals("$zero,")) {
      return 0;
    }
    else if (reg.equals("$v0") || reg.equals("$v0,")) {
      return 2;
    }
    else if (reg.equals("$v1") || reg.equals("$v1,")) {
      return 3;
    } 
    else if (reg.equals("$a0") || reg.equals("$a0,")) {
      return 4;
    }
    else if (reg.equals("$a1") || reg.equals("$a1,")) {
      return 5;
    }
    else if (reg.equals("$a2") || reg.equals("$a2,")) {
      return 6;
    }
    else if (reg.equals("$a3") || reg.equals("$a3,")) {
      return 7;
    }
    else if (reg.equals("$t0") || reg.equals("$t0,")) {
      return 8;
    }
    else if (reg.equals("$t1") || reg.equals("$t1,")) {
      return 9;
    }
    else if (reg.equals("$t2") || reg.equals("$t2,")) {
      return 10;
    }
    else if (reg.equals("$t3") || reg.equals("$t3,")) {
      return 11;
    }
    else if (reg.equals("$t4") || reg.equals("$t4,")) {
      return 12;
    }
    else if (reg.equals("$t5") || reg.equals("$t5,")) {
      return 13;
    }
    else if (reg.equals("$t6") || reg.equals("$t6,")) {
      return 14;
    }
    else if (reg.equals("$t7") || reg.equals("$t7,")) {
      return 15;
    }
    else if (reg.equals("$s0") || reg.equals("$s0,")) {
      return 16;
    }
    else if (reg.equals("$s1") || reg.equals("$s1,")) {
      return 17;
    }
    else if (reg.equals("$s2") || reg.equals("$s2,")) {
      return 18;
    }
    else if (reg.equals("$s3") || reg.equals("$s3,")) {
      return 19;
    }
    else if (reg.equals("$s4") || reg.equals("$s4,")) {
      return 20;
    }
    else if (reg.equals("$s5") || reg.equals("$s5,")) {
      return 21;
    }
    else if (reg.equals("$s6") || reg.equals("$s6,")) {
      return 22;
    }
    else if (reg.equals("$s7") || reg.equals("$s7,")) {
      return 23;
    }
    else if (reg.equals("$t8") || reg.equals("$t8,")) {
      return 24;
    }
    else if (reg.equals("$t9") || reg.equals("$t9,")) {
      return 25;
    }
    else if (reg.equals("$sp") || reg.equals("$sp,")) {
      return 29;
    }
    else if (reg.equals("$ra") || reg.equals("$ra,")) {
      return 31;
    }
    else {
      // error
      return 100;
    }
  }

  private static int isBlankLine(String line)
  {
    if (line.trim().isEmpty()) {
      line = null;
    }
    // check if line is empty
    if (line != null) {
      int len = line.length();
      // check if line starts with comment
      if ((int)(line.charAt(0)) == 35) {
        return 0;
      }
      else {
        return 1;
      }
    }
    else {
      return 0;
    }
  }

  public static int isInteger(String s) {
    if (s.isEmpty()) {
      return 0;
    }
    else {
      for (int i = 0; i < s.length(); i++) {
        if (i == 0 && s.charAt(i) == '-') {
          if (s.length() == 1) {
            return 0;
          }
          else {
            continue;
          }
        }
        if (Character.digit(s.charAt(i), 10) < 0) {
          return 0;
        }
      }
     return 1;
    }
  }

  public static String extractBinary(Instruction inst) {
    String binaryString = "";
    String tempBin;
    int tempBinLen;
    char msb;
    int addBits = 0;
    String addBitsStr = "";
    if (inst instanceof Register) {
      // opcode
      tempBin = Integer.toBinaryString(((Register)inst).getOpcode());
      //System.out.println("R opcode before clip: " + tempBin);
      tempBinLen = tempBin.length();
      if (tempBinLen >= 6) {
        tempBin = tempBin.substring(tempBinLen - 6);
      }
      else {
        addBits = 6 - tempBinLen;
        addBitsStr = new String(new char[addBits]).replace('\0', '0');
        tempBin = addBitsStr + tempBin;
      }
      //System.out.println("R opcode after clip: " + tempBin);
      binaryString = binaryString + " " + tempBin;
      // rs
      tempBin = Integer.toBinaryString(((Register)inst).getRS());
      //System.out.println("R rs before clip: " + tempBin);
      tempBinLen = tempBin.length();
      if (tempBinLen >= 5) {
        tempBin = tempBin.substring(tempBinLen - 5);
      }
      else {
        addBits = 5 - tempBinLen;
        addBitsStr = new String(new char[addBits]).replace('\0', '0');
        tempBin = addBitsStr + tempBin;
      }
      //System.out.println("R rs after clip: " + tempBin);
      binaryString = binaryString + " " + tempBin;
      // rt
      tempBin = Integer.toBinaryString(((Register)inst).getRT());
      //System.out.println("R rt before clip: " + tempBin);
      tempBinLen = tempBin.length();
      if (tempBinLen >= 5) {
        tempBin = tempBin.substring(tempBinLen - 5);
      }
      else {
        addBits = 5 - tempBinLen;
        addBitsStr = new String(new char[addBits]).replace('\0', '0');
        tempBin = addBitsStr + tempBin;
      }
      //System.out.println("R rt after clip: " + tempBin);
      binaryString = binaryString + " " + tempBin;
      // rd
      tempBin = Integer.toBinaryString(((Register)inst).getRD());
      //System.out.println("R rd before clip: " + tempBin);
      tempBinLen = tempBin.length();
      if (tempBinLen >= 5) {
        tempBin = tempBin.substring(tempBinLen - 5);
      }
      else {
        addBits = 5 - tempBinLen;
        addBitsStr = new String(new char[addBits]).replace('\0', '0');
        tempBin = addBitsStr + tempBin;
      }
      //System.out.println("R rd after clip: " + tempBin);
      binaryString = binaryString + " " + tempBin;
      // shamt
      tempBin = Integer.toBinaryString(((Register)inst).getShamt());
      //System.out.println("R shamt before clip: " + tempBin);
      tempBinLen = tempBin.length();
      if (tempBinLen >= 5) {
        tempBin = tempBin.substring(tempBinLen - 5);
      }
      else {
        msb = tempBin.charAt(0);
        addBits = 5 - tempBinLen;
        addBitsStr = new String(new char[addBits]).replace('\0', '0');
        tempBin = addBitsStr + tempBin;
      }
      //System.out.println("R shamt after clip: " + tempBin);
      binaryString = binaryString + " " + tempBin;
      // funct
      tempBin = Integer.toBinaryString(((Register)inst).getFunct());
      //System.out.println("R funct before clip: " + tempBin);
      tempBinLen = tempBin.length();
      if (tempBinLen >= 6) {
        tempBin = tempBin.substring(tempBinLen - 6);
      }
      else {
        //msb = Character.getNumericValue(tempBin.charAt(0));
        addBits = 6 - tempBinLen;
        addBitsStr = new String(new char[addBits]).replace('\0', '0');
        tempBin = addBitsStr + tempBin;
      }
      //System.out.println("R funct after clip: " + tempBin);
      binaryString = binaryString + " " + tempBin;
    }
    else if (inst instanceof Immediate) {
      // opcode
      tempBin = Integer.toBinaryString(((Immediate)inst).getOpcode());
      //System.out.println("I opcode before clip: " + tempBin);
      tempBinLen = tempBin.length();
      if (tempBinLen >= 6) {
        tempBin = tempBin.substring(tempBinLen - 6);
      }
      else {
        addBits = 6 - tempBinLen;
        addBitsStr = new String(new char[addBits]).replace('\0', '0');
        tempBin = addBitsStr + tempBin;
      }
      //System.out.println("I opcode after clip: " + tempBin);
      binaryString = binaryString + " " + tempBin;
      // rs
      tempBin = Integer.toBinaryString(((Immediate)inst).getRS());
      //System.out.println("I rs before clip: " + tempBin);
      tempBinLen = tempBin.length();
      if (tempBinLen >= 5) {
        tempBin = tempBin.substring(tempBinLen - 5);
      }
      else {
        addBits = 5 - tempBinLen;
        addBitsStr = new String(new char[addBits]).replace('\0', '0');
        tempBin = addBitsStr + tempBin;
      }
      //System.out.println("I rs after clip: " + tempBin);
      binaryString = binaryString + " " + tempBin;
      // rt
      tempBin = Integer.toBinaryString(((Immediate)inst).getRT());
      //System.out.println("I rt before clip: " + tempBin);
      tempBinLen = tempBin.length();
      if (tempBinLen >= 5) {
        tempBin = tempBin.substring(tempBinLen - 5);
      }
      else {
        addBits = 5 - tempBinLen;
        addBitsStr = new String(new char[addBits]).replace('\0', '0');
        tempBin = addBitsStr + tempBin;
      }
      //System.out.println("I rt after clip: " + tempBin);
      binaryString = binaryString + " " + tempBin;
      // imm
      tempBin = Integer.toBinaryString(((Immediate)inst).getIMM());
      //System.out.println("I imm before clip: " + tempBin);
      tempBinLen = tempBin.length();
      if (tempBinLen >= 16) {
        tempBin = tempBin.substring(tempBinLen - 16);
      }
      else {
        msb = tempBin.charAt(0);
        addBits = 16 - tempBinLen;
        addBitsStr = new String(new char[addBits]).replace('\0', '0');
        tempBin = addBitsStr + tempBin;
      }
      //System.out.println("I imm after clip: " + tempBin);
      binaryString = binaryString + " " + tempBin;
    }
    else if (inst instanceof Jump) {
      // opcode
      tempBin = Integer.toBinaryString(((Jump)inst).getOpcode());
      //System.out.println("J opcode before clip: " + tempBin);
      tempBinLen = tempBin.length();
      if (tempBinLen >= 6) {
        tempBin = tempBin.substring(tempBinLen - 6);
      }
      else {
        msb = tempBin.charAt(0);
        addBits = 6 - tempBinLen;
        addBitsStr = new String(new char[addBits]).replace('\0', '0');
        tempBin = addBitsStr + tempBin;
      }
      //System.out.println("J opcode after clip: " + tempBin);
      binaryString = binaryString + " " + tempBin;
      // address
      tempBin = Integer.toBinaryString(((Jump)inst).getAddress());
      //System.out.println("J address before clip: " + tempBin);
      tempBinLen = tempBin.length();
      if (tempBinLen > 26) {
        tempBin = tempBin.substring(tempBinLen - 26);
      }
      else {
        msb = tempBin.charAt(0);
        addBits = 26 - tempBinLen;
        addBitsStr = new String(new char[addBits]).replace('\0', '0');
        tempBin = addBitsStr + tempBin;
      }
      //System.out.println("j address after clip: " + tempBin);
      binaryString = binaryString + " " + tempBin;
    }
    else {
      // error
      System.out.println("52 Syntax error on line: " + inst.getPC());
      System.exit(0);
    }
    return binaryString;
  }
}

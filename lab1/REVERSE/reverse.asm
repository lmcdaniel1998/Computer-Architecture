#Lab 1
#
# Luke McDaniel
#
# CPE 315-03

# reverse program - reverses a positive 32 bit number

# java code

#import java.util.Scanner;
#
#public class reverse
#{
#
#  public static void main(String[] args)
#  {
#
#    int rev = 0;
#    int n, i;
#    int num_bits = 0;
#
#    Scanner myScan = new Scanner(System.in);
#
#    System.out.println("Enter number to reverse: ");
#    n = myScan.nextInt();
#
#    for(i = 0; i < num_bits; i++) {
#      if((n & (1 << i)) >= 1) {
#        rev = rev | ( 1 << ((num_bits - 1) - i));
#      }
#    }
#    System.out.println("Reverse " + rev);    
#  }
#}

#declare global variables
.globl numGet
.globl resPut

# Data Area
.data


# Text Area
numGet:
  .asciiz "Enter a positive number to reverse: "

resPut:
  .asciiz "Reverse: "
.text

main:

  # load 4 into $v0 to display first text
  ori $v0, $0, 4

  # get starting address of first message
  lui $a0, 0x1001
  syscall

  # read number in from user
  ori $v0, $0, 5
  syscall

  # store number entered in $a1
  addi $a1, $v0, 0

  # rev will be register $a2
  addi $a2, $0, 0

  # counter will be in register $t6
  addi $t6, $0, 0

  # 32 will be in register $t7
  addi $t7, $0, 32

  # 1 will be in register $t5
  addi $t5, $0, 1

loop:

  # shift 1 to the left counter bits
  sllv $t0, $t5, $t6

  # and number with single bit
  and $t1, $a1, $t0

  # if less than 1 dont do bit work
  # t2 = 1 if less than 1
  sltiu $t2, $t1, 1
  beq $t2, $t5, notSet

  # subtract 1 from num_bits
  addi $t3, $t7, -1
  # subtract counter from $t3
  sub $t3, $t3, $t6

  # shift 1 left $t3 places
  sllv $t4, $t5, $t3

  # or $t4 with rev and put in rev
  or $a2, $a2, $t4

notSet:

  # increment counter
  addi $t6, $t6, 1

  # if counter != 32 stay in loop
  bne $t6, $t7, loop

  # print out second prompt
  ori $v0, $0, 4
  lui $a0, 0x1001 
  ori $a0, $a0, 0x25
  syscall

  #print out reversed inupt
  ori $v0, $0, 1
  addi $a0, $a2, 0
  syscall

  # Exit
  ori $v0, $0, 10
  syscall

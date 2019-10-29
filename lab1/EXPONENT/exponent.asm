#Lab 1
#
# Luke McDaniel
#
# CPE 315-03

# exponent program - raises the first number entered to the power
# of the second number entered. Only works on positive numbers.

# java code

#import java.util.Scanner;
#
#public class exponent
#{
#
#  public static int mult(int x, int y) {
#    int mytemp = 0;
#    int i;
#
#    for(i = 0; i < y; i++) {
#      mytemp += x;
#    }
#    return mytemp;
#  }
#
#  public static void main(String[] args)
#  {
#    int x;
#    int y;
#    int temp = 1;
#
#    Scanner myScan = new Scanner(System.in);
#
#   System.out.println("Enter base: ");
#    x = myScan.nextInt();
#
#    System.out.println("Enter power: ");
#    y = myScan.nextInt();
#
#    while(y != 0) {
#      temp = mult(temp, x);
#      y = y - 1;
#    }   
#    System.out.println("Answer: " + temp);
#  }
#}

#declare global variables
.globl baseGet
.globl powGet
.globl resPut

# Data Area
.data


# Text Area
baseGet:
  .asciiz "Enter Base: "

powGet:
  .asciiz "Enter Power: "

resPut:
  .asciiz "Result: "
.text

main:

  # load 4 into $v0 to display first text
  ori $v0, $0, 4

  #get starting address of first message
  lui $a0, 0x1001
  syscall

  # read in base
  ori $v0, $0, 5
  syscall

  # store base in $a1
  addi $a1, $v0, 0

  # display second prompt
  ori $v0, $0, 4
  lui $a0, 0x1001
  # first message is 13 char long so 0xd
  ori $a0, $a0, 0xd
  syscall

  # read power from user
  ori $v0, $0, 5
  syscall

  # store power in $a2
  addi $a2, $v0, 0

  # result in in $t0
  addi $t0, $0, 1
  
loop:

  # make sure $a1 is base and #a3 is result
  addi $a3, $t0, 0

  # result = base * result
  jal Mult
  # set $t0 to result of mult
  addi $t0, $v0, 0

  # subtract 1 from power
  addi $a2, $a2, -1

  # stay in loop if power isn't 0
  bne $a2, $0, loop

  # put out third message
  ori $v0, $0, 4
  lui $a0, 0x1001
  # third message is 0x1b away from start
  ori $a0, 0x1b
  syscall

  # output result
  ori $v0, $0, 1
  addi $a0, $t0, 0
  syscall

  # Exit
  ori $v0, $0, 10
  syscall


# This function is used to multiply two integers
# $a1 has first num and $a3 has second num
Mult:

  # save all registers
  addi $sp, $sp, -12
  sw $a1, 0($sp)
  sw $a3, 4($sp)
  sw $ra, 8($sp)

  # use $t5 as a counter
  addi $t5, $0, 0  
  # use $t6 as answer
  addi $t6, $0, 0

loop2:

  # add first to $v0 second times
  add $t6, $t6, $a1

  # add 1 to #t5
  addi $t5, $t5, 1

  # break when counter = second
  bne $t5, $a3, loop2

  # restore all registers
  lw $ra, 8($sp)
  lw $a3, 4($sp)
  lw $a1, 0($sp)
  addi $sp, $sp, 12
  
  # put result in $v0
  addi $v0, $t6, 0

  # return to main
  jr $ra

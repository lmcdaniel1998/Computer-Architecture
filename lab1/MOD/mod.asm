# Lab 1
#
# Luke McDaniel
#
# CPE 315-03

# mod program - performs modulus operator on two positive
# 32 bit integers.

# java code

#import java.util.Scanner;
#
#public class mod
#{
#
#  public static void main(String[] args)
#  {
#    int num;
#    int div;
#    int res;
#
#    Scanner myScan = new Scanner(System.in);
#
#    System.out.println("Enter an integer: ");
#    num = myScan.nextInt();
#
#    System.out.println("Enter divisor that is a power of two: ");
#    div = myScan.nextInt();
#
#    res = num & (div - 1);
#
#    System.out.println("Remainder " + res);    
#  }
#}

#declare global variables
.globl numGet
.globl divGet
.globl resPut

# Data Area
.data


# Text Area
numGet:
  .asciiz "Enter an integer: "

divGet:
  .asciiz "Enter divisor that is a power of two: "

resPut:
  .asciiz "Remainder "
.text

main:

  # load 4 int $v0 to display first text
  ori $v0, $0, 4

  # get starting address of first message
  lui $a0, 0x1001
  syscall

  # read first integer (num) from user
  ori $v0, $0, 5
  syscall

  # store first integer in $a1
  addi $a1, $v0, 0

  # display second prompt
  ori $v0, $0, 4
  lui $a0, 0x1001
  # first message is 19 chars long so or an additonal 0x12
  ori $a0, $a0, 0x13
  syscall

  # read second integer from user
  ori $v0, $0, 5
  syscall

  # store second integer (div) from user in $a2
  addi $a2, $v0, 0

  # compute remainder with following formula res = num & (div - 1)
  # div - 1
  addi $t0, $a2, -1
  # num & (div - 1)
  and $t0, $t0, $a1
  # result will be in register $t0

  # put out third message
  ori $v0, $0, 4
  lui $a0, 0x1001
  # third message is 58 characters away from start of text
  ori $a0, 0x3a
  syscall

  # output result
  ori $v0, $0, 1
  addi $a0, $t0, 0
  syscall 

  # Exit
  ori $v0, $0, 10
  syscall 

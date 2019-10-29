#Lab 1
#
# Luke McDaniel
#
# CPE 315-03

# divide program - program divides a 64 bit number ( two ints ) by
# a 32 bit number and outputs a 64 bit number. Divisor must be a 
# power of two.

# java code

#import java.util.Scanner;
#
#public class divide
#{
#
#  public static void main(String[] args)
#  {
#
#    int high, low, div;
#    int highOne = (1 << 31);
#    int lowOne = 1;
#
#    Scanner myScan = new Scanner(System.in);
#
#    System.out.println("Enter upper 32 bits: ");
#    high = myScan.nextInt();
#
#    System.out.println("Enter lower 32 bits: ");
#    low = myScan.nextInt();
#
#    System.out.println("Enter divisor: ");
#    div = myScan.nextInt();
#
#    while (div > 1) {
#      low = low >> 1;
#      if((lowOne & high) != 0) {
#        low = low | highOne;
#      }
#      high = high >> 1;
#      div = div >> 1;
#    }
#    System.out.println("Quotient High: " + high);
#    System.out.println("Quotient Low: " + low);
#  }
#}

#declare global variables
.globl upperGet
.globl lowerGet
.globl divGet
.globl resUpper
.globl resLower

# Data Area
.data


# Text Area
upperGet:
  .asciiz "Enter upper 32 bits of numerator: "

lowerGet:
  .asciiz "Enter lower 32 bits of numerator: "

divGet:
  .asciiz "Enter denominator: "

resUpper:
  .asciiz "Quotient Upper 32: "

resLower:
  .asciiz "     Quotient Lower 32: "
.text

main:

  # load 4 int $v0 to display first text
  ori $v0, $0, 4

  # get starting address of first message
  lui $a0, 0x1001
  syscall

  # read high from user
  ori $v0, $0, 5
  syscall

  # store high in $a1
  addi $a1, $v0, 0

  # display second prompt
  ori $v0, $0, 4
  lui $a0, 0x1001
  # first message is 35 chars long so or an additonal 0x23
  ori $a0, $a0, 0x23
  syscall

  # read low from user
  ori $v0, $0, 5
  syscall

  # store low in $a2
  addi $a2, $v0, 0

  # display third prompt
  ori $v0, $0, 4
  lui $a0, 0x1001
  ori $a0, $a0, 0x46
  syscall

  # read div from user
  ori $v0, $0, 5
  syscall

  # store div in $a3
  addi $a3, $v0, 0

  # create highOne $t1
  addi $t1, $0, 1
  sll $t1, $t1, 31
  # create lowOne $t0
  addi $t0, $0, 1

loop:

  # shift low to the right ( div by 2)
  srl $a2, $a2, 1

  # stay in loop while div > 1

  # check if LSB of high is 1
  and $t2, $a1, $t0
  # if $t2 == 0 then LSB is zero sip next line
  beq $t2, $0, next

  # if not equal to zero
  # or low with high one to carry bit
  or $a2, $a2, $t1

next:

  # shift high right by 1
  srl $a1, $a1, 1
  # shift div right by 1
  srl $a3, $a3, 1

  # if div < 1 then $t6 = 1
  addi $t5, $0, 2
  sltu $t6, $a3, $t5
  beq $t6, $0, loop

  # first output message
  ori $v0, $0, 4
  lui $a0, 0x1001
  ori $a0, 0x5a
  syscall

  # output result
  ori $v0, $0, 1
  addi $a0, $a1, 0
  syscall

  # second output message
  ori $v0, $0, 4
  lui $a0, 0x1001
  ori $a0, $a0, 0x6e
  syscall

  # output result
  ori $v0, $0, 1
  addi $a0, $a2, 0
  syscall

  # Exit
  ori $v0, $0, 10
  syscall 

import java.util.Scanner;

public class reverse
{

  public static void main(String[] args)
  {

    int rev = 0;
    int n, i;
    int num_bits = 32;


    Scanner myScan = new Scanner(System.in);

    System.out.println("Enter number to reverse: ");
    n = myScan.nextInt();

    for(i = 0; i < num_bits; i++) {
      if((n & (1 << i)) >= 1) {
        rev = rev | ( 1 << ((num_bits - 1) - i));
      }
    }

    System.out.println("Reverse " + rev);    
  }
}

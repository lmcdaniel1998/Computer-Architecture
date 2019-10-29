import java.util.Scanner;

public class divide
{


  public static void main(String[] args)
  {

    int high, low, div;
    int highOne = (1 << 31);
    System.out.println("high one: " + highOne);
    int lowOne = 1;

    Scanner myScan = new Scanner(System.in);

    System.out.println("Enter upper 32 bits: ");
    high = myScan.nextInt();

    System.out.println("Enter lower 32 bits: ");
    low = myScan.nextInt();

    System.out.println("Enter divisor: ");
    div = myScan.nextInt();

    while (div > 1) {
      low = low >> 1;
      System.out.println("low after shift: " + low);
      if((lowOne & high) != 0) {
        low = low | highOne;
        System.out.println("low after carry: " + low);
      }
      high = high >> 1;
      System.out.println("high after shift: " + high);
      div = div >> 1;
      System.out.println("div after shift: " + div);
    }
    System.out.println("Quotient High: " + high);
    System.out.println("Quotient Low: " + low);
  }
}

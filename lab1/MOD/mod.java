import java.util.Scanner;

public class mod
{

  public static void main(String[] args)
  {
    int num;
    int div;
    int res;

    Scanner myScan = new Scanner(System.in);

    System.out.println("Enter number: ");
    num = myScan.nextInt();

    System.out.println("Enter divisor that is a power of two: ");
    div = myScan.nextInt();

    res = num & (div - 1);

    System.out.println("Remainder " + res);    
  }
}

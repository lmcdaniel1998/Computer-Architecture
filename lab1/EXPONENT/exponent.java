import java.util.Scanner;

public class exponent
{

  public static int mult(int x, int y) {
    int mytemp = 0;
    int i;

    for(i = 0; i < y; i++) {
      mytemp += x;
    }
    return mytemp;
  }

  public static void main(String[] args)
  {
    int x;
    int y;
    int temp = 1;
    int i;

    Scanner myScan = new Scanner(System.in);

    System.out.println("Enter base: ");
    x = myScan.nextInt();

    System.out.println("Enter power: ");
    y = myScan.nextInt();

    while(y != 0) {
      temp = mult(temp, x);
      System.out.println("temp: " + temp);
      y = y - 1;
    }   
    System.out.println("Answer: " + temp);
  }
}

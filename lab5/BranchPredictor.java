import java.util.*;

public class BranchPredictor
{
  int correct;
  int predictions;
  int GHR[];
  int predictorTable[];

  public BranchPredictor(int correct, int predictions, int GHR[], int predictorTable[]) {
    this.correct = correct;
    this.predictions = predictions;
    this.GHR = GHR;
    this.predictorTable = predictorTable;
  }

  int getCorrect() { return correct; }
  void incrementCorrect() { correct = correct + 1; }

  int getPredict() { return predictions; }
  void incrementPredict() { predictions = predictions + 1; }

  int[] getGHR() { return GHR; }

  void setGHR(int[] newGHR) { GHR = newGHR; }

  void shiftGHR(boolean result) {
    int ghrSize = GHR.length;
    for (int i = 1; i < ghrSize; i++) {
      GHR[i - 1] = GHR[i];
    }
    if (result == true) {
      GHR[ghrSize - 1] = 1;
    }
    else {
      GHR[ghrSize - 1] = 0;
    }
    //for (int c = 0; c < ghrSize; c++) {
      //System.out.print(GHR[c]);
    //}
  }

  int numGHR() {
    int ghrSize = GHR.length;
    StringBuilder singleGHR = new StringBuilder();
    for (int i = 0; i < ghrSize; i++) {
      singleGHR.append(Integer.toString(GHR[i]));
    }
    String finishedStr = singleGHR.toString();
    return Integer.parseInt(finishedStr, 2);
  }

  int getPrediction(int inst) { return predictorTable[inst]; }

  boolean successfulPrediction(int inst, boolean result) {
    boolean retVal = false;
    if (result == true) {
      // branch taken prediction taken
      if (predictorTable[inst] == 2 || predictorTable[inst] == 3) {
        retVal = true;
      }
      // branch taken prediction not taken
      else if (predictorTable[inst] == 0 || predictorTable[inst] == 1) {
        retVal = false;
      }
      else {
        System.out.println("error predictor table broken");
        System.exit(1);
      }
    }
    else {
      // branch not taken prediction taken
      if (predictorTable[inst] == 2 || predictorTable[inst] == 3) {
        retVal = false;
      }
      // branch not taken prediction not taken
      else if (predictorTable[inst] == 0 || predictorTable[inst] == 1) {
        retVal = true;
      }
      else {
        System.out.println("error predictor table broken");
        System.exit(1);
      }
    }
    return retVal;
  }

  void updatePrediction(int inst, boolean result) {
    // strongly not taken
    if (predictorTable[inst] == 0) {
      if (result == true) {
        predictorTable[inst] = 1;
      }
    }
    // weakly not taken
    else if (predictorTable[inst] == 1) {
      if (result == true) {
        predictorTable[inst] = 2;
      }
      else {
        predictorTable[inst] = 0;
      }
    }
    // weakly taken
    else if (predictorTable[inst] == 2 ) {
      if (result == true) {
        predictorTable[inst] = 3;
      }
      else {
        predictorTable[inst] = 1;
      }
    }
    // strongly taken
    else if (predictorTable[inst] == 3) {
      if (result == false) {
        predictorTable[inst] = 2;
      }
    }
    else {
      System.out.println("error update prediction broken");
      System.exit(1);
    }
  }
}

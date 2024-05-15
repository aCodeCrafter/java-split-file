package Chapter_17;
/* Exercise17_11.java
 * Callahan
 * 04/23/24
 */

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class SplitFile extends Application {
  public static void main(String[] args) {
    launch();
  }

  // Start method to build and launch window in JavaFX
  @Override
  public void start(Stage primaryStage) {
    final int WINDOW_X = 425;
    final int WINDOW_Y = 115;
    
    Text guideText = new Text("If you split a file named temp.txt into 3 "+
      "smaller files, the three smaller files are temp.txt.1, temp.txt.2, and temp.txt.3");
    guideText.setWrappingWidth(WINDOW_X);
    
    TextField filenameInput = new TextField();
    filenameInput.setText("temp.txt");
    Label lb1 = new Label("Enter a File:                                     ", filenameInput);
    lb1.setContentDisplay(ContentDisplay.RIGHT);

    TextField numFileSplitInput = new TextField();
    numFileSplitInput.setText("3");
    Label lb2 = new Label("Specify the number of smaller files:", numFileSplitInput);
    lb2.setContentDisplay(ContentDisplay.RIGHT);
    
    Text statusText = new Text("");

    Button startBtn = new Button("Start");

    VBox vbox = new VBox();
    vbox.setAlignment(Pos.CENTER);
    vbox.getChildren().addAll(guideText,lb1,lb2,startBtn,statusText);
    
    Scene scene = new Scene(vbox,WINDOW_X,WINDOW_Y);
    primaryStage.setTitle("Excercise17_11");
    primaryStage.setScene(scene);
    primaryStage.show();

    // Code for the start button, calls the splitFile method
    startBtn.setOnAction((e) -> {
      try {
        Scanner sc = new Scanner(numFileSplitInput.getText());
        splitFile(filenameInput.getText(), sc.nextInt());
        sc.close();
        statusText.setText("Success!");
      } catch (FileNotFoundException exception) {
        System.out.println("FileNotFoundException");
        statusText.setText("Error: File \""+filenameInput.getText()+"\" not found");
      } catch (IOException exception) {
        System.out.println("IOException");
        statusText.setText("Error: IOException");
      }
    });
  }

  // Method takes file name and number of files, then splits input file into smaller files 
  // (Note, does not delete original file.)
  public static void splitFile(String inputFileName, int numSmallerFiles) throws FileNotFoundException, IOException {
    System.out.println("Split "+inputFileName+" into "+numSmallerFiles+" smaller files");
    DataInputStream inputFile = new DataInputStream(new BufferedInputStream(new FileInputStream(inputFileName)));

    int bytelen = inputFile.available();
    for (int i = 1; i <= numSmallerFiles; i++) {
      // Add bytes that don't fit evenly into split files to last file.
      byte[] tempByteStorage;
      if (i == numSmallerFiles) {
        tempByteStorage = new byte[(int)(bytelen/numSmallerFiles) + (bytelen % numSmallerFiles)];
      } else {
        tempByteStorage = new byte[(int)(bytelen/numSmallerFiles)];
      }
      
      inputFile.read(tempByteStorage,0,tempByteStorage.length);

      DataOutputStream outputFile = new DataOutputStream(
        new BufferedOutputStream(new FileOutputStream(inputFileName+"."+i)));
      outputFile.write(tempByteStorage);
      outputFile.close();
    }
    inputFile.close();
  }
}

import java.util.Random;

import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import javafx.stage.Window;


public class App extends Application {

    @Override 

    public void start(Stage primaryStage) {

        Pane Pane = new Pane();
        Label question = new Label("");
        Label answer = new Label();
        TextField result = new TextField();
        Loader loader = new Loader(result, answer, question);
   
        Scene scene = new Scene(Pane, 600, 600);

        primaryStage.setTitle("Space game!");
        primaryStage.setScene(scene);
        primaryStage.show();
        
        loader.loadText(Pane);
        loader.submitBtn(Pane);
        loader.renewBtn(Pane);
        loader.exitBtn(Pane, primaryStage);
  
    }

    public static void main(String[] args){
        launch(args);
    }

}



class Loader {

    private Label question;
    private Label answer;
    private TextField result;

    int sum1;
    int sum2 = -1;

    Random rand;
    int number1;
    int number2;

    int counter = 1;

    Loader(TextField result, Label answer, Label question) {
        this.question = question;
        this.answer = answer;
        this.result = result;
    }


    public int loadQuestion(int number1, int number2) {
        question.setText(number1 + " x " + number2);
        return number1 * number2;
    }


    public int loadAnswer(TextField result, Label answer) {
        int res = Integer.parseInt(result.getText());
        answer.setText(result.getText());
        return res;     
    }


    public void loadText(Pane gp) {

        Text questiontxt = new Text("Question: ");
        Text answertxt = new Text("Answer: ");

        gp.getChildren().add(questiontxt);
        gp.getChildren().add(answertxt);
        gp.getChildren().add(question);
        gp.getChildren().add(result);
        gp.getChildren().add(answer);

        questiontxt.setLayoutX(50);
        questiontxt.setLayoutY(200);
        question.setLayoutX(110);
        question.setLayoutY(200);

        answertxt.setLayoutX(50);
        answertxt.setLayoutY(230);
        answer.setLayoutX(110);
        answer.setLayoutY(230);

        result.setLayoutX(50);
        result.setLayoutY(255);

        rand = new Random(); 
        number1 = rand.nextInt(10); 
        number2 = rand.nextInt(10);

        question.setText(number1 + " x " + number2);

        AnswerTextPrompt prompt = new AnswerTextPrompt(
            gp.getScene().getWindow()
        );

        String res = prompt.getResult();

        result.setText(res);
    }



    public void submitBtn(Pane gp) {

        Button submit = new Button("Summit");
        TranslateTransition transition = new TranslateTransition();
        Circle cir = new Circle();

        gp.getChildren().add(submit);
        gp.getChildren().add(cir);

        submit.setLayoutX(260);
        submit.setLayoutY(270);

        cir.setFill(Color.DARKSALMON);
        cir.setRadius(30);
        cir.setLayoutX(500);
        cir.setLayoutY(550);
        
        submit.setDefaultButton(true);
        submit.setOnAction(e -> {

            try {
                sum1 = loadQuestion(number1, number2);
                sum2 = loadAnswer(result, answer);
            } catch(NumberFormatException ex){ 
                ;
            }

            if (counter < 10) { 

                if (sum2 == -1) {
                    Alert alert = new Alert(AlertType.ERROR, "Please enter a number");
                    alert.showAndWait();
                }
                else if (sum1 == sum2) {
                            
                    transition.setAutoReverse(false);
                    transition.setNode(cir);
                    
                    cir.setLayoutX(500);
                    cir.setLayoutY(550 - 50 * counter);

                    transition.play();

                    counter++;
                    
                    answer.setText(number1 + " x " + number2 + " = " + result.getText() + " correct");
                }
                else {
                    counter++;answer.setText(number1 + " x " + number2 + " = " + result.getText() + " incorrect");
                } 
                
            } else {
                    transition.setAutoReverse(false);
                    transition.setNode(cir);
                    transition.play();
                    cir.setLayoutY(50);
            }
        });
    }

    public void renewBtn(Pane gp) {

        Button continuebtn = new Button("Continue");
        
        continuebtn.setOnAction(e -> {
            number1 = rand.nextInt(10); 
            number2 = rand.nextInt(10);

            question.setText(number1 + " x " + number2);
            result.clear();
            answer.setText("");
            sum1 = 0;
            sum2 = 0;
        });

        gp.getChildren().add(continuebtn);
        continuebtn.setLayoutX(260);
        continuebtn.setLayoutY(300);
        
    }


    public void exitBtn(Pane gp, Stage stage) {

        Button btnExit = new Button("Exit");
        btnExit.setOnAction(e -> {
            stage.close();
        });

        gp.getChildren().add(btnExit);
        btnExit.setLayoutX(260);
        btnExit.setLayoutY(330);
    }

    

    class AnswerTextPrompt {
        private final String result;

        AnswerTextPrompt(Window owner) {
            final Stage dialog = new Stage();

            dialog.setTitle("Enter your answer");
            dialog.initOwner(owner);
            dialog.initStyle(StageStyle.UTILITY);
            dialog.initModality(Modality.WINDOW_MODAL);
            dialog.setX(owner.getX() + owner.getWidth());
            dialog.setY(owner.getY());

            final TextField textField = new TextField();
            final Button submitButton = new Button("Submit");
            submitButton.setDefaultButton(true);
            submitButton.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent t) {
                    dialog.close();
                }
            });

            textField.setMinHeight(TextField.USE_PREF_SIZE);

            final VBox layout = new VBox(10);
            layout.setAlignment(Pos.CENTER_RIGHT);
            layout.setStyle("-fx-background-color: azure; -fx-padding: 10;");
            layout.getChildren().setAll(
                textField, 
                submitButton
            );

            dialog.setScene(new Scene(layout));
            dialog.showAndWait();

            result = textField.getText();
        }

        private String getResult() {
            return result;
        }
    }
	
    
}

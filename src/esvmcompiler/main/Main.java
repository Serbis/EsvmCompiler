package esvmcompiler.main;

import esvmcompiler.lexer.Lexer;
import esvmcompiler.parcer.Parcer;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.FileInputStream;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        /*Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();*/

        System.setIn(new FileInputStream("/home/serbis/Projects/Outline/EsvmCompiler/typer"));
        Lexer lex = new Lexer();
        Parcer parce = new Parcer(lex);
        parce.program();
        System.out.write('\n');
    }


    public static void main(String[] args) {
        launch(args);
    }
}

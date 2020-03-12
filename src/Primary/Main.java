package Primary;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Main extends Application {

    /**
     * This is the primary controller for the program. This will create the intersection, create the Graphics, and give the
     * intersection to the TestTCS program for them to run. This will also call run() on the TestTCS code.
     */

    @Override
    public void start(Stage primaryStage) {


        // Setup border pane with HBox of two buttons along the top and
        // canvas to display the simulation in the center, also initialize
        // the controller with something to draw on
        BorderPane root = new BorderPane();
        Canvas canvas = new Canvas(550, 550);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        VBox controlBox = new VBox(15);
        HBox speedBox = new HBox(10);
        VBox pedSpeedBox = new VBox(10);
        VBox carSpeedBox = new VBox(10);

        controlBox.setPadding(new Insets(0,0,0,5));
        speedBox.setPadding(new Insets(0,0,20,0));
        controlBox.setPrefSize(150, 550);
        controlBox.setStyle("-fx-background-color: #e0e0e0;");

        Label resultLabel = new Label("");
        resultLabel.setPrefSize(150, 50);

        Label controlLabel = new Label("Modes:\n\n");
        controlLabel.setPrefSize(150, 100);

        Button rushButton = new Button("Rush Hour Traffic");
        Button heavyButton = new Button("Heavy Traffic");
        Button moderateButton = new Button("Moderate Traffic");
        Button lightButton = new Button("Light Traffic");
        Button spawnCarButton = new Button("Spawn Car");
        Button spawnEmergencyButton = new Button("Spawn Emergency");
        Button spawnPedButton = new Button("Spawn Pedestrian");
        Button resetButton = new Button("Reset");

        Label pedLabel = new Label("Ped Speed");
        pedLabel.setFont(new Font("Serif", 12));
        Label pedSpeedVal = new Label("1");
        pedSpeedVal.setFont(new Font("Serif", 12));
        Button walkFaster = new Button("\u2191");
        Button walkSlower = new Button("\u2193");

        Label carLabel = new Label("Car Speed");
        carLabel.setFont(new Font("Serif", 12));
        Label carSpeedVal = new Label("1");
        carSpeedVal.setFont(new Font("Serif", 12));
        Button driveFaster = new Button("\u2191");
        Button driveSlower = new Button("\u2193");




        rushButton.setStyle("-fx-background-color: #1f3d7a;-fx-text-fill: white; -fx-font: 14px Calibri;");
        heavyButton.setStyle("-fx-background-color: #1f3d7a;-fx-text-fill: white; -fx-font: 14px Calibri;");
        moderateButton.setStyle("-fx-background-color: #1f3d7a;-fx-text-fill: white; -fx-font: 14px Calibri;");
        lightButton.setStyle("-fx-background-color: #1f3d7a;-fx-text-fill: white; -fx-font: 14px Calibri;");

        spawnCarButton.setStyle("-fx-background-color: #ffffff;-fx-text-fill: #1f3d7a; -fx-border-radius: 2; -fx-border-width: 1; -fx-border-color: #1f3d7a; -fx-font: 13px Calibri;");
        spawnEmergencyButton.setStyle("-fx-background-color: #ffffff;-fx-text-fill: #1f3d7a; -fx-border-radius: 2; -fx-border-width: 1; -fx-border-color: #1f3d7a; -fx-font: 13px Calibri;");
        spawnPedButton.setStyle("-fx-background-color: #ffffff;-fx-text-fill: #1f3d7a; -fx-border-radius: 2; -fx-border-width: 1; -fx-border-color: #1f3d7a; -fx-font: 13px Calibri;");

        resetButton.setStyle("-fx-background-color: #4775d1;-fx-text-fill: white; -fx-font: 14px Calibri;");
        walkFaster.setStyle("-fx-background-color: #4775d1;-fx-text-fill: white; -fx-font: 14px Calibri;");
        walkSlower.setStyle("-fx-background-color: #4775d1;-fx-text-fill: white; -fx-font: 14px Calibri;");
        driveFaster.setStyle("-fx-background-color: #4775d1;-fx-text-fill: white; -fx-font: 14px Calibri;");
        driveSlower.setStyle("-fx-background-color: #4775d1;-fx-text-fill: white; -fx-font: 14px Calibri;");


        rushButton.setPrefSize(140, 30);
        heavyButton.setPrefSize(140, 30);
        moderateButton.setPrefSize(140, 30);
        lightButton.setPrefSize(140, 30);
        spawnCarButton.setPrefSize(140, 30);
        spawnEmergencyButton.setPrefSize(140, 30);
        spawnPedButton.setPrefSize(140, 30);
        resetButton.setPrefSize(140, 30);

        Controller controller = new Controller(gc);
        controller.start();

        // Handle button press actions
        rushButton.setOnMousePressed(e -> {
            controller.rushMode(controlLabel);
            DayNight.DAY.setDay(true);
        });
        heavyButton.setOnMousePressed(e -> {
            controller.heavyMode(controlLabel);
            DayNight.DAY.setDay(true);
        });
        moderateButton.setOnMousePressed(e -> {
            controller.moderateMode(controlLabel);
            DayNight.DAY.setDay(true);
        });
        lightButton.setOnMousePressed(e -> {
            controller.lightMode(controlLabel);
            DayNight.DAY.setDay(false);
        });
        spawnCarButton.setOnMousePressed(e -> controller.spawnCar());
        spawnEmergencyButton.setOnMousePressed(e -> controller.spawnEmergency());
        spawnPedButton.setOnMousePressed(e -> controller.spawnPed());
        walkFaster.setOnMousePressed(e -> controller.walkFaster(true, pedSpeedVal));
        walkSlower.setOnMousePressed(e -> controller.walkFaster(false, pedSpeedVal));
        driveFaster.setOnMousePressed(e -> controller.driveFaster(true, carSpeedVal));
        driveSlower.setOnMousePressed(e -> controller.driveFaster(false, carSpeedVal));
        resetButton.setOnMousePressed(e -> {
            controller.reset();
            carSpeedVal.setText("1");
            pedSpeedVal.setText("1");
        });


        // Setup the scene
        carSpeedBox.getChildren().addAll(carLabel, carSpeedVal, driveFaster, driveSlower);
        pedSpeedBox.getChildren().addAll(pedLabel, pedSpeedVal, walkFaster, walkSlower);
        speedBox.getChildren().addAll(pedSpeedBox, carSpeedBox);
        controlBox.getChildren().addAll(controlLabel, rushButton, heavyButton, moderateButton, lightButton, spawnCarButton, spawnEmergencyButton, spawnPedButton, resetButton, resultLabel, speedBox);
        root.setRight(controlBox);
        root.setLeft(canvas);

        Scene scene = new Scene(root, 700, 550);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Traffic Control System: Testbed");
        primaryStage.show();
        primaryStage.setOnCloseRequest(event -> {
            System.exit(0);
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}

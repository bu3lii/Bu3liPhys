package physicsengine2d;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button; // For buttons
import javafx.scene.control.CheckBox; // For toggles
import javafx.scene.control.Label; // For text display
import javafx.scene.control.Slider; // For sliders
import javafx.scene.input.KeyCode; // For key events
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox; // For vertical layout
import javafx.scene.paint.Color;
import javafx.scene.text.Font; // For font styling
import javafx.stage.Stage;

import java.util.List;

public class HelloApplication extends Application {

    private static final int WIDTH = 900;
    private static final int HEIGHT = 600;
    private static final int BALL_RADIUS = 10;
    private static final double FIXED_TIMESTEP = 0.016; // ~60FPS

    private PhysicsObject selectedObject = null;
    private Vector2D dragStart = null;
    private Vector2D dragEnd = null;
    private boolean isDragging = false;

    private boolean isPaused = false;
    private double simSpeedMultiplier = 1.0;
    private boolean showTrails = true;
    private boolean showAxes = true;
    private boolean showVelocityVectors = true;

    private Label objectCountLabel;


    private World world;
    private Canvas canvas;

    @Override
    public void start(Stage stage) {
        canvas = new Canvas(WIDTH, HEIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        world = new World(WIDTH, HEIGHT);

        // UI control area (QoL stuff)
        VBox control = new VBox(10);
        control.setTranslateX(10);
        control.setTranslateY(10);
        control.setStyle("-fx-padding: 10; -fx-background-color: rgba(0, 0, 0, 0.5); -fx-background-radius: 5;");

        // pause and resume buttons
        Button pauseButton = new Button("Pause");
        pauseButton.setOnAction(e -> {
            isPaused = !isPaused;
            if(isPaused){
                pauseButton.setText("Resume");
            }
            else {
                pauseButton.setText("Pause");
            }
        });

        // reset sim button
        Button resetButton = new Button("Reset Simulation");
        resetButton.setOnAction(e -> {
            world.clearObjects(); // remove all physics objects
            world.setGravity(new Vector2D(0, 9.8)); // reset gravity to normal
            // reset all selection dragging parameters
           selectedObject = null;
           dragStart = null;
           dragEnd = null;
           isDragging = false;
            // also reset pause state
            isPaused = false;
        });

        // gravity x slider
        Label gravityXLabel = new Label("Gravity X: 0.0");
        gravityXLabel.setTextFill(Color.WHITE);
        Slider gravityXSlider = new Slider(-50,50,world.getGravity().x);
        gravityXSlider.setShowTickLabels(true);
        gravityXSlider.setShowTickMarks(true);
        gravityXSlider.setMajorTickUnit(25);
        gravityXSlider.setMinorTickCount(5);
        gravityXSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            world.setGravity(new Vector2D(newVal.doubleValue(), world.getGravity().y));
            gravityXLabel.setText("Gravity X: " + world.getGravity().x);
        });

        // gravity y slider
        Label gravityYLabel = new Label("Gravity X: 9.8");
        gravityYLabel.setTextFill(Color.WHITE);
        Slider gravityYSlider = new Slider(-50,50,world.getGravity().x);
        gravityYSlider.setShowTickLabels(true);
        gravityYSlider.setShowTickMarks(true);
        gravityYSlider.setMajorTickUnit(25);
        gravityYSlider.setMinorTickCount(5);
        gravityYSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            world.setGravity(new Vector2D(world.getGravity().x,newVal.doubleValue()));
            gravityXLabel.setText("Gravity Y: " + world.getGravity().y);
        });

        // simulation speed slider
        Label speedLabel = new Label("Simulation Speed: 1.0x");
        speedLabel.setTextFill(Color.WHITE);
        Slider speedSlider = new Slider(0.1,5.0,simSpeedMultiplier); // sim speed from 0.1x to 5x
        speedSlider.setShowTickLabels(true);
        speedSlider.setShowTickMarks(true);
        speedSlider.setMajorTickUnit(1.0);
        speedSlider.setMinorTickCount(4);
        speedSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            simSpeedMultiplier = newVal.doubleValue();
            speedLabel.setText("Simulation Speed: " + simSpeedMultiplier + "x");
        });

        // visualizatiion toggles and checkboxes
        CheckBox showTrailsCheckbox = new CheckBox("Show Trails");
        showTrailsCheckbox.setSelected(showTrails);
        showTrailsCheckbox.setTextFill(Color.WHITE);
        showTrailsCheckbox.setOnAction(e -> {showTrails = showTrailsCheckbox.isSelected();});

        CheckBox showAxesCheckbox = new CheckBox("Show Axes");
        showAxesCheckbox.setSelected(showAxes);
        showAxesCheckbox.setTextFill(Color.WHITE);
        showAxesCheckbox.setOnAction(e -> {showAxes = showAxesCheckbox.isSelected();});

         CheckBox showVelCheckbox = new CheckBox("Show Velocity Vectors");
         showVelCheckbox.setSelected(showVelocityVectors);
         showVelCheckbox.setTextFill(Color.WHITE);
         showVelCheckbox.setOnAction(e -> {showVelocityVectors = showVelCheckbox.isSelected();});

         // object count label
        objectCountLabel = new Label("Objects Count:");
        objectCountLabel.setTextFill(Color.WHITE);
        objectCountLabel.setFont(new Font("Arial",14));

        // adding all the controls to the vbox
        control.getChildren().addAll(
                new Label("Simulation Controls"),
                pauseButton,
                resetButton,
                gravityXLabel,
                gravityXSlider,
                gravityYLabel,
                gravityYSlider,
                speedLabel,
                speedSlider,
                showTrailsCheckbox,
                showAxesCheckbox,
                showVelCheckbox,
                objectCountLabel
        );

        Group root = new Group(canvas,control);
        Scene scene = new Scene(root, WIDTH, HEIGHT, Color.BLACK);
        stage.setTitle("Bu3li's Physics World");
        stage.setScene(scene);
        stage.show();

        // space to pause
        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.SPACE) {
                isPaused = !isPaused;
                if(isPaused){
                    pauseButton.setText("Resume");
                }
                else{
                    pauseButton.setText("Pause");
                }
            }
        });

        // Click to spawn
        canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
            Vector2D click = new Vector2D(e.getX(), e.getY());
            selectedObject = null;

            // Try to find a nearby object
            for (PhysicsObject obj : world.getObjects()) {
                Vector2D pos = obj.getPosition();
                double dx = pos.x - click.x;
                double dy = pos.y - click.y;
                if (Math.sqrt(dx * dx + dy * dy) < BALL_RADIUS) {
                    selectedObject = obj;
                    dragStart = click;
                    dragEnd = click;
                    isDragging = true;
                    selectedObject.setFrozen(true);
                    return;
                }
            }

            // If none selected, spawn new object
            PhysicsObject newObj = new PhysicsObject(click, 1.0,10);
            world.addObject(newObj);
        });

        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, e -> {
            if (isDragging) {
                dragEnd = new Vector2D(e.getX(), e.getY());
            }
        });

        canvas.addEventHandler(MouseEvent.MOUSE_RELEASED, e -> {
            if (isDragging && selectedObject != null && dragStart != null && dragEnd != null) {
                // Direction: from dragEnd back to dragStart
                Vector2D force = dragEnd.sub(dragStart).multiplyScalar(75); // scale to make force visible
                selectedObject.applyForce(force);
                selectedObject.setFrozen(false);
            }
            isDragging = false;
            selectedObject = null;
            dragStart = null;
            dragEnd = null;
        });

        new AnimationTimer() {
            long lastTime = 0;
            double accumulator = 0;

            @Override
            public void handle(long now) {
                if (lastTime == 0) {
                    lastTime = now;
                    return;
                }

                double dt = (now - lastTime) / 1_000_000_000.0;
                lastTime = now;

                // Clamp just in case of spikes
                dt = Math.min(dt, 0.05);
                // apply sim speed multiplier
                accumulator += dt*simSpeedMultiplier;

                // Catch up using multiple fixed steps
                if(!isPaused) {
                    while (accumulator >= FIXED_TIMESTEP) {
                        world.step(FIXED_TIMESTEP, selectedObject, isDragging);
                        accumulator -= FIXED_TIMESTEP;
                    }}
                else {
                    accumulator = 0;
                }

                // Render after physics is caught up
                renderFrame();
            }

            private void renderFrame() {
                gc.setFill(Color.BLACK);
                gc.fillRect(0, 0, WIDTH, HEIGHT);

                // Axes
                if(showAxes) {
                    gc.setStroke(Color.DARKGRAY);
                    gc.setLineWidth(1);
                    gc.strokeLine(50, 0, 50, HEIGHT);
                    gc.strokeLine(0, HEIGHT - 50, WIDTH, HEIGHT - 50);
                }

                // Trails
                if(showTrails) {
                    gc.setStroke(Color.DARKCYAN);
                    gc.setLineWidth(1);
                    for (PhysicsObject obj : world.getObjects()) {
                        List<Vector2D> trail = obj.getTrail();
                        for (int i = 1; i < trail.size(); i++) {
                            Vector2D p1 = trail.get(i - 1);
                            Vector2D p2 = trail.get(i);
                            gc.strokeLine(p1.x, p1.y, p2.x, p2.y);
                        }
                    }
                }

                // drawing the actual objects
                gc.setFill(Color.CYAN);
                for (PhysicsObject obj : world.getObjects()) {
                    Vector2D pos = obj.getPosition();
                    gc.fillOval(pos.x - BALL_RADIUS, pos.y - BALL_RADIUS, BALL_RADIUS * 2, BALL_RADIUS * 2);
                }

                // creating the velocity vectors and their arrow
                if (showVelocityVectors) {
                    gc.setStroke(Color.LIMEGREEN);
                    gc.setLineWidth(1.5);
                    for (PhysicsObject obj : world.getObjects()) {
                        Vector2D pos = obj.getPosition();
                        Vector2D vel = obj.getVelocity();
                        Vector2D velDraw = vel.multiplyScalar(5.0);
                        Vector2D velEnd = pos.add(velDraw);

                        gc.strokeLine(pos.x, pos.y, velEnd.x, velEnd.y);

                        double dx = velEnd.x - pos.x;
                        double dy = velEnd.y - pos.y;
                        if (dx != 0 || dy != 0) {
                            double angle = Math.atan2(dy, dx);
                            double len = 7;
                            double angleOffset = Math.toRadians(30);

                            double x1 = velEnd.x - len * Math.cos(angle - angleOffset);
                            double y1 = velEnd.y - len * Math.sin(angle - angleOffset);

                            double x2 = velEnd.x - len * Math.cos(angle + angleOffset);
                            double y2 = velEnd.y - len * Math.sin(angle + angleOffset);

                            gc.strokeLine(velEnd.x, velEnd.y, x1, y1);
                            gc.strokeLine(velEnd.x, velEnd.y, x2, y2);
                        }
                    }
                }

                // creating the arrow line
                if (isDragging && dragStart != null && dragEnd != null) {
                    gc.setStroke(Color.RED);
                    gc.setLineWidth(2);
                    gc.strokeLine(dragStart.x, dragStart.y, dragEnd.x, dragEnd.y);

                    // force label using arbitrary units
                    Vector2D forceVec = dragStart.sub(dragEnd);
                    double forceMag = forceVec.magnitude();
                    String forceText = String.format("Force: %.1f Units", forceMag);

                    gc.setFill(Color.WHITE);
                    gc.fillText(forceText, dragStart.x + 10, dragStart.y - 10);

                    // thank you stack overflow :prayge: why is drawing an arrow this complicated man holy
                    double dx = dragEnd.x - dragStart.x;
                    double dy = dragEnd.y - dragStart.y;
                    double angle = Math.atan2(dy, dx);
                    double len = 10; // arrowhead length
                    double angleOffset = Math.toRadians(25);

                    double x1 = dragEnd.x - len * Math.cos(angle - angleOffset);
                    double y1 = dragEnd.y - len * Math.sin(angle - angleOffset);

                    double x2 = dragEnd.x - len * Math.cos(angle + angleOffset);
                    double y2 = dragEnd.y - len * Math.sin(angle + angleOffset);

                    gc.strokeLine(dragEnd.x, dragEnd.y, x1, y1);
                    gc.strokeLine(dragEnd.x, dragEnd.y, x2, y2);
                }
                objectCountLabel.setText("Objects: " + world.getObjects().size());

            }
        }.start();
    }

    public static void main(String[] args) {
        launch();
    }
}

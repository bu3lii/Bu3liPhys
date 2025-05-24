package pr.phys.physicsengine2d;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.List;

public class HelloApplication extends Application {

    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final int BALL_RADIUS = 10;
    private static final double FIXED_TIMESTEP = 0.016; // ~60FPS

    private PhysicsObject selectedObject = null;
    private Vector2D dragStart = null;
    private Vector2D dragEnd = null;
    private boolean isDragging = false;


    private World world;
    private Canvas canvas;

    @Override
    public void start(Stage stage) {
        canvas = new Canvas(WIDTH, HEIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        Group root = new Group(canvas);
        Scene scene = new Scene(root, WIDTH, HEIGHT, Color.BLACK);
        stage.setTitle("Bu3li's Physics World");
        stage.setScene(scene);
        stage.show();

        world = new World();

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
            PhysicsObject newObj = new PhysicsObject(click, 1.0);
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
                Vector2D force = dragEnd.sub(dragStart).multiplyScalar(50); // scale to make force visible
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
                accumulator += dt;

                // Catch up using multiple fixed steps
                while (accumulator >= FIXED_TIMESTEP) {
                    world.step(FIXED_TIMESTEP,selectedObject,isDragging);
                    accumulator -= FIXED_TIMESTEP;
                }

                // Render after physics is caught up
                renderFrame();
            }

            private void renderFrame() {
                gc.setFill(Color.BLACK);
                gc.fillRect(0, 0, WIDTH, HEIGHT);

                // Axes
                gc.setStroke(Color.DARKGRAY);
                gc.setLineWidth(1);
                gc.strokeLine(50, 0, 50, HEIGHT);
                gc.strokeLine(0, HEIGHT - 50, WIDTH, HEIGHT - 50);

                // Trails
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

                // drawing the actual objects
                gc.setFill(Color.CYAN);
                for (PhysicsObject obj : world.getObjects()) {
                    Vector2D pos = obj.getPosition();
                    gc.fillOval(pos.x - BALL_RADIUS, pos.y - BALL_RADIUS, BALL_RADIUS * 2, BALL_RADIUS * 2);
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

            }
        }.start();
    }

    public static void main(String[] args) {
        launch();
    }
}

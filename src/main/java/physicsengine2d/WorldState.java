package physicsengine2d;

import java.util.List;
import java.util.ArrayList;

public class WorldState {

    public Vector2D gravity;
    public List<PhysicsObject> objects;

    public WorldState(){
        this.objects=new ArrayList<>();
    }

    public WorldState(Vector2D gravity,List<PhysicsObject> objects){
        this.gravity=gravity;
        this.objects=new ArrayList<>(objects);
    }

    public Vector2D getGravity(){
        return gravity;
    }

    public void setGravity(Vector2D gravity){
        this.gravity=gravity;
    }

    public List<PhysicsObject> getObjects() {
        return objects;
    }

    public void setObjects(List<PhysicsObject> objects){
        this.objects=objects;
    }
}

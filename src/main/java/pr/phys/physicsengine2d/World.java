package pr.phys.physicsengine2d;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class World {
private final List<PhysicsObject> objects;
private Vector2D gravity;

public World(){
    objects = new ArrayList<>();
    gravity = new Vector2D(0,9.8);
}

public void addObject(PhysicsObject obj){
    if(obj!=null) {
        objects.add(obj);
    }
}

public void step(double dt, PhysicsObject frozen, boolean isDragging){
    if(dt<0){
        throw new IllegalArgumentException("Time step cannot be less than zero");
    }
    for(PhysicsObject obj : objects){
        if(isDragging && obj==frozen) continue;
        obj.applyForce(gravity.multiplyScalar(obj.getMass()));
        obj.update(dt);
    }
}

public List<PhysicsObject> getObjects(){
    return Collections.unmodifiableList(objects);
}

public void setGravity(Vector2D g){
    if(gravity==null){
        throw new IllegalArgumentException("Gravity Cannot be Null");
    }
    gravity = g;
}

public Vector2D getGravity(){
    return new Vector2D(gravity.x, gravity.y);
}


}

package physicsengine2d;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class World {
private final List<PhysicsObject> objects;
private Vector2D gravity;
private final int width;
private final int height;
private final double restitution = 0.9;

public World(int width, int height){
    objects = new ArrayList<>();
    gravity = new Vector2D(0,9.8);
    this.width = width;
    this.height = height;
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
        handleBorderCollision(obj);
    }

    for(int i = 0; i<objects.size(); i++){
        for(int j = i+1; j<objects.size(); j++){
            resolveCollision(objects.get(i),objects.get(j));
        }
    }
}

public List<PhysicsObject> getObjects(){
    return Collections.unmodifiableList(objects);
}

public void setGravity(Vector2D g){
    if(g==null){
        throw new IllegalArgumentException("Gravity Cannot be Null");
    }
    gravity = g;
}

public Vector2D getGravity(){
    return new Vector2D(gravity.x, gravity.y);
}

public void clearObjects(){
    objects.clear();
}

private void handleBorderCollision(PhysicsObject obj){
    Vector2D vel = obj.getVelocity();
    Vector2D pos = obj.getPosition();
    double r = obj.getRadius();

    if(pos.x -r < 0){
        pos.x = r;
        vel.x = -vel.x * restitution;
    }
    else if(pos.x + r > width){
        pos.x=width-r;
        vel.x = -vel.x * restitution;
    }

    if(pos.y -r < 0){
        pos.y = r;
        vel.y = -vel.y * restitution;
    }
    else if(pos.y + r > height){
        pos.y=height-r;
        vel.y = -vel.y * restitution;
    }

    obj.setPosition(pos);
    obj.setVelocity(vel);
}

private void resolveCollision(PhysicsObject a,PhysicsObject b){
    Vector2D posA = a.getPosition();
    Vector2D posB = b.getPosition();
    Vector2D delta = posB.sub(posA);
    double dist = delta.magnitude();
    double mindist = a.getRadius() + b.getRadius();

    if(dist==0||mindist<dist){
        return;
    }

    Vector2D normal = delta.multiplyScalar(1.0/dist);
    double overlap = mindist-dist;

    double slop = 0.01;
    double percent = 0.8;

    double correctionMag = Math.max(overlap-slop,0.0)/(1/a.getMass()+1/b.getMass()) * percent;
    Vector2D correction = normal.multiplyScalar(correctionMag);

    posA = posA.sub(correction.multiplyScalar(1.0/a.getMass()));
    posB = posB.add(correction.multiplyScalar(1.0/b.getMass()));

    Vector2D velA = a.getVelocity();
    Vector2D velB = b.getVelocity();
    Vector2D relVel =  velB.sub(velA);
    double sepVel = relVel.dot(normal);

    if(sepVel>0){
        a.setPosition(posA);
        b.setPosition(posB);
        return;
    }

    double m1 = a.getMass();
    double m2 = b.getMass();

    double impulseMag =  -(1+restitution)*sepVel/(1/m1 + 1/m2);
    Vector2D impulse = normal.multiplyScalar(impulseMag);

    velA = velA.sub(impulse.multiplyScalar(1/m1));
    velB = velB.add(impulse.multiplyScalar(1/m2));

    Vector2D tangent = relVel.sub(normal.multiplyScalar(sepVel));
    tangent = tangent.normalize();

    double jt = -relVel.dot(tangent)/(1/m1+1/m2);
    double mu = 0.1;

    Vector2D frictionImpulse;
    if(Math.abs(jt) < impulseMag * mu){
        frictionImpulse = tangent.multiplyScalar(jt);
    } else {
        frictionImpulse = tangent.multiplyScalar(impulseMag * mu * Math.signum(jt));
    }

    velA = velA.sub(frictionImpulse.multiplyScalar(1/m1));
    velB = velB.add(frictionImpulse.multiplyScalar(1/m2));

    double damping = 0.999;
    velA = velA.multiplyScalar(damping);
    velB = velB.multiplyScalar(damping);

    a.setVelocity(velA);
    b.setVelocity(velB);
    a.setPosition(posA);
    b.setPosition(posB);
}


}

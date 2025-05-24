package pr.phys.physicsengine2d;


/*
Represents a physical object in the 2D simulation
The class implements very basic mechanics using semi implicit euler integration
 */

import java.util.ArrayList;
import java.util.List;

public class PhysicsObject {
    private Vector2D position;
    private Vector2D velocity;
    private Vector2D acceleration;
    private double mass;
    private boolean frozen = false;
    private final List<Vector2D> trail = new ArrayList<>();

    /*
    Constructor sort of acts as a object creator, this makes a new object with input and position as parameters
    Velocity and Acceleration vectors are initialized to zero
    basic checks done to keep logic intact
     */
    
    public PhysicsObject(Vector2D position, double mass){
        if(position==null){
            throw new IllegalArgumentException("Position Cannot be Null");
        }
        if(mass<=0){
            throw new IllegalArgumentException("Mass cannot be negative");
        }
        
        this.position = position;
        this.velocity = new Vector2D(0, 0);
        this.acceleration = new Vector2D(0, 0);
        this.mass = mass;
    }
    
    /*
    applies a force to the object, this method updates acceleration according to F=ma
    this effect will remain until the next update method is called
     */
    
    public void applyForce(Vector2D force){
        if(force == null){
            throw new IllegalArgumentException("Force cannot be null");
        }
        
        Vector2D a = force.multiplyScalar(1.0/mass); // finds the acceleration vector
        this.acceleration = this.acceleration.add(a); // adds acceleration vector
    }

    /*
    method updates position and velocity again based semi implicit euler integration
    takes time as a parameter and will probably be called every x ms in gameloop
    after update accel is reset
    */

    public void update(double dt){ // both derived according to semi implcit eule r integration
        if(dt<0){
            throw new IllegalArgumentException("Time step cannot be negative");
        }
        this.velocity = this.velocity.add(acceleration.multiplyScalar(dt)); // vf = vi + a*dt
        this.position = this.position.add(velocity.multiplyScalar(dt)); // pf = pi + v*dt

        acceleration = new Vector2D(0, 0); // reset acceleration after update, at ths point the force is done being applied

        trail.add(new Vector2D(position.x, position.y));
        if (trail.size() > 300) {
            trail.remove(0); // optional limit to prevent memory spam
        }
    }
    
    // below are getters, return copies of vectors rather than the vectors themselves because IntelliJ AI wouldnt stop screaming about it when i want to commit
    
    public Vector2D getPosition(){
        return new Vector2D(position.x,position.y);
    }
    
    public Vector2D getVelocity(){
        return new Vector2D(velocity.x,velocity.y);
    }
    
    public Vector2D getAcceleration(){
        return new Vector2D(acceleration.x, acceleration.y);
    } 
    
    public double getMass(){
        return mass;
    }

    public List<Vector2D> getTrail() {
        return trail;
    }

    public void setFrozen(boolean frozen) {
        this.frozen = frozen;
    }

    public boolean isFrozen() {
        return frozen;
    }

    
    // toString override, might cut down later
    @Override
    public String toString(){
        return String.format("Position: %s , Velocity: %s , Acceleration: %s , Mass: %.2f" , position , velocity, acceleration, mass);
    }

}

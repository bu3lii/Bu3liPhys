package pr.phys.physicsengine2d;

public class PhysicsObject {
    public Vector2D position;
    public Vector2D velocity;
    public Vector2D acceleration;
    public double mass;

    public PhysicsObject(Vector2D position, double mass){
        this.position = position;
        this.velocity = new Vector2D(0, 0);
        this.acceleration = new Vector2D(0, 0);
        this.mass = mass;
    }

    // apply force with F= ma | were also updating
    public void applyForce(Vector2D force){
        Vector2D a = force.multiplyScalar(1.0/mass); // finds the acceleration vector
        this.acceleration = this.acceleration.add(a); // adds acceleration vector
    }

    // this method is gonna be frequently called in the gameloop (very likely to change, taking dt as a parameter might not be the best idea)
    public void update(double dt){ // // both derived according to semi implcit eule r integration
        this.velocity = this.velocity.add(acceleration.multiplyScalar(dt)); // vf = vi + a*dt
        this.position = this.position.add(velocity.multiplyScalar(dt)); // pf = pi + v*dt

        acceleration = new Vector2D(0, 0); // reset acceleration after update, at ths point the force is done being applied
    }

    @Override
    public String toString(){
        return String.format("Position: %s , Velocity: %s , Acceleration: %s , Mass: %.2f" , position , velocity, acceleration, mass);
    }

}

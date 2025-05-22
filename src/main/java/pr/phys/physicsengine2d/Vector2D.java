package pr.phys.physicsengine2d;

public class Vector2D {
    public double x;
    public double y;

    public Vector2D(){
        this.x=0;
        this.y=0;
    }

    public Vector2D(double x, double y){
        this.x=x;
        this.y=y;
    }

    public Vector2D add(Vector2D v2){
        return new Vector2D(this.x+v2.x,this.y+v2.y);
    }

    public Vector2D sub(Vector2D v2){
        return new Vector2D(this.x-v2.x,this.y-v2.y);
    }

    public Vector2D multiplyScalar(double scalar){
        return new Vector2D(this.x*scalar,this.y*scalar);
    }

    public Vector2D multiplyElementWise(Vector2D v2){
        return new Vector2D(this.x*v2.x,this.y*v2.y);
    }

    public double magnitude(){
        return Math.sqrt(x*x+y*y);
    }

    public Vector2D normalize(){
        double mag = magnitude();
        if (mag == 0) {
            return new Vector2D(0, 0);
        }
        return new Vector2D(x / mag, y / mag);
    }
}

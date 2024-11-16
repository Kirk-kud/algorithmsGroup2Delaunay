public class Point{
    double x_cor;
    double y_cor;

    Point(double xcor, double ycor){
        this.x_cor = xcor;
        this.y_cor = ycor;
    }

    public String toString(){
        return String.format("[%f, %f]",this.x_cor, this.y_cor);
    }

    double getX(){
        return this.x_cor;
    }

    double getY(){
        return this.y_cor;
    }

    double getDistance(Point otherPoint){
        return Math.sqrt(Math.pow((this.x_cor - otherPoint.getX()), 2) + Math.pow((this.y_cor - otherPoint.getY()),2));
    }
}
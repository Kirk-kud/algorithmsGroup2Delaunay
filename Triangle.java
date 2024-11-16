public class Triangle{
    Point vertexOne;
    Point vertexTwo;
    Point vertexThree;

    Triangle(Point vOne, Point vTwo, Point vThree){
        this.vertexOne = vOne;
        this.vertexTwo = vTwo;
        this.vertexThree = vThree;
    }

    public String toString() {
        return String.format("Vertex One: %s, Vertex Two: %s, Vertex: %s", this.vertexOne, this.vertexTwo, vertexThree);
    }
}
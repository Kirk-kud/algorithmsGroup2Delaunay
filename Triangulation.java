import java.util.ArrayList;
import java.util.Scanner;
import java.util.HashSet;

public class Triangulation {
    ArrayList<Point> p = new ArrayList<>();
    ArrayList<Triangle> t = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("Hello, World!");
    }

    public void findTriangles(){
        ArrayList<Triangle> listOfTriangles = new ArrayList<>();

        double minXCor = Double.MAX_VALUE;
        double minYCor = Double.MAX_VALUE;
        double maxXCor = Double.MIN_VALUE;
        double maxYCor = Double.MIN_VALUE;

        for (int i = 0; i < p.size(); i++){
            minXCor = Math.min(minXCor, p.get(i).x_cor);
            minYCor = Math.min(minYCor, p.get(i).y_cor);

            maxXCor = Math.max(maxXCor, p.get(i).x_cor);
            maxYCor = Math.max(maxYCor, p.get(i).x_cor);
        }

        Triangle superTriangle = new Triangle(new Point(minXCor - 1, minYCor - 1),
                new Point(maxXCor + 1, minYCor - 1),
                new Point((maxXCor + maxYCor)/2,maxYCor + 1));

        listOfTriangles.add(superTriangle);

        for (Point point : p) {
            ArrayList<Triangle> badTriangles = new ArrayList<>();

            for (Triangle triangle : listOfTriangles) {
                if (isPointInCircumcircle(point, triangle)) {
                    badTriangles.add(triangle);
                }
            }

            HashSet<Edge> polygon = new HashSet<>();

            for (Triangle triangle : badTriangles) {
                Edge e1 = new Edge(triangle.vertexOne, triangle.vertexTwo);
                Edge e2 = new Edge(triangle.vertexTwo, triangle.vertexThree);
                Edge e3 = new Edge(triangle.vertexThree, triangle.vertexOne);

                if (!isSharedEdge(e1, badTriangles, triangle)) polygon.add(e1);
                if (!isSharedEdge(e2, badTriangles, triangle)) polygon.add(e2);
                if (!isSharedEdge(e3, badTriangles, triangle)) polygon.add(e3);
            }

            listOfTriangles.removeAll(badTriangles);

            for (Edge edge : polygon) {
                Triangle newTri = new Triangle(edge.start, edge.end, point);
                listOfTriangles.add(newTri);
            }
        }

        ArrayList<Triangle> finalTriangles = new ArrayList<>();
        for (Triangle triangle : listOfTriangles) {
            if (!isTriangleUsingVertex(triangle, superTriangle.vertexOne) &&
                    !isTriangleUsingVertex(triangle, superTriangle.vertexTwo) &&
                    !isTriangleUsingVertex(triangle, superTriangle.vertexThree)) {
                finalTriangles.add(triangle);
            }
        }

        t = finalTriangles;
    }

    public void getPoints(){
        Scanner ask = new Scanner(System.in);
        ArrayList<Point> list = new ArrayList<>();

        int numberOfPoints = 0;

        System.out.print("Enter in the number of points for the problem: ");
        numberOfPoints = ask.nextInt();

        for (int i = 0; i < numberOfPoints; i++){
            System.out.printf("Point %d\n", i+1);

            Point p;
            double x, y;
            System.out.printf("Enter in the x co-ordinate for point %d: ",i+1);
            x = ask.nextDouble();

            System.out.printf("Enter in the y co-ordinate for point %d: ",i+1);
            y = ask.nextDouble();
            p = new Point(x, y);
            list.add(p);
        }
        this.p = list;
    }
    boolean isPointInCircumcircle(Point point, Triangle triangle) {
        Point a = triangle.vertexOne;
        Point b = triangle.vertexTwo;
        Point c = triangle.vertexThree;

        double ax = a.x_cor - point.x_cor;
        double ay = a.y_cor - point.y_cor;
        double bx = b.x_cor - point.x_cor;
        double by = b.y_cor - point.y_cor;
        double cx = c.x_cor - point.x_cor;
        double cy = c.y_cor - point.y_cor;

        double det = (ax * ax + ay * ay) * (bx * cy - cx * by) -
                (bx * bx + by * by) * (ax * cy - cx * ay) +
                (cx * cx + cy * cy) * (ax * by - bx * ay);

        return det > 0;
    }

    boolean isSharedEdge(Edge edge, ArrayList<Triangle> triangles, Triangle current) {
        for (Triangle triangle : triangles) {
            if (triangle == current) continue;

            Edge e1 = new Edge(triangle.vertexOne, triangle.vertexTwo);
            Edge e2 = new Edge(triangle.vertexTwo, triangle.vertexThree);
            Edge e3 = new Edge(triangle.vertexThree, triangle.vertexOne);

            if (edge.equals(e1) || edge.equals(e2) || edge.equals(e3)) {
                return true;
            }
        }
        return false;
    }

    boolean isTriangleUsingVertex(Triangle triangle, Point vertex) {
        return triangle.vertexOne.x_cor == vertex.x_cor && triangle.vertexOne.y_cor == vertex.y_cor ||
                triangle.vertexTwo.x_cor == vertex.x_cor && triangle.vertexTwo.y_cor == vertex.y_cor ||
                triangle.vertexThree.x_cor == vertex.x_cor && triangle.vertexThree.y_cor == vertex.y_cor;
    }

}
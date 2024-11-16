import java.util.*;

class Point {
    double x, y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return Math.abs(point.x - x) < 1e-10 && Math.abs(point.y - y) < 1e-10;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}

class Edge {
    Point p1, p2;

    public Edge(Point p1, Point p2) {
        // Ensure consistent ordering of points for edge equality
        if (p1.x < p2.x || (p1.x == p2.x && p1.y < p2.y)) {
            this.p1 = p1;
            this.p2 = p2;
        } else {
            this.p1 = p2;
            this.p2 = p1;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Edge edge = (Edge) o;
        return p1.equals(edge.p1) && p2.equals(edge.p2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(p1, p2);
    }
}

class Triangle {
    Point[] vertices;
    Point circumcenter;
    double circumradius;

    public Triangle(Point p1, Point p2, Point p3) {
        vertices = new Point[]{p1, p2, p3};
        calculateCircumcircle();
    }

    private void calculateCircumcircle() {
        double x1 = vertices[0].x, y1 = vertices[0].y;
        double x2 = vertices[1].x, y2 = vertices[1].y;
        double x3 = vertices[2].x, y3 = vertices[2].y;

        double D = 2 * (x1 * (y2 - y3) + x2 * (y3 - y1) + x3 * (y1 - y2));

        if (Math.abs(D) < 1e-10) {
            throw new IllegalArgumentException("Points are collinear");
        }

        double Ux = ((x1 * x1 + y1 * y1) * (y2 - y3) +
                    (x2 * x2 + y2 * y2) * (y3 - y1) +
                    (x3 * x3 + y3 * y3) * (y1 - y2)) / D;

        double Uy = ((x1 * x1 + y1 * y1) * (x3 - x2) +
                    (x2 * x2 + y2 * y2) * (x1 - x3) +
                    (x3 * x3 + y3 * y3) * (x2 - x1)) / D;

        circumcenter = new Point(Ux, Uy);
        circumradius = Math.sqrt((x1 - Ux) * (x1 - Ux) + (y1 - Uy) * (y1 - Uy));
    }

    public boolean containsPoint(Point point) {
        double dist = Math.sqrt(Math.pow(point.x - circumcenter.x, 2) +
                              Math.pow(point.y - circumcenter.y, 2));
        return dist < circumradius - 1e-10;
    }

    public Set<Edge> getEdges() {
        Set<Edge> edges = new HashSet<>();
        for (int i = 0; i < 3; i++) {
            edges.add(new Edge(vertices[i], vertices[(i + 1) % 3]));
        }
        return edges;
    }

    public boolean hasVertex(Point p) {
        return Arrays.asList(vertices).contains(p);
    }
}

public class DelaunayTriangulation {
    public static List<Triangle> bowyerWatson(List<Point> points) {
        // Find bounding box
        double minX = points.stream().mapToDouble(p -> p.x).min().getAsDouble();
        double maxX = points.stream().mapToDouble(p -> p.x).max().getAsDouble();
        double minY = points.stream().mapToDouble(p -> p.y).min().getAsDouble();
        double maxY = points.stream().mapToDouble(p -> p.y).max().getAsDouble();

        double dx = maxX - minX;
        double dy = maxY - minY;
        double dmax = Math.max(dx, dy);
        double midX = (minX + maxX) / 2;
        double midY = (minY + maxY) / 2;

        // Create super triangle
        Point p1 = new Point(midX - 20 * dmax, midY - dmax);
        Point p2 = new Point(midX, midY + 20 * dmax);
        Point p3 = new Point(midX + 20 * dmax, midY - dmax);
        Triangle superTriangle = new Triangle(p1, p2, p3);

        // Initialize triangulation with super triangle
        List<Triangle> triangulation = new ArrayList<>();
        triangulation.add(superTriangle);

        // Add points one at a time
        for (Point point : points) {
            List<Triangle> badTriangles = new ArrayList<>();

            // Find triangles whose circumcircle contains the point
            for (Triangle triangle : triangulation) {
                if (triangle.containsPoint(point)) {
                    badTriangles.add(triangle);
                }
            }

            // Find boundary of polygon hole
            Set<Edge> boundary = new HashSet<>();
            for (Triangle triangle : badTriangles) {
                for (Edge edge : triangle.getEdges()) {
                    boolean isShared = badTriangles.stream()
                        .filter(t -> t != triangle)
                        .anyMatch(t -> t.getEdges().contains(edge));
                    if (!isShared) {
                        boundary.add(edge);
                    }
                }
            }

            // Remove bad triangles
            triangulation.removeAll(badTriangles);

            // Re-triangulate the hole
            for (Edge edge : boundary) {
                Triangle newTriangle = new Triangle(point, edge.p1, edge.p2);
                triangulation.add(newTriangle);
            }
        }

        // Remove triangles that share vertices with super triangle
        Set<Point> superVertices = new HashSet<>(Arrays.asList(p1, p2, p3));
        triangulation.removeIf(triangle -> 
            Arrays.stream(triangle.vertices)
                  .anyMatch(superVertices::contains));

        return triangulation;
    }

    public static void main(String[] args) {
        // Example usage
        List<Point> points = Arrays.asList(
            new Point(0, 0),
            new Point(1, 0),
            new Point(0.5, 1),
            new Point(0.2, 0.2),
            new Point(0.8, 0.2)
        );

        List<Triangle> triangles = bowyerWatson(points);

        // Print results
        System.out.println("Generated " + triangles.size() + " triangles:");
        for (int i = 0; i < triangles.size(); i++) {
            System.out.println("Triangle " + (i + 1) + ":");
            for (Point vertex : triangles.get(i).vertices) {
                System.out.printf("  (%.2f, %.2f)%n", vertex.x, vertex.y);
            }
        }
    }
}
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Plotting extends JPanel {
    ArrayList<Point> listOfPoints = new ArrayList<>();
    ArrayList<Triangle> listOfTriangles = new ArrayList<>();

    public void addPoint(Point p){
        listOfPoints.add(p);
    }

    public void addTriangle(Triangle t) {
        listOfTriangles.add(t);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();
        int padding = 50;

        // Find data bounds
        double minX = Double.MAX_VALUE;
        double maxX = Double.MIN_VALUE;
        double minY = Double.MAX_VALUE;
        double maxY = Double.MIN_VALUE;

        for (Point p : listOfPoints) {
            minX = Math.min(minX, p.x_cor);
            maxX = Math.max(maxX, p.x_cor);
            minY = Math.min(minY, p.y_cor);
            maxY = Math.max(maxY, p.y_cor);
        }

        // Scale factors
        double scaleX = (width - 2 * padding) / (maxX - minX);
        double scaleY = (height - 2 * padding) / (maxY - minY);

        // Draw triangles
        g2d.setColor(Color.BLUE);
        for (Triangle t : listOfTriangles) {
            int x1 = (int)(padding + (t.vertexOne.x_cor - minX) * scaleX);
            int y1 = (int)(height - padding - (t.vertexOne.y_cor - minY) * scaleY);
            int x2 = (int)(padding + (t.vertexTwo.x_cor - minX) * scaleX);
            int y2 = (int)(height - padding - (t.vertexTwo.y_cor - minY) * scaleY);
            int x3 = (int)(padding + (t.vertexThree.x_cor - minX) * scaleX);
            int y3 = (int)(height - padding - (t.vertexThree.y_cor - minY) * scaleY);

            g2d.drawLine(x1, y1, x2, y2);
            g2d.drawLine(x2, y2, x3, y3);
            g2d.drawLine(x3, y3, x1, y1);
        }

        // Draw points
        g2d.setColor(Color.RED);
        int pointSize = 6;
        for (Point p : listOfPoints) {
            int x = (int)(padding + (p.x_cor - minX) * scaleX) - pointSize/2;
            int y = (int)(height - padding - (p.y_cor - minY) * scaleY) - pointSize/2;
            g2d.fillOval(x, y, pointSize, pointSize);
        }
    }

    public static void main(String[] args){
        JFrame frame = new JFrame("Delaunay Triangulation Plotting");
        Plotting drawPanel = new Plotting();

        Triangulation solveProblem = new Triangulation();

        solveProblem.getPoints();
        solveProblem.findTriangles();

        // Adding the points
        ArrayList<Point> p = solveProblem.p;
        for (int a = 0; a < p.size(); a++){
            drawPanel.addPoint(p.get(a));
        }

        // Adding the triangles
        ArrayList<Triangle> t = solveProblem.t;
        for (int b = 0; b < t.size(); b++){
            drawPanel.addTriangle(t.get(b));
        }

        frame.add(drawPanel);
        frame.setSize(500, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }


}

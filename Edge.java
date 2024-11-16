public class Edge {
    Point start;
    Point end;

    Edge(Point start, Point end) {
        this.start = start;
        this.end = end;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof Edge))
            return false;
        Edge other = (Edge) obj;
        return (start.x_cor == other.start.x_cor && start.y_cor == other.start.y_cor &&
                end.x_cor == other.end.x_cor && end.y_cor == other.end.y_cor) ||
                (start.x_cor == other.end.x_cor && start.y_cor == other.end.y_cor &&
                        end.x_cor == other.start.x_cor && end.y_cor == other.start.y_cor);
    }

    public int hashCode() {
        return (int)(start.x_cor * 31 + start.y_cor * 17 + end.x_cor * 13 + end.y_cor * 7);
    }
}
/**
 * Created by Miron on 22.11.2017.
 */
public class Index {
    private int x;
    private int y;

    public Index plusXY(int x, int y){
        return new Index(this.x+x,this.y+y);
    }

    public Index(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Index index = (Index) o;

        if (x != index.x) return false;
        return y == index.y;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }

    public int x() {
        return x;
    }

    public void x(int x) {
        this.x = x;
    }

    public int y() {
        return y;
    }

    public void y(int y) {
        this.y = y;
    }
}

import java.util.List;

/**
 * Created by Miron on 22.11.2017.
 */
public class Przeniesienie {
    public Przeniesienie(List<Byt> byty, int x, int y) {
        this.byty = byty;
        this.x = x;
        this.y = y;
    }


    public List<Byt> Byty() {
        return byty;
    }

    public int x(){
        return x;
    }

    public int y(){
        return y;
    }

    private List<Byt> byty;
    private int x,y;
}

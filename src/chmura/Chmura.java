package chmura;

import chmura.Pomocnicze.Index;

import java.util.*;
import java.util.function.BiPredicate;

/**
 * Created by Miron on 22.11.2017.
 */
public class Chmura {
    private BiPredicate<Integer,Integer> stan = new BiPredicate<Integer, Integer>() {
        @Override
        public boolean test(Integer integer, Integer integer2) {
            return false;
        }
    };

    private List<Byt> chmura = new LinkedList<>();
    private HashMap<Byt,Index> bytIndexHashMap = new HashMap<>();
    private HashMap<Index,Byt> indexBytHashMap = new HashMap<>();

    public Chmura(){}

    public Chmura(BiPredicate<Integer, Integer> stan) {
        this.stan = stan;
    }

    public Byt ustaw(int x, int y) throws InterruptedException{
        Byt result = null;
        synchronized (this){
            if(stan.test(x,y)){
                while(true)
                    this.wait();
            }
            try{
                while(indexBytHashMap.containsKey(new Index(x,y))){
                    this.wait();
                }
                result = new Byt();
                bytIndexHashMap.put(result,new Index(x,y));
                indexBytHashMap.put(new Index(x,y), result);

            }   catch (Exception e){
                throw e;
            }
        }
        return result;
    }

    private boolean możnaPrzestawić(Collection<Byt> byty, int dx, int dy) throws NiebytException {
        for(Byt byt : byty) {

            if(bytIndexHashMap.containsKey(byt) == false)
                throw new NiebytException();
            if (indexBytHashMap.containsKey(bytIndexHashMap.get(byt).plusXY(dx, dy)) == true && byty.contains(indexBytHashMap.get(bytIndexHashMap.get(byt).plusXY(dx, dy))) == false )
                return false;
        }
        return true;
    }

    private void operacjaPrzestawiania(Collection<Byt> byty, int dx, int dy){
        for(Byt byt : byty){
            Index index = bytIndexHashMap.get(byt);
            bytIndexHashMap.replace(byt,index,index.plusXY(dx,dy));
            indexBytHashMap.remove(index);
            indexBytHashMap.put(index.plusXY(dx,dy),byt);
        }
    }

    public void przestaw(Collection<Byt> byty, int dx, int dy) throws NiebytException,InterruptedException{
        synchronized (this){
            try{
                while(możnaPrzestawić(byty,dx,dy) == false){
                    this.wait();
                }
                operacjaPrzestawiania(byty,dx,dy);
            }catch (InterruptedException e){
                e.printStackTrace();
            } catch (NiebytException e){
                e.printStackTrace();
            }
            this.notifyAll();
        }
    }

    void kasuj(Byt byt) throws NiebytException {
        synchronized (this) {
            if (bytIndexHashMap.containsKey(byt) == false) {
                throw new NiebytException();
            }
            Index index = bytIndexHashMap.get(byt);
            bytIndexHashMap.remove(byt);
            indexBytHashMap.remove(index);
            this.notifyAll();
        }
    }

    int[] miejsce(Byt byt){
        Index index;
        synchronized (this) {
            index = bytIndexHashMap.get(byt);
        }
        if(index == null)
            return null;
        int[] result = new int[2];
        result[0]=index.x();
        result[1]=index.y();
        return result;
    }
}

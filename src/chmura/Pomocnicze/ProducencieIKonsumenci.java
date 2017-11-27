package chmura.Pomocnicze;

import chmura.Byt;
import chmura.Chmura;
import chmura.NiebytException;

import java.util.LinkedList;
import java.util.Random;
import java.util.function.BiPredicate;

/**
 * Created by Miron on 26.11.2017.
 */
public class ProducencieIKonsumenci {
    public static void main(String [] args)throws InterruptedException, NiebytException {
        class Producent {
            public Producent(int x, int y, Chmura chmura, int n, int[] bufor,Byt byt) {
                this.x = x;
                this.y = y;
                this.chmura = chmura;
                N = n;
                this.bufor = bufor;
                mójByt = byt;
            }
            public void produkuj() throws InterruptedException,NiebytException{
                int rand =new  Random().nextInt(1000);
                System.out.println("Produkuje produkuje na polu "+x+"|"+y+" wartość:"  +rand);
                bufor[y] = rand;
                Thread.sleep(10*new Random().nextInt(100));

                if(y+1 == N){
                    chmura.przestaw(new LinkedList<Byt>(){{add(mójByt);}},0,-N+1);
                    y=0;
                }
                else{
                    chmura.przestaw(new LinkedList<Byt>(){{add(mójByt);}},0,1);
                    y++;
                }
            }
            int x,y;
            Chmura chmura;
            int N;
            int bufor[];
            Byt mójByt;
        }

        class Konsument {
            public Konsument(int x, int y, Chmura chmura, int n, int[] bufor, Byt byt) {
                this.x = x;
                this.y = y;
                this.chmura = chmura;
                N = n;
                this.bufor = bufor;
                mójByt = byt;

            }
            public void konsumuj() throws InterruptedException,NiebytException{
                System.out.println("    Konsument konsumuje z pola "+x+"|"+y+" wartość:"  + bufor[y]);
                bufor[y]=-1;
                Thread.sleep(10*new Random().nextInt(1000));
                if(y+1 == N){
                    chmura.przestaw(new LinkedList<Byt>(){{add(mójByt);}},0,-N+1);
                    y=0;
                }
                else{
                    chmura.przestaw(new LinkedList<Byt>(){{add(mójByt);}},0,1);
                    y++;
                }
            }
            int x,y;
            Chmura chmura;
            int N;
            int bufor[];
            Byt mójByt;

        }

        int n = 10;
        int bufor[] = new int[10];
         BiPredicate<Integer,Integer> stan = new BiPredicate<Integer, Integer>() {
            @Override
            public boolean test(Integer integer, Integer integer2) {
                return ( integer==1 && integer2==1 ) || ( integer<0 || integer2<0 ) || ( integer == n || integer2 == n );
            }
        };

        Chmura chmura  = new Chmura(stan);

        Byt bytProducenta = chmura.ustaw(0,0);
        Byt bytKonsumenta = chmura.ustaw(0,n-1);

        Konsument konsument = new Konsument(0,0,chmura,n,bufor,bytKonsumenta);
        Producent producent = new Producent(0,0,chmura,n,bufor,bytProducenta);

        Thread threadProducenta = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    try{
                        producent.produkuj();
                    }  catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        });

        Thread threadKonsumenta = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    chmura.przestaw(new LinkedList<Byt>() {{add(bytKonsumenta);}}, 0, -n+1);
                    while (true) {
                        konsument.konsumuj();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        threadKonsumenta.start();
        threadProducenta.start();
    }
}

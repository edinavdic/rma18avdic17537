package ba.unsa.etf.rma.edin.s1_17537;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Edin on 22.05.2018..
 */

public class Autor implements Serializable {

    private String imeiPrezime;
    private ArrayList<String> knjige;

    public Autor(String imeiPrezime, String id){
        this.imeiPrezime = imeiPrezime;
        knjige = new ArrayList<String>();
        knjige.add(id);
    }

    public String getImeiPrezime() {return imeiPrezime;}
    public void setImeiPrezime(String imeiPrezime) {this.imeiPrezime = imeiPrezime;}
    public void dodajKnjigu(String id){
        if(!knjige.contains(id))
            knjige.add(id);
    }

}

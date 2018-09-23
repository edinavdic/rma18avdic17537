package ba.unsa.etf.rma.edin.s1_17537;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Parcelable;

import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Edin on 28.03.2018..
 */

public class Knjiga implements Serializable {

//trazeni atributi
    private String naziv;
    private String id;
    private String opis;
    private ArrayList<Autor> autori;
    private String datumObjavljivanja;
    private URL slika;
    private int brojStranica;
//dodatni atributi
    private static int globalID = 100;
    private String imeAutora;
    private String kategorijaKnjige;
    private Boolean dirnut;
    private Boolean ponovioSe;
    private int howmuchAutor;

    public Knjiga(String id, String naziv, ArrayList<Autor> autori,  String opis, String datumObjavljivanja, URL slika, int brojStranica){
        this.naziv = naziv;
        this.id = id;
        if(this.id == null) this.id = "IdNull";
        this.brojStranica = brojStranica;
        this.opis = opis;
        if(this.opis == null) this.opis = "OpisNull";
        this.autori = autori;
        this.datumObjavljivanja = datumObjavljivanja;
        if(this.datumObjavljivanja == null) this.datumObjavljivanja = "DatumObjavljivanjaNull";
        this.slika = slika;


        this.kategorijaKnjige = "Roman";
        this.howmuchAutor = 1;
        this.dirnut = false;
        this.ponovioSe = false;
        this.imeAutora = autori.get(0).getImeiPrezime();
    }

    public Knjiga(String naziv, String id, String opis, ArrayList<Autor> autori, String datumObjavljivanja, int brojStranica){
        this.naziv = naziv;
        this.id = id;
        if(this.id == null) this.id = "IdNull";
        this.brojStranica = brojStranica;
        this.opis = opis;
        if(this.opis == null) this.opis = "OpisNull";
        this.autori = autori;
        this.datumObjavljivanja = datumObjavljivanja;
        if(this.datumObjavljivanja == null) this.datumObjavljivanja = "DatumObjavljivanjaNull";
        this.slika = null;

        this.kategorijaKnjige = "Roman";
        this.howmuchAutor = 1;
        this.dirnut = false;
        this.ponovioSe = false;
        this.imeAutora = autori.get(0).getImeiPrezime();
    }

    public Knjiga(String nazivKnjige, String imeAutora, String kategorijaKnjige) {
        this.naziv = nazivKnjige;
        this.imeAutora = imeAutora;
        this.kategorijaKnjige = kategorijaKnjige;

        int id = globalID + 1; globalID++;
        this.id = String.valueOf(id);

        this.dirnut = false;
        this.ponovioSe = false;
        this.howmuchAutor = 1;

        //za ostale normalne atribute
    }

    public int getBrojStranica() {
        return brojStranica;
    }

    public void setBrojStranica(int brojStranica) {
        this.brojStranica = brojStranica;
    }

    public URL getSlika() {
        return slika;
    }

    public void setSlika(URL slika) {
        this.slika = slika;
    }

    public String getDatumObjavljivanja() {
        return datumObjavljivanja;
    }

    public void setDatumObjavljivanja(String datumObjavljivanja) {
        this.datumObjavljivanja = datumObjavljivanja;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public ArrayList<Autor> getAutori() {
        return autori;
    }

    public void setAutori(ArrayList<Autor> autori) {
        this.autori = autori;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }




    public Boolean getDirnut() {
        return dirnut;
    }

    public void setDirnut(Boolean dirnut) {
        this.dirnut = dirnut;
    }

    public String getImeAutora() {
        return imeAutora;
    }

    public void setImeAutora(String imeAutora) {
        this.imeAutora = imeAutora;
    }

    public String getKategorijaKnjige() {
        return kategorijaKnjige;
    }

    public void setKategorijaKnjige(String kategorijaKnjige) {
        this.kategorijaKnjige = kategorijaKnjige;
    }
    public Boolean getPonovioSe() {
        return ponovioSe;
    }

    public void setPonovioSe(Boolean ponovioSe) {
        this.ponovioSe = ponovioSe;
    }

    public int getHowmuchAutor() {
        return howmuchAutor;
    }

    public void setHowmuchAutor(int howmuchAutor) {
        this.howmuchAutor = howmuchAutor;
    }
}

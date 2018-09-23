package ba.unsa.etf.rma.edin.s1_17537;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Edin on 04.06.2018..
 */

public class BazaOpenHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "mojaBaza.db";
    public static final int DATABASE_VERSION = 1;


    public static final String DATABASE_TABLE_KATEGORIJA = "Kategorija";
    public static final String DATABASE_TABLE_KNJIGA = "Knjiga";
    public static final String DATABASE_TABLE_AUTOR = "Autor";
    public static final String DATABASE_TABLE_AUTORSTVO = "Autorstvo";




    public static final String TABLE_AUTORSTVO_ID = "_id";
    public static final String TABLE_AUTORSTVO_ID_AUTORA = "idautora";
    public static final String TABLE_AUTORSTVO_ID_KNJIGE = "idknjige";

    public static final String TABLE_AUTOR_ID = "_id";
    public static final String TABLE_AUTOR_IME = "ime";

    public static final String TABLE_KNJIGA_ID = "_id";
    public static final String TABLE_KNJIGA_NAZIV = "naziv";
    public static final String TABLE_KNJIGA_OPIS = "opis";
    public static final String TABLE_KNJIGA_DATUM = "datumObjavljivanja";
    public static final String TABLE_KNJIGA_STRANICE = "brojStranica";
    public static final String TABLE_KNJIGA_SERVIS = "idWebServis";
    public static final String TABLE_KNJIGA_ID_KATEGORIJE = "idkategorije";
    public static final String TABLE_KNJIGA_SLIKA = "slika";
    public static final String TABLE_KNJIGA_PREGLEDANA = "pregledana";

    public static final String TABLE_KATEGORIJA_ID = "_id";
    public static final String TABLE_KATEGORIJA_NAZIV = "naziv";

    private static final String DB_CREATE_AUTORSTVO = "create table "+DATABASE_TABLE_AUTORSTVO+" ("+
            TABLE_AUTORSTVO_ID+" integer primary key autoincrement, "+
            TABLE_AUTORSTVO_ID_AUTORA+" integer not null, "+
            TABLE_AUTORSTVO_ID_KNJIGE+" integer not null);";
    private static final String DB_CREATE_AUTOR = "create table "+DATABASE_TABLE_AUTOR+" ("+
            TABLE_AUTOR_ID+" integer primary key autoincrement, "+
            TABLE_AUTOR_IME+" text not null);";
    private static final String DB_CREATE_KNJIGA = "create table "+DATABASE_TABLE_KNJIGA+" ("+
            TABLE_KNJIGA_ID + " integer primary key autoincrement, "+
            TABLE_KNJIGA_NAZIV + " text not null, "+
            TABLE_KNJIGA_OPIS + " text, "+
            TABLE_KNJIGA_DATUM + " text, "+
            TABLE_KNJIGA_STRANICE + " integer, "+
            TABLE_KNJIGA_SERVIS + " text, "+
            TABLE_KNJIGA_ID_KATEGORIJE + " integer not null, "+
            TABLE_KNJIGA_SLIKA + " text, "+
            TABLE_KNJIGA_PREGLEDANA + " integer not null);";
    private static final String DB_CREATE_KATEGORIJA = "create table "+DATABASE_TABLE_KATEGORIJA+" ("+
            TABLE_KATEGORIJA_ID + " integer primary key autoincrement, "+
            TABLE_KATEGORIJA_NAZIV + " text not null);";




    public BazaOpenHelper(Context context){
        super(context,DATABASE_NAME,null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(DB_CREATE_KATEGORIJA);
        db.execSQL(DB_CREATE_KNJIGA);
        db.execSQL(DB_CREATE_AUTOR);
        db.execSQL(DB_CREATE_AUTORSTVO);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1){
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_AUTORSTVO);
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_AUTOR);
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_KNJIGA);
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_KATEGORIJA);
        onCreate(db);
    }

    public long dodajKategoriju(String naziv){

        long katId;
        SQLiteDatabase db;
        try {
            db = getWritableDatabase();
        }
        catch(Exception e){
            e.printStackTrace();
            return -1;
        }

        ContentValues val = new ContentValues();
        try {
            val.put(TABLE_KATEGORIJA_NAZIV, naziv);
        }
        catch (Exception e){
            e.printStackTrace();db.close();
            return  -1;
        }
        katId = db.insert( DATABASE_TABLE_KATEGORIJA,null, val);

        db.close();

        return katId;
    }

    public long dodajKnjigu(Knjiga knjiga){

        long idKnjige;
        long idKategorije = dajIdKategorije(knjiga.getKategorijaKnjige());
        if(idKategorije == -1)
            return  -1;
        int pregledana = 0;
        if (knjiga.getDirnut())
            pregledana = 1;

        try {

            SQLiteDatabase db = getWritableDatabase();
            ContentValues val = new ContentValues();

            val.put(TABLE_KNJIGA_NAZIV, knjiga.getNaziv());
            val.put(TABLE_KNJIGA_OPIS, knjiga.getOpis());
            val.put(TABLE_KNJIGA_DATUM, knjiga.getDatumObjavljivanja());
            val.put(TABLE_KNJIGA_STRANICE, knjiga.getBrojStranica());
            val.put(TABLE_KNJIGA_SERVIS, knjiga.getId());
            val.put(TABLE_KNJIGA_ID_KATEGORIJE, idKategorije);

            if (knjiga.getSlika() != null)
                val.put(TABLE_KNJIGA_SLIKA, knjiga.getSlika().toString());
            else
                val.put(TABLE_KNJIGA_SLIKA, "");

            val.put(TABLE_KNJIGA_PREGLEDANA, pregledana);

            long idNoveKnjige=db.insert(DATABASE_TABLE_KNJIGA, null, val);
            if(idNoveKnjige==-1){
                close();
                return -1;
            }
            db.close();



            idKnjige = dajIdKnjige(knjiga);
            ArrayList<Autor> autoriKnjige = knjiga.getAutori();

            if (autoriKnjige != null) {
                for (Autor autor : autoriKnjige) {

                    String imeAutora = autor.getImeiPrezime();

                    db = getWritableDatabase();
                    ContentValues val2 = new ContentValues();
                    val2.put(TABLE_AUTOR_IME, imeAutora);
                    db.insert(DATABASE_TABLE_AUTOR, null, val2);
                    db.close();

                    long idAutora = dajIdAutora(imeAutora);
                    if (idAutora == -1)
                        return -1;

                    db = getWritableDatabase();
                    ContentValues val3 = new ContentValues();
                    val3.put(TABLE_AUTORSTVO_ID_AUTORA, idAutora);
                    val3.put(TABLE_AUTORSTVO_ID_KNJIGE, idKnjige);
                    long idNew = db.insert(DATABASE_TABLE_AUTORSTVO, null, val3);
                    if(idNew == -1){
                        close();
                        return -1;
                    }
                }
            }
            db.close();
            return idKnjige;
        }
        catch (Exception e){e.printStackTrace();close();return -1;}
    }

    public boolean writeKategorije(ArrayList<String> arrKategorije){
        try {
            String[] colResult = { TABLE_KATEGORIJA_NAZIV };
            SQLiteDatabase db = getWritableDatabase();
            Cursor cursor = db.query(DATABASE_TABLE_KATEGORIJA, colResult,
                    null, null,
                    null, null,
                    null);

            while (cursor.moveToNext()) {
                arrKategorije.add(cursor.getString(0));
            }
            cursor.close();
            db.close();
        }
        catch (Exception ex) {
            ex.printStackTrace();
            close();
            return false;
        }
        return true;
    }

    public void writeKnjige(ArrayList<Knjiga> arrKnjige){
        SQLiteDatabase db = getWritableDatabase();
        String[] colResult = {TABLE_KNJIGA_ID};
        Cursor cursor = db.query(DATABASE_TABLE_KNJIGA, colResult,
                null,null,null,
                null,null);
        // FORMIRANJE KNJIGE IZ BAZE ............
        cursor.close();
        db.close();
        // ne pozivam
    }

    private long dajIdKategorije(String naziv){

        long id = -1;
        SQLiteDatabase db;

        try{
            db=getWritableDatabase();
        }
        catch (Exception e){
            e.printStackTrace();
            return -1;
        }

        try {
            String[] colRes = {TABLE_KATEGORIJA_ID};
            String[] whereArgs={naziv};
            String where = TABLE_KATEGORIJA_NAZIV + " = ?";
            Cursor cursor = db.query(DATABASE_TABLE_KATEGORIJA, colRes, where, whereArgs, null, null, null);
            cursor.moveToFirst();
            id = cursor.getInt(0);
            cursor.close();
        }
        catch (Exception e){
            e.printStackTrace();
            db.close();
            return -1;
        }
        db.close();
        return id;
    }

    private long dajIdAutora(String naziv){
        long id = -1;
        SQLiteDatabase db;

        try{
            db=getWritableDatabase();
        }
        catch (Exception e){
            e.printStackTrace();
            return -1;
        }

        try {
            String[] colRes = {TABLE_AUTOR_ID};
            String[] whereArgs={naziv};
            String where = TABLE_AUTOR_IME + " = ?";
            Cursor cursor = db.query(DATABASE_TABLE_AUTOR, colRes, where, whereArgs,
                    null,
                    null,
                    null);

            cursor.moveToFirst();
            id = cursor.getInt(0);
            cursor.close();
        }
        catch (Exception e){
            e.printStackTrace();
            db.close();
            return -1;
        }
        db.close();
        return id;
    }


    private int dajIdKnjige(Knjiga k){
        SQLiteDatabase db;
        int id;
        try{
            db=getWritableDatabase();
        }
        catch (Exception e){
            e.printStackTrace();
            return -1;
        }
        try{
            String[] whereArgs = {k.getNaziv(), k.getId()};
            String[] colRes = {TABLE_KNJIGA_ID};
            String where = TABLE_KNJIGA_NAZIV + " = ? AND "+ TABLE_KNJIGA_SERVIS + " = ?";
            Cursor cursor= db.query(DATABASE_TABLE_KNJIGA, colRes, where, whereArgs,
                    null,
                    null,
                    null);

            cursor.moveToFirst();

            id = cursor.getInt(0);
            cursor.close();
        }
        catch (Exception e){
            e.printStackTrace();
            db.close();
            return -1;
        }
        db.close();
        return id;
    }




    public ArrayList<Knjiga> knjigeKategorije(long idKategorije){
        ArrayList<Knjiga> arrKnjigeKategorije=new ArrayList<Knjiga>();
        return arrKnjigeKategorije;
    }
    public ArrayList<Knjiga> knjigeAutora(long idAutora){
        ArrayList<Knjiga> arrKnjiga=new ArrayList<Knjiga>();
        return arrKnjiga;
    }


}

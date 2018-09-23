package ba.unsa.etf.rma.edin.s1_17537;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class KategorijeAkt extends AppCompatActivity implements FragmentPreporuci.OnFragmentInteractionListener, KnjigeFragment.OnFragmentInteractionListener, ListeFragment.OnFragmentInteractionListener, DodavanjeKnjigeFragment.OnFragmentInteractionListener, FragmentOnline.OnFragmentInteractionListener {


    @Override
    public void pokreniMe(String imeKnjige, String imeAutora) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentPreporuci preporuciFragment = FragmentPreporuci.newInstance(imeKnjige, imeAutora);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frameL, preporuciFragment , "Preporuci").addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void nosiGaUFragmentOnline() {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentOnline onlineFragment = FragmentOnline.newInstance(arrayKategorija, arrayKnjiga);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frameL, onlineFragment , "Online").addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void nosiGaUDodajKnjigu() {
        FragmentManager fragmentManager = getFragmentManager();
        DodavanjeKnjigeFragment dodavanjeFragment = DodavanjeKnjigeFragment.newInstance(arrayKategorija, arrayKnjiga);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frameL, dodavanjeFragment , "Dodaj").addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void spasiBoje(ArrayList<Knjiga> arKnj) {
        arrayKnjiga = arKnj;
    }

    @Override
    public void dajMuKnjigeFragment(Boolean kategorije, String trazeno){
        FragmentManager fragmentManager = getFragmentManager();
        KnjigeFragment knjigeFragment = KnjigeFragment.newInstance(kategorije, arrayKnjiga, trazeno);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frameL, knjigeFragment, "Knjige").addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void novaKnjigaSpasi(ArrayList<Knjiga> arrKnjig, Knjiga newKnjigaBaza) {
        Log.d("bookaddDB", "dodajKnjigu!");
        BOH = new BazaOpenHelper(this);
        BOH.dodajKnjigu(newKnjigaBaza);
        BOH.close();
        arrayKnjiga = arrKnjig;
    }


    @Override
    public void spasiMeUAktivnostKategorija(ArrayList<String> arrKat, ArrayList<Knjiga> arrKnj, String newKategorijaBaza) {
        // dodajKategoriju UBACITI
        Log.d("cataddDB", "dodajKategoriju!");
        BOH = new BazaOpenHelper(this);
        BOH.dodajKategoriju(newKategorijaBaza);
        BOH.close();

        arrayKategorija = arrKat;
        arrayKnjiga = arrKnj;
    }




    //atributi

    ArrayList<String> arrayKategorija;
    ArrayList<Knjiga> arrayKnjiga;

    BazaOpenHelper BOH;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kategorije_akt);


        arrayKnjiga = new ArrayList<Knjiga>();
        arrayKategorija = new ArrayList<String>();

        //start values
        arrayKategorija.add(0,"Drama");
        arrayKategorija.add(0,"Ekonomija");
        arrayKategorija.add(0,"Sport");
        arrayKategorija.add(0,"Fantastika");
        arrayKategorija.add(0,"Poezija");
        arrayKategorija.add(0,"Roman");
        arrayKategorija.add(0,"Filozofija");
        //


        BOH = new BazaOpenHelper(this);
        //<!----------------------------- zakomentirati prije provjera!
        boolean katWriteValidno = BOH.writeKategorije(arrayKategorija);
        if(!katWriteValidno)
            Log.d("ErrorDB", "Greska prilikom upisivanja kategorija!");
        BOH.close();


        FragmentManager fragmentManager = getFragmentManager();
        ListeFragment listeFrag = ListeFragment.newInstance(arrayKategorija, arrayKnjiga);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.frameL, listeFrag , "Lista");
        transaction.commit();




        /*------------------------------

        adapterKategorija = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayKategorija);
        listaKategorija.setAdapter(adapterKategorija);

        dPretraga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String upisano = tekstPretraga.getText().toString();
                adapterKategorija.getFilter().filter(upisano,new Filter.FilterListener() {
                    @Override
                    public void onFilterComplete(int i) {
                        if(adapterKategorija.isEmpty()) {
                            dDodajKategoriju.setEnabled(true);
                        }
                        else
                            dDodajKategoriju.setEnabled(false);
                    }
                } );
              adapterKategorija.notifyDataSetChanged();

            }
        });

        dDodajKategoriju.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arrayKategorija.add(0,tekstPretraga.getText().toString());
                tekstPretraga.setText("");
                adapterKategorija.clear();
                adapterKategorija.addAll(arrayKategorija);
                adapterKategorija.notifyDataSetChanged();
                adapterKategorija.getFilter().filter("");
                dDodajKategoriju.setEnabled(false);

            }
        });

        dDodajKnjigu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle a = new Bundle();
                a.putSerializable("knjige", arrayKnjiga);
                Intent i = new Intent(KategorijeAkt.this, DodavanjeKnjigeAkt.class);
                i.putExtra("kategorije", arrayKategorija);
                i.putExtras(a);
                startActivityForResult(i, staticInteger );

            }
        });

        listaKategorija.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String kategorija = arrayKategorija.get(i);
                Intent myIntent = new Intent(KategorijeAkt.this, ListaKnjigaAkt.class);
                myIntent.putExtra("kategorija", kategorija);
                Bundle bundle1 = new Bundle();
                bundle1.putSerializable("knjige", arrayKnjiga);
                myIntent.putExtras(bundle1);
                startActivityForResult(myIntent, 2);

            }
        });

        --------------------------*/

    }


}

package ba.unsa.etf.rma.edin.s1_17537;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ListaKnjigaAkt extends AppCompatActivity {

    ListView listaKnjiga;
    Button dPovratak;

    //

    CustomAdapterv2 adapter1;

    ArrayList<Knjiga> arrayKnjiga;
    ArrayList<Knjiga> spisak;
    String kategorija;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_knjiga_akt);

        listaKnjiga = (ListView) findViewById(R.id.listaKnjiga);
        dPovratak = (Button) findViewById(R.id.dPovratak);

        spisak = new ArrayList<Knjiga>();
        arrayKnjiga = new ArrayList<Knjiga>();


        Bundle a = this.getIntent().getExtras();
        kategorija = (String) this.getIntent().getExtras().getString("kategorija");
        arrayKnjiga = (ArrayList<Knjiga>) a.getSerializable("knjige");

        for(Knjiga k : arrayKnjiga) {
            if (k.getKategorijaKnjige().equals(kategorija)){
                spisak.add(0, k);
            }
        }

        Toast.makeText(this, "Knjiga u kategoriji: " + spisak.size(), Toast.LENGTH_LONG).show();

        adapter1 = new CustomAdapterv2(this, R.layout.element_liste, spisak);
        listaKnjiga.setAdapter(adapter1);


        /*
        for(int i = 0; i < spisak.size(); i++){
            if(spisak.get(i).getDirnut()){
                if(listaKnjiga.getChildAt(i) != null){
                    listaKnjiga.getChildAt(i).setBackgroundColor(ContextCompat.getColor(this, R.color.brightBlue));
                }
                        // 2-1 ne moze ovdje
        }
        */
        dPovratak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();

                Bundle bundl = new Bundle();
                bundl.putSerializable("Knjige", arrayKnjiga);
                i.putExtras(bundl);

                setResult(RESULT_OK, i);
                finish();
            }
        });

        listaKnjiga.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                listaKnjiga.getChildAt(i).setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.brightBlue));
                // 1-2 ovdje moze
                spisak.get(i).setDirnut(true);
            }
        });

    }
}

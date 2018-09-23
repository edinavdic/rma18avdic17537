package ba.unsa.etf.rma.edin.s1_17537;

import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class DodavanjeKnjigeAkt extends AppCompatActivity {

    Button dNadjiSliku;
    Button dPonisti;
    Button dUpisiKnjigu;
    Spinner sKategorijaKnjige;
    EditText nazivKnjige;
    EditText imeAutora;
    ImageView naslovnaStr;

    ArrayAdapter<String> adapterArray;
    ArrayList<String> arrayKategorija;
    ArrayList<Knjiga> arrayKnjiga;
    Boolean unioSliku;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dodavanje_knjige_akt);


        naslovnaStr = (ImageView) findViewById(R.id.naslovnaStr);
        dNadjiSliku = (Button) findViewById(R.id.dNadjiSliku);
        dPonisti = (Button) findViewById(R.id.dPonisti);
        dUpisiKnjigu = (Button) findViewById(R.id.dUpisiKnjigu);
        sKategorijaKnjige = (Spinner) findViewById(R.id.sKategorijaKnjige);
        nazivKnjige = (EditText) findViewById(R.id.nazivKnjige);
        imeAutora = (EditText) findViewById(R.id.imeAutora);

        unioSliku = false;

        Bundle a = this.getIntent().getExtras();
        arrayKnjiga = (ArrayList<Knjiga>) a.getSerializable("knjige");
        arrayKategorija = this.getIntent().getExtras().getStringArrayList("kategorije"); // uvijek ce biti nesto

        adapterArray = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, arrayKategorija);
        sKategorijaKnjige.setAdapter(adapterArray);

        //

        dUpisiKnjigu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(nazivKnjige.getText().toString().length() == 0 || imeAutora.getText().toString().length() == 0){
                    Toast.makeText(DodavanjeKnjigeAkt.this, "Polja moraju biti ispunjena", Toast.LENGTH_LONG).show();
                    return;
                }
                else if(postojiNaziv(nazivKnjige.getText().toString())){ //isti naziv provjera
                    Toast.makeText(DodavanjeKnjigeAkt.this, "Naziv vec postoji", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(!unioSliku){
                    Toast.makeText(DodavanjeKnjigeAkt.this, "Slika nije odabrana", Toast.LENGTH_SHORT).show();
                    return;
                }

                Knjiga knjiga = new Knjiga
                        (nazivKnjige.getText().toString(), imeAutora.getText().toString(), sKategorijaKnjige.getSelectedItem().toString());

                arrayKnjiga.add(0, knjiga);

                Intent i = new Intent();

                Bundle a = new Bundle();
                a.putSerializable("Knjige", arrayKnjiga);
                i.putExtras(a);

                setResult(RESULT_OK, i);
                Toast.makeText(DodavanjeKnjigeAkt.this, "Knjiga uspjesno dodana", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        dNadjiSliku.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(nazivKnjige.getText().toString().length() == 0){
                    Toast.makeText(DodavanjeKnjigeAkt.this, "Upisite naziv knjige", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(postojiNaziv(nazivKnjige.getText().toString())){
                    Toast.makeText(DodavanjeKnjigeAkt.this, "Naziv vec postoji", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);
                if(i.resolveActivity(getPackageManager()) != null){
                    startActivityForResult(i, 2); // standard
                    //startActivityForResult(Intent.createChooser(i, "Choose photo"), 2);
                }
            }
        });

        dPonisti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setResult(RESULT_CANCELED);
                finish();
            }
        });

    }

    private Boolean postojiNaziv(String str){
        for(Knjiga k : arrayKnjiga)
            if(k.getNaziv().equals(str))
                return true;

        return false;
    }

    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            if(requestCode == 2){
                if(resultCode == RESULT_OK){
                    FileOutputStream outputStream;
                    String naziv = nazivKnjige.getText().toString();
                    try {
                        outputStream = openFileOutput(naziv, Context.MODE_PRIVATE);
                        getBitmapFromUri(data.getData()).compress(Bitmap.CompressFormat.JPEG,90,outputStream);
                        outputStream.close();
                        naslovnaStr.setImageBitmap(BitmapFactory.decodeStream(openFileInput(naziv)));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    unioSliku = true;
                }
            }

    }
}

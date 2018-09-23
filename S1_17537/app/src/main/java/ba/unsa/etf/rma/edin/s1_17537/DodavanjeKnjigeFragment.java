package ba.unsa.etf.rma.edin.s1_17537;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.app.Fragment;
//import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;


public class DodavanjeKnjigeFragment extends Fragment {

    public interface OnFragmentInteractionListener {
        void novaKnjigaSpasi(ArrayList<Knjiga> arrKnjig, Knjiga newBookBaza);
    }

    private OnFragmentInteractionListener mListener;

    Button dNadjiSliku;
    Button dPonisti;
    Button dUpisiKnjigu;
    Spinner sKategorijaKnjige;
    EditText nazivKnjige;
    EditText imeAutora;
    ImageView naslovnaStr;

    Boolean unioSliku;

    private ArrayList<String> arrayKategorije;
    private ArrayList<Knjiga> arrayKnjige;

    ArrayAdapter<String> adapterArray;

    public DodavanjeKnjigeFragment() {
        // Required empty public constructor
    }

    public static DodavanjeKnjigeFragment newInstance(ArrayList<String> arrayKat, ArrayList<Knjiga> arrayKnj) {
        DodavanjeKnjigeFragment fragment = new DodavanjeKnjigeFragment();
        Bundle args = new Bundle();
        args.putStringArrayList("kategorije", arrayKat);
        args.putSerializable("knjige", arrayKnj);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            arrayKategorije = getArguments().getStringArrayList("kategorije");
            arrayKnjige = (ArrayList<Knjiga>) getArguments().getSerializable("knjige");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_dodavanje_knjige, container, false);

        naslovnaStr = (ImageView) rootView.findViewById(R.id.naslovnaStr);
        dNadjiSliku = (Button) rootView.findViewById(R.id.dNadjiSliku);
        dPonisti = (Button) rootView.findViewById(R.id.dPonisti);
        dUpisiKnjigu = (Button) rootView.findViewById(R.id.dUpisiKnjigu);
        sKategorijaKnjige = (Spinner) rootView.findViewById(R.id.sKategorijaKnjige);
        nazivKnjige = (EditText) rootView.findViewById(R.id.nazivKnjige);
        imeAutora = (EditText) rootView.findViewById(R.id.imeAutora);

        unioSliku = false;

        adapterArray = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, arrayKategorije);
        sKategorijaKnjige.setAdapter(adapterArray);

        //

        dUpisiKnjigu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(nazivKnjige.getText().toString().length() == 0 || imeAutora.getText().toString().length() == 0){
                    Toast.makeText(getActivity(), "Polja moraju biti ispunjena", Toast.LENGTH_LONG).show();
                    return;
                }
                else if(postojiNaziv(nazivKnjige.getText().toString())){ //isti naziv provjera
                    Toast.makeText(getActivity(), "Naziv vec postoji", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(!unioSliku){
                    Toast.makeText(getActivity(), "Slika nije odabrana", Toast.LENGTH_SHORT).show();
                    return;
                }

                Knjiga knjiga = new Knjiga
                        (nazivKnjige.getText().toString(), imeAutora.getText().toString(), sKategorijaKnjige.getSelectedItem().toString());

                arrayKnjige.add(0, knjiga);

                mListener.novaKnjigaSpasi(arrayKnjige, knjiga); // toSave

                Toast.makeText(getActivity(), "Knjiga uspjesno dodana", Toast.LENGTH_SHORT).show();

                getActivity().getFragmentManager().popBackStack();
            }
        });

        dNadjiSliku.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(nazivKnjige.getText().toString().length() == 0){
                    Toast.makeText(getActivity(), "Upisite naziv knjige", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(postojiNaziv(nazivKnjige.getText().toString())){
                    Toast.makeText(getActivity(), "Naziv vec postoji", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);
                if(i.resolveActivity(getActivity().getPackageManager()) != null){
                    startActivityForResult(i, 2); // standard
                }
            }
        });

        dPonisti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getActivity().getFragmentManager().popBackStack();
            }
        });

        return rootView;
    }


    private Boolean postojiNaziv(String str){
        for(Knjiga k : arrayKnjige)
            if(k.getNaziv().equals(str))
                return true;

        return false;
    }

    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getActivity().getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 2){
            if(resultCode == RESULT_OK){
                FileOutputStream outputStream;
                String naziv = nazivKnjige.getText().toString();
                try {
                    outputStream = getActivity().openFileOutput(naziv, Context.MODE_PRIVATE);
                    getBitmapFromUri(data.getData()).compress(Bitmap.CompressFormat.JPEG,90,outputStream);
                    outputStream.close();
                    naslovnaStr.setImageBitmap(BitmapFactory.decodeStream(getActivity().openFileInput(naziv)));

                } catch (Exception e) {
                    e.printStackTrace();
                }
                unioSliku = true;
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

}

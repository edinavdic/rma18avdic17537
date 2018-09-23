package ba.unsa.etf.rma.edin.s1_17537;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
//import android.support.v4.app.Fragment;
import android.os.Handler;
import android.os.ResultReceiver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class FragmentOnline extends Fragment implements MojResultReceiver.Receiver, DohvatiKnjige.IDohvatiKnjigeDone, DohvatiNajnovije.IDohvatiNajnovijeDone {

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        switch (resultCode){
            case KnjigePoznanika.STATUS_START:
                //kod
                Toast.makeText(getActivity(), "IntentService zapoceo.", Toast.LENGTH_SHORT).show();
                break;
            case KnjigePoznanika.STATUS_FINISH:
                //kod
                arrayNovihZaPrikazat = (ArrayList<Knjiga>) resultData.getSerializable("result");
                naziviKnjigaNovih.clear();
                for(int i = 0; i < arrayNovihZaPrikazat.size(); i++){
                    naziviKnjigaNovih.add(arrayNovihZaPrikazat.get(i).getNaziv());
                }
                adapterNoveKnjige.notifyDataSetChanged();
                break;
            case KnjigePoznanika.STATUS_ERROR:
                //kod
                Toast.makeText(getActivity(), resultData.getString("error"), Toast.LENGTH_SHORT).show();
                break;

        }
    }

    @Override
    public void onNajnovijeDone(ArrayList<Knjiga> rez) {
        arrayNovihZaPrikazat = rez;
        naziviKnjigaNovih.clear();
        for(int i = 0; i < arrayNovihZaPrikazat.size(); i++){
            naziviKnjigaNovih.add(arrayNovihZaPrikazat.get(i).getNaziv());
        }
        adapterNoveKnjige.notifyDataSetChanged();
    }

    @Override
    public void onDohvatiDone(ArrayList<Knjiga> rez) {
        //arrayNovihZaPrikazat.addAll(rez);
        arrayNovihZaPrikazat = rez;
        naziviKnjigaNovih.clear();
        for(int i = 0; i < arrayNovihZaPrikazat.size(); i++){
            naziviKnjigaNovih.add(arrayNovihZaPrikazat.get(i).getNaziv());
        }
        adapterNoveKnjige.notifyDataSetChanged();
    }

    public interface OnFragmentInteractionListener {

        void novaKnjigaSpasi(ArrayList<Knjiga> arrKnjig, Knjiga newKnjigaBaza);
    }

    private OnFragmentInteractionListener mListener;

    //Ubaciti viewe
    Button dAdd, dRun, dPovratak;
    EditText tekstUpit;
    Spinner sKategorije, sRezultat;



    private ArrayList<String> arrayKategorije;
    private ArrayList<Knjiga> arrayKnjige;
    private ArrayList<Knjiga> arrayNovihZaPrikazat;

    ArrayAdapter<String> adapterArray;
    ArrayAdapter<String> adapterNoveKnjige;

    ArrayList<String> naziviKnjigaNovih;

    MojResultReceiver mReceiver;


    public FragmentOnline() {
        // Required empty public constructor
    }


    public static FragmentOnline newInstance(ArrayList<String> arrayKat, ArrayList<Knjiga> arrayKnj) {
        FragmentOnline fragment = new FragmentOnline();
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
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_fragment_online, container, false);

        dAdd = (Button) rootView.findViewById(R.id.dAdd);
        dRun = (Button) rootView.findViewById(R.id.dRun);
        dPovratak = (Button) rootView.findViewById(R.id.dPovratak);
        tekstUpit = (EditText) rootView.findViewById(R.id.tekstUpit);
        sKategorije = (Spinner) rootView.findViewById(R.id.sKategorije);
        sRezultat = (Spinner) rootView.findViewById(R.id.sRezultat);


        adapterArray = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, arrayKategorije);
        sKategorije.setAdapter(adapterArray);

        arrayNovihZaPrikazat = new ArrayList<Knjiga>(); // IMA I U ONCREATE PROVJERIT SA BEZ nema vise
        //best customadaptera samo sa stringom
        naziviKnjigaNovih = new ArrayList<String>();
        adapterNoveKnjige = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, naziviKnjigaNovih);
        sRezultat.setAdapter(adapterNoveKnjige);



        dPovratak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getFragmentManager().popBackStack();
            }
        });

        dAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Knjiga newBook = null;
                String selektovaniNaziv = sRezultat.getSelectedItem().toString();
                for(int i = 0; i < arrayNovihZaPrikazat.size(); i++){
                    if(arrayNovihZaPrikazat.get(i).getNaziv().equals(selektovaniNaziv)){
                        newBook = arrayNovihZaPrikazat.get(i);
                        break;
                    }
                }
                if(newBook != null){
                    newBook.setKategorijaKnjige(sKategorije.getSelectedItem().toString());
                    arrayKnjige.add(newBook); // u biti i ne treba vracati nazad jer se radi o istom objektu vidis pak
                    if(mListener != null) mListener.novaKnjigaSpasi(arrayKnjige, newBook);
                    Toast.makeText(getActivity(), "Knjiga upisana.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //CHECK ZA UPIS U tekstUpis
                String upisao = tekstUpit.getText().toString();

                if(upisao.contains("autor:")){
                    (new DohvatiNajnovije((DohvatiNajnovije.IDohvatiNajnovijeDone)FragmentOnline.this)).execute(upisao.substring(upisao.indexOf(':')));
                }
                else if(upisao.contains("korisnik:")){
                    String idKorisnika = (upisao.substring(upisao.indexOf(':'))).replace(":", "");
                    Toast.makeText(getActivity(), idKorisnika, Toast.LENGTH_SHORT).show(); /////
                    Intent intent = new Intent(Intent.ACTION_SYNC, null, getActivity(), KnjigePoznanika.class);
                    mReceiver = new MojResultReceiver(new Handler());
                    mReceiver.setReceiver((MojResultReceiver.Receiver)FragmentOnline.this);
                    intent.putExtra("id", idKorisnika);
                    intent.putExtra("receiver", mReceiver);
                    //pokretanje
                    getActivity().startService(intent);
                }
                else{
                    if(!upisao.contains(";"))
                        (new DohvatiKnjige((DohvatiKnjige.IDohvatiKnjigeDone)FragmentOnline.this)).execute(upisao);
                    else{
                        int countWords = upisao.length() - upisao.replace(";", "").length() + 1;
                        String[] nizStringova = new String[countWords];
                        int lastIdx = -1;
                        for(int i = 0; i < countWords; i++){
                            if(i != countWords - 1){
                                nizStringova[i] = upisao.substring(lastIdx + 1, upisao.indexOf(';', lastIdx + 1));
                                lastIdx = upisao.indexOf(';', lastIdx + 1);
                            }
                            else{
                                nizStringova[i] = upisao.substring(lastIdx + 1);
                            }

                        }

                        (new DohvatiKnjige((DohvatiKnjige.IDohvatiKnjigeDone)FragmentOnline.this)).execute(nizStringova);
                    }
                }

            }
        });

        return rootView;
    }




    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            //mListener.onFragmentInteraction(uri);
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

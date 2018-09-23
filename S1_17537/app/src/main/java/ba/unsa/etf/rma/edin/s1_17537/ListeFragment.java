package ba.unsa.etf.rma.edin.s1_17537;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
//import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;



public class ListeFragment extends Fragment {

    public interface OnFragmentInteractionListener {
        void spasiMeUAktivnostKategorija(ArrayList<String> arrKat, ArrayList<Knjiga> arrKnj, String newKategorijaBaza);
        void nosiGaUDodajKnjigu();
        void dajMuKnjigeFragment(Boolean kategorije, String trazeno);
        void nosiGaUFragmentOnline();
    }

    private OnFragmentInteractionListener mListener;

    Button dDodajOnline;
    Button dKategorije;
    Button dAutori;
    Button dPretraga;
    Button dDodajKategoriju;
    Button dDodajKnjigu;
    EditText tekstPretraga;
    ListView listView;

    ArrayAdapter<String> adapterKategorija;
    CustomAdapterAutor adapterAutor;


    ArrayList<String> arrayKategorija;
    ArrayList<Knjiga> arrayKnjiga;
    ArrayList<Knjiga> arrayKnjigaZaPrikaz;



    Boolean kategorije;

    public ListeFragment() {
        // Required empty public constructor
    }


    public static ListeFragment newInstance(ArrayList<String> arrKategorija, ArrayList<Knjiga> arrKnjiga) {
        ListeFragment fragment = new ListeFragment();

        Bundle argumenti = new Bundle();
        argumenti.putStringArrayList("kategorije", arrKategorija);
        argumenti.putSerializable("knjige", arrKnjiga);
        fragment.setArguments(argumenti);

        return fragment;
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            arrayKategorija = getArguments().getStringArrayList("kategorije");
            arrayKnjiga = (ArrayList<Knjiga>) getArguments().getSerializable("knjige");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_liste, container, false);

        dDodajOnline = (Button) rootView.findViewById(R.id.dDodajOnline);

        dAutori = (Button) rootView.findViewById(R.id.dAutori);
        dKategorije = (Button)rootView.findViewById(R.id.dKategorije);
        kategorije = true;
        dPretraga = (Button) rootView.findViewById(R.id.dPretraga);
        dDodajKategoriju = (Button) rootView.findViewById(R.id.dDodajKategoriju);
        dDodajKategoriju.setEnabled(false); // fkt jest false
        dDodajKnjigu = (Button) rootView.findViewById(R.id.dDodajKnjigu);
        tekstPretraga = (EditText) rootView.findViewById(R.id.tekstPretraga);
        listView = (ListView) rootView.findViewById(R.id.listaKategorija);

        arrayKnjigaZaPrikaz = new ArrayList<>(arrayKnjiga);
        for(int i = 0; i < arrayKnjigaZaPrikaz.size(); i++){
            arrayKnjigaZaPrikaz.get(i).setHowmuchAutor(1);
            for(int j = i + 1; j < arrayKnjigaZaPrikaz.size(); j++){
                if(arrayKnjigaZaPrikaz.get(i).getImeAutora().equals(arrayKnjigaZaPrikaz.get(j).getImeAutora())){
                    arrayKnjigaZaPrikaz.remove(j);
                    j--;
                    arrayKnjigaZaPrikaz.get(i).setHowmuchAutor(arrayKnjigaZaPrikaz.get(i).getHowmuchAutor() + 1);
                }
            }
        }

        adapterAutor = new CustomAdapterAutor(getActivity(), R.layout.ele_liste_autor, arrayKnjigaZaPrikaz);
        adapterKategorija = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, arrayKategorija);


        if(kategorije){
            listView.setAdapter(adapterKategorija);
        }
        else{
            listView.setAdapter(adapterAutor);
        }

        dDodajOnline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.nosiGaUFragmentOnline();
            }
        });

        dAutori.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(kategorije){
                    kategorije = false;
                    listView.setAdapter(adapterAutor);
                    dPretraga.setVisibility(View.GONE);
                    tekstPretraga.setVisibility(View.GONE);
                    dDodajKategoriju.setVisibility(View.GONE);
                }
            }
        });

        dKategorije.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!kategorije){
                    kategorije = true;
                    listView.setAdapter(adapterKategorija);
                    dPretraga.setVisibility(View.VISIBLE);
                    tekstPretraga.setVisibility(View.VISIBLE);
                    dDodajKategoriju.setVisibility(View.VISIBLE);
                }
            }
        });

        dPretraga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String upisano = tekstPretraga.getText().toString();
                adapterKategorija.getFilter().filter(upisano, new Filter.FilterListener() {
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
                String newKategorija = tekstPretraga.getText().toString();
                arrayKategorija.add(0,newKategorija);
                tekstPretraga.setText("");
                adapterKategorija.clear();
                adapterKategorija.addAll(arrayKategorija);
                adapterKategorija.notifyDataSetChanged();
                adapterKategorija.getFilter().filter("");
                dDodajKategoriju.setEnabled(false);
                // ne sadrzi
                if(mListener != null && !arrayKategorija.contains(newKategorija)) mListener.spasiMeUAktivnostKategorija(arrayKategorija, arrayKnjiga, newKategorija);

            }
        });

        dDodajKnjigu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               mListener.nosiGaUDodajKnjigu();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String trazeno;
                if(kategorije){
                    trazeno =(String) adapterView.getAdapter().getItem(i);
                    mListener.dajMuKnjigeFragment(true, trazeno);
                }
                else{
                    Knjiga p =(Knjiga) adapterView.getAdapter().getItem(i);
                    trazeno = p.getImeAutora();
                    mListener.dajMuKnjigeFragment(false, trazeno);
                }
            }
        });


        return rootView;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /*
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
public interface OnFragmentInteractionListener {
        void spasiMeUAktivnost(ArrayList<String> arrKat, ArrayList<Knjiga> arrKnj);
    }
    */

}

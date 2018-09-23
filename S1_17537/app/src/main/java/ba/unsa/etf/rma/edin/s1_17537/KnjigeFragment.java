package ba.unsa.etf.rma.edin.s1_17537;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
//import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


public class KnjigeFragment extends Fragment {

    public interface OnFragmentInteractionListener {
        void spasiBoje(ArrayList<Knjiga> arKnj);
    }

    private OnFragmentInteractionListener mListener;


    ListView listaKnjiga;
    Button dPovratak;

    //

    //CustomAdapterv2 adapter1; stari adapter stara spirala
    CustomAdapterv3_s4_1 adapter2;

    ArrayList<Knjiga> arrayKnjiga;
    ArrayList<Knjiga> spisak;
    String trazeno;
    Boolean kategorija;

    public KnjigeFragment() {
        // Required empty public constructor
    }


    public static KnjigeFragment newInstance(Boolean kategorija, ArrayList<Knjiga> arrKnjg, String trazeno) {
        KnjigeFragment fragment = new KnjigeFragment();
        Bundle args = new Bundle();
        args.putString("trazeno", trazeno);
        args.putBoolean("kategorija", kategorija);
        args.putSerializable("knjige", arrKnjg);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            trazeno = getArguments().getString("trazeno");
            kategorija = getArguments().getBoolean("kategorija");
            arrayKnjiga = (ArrayList<Knjiga>) getArguments().getSerializable("knjige");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_knjige, container, false);

        listaKnjiga = (ListView) rootView.findViewById(R.id.listaKnjiga);
        dPovratak = (Button) rootView.findViewById(R.id.dPovratak);
        spisak = new ArrayList<Knjiga>();

        for(Knjiga k : arrayKnjiga) {
            if (kategorija && k.getKategorijaKnjige().equals(trazeno)){
                spisak.add(0, k);
            }
            else if (!kategorija && k.getImeAutora().equals(trazeno)){
                spisak.add(0, k);
            }
        }

        Toast.makeText(getActivity(), "Knjiga u kategoriji: " + spisak.size(), Toast.LENGTH_LONG).show();

        //adapter1 = new CustomAdapterv2(getActivity(), R.layout.element_liste, spisak); stari adapter stara spirala
        adapter2 = new CustomAdapterv3_s4_1(getActivity(), R.layout.element_liste_s4_1, spisak);
        listaKnjiga.setAdapter(adapter2);

        dPovratak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.spasiBoje(arrayKnjiga);
                getActivity().getFragmentManager().popBackStack();
            }
        });

        listaKnjiga.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                listaKnjiga.getChildAt(i).setBackgroundColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.brightBlue));
                spisak.get(i).setDirnut(true);
            }
        });

        return rootView;
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

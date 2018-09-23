package ba.unsa.etf.rma.edin.s1_17537;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Edin on 31.03.2018..
 */

public class CustomAdapterAutor extends ArrayAdapter<Knjiga> {

    private int res;
    private ArrayList<Knjiga> listaK;

    public CustomAdapterAutor(Context context, int resource, List<Knjiga> items){
        super(context, resource, items);

        listaK = new ArrayList<Knjiga>(items);
        res=resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        LinearLayout v;

        if (convertView == null) {
            v = new LinearLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater vi;
            vi = (LayoutInflater)getContext().getSystemService(inflater);
            vi.inflate(res, v, true);
        }
        else {
            v = (LinearLayout)convertView; // samo izmijeniti vrijednost polja
        }

        Knjiga p = getItem(position);

        /*
        int brojK = 1;
        Boolean nalaziSe = false;

        for(int j = position-1; j >= 0; j--){
            Knjiga tmp = getItem(j);
            if(tmp.getImeAutora().equals(p.getImeAutora())) nalaziSe = true;
        }
        if(nalaziSe){
            p.setPonovioSe(true);
            return v;
        }

        for(int i = 0; i < listaK.size(); i++){
            if(listaK.get(i).getImeAutora().equals(p.getImeAutora()) && !listaK.get(i).getBookId().equals(p.getBookId())){
                brojK++;
            }
        }
        */

        if(p != null){

            TextView tViewIme = (TextView) v.findViewById(R.id.eImeAutora);
            TextView tViewBroj = (TextView) v.findViewById(R.id.eBrojKnjiga);

            tViewIme.setText(p.getImeAutora());
            tViewBroj.setText("Broj Knjiga: " + p.getHowmuchAutor());
        }

        return v;
    }

}

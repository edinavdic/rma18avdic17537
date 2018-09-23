package ba.unsa.etf.rma.edin.s1_17537;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.util.List;

import ba.unsa.etf.rma.edin.s1_17537.Knjiga;
import ba.unsa.etf.rma.edin.s1_17537.R;

/**
 * Created by Edin on 31.03.2018..
 */

public class CustomAdapterv2 extends ArrayAdapter<Knjiga> {

    private int res;

    public CustomAdapterv2(Context context, int resource, List<Knjiga> items){
        super(context, resource, items);

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

        if(p != null){

            ImageView iView = (ImageView) v.findViewById(R.id.eNaslovna);
            TextView tView1 = (TextView) v.findViewById(R.id.eNaziv);
            TextView tView2 = (TextView) v.findViewById(R.id.eAutor);

            tView1.setText(p.getNaziv());
            tView2.setText(p.getImeAutora());

            try {
                iView.setImageBitmap(BitmapFactory.decodeStream(getContext().openFileInput(p.getNaziv())));
            }
            catch (FileNotFoundException e) {
                e.printStackTrace();
                Log.d("PORUKA", e.getMessage().toString());
            }

            if(p.getDirnut()){
                v.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.brightBlue));
            }
        }

        return v;
    }

}

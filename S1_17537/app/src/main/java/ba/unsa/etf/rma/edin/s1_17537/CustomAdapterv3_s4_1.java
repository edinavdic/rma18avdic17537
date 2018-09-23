package ba.unsa.etf.rma.edin.s1_17537;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.util.List;

/**
 * Created by Edin on 31.03.2018..
 */

public class CustomAdapterv3_s4_1 extends ArrayAdapter<Knjiga> {


    private int res;
    private Context contxt;


    public CustomAdapterv3_s4_1(Context context, int resource, List<Knjiga> items){
        super(context, resource, items);

        this.contxt = context;
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







        final Knjiga p = getItem(position);

        if(p != null){

            Button btn = (Button) v.findViewById(R.id.dPreporuci);
            ImageView iView = (ImageView) v.findViewById(R.id.eNaslovna);
            TextView tView1 = (TextView) v.findViewById(R.id.eNaziv);
            TextView tView2 = (TextView) v.findViewById(R.id.eAutor);
            TextView tView3 = (TextView) v.findViewById(R.id.eDatumObjavljivanja);
            TextView tView4 = (TextView) v.findViewById(R.id.eOpis);
            TextView tView5 = (TextView) v.findViewById(R.id.eBrojStranica);

            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // kod za email cudesa
                    if(contxt instanceof KategorijeAkt){
                        ((KategorijeAkt)contxt).pokreniMe(p.getNaziv(), p.getImeAutora());
                    }
                }
            });
            tView1.setText(p.getNaziv());
            tView2.setText(p.getImeAutora());
            tView3.setText(p.getDatumObjavljivanja());
            tView4.setText(p.getOpis());
            tView5.setText(String.valueOf(p.getBrojStranica()));
            try {
                if(p.getSlika() != null){
                    Picasso.get().load(p.getSlika().toString()).into(iView);
                }
                else{
                    iView.setImageBitmap(BitmapFactory.decodeStream(getContext().openFileInput(p.getNaziv())));
                }
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

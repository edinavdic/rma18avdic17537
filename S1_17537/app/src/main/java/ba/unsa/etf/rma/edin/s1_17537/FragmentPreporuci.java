package ba.unsa.etf.rma.edin.s1_17537;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.ContactsContract;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.support.v4.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class FragmentPreporuci extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public interface OnFragmentInteractionListener {
        void pokreniMe(String imeKnjige, String imeAutora);
    }
    private OnFragmentInteractionListener mListener;


    private String imeKnjige;
    private String imeAutora;

    private Spinner sKontakti;
    private Button dPosalji;
    private TextView tViewPreview;

    private SimpleCursorAdapter adapter;

    private final static String[] FROM_COLUMNS = {ContactsContract.Data.DATA1};
    private final static int[] TO_IDS = {android.R.id.text1}; // permisije andr 6.0 nadalje

    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;

    private static final String[] PROJECTION = { ContactsContract.Data._ID,
                                                 ContactsContract.Data.DISPLAY_NAME_PRIMARY,
                                                 ContactsContract.Data.DATA1 };


    private static final String SELECTION = ContactsContract.Data.MIMETYPE+" = '"+
            ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE+"'";



    public FragmentPreporuci() {
    }


    public static FragmentPreporuci newInstance(String imeKnjige, String imeAutora) {
        FragmentPreporuci fragment = new FragmentPreporuci();
        Bundle args = new Bundle();
        args.putString("knjiga", imeKnjige);
        args.putString("autor", imeAutora);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            imeKnjige = getArguments().getString("knjiga");
            imeAutora = getArguments().getString("autor");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_fragment_preporuci, container, false);

        dPosalji = (Button) rootView.findViewById(R.id.dPosalji);
        tViewPreview = (TextView) rootView.findViewById(R.id.tViewPreview);
        tViewPreview.setHint("Naziv knjige je: " + imeKnjige + "\nPisac knjige je: " + imeAutora);
        sKontakti = (Spinner) rootView.findViewById(R.id.sKontakti);

        adapter = new android.support.v4.widget.SimpleCursorAdapter(
                getActivity(),android.R.layout.simple_list_item_1,null,FROM_COLUMNS,TO_IDS, 3
        );
        sKontakti.setAdapter(adapter);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && getActivity().checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
        }
        else {
            getLoaderManager().initLoader(0, null, this);
        }


        sKontakti.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Cursor cursor = adapter.getCursor();
                cursor.moveToPosition(i);
                String textView = "Zdravo " + cursor.getString(1) + ",\nPročitaj knjigu " + imeKnjige + " od " + imeAutora + "!";
                tViewPreview.setText(textView);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                String textView ="Mora se neki email selektovati!";
                tViewPreview.setText(textView);
            }
        });

        dPosalji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int i = sKontakti.getSelectedItemPosition();
                Cursor cursor = adapter.getCursor();
                cursor.moveToPosition(i);
                String mail = cursor.getString(2);
                String[] TO = {mail};

                Intent emailIntent=new Intent(Intent.ACTION_SEND);
                emailIntent.setData(Uri.parse("mailto:"));
                emailIntent.setType("text/plain");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "S1_17537 Projekat s4");
                emailIntent.putExtra(Intent.EXTRA_TEXT, tViewPreview.getText().toString());
                try {
                    startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                    getActivity().finish();
                    Log.i("Finished sending email", "Finished sending email");
                }
                catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getActivity(), "There is no email client installed.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        return rootView;
    }


    @Override
    public android.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return  new CursorLoader(
                getActivity(),
                ContactsContract.Data.CONTENT_URI,
                PROJECTION,
                SELECTION,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(android.content.Loader<Cursor> loader, Cursor cursor) { adapter.swapCursor(cursor); }

    @Override
    public void onLoaderReset(android.content.Loader<Cursor> loader) { adapter.swapCursor(null); }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                getLoaderManager().initLoader(0, null, this);
            } else {
                Log.d("LogMsg","Permissions needed");
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

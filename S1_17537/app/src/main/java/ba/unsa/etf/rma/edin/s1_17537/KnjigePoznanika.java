package ba.unsa.etf.rma.edin.s1_17537;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by Edin on 23.05.2018..
 */

public class KnjigePoznanika extends IntentService {

    ArrayList<Knjiga> listaKnjiga;
    ResultReceiver resultReceiver;
    String idKorisnika;


    public static final int STATUS_START = 0;
    public static final int STATUS_FINISH = 1;
    public static final int STATUS_ERROR = 2;

    public KnjigePoznanika(){
        super(null);
    }

    public KnjigePoznanika(String name){
        super(name);
        // posao za konstruktor
        listaKnjiga = new ArrayList<Knjiga>();
        resultReceiver = null;
        idKorisnika = "";
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //prilikom kreiranja
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        // kod ce se izvrsavati u posebnoj niti
        // ovdje sav kod za funkc ide
        resultReceiver = intent.getParcelableExtra("receiver");
        idKorisnika = intent.getStringExtra("id");
        Bundle bundle = new Bundle();

        resultReceiver.send(STATUS_START, Bundle.EMPTY);

        // POSTUPAK
        String query = null;
        try{
            query = URLEncoder.encode(idKorisnika, "utf-8");
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
        String apiAdresa = "https://www.googleapis.com/books/v1/users/" + query + "/bookshelves/6/volumes";
        try{
            URL url = new URL(apiAdresa);

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            int responseCode = urlConnection.getResponseCode();
            if(responseCode == HttpURLConnection.HTTP_OK){
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                String rezultat = convertStreamToString(in);
                JSONObject jo = new JSONObject(rezultat);
                JSONArray items = jo.getJSONArray("items");
                //
                for(int i = 0; i < items.length(); i++){
                    JSONObject item = items.getJSONObject(i);
                    String id = item.getString("id");

                    JSONObject volumeInfo = item.getJSONObject("volumeInfo");

                    String naziv = volumeInfo.getString("title");
                    String opis = volumeInfo.optString("description");
                    String datumObjavljivanja = volumeInfo.optString("publishedDate");
                    int brojStranica = volumeInfo.optInt("pageCount", -1);
                    URL slika = null;

                    ArrayList<Autor> autori = new ArrayList<Autor>();
                    JSONArray autoriJSON = volumeInfo.optJSONArray("authors");

                    if(autoriJSON != null){
                        String author = autoriJSON.getString(0);
                        Autor autor = new Autor(author, id);
                        autori.add(autor);
                        if(autoriJSON.length() > 1){
                            author = autoriJSON.getString(1);
                            autor = new Autor(author, id);
                            autori.add(autor);
                        }
                    }
                    else{
                        autori.add(new Autor("NULL AUTOR", id));
                    }


                    listaKnjiga.add(new Knjiga(id, naziv, autori, opis, datumObjavljivanja, slika, brojStranica));
                }

                bundle.putSerializable("result", listaKnjiga);
                resultReceiver.send(STATUS_FINISH, bundle);

            }
            else{
                bundle.putString("error", "nije vraceno HTTP_OK");
                resultReceiver.send(STATUS_ERROR, bundle);
            }


        }catch (MalformedURLException e){
            e.printStackTrace();
        }catch (IOException e1){
            e1.printStackTrace();
        }catch (JSONException e2){
            e2.printStackTrace();
        }
    }

    public String convertStreamToString(InputStream is){
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try{
            while((line = reader.readLine()) != null){
                sb.append(line + "\n");
            }
        }catch (IOException e){

        }finally {
            try{
                is.close();
            }catch (IOException e){

            }
        }
        return  sb.toString();
    }

}

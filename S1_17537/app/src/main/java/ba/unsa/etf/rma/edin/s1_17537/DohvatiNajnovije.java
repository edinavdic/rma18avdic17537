package ba.unsa.etf.rma.edin.s1_17537;

import android.os.AsyncTask;

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
 * Created by Edin on 22.05.2018..
 */

public class DohvatiNajnovije extends AsyncTask<String, Integer, Void> {

    public interface IDohvatiNajnovijeDone{
        public void onNajnovijeDone(ArrayList<Knjiga> rez);
    }


    private DohvatiNajnovije.IDohvatiNajnovijeDone interfejs;
    private ArrayList<Knjiga> petpopetKnjiga;


    public DohvatiNajnovije(DohvatiNajnovije.IDohvatiNajnovijeDone itf){
        petpopetKnjiga = new ArrayList<Knjiga>();
        this.interfejs = itf;
    }



    @Override
    protected Void doInBackground(String... params) {

        for(int k = 0; k < params.length; k++){

            String query = null;
            try{
                query = URLEncoder.encode(params[k], "utf-8");
            }catch (UnsupportedEncodingException e){
                e.printStackTrace();
            }
            String apiAdresa = "https://www.googleapis.com/books/v1/volumes?q=inauthor" + query + "&orderBy=newest&maxResults=5";
            try{
                URL url = new URL(apiAdresa);

                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                //int responseCode = urlConnection.getResponseCode();
                //if(responseCode == HttpURLConnection.HTTP_OK)
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

                    //fotkica
                    URL slika = null;
                    JSONObject imageLinks = volumeInfo.optJSONObject("imageLinks");
                    String urlThumbnail = null;
                    if(imageLinks != null) urlThumbnail = imageLinks.optString("thumbnail");
                    if(urlThumbnail != null) slika = new URL(urlThumbnail);
                    //slika = new URL(volumeInfo.optJSONObject("imageLinks").optString("thumbnail"));

                    ArrayList<Autor> autori = new ArrayList<Autor>();
                    JSONArray autoriJSON = volumeInfo.optJSONArray("authors");

                    //for(int j = 0; j < autoriJSON.length(); j++)
                    if(autoriJSON != null){ //  1 AUTOR
                        String author = autoriJSON.getString(0);
                        Autor autor = new Autor(author, id);
                        autori.add(autor);
                        /*
                        if(autoriJSON.length() > 1){
                            author = autoriJSON.getString(1);
                            autor = new Autor(author, id);
                            autori.add(autor);
                        }
                        */
                    }
                    else{
                        autori.add(new Autor("NULL AUTOR", id));
                    }


                    petpopetKnjiga.add(new Knjiga(id, naziv, autori, opis, datumObjavljivanja, slika, brojStranica));
                }


            }catch (MalformedURLException e){
                e.printStackTrace();
            }catch (IOException e1){
                e1.printStackTrace();
            }catch (JSONException e2){
                e2.printStackTrace();
            }

        }

        return null;
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

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        interfejs.onNajnovijeDone(petpopetKnjiga);
    }
}

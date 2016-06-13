package ipleiria.pt.mymusicapp2016;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private ArrayList<String> musics;
    private MediaPlayer mediaPlayer;
    private ArrayList<String> link_music;
    //atributo da classe.
    private AlertDialog alerta;
    private static final int SELECTED_PICTURE = 20;
    private ImageView imgPicture;


    private void exemplo_simples(final int position) {
        //Cria o gerador do AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //define o titulo
        builder.setTitle(R.string.Delete_album);
        //define a mensagem
        builder.setMessage(R.string.Aviso_Delete);
        //define um botão como positivo

        builder.setPositiveButton(R.string.Ok_Aviso_Delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.teclado);
                mediaPlayer.setLooping(true);
                mediaPlayer.start();
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.setLooping(false);
                }
                Toast.makeText(MainActivity.this, R.string.Toast_deleted, Toast.LENGTH_SHORT).show();

                musics.remove(position);

                SimpleAdapter adapter = createSimpleAdapter(musics);
                //ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, musics);

                ListView listView = (ListView) findViewById(R.id.listView_musics);
                listView.setAdapter(adapter);

            }

        });

        //define um botão como negativo.
        builder.setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.teclado);
                mediaPlayer.setLooping(true);
                mediaPlayer.start();
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.setLooping(false);
                }

                Toast.makeText(MainActivity.this, R.string.Toast_Canceled, Toast.LENGTH_SHORT).show();
            }
        });
        //cria o AlertDialog
        alerta = builder.create();
        //Exibe
        alerta.show();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imgPicture = (ImageView) findViewById(R.id.imgPicture);

        SharedPreferences sp = getSharedPreferences("appMusics", 0);
        Set<String> musicsSet = sp.getStringSet("musicsKey", new HashSet<String>());
        Set<String> youtubeSet = sp.getStringSet("youtubeKey", new HashSet<String>());

        musics = new ArrayList<String>(musicsSet);
        Collections.sort(musics);

//        musics.add("Paulo Sousa| • Todos Os Dias| • Farol Múica, Lda.| • 2016| • 4 stars");
//        musics.add("António Variações - Anjo da Guarda \n• 1988 - 5 stars •");
//        musics.add("Deolinda - Mundo Pequenino \n• 2013 - 3 stars •");
//        musics.add("FF - Estou Aqui \n• 2006 - 4 stars •");
//        musics.add("Jorge Palma - Voo Nocturno \n• 1980 - 4 stars •");
//        musics.add("Pedro Abrunhosa - Luz \n• 2007 - 5 stars •");
//        musics.add("Rui Veloso - Ar de Rock \n• 1980 - 3 stars •");
//        musics.add("Tiago Bettencourt - Do Princípio \n• Universal Music Argentina 2014 - 3 stars •");n

        //Código da ListViews
        //Mudar o imagem da aplicação

        SimpleAdapter adapter = createSimpleAdapter(musics);
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, musics);

        ListView listView = (ListView) findViewById(R.id.listView_musics);
        listView.setAdapter(adapter);

        link_music = new ArrayList<String>(youtubeSet);

//        link_music.add("https://www.youtube.com/watch?v=wJAHg-vSeaw");



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String video = link_music.get(position);

                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(video)));


                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, R.string.URL_Invalid, Toast.LENGTH_SHORT).show();
                }
            }
        });

        Spinner spinner = (Spinner) findViewById(R.id.spinner_search);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapters = ArrayAdapter.createFromResource(this, R.array.spinner_options, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapters.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapters);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                exemplo_simples(position);
                mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.teclado);
                mediaPlayer.setLooping(true);
                mediaPlayer.start();
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.setLooping(false);
                }
                // código que é executado quando se clica num item da lista.
                // não é preciso saber ao promenor aquilo que este código faz, até pq é complexo.

                return true;
                //atualizar a lista sem o contacto
            }
        });
    }

    public void onImageGalleryClicked(View v){
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);

        File pictureDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_ALARMS);
        String pictureDirectoryPath = pictureDirectory.getPath();

        Uri data = Uri.parse(pictureDirectoryPath);

        photoPickerIntent.setDataAndType(data, "image/*");

        startActivityForResult(photoPickerIntent, SELECTED_PICTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if(requestCode == SELECTED_PICTURE){

                Uri imageUri = data.getData();

                InputStream inputStream;

                try{
                    inputStream = getContentResolver().openInputStream(imageUri);

                    Bitmap image = BitmapFactory.decodeStream(inputStream);

                    imgPicture.setImageBitmap(image);
                } catch (FileNotFoundException e){
                    e.printStackTrace();

                    Toast.makeText(this, "Unable to open image", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    public void onClick_search(View view) {
        // ir buscar as referências para a editText, o spinner e a ListView.
        EditText et = (EditText) findViewById(R.id.editText_search);
        Spinner sp = (Spinner) findViewById(R.id.spinner_search);
        ListView lv = (ListView) findViewById(R.id.listView_musics);

        mediaPlayer = MediaPlayer.create(MainActivity.this,R.raw.teclado);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
        if(mediaPlayer.isPlaying()){
            mediaPlayer.setLooping(false);
        }

        // criar uma nova lista, que guarde os albuns pesquisados.
        ArrayList<String> searchMusics = new ArrayList<>();

        // percorrer todas as informações e adicionar os que contêm o termo a pesquisar à nova lista
        String termo = et.getText().toString();

        String selectedItem = (String) sp.getSelectedItem();

        if (termo.equals("")) { // a editText está vazia?
            // mostro todos as informações.
            SimpleAdapter adapter = createSimpleAdapter(musics);
//            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, musics);
            lv.setAdapter(adapter);

            Toast.makeText(MainActivity.this, R.string.Toast1, Toast.LENGTH_SHORT).show();
        } else { // a editText não está vazia

            if (selectedItem.equals(getString(R.string.All_main))) {
                for (String c : musics) { // para cada c em musics
                    if (c.contains(termo)) { // se o c contiver o termo
                        searchMusics.add(c); //adicionar o c à lista searchContacts.
                    }
                }
            } else if (selectedItem.equals(getString(R.string.Artist_main))) {
                // pesquisa pelo artista
                for (String c : musics) {
                    String[] split = c.split("\\|");
                    String artist = split[0];
                    artist = artist.trim();

                    if (artist.contains(termo)) {
                        searchMusics.add(c);
                    }
                }
            } else if (selectedItem.equals(getString(R.string.Album_main))) {
                // pesquisa pelo album
                for (String c : musics) {
                    String[] split = c.split("\\|");
                    String album = split[1];
                    album = album.trim();

                    if (album.contains(termo)) {
                        searchMusics.add(c);
                    }
                }
            }else if (selectedItem.equals(getString(R.string.Editor_main))) {
                // pesquisa pela idade
                for (String c : musics) {
                    String[] split = c.split("\\|");
                    String year = split[2];
                    year = year.trim();

                    if (year.contains(termo)) {
                        searchMusics.add(c);
                    }
                }
            }else if (selectedItem.equals(getString(R.string.Year_main))) {
                // pesquisa pela idade
                for (String c : musics) {
                    String[] split = c.split("\\|");
                    String year = split[3];
                    year = year.trim();


                    if (year.contains(termo)) {
                        searchMusics.add(c);
                    }
                }
            }else if (selectedItem.equals(getString(R.string.Stars_main))) {
                // pesquisa pelas estrelas
                for (String c : musics) {
                    String[] split = c.split("\\|");
                    String stars = split[4];
                    stars = stars.trim();

                    if (stars.contains(termo)) {
                        searchMusics.add(c);
                    }
                }
            }

            boolean vazia = searchMusics.isEmpty();

            if (!vazia) { // vazia == false || se a lista não estiver vazia
                // mostrar o conteúdo da lista de albuns pesquisados na listViews
                SimpleAdapter adapter = createSimpleAdapter(searchMusics);
//                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, searchMusics);
                lv.setAdapter(adapter);

                Toast.makeText(MainActivity.this, R.string.Toast2, Toast.LENGTH_SHORT).show();
            } else { // lista de resultados está vazia.

//                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, musics);
                SimpleAdapter adapter = createSimpleAdapter(musics);

                lv.setAdapter(adapter);

                Toast.makeText(MainActivity.this, R.string.Toast3, Toast.LENGTH_SHORT).show();
            }
        }
    }

    //Mudar o imagem da aplicação
    private SimpleAdapter createSimpleAdapter(ArrayList<String> musics) {
        List<HashMap<String, String>> simpleAdapterData = new ArrayList<HashMap<String, String>>();

        for (String c : musics) {
            HashMap<String, String> hashMap = new HashMap<>();

            String[] split = c.split("\\|");

            hashMap.put("artist", split[0].trim());
            hashMap.put("album", split[1].trim());
            hashMap.put("editor", split[2].trim());
            hashMap.put("year", split[3].trim());
            hashMap.put("stars", split[4].trim());

            simpleAdapterData.add(hashMap);
        }

        String[] from = {"artist", "album", "editor", "year", "stars"};
        int[] to = {R.id.textView_artist, R.id.textView_album, R.id.textView_editor, R.id.textView_year, R.id.textView_ratingBar};
        SimpleAdapter simpleAdapter = new SimpleAdapter(getBaseContext(), simpleAdapterData, R.layout.listview_item, from, to);
        return simpleAdapter;
    }
    @Override
    protected void onStop() {
        super.onStop();

        //Toast.makeText(MainActivity.this, "A guardar contactos.", Toast.LENGTH_SHORT).show();

        // o 0 é para ficar em privado
        SharedPreferences sp = getSharedPreferences("appMusics", 0);
        SharedPreferences.Editor edit = sp.edit();

        // Guardar as coisas em memória

        // Mete os contactos num conjunto porque ele é uma lista e isso daria problemas ao putStringSet
        HashSet musicsSet = new HashSet(musics);
        HashSet youtubeSet = new HashSet(link_music);

        //Colocar o conjunto e criar uma chave que nos lembremos
        edit.putStringSet("musicsKey", musicsSet);
        edit.putStringSet("youtubeKey", youtubeSet);

        // Para armazenar lá as coisas
        edit.commit();
    }

    public void onClick_add(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        mediaPlayer = MediaPlayer.create(MainActivity.this,R.raw.teclado);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
        if(mediaPlayer.isPlaying()){
            mediaPlayer.setLooping(false);
        }

        // Get the layout inflater
        LayoutInflater inflater = this.getLayoutInflater();
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(inflater.inflate(R.layout.music_add, null));
        // Add the buttons
        // Adicionar dois botões, um positivo e outro negativo
        builder.setPositiveButton(R.string.Ok_button, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                // Obter referência para as editTexts
                // Fazer o cast de um diálogo "genérico"
                // Para um alertdialog
                AlertDialog al = (AlertDialog) dialog;

                EditText etArtist = (EditText) al.findViewById(R.id.editText_artist);
                EditText etAlbum = (EditText) al.findViewById(R.id.editText_album);
                EditText etEditor = (EditText) al.findViewById(R.id.editText_editor);
                EditText etYear = (EditText) al.findViewById(R.id.editText_year);
                RatingBar star = (RatingBar) al.findViewById(R.id.ratingBar);
                EditText etLink = (EditText) al.findViewById(R.id.editText_link);


                mediaPlayer = MediaPlayer.create(MainActivity.this,R.raw.teclado);
                mediaPlayer.setLooping(true);
                mediaPlayer.start();
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.setLooping(false);
                }

                // Obter as informações das editTexts
                String artist = etArtist.getText().toString();
                String music = etAlbum.getText().toString();
                String editor = etEditor.getText().toString();
                String year = etYear.getText().toString();
                int rating = (int)star.getRating();
                String video = etLink.getText().toString();


                // Criar um novo album.
                String newMusic = artist + "|" + " • " + music + "|" +editor+ "|" + " • " +year+ "|" + " • " +rating+ " stars";

                // adicionar o novo album.
                // anti-bugs
                if (!artist.isEmpty() && !music.isEmpty() && !editor.isEmpty() && !year.isEmpty() && !video.isEmpty()) {
                    musics.add(newMusic);
                    link_music.add(video);

                    Toast.makeText(MainActivity.this, R.string.Toast_Created, Toast.LENGTH_SHORT).show();
                    //}else if (!artist.isEmpty() || !music.isEmpty() || !editor.isEmpty() || !year.isEmpty()) {
                    //  musics.add(newMusic);
                    //Toast.makeText(MainActivity.this, "Created", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, R.string.Toast_No_Created, Toast.LENGTH_SHORT).show();
                }


                // dizer à listView para se actualizar
                ListView lv = (ListView) findViewById(R.id.listView_musics);

                SimpleAdapter adapter = createSimpleAdapter(musics);
            //ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, musics);

                lv.setAdapter(adapter);

                //Toast.makeText(MainActivity.this, "Confirmed", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton(R.string.Button_Cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                mediaPlayer = MediaPlayer.create(MainActivity.this,R.raw.teclado);
                mediaPlayer.setLooping(true);
                mediaPlayer.start();
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.setLooping(false);
                }
                // User cancelled the dialog
                Toast.makeText(MainActivity.this, R.string.Toast_Cancel, Toast.LENGTH_SHORT).show();
            }
        });

        // Set other dialog properties
        builder.setTitle(R.string.Add_Album_title);

        // Create the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_exit) {
            System.exit(0);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
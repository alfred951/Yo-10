package co.edu.eafit.yomas10;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import co.edu.eafit.yomas10.Clases.Equipo;
import co.edu.eafit.yomas10.Clases.Jugador;
import co.edu.eafit.yomas10.MainActivity;
import co.edu.eafit.yomas10.R;

/**
 * Activity con la informacion del equipo que lo haya llamado
 */
public class EquipoActivity extends AppCompatActivity {

    private final Context ctx = this;
    private TextView capitan;
    private ListView listaJugadores;
    private ArrayList<String> nombreJugadores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipo);

        setTitle(getIntent().getExtras().getString("NOMBRE"));

        capitan = (TextView) findViewById(R.id.capitanTV);
        listaJugadores = (ListView) findViewById(R.id.listaJugadores);

        capitan.setText(MainActivity.equipo.getCapitan().getNombre());

        for (int i = 0; i<MainActivity.jugadores.size();i++){
            nombreJugadores.add(MainActivity.jugadores.get(i).getNombre());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, nombreJugadores);



        listaJugadores.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_equipo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
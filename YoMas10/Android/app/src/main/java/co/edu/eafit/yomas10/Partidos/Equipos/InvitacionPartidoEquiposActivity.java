package co.edu.eafit.yomas10.Partidos.Equipos;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import co.edu.eafit.yomas10.R;

/**
 * Activity con la informacion del partido que la llamo
 */
public class InvitacionPartidoEquiposActivity extends AppCompatActivity {
    //TODo meter el lugar

    private TextView fecha, equipo1, equipo2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitacion_partido);

        fecha = (TextView) findViewById(R.id.fecha);
        equipo1 = (TextView) findViewById(R.id.equipo1);
        equipo2 = (TextView) findViewById(R.id.equipo2);

        fecha.setText(getIntent().getExtras().getString("FECHA"));
        equipo1.setText(getIntent().getExtras().getString("EQUIPO1"));
        equipo2.setText(getIntent().getExtras().getString("EQUIPO2"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_partido, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public void aceptarPartido(View view){
        //Aqui se debe suscribir al canal del partido
        //TODO
    }

    public void rechazarPartido(View viw){
        //TODO
    }
}

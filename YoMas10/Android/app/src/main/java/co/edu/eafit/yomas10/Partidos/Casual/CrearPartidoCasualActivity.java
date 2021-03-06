package co.edu.eafit.yomas10.Partidos.Casual;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import co.edu.eafit.yomas10.Jugador.Jugador;
import co.edu.eafit.yomas10.Jugador.SeleccionarAmigosActivity;
import co.edu.eafit.yomas10.Main2Activity;
import co.edu.eafit.yomas10.MyApplication;
import co.edu.eafit.yomas10.R;
import co.edu.eafit.yomas10.Util.Connection.Http;
import co.edu.eafit.yomas10.Util.Connection.HttpBridge;
import co.edu.eafit.yomas10.Util.ParseNotificationSender;
import co.edu.eafit.yomas10.Util.Connection.Receiver;

public class CrearPartidoCasualActivity extends AppCompatActivity implements Receiver {

    private static TextView fechaPartido, horaPartido;
    private EditText canchaPartido;
    private ListView jugadoresLV;
    private ArrayList<Jugador> jugadores;
    private ArrayList<String> nuevosJugadores;
    private ArrayAdapter<Jugador> mAdapter;
    private final static int REQUEST_AMIGOS = 1;

    private PartidoCasual partido;
    private Jugador user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_partido);

        user = ((MyApplication)getApplicationContext()).getUser();

        fechaPartido = (TextView) findViewById(R.id.fechaPartido);
        horaPartido = (TextView) findViewById(R.id.horaPartido);
        canchaPartido = (EditText) findViewById(R.id.canchaPartido);

        jugadoresLV = (ListView) findViewById(R.id.jugadores);

        jugadores = new ArrayList<>();
        jugadores.add(((MyApplication)getApplicationContext()).getUser());

        mAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, jugadores);
        //jugadoresLV.setEmptyView((TextView) findViewById(R.id.emptyText));


        Button btn = new Button(getApplicationContext());
        btn.setText("Agregar Jugadores");
        jugadoresLV.addFooterView(btn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getApplicationContext(), SeleccionarAmigosActivity.class);
                startActivityForResult(in, REQUEST_AMIGOS);

            }
        });
        jugadoresLV.setAdapter(mAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            nuevosJugadores = data.getExtras().getStringArrayList("JUGADORES");

            for (String jugador: nuevosJugadores){
                jugadores.add(((MyApplication)getApplicationContext()).getUser().findAmigo(jugador));
            }
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_crear_partido, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.crearPartido){
            partido = new PartidoCasual(fechaPartido.getText().toString(),
                    horaPartido.getText().toString(),canchaPartido.getText().toString(), jugadores);
            user.agregarPartido(partido);

            updateDB();
            //updateParticipantes();


            //finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public void updateDB(){
        HashMap<String, String> map = new HashMap<>();
        map.put("fecha", fechaPartido.getText().toString());
        map.put("hora", horaPartido.getText().toString());
        map.put("cancha", canchaPartido.getText().toString());

        try{
            startService(HttpBridge.startWorking(this, map, this, Http.PARTIDO));
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
    }

    public void updateParticipantes(){
        for (int i = 0; i < partido.getIntegrantes().size(); i++) {
            HashMap<String, String> map = new HashMap<>();
            map.put("nickname", partido.getIntegrantes().get(i).getUsername());

            try {
                startService(HttpBridge.startWorking(this, map, this, Http.PARTICIPANTES));
            }catch (UnsupportedEncodingException e){
                e.printStackTrace();
            }
        }
    }

    public void elegirFecha(View view){
        DialogFragment selFecha = new DatePickerFragment();
        selFecha.show(getFragmentManager(), "datePicker");
    }

    public void elegirHora(View view) throws UnsupportedEncodingException {
        DialogFragment selHora = new TimePickerFragment();
        selHora.show(getFragmentManager(), "timePicker");
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        try {
            JSONArray jsonArray = new JSONArray(resultData.getString("GetResponse"));
            JSONObject json = jsonArray.getJSONObject(0);
            if (json.has("fecha"))
                partido.setId(json.getInt("idPartido"));
        }catch (JSONException e){
            e.printStackTrace();
        }

        for (Jugador jugador: jugadores){
            try {
                ParseNotificationSender.sendCasualGameInvitation(jugador.getUsername(),
                        fechaPartido.getText().toString(), horaPartido.getText().toString(),
                        canchaPartido.getText().toString(), jugadores, partido.getId());
            }catch (JSONException e){
                Log.e("PARSE NOTIFICATION", "Error enviado la notificacion");
            }
        }
        Toast.makeText(getApplicationContext(), "Se han invitado los jugadores", Toast.LENGTH_LONG).show();


        //((MyApplication)getApplicationContext()).getUser().agregarPartido(partido);
        startActivity(new Intent(this, Main2Activity.class));
    }

    //Clases para elegir fecha y hora

    public static class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

        private int hora, minuto;

        @Override
        public TimePickerDialog onCreateDialog(Bundle savedInstace){
            final Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minutes = calendar.get(Calendar.MINUTE);

            return new TimePickerDialog(getActivity(), this, hour, minutes, DateFormat.is24HourFormat(getActivity()));
        }

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            horaPartido.setText(hourOfDay + ":" + minute + ":00");
            hora = hourOfDay;
            minuto = minute;
        }

        public int getHora() {
            return hora;
        }

        public int getMinuto(){
            return minuto;
        }
    }

    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener{

        private int year, month, day;

        @Override
        public Dialog onCreateDialog(Bundle savedInstance){
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            fechaPartido.setText(year + "-" + monthOfYear + "-" + dayOfMonth);
            this.year = year;
            this.month = monthOfYear;
            this.day = dayOfMonth;
        }

        public int getYear() {
            return year;
        }

        public int getMonth() {
            return month;
        }

        public int getDay() {
            return day;
        }
    }
}

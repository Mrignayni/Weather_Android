

package com.mrig.mrignayni.weatherwow;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
//import android.support.v7.app.ActionBarActivity;

//import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class WeatherActivity extends ActionBarActivity implements WeatherFragment.OnDataPass {
    //Typeface weatherFont;
    double temperatureC;
    double temperatureF;
    //private static Criteria searchProviderCriteria = new Criteria();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        //getActionBar().setDisplayUseLogoEnabled(Boolean.TRUE);

        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new WeatherFragment())
                    .commit();
        }


//        else {
//            JSONSearchTask task = new JSONSearchTask();
//            task.execute(new Location[]{loc});
//        }




//        TextView weatherIcon;// = (TextView) findViewById(R.id.weather_icon);
//        weatherFont = Typeface.createFromAsset(getAssets(), "fonts/weather.ttf");
//        weatherIcon = (TextView)findViewById(R.id.weather_icon);
//
//        weatherIcon.setTypeface(weatherFont);

//       weatherFont = Typeface.createFromAsset(getActivity().getAssets(), "fonts/weather.ttf");
//        updateWeatherData(new CityPreference(getActivity()).getCity());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_weather, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        //int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(item.getItemId() == R.id.change_city){
            showInputDialog();
        }
        return false;

        //return super.onOptionsItemSelected(item);
    }


    public void tempOnClick(View v)
    {
        try{



            //WeatherFragment fragment = (WeatherFragment) getSupportFragmentManager().findFragmentById(R.id.container);
            //temperatureC = fragment.temperatureC;
            //temperatureF = 32 + (temperatureC * 9 / 5);
            //View rootView = this.getLayoutInflater().inflate(R.layout.fragment_weather, , false);

            TextView tempTV = (TextView)findViewById(R.id.current_temperature_field);


           if(tempTV.getText().charAt(tempTV.getText().length()-1)=='F')
            //{

                tempTV.setText(String.format("%.2f", temperatureC+ "°C"));
            //}
            else
            //{{
           {
               Toast a = Toast.makeText(getApplicationContext(), Double.toString(temperatureF), Toast.LENGTH_LONG);
               //a.show();
               tempTV.setText(String.format("%.2f", temperatureF + "°F"));

           }
            //}
            Toast t = Toast.makeText(getApplicationContext(), "TextView On Click!", Toast.LENGTH_LONG);
            t.show();

      }
        catch(Exception e)
        {

        }
    }
    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_weather, container, false);
            return rootView;
        }
    }

    private void showInputDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Change city");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        builder.setPositiveButton("Go", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                changeCity(input.getText().toString());
            }
        });
        builder.show();
    }

    public void changeCity(String city){
        WeatherFragment wf = (WeatherFragment)getFragmentManager()
                .findFragmentById(R.id.container);
        wf.changeCity(city);
        new CityPreference(this).setCity(city);
    }

    @Override
    public void onDataPass(double temp) {
        temperatureC=temp;
        temperatureF=32.00 + (temperatureC * 9.00 / 5.00);
        //Toast t = Toast.makeText(getApplicationContext(), "Data Passed from fragment to Activity " + Double.toString(temp), Toast.LENGTH_LONG);
        //t.show();
    }


}

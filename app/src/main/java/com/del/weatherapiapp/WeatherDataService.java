package com.del.weatherapiapp;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.util.ArrayList;
import java.util.List;

public class WeatherDataService {

    public static final String QUERY_FOR_CITY_ID = "http://api.openweathermap.org/data/2.5/weather?q=";
    public static final String QUERY_FOR_CITY_WEATHER_BY_ID = "https://api.openweathermap.org/data/2.5/forecast?id=";
    public static final String QUERY_FOR_DAILY_HOURLY = "&exclude=hourly,daily";
    public static final String API_ID = "&APPID=eb0101684f70485506c3e15b9b64d204";
    Context context;
    String cityID;
    String cityName;

    public WeatherDataService(Context context) {
        this.context = context;
    }

    public interface VolleyResponseListener {

        void onError(String message);

        void onResponse(String cityID);
    }


    public void getCityID(String cityName, VolleyResponseListener volleyResponseListener) {
        String url = QUERY_FOR_CITY_ID + cityName + API_ID;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                cityID = "";
                try {
                    cityID = response.getString("id");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //Toast.makeText(context, "City ID: " + cityID, Toast.LENGTH_SHORT).show();
                volleyResponseListener.onResponse(cityID);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(context, "something error", Toast.LENGTH_SHORT).show();
                volleyResponseListener.onError("Something wrong");

            }
        });
        MySingleton.getInstance(context).addToRequestQueue(request);

        //return cityID;

    }


    public interface ForeCastByIDResponse {

        void onError(String message);

        void onResponse(List<WeatherRepotModel> weatherRepotModels);
    }
    public interface ForeCastByNameResponse {

        void onError(String message);

        void onResponse(List<WeatherRepotModel> weatherRepotModels);
    }

    public void getCityForecastByID(String cityID, ForeCastByIDResponse foreCastByIDResponse) {
        List<WeatherRepotModel> report = new ArrayList<>();
        String url = QUERY_FOR_CITY_WEATHER_BY_ID + cityID + QUERY_FOR_DAILY_HOURLY + API_ID;
        //get json object
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url,null,  new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONArray weather_list = response.getJSONArray("list");


                    for (int i = 0; i < weather_list.length(); i++) {
                        //get first item in Array
                        WeatherRepotModel fistHour = new WeatherRepotModel();


                        JSONObject weatherList = (JSONObject) weather_list.get(i);
                        JSONArray weatherArray = (JSONArray) weatherList.get("weather");
                        String datum = (String) weatherList.get("dt_txt");
                        JSONObject firstHourFromAPI = (JSONObject) weatherArray.get(0);


                        fistHour.setId(firstHourFromAPI.getInt("id"));
                        fistHour.setMain(firstHourFromAPI.getString("main"));
                        fistHour.setDescription(firstHourFromAPI.getString("description"));
                        fistHour.setDatum(datum);
                        report.add(fistHour);
                    }
                    foreCastByIDResponse.onResponse(report);
                } catch (JSONException e) {
                   e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "error something wrong", Toast.LENGTH_SHORT).show();

            }
        });
        MySingleton.getInstance(context).addToRequestQueue(request);

    }
    public  void  getCityForecastByName( String cityName,final ForeCastByNameResponse foreCastByNameResponse) {
      getCityID(cityName, new VolleyResponseListener() {
          @Override
          public void onError(String message) {

          }

          @Override
          public void onResponse(String cityID) {

              getCityForecastByID(cityID, new ForeCastByIDResponse() {
                  @Override
                  public void onError(String message) {

                  }

                  @Override
                  public void onResponse(List<WeatherRepotModel> weatherRepotModels) {

                      foreCastByNameResponse.onResponse(weatherRepotModels);
                  }
              });
          }
      });
    }

}

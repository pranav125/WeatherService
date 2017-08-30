import net.aksingh.owmjapis.CurrentWeather;
import net.aksingh.owmjapis.OpenWeatherMap;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.zip.GZIPInputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

class OpenWeatherMapApi extends OpenWeatherMap{

    public OpenWeatherMapApi(Units units, Language lang, String apiKey) {
      super(units,lang,apiKey);

    }
    @Override
    public CurrentWeather currentWeatherByCityCode(long cityCode) throws JSONException {

        return super.currentWeatherByCityCode(cityCode);
    }

    public String currentWeatherHistoryByCityName(String cityName)  {
        try {

            String requestString = super.getOwmAddressInstance().currentWeatherByCityName(cityName);

            requestString.replace("2.5/","2.5/history/city/");

            return httpGET(requestString);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    return "woohoo";
    }

    private String httpGET(String requestAddress) {
        URL request;
        HttpURLConnection connection = null;
        BufferedReader reader = null;

        String s;
        String response = null;

        try {
            request = new URL(requestAddress);
            connection = (HttpURLConnection) request.openConnection();

            connection.setRequestMethod("GET");
            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(false);
            connection.setRequestProperty("Accept-Encoding", "gzip, deflate");
            connection.connect();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                String encoding = connection.getContentEncoding();

                try {
                    if (encoding != null && encoding.equalsIgnoreCase("gzip")) {
                        reader = new BufferedReader(new InputStreamReader(new GZIPInputStream(connection.getInputStream())));
                    } else if (encoding != null && encoding.equalsIgnoreCase("deflate")) {
                        reader = new BufferedReader(new InputStreamReader(new InflaterInputStream(connection.getInputStream(), new Inflater(true))));
                    } else {
                        reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    }

                    while ((s = reader.readLine()) != null) {
                        response = s;
                    }
                } catch (IOException e) {
                    System.err.println("Error: " + e.getMessage());
                } finally {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (IOException e) {
                            System.err.println("Error: " + e.getMessage());
                        }
                    }
                }
            } else { // if HttpURLConnection is not okay
                try {
                    reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                    while ((s = reader.readLine()) != null) {
                        response = s;
                    }
                } catch (IOException e) {
                    System.err.println("Error: " + e.getMessage());
                } finally {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (IOException e) {
                            System.err.println("Error: " + e.getMessage());
                        }
                    }
                }

                // if response is bad
                System.err.println("Bad Response: " + response + "\n");
                return null;
            }
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
            response = null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }

        return response;
    }
}

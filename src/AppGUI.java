import org.json.simple.JSONObject;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class AppGUI extends JFrame {

    private JSONObject weatherData;
    public AppGUI() {
        super("Weather");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(450, 600);
        setBackground(Color.black);
        setLocationRelativeTo(null); // open in center of the screen
        setResizable(false); // fixed size
        setLayout(null);

        addGUIComponents();
    }

    private void addGUIComponents() {
        //Search bar
        JTextField searchTextField = new JTextField();
        //Search bar dimensions
        searchTextField.setBounds(new Rectangle(15, 15, 250, 60));
        //Search bar text
        searchTextField.setFont(new Font("Dialog", Font.BOLD, 24));
        add(searchTextField);

        //Weather image
        JLabel weatherImage = new JLabel(loadImage("D:\\GitHub\\WeatherApp\\WeatherApp\\src\\assets\\cloudy.png"));
        weatherImage.setBounds(0, 125, 450, 217);
        add(weatherImage);

        //Temperature text
        JLabel tempText = new JLabel("10 C");
        tempText.setBounds(0, 350, 450,54);
        tempText.setFont(new Font("Dialog", Font.BOLD, 40));
        //Center the text
        tempText.setHorizontalAlignment(SwingConstants.CENTER);
        add(tempText);

        JLabel weatherCondition = new JLabel("Cloudy");
        weatherCondition.setBounds(0, 390, 450,40);
        weatherCondition.setHorizontalAlignment(SwingConstants.CENTER);
        weatherCondition.setFont(new Font("Dialog", Font.PLAIN, 40));
        add(weatherCondition);

        JLabel humidityImage = new JLabel(loadImage("D:\\GitHub\\WeatherApp\\WeatherApp\\src\\assets\\humidity.png"));
        humidityImage.setBounds(15, 450, 66, 66);
        add(humidityImage);

        JLabel humidityText = new JLabel("<html><b>Humidity</b> 100%</html>");
        humidityText.setBounds(90, 450, 85, 55);
        humidityText.setFont(new Font("Dialog", Font.PLAIN, 16));
        add(humidityText);

        JLabel windspeed = new JLabel(loadImage("D:\\GitHub\\WeatherApp\\WeatherApp\\src\\assets\\windspeed.png"));
        windspeed.setBounds(220, 450, 74, 66);
        add(windspeed);

        JLabel windSpeedText = new JLabel("<html><b>Windspeed</b> 25km/h</html>");
        windSpeedText.setBounds(310, 450, 100, 55);
        windSpeedText.setFont(new Font("Dialog", Font.PLAIN, 16));
        add(windSpeedText);


        //Search Button
        JButton button = new JButton(loadImage("D:\\GitHub\\WeatherApp\\WeatherApp\\src\\assets\\search.png"));
        //chage cursor to a hand cursor when hovering over button
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setBounds(300, 15, 60, 60);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // get location from user
                String userInput = searchTextField.getText();
                if(userInput.replaceAll("\\s", "").length() == 0) {
                    return;
                }
                weatherData = WeatherApp.getWeatherData(userInput);
                String weatherConditons = (String) weatherData.get("weather_condition");

                switch (weatherConditons) {
                    case "Clear":
                        weatherImage.setIcon(loadImage("D:\\GitHub\\WeatherApp\\WeatherApp\\src\\assets\\clear.png"));
                        break;
                    case "Cloudy":
                        weatherImage.setIcon(loadImage("src/assets/cloudy.png"));
                        break;
                    case "Rain":
                        weatherImage.setIcon(loadImage("src/assets/rain.png"));
                        break;
                    case "Snow":
                        weatherImage.setIcon(loadImage("src/assets/snow.png"));
                        break;
                }

                double temperature = (double) weatherData.get("temperature");
                tempText.setText(temperature + "C");

                weatherCondition.setText(weatherConditons);

                long humidity = (long) weatherData.get("humidity");
                double guiTemp = (double) weatherData.get("temperature");
                tempText.setText(guiTemp + "C");

                weatherCondition.setText(weatherConditons);

                long guiHumidity = (long) weatherData.get("humidity");
                humidityText.setText("<html><b>Humidity</b> " + guiHumidity + "hm/h</html>");

            }
        });
        add(button);

    }

    private ImageIcon loadImage(String path) {
        try {
            //read the image file from the path
            BufferedImage image = ImageIO.read(new File(path));
            return new ImageIcon(image);
        } catch (IOException e ){
            e.printStackTrace();
        }
        System.out.println("Could not find resource");
        return null;
    }
}

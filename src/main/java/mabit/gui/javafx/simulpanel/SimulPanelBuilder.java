package mabit.gui.javafx.simulpanel;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import org.apache.log4j.Logger;

import java.io.IOException;

/**
 * Created by martin on 6/9/2016.
 */
public class SimulPanelBuilder {
    private static final Logger Log = Logger.getLogger(SimulPanelBuilder.class);

    BorderPane simulPane;

   /* Parent root = null; */

    public SimulPanelBuilder() throws IOException {

        FXMLLoader loader = new FXMLLoader(SimulPanelBuilder.class.getResource("simulpanel.fxml"));
        simulPane = loader.load();
//        controller.setSimulDispatcher(dispatcher);
         Log.info("SimulPanel loaded");
 //       controler.addStopCallBack(stopButt);
    }

    public Parent getPanel() {
        return simulPane;
    }

}

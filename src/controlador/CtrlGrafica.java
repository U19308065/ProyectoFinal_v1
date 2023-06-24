package controlador;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JTextField;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class CtrlGrafica extends Thread {

    public JTextField cajaTemp;

    float celsius = 0;

    final XYSeries Serie = new XYSeries("Temperatura Celsius");
    final XYSeriesCollection Coleccion = new XYSeriesCollection();
    JFreeChart Grafica;
    int i = 0;

    public CtrlGrafica(JTextField cajaTemp) {
        this.cajaTemp = cajaTemp;
    }

    @Override
    public void run() {
        while (true) {
            try {
                i++;
                celsius = Float.parseFloat(cajaTemp.getText());
                Serie.add(i, celsius);
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(CtrlGrafica.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public void Inigraf() {
        Serie.add(0, 0);
        Coleccion.addSeries(Serie);
        Grafica = ChartFactory.createXYLineChart("Temperatura Celsius vs. Tiempo", "Tiempo", "Temperatura Celsius",
                Coleccion, PlotOrientation.VERTICAL, true, true, false);
    }

    public void Grafica() {
        ChartPanel panel = new ChartPanel(Grafica);
        JFrame Ventana = new JFrame("Celsius");
        Ventana.getContentPane().add(panel);
        Ventana.pack();
        Ventana.setVisible(true);
        Ventana.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
}

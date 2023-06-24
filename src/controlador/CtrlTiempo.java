package controlador;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextField;

public class CtrlTiempo extends Thread {

    private JTextField cajaFecha, cajaHora, cajaDiaSemana;

    public CtrlTiempo(JTextField cajaFecha, JTextField cajaHora, JTextField cajaDiaSemana) {
        this.cajaFecha = cajaFecha;
        this.cajaHora = cajaHora;
        this.cajaDiaSemana = cajaDiaSemana;
    }
    @Override
    public void run() {
        while (true) 
       try {
            Calendar cal = new GregorianCalendar();
            Calendar datetime = Calendar.getInstance();
            //Para los dias de la semana
            String[] strDays = new String[]{"domingo", "lunes", "martes", "miercoles", "jueves",
                "viernes", "sabado"};
            String wd;
            wd = strDays[datetime.get(Calendar.DAY_OF_WEEK) - 1];
            
           // Formatear la fecha y hora
           SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
           String formattedDate = dateFormat.format(cal.getTime());
           SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
           String formattedTime = timeFormat.format(cal.getTime());
            
            String diaSem = wd;
            this.cajaFecha.setText(formattedDate);
            this.cajaHora.setText(formattedTime);
            this.cajaDiaSemana.setText(diaSem);
            Thread.sleep(1000);          
        } catch (InterruptedException ex) {
            Logger.getLogger(CtrlTiempo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

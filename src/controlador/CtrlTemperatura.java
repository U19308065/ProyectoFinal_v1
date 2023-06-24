package controlador;

import com.panamahitek.ArduinoException;
import com.panamahitek.PanamaHitek_Arduino;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextField;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import modelo.Conexion;
import vista.frmMenuPrincipal;


public class CtrlTemperatura {

    PanamaHitek_Arduino arduinouno = new PanamaHitek_Arduino();

    public JTextField cajaTemp;
    public JTextField cajaTiempo;
    public JTextField cajaFecha;
    public JTextField cajaEstado;

    float celsius = 0;
    String fechaReg = "";
    String horaInicio = "";
    String horaFinal = "";
    String tempMax = "";
    String tempMin = "";
    String Num_prog = "";
    int flag_1 = 1;
    int flag_2 = 1;

    PreparedStatement ps;
    ResultSet rs;
    Connection con = null;
    Conexion conectar = new Conexion();

    public CtrlTemperatura(JTextField cajaTemp, JTextField cajaTiempo, JTextField cajaFecha, JTextField cajaEstado) {
        this.cajaTemp = cajaTemp;
        this.cajaTiempo = cajaTiempo;
        this.cajaFecha = cajaFecha;
        this.cajaEstado = cajaEstado;
    }

    SerialPortEventListener listener = new SerialPortEventListener() {
        @Override
        public void serialEvent(SerialPortEvent spe) {

            try {
                if (arduinouno.isMessageAvailable() == true) {//Si llega un valor
                    celsius = Float.parseFloat(arduinouno.printMessage());//transformo un string a float
                    cajaTemp.setText(String.format("%.2f", celsius));

                    //verificar fecha y hora para encendido
                    //----------------------------
                    String campo1 = cajaTiempo.getText();
                    String campo2 = cajaFecha.getText();
                    String campo3 = cajaTemp.getText();

                    String where = "";
                    if (!"".equals(campo1)) {
                        where = "WHERE HoraIni = '" + campo1 + "'" + " AND Fecha = '" + campo2 + "'";
                    }
                    String sql = "SELECT * FROM tab_registro " + where;
                    con = conectar.getConnection();
                    try {
                        ps = con.prepareStatement(sql);
                        rs = ps.executeQuery();
                        String datos[];
                        datos = new String[6];
                        if (rs.next()) {
                            datos[0] = rs.getString("fecha");
                            datos[1] = rs.getString("HoraIni");
                            datos[2] = rs.getString("HoraFin");
                            datos[3] = rs.getString("TmpMax");
                            datos[4] = rs.getString("TmpMin");
                            datos[5] = rs.getString("Num_prog");
                            flag_1 = 0;
                        }

                        if (flag_1 == 0) {
                            fechaReg = datos[0];
                            horaInicio = datos[1];
                            horaFinal = datos[2];
                            tempMax = datos[3];
                            tempMin = datos[4];
                            Num_prog = datos[5];
                            flag_1 = 1;
                        }

                    } catch (SQLException ex) {
                        Logger.getLogger(CtrlTemperatura.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    // Control de encendido y apagado del sistema segun fecha y hora
                    //Tambien se envia los parametros de temperatura maxima y minima
                    if (cajaTiempo.getText().equals(horaInicio) && flag_2 == 1) {
                        flag_2 = 0;
                        sql = "insert into tab_encendido(Num_prog,FechaEnc,Hora,TmpIni)values(?,?,?,?)";
                        try {
                            con = conectar.getConnection();
                            ps = con.prepareStatement(sql);
                            ps.setString(1, Num_prog);
                            ps.setString(2, campo2);
                            ps.setString(3, campo1);
                            ps.setString(4, campo3);
                            ps.executeUpdate();
                        } catch (SQLException e) {
                        }

                        String valorMax = tempMax;
                        String valorMin = tempMin;
                        System.out.println("encendido");
                        cajaEstado.setText("ON");
                        try {
                            arduinouno.sendData("Max" + valorMax + "Min" + valorMin);
                        } catch (ArduinoException | SerialPortException ex) {
                            Logger.getLogger(frmMenuPrincipal.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    if (cajaTiempo.getText().equals(horaFinal) && flag_2 == 0) {
                        flag_2 = 1;
                        sql = "insert into tab_apagado(Num_prog,FechaApa,Hora,TmpFin)values(?,?,?,?)";
                        try {
                            con = conectar.getConnection();
                            ps = con.prepareStatement(sql);
                            ps.setString(1, Num_prog);
                            ps.setString(2, campo2);
                            ps.setString(3, campo1);
                            ps.setString(4, campo3);
                            ps.executeUpdate();

                        } catch (SQLException e) {
                        }

                        System.out.println("apagado");
                        cajaEstado.setText("OFF");
                        try {
                            arduinouno.sendData("Max" + 99 + "Min" + 0);
                        } catch (ArduinoException | SerialPortException ex) {
                            Logger.getLogger(frmMenuPrincipal.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            } catch (SerialPortException | ArduinoException ex) {
                Logger.getLogger(CtrlTemperatura.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    };

    public void Puerto() {
        try {
            arduinouno.arduinoRXTX("COM6", 9600, listener);
        } catch (ArduinoException ex) {
            Logger.getLogger(CtrlTemperatura.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}

package controlador;

import modelo.RegistroProg;
import modelo.ConsultaRegistroProg;
import vista.frmRegProgCRUD;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class Ctrl implements ActionListener {

    ConsultaRegistroProg dao = new ConsultaRegistroProg();
    RegistroProg re = new RegistroProg();
    frmRegProgCRUD vista = new frmRegProgCRUD();
    DefaultTableModel modelo = new DefaultTableModel();

    @SuppressWarnings("LeakingThisInConstructor")
    public Ctrl(frmRegProgCRUD v) {
        this.vista = v;
        this.vista.btnListar.addActionListener(this);
        this.vista.btnAgregar.addActionListener(this);
        this.vista.btnEditar.addActionListener(this);
        this.vista.btnDelete.addActionListener(this);
        this.vista.btnActualizar.addActionListener(this);
        this.vista.btnNuevo.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.btnListar) {
            limpiarTabla();
            listar(vista.tabla);
            nuevo();
        }
        if (e.getSource() == vista.btnAgregar) {
            add();
            listar(vista.tabla);
            nuevo();
        }
        if (e.getSource() == vista.btnEditar) {
            int fila = vista.tabla.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(vista, "Debe seleccionar una fila..!!");
            } else {
                String numProg = (String) vista.tabla.getValueAt(fila, 0);
                String fecha = (String) vista.tabla.getValueAt(fila, 1);
                String horaIni = (String) vista.tabla.getValueAt(fila, 2);
                String horaFin = (String) vista.tabla.getValueAt(fila, 3);
                String tmpMax = (String) vista.tabla.getValueAt(fila, 4);
                String tmpMin = (String) vista.tabla.getValueAt(fila, 5);
                String CodUsu = (String) vista.tabla.getValueAt(fila, 6);

                vista.txtNum_prog.setText("" + numProg);
                //inicio para la fecha con jcalendar
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Date fecha2 = dateFormat.parse(fecha);
                    vista.fechajc.setDate(fecha2);
                } catch (ParseException ex) {
                    Logger.getLogger(Ctrl.class.getName()).log(Level.SEVERE, null, ex);
                }
                //Inicio para la hora con combobox
                String[] partesI = horaIni.split(":");
                String horaI = partesI[0];
                String minutoI = partesI[1];
                vista.cbHoraIni.setSelectedItem(horaI);
                vista.cbMinutoIni.setSelectedItem(minutoI);

                String[] partesF = horaFin.split(":");
                String horaF = partesF[0];
                String minutoF = partesF[1];
                vista.cbHoraFin.setSelectedItem(horaF);
                vista.cbMinutoFin.setSelectedItem(minutoF);

                vista.cbTempMax.setSelectedItem(tmpMax);
                vista.cbTempMin.setSelectedItem(tmpMin);
                vista.cbUsuario.setSelectedItem(CodUsu);

            }
        }
        if (e.getSource() == vista.btnActualizar) {
            Actualizar();
            listar(vista.tabla);
            nuevo();
        }
        if (e.getSource() == vista.btnDelete) {
            delete();
            listar(vista.tabla);
            nuevo();
        }
        if (e.getSource() == vista.btnNuevo) {
            nuevo();
        }
    }

    void nuevo() {
        vista.txtNum_prog.setText("");
        //inicio para la fecha con jcalendar - Hoy
        Date fechaHoy = new Date();
        vista.fechajc.setDate(fechaHoy);

        //inicio para la hora combobox
        vista.cbHoraIni.setSelectedItem("00");
        vista.cbMinutoIni.setSelectedItem("00");
        vista.cbHoraFin.setSelectedItem("00");
        vista.cbMinutoFin.setSelectedItem("00");

        vista.cbTempMax.setSelectedItem("0");
        vista.cbTempMin.setSelectedItem("0");
        vista.cbUsuario.setSelectedItem("U000000001");

        vista.fechajc.requestFocus();
    }

    public void delete() {
        int fila = vista.tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(vista, "Debe Seleccionar una Fila...!!!");
        } else {
            String numProg = (String) vista.tabla.getValueAt(fila, 0);
            dao.Delete(numProg);
            JOptionPane.showMessageDialog(vista, "Programa Eliminado...!!!");
        }
        limpiarTabla();
    }

    public void add() {

        //inicio para la fecha con jcalendar
        Date fechaCrudo = vista.fechajc.getDate();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String fecha = dateFormat.format(fechaCrudo);
        //inicio para la hora con combobox
        String horaI = (String) vista.cbHoraIni.getSelectedItem();
        String minutoI = (String) vista.cbMinutoIni.getSelectedItem();
        String horaIni = horaI + ":" + minutoI;

        String horaF = (String) vista.cbHoraFin.getSelectedItem();
        String minutoF = (String) vista.cbMinutoFin.getSelectedItem();
        String horaFin = horaF + ":" + minutoF;

        String tmpMax = (String) vista.cbTempMax.getSelectedItem();
        String tmpMin = (String) vista.cbTempMin.getSelectedItem();
        String CodUsu = (String) vista.cbUsuario.getSelectedItem();

        re.setFecha(fecha);
        re.setHoraIni(horaIni);
        re.setHoraFin(horaFin);
        re.setTmpMax(tmpMax);
        re.setTmpMin(tmpMin);
        re.setCod_Usu(CodUsu);

        int r = dao.agregar(re);
        if (r == 1) {
            JOptionPane.showMessageDialog(vista, "Programa Agregado con Exito.");
        } else {
            JOptionPane.showMessageDialog(vista, "Error");
        }
        limpiarTabla();
    }

    public void Actualizar() {
        if (vista.txtNum_prog.getText().equals("")) {
            JOptionPane.showMessageDialog(vista, "No se Identifica el Numero de programa, debe selecionar la opcion Editar");
        } else {
            String numProg = vista.txtNum_prog.getText();
            //inicio para la fecha con jcalendar
            Date fechaCrudo = vista.fechajc.getDate();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String fecha = dateFormat.format(fechaCrudo);
            //inicio para la Hora con combobox
            String horaI = (String) vista.cbHoraIni.getSelectedItem();
            String minutoI = (String) vista.cbMinutoIni.getSelectedItem();
            String horaIni = horaI + ":" + minutoI;

            String horaF = (String) vista.cbHoraFin.getSelectedItem();
            String minutoF = (String) vista.cbMinutoFin.getSelectedItem();
            String horaFin = horaF + ":" + minutoF;

            String tmpMax = (String) vista.cbTempMax.getSelectedItem();
            String tmpMin = (String) vista.cbTempMin.getSelectedItem();
            String CodUsu = (String) vista.cbUsuario.getSelectedItem();

            re.setNum_prog(numProg);
            re.setFecha(fecha);
            re.setHoraIni(horaIni);
            re.setHoraFin(horaFin);
            re.setTmpMax(tmpMax);
            re.setTmpMin(tmpMin);
            re.setCod_Usu(CodUsu);

            int r = dao.Actualizar(re);
            if (r == 1) {
                JOptionPane.showMessageDialog(vista, "Programa Actualizado con Exito.");
            } else {
                JOptionPane.showMessageDialog(vista, "Error");
            }
        }
        limpiarTabla();
    }

    public void listar(JTable tabla) {
        centrarCeldas(tabla);
        modelo = (DefaultTableModel) tabla.getModel();
        tabla.setModel(modelo);
        List<RegistroProg> lista = dao.listar();
        Object[] objeto = new Object[7];
        for (int i = 0; i < lista.size(); i++) {
            objeto[0] = lista.get(i).getNum_prog();
            objeto[1] = lista.get(i).getFecha();
            objeto[2] = lista.get(i).getHoraIni();
            objeto[3] = lista.get(i).getHoraFin();
            objeto[4] = lista.get(i).getTmpMax();
            objeto[5] = lista.get(i).getTmpMin();
            objeto[6] = lista.get(i).getCod_Usu();
            modelo.addRow(objeto);
        }
        tabla.setRowHeight(25);
        tabla.setRowMargin(5);
    }

    void centrarCeldas(JTable tabla) {
        DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();
        tcr.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < vista.tabla.getColumnCount(); i++) {
            tabla.getColumnModel().getColumn(i).setCellRenderer(tcr);
        }
    }

    void limpiarTabla() {
        for (int i = 0; i < vista.tabla.getRowCount(); i++) {
            modelo.removeRow(i);
            i = i - 1;
        }
    }
}

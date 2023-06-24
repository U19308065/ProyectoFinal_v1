package controlador;

import modelo.RegistroProg;
import modelo.ConsultaRegistroProg;
import vista.frmRegProgCRUD;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
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
                vista.txtFecha.setText(fecha);
                vista.txtHoraIni.setText(horaIni);
                vista.txtHoraFin.setText(horaFin);
                vista.txtTmpMax.setText(tmpMax);
                vista.txtTmpMin.setText(tmpMin);
                vista.txtUsuario.setText(CodUsu);
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
        vista.txtFecha.setText("");
        vista.txtHoraFin.setText("");
        vista.txtHoraIni.setText("");
        vista.txtTmpMax.setText("");
        vista.txtTmpMin.setText("");
        vista.txtUsuario.setText("");
        vista.txtFecha.requestFocus();
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
        String fecha = vista.txtFecha.getText();
        String horaIni = vista.txtHoraIni.getText();
        String horaFin = vista.txtHoraFin.getText();
        String tmpMax = vista.txtTmpMax.getText();
        String tmpMin = vista.txtTmpMin.getText();
        String CodUsu = vista.txtUsuario.getText();

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
            String fecha = vista.txtFecha.getText();
            String horaIni = vista.txtHoraIni.getText();
            String horaFin = vista.txtHoraFin.getText();
            String tmpMax = vista.txtTmpMax.getText();
            String tmpMin = vista.txtTmpMin.getText();
            String CodUsu = vista.txtUsuario.getText();

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

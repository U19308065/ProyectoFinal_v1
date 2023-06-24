package modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ConsultaRegistroProg {

    PreparedStatement ps;
    ResultSet rs;
    Connection con;
    Conexion conectar = new Conexion();

    public List listar() {
        List<RegistroProg> datos = new ArrayList<>();
        try {
            con = conectar.getConnection();
            ps = con.prepareStatement("select * from tab_registro");
            rs = ps.executeQuery();
            while (rs.next()) {
                RegistroProg reg = new RegistroProg();
                reg.setNum_prog(rs.getString(1));
                reg.setFecha(rs.getString(2));
                reg.setHoraIni(rs.getString(3));
                reg.setHoraFin(rs.getString(4));
                reg.setTmpMax(rs.getString(5));
                reg.setTmpMin(rs.getString(6));
                reg.setCod_Usu(rs.getString(7));
                datos.add(reg);
            }
        } catch (Exception e) {
        }
        return datos;
    }

    public int agregar(RegistroProg rgt) {
        int r = 0;
        String sql = "insert into tab_registro(Fecha,HoraIni,HoraFin,TmpMax,TmpMin,Cod_Usu)values(?,?,?,?,?,?)";
        try {
            con = conectar.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, rgt.getFecha());
            ps.setString(2, rgt.getHoraIni());
            ps.setString(3, rgt.getHoraFin());
            ps.setString(4, rgt.getTmpMax());
            ps.setString(5, rgt.getTmpMin());
            ps.setString(6, rgt.getCod_Usu());

            r = ps.executeUpdate();
            if (r == 1) {
                return 1;
            } else {
                return 0;
            }
        } catch (Exception e) {
        }
        return r;
    }

    public int Actualizar(RegistroProg rgt) {
        int r = 0;
        String sql = "update tab_registro set Fecha=?,HoraIni=?,HoraFin=?,TmpMax=?,TmpMin=?,Cod_Usu=? where Num_prog=?";
        try {
            con = conectar.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, rgt.getFecha());
            ps.setString(2, rgt.getHoraIni());
            ps.setString(3, rgt.getHoraFin());
            ps.setString(4, rgt.getTmpMax());
            ps.setString(5, rgt.getTmpMin());
            ps.setString(6, rgt.getCod_Usu());
            ps.setString(7, rgt.getNum_prog());
            r = ps.executeUpdate();
            if (r == 1) {
                return 1;
            } else {
                return 0;
            }
        } catch (Exception e) {
        }
        return r;
    }

    public int Delete(String num_prog) {
        int r = 0;
        String sql = "delete from tab_registro where Num_prog=" + "'" + num_prog + "'";
        try {
            con = conectar.getConnection();
            ps = con.prepareStatement(sql);
            r = ps.executeUpdate();
        } catch (Exception e) {
        }
        return r;
    }
}

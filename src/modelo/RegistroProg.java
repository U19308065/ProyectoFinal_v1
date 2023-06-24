package modelo;

public class RegistroProg {
    String Num_prog;
    String Fecha;
    String HoraIni;
    String HoraFin;
    String tmpMax;
    String tmpMin;
    String Cod_Usu;

    public RegistroProg() {
    }
    public RegistroProg(String Num_prog, String Fecha, String HoraIni, 
            String HoraFin, String tmpMax, String tmpMin, String Cod_Usu) {
        this.Num_prog = Num_prog;
        this.Fecha = Fecha;
        this.HoraIni = HoraIni;
        this.HoraFin = HoraFin;
        this.tmpMax = tmpMax;
        this.tmpMin = tmpMin;
        this.Cod_Usu = Cod_Usu;
    }

    public String getNum_prog() {
        return Num_prog;
    }

    public void setNum_prog(String Num_prog) {
        this.Num_prog = Num_prog;
    }

    public String getFecha() {
        return Fecha;
    }

    public void setFecha(String Fecha) {
        this.Fecha = Fecha;
    }

    public String getHoraIni() {
        return HoraIni;
    }

    public void setHoraIni(String HoraIni) {
        this.HoraIni = HoraIni;
    }

    public String getHoraFin() {
        return HoraFin;
    }

    public void setHoraFin(String HoraFin) {
        this.HoraFin = HoraFin;
    }

    public String getTmpMax() {
        return tmpMax;
    }

    public void setTmpMax(String tmpMax) {
        this.tmpMax = tmpMax;
    }

    public String getTmpMin() {
        return tmpMin;
    }

    public void setTmpMin(String tmpMin) {
        this.tmpMin = tmpMin;
    }

    public String getCod_Usu() {
        return Cod_Usu;
    }

    public void setCod_Usu(String Cod_Usu) {
        this.Cod_Usu = Cod_Usu;
    }
}

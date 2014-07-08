package clinicaaudinsa.audiologia.businessdomain;

public class TipoExamen {
	public long getIdTipoExamen() {
		return idTipoExamen;
	}
	public void setIdTipoExamen(long idTipoExamen) {
		this.idTipoExamen = idTipoExamen;
	}
	public String getNombreExamen() {
		return nombreExamen;
	}
	public void setNombreExamen(String nombreExamen) {
		this.nombreExamen = nombreExamen;
	}
	public String getInstrucciones() {
		return instrucciones;
	}
	public TipoExamen()
	{
	}
	public TipoExamen(long idTipoExamen, String nombreExamen,
			String instrucciones) {
		super();
		this.idTipoExamen = idTipoExamen;
		this.nombreExamen = nombreExamen;
		this.instrucciones = instrucciones;
	}
	public void setInstrucciones(String instrucciones) {
		this.instrucciones = instrucciones;
	}
	private long idTipoExamen;
	private String nombreExamen;
	private String instrucciones;


}

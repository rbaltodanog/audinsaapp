package clinicaaudinsa.audiologia.businessdomain;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Mensaje {
	private long id_mensaje;
	
	private boolean leido;
	
	private Date fecha_mensaje;
	
	private String texto;
	
	public long getId_mensaje() {		
		return id_mensaje;
	}

	public void setId_mensaje(long id_mensaje) {
		this.id_mensaje = id_mensaje;
	}

	public boolean isLeido() {
		return leido;
	}

	public void setLeido(boolean leido) {
		this.leido = leido;
	}

	public Date getFecha_mensaje() {
		return fecha_mensaje;
	}
	
	public String getFecha_mensajeAsString()
	{
		SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.US);
		return df.format(getFecha_mensaje());
	}

	public void setFecha_mensaje(Date fecha_mensaje) {
		this.fecha_mensaje = fecha_mensaje;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}
}

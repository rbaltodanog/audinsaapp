package audinsa.audiologia.businessdomain;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Perfil {
	private int idPerfil;
	private String nombre;
	private Date fechaNacimiento;
	private String correoElectronico;

	public long getIdPerfil() {
		return idPerfil;
	}
	public Perfil()
	{
	}
	public Perfil(int idPerfil, String nombre, Date fechaNacimiento,
			String correoElectronico) {
		super();
		this.idPerfil = idPerfil;
		this.nombre = nombre;
		this.fechaNacimiento = fechaNacimiento;
		this.correoElectronico = correoElectronico;
	}
	public void setIdPerfil(int idPerfil) {
		this.idPerfil = idPerfil;
	}
	public String getNombre() {
		return nombre.trim();
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public Date getFechaNacimiento() {
		return fechaNacimiento;
	}
	public String getFechaNacimientoText() {
		SimpleDateFormat dtFormat = new SimpleDateFormat("d/M/yyyy", Locale.US);
		return dtFormat.format(fechaNacimiento).trim();
	}
	public void setFechaNacimiento(Date fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}
	public String getCorreoElectronico() {
		return correoElectronico.trim();
	}
	public void setCorreoElectronico(String correoElectronico) {
		this.correoElectronico = correoElectronico;
	}

	@Override
	public String toString()
	{
		return nombre;
	}
}

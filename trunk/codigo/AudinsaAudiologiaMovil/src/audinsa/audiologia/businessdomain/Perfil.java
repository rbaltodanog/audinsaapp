package audinsa.audiologia.businessdomain;

import java.util.Date;

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
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public Date getFechaNacimiento() {
		return fechaNacimiento;
	}
	public void setFechaNacimiento(Date fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}
	public String getCorreoElectronico() {
		return correoElectronico;
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

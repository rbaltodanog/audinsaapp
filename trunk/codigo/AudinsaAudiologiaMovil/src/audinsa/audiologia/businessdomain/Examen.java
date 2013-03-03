package audinsa.audiologia.businessdomain;

import java.util.Date;

public class Examen {


	private int id_examen;
	private int id_tipo_examen;
	private Date fecha_inicio;
	private int  duracion_aproximada;
	public double porcentaje_completado;



	public Examen()
	{
	}
	public Examen(int id_examen,int id_tipo_examen,Date fecha_inicio,
			int duracion_aproximada,double porcentaje_completado) {
		super();
		this.id_examen = id_examen;
		this.id_tipo_examen = id_tipo_examen;
		this.fecha_inicio = fecha_inicio;
		this.duracion_aproximada = duracion_aproximada;
		this.porcentaje_completado = porcentaje_completado;
	}


	public long getId_examen() {
		return id_examen;
	}
	public void setId_examen(int id_examen) {
		this.id_examen = id_examen;
	}
	public long getId_tipo_examen() {
		return id_tipo_examen;
	}
	public void setId_tipo_examen(int id_tipo_examen) {
		this.id_tipo_examen = id_tipo_examen;
	}
	public Date getFecha_inicio() {
		return fecha_inicio;
	}
	public void setFecha_inicio(Date fecha_inicio) {
		this.fecha_inicio = fecha_inicio;
	}
	public int getDuracion_aproximada() {
		return duracion_aproximada;
	}
	public void setDuracion_aproximada(int duracion_aproximada) {
		this.duracion_aproximada = duracion_aproximada;
	}
	public double getPorcentaje_completado() {
		return porcentaje_completado;
	}
	public void setPorcentaje_completado(double porcentaje_completado) {
		this.porcentaje_completado = porcentaje_completado;
	}
	public long getIdExamen() {
		return id_examen;
	}
}

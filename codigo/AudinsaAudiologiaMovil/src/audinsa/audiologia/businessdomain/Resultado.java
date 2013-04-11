package audinsa.audiologia.businessdomain;


public class Resultado {

	private int id_resultado;	
	private int id_perfil;
	private int id_examen;
	private int valor_examen;

	public Resultado()
	{
	}
	public Resultado(int id_examen,int id_perfil,
			int valor_examen,int duracion_real) {
		super();

		this.id_examen = id_examen;
		this.id_perfil = id_perfil;
		this.valor_examen = valor_examen;
	}

	public int getId_resultado() {
		return id_resultado;
	}
	public void setId_resultado(int id_resultado) {
		this.id_resultado = id_resultado;
	}
	public int getId_perfil() {
		return id_perfil;
	}
	public void setId_perfil(int id_perfil) {
		this.id_perfil = id_perfil;
	}
	public int getId_examen() {
		return id_examen;
	}
	public void setId_examen(int id_examen) {
		this.id_examen = id_examen;
	}
	public int getValor_examen() {
		return valor_examen;
	}
	public void setValor_examen(int valor_examen) {
		this.valor_examen = valor_examen;
	}
}

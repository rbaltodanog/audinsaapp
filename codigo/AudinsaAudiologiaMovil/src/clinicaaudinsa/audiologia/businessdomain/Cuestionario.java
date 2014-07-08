package clinicaaudinsa.audiologia.businessdomain;

import java.util.ArrayList;

public class Cuestionario {
	private ArrayList<String> preguntas;
	private int puntaje;
	
	public Cuestionario()
	{
		preguntas = new ArrayList<String>();
		puntaje=0;
	}

	public ArrayList<String> getPreguntas() {
		return preguntas;
	}

	public void setPreguntas(ArrayList<String> preguntas) {
		this.preguntas = preguntas;
	}

	public int getPuntaje() {
		return puntaje;
	}

	public void setPuntaje(int puntaje) {
		this.puntaje = puntaje;
	}
	
}

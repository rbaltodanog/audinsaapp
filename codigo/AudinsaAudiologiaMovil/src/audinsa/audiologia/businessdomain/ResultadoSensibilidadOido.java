package audinsa.audiologia.businessdomain;

public class ResultadoSensibilidadOido {
	private boolean izquierdo;
	private boolean derecho;
	private int resId;
	private int frecuencia;
	private float tiempoParaSerOido;
	
	public ResultadoSensibilidadOido()
	{
	}
	
	public ResultadoSensibilidadOido(boolean izquierdo, boolean derecho,
			int resId, int frecuencia, float tiempoParaSerOido) {
		super();
		this.izquierdo = izquierdo;
		this.derecho = derecho;
		this.resId = resId;
		this.frecuencia = frecuencia;
		this.tiempoParaSerOido = tiempoParaSerOido;
	}
	
	public float getTiempoParaSerOido() {
		return tiempoParaSerOido;
	}
	public void setTiempoParaSerOido(float tiempoParaSerOido) {
		this.tiempoParaSerOido = tiempoParaSerOido;
	}
	public int getFrecuencia() {
		return frecuencia;
	}
	public void setFrecuencia(int frecuencia) {
		this.frecuencia = frecuencia;
	}
	public int getResId() {
		return resId;
	}
	public void setResId(int resId) {
		this.resId = resId;
	}
	public boolean isDerecho() {
		return derecho;
	}
	public void setDerecho(boolean derecho) {
		this.derecho = derecho;
	}
	public boolean isIzquierdo() {
		return izquierdo;
	}
	public void setIzquierdo(boolean izquierdo) {
		this.izquierdo = izquierdo;
	}
}

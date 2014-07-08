package clinicaaudinsa.audiologia.businessdomain;

public class ResultadoCuestionario {
		public ResultadoCuestionario()
		{
			//dataSource= new PerfilDataSource();
		}
		public ResultadoCuestionario(long idResultado, long idPerfil,
				long idExamen) {
			super();
			this.idExamen = idExamen;
			this.idResultado = idResultado;
			this. idPerfil =  idPerfil;
		}
			
		public long getIdExamen() {
			return idExamen;
		}
		public void setIdExamen(long idExamen) {
			this.idExamen = idExamen;
		}
		public long getIdPerfil() {
			return idPerfil;
		}
		public void setIdPerfil(long idPerfil) {
			this.idPerfil = idPerfil;
		}
		public long getIdResultado() {
			return idResultado;
		}
		public void setIdResultado(long idResultado) {
			this.idResultado = idResultado;
		}
		
		private long idExamen;
		private long idPerfil;
		private long idResultado;
			

}

package team_B18;

public enum JogadorEstado {
	CHUTAR_BOLA(0),
	POSICIONAR_ATAQUE(1),
	POSICIONAR_DEFESA(2),
	INTERCEPTAR_LANCAMENTO(3),
	RECEBER_BOLA(4),
	PERSEGUIR_BOLA(5);
	
	private final int valor;
	
	JogadorEstado(int valor) {
		this.valor = valor;
	}
	
	public int getValor() {
		return valor;
	}
}

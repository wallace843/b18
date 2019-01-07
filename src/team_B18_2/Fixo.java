package team_B18_2;

import simple_soccer_lib.PlayerCommander;
import simple_soccer_lib.utils.EFieldSide;
import simple_soccer_lib.utils.Vector2D;

public class Fixo extends JogadorBase{

	public Fixo(PlayerCommander commander, InformacaoTime informacao) {
		super(commander, informacao);
	}
	
	public void beforeKickOffAcao() {
		habilidade.posicaoInicial(new Vector2D(-0.5,0));
		habilidade.virarParaPonto(habilidade.pegarPosicaoBola());	
	}
	
	public void kickOffLeftAcao() {
		if(habilidade.ladoCampo() == EFieldSide.LEFT) {
			habilidade.passarBola(new Vector2D(-5,5));
			ESTADO = JogadorEstado.POSICIONAR_ATAQUE;	
		}else
			ESTADO = JogadorEstado.POSICIONAR_DEFESA;
	}
	
	public void kickOffRightAcao() {
		if(habilidade.ladoCampo() == EFieldSide.RIGHT) {
			habilidade.passarBola(new Vector2D(-5,-5));
			ESTADO = JogadorEstado.POSICIONAR_ATAQUE;	
		}else
			ESTADO = JogadorEstado.POSICIONAR_DEFESA;
	}
}

package team_B18;

import simple_soccer_lib.PlayerCommander;
import simple_soccer_lib.utils.EFieldSide;
import simple_soccer_lib.utils.Vector2D;

public class ArmadorCentral extends JogadorBase {

	public ArmadorCentral(PlayerCommander commander, InformacaoTime informacao) {
		super(commander, informacao);
	}
	
	public void beforeKickOffAcao() {
		habilidade.virarParaPonto(habilidade.getPosBola());
		habilidade.posicaoInicial(new Vector2D(-5,5));
	}
	
	public void kickOffLeftAcao() {
		if(habilidade.ladoCampo() == EFieldSide.LEFT) {
			ESTADO = JogadorEstado.RECEBER_BOLA;	
		}else
			ESTADO = JogadorEstado.POSICIONAR_DEFESA;
	}
	
	public void kickOffRightAcao() {
		habilidade.virarParaPonto(habilidade.getPosBola());
		if(habilidade.ladoCampo() == EFieldSide.RIGHT) {
			ESTADO = JogadorEstado.RECEBER_BOLA;	
		}else
			ESTADO = JogadorEstado.POSICIONAR_DEFESA;
	}
}

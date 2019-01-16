package team_B18;

import simple_soccer_lib.PlayerCommander;
import simple_soccer_lib.utils.EFieldSide;
import simple_soccer_lib.utils.Vector2D;

public class Fixo extends JogadorBase{

	public Fixo(PlayerCommander commander, InformacaoTime informacao) {
		super(commander, informacao);
	}
	
	public void beforeKickOffAcao() {
		habilidade.posicaoInicial(new Vector2D(-0.5,0));
		habilidade.virarParaPonto(habilidade.getPosBola());	
	}
	
	public void kickOffLeftAcao() {
		Vector2D passe = new Vector2D(-50,0);//habilidade.getFieldPerception().getTeamPlayer(habilidade.ladoCampo(), 5).getPosition();
		if(habilidade.ladoCampo() == EFieldSide.LEFT) {
			habilidade.passarBola(passe);
			ESTADO = JogadorEstado.POSICIONAR_ATAQUE;	
		}else
			ESTADO = JogadorEstado.POSICIONAR_DEFESA;
	}
	
	public void kickOffRightAcao() {
		Vector2D passe = habilidade.getFieldPerception().getTeamPlayer(habilidade.ladoCampo(), 5).getPosition();
		if(habilidade.ladoCampo() == EFieldSide.RIGHT) {
			habilidade.passarBola(passe);
			ESTADO = JogadorEstado.POSICIONAR_ATAQUE;	
		}else
			ESTADO = JogadorEstado.POSICIONAR_DEFESA;
	}
}

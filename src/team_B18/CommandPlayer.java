package team_B18;

import simple_soccer_lib.PlayerCommander;

public class CommandPlayer extends Thread {
	private InformacaoTime informacao;
	private long proximaInteracao;
	private PlayerCommander commander;
	
	public CommandPlayer(PlayerCommander player, InformacaoTime informacao) {
		this.commander = player;
		this.informacao = informacao;
	}

	@Override
	public void run() {
		System.out.println(">> Executando...");
		int uniformeNum = commander.perceiveSelfBlocking().getUniformNumber();
		proximaInteracao = System.currentTimeMillis() + 100;
		while(commander.isActive()) {
			JogadorBase jogador;
			switch (uniformeNum) {
			case 1:
				jogador = new Goleiro(commander, informacao);
				break ;
			case 2:
				jogador = new DefensorDireito(commander, informacao);
				break ;
			case 3:
				jogador = new DefensorEsquerdo(commander, informacao);
				break ;
			case 4:
				jogador = new ArmadorDireito(commander, informacao);
				break ;
			case 5:
				jogador = new ArmadorCentral(commander, informacao);
				break ;
			case 6:
				jogador = new ArmadorEsquerdo(commander, informacao);
				break ;
			case 7:
				jogador = new Fixo(commander, informacao);
				break ;
			default :
				jogador = new JogadorBase(commander, informacao);
				break ;
			}
			while(true) {
				//if(proximaInteracao < System.currentTimeMillis()) {
					proximaInteracao = System.currentTimeMillis() + 100;
					jogador.acao();
				//}
			}
		}
	}	
}

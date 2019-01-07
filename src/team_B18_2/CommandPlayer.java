package team_B18_2;


import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Random;
import simple_soccer_lib.PlayerCommander;
import simple_soccer_lib.perception.FieldPerception;
import simple_soccer_lib.perception.MatchPerception;
import simple_soccer_lib.perception.PlayerPerception;
import simple_soccer_lib.utils.EFieldSide;
import simple_soccer_lib.utils.Vector2D;

public class CommandPlayer extends Thread {
	private InformacaoTime informacao;
	private long proximaInteracao;
	private PlayerCommander commander;
	
	private PlayerPerception selfPerc;
	private FieldPerception  fieldPerc;
	private MatchPerception matchPerc;
	
	public CommandPlayer(PlayerCommander player, InformacaoTime informacao) {
		this.commander = player;
		this.informacao = informacao;
	}

	@Override
	public void run() {
		System.out.println(">> Executando...");
		int uniformeNum = commander.perceiveSelfBlocking().getUniformNumber();
		proximaInteracao = System.currentTimeMillis() + 100;
		//updatePerceptions();
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
				if(proximaInteracao < System.currentTimeMillis()) {
					proximaInteracao = System.currentTimeMillis() + 100;
					jogador.acao();
				}
			}
			/*switch (selfPerc.getUniformNumber()) {
				case 1:
					acaoGoleiro(nextIteration);
					break ;
				case 2:
					acaoDefensor(nextIteration, 1);
					break ;
				case 3:
					acaoDefensor(nextIteration, -1);
					break ;
				case 4:
					acaoArmador(nextIteration, -1);
					break ;
				case 5:
					acaoArmador(nextIteration, 0);
					break ;
				case 6:
					acaoArmador(nextIteration, 1);
					break ;
				case 7:
					acaoFixo(nextIteration);
					break ;
				default : 
					break ;
			}*/
		}
	}	
	
	private void acaoArmador(long nextIteration, int pos) {
		EFieldSide side = selfPerc.getSide();
		EFieldSide sideAdv;
		Vector2D golAdv;
		
		//Definicao do local do lado e gol adversario
		if(side == EFieldSide.LEFT) {
			sideAdv = EFieldSide.RIGHT;
			golAdv = new Vector2D(50d,0d);
		}
		else {
			sideAdv = EFieldSide.LEFT;
			golAdv = new Vector2D(-50d,0d);
		}
		
		Vector2D ballPos;
		while(true) {
			updatePerceptions();
			ballPos = fieldPerc.getBall().getPosition();
			switch(matchPerc.getState()) {
			case BEFORE_KICK_OFF:
				//Posicao inicial antes do inicio da partida
				commander.doMoveBlocking(-25d, 25d*pos);
				break;
				
			case KICK_OFF_LEFT:
				commander.doMoveBlocking(-25d, 25d*pos);
				break;
			case KICK_OFF_RIGHT:
				commander.doMoveBlocking(-25d, 25d*pos);
				break;
			case PLAY_ON:
				
				//Se o jogador estiver a distancia de 1 perto da bola
				if(isPointsAreClose(selfPerc.getPosition(), fieldPerc.getBall().getPosition(), 1d)) {
					
					//Se o jogador estiver a distancia menor que 30 do gol adversario
					if(distanceOfPoints(selfPerc.getPosition(), golAdv) < 30) {
						chutarGol(golAdv);
					}
					
					//Se o jogador adversairo mais proximo estiver a mais de 5 de distancia
					else if(distanceOfPoints(getClosestPlayerPoint(fieldPerc.getBall().getPosition(), sideAdv).getPosition(), fieldPerc.getBall().getPosition()) > 5) {
						kickToPoint(golAdv, 20d);
					}
					
					//Toca pra alguem do time
					else {
						passarBola(selfPerc.getPosition(), fieldPerc.getTeamPlayer(side, outroNumero(selfPerc.getUniformNumber())).getPosition());
					}
				
				//Se o jogador estiver longe da bola
				}else{
					
					//Se eu for jogador do meu time mais perto da bola corro atras da bola
					if(getClosestPlayerPoint(fieldPerc.getBall().getPosition(), side).getUniformNumber() == selfPerc.getUniformNumber())
						dash(fieldPerc.getBall().getPosition());
					
					//Se nao olho para bola
					else
						turnToPoint(fieldPerc.getBall().getPosition());
				}
				break;
			default:
				break;
			}
		}
	}
	
	private void acaoDefensor(long nextIteration, int pos) {
		EFieldSide side = selfPerc.getSide();
		Vector2D ballPos;
		EFieldSide sideAdv;
		Vector2D golAdv;
		
		//Definicao do local do lado e gol adversario
		if(side == EFieldSide.LEFT) {
			sideAdv = EFieldSide.RIGHT;
			golAdv = new Vector2D(50d,0d);
		}
		else {
			sideAdv = EFieldSide.LEFT;
			golAdv = new Vector2D(-50d,0d);
		}
		
		while(true) {
			updatePerceptions();
			ballPos = fieldPerc.getBall().getPosition();
			switch(matchPerc.getState()) {
			case BEFORE_KICK_OFF:
				commander.doMoveBlocking(-30d, -15d*pos);
				break;
			case KICK_OFF_LEFT:
				commander.doMoveBlocking(-30d, -15d*pos);
				break;
			case KICK_OFF_RIGHT:
				commander.doMoveBlocking(-30d, -15d*pos);
				break;
			case PLAY_ON:
				//Se o jogador estiver a distancia menor que 30 do gol adversario
				if(distanceOfPoints(selfPerc.getPosition(), golAdv) < 30) {
					chutarGol(golAdv);
				}
				else if(isPointsAreClose(selfPerc.getPosition(), fieldPerc.getBall().getPosition(), 1d))
					passarBola(selfPerc.getPosition(),fieldPerc.getTeamPlayer(side, outroNumero(selfPerc.getUniformNumber())).getPosition());
				else{
					if(getClosestPlayerPoint(fieldPerc.getBall().getPosition(), side).getUniformNumber() == selfPerc.getUniformNumber())
						dash(fieldPerc.getBall().getPosition());
					else
						turnToPoint(fieldPerc.getBall().getPosition());
				}
				break;
			default:
				break;
			}
		}
	}
	
	private void acaoFixo(long nextIteration) {
		EFieldSide side = selfPerc.getSide();
		
		EFieldSide sideAdv;
		Vector2D golAdv;
		
		//Definicao do local do lado e gol adversario
		if(side == EFieldSide.LEFT) {
			sideAdv = EFieldSide.RIGHT;
			golAdv = new Vector2D(50d,0d);
		}
		else {
			sideAdv = EFieldSide.LEFT;
			golAdv = new Vector2D(-50d,0d);
		}
		
		Vector2D ballPos;
		while(true) {
			updatePerceptions();
			ballPos = fieldPerc.getBall().getPosition();
			switch(matchPerc.getState()) {
			case BEFORE_KICK_OFF:
				commander.doMoveBlocking(-0.5d, 0d);
				Vector2D newDirection = fieldPerc.getTeamPlayer(side, 5).getPosition().sub(selfPerc.getPosition());
				commander.doTurnToDirectionBlocking(newDirection);
				break;
			case KICK_OFF_LEFT:
				commander.doMoveBlocking(-0.5d, 0d);
				commander.doKickBlocking(100, 0);
				break;
			case KICK_OFF_RIGHT:
				commander.doMoveBlocking(-0.5d, 0d);
				commander.doKickBlocking(100, 0);
				break;
			case PLAY_ON:
				//Se a minha distancia para bola for 1
				if(isPointsAreClose(selfPerc.getPosition(), fieldPerc.getBall().getPosition(), 1d)) {
					
					//Se o jogador estiver a distancia menor que 30 do gol adversario
					if(distanceOfPoints(selfPerc.getPosition(), golAdv) < 30) {
						chutarGol(golAdv);
					}
					else if(distanceOfPoints(getClosestPlayerPoint(fieldPerc.getBall().getPosition(), sideAdv).getPosition(), fieldPerc.getBall().getPosition()) > 5) {
						if(side == EFieldSide.LEFT)
							kickToPoint(new Vector2D(50d,0d), 20d);
						else
							kickToPoint(new Vector2D(-50d,0d), 20d);
					}
					else {
						passarBola(selfPerc.getPosition(), fieldPerc.getTeamPlayer(side, outroNumero(selfPerc.getUniformNumber())).getPosition());
					}
				}else{
					if(getClosestPlayerPoint(fieldPerc.getBall().getPosition(), side).getUniformNumber() == selfPerc.getUniformNumber())
						dash(fieldPerc.getBall().getPosition());
					else
						turnToPoint(fieldPerc.getBall().getPosition());
				}
				break;
			default:
				break;
			}
		}
	}
	
	private void acaoGoleiro(long nextIteration) {
		EFieldSide side = selfPerc.getSide();
		EFieldSide sideAdv = side == EFieldSide.LEFT ? EFieldSide.RIGHT:EFieldSide.LEFT;
		Rectangle area = side == EFieldSide. LEFT ?	new Rectangle(-52, -20, 16, 40):new Rectangle(36, -20, 16, 40);
		Vector2D gol = side == EFieldSide.LEFT? new Vector2D(-50d,0d):new Vector2D(50d,0d);
		Vector2D ballPos;
		while(true) {
			updatePerceptions();
			ballPos = fieldPerc.getBall().getPosition();
			switch(matchPerc.getState()) {
			case BEFORE_KICK_OFF:
				commander.doMoveBlocking(-50d, 0d);
				break;
			case KICK_OFF_LEFT:
				commander.doMoveBlocking(-50d, 0d);
				break;
			case KICK_OFF_RIGHT:
				commander.doMoveBlocking(-50d, 0d);
				break;
			case PLAY_ON:
				getBallVelocity();
				if(isPointsAreClose(selfPerc.getPosition(), fieldPerc.getBall().getPosition(), 1d))
					kickToPoint(new Vector2D(0,0), 200);
				else{
					if(getClosestPlayerPoint(fieldPerc.getBall().getPosition(), side).getUniformNumber() == selfPerc.getUniformNumber())
						dash(fieldPerc.getBall().getPosition());
					else if(distanceOfPoints(selfPerc.getPosition(),gol ) > 1) 
						dash(gol);
					else
						turnToPoint(fieldPerc.getBall().getPosition());
				}
				break;
			
			default:
				break;
			}
		}
	}
	
	private void updatePerceptions() {
		PlayerPerception newSelf = commander.perceiveSelfBlocking();
		FieldPerception newField = commander.perceiveFieldBlocking();
		MatchPerception newMatch = commander.perceiveMatchBlocking();
		if(newSelf != null)
			this.selfPerc = newSelf;
		if(newField != null)
			this.fieldPerc = newField;
		if (newMatch != null ) 
			this.matchPerc = newMatch;
	}
	
	//Acoes Basicas
	private void kickToPoint(Vector2D point, double intensity){
		Vector2D newDirection = point.sub(selfPerc.getPosition());
		double angle = newDirection.angleFrom(selfPerc.getDirection());
		if (angle > 20 || angle < -20){
			commander.doKick(4, angle);
		}else
			commander.doKickBlocking(intensity, angle);
	}
	
	private PlayerPerception getClosestPlayerPoint(Vector2D point, EFieldSide side){
		ArrayList<PlayerPerception> lp = fieldPerc.getTeamPlayers(side);
		PlayerPerception np = null ;
		if (lp != null && !lp.isEmpty()){
			double dist,temp;
			dist = lp.get(0).getPosition().distanceTo(point);
			np = lp.get(0);
			for (PlayerPerception p : lp) {
				if (p.getPosition() == null ) 
					break ;
				temp = p.getPosition().distanceTo(point);
				if (temp < dist){
					dist = temp;
					np = p;
				}
			}
		}
		return np;
	}
	
	private double distanceOfPoints(Vector2D point1, Vector2D point2) {
		return point1.distanceTo(point2);
	}
	
	private boolean isPointsAreClose(Vector2D reference, Vector2D point, double margin){
		return reference.distanceTo(point) <= margin;
	}
	
	private void dash(Vector2D point){
		if (selfPerc.getPosition().distanceTo(point) <= 1) 
			return ;
		if (!isAlignToPoint(point, 15)) 
			turnToPoint(point);
		commander.doDashBlocking(70);
	}
	
	private void turnToPoint(Vector2D point){
		Vector2D newDirection = point.sub(selfPerc.getPosition());
		commander.doTurnToDirectionBlocking(newDirection);
	}
	
	private boolean isAlignToPoint(Vector2D point, double margin){
		double angle = point.sub(selfPerc.getPosition()).angleFrom(selfPerc.getDirection());
		return angle < margin && angle > margin*(-1);
	}
	
	//Jogadas
	private void passarBola(Vector2D origem, Vector2D destino) {
		double distancia = origem.distanceTo(destino);
		double intensidade = Math.sqrt(210d*distancia);
		kickToPoint(destino, intensidade);
	}
	
	private void chutarGol(Vector2D golAdv) {
		Vector2D newDirection = golAdv.sub(selfPerc.getPosition());
		double angle = newDirection.angleFrom(selfPerc.getDirection());
		if (angle > 45 || angle < -45){
			commander.doTurnToDirectionBlocking(newDirection);
			commander.doKickBlocking(10d, 0d);
		}else
			commander.doKickBlocking(150, angle);
	}
	
	private int outroNumero(int meuNumero){
		Random r = new Random();
		int player = r.nextInt(6)+ 2;
		
		while(player == meuNumero) {
			player = r.nextInt(6) + 2;
		}
		return player;
	}
	
	private Vector2D getBallVelocity() {
		Vector2D origem = fieldPerc.getBall().getPosition();
		long intevalo = 50;
		try {
			sleep(intevalo);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		fieldPerc = commander.perceiveFieldBlocking();
		Vector2D destino = fieldPerc.getBall().getPosition();
		Vector2D velocity = destino.sub(origem).multiply(1/intevalo);
		return velocity;
	}
}

package team_B18_2;


import java.net.UnknownHostException;


public class Main {

	public static void main(String[] args) throws UnknownHostException {
		CommandTeam team1 = new CommandTeam("B18");
		CommandTeam team2 = new CommandTeam("OUTROS");
		
		team1.launchTeamAndServer();
		team2.launchTeam();
	}
}


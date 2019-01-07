package team_B18;


import java.net.UnknownHostException;


public class Main {

	public static void main(String[] args) throws UnknownHostException {
		CommandTeam team1 = new CommandTeam("B18");
		//team_B18_2.CommandTeam team2 = new team_B18_2.CommandTeam("B18_2");
		CommandTeam team2 = new CommandTeam("B18_2");
		
		team1.launchTeamAndServer();
		team2.launchTeam();
	}
	
}


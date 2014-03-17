/**
 * 
 */
package com.wdahl.dicegreed;

/**
 * @author Will
 * Player - represents a game participant.
 * Used to track a players score.
 */

public class Players {
	
	private int mActivePlayer;
	private int[] mScores;
	public Players(int TotalPlayers){
		mActivePlayer = 0;
		mScores = new int[TotalPlayers];
		// Zero-based array.
	}
	
	public int getScore() {
		return this.mScores[mActivePlayer];
	}
	public void setScore(int score) {
		this.mScores[mActivePlayer] = score;
	}
	public void addScore(int points){
		this.mScores[mActivePlayer] += points;
	}
	public int getActivePlayer() {
		return mActivePlayer + 1;
	}
	public void nextActivePlayer() {
		this.mActivePlayer += 1;
		if (this.mActivePlayer >= mScores.length){
			this.mActivePlayer = 0;
		}
	}

	/**
	 * Provide a string listing each player and their score
	 * the active player will be shown in bold
	 */
	public String getScoresText() {
		String ScoresText = "";
		
		for( int i = 0; i <= mScores.length - 1; i++)
		{
			if (i == mActivePlayer){
				// Make bold, and prefix with ">" for the active player
				ScoresText += "<b>> Player " + (i+1) + ": " + mScores[i] + "<br/></b>";
			}
			else{
				ScoresText += "Player " + (i+1) + ": " + mScores[i] + "<br/>";	
			}
			
		}
		
		return ScoresText;
	}
	
}

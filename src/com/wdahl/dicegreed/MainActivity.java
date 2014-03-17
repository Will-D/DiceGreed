package com.wdahl.dicegreed;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author Will
 * MainActivity - this activity represents the dice game.
 * It is based on Pig Dice, for details refer to wikipedia:
 * http://en.wikipedia.org/wiki/Pig_(dice_game)
 * 
 */

public class MainActivity extends Activity {
	
	private Dice mDice; 
	private int mTurnScore;
	private TextView mViewTurnScore;
	private TextView mViewPlayerScore;
	private Players mPlayers;
	private ImageView mViewDiceImage;
	private Button mRollButton;
	private Button mHoldButton;
	private Button mNewButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mHoldButton = (Button) findViewById(R.id.btnHold);
		mRollButton = (Button) findViewById(R.id.btnRoll);
		mNewButton = (Button) findViewById(R.id.btnNew);
		
		// Enable buttons other than "New"
		mHoldButton.setVisibility(View.VISIBLE);
		mRollButton.setVisibility(View.VISIBLE);
		mNewButton.setVisibility(View.VISIBLE);
				
		mViewTurnScore = (TextView) findViewById(R.id.turnScore);
		mViewPlayerScore = (TextView) findViewById(R.id.playerScore);
		mViewDiceImage = (ImageView) findViewById(R.id.imageView1); 
		
		// Initialize variables for start of game 
		mPlayers = new Players(2);
		mDice = new Dice();
		mTurnScore = 0;
		refreshPlayerScores();
		
		showRules();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void onRollClick(View v) { 
		rollDice(); 	
      }  
	
	/**
	 * Add turn score to player score.
	 * Check for winner (> 100 score).
	 * Else change player to next.
	 */
	public void onHoldClick(View v) { 
		// TODO refactor this to be cleaner
		if (mTurnScore == 0){
			showOkMessage(getString(R.string.promptZeroTurnScoreTitle),getString(R.string.promptZeroTurnScore));
		} else{
			
			mPlayers.addScore(mTurnScore);
			resetTurnScore();
			
			if (mPlayers.getScore() >= 100) onPlayerWin();
			else {
				ChangePlayer();
				showOkMessage(getString(R.string.promptChangePlayerTitle),
						getString(R.string.promptHold) + getString(R.string.promptChangePlayerPart1) + 
						(mPlayers.getActivePlayer()) + getString(R.string.promptChangePlayerPart2));
			}
		}
      }

	/**
	 * Announce the winning player
	 * Hide other buttons (until new game is started)
	 */
	private void onPlayerWin() {
		refreshPlayerScores();
		showOkMessage(getString(R.string.promptWinnerTitle), 
				getString(R.string.promptWinnerPart1) + 
				(mPlayers.getActivePlayer()) + 
				getString(R.string.promptWinnerPart2));
		
		// Hide buttons other than "New"
		mHoldButton.setVisibility(View.INVISIBLE);
		mRollButton.setVisibility(View.INVISIBLE);
	}  
	
	/**
	 * Confirm before starting new game
	 * @param v - the view which was clicked on
	 */
	public void onNewClick(View v){
		if (mPlayers.getScore() > 100) resetGame();
		else {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle(getString(R.string.promptNewGameTitle)); 
			builder.setMessage(getString(R.string.promptNewGameMessage));
			builder.setNegativeButton(android.R.string.cancel, null);
			builder.setPositiveButton(android.R.string.ok,
					new DialogInterface.OnClickListener() {
			            public void onClick(DialogInterface dialog, int buttonId) {
			                 resetGame();
			            }
			        });
			AlertDialog dialog = builder.create();
			dialog.show();
		}
	}

	/**
	 * Resets the current game.
	 * Starts a new game from scratch.
	 */
	public void resetGame() {
		resetTurnScore();
		mPlayers = new Players(2); 
		refreshPlayerScores();
		// Enable buttons other than "New"
		mHoldButton.setVisibility(View.VISIBLE);
		mRollButton.setVisibility(View.VISIBLE);
	}
	
	/**
	 * Sets turn score to 0. 
	 * Updates the turn score view
	 */
	public void resetTurnScore(){
		mTurnScore = 0;
		mViewTurnScore.setText(getString(R.string.turnScoreLabel) + mTurnScore);
	}
	
	/**
	 * Switch to the next player.
	 */
	private void ChangePlayer(){
		mPlayers.nextActivePlayer();
		refreshPlayerScores();
	}

	/** Updates the TextView showing player scores.
	 *  Reads the scores from Players class.
	 */
	private void refreshPlayerScores() {
		mViewPlayerScore.setText(Html.fromHtml(mPlayers.getScoresText())); 
		// HTML used to bold the current player and for newline. 
		// If not using above HTML logic, \n would work instead.
	}
	
	private void setDiceImage(int DiceNumber){
		int[] imageResources = {
				R.drawable.dice1, 
				R.drawable.dice2, 
				R.drawable.dice3,
				R.drawable.dice4, 
				R.drawable.dice5,
				R.drawable.dice6
		};
		
		// Array is zero based, thus minus one to get the right number image.
		mViewDiceImage.setImageResource(imageResources[DiceNumber-1]);
		       
	}
	
	/** Shows a Toast pop-up message on screen
	 */
	public void showPopupMsg(CharSequence msg){
		Context context = getApplicationContext();
		int duration = Toast.LENGTH_LONG;

		Toast.makeText(context, msg, duration).show();
	}
	
	/** Shows a standard "ok" prompt on screen 
	 *  with message and title from the parameters
	 */
	private void showOkMessage(String title, String message) {
		
        
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(title); 
		builder.setMessage(message);
		builder.setPositiveButton(android.R.string.ok, null);
		AlertDialog dialog = builder.create();
		dialog.show();
	}
	
	/**
	 *  Displays the rules on screen, for the players to read
	 */
	private void showRules(){
		String msg = getString(R.string.rules);
		TextView content = new TextView(this);
        content.setText(msg);
        content.setTypeface(Typeface.SANS_SERIF);
        content.setPadding(10, 10, 10, 10);
        
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(getString(R.string.rulesTitle)); 
		builder.setView(content);
		builder.setPositiveButton(android.R.string.ok, null);
		AlertDialog dialog = builder.create();
		dialog.show();
	}
	
	/**
	 * Triggers dice animation OR
	 * if dice was rolling, dice will stop 
	 * and randomly choose a result.
	 * Also hides other buttons until roll is completed.
	 */
	private void rollDice(){
		mViewDiceImage.setImageResource(R.drawable.dice_animation);
		AnimationDrawable diceAnimation = (AnimationDrawable) mViewDiceImage.getDrawable();
		
		if (!diceAnimation.isRunning()){
			diceAnimation.start();
			mRollButton.setText("Stop");
			
			// Hide other buttons, they can't be used until roll is complete
			mNewButton.setVisibility(View.INVISIBLE);
			mHoldButton.setVisibility(View.INVISIBLE);
		}
		else{
			diceAnimation.stop();
			int result = mDice.getNewResult();
			setDiceImage(result);
			mRollButton.setText("Roll");
			
			// Show other buttons again. They can be used now.
			mNewButton.setVisibility(View.VISIBLE);
			mHoldButton.setVisibility(View.VISIBLE);
			
			if (result == 1){
				resetTurnScore();
				ChangePlayer();
				showOkMessage(getString(R.string.promptChangePlayerTitle),
						getString(R.string.promptRolledOne) + 
						getString(R.string.promptChangePlayerPart1) + 
						(mPlayers.getActivePlayer()) + 
						getString(R.string.promptChangePlayerPart2));
			}
			else {
				mTurnScore += result;
				mViewTurnScore.setText(getString(R.string.turnScoreLabel) + mTurnScore);
			}
	        
		}
		
	}
	
}
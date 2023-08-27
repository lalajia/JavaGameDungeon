package controller;

import game.Game;

/**
 * Controller Interface handles the user input.
 */
public interface Controller {

  /**
   * Execute a single game given a Game Model. When the game is over, the playGame method ends.
   *
   * @param m the not null Game model m.
   */
  void playGame(Game m);
}



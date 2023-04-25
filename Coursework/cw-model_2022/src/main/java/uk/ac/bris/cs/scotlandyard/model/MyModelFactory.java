package uk.ac.bris.cs.scotlandyard.model;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import java.util.*;
import javax.annotation.Nonnull;
import uk.ac.bris.cs.scotlandyard.model.ScotlandYard.*;

/**
 * cw-model
 * Stage 2: Complete this class
 */
public final class MyModelFactory implements Factory<Model> {

	private final class MyModel implements Model {

		private GameSetup setup;
		private Player mrX;
		private ImmutableList<Player> detectives;
		private ImmutableSet<Observer> observers;
		private Board.GameState state;



		private MyModel(
				final GameSetup setup,
				final Player mrX,
				final ImmutableList<Player> detectives) {

			this.setup = setup;
			this.mrX = mrX;
			this.detectives = detectives;
			this.observers = ImmutableSet.of();
			//this.state = MyGameStateFactory.build(setup, mrX, detectives);
			//board.GameState state = gameStateFactory.build();
		}

		@Nonnull
		@Override
		public Board getCurrentBoard() {

			//return new ImmutableBoard(setup, detectiveLocations, tickets, log, winner, availableMoves);
			return state;
		}

		@Override
		public void registerObserver(@Nonnull Observer observer) {
			List<Observer> copyObservers = new ArrayList<>(List.copyOf(observers));
			if (copyObservers.contains(observer)) throw new IllegalArgumentException("Duplicate Observer");
			copyObservers.add(observer);
			observers = ImmutableSet.copyOf(copyObservers);
		}

		@Override
		public void unregisterObserver(@Nonnull Observer observer) {
			List<Observer> copyObservers = new ArrayList<>(List.copyOf(observers));
			if (observer == null) throw new NullPointerException("Null Observer");
			if (copyObservers.isEmpty()) throw new IllegalArgumentException("Empty Observers");
			else copyObservers.remove(observer);
			observers = ImmutableSet.copyOf(copyObservers);
		}

		@Nonnull
		@Override
		public ImmutableSet<Observer> getObservers() {
			return observers;
		}

		@Override
		public void chooseMove(@Nonnull Move move) {
			state = state.advance(move);
			if (state.getWinner().isEmpty()) {
				for (Observer observer:observers) {
					observer.onModelChanged(state, Observer.Event.MOVE_MADE);
				}
			}
			else {
				for (Observer observer:observers) {
					observer.onModelChanged(state, Observer.Event.GAME_OVER);
				}
			}
			// TODO Advance the model with move, then notify all observers of what what just happened.
			//  you may want to use getWinner() to determine whether to send out Event.MOVE_MADE or Event.GAME_OVER
		}
	}

	@Nonnull @Override public Model build(GameSetup setup,
	                                      Player mrX,
	                                      ImmutableList<Player> detectives) {

		return new MyModelFactory.MyModel(setup, mrX, detectives);
	}
}

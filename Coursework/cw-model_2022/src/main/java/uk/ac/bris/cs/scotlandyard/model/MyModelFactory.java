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

		private final GameSetup setup;
		private final Player mrX;
		private final ImmutableList<Player> detectives;
		private ImmutableSet<Observer> observers;


		private MyModel(
				final GameSetup setup,
				final Player mrX,
				final ImmutableList<Player> detectives) {

			this.setup = setup;
			this.mrX = mrX;
			this.detectives = detectives;
			this.observers = ImmutableSet.of();
		}

		@Nonnull
		@Override
		public Board getCurrentBoard() {
			return null;
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
			if (copyObservers.isEmpty()) throw new IllegalArgumentException("Empty Observers");
			if (copyObservers == null) throw new IllegalArgumentException("Empty Observers");
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
			// TODO Advance the model with move, then notify all observers of what what just happened.
			//  you may want to use getWinner() to determine whether to send out Event.MOVE_MADE or Event.GAME_OVER
		}
	}

	@Nonnull @Override public Model build(GameSetup setup,
	                                      Player mrX,
	                                      ImmutableList<Player> detectives) {

		// TODO
		return new MyModelFactory.MyModel(setup, mrX, detectives);
	}
}

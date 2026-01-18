package com.stockmarketpotato.bot.task;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.stockmarketpotato.bot.BotConfiguration;
import com.stockmarketpotato.bot.strategy.BaseStrategy;

/**
 * Abstract base class for tasks that are scheduled to run at specific times or intervals.
 * Defines the contract for executability checks and strategy execution logic.
 */
public abstract class BaseStrategyTimedTask extends AbstractStrategyTask {
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	/**
	 * Constructor for BaseStrategyTimedTask.
	 * 
	 * @param baseStrategy The parent strategy.
	 * @param name The name of the task.
	 */
	public BaseStrategyTimedTask(BaseStrategy baseStrategy, String name) {
		super(baseStrategy, name);
		Preconditions.checkNotNull(name, "Name of Strategy must be set");
	}

	abstract protected boolean isExecutable(final BotConfiguration botConfiguration);
	
	abstract protected void runStrategy(final BotConfiguration botConfiguration);
	
	/**
	 * Attempts to execute the strategy task if the configuration allows it.
	 * 
	 * @param botConfiguration The current bot configuration.
	 */
	public void tryExecute(final BotConfiguration botConfiguration) {
		if (isExecutable(botConfiguration)) {
			log.info("Execute Strategy Task " + getName());
			runStrategy(botConfiguration);
		}
	}
	
	/**
	 * Computes the hash code for this task based on its name.
	 * @return The hash code.
	 */
	@Override
	public int hashCode()
	{
	    return Objects.hash(name);
	}

	/**
	 * Checks equality based on the task name.
	 * 
	 * @param o The object to compare.
	 * @return True if names are equal, false otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		BaseStrategyTimedTask baseStrategyTask = (BaseStrategyTimedTask) o;
		return Objects.equals(this.name, baseStrategyTask.name);
	}

	/**
	 * Gets the name of the task.
	 * @return The task name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of the task.
	 * @param name The new task name.
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Fluent setter for the task name.
	 * 
	 * @param name The new task name.
	 * @return The current instance.
	 */
	public BaseStrategyTimedTask name(String name) {
		this.name = name;
		return this;
	}
}

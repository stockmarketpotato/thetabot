package com.stockmarketpotato.bot.task;

import com.stockmarketpotato.bot.strategy.BaseStrategy;

/**
 * Base abstract class for all strategy tasks.
 * A Strategy consists of Stategy Tasks. A Strategy Task is the smallest functional entity of a Strategy.
 * Holds the reference to the parent strategy and the task name.
 */
public abstract class AbstractStrategyTask {
	/**
	 * The strategy to which this task belongs
	 */
	protected BaseStrategy baseStrategy;
	
	/**
	 * The name of the strategy, cannot be empty
	 */
	protected String name;
	
	/**
	 * Constructor for AbstractStrategyTask.
	 * 
	 * @param baseStrategy The strategy to which this task belongs.
	 * @param name The name of the task.
	 */
	public AbstractStrategyTask(BaseStrategy baseStrategy, String name) {
		this.baseStrategy = baseStrategy;
		this.name = name;
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
}

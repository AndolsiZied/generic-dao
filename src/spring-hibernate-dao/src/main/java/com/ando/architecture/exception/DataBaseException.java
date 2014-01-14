package com.ando.architecture.exception;

/**
 * The {@link DataBaseException} exception should be thrown when an exception occurs while trying to connect to a
 * database.
 * 
 * @author Zied ANDOLSI
 */
public class DataBaseException extends Exception {

	/**
	 * {@link DataBaseException}'s default serial identifier.
	 */
	private static final long serialVersionUID = -7920779797756759564L;

	/**
	 * Constructs a new <code>DataBaseException</code>.It only calls the corresponding parent constructor.
	 */
	public DataBaseException() {
		super();
	}

	/**
	 * Constructs a new <code>DataBaseException</code> with the specified detail message. It only calls the
	 * corresponding parent constructor.
	 * 
	 * @param msg
	 *            message
	 */
	public DataBaseException(String msg) {
		super(msg);
	}

	/**
	 * Constructs a new <code>DataBaseException</code> with the specified detail message and encapsulated exception. It
	 * only calls the corresponding parent constructor.
	 * 
	 * @param msg
	 *            message
	 * @param e
	 *            throwable
	 */
	public DataBaseException(String msg, Throwable e) {
		super(msg, e);
	}

	/**
	 * Constructs a new <code>DataBaseException</code> with the encapsulated exception. It only calls the corresponding
	 * parent constructor.
	 * 
	 * @param e
	 *            throwable
	 */
	public DataBaseException(Throwable e) {
		super(e);
	}
}

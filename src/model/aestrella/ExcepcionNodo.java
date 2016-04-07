package model.aestrella;

@SuppressWarnings("serial")
public class ExcepcionNodo extends RuntimeException {

	public ExcepcionNodo() {
	}

	public ExcepcionNodo(String message) {
		super(message);
	}
}

package exception.service;

public class ServiceException extends Exception {

	private static final long serialVersionUID = -1276822170861281157L;
	
	public ServiceException(String message, Throwable cause){
		super(message, cause);
	}
	
	public ServiceException(String message){
		super(message);
	}
	
	public ServiceException(Throwable cause){
		super(cause);
	}
	
	public ServiceException(){
		super();
	}

}

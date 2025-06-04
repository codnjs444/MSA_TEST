package com.example.userService.global.exception;

public class CMailSenderException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public CMailSenderException(String message, Throwable t) {
        super(message, t);
    }
	
	public CMailSenderException(String message) {
        super(message);
    }
	
	public CMailSenderException() {
        super();
    }
	
    public static class CMailAuthenticationException extends CMailSenderException {
    	private static final long serialVersionUID = 1L;
        public CMailAuthenticationException(String message, Throwable t) {
            super(message, t);
        }
    }
    
    public static class CMailParseException extends CMailSenderException {
    	private static final long serialVersionUID = 1L;
        public CMailParseException(String message, Throwable t) {
            super(message, t);
        }
    }
    
    public static class CMailPreparationException extends CMailSenderException {
    	private static final long serialVersionUID = 1L;
        public CMailPreparationException(String message, Throwable t) {
            super(message, t);
        }
    }
    
    public static class CMailSendException extends CMailSenderException {
		private static final long serialVersionUID = 1L;
		public CMailSendException(String message, Throwable t) {
            super(message, t);
        }
    }
    
    public static class CMessagingException extends CMailSenderException {
    	private static final long serialVersionUID = 1L;
        public CMessagingException(String message, Throwable t) {
            super(message, t);
        }
    }
}

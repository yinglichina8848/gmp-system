package com.gmp.auth.exception;

/**
 * 令牌相关异常
 * 用于表示JWT令牌的各种错误情况
 */
public class TokenException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 构造函数
     * @param message 错误消息
     */
    public TokenException(String message) {
        super(message);
    }
    
    /**
     * 构造函数
     * @param message 错误消息
     * @param cause 异常原因
     */
    public TokenException(String message, Throwable cause) {
        super(message, cause);
    }
    
    /**
     * 令牌已过期异常
     */
    public static class ExpiredTokenException extends TokenException {
        
        private static final long serialVersionUID = 1L;
        
        public ExpiredTokenException() {
            super("令牌已过期");
        }
        
        public ExpiredTokenException(String message) {
            super(message);
        }
    }
    
    /**
     * 无效令牌异常
     */
    public static class InvalidTokenException extends TokenException {
        
        private static final long serialVersionUID = 1L;
        
        public InvalidTokenException() {
            super("无效的令牌");
        }
        
        public InvalidTokenException(String message) {
            super(message);
        }
    }
    
    /**
     * 撤销的令牌异常
     */
    public static class RevokedTokenException extends TokenException {
        
        private static final long serialVersionUID = 1L;
        
        public RevokedTokenException() {
            super("令牌已被撤销");
        }
    }
    
    /**
     * 不支持的令牌异常
     */
    public static class UnsupportedTokenException extends TokenException {
        
        private static final long serialVersionUID = 1L;
        
        public UnsupportedTokenException() {
            super("不支持的令牌类型");
        }
    }
    
    /**
     * 令牌签名异常
     */
    public static class SignatureException extends TokenException {
        
        private static final long serialVersionUID = 1L;
        
        public SignatureException() {
            super("令牌签名验证失败");
        }
    }
}
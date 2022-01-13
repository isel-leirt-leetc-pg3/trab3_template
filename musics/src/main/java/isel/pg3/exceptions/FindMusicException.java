package isel.pg3.exceptions;

public class FindMusicException extends RuntimeException{
    public FindMusicException(Exception e) {
        super(e);
    }

    public FindMusicException(String msg) {
        super(msg);
    }

    public FindMusicException(String msg, Exception e) {
        super(msg, e);
    }
}

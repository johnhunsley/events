package com.johnhunsley.events.repository;

/**
 * @author John Hunsley
 *         jphunsley@gmail.com
 *         Date : 09/05/2017
 *         Time : 16:57
 */
public class RepositoryException extends Throwable {
    public RepositoryException(String s) {
        super(s);
    }

    public RepositoryException(Throwable e) {
        super(e);
    }
}

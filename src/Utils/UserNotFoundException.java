/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

/**
 *
 * @author hlsmi
 */
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String username) {
        super(String.format("Could not find user %s.", username));
    }
}

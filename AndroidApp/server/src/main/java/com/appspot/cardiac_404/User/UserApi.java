package com.appspot.cardiac_404.User;

import com.appspot.cardiac_404.CARdiacApiBase;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.response.ConflictException;
import com.google.api.server.spi.response.UnauthorizedException;
import com.google.appengine.api.users.User;

/**
 * Created by Alan on 11/22/2015.
 */
public class UserApi extends CARdiacApiBase {

    @ApiMethod(httpMethod = "post")
    public void registerUser(User user) throws UnauthorizedException, ConflictException {
        try {
            loadUser(user);
            throw new ConflictException("user is already registered");
        }
        catch(UnauthorizedException e){
            if (e.getCause() instanceof NullPointerException){
                throw e;
            }else {
                CardiacUser newUser = new CardiacUser(user);//copy data to custom class for use in datastore
                saveCardiacUser(newUser);
            }
        }
    }
    @ApiMethod(httpMethod = "delete")
    public void unRegisterUser(User user) throws UnauthorizedException, ConflictException {
        deleteUser(user);
    }
    @ApiMethod(httpMethod = "put")
    public void toggleMonitorPermission(User user) throws UnauthorizedException {
        CardiacUser cardiacUser = loadUser(user);
        cardiacUser.toggleMonitor();
        saveCardiacUser(cardiacUser);
    }
}

package com.appspot.cardiac_404.User;

import com.appspot.cardiac_404.CARdiacApiBase;
import com.appspot.cardiac_404.MailService;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.response.ConflictException;
import com.google.api.server.spi.response.InternalServerErrorException;
import com.google.api.server.spi.response.UnauthorizedException;
import com.google.appengine.api.users.User;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.mail.MessagingException;

/**
 * Created by Alan on 11/22/2015.
 */
public class UserApi extends CARdiacApiBase {

    @ApiMethod(httpMethod = "post")
    public void registerUser(User user) throws UnauthorizedException, ConflictException, InternalServerErrorException {
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
                try {
                    MailService.sendConfirmation(user.getEmail());
                } catch (MessagingException e1) {
                    StringWriter msg = new StringWriter();
                    PrintWriter writer = new PrintWriter(msg);
                    writer.println("Failed to mail confirmation to "+user.getEmail());
                    writer.println(e1.getMessage());
                    e1.printStackTrace(writer);
                    throw new InternalServerErrorException(msg.toString(), e1);
                }
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

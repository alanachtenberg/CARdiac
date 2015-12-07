package com.appspot.cardiac_404.ECG;

import com.appspot.cardiac_404.CARdiacApiBase;
import com.appspot.cardiac_404.MailService;
import com.appspot.cardiac_404.User.CardiacUser;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.response.NotFoundException;
import com.google.api.server.spi.response.UnauthorizedException;
import com.google.appengine.api.users.User;
import com.googlecode.objectify.cmd.Query;

import java.util.ArrayList;

import javax.mail.MessagingException;

import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * Created by Alan on 9/25/2015.
 */

public class EcgApi extends CARdiacApiBase {

    @ApiMethod(httpMethod = "post")
    public void insertECG(User user, ECGBean data) throws UnauthorizedException {
        CardiacUser cardiacUser = loadUser(user);
        cardiacUser.getEcgData().add(data);//insert ecg data
        try {
            MailService.sendMail(user.getEmail(),"Alert!!!\n\n" + data.toString(),"CARdiac ALERT!");
        }
        catch (MessagingException e){
            e.printStackTrace();
        }
        saveCardiacUser(cardiacUser); //save user to db
    }

    @ApiMethod(httpMethod = "get")
    public ArrayList<ECGBean> listECG(User user) throws UnauthorizedException {
        return loadUser(user).getEcgData();
    }

    @ApiMethod(name = "listAllECG", httpMethod = "get")
    public ArrayList<ArrayList<ECGBean>> listAllECG(User user) throws UnauthorizedException {
        CardiacUser cardiacUser = loadUser(user);
        if (!cardiacUser.isMonitor()){
            throw new UnauthorizedException("User does not have monitor permissions");
        }
        ArrayList<ArrayList<ECGBean>> list = new ArrayList<ArrayList<ECGBean>>();
        Query<CardiacUser> cUsers = ofy().load().type(CardiacUser.class);
        for (CardiacUser cUser : cUsers){
            list.add(cUser.getEcgData());
        }
        return list;
    }

    @ApiMethod(httpMethod = "delete")
    public void deleteECG(User user, ECGBean ecgBean) throws UnauthorizedException, NotFoundException {
        CardiacUser cardiacUser = loadUser(user);
        if (!cardiacUser.getEcgData().contains(ecgBean)){
            throw new NotFoundException("ECG data does not exist");
        }
        cardiacUser.getEcgData().remove(ecgBean);
        saveCardiacUser(cardiacUser);//update user in db
    }



}

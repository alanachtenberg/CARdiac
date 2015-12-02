package com.appspot.cardiac_404.Vehicle;

import com.appspot.cardiac_404.CARdiacApiBase;
import com.appspot.cardiac_404.TimeLocBean;
import com.appspot.cardiac_404.User.CardiacUser;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.response.NotFoundException;
import com.google.api.server.spi.response.UnauthorizedException;
import com.google.appengine.api.users.User;
import com.googlecode.objectify.cmd.Query;
import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.ArrayList;

/**
 * Created by Alan on 10/30/2015.
 */
public class VehicleApi extends CARdiacApiBase {

    @ApiMethod(httpMethod = "get")
    public TimeLocBean test(User user) throws UnauthorizedException {
        if (user == null || user.getEmail()==null)
            throw new UnauthorizedException("null or unauthorized user");
        return new TimeLocBean();
    }


    @ApiMethod(httpMethod = "post")
    public void insertVehicle(User user, VehicleBean data) throws UnauthorizedException {
        CardiacUser cardiacUser = loadUser(user);
        cardiacUser.getVehicleData().add(data);//insert vehicle data
        saveCardiacUser(cardiacUser); //save user to db
    }

    @ApiMethod(httpMethod = "get")
    public ArrayList<VehicleBean> listVehicle(User user) throws UnauthorizedException {
        return loadUser(user).getVehicleData();
    }

    @ApiMethod(name = "listAllVehicle", httpMethod = "get")
    public ArrayList<ArrayList<VehicleBean>> listAllVehicle(User user) throws UnauthorizedException {
        CardiacUser cardiacUser = loadUser(user);
        if (!cardiacUser.isMonitor()){
            throw new UnauthorizedException("User does not have monitor permissions");
        }
        ArrayList<ArrayList<VehicleBean>> list = new ArrayList<ArrayList<VehicleBean>>();
        Query<CardiacUser> cUsers = ofy().load().type(CardiacUser.class);
        for (CardiacUser cUser : cUsers){
            list.add(cUser.getVehicleData());
        }
        return list;
    }

    @ApiMethod(httpMethod = "delete")
    public void deleteVehicle(User user, VehicleBean vehicleBean) throws UnauthorizedException, NotFoundException {
        CardiacUser cardiacUser = loadUser(user);
        if (!cardiacUser.getVehicleData().contains(vehicleBean)){
            throw new NotFoundException("Vehicle data does not exist");
        }
        cardiacUser.getVehicleData().remove(vehicleBean);
        saveCardiacUser(cardiacUser);//update user in db
    }

}

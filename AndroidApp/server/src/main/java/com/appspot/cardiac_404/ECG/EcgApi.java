package com.appspot.cardiac_404.ECG;

import com.appspot.cardiac_404.CARdiacApiBase;
import com.appspot.cardiac_404.TimeBean;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.Named;
import com.google.api.server.spi.response.NotFoundException;
import com.google.api.server.spi.response.UnauthorizedException;
import com.google.appengine.api.users.User;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * Created by Alan on 9/25/2015.
 */

public class EcgApi extends CARdiacApiBase {
    private static Set<User> monitors = new HashSet<User>();
    private static ArrayList<ECGBean> ecgDataList = new ArrayList<ECGBean>();

    static {
        //test data
        ECGBean bean = new ECGBean((long) 0, new TimeBean(), 123, true, false, false);
        ecgDataList.add(bean);
    }

    @ApiMethod(httpMethod = "post")
    public void insertECG(User user, ECGBean data) throws UnauthorizedException {
        if (user==null || user.getEmail()==null){
            throw new UnauthorizedException("user or email is null");
        }

        ecgDataList.add(data);
    }

    @ApiMethod(httpMethod = "get")
    public ArrayList<ECGBean> listECG(User user) throws UnauthorizedException {
        if (user==null || user.getEmail()==null){
            throw new UnauthorizedException("user or email is null");
        }

        //TODO aggregate ECGBeans


        return ecgDataList;
    }

    @ApiMethod(httpMethod = "delete")
    public ArrayList<ECGBean> deleteECG(User user, @Named("id") Long id) throws UnauthorizedException, NotFoundException {
        if (user==null || user.getEmail()==null){
            throw new UnauthorizedException("user or email is null");
        }
        ECGBean record = findRecord(id);
        if(record == null) {
            throw new NotFoundException("Record does not exist");
        }
        ofy().delete().entity(record).now();
        return ecgDataList;
    }

    private ECGBean findRecord(Long id) {
        return ofy().load().type(ECGBean.class).id(id).now();
    }





}

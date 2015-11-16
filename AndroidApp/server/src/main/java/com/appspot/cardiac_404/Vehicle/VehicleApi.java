package com.appspot.cardiac_404.Vehicle;

import com.appspot.cardiac_404.CARdiacApiBase;
import com.appspot.cardiac_404.TimeBean;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.response.UnauthorizedException;
import com.google.appengine.api.users.User;

/**
 * Created by Alan on 10/30/2015.
 */
public class VehicleApi extends CARdiacApiBase {

    @ApiMethod(httpMethod = "get")
    public TimeBean test(User user) throws UnauthorizedException {
        if (user == null || user.getEmail()==null)
            throw new UnauthorizedException("null or unauthorized user");
        return new TimeBean();
    }

}

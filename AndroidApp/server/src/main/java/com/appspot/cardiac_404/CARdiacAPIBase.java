package com.appspot.cardiac_404;

import com.appspot.cardiac_404.User.CardiacUser;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.UnauthorizedException;
import com.google.appengine.api.users.User;
import com.googlecode.objectify.ObjectifyService;

import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * Created by Alan on 10/30/2015.
 */
@Api(name = Constants.API_NAME,
        version = Constants.VERSION,
        namespace = @ApiNamespace(ownerDomain = Constants.DOMAIN, ownerName = Constants.DOMAIN,
                packagePath = ""),
        clientIds = {Constants.WEB_CLIENT_ID, Constants.API_EXPLORER_CLIENT_ID, Constants.ANDROID_CLIENT_ID, Constants.ANDROID_CLIENT_ID},
        audiences = {Constants.ANDROID_AUDIENCE}
)
public abstract class CARdiacApiBase {

    public CARdiacApiBase() {
        ObjectifyService.register(CardiacUser.class);
    }

    protected CardiacUser loadUser(User user) throws UnauthorizedException {
        if (user == null || user.getEmail() == null) {
            throw new UnauthorizedException("user or email is null", new NullPointerException());
        }
        CardiacUser cardiacUser = ofy().load().type(CardiacUser.class).id(user.getEmail()).now();
        ;//retrieve user from db
        if (cardiacUser == null) {
            throw new UnauthorizedException("user is not registered");
        }
        return cardiacUser;
    }

    protected void deleteUser(User user) {
        ofy().delete().type(CardiacUser.class).id(user.getUserId()).now();
    }

    protected void saveCardiacUser(CardiacUser user) {
        ofy().save().entity(user).now();
    }

}
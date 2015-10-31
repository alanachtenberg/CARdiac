package com.appspot.cardiac_404;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiNamespace;

/**
 * Created by Alan on 10/30/2015.
 */
@Api(name = Constants.API_NAME,
        version = Constants.VERSION,
        namespace = @ApiNamespace(ownerDomain = Constants.DOMAIN, ownerName = Constants.DOMAIN,
                packagePath = ""),
        clientIds = {Constants.WEB_CLIENT_ID, Constants.API_EXPLORER_CLIENT_ID, Constants.ANDROID_CLIENT_ID})
public class CARdiacApiBase {

        //TODO common functions such as sign in
}

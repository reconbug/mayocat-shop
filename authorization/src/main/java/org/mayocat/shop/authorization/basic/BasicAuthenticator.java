package org.mayocat.shop.authorization.basic;

import java.io.UnsupportedEncodingException;

import javax.inject.Inject;
import javax.inject.Provider;

import org.eclipse.jetty.util.B64Code;
import org.eclipse.jetty.util.StringUtil;
import org.mayocat.shop.authorization.Authenticator;
import org.mayocat.shop.authorization.PasswordManager;
import org.mayocat.shop.model.Tenant;
import org.mayocat.shop.model.User;
import org.mayocat.shop.store.StoreException;
import org.mayocat.shop.store.UserStore;
import org.slf4j.Logger;
import org.xwiki.component.annotation.Component;

import com.google.common.base.Optional;
import com.google.common.net.HttpHeaders;

@Component("basic")
public class BasicAuthenticator implements Authenticator
{
    @Inject
    private Provider<UserStore> userStore;

    @Inject
    private PasswordManager passwordManager;

    @Inject
    private Logger logger;

    private final static String METHOD = "Basic";

    @Override
    public boolean respondTo(String headerName, String headerValue)
    {
        if (headerName.equalsIgnoreCase(HttpHeaders.AUTHORIZATION)) {
            final int space = headerValue.indexOf(' ');
            if (space > 0) {
                final String method = headerValue.substring(0, space);
                if (method.equalsIgnoreCase(METHOD)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public Optional<User> verify(String value, Tenant tenant)
    {
        final int space = value.indexOf(' ');
        if (space > 0) {
            try {
                final String decoded = B64Code.decode(value.substring(space + 1), StringUtil.__ISO_8859_1);
                final int i = decoded.indexOf(':');
                if (i > 0) {
                    final String username = decoded.substring(0, i);
                    final String password = decoded.substring(i + 1);
                    try {
                        // FIXME
                        User user = userStore.get().findByEmailOrUserNameAndTenant(username, tenant);
                        if (user != null) {
                            if (this.passwordManager.verifyPassword(password, user.getPassword())) {
                                return Optional.of(user);
                            }
                        }
                    } catch (StoreException e) {
                        this.logger.error("Failed to retriev user against store", e);
                    }
                }
            } catch (UnsupportedEncodingException e) {
                this.logger.debug("Failed to decode basic auth credentials");
            }
        }
        return Optional.absent();
    }

}

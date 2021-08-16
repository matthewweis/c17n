package io.ignice.c17n;

import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Service;

@Service
public class AppService {

    //
    // Services can wire repo, or may choose to hardcode queries with fluent api
    // @see AppRepository for Entity-mapped automatic generation
    //
    // todo these should be stored procedures
    //

    public final R2dbcEntityTemplate template;

    public AppService(R2dbcEntityTemplate template) {
        this.template = template;
    }

}

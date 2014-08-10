package org.technologyhatchery.edgar;

import javax.persistence.Basic;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by Alfred on 8/8/2014.
 */
public class Context {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long uniqueId;
    @Basic
    String id;

    public Context() {

    }


}

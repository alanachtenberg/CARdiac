package com.appspot.cardiac_404.ECG;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

/**
 * Created by Alan on 11/16/2015.
 */
@Entity
public class ECGBeanTable {
    @Id
    Long id;
    String userEmail;
    Key[] ECGData;
}

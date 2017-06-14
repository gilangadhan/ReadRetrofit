/*
 * Copyright (c) 2017. Gilang Ramadhan (gilangramadhan96.gr@gmail.com)
 */

package id.codinate.readretrofit.apihelper;

/**
 * Created by Gilang Ramadhan on 14/06/2017.
 */

public class UploadObject {
    private String success;
    public UploadObject(String success) {
        this.success = success;
    }
    public String getSuccess() {
        return success;
    }
}

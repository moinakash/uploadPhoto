package com.project.update;

public class APIUtils {


    private  APIUtils(){


    }

    public static final String API_URL = "http://172.16.153.6:8000/api/v1/user/profile/";

    public static FileService getFileServices(){

        return RetrofitClient.getClient(API_URL).create(FileService.class);
    }

}

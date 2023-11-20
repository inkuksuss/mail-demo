package com.example.maildemo.service;

public interface CustomPublishService<T, R> {

    R doPublish(T t);
}

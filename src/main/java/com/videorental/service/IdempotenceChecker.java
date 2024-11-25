package com.videorental.service;

import com.videorental.controller.exceptions.IdempotenceException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author andrey.semenov
 */
public class IdempotenceChecker {

    private Set<Integer> set = new HashSet<>();
    private List<Integer> order = new ArrayList<>();
    private int pointer = 0;
    private int size = 0;

    public IdempotenceChecker(int size) {

        for (int i = 0; i < size; i++) {
            order.add(null);
        }
        this.size = size;
    }

    public void check(int value) {

        if (!set.contains(value)) {
            set.add(value);
            set.remove(order.get(pointer));
            order.set(pointer, value);

            if (pointer == size) {
                pointer = 0;
            } else {
                pointer++;
            }
        } else {
            throw new IdempotenceException("Duplicate request received");
        }
    }

}
